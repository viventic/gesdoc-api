package co.com.minvivienda.gesdoc;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.BodyPart;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import javax.mail.util.SharedByteArrayInputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;


@RestController
//@RequestMapping("/gesdoc")
@CrossOrigin(origins = "*")
public class GesdocController {

	/*
    @PostMapping(value = "/mock/createReceived", produces = {"multipart/mixed"}, consumes = {"multipart/form-data"})
    public ResponseEntity<?> createReceived(@RequestParam Map<String, MultipartFile> files)  throws Exception {
    	
    	try {
	    	String fileName = "etiqueta.png";
	        byte[] etiquetaBytes = readImageFromClasspath(fileName);
	        System.out.println("etiquetaBytes = " + etiquetaBytes.length);
	    	if (etiquetaBytes != null && etiquetaBytes.length <= 0) {
	        	System.out.println("Archivo no existe");
	            return ResponseEntity.notFound().build();
	        }
	        
	        ByteArrayResource resource = new ByteArrayResource(etiquetaBytes) {
	            @Override
	            public String getFilename() {
	                return fileName;
	            }
	        };
	        
	        HttpHeaders jsonResponseHeaders = new HttpHeaders();
	        jsonResponseHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);       
	        jsonResponseHeaders.add(HttpHeaders.TRANSFER_ENCODING, "binary");
	        jsonResponseHeaders.set("Content-ID", "<root>");
	        
	        files = null;
	        String jsonResponse = "{"
	        		+ "    \"idelement\": {"
	        		+ "        \"idtype\": 2,"
	        		+ "        \"id\": \"2022ER0008851\""
	        		+ "    },"
	        		+ "    \"message\": \"Los cambios se realizaron\","
	        		+ "    \"actionDate\": \"20240611063034-0500\""
	        		+ "}";
	        
	        HttpEntity<String> jsonEntity = new HttpEntity<String>(jsonResponse, jsonResponseHeaders);
	        HttpHeaders jsonFileHeaders = new HttpHeaders();
	        jsonFileHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
	        jsonFileHeaders.add(HttpHeaders.TRANSFER_ENCODING, "binary");
	        jsonFileHeaders.set("Content-ID", "<etiqueta.png>");
	        HttpEntity<ByteArrayResource> fileEntity = new HttpEntity<ByteArrayResource>(resource, jsonFileHeaders);
	        
	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.MULTIPART_MIXED);
	        
	        MultiValueMap<String, Object> responseBody = new LinkedMultiValueMap<>();
	        responseBody.clear();
	        responseBody.set("root", jsonEntity);
	        responseBody.set("etiqueta.png", fileEntity);
	        
	        return new ResponseEntity<MultiValueMap<String, Object>>(responseBody, headers, HttpStatus.OK);
    	}catch(Exception ex) {
    		ex.printStackTrace();
    		return new ResponseEntity<String>("EXCEPCION ", HttpStatus.INTERNAL_SERVER_ERROR);
    	}
    }
    
    
    @PostMapping(value = "/mock/createInner", produces = {"multipart/mixed"}, consumes = {"multipart/form-data"})
    public ResponseEntity<?> createInner(@RequestParam Map<String, MultipartFile> files)  throws Exception {
    	
    	try {
	    	String fileName = "etiqueta.png";
	        byte[] etiquetaBytes = readImageFromClasspath(fileName);
	        System.out.println("etiquetaBytes = " + etiquetaBytes.length);
	    	if (etiquetaBytes != null && etiquetaBytes.length <= 0) {
	        	System.out.println("Archivo no existe");
	            return ResponseEntity.notFound().build();
	        }
	        
	        ByteArrayResource resource = new ByteArrayResource(etiquetaBytes) {
	            @Override
	            public String getFilename() {
	                return fileName;
	            }
	        };
	        
	        HttpHeaders jsonResponseHeaders = new HttpHeaders();
	        jsonResponseHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);       
	        jsonResponseHeaders.add(HttpHeaders.TRANSFER_ENCODING, "binary");
	        jsonResponseHeaders.set("Content-ID", "<root>");
	        
	        files = null;
	        String jsonResponse = "{"
	        		+ "    \"idelement\": {"
	        		+ "        \"idtype\": 2,"
	        		+ "        \"id\": \"2022ER0008851\""
	        		+ "    },"
	        		+ "    \"message\": \"Los cambios se realizaron\","
	        		+ "    \"actionDate\": \"20240611063034-0500\""
	        		+ "}";
	        
	        
	        HttpEntity<String> jsonEntity = new HttpEntity<String>(jsonResponse, jsonResponseHeaders);
	        HttpHeaders jsonFileHeaders = new HttpHeaders();
	        jsonFileHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
	        jsonFileHeaders.add(HttpHeaders.TRANSFER_ENCODING, "binary");
	        jsonFileHeaders.set("Content-ID", "<etiqueta.png>");
	        HttpEntity<ByteArrayResource> fileEntity = new HttpEntity<ByteArrayResource>(resource, jsonFileHeaders);
	        
	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.MULTIPART_MIXED);
	        
	        MultiValueMap<String, Object> responseBody = new LinkedMultiValueMap<>();
	        responseBody.clear();
	        responseBody.set("root", jsonEntity);
	        responseBody.set("etiqueta.png", fileEntity);
	        
	        return new ResponseEntity<MultiValueMap<String, Object>>(responseBody, headers, HttpStatus.OK);
    	}catch(Exception ex) {
    		ex.printStackTrace();
    		return new ResponseEntity<String>("EXCEPCION ", HttpStatus.INTERNAL_SERVER_ERROR);
    	}
    }
    
    
    
    @PostMapping(value = "/mock/createSended", produces = {"multipart/mixed"}, consumes = {"multipart/form-data"})
    public ResponseEntity<?> createSended(@RequestParam Map<String, MultipartFile> files)  throws Exception {
    	
    	try {
	    	String fileName = "etiqueta.png";
	        byte[] etiquetaBytes = readImageFromClasspath(fileName);
	        System.out.println("etiquetaBytes = " + etiquetaBytes.length);
	    	if (etiquetaBytes != null && etiquetaBytes.length <= 0) {
	        	System.out.println("Archivo no existe");
	            return ResponseEntity.notFound().build();
	        }
	        
	        ByteArrayResource resource = new ByteArrayResource(etiquetaBytes) {
	            @Override
	            public String getFilename() {
	                return fileName;
	            }
	        };
	        
	        HttpHeaders jsonResponseHeaders = new HttpHeaders();
	        jsonResponseHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);       
	        jsonResponseHeaders.add(HttpHeaders.TRANSFER_ENCODING, "binary");
	        jsonResponseHeaders.set("Content-ID", "<root>");
	        
	        files = null;
	        String jsonResponse = "{"
	        		+ "    \"idelement\": {"
	        		+ "        \"idtype\": 2,"
	        		+ "        \"id\": \"2022ER0008851\""
	        		+ "    },"
	        		+ "    \"message\": \"Los cambios se realizaron\","
	        		+ "    \"actionDate\": \"20240611063034-0500\""
	        		+ "}";
	        
	        HttpEntity<String> jsonEntity = new HttpEntity<String>(jsonResponse, jsonResponseHeaders);
	        HttpHeaders jsonFileHeaders = new HttpHeaders();
	        jsonFileHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
	        jsonFileHeaders.add(HttpHeaders.TRANSFER_ENCODING, "binary");
	        jsonFileHeaders.set("Content-ID", "<etiqueta.png>");
	        HttpEntity<ByteArrayResource> fileEntity = new HttpEntity<ByteArrayResource>(resource, jsonFileHeaders);
	        
	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.MULTIPART_MIXED);
	        
	        MultiValueMap<String, Object> responseBody = new LinkedMultiValueMap<>();
	        responseBody.clear();
	        responseBody.set("root", jsonEntity);
	        responseBody.set("etiqueta.png", fileEntity);
	        
	        return new ResponseEntity<MultiValueMap<String, Object>>(responseBody, headers, HttpStatus.OK);
    	}catch(Exception ex) {
    		ex.printStackTrace();
    		return new ResponseEntity<String>("EXCEPCION ", HttpStatus.INTERNAL_SERVER_ERROR);
    	}
    }
    
    
    @PostMapping(value = "/mock/createRegistered", produces = {"multipart/mixed"}, consumes = {"multipart/form-data"})
    public ResponseEntity<?> createRegistered(@RequestParam Map<String, MultipartFile> files)  throws Exception {
    	
    	try {	        
	        HttpHeaders jsonResponseHeaders = new HttpHeaders();
	        jsonResponseHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);       
	        jsonResponseHeaders.add(HttpHeaders.TRANSFER_ENCODING, "binary");
	        jsonResponseHeaders.set("Content-ID", "<root>");
	        
	        files = null;
	        String jsonResponse = "{"
	        		+ "    \"idelement\": {"
	        		+ "        \"idtype\": 2,"
	        		+ "        \"id\": \"2022ER0008851\""
	        		+ "    },"
	        		+ "    \"message\": \"Los cambios se realizaron\","
	        		+ "    \"actionDate\": \"20240611063034-0500\""
	        		+ "}";
	        
	        HttpEntity<String> jsonEntity = new HttpEntity<String>(jsonResponse, jsonResponseHeaders);
	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.MULTIPART_MIXED);
	        MultiValueMap<String, Object> responseBody = new LinkedMultiValueMap<>();
	        responseBody.set("root", jsonEntity);
	        
	        return new ResponseEntity<MultiValueMap<String, Object>>(responseBody, headers, HttpStatus.OK);
    	}catch(Exception ex) {
    		ex.printStackTrace();
    		return new ResponseEntity<String>("EXCEPCION ", HttpStatus.INTERNAL_SERVER_ERROR);
    	}
    }
    
  
 */   
    @PostMapping(value = "/createReceived", produces = {"application/json"}, consumes = {"multipart/form-data"})
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> createReceivedReal(@RequestParam Map<String, MultipartFile> files)  throws Exception {
    	
    	String xmlBody = null;
    	Map<String, Object> mapJsonResponse = null;
    	
    	try {
            //Se crea la peticion Multipart
    		MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
            entityBuilder.setContentType(ContentType.create("multipart/related"));
            
	        Iterator<Map.Entry<String, MultipartFile>> it = files.entrySet().iterator();
	        Map.Entry<String, MultipartFile> multipartFile = null;
	        
	        //Se iteran las partes de la peticion para convertir la peticion y enviarla a Gesdoc
	        while (it.hasNext()) {
	        	multipartFile = it.next();
	        	MultipartFile multipartFileValue = multipartFile.getValue();
	        	String documentJson = new String(multipartFileValue.getBytes());
	        	
	        	if(multipartFileValue.getName().equals("document.json") && documentJson != null && !documentJson.isEmpty()) {
		        	//Se lee el documento adjunto en formato JSON y se convierte a XML para pasarlo al servicio SOAP de Gesdoc
	        		xmlBody = jsonToXmlBody(documentJson);
		        	
	        	}else {
	        		//Se obteniene el contenido del anexo que se quiere radicar en Gesdoc 
	        		String originalName = multipartFileValue.getOriginalFilename();
	        		
	        		//Se codifica el contenido del anexo en base64 y se pasa a la plantilla XML que se envia en el cuerpo de la peticion hacia Gesdoc 
	        		String anexoBase64 = new String(Base64.encodeBase64(multipartFileValue.getBytes()));
	        		xmlBody = xmlBody.replace("#{annexes.file}", anexoBase64);
	        		xmlBody = xmlBody.replace("#{annexes.description}", originalName);
	        		xmlBody = xmlBody.replace("#{annexes.name}", originalName);
	        		
	        		String[] originalNameSplit = originalName.split("\\.");
	        		String fileExt = originalNameSplit[originalNameSplit.length-1];
	        		xmlBody = xmlBody.replace("#{annexes.fileFormat}", fileExt);
	        		
	        		//Se agregan las partes (XML y anexo) de la peticion multipart/formdata
	        		entityBuilder.addTextBody("xmlPart", xmlBody, ContentType.create("text/xml"));
	        		entityBuilder.addBinaryBody("filePart", multipartFileValue.getBytes(), ContentType.create("application/pdf"), originalName);
	        	}
	        }
	        
	        
	        /*try {
	        	//Se guardar el cuerpo de la peticion en un archivo XML para hacer debug
	        	Path filePath = Path.of("src/main/resources/xmlbody.xml");
	            Files.writeString(filePath, xmlBody, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }*/
			
	        //Se crea la peticion POST hacia Gesdoc con las diferentes partes del cuerpo de la solicitud
	        CloseableHttpClient httpClient = HttpClients.createDefault();
	        HttpPost httpPost = new HttpPost("https://pruebas-gesdoc.minvivienda.gov.co/SGD_WS/GESDOC");
	        HttpEntity multipartEntity  = entityBuilder.build();
            httpPost.setEntity(multipartEntity);
            
            //System.out.println("*********** PETICION COMPLETA *****************");
            /*try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                multipartEntity.writeTo(outputStream);
                String requestBody = outputStream.toString("UTF-8");
                System.out.println("Cuerpo de la solicitud:\n" + requestBody);
            } catch (IOException e) {
                e.printStackTrace();
            }*/
	        
            //Se envia la peticion a Gesdoc
            try (CloseableHttpResponse gesdocResponse = httpClient.execute(httpPost)) {
            	
            	int statusCode = gesdocResponse.getCode();
                HttpEntity responseEntity = gesdocResponse.getEntity();
                byte[] responseBody = IOUtils.toByteArray(responseEntity.getContent());
                InputStream inputStream = new ByteArrayInputStream(responseBody);
                
        		//System.out.println("*********** RESPUESTA COMPLETA *****************");
        		//System.out.println(new String(responseBody));
                
            	if (responseEntity != null) {
                    ByteArrayDataSource datasource = new ByteArrayDataSource(inputStream, "multipart/form-data");
                    MimeMultipart multipart = new MimeMultipart(datasource);
                    
                    //Numero de partes de la respuesta
                    int count = multipart.getCount();
                    SharedByteArrayInputStream sbais = null;
                    byte[] contentBytes = null;
                    String response = "";
                    for (int i = 0; i < count; i++) {
                    	BodyPart bodyPart = multipart.getBodyPart(i);
                    	response = "";
                    	
                    	//Se obtiene la parte de la respuesta que corresponde al XML que contiene los datos del radicado 
                    	if (bodyPart.isMimeType("application/xop+xml")) {
                    		sbais = (SharedByteArrayInputStream) bodyPart.getContent();
                    		contentBytes = new byte[sbais.available()];
                    		sbais.read(contentBytes);
                    		
                    		//Se convierte la respuesta XML a JSON 
                    		if(statusCode == 200) {
                    			response = GesdocController.xmlToJson(new String(contentBytes));
                    		}else {
                    			response = GesdocController.extractFaultString(new String(contentBytes));
                    		}
                    		
                            
                    		try {
                            	ObjectMapper mapperJsonResponse = new ObjectMapper();
                                mapJsonResponse = mapperJsonResponse.readValue(response, HashMap.class);
                            } catch (Exception e) {
                            	e.printStackTrace();
                            	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"response\":\"error\": \"message\":\"No se pudo leer la respuesta XML de Gesdoc\"}");
                            }
                    	 }
                    	
                    	//Se obtiene la parte de la respuesta que corresponde al contenido de la etiqueda del radicado
                    	if (bodyPart.isMimeType("application/octet-stream")) {
                    		sbais = (SharedByteArrayInputStream) bodyPart.getContent();
                    		contentBytes = new byte[sbais.available()];
                    		sbais.read(contentBytes);
                    		mapJsonResponse.put("etiqueta", new String(Base64.encodeBase64(contentBytes)));
                    	 }
                     }   
            	 }
            }catch(Exception ex) {
            	ex.printStackTrace();
            }       
	        
            //Se convierte el HashMap a cadena en formato JSON para responder la peticion de este servicio intermediario
            String jsonResponse = "";
            try {
            	ObjectMapper objectMapper = new ObjectMapper();
                jsonResponse = objectMapper.writeValueAsString(mapJsonResponse);
            } catch (Exception e) {
            	jsonResponse = "{\"response\":\"error\": \"message\":\"No fue posible convertir la respuesta a formato JSON\"}";
                e.printStackTrace();
            }
        	
        	return ResponseEntity.status(HttpStatus.OK).body(jsonResponse);
    	}catch(Exception ex) {
    		ex.printStackTrace();
    		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"response\":\"error\": \"message\":\"Error interno del servidor\"}");
    	}
    }
    
    
    
    /**
     * 
     * @param jsonRequest
     * @return
     * @throws Exception
     */
    @CrossOrigin(origins = "*")
    @PostMapping( value = "/searchDocument", produces = {"application/json"}, consumes = {"application/json"})
    public ResponseEntity<String> searchDocument(@RequestBody String jsonRequest)  throws Exception {
    	System.out.println("[searchDocument] jsonRequest: " + jsonRequest);
        
    	try {
    		ObjectMapper mapper = new ObjectMapper();
            mapper.readTree(jsonRequest);
        } catch (Exception e) {
        	e.printStackTrace();
        	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"response\":\"error\": \"message\":\"Los datos de entrada no son correctos\"}");
        }
        
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.readValue(jsonRequest, new TypeReference<Map<String, String>>(){});
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"response\":\"error\": \"message\":\"Los datos de entrada no son correctos\"}");
		}
		
    	String seachDocumentJsonResponse = this.callSearchDocument(jsonRequest);
        Map<String, Object> mapJsonResponse = null;
		
        try {
        	ObjectMapper mapperJsonResponse = new ObjectMapper();
            mapJsonResponse = mapperJsonResponse.readValue(seachDocumentJsonResponse, HashMap.class);
        } catch (Exception e) {
        	e.printStackTrace();
        	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"response\":\"error\": \"message\":\"No se pudo leer la respuesta XML de Gesdoc\"}");
        }
    	
        
        Map<String, Object> mapDocumentAnnexResponse = this.callGetDocumentAnnex(jsonRequest);
        List<Map<String, Object>> anexos = (List<Map<String, Object>>) mapDocumentAnnexResponse.get("annex");
        mapJsonResponse.put("annexes", anexos);
        
        String jsonResponse = null;
        try {
        	ObjectMapper objectMapper = new ObjectMapper();
            jsonResponse = objectMapper.writeValueAsString(mapJsonResponse);
        } catch (Exception e) {
        	jsonResponse = "{\"response\":\"error\": \"message\":\"No fue posible convertir la respuesta a formato JSON\"}";
            e.printStackTrace();
        }
    	
    	return ResponseEntity.status(HttpStatus.OK).body(jsonResponse);
        /*HttpHeaders jsonResponseHeaders = new HttpHeaders();
        jsonResponseHeaders.set("Access-Control-Allow-Origin", "*");
        return new ResponseEntity<String>(jsonResponse, jsonResponseHeaders, HttpStatus.OK);*/
    }
    
    
    
    /**
     * 
     * @param jsonRequest
     * @return
     */
    private Map<String, Object> callGetDocumentAnnex(String jsonRequest) {
    	//String response = "";
    	HashMap<String, Object> mapMergedResponse = null;
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> jsonInputMap = null;
		try {
			jsonInputMap = objectMapper.readValue(jsonRequest, new TypeReference<Map<String, String>>(){});
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return null;
		}
		
		InputStream file = GesdocController.class.getClassLoader().getResourceAsStream("velocity/soap-getDocumentAnnex-body.vm");
		String xmlInputBody = templateParser(file);
		
		Map<String, String> templateValues = new HashMap<String, String>();
		templateValues.put("idDocument", jsonInputMap.get("trackNumber"));
		templateValues.put("idType", jsonInputMap.get("idType"));
        Iterator<Map.Entry<String, String>> it = templateValues.entrySet().iterator();
        Map.Entry<String, String> pair = null;
        while (it.hasNext()) {
            pair = it.next();
        	Pattern p = Pattern.compile("#\\{"+pair.getKey()+"\\}", Pattern.MULTILINE);
        	Matcher m = p.matcher(xmlInputBody);
        	xmlInputBody = m.replaceAll(pair.getValue() == null ? "-" : pair.getValue());
        }
		
        
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("https://pruebas-gesdoc.minvivienda.gov.co/SGD_WS/GESDOC");
        StringEntity entity = new StringEntity(xmlInputBody, ContentType.MULTIPART_FORM_DATA);
        httpPost.setEntity(entity);
         
        Map<String, Object> responseMap = null;
        try (CloseableHttpResponse gesdocResponse = httpClient.execute(httpPost)) {
        	
        	 int statusCode = gesdocResponse.getCode();
        	 System.out.println("[callGetDocumentAnnex] statusCode="+statusCode);
        	
        	HttpEntity responseEntity = gesdocResponse.getEntity();
        	System.out.println("[callGetDocumentAnnex] contentType="+responseEntity.getContentType());
        	
        	//if (responseEntity != null && ContentType.get(responseEntity).getMimeType().equals("multipart/related")) {
        	if (responseEntity != null) {
        			
        		byte[] responseBody = IOUtils.toByteArray(responseEntity.getContent());
                System.out.println("[callGetDocumentAnnex] Tamanio de la respuesta: " + responseBody.length);
                
                InputStream inputStream = new ByteArrayInputStream(responseBody);
                ByteArrayDataSource datasource = new ByteArrayDataSource(inputStream, "multipart/form-data");
                MimeMultipart multipart = new MimeMultipart(datasource);
                
                int count = multipart.getCount();
                System.out.println("[callGetDocumentAnnex] Numero de partes en la respuesta: " + count);
                 
                SharedByteArrayInputStream sbais = null;
                byte[] contentBytes = null;
                String jsonPart = null;
                BodyPart bodyPart = null;
                List<Map<String, Object>> anexos = null;
                Map<String, Object> annex = null;
                for (int i = 0; i < count; i++) {
                	jsonPart = null;
                	bodyPart = multipart.getBodyPart(i);
                	
                	//Contenido multipart
            		sbais = (SharedByteArrayInputStream) bodyPart.getContent();
            		contentBytes = new byte[sbais.available()];
            		sbais.read(contentBytes);
            		
                	//Se obtiene la parte XML de la respuesta que tiene los datos del anexo
                	if (bodyPart.isMimeType("application/xop+xml")) {
                		System.out.println("[callGetDocumentAnnex] Tamanio respuesta XML: " + contentBytes.length);
                		
                		if(statusCode == 200) {
                			jsonPart = GesdocController.xmlToJson(new String(contentBytes));
                		}else {
                			jsonPart = GesdocController.extractFaultString(new String(contentBytes));
                		}
                		
                		
                        try {
                        	ObjectMapper mapper = new ObjectMapper();
                            mapMergedResponse = mapper.readValue(jsonPart, HashMap.class);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                	 }
                	
                	
                	//Se obtiene la parte binaria de la respuesta que tiene el contenido del anexo
                	if (bodyPart.getDisposition() != null && bodyPart.isMimeType("application/octet-stream")) {
                		if(statusCode == 200) {
                    		System.out.println("[callGetDocumentAnnex] Tamanio del anexo: " + contentBytes.length);
                    		
                    		if(mapMergedResponse != null) {
                                responseMap = (Map<String, Object>) mapMergedResponse.get("Body");
                                responseMap = (Map<String, Object>) responseMap.get("getDocumentAnnexResponse");
                                
                                if(responseMap.get("annex") instanceof ArrayList) {
                                	anexos = (List<Map<String, Object>>) responseMap.get("annex");
                                	if(anexos != null && !anexos.isEmpty()) {
                                		annex = anexos.get(i-1);
                                		annex.put("file", new String(Base64.encodeBase64(contentBytes)));
                                	}
                                }else {
                                	annex = (Map<String, Object>) responseMap.get("annex");
                                	annex.put("file", new String(Base64.encodeBase64(contentBytes)));
                                	anexos = new ArrayList<Map<String, Object>>();
                                	anexos.add(annex);
                                	responseMap.put("annex", anexos);
                                }   
                    		}
                		}else {
                			mapMergedResponse.put("annexContent", null);
                			responseMap = (Map<String, Object>) mapMergedResponse.get("Body");
                			responseMap.put("file", null);
                		}
                	 }
                 }
        	}
        	
            return responseMap;
            
        }catch (Exception e) {
        	e.printStackTrace();
			responseMap = new HashMap<String, Object>();
			responseMap.put("response", "error");
			responseMap.put("message", "Peticion HTTP abortada");
			return responseMap;
		}   
    }
    
    
    /**
     * 
     * @param jsonRequest
     * @return
     */
    private String callSearchDocument(String jsonRequest) {
    	String response = "";
    	Map<String, String> jsonInputMap = null;
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			jsonInputMap = objectMapper.readValue(jsonRequest, new TypeReference<Map<String, String>>(){});
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return null;
		}
		
		InputStream file = GesdocController.class.getClassLoader().getResourceAsStream("velocity/soap-searchDocument-body.vm");
		String xmlInputBody = templateParser(file);
		
		Map<String, String> templateValues = new HashMap<String, String>();
		templateValues.put("trackNumber", jsonInputMap.get("trackNumber"));
		templateValues.put("identificationType", jsonInputMap.get("identificationType"));
		templateValues.put("identification", jsonInputMap.get("identification"));		
        Iterator<Map.Entry<String, String>> it = templateValues.entrySet().iterator();
        Map.Entry<String, String> pair = null;
        while (it.hasNext()) {
            pair = it.next();
        	Pattern p = Pattern.compile("#\\{"+pair.getKey()+"\\}", Pattern.MULTILINE);
        	Matcher m = p.matcher(xmlInputBody);
        	xmlInputBody = m.replaceAll(pair.getValue() == null ? "-" : pair.getValue());
        }
		
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("https://pruebas-gesdoc.minvivienda.gov.co/SGD_WS/GESDOC");
        StringEntity entity = new StringEntity(xmlInputBody, ContentType.MULTIPART_FORM_DATA);
        httpPost.setEntity(entity);
        
        try (CloseableHttpResponse gesdocResponse = httpClient.execute(httpPost)) {
        	
        	int statusCode = gesdocResponse.getCode();
        	System.out.println("[callSearchDocument] statusCode="+statusCode);
        	
        	HttpEntity responseEntity = gesdocResponse.getEntity();
        	System.out.println("[callSearchDocument] contentType="+responseEntity.getContentType());
        	
        	
        	//if (responseEntity != null && ContentType.get(responseEntity).getMimeType().equals("multipart/related")) {
        	if (responseEntity != null) {
        			
        		byte[] responseBody = IOUtils.toByteArray(responseEntity.getContent());
                System.out.println("[callSearchDocument] Tamanio de la respuesta: " + responseBody.length);
                
                InputStream inputStream = new ByteArrayInputStream(responseBody);
                ByteArrayDataSource datasource = new ByteArrayDataSource(inputStream, "multipart/form-data");
                MimeMultipart multipart = new MimeMultipart(datasource);
                
                int count = multipart.getCount();
                System.out.println("[callSearchDocument] Numero de partes de la respuesta: " + count);
                 
                SharedByteArrayInputStream sbais = null;
                byte[] contentBytes = null;
                for (int i = 0; i < count; i++) {
                	BodyPart bodyPart = multipart.getBodyPart(i);
                	if (bodyPart.isMimeType("application/xop+xml")) {
                		
                		sbais = (SharedByteArrayInputStream) bodyPart.getContent();
                		contentBytes = new byte[sbais.available()];
                		sbais.read(contentBytes);
                		
                		if(statusCode == 200) {
                			response = GesdocController.xmlToJson(new String(contentBytes));
                		}else {
                			response = GesdocController.extractFaultString(new String(contentBytes));
                		}
                	 }
                 }   
        	 }
        }catch (Exception e) {
			e.printStackTrace();
			response = "{\"response\":\"error\": \"message\":\"Peticion HTTP abortada\"}";
		}
        
        return response;
    }
    
    /**
     * Convierte una cadena en formato XML a formato JSON
     * 
     * @param xml
     * @return
     */
    public static String xmlToJson(String xml) {
        try {
            XmlMapper xmlMapper = new XmlMapper();
            JsonNode node = xmlMapper.readTree(xml.getBytes());
            
            ObjectMapper jsonMapper = new ObjectMapper();
            return jsonMapper.writeValueAsString(node);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    
    
    public static String jsonToXmlBody(String documentJson) {
		InputStream file = GesdocController.class.getClassLoader().getResourceAsStream("velocity/soap-createReceived-body2.vm");
		String xmlInputBody = templateParser(file);
		
    	Map<String, String> jsonInputMap =  new HashMap<>();;
    	
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode rootNode = objectMapper.readTree(documentJson);
	        flattenJson("", rootNode, jsonInputMap);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return null;
		}
		
		System.out.println("******* jsonInputMap ********");
		System.out.println(jsonInputMap);
		
		
		Map<String, String> templateValues = new HashMap<String, String>();
		templateValues.put("targetDependence.code", (String) jsonInputMap.get("targetDependence.code"));
		templateValues.put("sourceThirdPerson.identification", (String) jsonInputMap.get("sourceThirdPerson.identification"));
		templateValues.put("sourceThirdPerson.address", (String) jsonInputMap.get("sourceThirdPerson.address"));
		templateValues.put("sourceThirdPerson.municipality.code", (String) jsonInputMap.get("sourceThirdPerson.municipality.code"));	                
		templateValues.put("type.id", (String) jsonInputMap.get("type.id"));
		templateValues.put("targetUser.identification", (String) jsonInputMap.get("targetUser.identification"));
		templateValues.put("sourceThirdPerson.name", (String) jsonInputMap.get("sourceThirdPerson.name"));
		templateValues.put("sourceThirdPerson.lastname", (String) jsonInputMap.get("sourceThirdPerson.lastname"));
		templateValues.put("sourceThirdPerson.email", (String) jsonInputMap.get("sourceThirdPerson.email"));
		templateValues.put("reference", (String) jsonInputMap.get("reference"));
		templateValues.put("observations", (String) jsonInputMap.get("observations"));
		templateValues.put("authorDependence.code", (String) jsonInputMap.get("authorDependence.code"));
		templateValues.put("sourceThirdPerson.phone", (String) jsonInputMap.get("sourceThirdPerson.phone"));
		templateValues.put("author.identification", (String) jsonInputMap.get("author.identification"));
		templateValues.put("sourceThirdPerson.identificationType.name", (String) jsonInputMap.get("sourceThirdPerson.identificationType.name"));
		
		
		Iterator<Map.Entry<String, String>> itJson = templateValues.entrySet().iterator();
        Map.Entry<String, String> pair = null;
        while (itJson.hasNext()) {
            pair = itJson.next(); 
        	Pattern p = Pattern.compile("#\\{"+pair.getKey()+"\\}", Pattern.MULTILINE);
        	Matcher m = p.matcher(xmlInputBody);
        	xmlInputBody = m.replaceAll(pair.getValue() == null ? "-" : pair.getValue());
        }
        
		return xmlInputBody;
    }
    
    
    
    private static void flattenJson(String parentKey, JsonNode node, Map<String, String> result) {
        if (node.isObject()) {
            Iterator<Map.Entry<String, JsonNode>> fields = node.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> field = fields.next();
                String newKey = parentKey.isEmpty() ? field.getKey() : parentKey + "." + field.getKey();
                flattenJson(newKey, field.getValue(), result);
            }
        } else if (node.isArray()) {
            for (int i = 0; i < node.size(); i++) {
                String newKey = parentKey + "[" + i + "]";
                flattenJson(newKey, node.get(i), result);
            }
        } else {
            result.put(parentKey, node.asText());
        }
    }
    
    /**
     * Extrae el mensaje de fallo (nodo faultstring del XML de respuesta) cuando la busqueda no retorna ningun resultado
     * 
     * @param xml
     * @return
     */
    public static String extractFaultString(String xml) {
        try {
            XmlMapper xmlMapper = new XmlMapper();
            JsonNode node = xmlMapper.readTree(xml.getBytes());
            ObjectMapper jsonMapper = new ObjectMapper();
            String valueAsString = jsonMapper.writeValueAsString(node);
            Map<String, Map<String, Object>> configReglaMap = jsonMapper.readValue(valueAsString, new TypeReference<Map<String, Map<String, Object>>>(){});            
            Map<String, Object> body = (Map<String, Object>) configReglaMap.get("Body");
            Map<String, String> fault = (Map<String, String>) body.get("Fault");
            ObjectMapper jsonMapper2 = new ObjectMapper();
            return jsonMapper2.writeValueAsString(fault);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 
     * @param file
     * @return
     */
	private static String templateParser(InputStream file) {
		StringBuilder templateContent = new StringBuilder();
		
		try {
			String line = "";
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(file));
			while ((line = bufferedReader.readLine()) != null) {
				templateContent.append(line);
			}
			
			bufferedReader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return templateContent.toString();
	}
    
    /**
     * 
     * @param fileName
     * @return
     * @throws IOException
     */
    public byte[] readImageFromClasspath(String fileName) throws IOException {
        Resource resource = new ClassPathResource(fileName);
        
        //System.out.println("exists = " + resource.exists());
        //System.out.println("contentLength = " + resource.contentLength());
        //System.out.println(resource.getURI());
        try (InputStream inputStream = resource.getInputStream()) {
            
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            byte[] data = new byte[inputStream.available()];
            int bytesRead;
            while ((bytesRead = inputStream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, bytesRead);
            }
            
            return buffer.toByteArray();
        }
    }
    
}