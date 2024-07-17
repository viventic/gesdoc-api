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

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.springframework.stereotype.Component;

import co.com.minvivienda.middleware.exception.ServiceException;

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
            
            onException(ServiceException.class)
            .handled(true)
            .onExceptionOccurred(exchange -> {
            	System.out.println(exchange.getException().toString());
            	ServiceException ex = exchange.getException(ServiceException.class);
            	exchange.getOut().setBody(simple("{\"code\":\"" + ex.getCodigoError() + "\",\"errorMessage\":\"" + ex.getMessage() + "\"}"));
            })
            .to("direct:errorHandler");
            
            // Define error handler route
            from("direct:errorHandler")
                .log("Handling error: ${body}");
            
            rest("/gesdoc")
            	.post("/createReceived")
            	.consumes("multipart/form-data")
            	.produces("application/json")
        		.route().routeId("gesdoc-createReceived")
        		.description("Permite radicar, obtener el número y fecha de radicado de una comunicación externa recibida (ER)")
                .to("direct:transform")
            .endRest();

            
            // Transform and send to SOAP service
            from("direct:transform")
                .routeId("restToSoapRoute")
                .process(exchange -> {
                	// PROCESAR ENTRADA QUE LLEGA EN MULTIPART FORMDATA
                	
                	// TOMAR ESTE JSON DESDE EL ARCHIVO ADJUNTO QUE SE ENVIA COMO MULTIPART FORMDATA
                	String responseJson = "{\n"
                			+ "   \"author\":{\n"
                			+ "      \"identification\":\"123\"\n"
                			+ "   },\n"
                			+ "   \"authorDependence\":{\n"
                			+ "      \"code\":\"71301\"\n"
                			+ "   },\n"
                			+ "   \"type\":{\n"
                			+ "      \"id\":\"2563\"\n"
                			+ "   },\n"
                			+ "   \"reference\":\"Prueba Cancelación de Gravamenes\",\n"
                			+ "   \"observations\":\"Correo electrónico - checklist\",\n"
                			+ "   \"sourceThirdPerson\":{\n"
                			+ "      \"identificationType\":{\n"
                			+ "         \"name\":\"CC\"\n"
                			+ "      },\n"
                			+ "      \"identification\":\"13511400\",\n"
                			+ "      \"name\":\"Carlos\",\n"
                			+ "      \"lastname\":\"Chaparro\",\n"
                			+ "      \"email\":\"carlos.chaparro@xuecolombia.com\",\n"
                			+ "      \"address\":\"Cra 20 #45a 33\",\n"
                			+ "      \"phone\":\"6013401862\",\n"
                			+ "      \"municipality\":{\n"
                			+ "         \"code\":\"11001\"\n"
                			+ "      }\n"
                			+ "   },\n"
                			+ "   \"targetDependence\":{\n"
                			+ "      \"code\":\"71301\"\n"
                			+ "   },\n"
                			+ "   \"targetUser\":{\n"
                			+ "      \"identification\":\"46382722\"\n"
                			+ "   }\n"
                			+ "}";
                	
                	
                	//ITERAR ANEXOS QUE LLEGAN COMO ARCHIVOS ADJUNTOS EN LA PETICION Y CONVERTIRLOS EN BASE64
                	//GENERAR <annexes> PARA REEMPLAZAR EN LA PLANTILLA soap-createReceived-body.vm
                	
                	exchange.getOut().setBody(responseJson);
                	
                })
                .setHeader("author.identification", jsonpath("$.author.identification"))
                .setHeader("anexos", jsonpath("$.anexos"))
                .toD("velocity:velocity/soap-createReceived-body.vm")
                .setHeader(CxfConstants.OPERATION_NAME, constant("createReceived"))
                .setHeader(CxfConstants.OPERATION_NAMESPACE, constant(OPERATION_NAMESPACE))
                .to(CXF_ENDPOINT)
                .process(exchange -> {
                	String body = exchange.getIn().getBody(String.class);
                	String responseJson = "{\"filename\": \"$FILE_NAME$\", \"json_result\": $JSON_RESULT$, \"etiqueta\": \"$ETIQUETA$\"}";
                	
                	String[] parts = body.split("--");
                	int parte = 1;
                	if(parts != null && parts.length > 0) {
                		System.out.println("NUMERO DE PARTES: " + parts.length);
                		for(String part : parts) {
                            if (part.trim().isEmpty() || part.trim().equals("--")) {
                                continue;
                            }
                            
                            // Extract headers and content of each part
                            String[] headersAndContent = part.split("\r\n\r\n", 2);
                            
                            if(headersAndContent == null || headersAndContent.length < 2) {
                            	continue;
                            }
                            
                            String headers = headersAndContent[0];
                            String content = headersAndContent[1];
                            String filename = extractFilename(headers);
                            
                            if(parte == 1) {
                            	
                            	if(content != null) {
	                                try {
	                                    JSONObject jsonObject = XML.toJSONObject(content);
	                                    jsonObject = jsonObject.getJSONObject("soap:Envelope");
	                                    jsonObject = jsonObject.getJSONObject("soap:Body");
	                                    jsonObject = jsonObject.getJSONObject("ns2:createReceivedResponse");
	                                    jsonObject = jsonObject.getJSONObject("creation");
	                                    jsonObject.remove("file");
	                                    
	                                    String jsonString = jsonObject.toString(4); // Indent with 4 spaces for readability
	                                    System.out.println(jsonString);
	                                    responseJson = responseJson.replace("$JSON_RESULT$", jsonString);
	                                } catch (Exception e) {
	                                	responseJson = responseJson.replace("$JSON_RESULT$", "{}");
	                                    e.printStackTrace();
	                                }
                            	}else {
                            		responseJson = responseJson.replace("$JSON_RESULT$", "{}");
                            	}
                            }
                            
                            if(parte == 2) {
                            	responseJson = responseJson.replace("$FILE_NAME$", filename);
                            	
                            	if(content != null) {
                            		responseJson = responseJson.replace("$ETIQUETA$", new String(Base64.encodeBase64(content.getBytes())));
                            	}else {
                            		responseJson = responseJson.replace("$ETIQUETA$", "");
                            	}
                            }
                            
                            parte++;
                		}
                	}
                	
                	exchange.getOut().setBody(responseJson);
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