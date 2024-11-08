package co.com.minvivienda.gesdoc;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.BodyPart;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import javax.mail.util.SharedByteArrayInputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
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

@CrossOrigin(origins = "*")
@RestController
public class GesdocController {
	
	
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
    
    
    
    @PostMapping(value = "/createReceived", produces = {"multipart/mixed"}, consumes = {"multipart/form-data"})
    public ResponseEntity<?> createReceivedReal(@RequestParam Map<String, MultipartFile> files)  throws Exception {
    	
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
        mapJsonResponse.put("annex", mapDocumentAnnexResponse);
        
        String jsonResponse = null;
        try {
        	ObjectMapper objectMapper = new ObjectMapper();
            jsonResponse = objectMapper.writeValueAsString(mapJsonResponse);
        } catch (Exception e) {
        	jsonResponse = "{\"response\":\"error\": \"message\":\"No fue posible convertir la respuesta a formato JSON\"}";
            e.printStackTrace();
        }
    	
    	//return ResponseEntity.status(HttpStatus.OK).body(jsonResponse);
        HttpHeaders jsonResponseHeaders = new HttpHeaders();
        jsonResponseHeaders.set("Access-Control-Allow-Origin", "*");
        
    	return new ResponseEntity<String>(jsonResponse, jsonResponseHeaders, HttpStatus.OK);
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
        	
        	 int statusCode = gesdocResponse.getStatusLine().getStatusCode();
        	 System.out.println("[callGetDocumentAnnex] statusCode="+statusCode);
        	
        	org.apache.http.HttpEntity responseEntity = gesdocResponse.getEntity();
        	
        	if (responseEntity != null && ContentType.get(responseEntity).getMimeType().equals("multipart/related")) {
        			
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
                for (int i = 0; i < count; i++) {
                	jsonPart = null;
                	bodyPart = multipart.getBodyPart(i);
                	
                	//Se obtiene la parte XML de la respuesta que tiene los datos del anexo
                	if (bodyPart.isMimeType("application/xop+xml")) {
                		
                		sbais = (SharedByteArrayInputStream) bodyPart.getContent();
                		contentBytes = new byte[sbais.available()];
                		sbais.read(contentBytes);
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
                	if (bodyPart.isMimeType("application/octet-stream")) {
                		sbais = (SharedByteArrayInputStream) bodyPart.getContent();
                		contentBytes = new byte[sbais.available()];
                		sbais.read(contentBytes);
                		
                		if(statusCode == 200) {
                    		System.out.println("[callGetDocumentAnnex] Tamanio del anexo: " + contentBytes.length);
                    		
                    		if(mapMergedResponse != null) {
                                responseMap = (Map<String, Object>) mapMergedResponse.get("Body");
                                responseMap = (Map<String, Object>) responseMap.get("getDocumentAnnexResponse");
                                responseMap = (Map<String, Object>) responseMap.get("annex");
                                responseMap.put("file", new String(Base64.encodeBase64(contentBytes)));
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
        	
        	 int statusCode = gesdocResponse.getStatusLine().getStatusCode();
        	 System.out.println("[callSearchDocument] statusCode="+statusCode);
        	
        	org.apache.http.HttpEntity responseEntity = gesdocResponse.getEntity();
        	if (responseEntity != null && ContentType.get(responseEntity).getMimeType().equals("multipart/related")) {
        			
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
        
        System.out.println("exists = " + resource.exists());
        System.out.println("contentLength = " + resource.contentLength());
        System.out.println(resource.getURI());
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