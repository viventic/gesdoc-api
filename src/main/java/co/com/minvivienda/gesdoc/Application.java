/*
 * Copyright 2016 Red Hat, Inc.
 * <p>
 * Red Hat licenses this file to you under the Apache License, version
 * 2.0 (the "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.  See the License for the specific language governing
 * permissions and limitations under the License.
 *
 */
package co.com.minvivienda.gesdoc;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.springframework.stereotype.Component;

@SpringBootApplication
// load regular Spring XML file from the classpath that contains the Camel XML DSL
@ImportResource({"classpath:spring/camel-context.xml"})
public class Application {

	
	public static final String OPERATION_NAMESPACE = "http://services.ws.sgd.xuecolombia.com/";
	
	public static final String GESDOC_ENDPOINT = "https://pruebas-gesdoc.minvivienda.gov.co";
	
    public static final String CXF_ENDPOINT = "cxf://" + GESDOC_ENDPOINT + "/SGD_WS/GESDOC"
    		+ "?wsdlURL=" + GESDOC_ENDPOINT + "/SGD_WS/GESDOC?wsdl"
    		+ "&serviceName={" + OPERATION_NAMESPACE + "}GESDOC"
    		+ "&dataFormat=MESSAGE"
    		+ "&endpointName={" + OPERATION_NAMESPACE + "}GESDOCPort";

    
    
    /**
     * A main method to start this application.
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
    
    
    
    @Component
    class RestApi extends RouteBuilder {

        @Override
        public void configure() {
            restConfiguration()
            .contextPath("/api")
            	.apiContextPath("/swagger")
                .apiProperty("api.title", "Camel Gesdoc REST API")
                .apiProperty("api.version", "1.0")
                .apiProperty("cors", "false")
                .apiProperty("api.specification.contentType.json", "application/vnd.oai.openapi+json;version=2.0")
                .apiProperty("api.specification.contentType.yaml", "application/vnd.oai.openapi;version=2.0")
                .apiContextRouteId("doc-api")
            .component("servlet")
            .bindingMode(RestBindingMode.off);
            
            rest("/gesdoc")
            	.post("/createReceived")
            	.consumes("multipart/form-data")
            	.produces("application/json")
        		.route().routeId("gesdoc-createReceived")
        		//.log(">>> createReceived ${body}")
            	.description("Permite radicar, obtener el número y fecha de radicado de una comunicación externa recibida (ER)")
            	//.streamCaching("true")
            	//.marshal().json()
                .to("direct:transform")
            .endRest();
            
            

            
            // Transform and send to SOAP service
            from("direct:transform")
                .routeId("restToSoapRoute")
                //.log("Received REST request: ${body}")
                .toD("velocity:velocity/soap-createReceived-body.vm")
                .setHeader(CxfConstants.OPERATION_NAME, constant("createReceived"))
                .setHeader(CxfConstants.OPERATION_NAMESPACE, constant(OPERATION_NAMESPACE))
                .marshal().json(JsonLibrary.Jackson)
                .to(CXF_ENDPOINT)
                .process(exchange -> {
                	String body = exchange.getIn().getBody(String.class);
                	//System.out.println(exchange.getIn().getBody(String.class));
                	//HttpInputStream his = exchange.getIn().getBody();
                	Map<String, String> response = new HashMap<String, String>();
                	String[] parts = body.split("--");
                	int parte = 1;
                	if(parts != null && parts.length > 0) {
                		System.out.println("NUMERO DE PARTES: " + parts.length);
                		for(String part : parts) {
                            if (part.trim().isEmpty() || part.trim().equals("--")) {
                                continue;
                            }
                            
                            System.out.println("*************** PART **********************");
                            System.out.println("part: " + part.substring(1, 10));
                            
                            // Extract headers and content of each part
                            String[] headersAndContent = part.split("\r\n\r\n", 2);
                            
                            if(headersAndContent == null || headersAndContent.length < 2) {
                            	continue;
                            }
                            
                            String headers = headersAndContent[0];
                            String content = headersAndContent[1];
                            
                            System.out.println("******* HEADERS *******");
                            System.out.println("headers: " + headers);
                            
                            System.out.println("******* CONTENT *******");
                            System.out.println("content: " + content.length());
                            
                            String filename = extractFilename(headers);
                            System.out.println("****** FILE NAME ********");
                            System.out.println("filename: " + filename);
                            
                            if(parte == 1) {
                            	response.put("json_result", content);
                            }
                            
                            if(parte == 2) {
                            	response.put("filename", filename);
                            	response.put("etiqueta", new String(Base64.encodeBase64(content.getBytes())));
                            }
                            
                            parte++;
                		}
                	}
                	
                	exchange.getOut().setBody(response);
                })
                
                .setHeader("Content-Type", constant("application/json"))
                .to("mock:end");
        }
    }
    
    
    private String extractFilename(String headers) {
        String[] headerLines = headers.split("\r\n");
        for (String line : headerLines) {
            if (line.startsWith("Content-Disposition")) {
                String[] dispositionParts = line.split(";");
                for (String part : dispositionParts) {
                    if (part.startsWith("name=")) {
                        return part.substring("name=".length()).replace("\"", "");
                    }
                }
            }
        }
        return null;
    }

    private void saveAttachment(String filename, String content) throws IOException {
        // Example: Save attachment content to a file
        String filePath = "attachments/" + filename; // Adjust path as needed
        FileUtils.writeByteArrayToFile(new File(filePath), content.getBytes());
        System.out.println("Saved attachment: " + filename);
    }

}