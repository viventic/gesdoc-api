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

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.apache.camel.component.file.GenericFile;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.springframework.stereotype.Component;

@SpringBootApplication
// load regular Spring XML file from the classpath that contains the Camel XML DSL
@ImportResource({"classpath:spring/camel-context.xml"})
public class Application {

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
            	.apiContextPath("/api-doc")
                .apiProperty("api.title", "Camel Gesdoc REST API")
                .apiProperty("api.version", "1.0")
                .apiProperty("cors", "false")
                .apiProperty("api.specification.contentType.json", "application/vnd.oai.openapi+json;version=2.0")
                .apiProperty("api.specification.contentType.yaml", "application/vnd.oai.openapi;version=2.0")
                .apiContextRouteId("doc-api")
            .component("servlet")
            .bindingMode(RestBindingMode.json);
            
            rest("/gesdoc")
            	.post("/createReceived")
            	.consumes("multipart/form-data")
            	//.produces("multipart/mixed")
        		.route().routeId("gesdoc-createReceived")
        		.log(">>> createReceived ${body}")
            	.description("Permite radicar, obtener el número y fecha de radicado de una comunicación externa recibida (ER)")
            	//.streamCaching("true")
            	
                .to("direct:transform")
            .endRest();
            
            
            String cxfEndpoint = "cxf://https://pruebas-gesdoc.minvivienda.gov.co:443/SGD_WS/GESDOC"
            		+ "?wsdlURL=https://pruebas-gesdoc.minvivienda.gov.co/SGD_WS/GESDOC?wsdl"
            		+ "&serviceName={http://services.ws.sgd.xuecolombia.com/}GESDOC"
            		+ "&dataFormat=PAYLOAD"
            		+ "&endpointName={http://services.ws.sgd.xuecolombia.com/}GESDOCPort";
            
            // Transform and send to SOAP service
            from("direct:transform")
                .routeId("restToSoapRoute")
                .log("Received REST request: ${body}")
                .process(exchange -> {
                    /*GenericFile<?> file = exchange.getIn().getBody(GenericFile.class);
                    
                    if (file != null) {
                        String fileName = file.getFileName();
                        byte[] fileContent = exchange.getIn().getBody(byte[].class);
                        
                        // Process the file (e.g., save it, log it, etc.)
                        System.out.println("Received file: " + fileName);
                        System.out.println("File content: " + new String(fileContent));
                        exchange.getIn().setBody(fileContent);
                    } else {
                        // Handle the case where no file is found in the request
                        System.out.println("No file found in the request.");
                    }*/
                    
                	/*String requestBody = exchange.getIn().getBody(String.class);
                    
                    // Create the SOAP client using the WSDL from the local path
                    JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
                    factory.setServiceClass(MySoapService.class);
                    factory.setWsdlURL("classpath://https://pruebas-gesdoc.minvivienda.gov.co/SGD_WS/GESDOC?wsdl");
                    factory.setAddress("https://pruebas-gesdoc.minvivienda.gov.co/SGD_WS/GESDOC");
                    
                    MySoapService soapService = (MySoapService) factory.create();

                    // Call the SOAP service and get the response
                    String soapResponse = soapService.createReceived(requestBody);
                    
                    // Set the response in the exchange
                    exchange.getMessage().setBody(soapResponse);*/
                })
                .toD("velocity:velocity/post-createReceived-body.vm")
                .setHeader(CxfConstants.OPERATION_NAME, constant("createReceived"))
                //.setHeader(CxfConstants.SERVICE_CLASS, constant("createReceived"))
                .setHeader(CxfConstants.OPERATION_NAMESPACE, constant("http://services.ws.sgd.xuecolombia.com/"))
                .log("*** NEW BODY: ${body}")
                .to(cxfEndpoint);
        }
    }

}