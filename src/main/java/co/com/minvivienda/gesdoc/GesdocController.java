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
    @PostMapping(value = "/searchDocument", produces = {"application/json"}, consumes = {"application/json"})
    public ResponseEntity<String> searchDocument(@RequestBody String jsonRequest)  throws Exception {
    	String response = "";
    	ObjectMapper mapper = new ObjectMapper();
    	System.out.println("jsonRequest: " + jsonRequest);
        
    	try {
            mapper.readTree(jsonRequest);
        } catch (Exception e) {
        	e.printStackTrace();
        	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Los datos de entrada no son correctos");
        }
        
        
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> jsonInputMap = null;
		try {
			jsonInputMap = objectMapper.readValue(jsonRequest, new TypeReference<Map<String, String>>(){});
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Los datos de entrada no son correctos");
		}
		
		
		InputStream file = GesdocController.class.getClassLoader()
				.getResourceAsStream("velocity/soap-searchDocument-body.vm");	
		
		Map<String, String> templateValues = new HashMap<String, String>();
		templateValues.put("trackNumber", jsonInputMap.get("trackNumber"));
		templateValues.put("identificationType", jsonInputMap.get("identificationType"));
		templateValues.put("identification", jsonInputMap.get("identification"));
		
		String xmlInputBody = templateParser(file);
        Iterator<Map.Entry<String, String>> it = templateValues.entrySet().iterator();
        Map.Entry<String, String> pair = null;
        while (it.hasNext()) {
            pair = it.next();
        	Pattern p = Pattern.compile("#\\{"+pair.getKey()+"\\}", Pattern.MULTILINE);
        	Matcher m = p.matcher(xmlInputBody);
        	xmlInputBody = m.replaceAll(pair.getValue() == null ? "-" : pair.getValue());
        }
		
        
        System.out.println("\n ******** xmlInputBody *********\n");
        System.out.println(xmlInputBody);
        
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("https://pruebas-gesdoc.minvivienda.gov.co/SGD_WS/GESDOC");
        StringEntity entity = new StringEntity(xmlInputBody, ContentType.MULTIPART_FORM_DATA);
        httpPost.setEntity(entity);
        
        try (CloseableHttpResponse gesdocResponse = httpClient.execute(httpPost)) {
        	
        	 int statusCode = gesdocResponse.getStatusLine().getStatusCode();
        	 if(statusCode != 200) {
        		 Map<String, String> responseMap = new HashMap<String, String>();
        		 responseMap.put("response", "error");
        		 responseMap.put("message", "");
        		 
        		 return ResponseEntity.status(HttpStatus.OK).body(response);
        	 }
        	
        	org.apache.http.HttpEntity responseEntity = gesdocResponse.getEntity();
        	System.out.println("getMimeType = " + ContentType.get(responseEntity).getMimeType());
        	if (responseEntity != null && ContentType.get(responseEntity).getMimeType().equals("multipart/related")) {
        			
        		byte[] responseBody = IOUtils.toByteArray(responseEntity.getContent());
                System.out.println("responseBody.length=" + responseBody.length);
                 
                InputStream inputStream = new ByteArrayInputStream(responseBody);
                ByteArrayDataSource datasource = new ByteArrayDataSource(inputStream, "multipart/form-data");
                MimeMultipart multipart = new MimeMultipart(datasource);
                
                int count = multipart.getCount();
                System.out.println("Num partes: " + count);
                 
                SharedByteArrayInputStream sbais = null;
                byte[] contentBytes = null;
                for (int i = 0; i < count; i++) {
                	BodyPart bodyPart = multipart.getBodyPart(i);
                	if (bodyPart.isMimeType("application/xop+xml")) {
                		
                		sbais = (SharedByteArrayInputStream) bodyPart.getContent();
                		contentBytes = new byte[sbais.available()];
                		sbais.read(contentBytes);
                		System.out.println("\n **** contentBytes **** \n");
                		System.out.println(new String(contentBytes));
                		
                		System.out.println("\n **** convertUsingJackson **** \n");
                		response = GesdocController.convertUsingJackson(new String(contentBytes));
                		System.out.println(response);
                	 }
                 }   
        	 }
        }catch (Exception e) {
			e.printStackTrace();
		}
        
        
    	return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    
    
    public static String convertUsingJackson(String xml) {
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