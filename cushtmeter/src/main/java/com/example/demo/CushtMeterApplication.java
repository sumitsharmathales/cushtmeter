package com.example.demo;

import java.io.IOException;
import java.net.MalformedURLException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CushtMeterApplication {

	public static void main(String[] args) throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, CertificateException, MalformedURLException, IOException {
		SpringApplication.run(CushtMeterApplication.class, args);
//		SSLContext sslContext = new SSLContextBuilder()
//				.loadTrustMaterial(new URL("file:src/main/resources/keystore.p12"), "mydemo".toCharArray()).build();
//		SSLConnectionSocketFactory sslConFactory = new SSLConnectionSocketFactory(sslContext);
//
//		CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(sslConFactory).build();
//		ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
//		RestTemplate restTemplate = new RestTemplate(requestFactory);
//		HttpHeaders headers = new HttpHeaders();
//		headers.add("Content-Type", "application/json");
//		headers.add("Authorization", "Bearer Njc3MTM1OTI2NjkyOowjfMSzdtZ+7I0Tyj8ObPMP3207");
//		HttpEntity<String> entity = new HttpEntity<String>(headers);
//		ResponseEntity<String> response = restTemplate.exchange("https://jira.gemalto.com/rest/api/2/search?jql=project%20%3D%20AVDT&fields=labels,Customer", HttpMethod.GET, entity, String.class);
//		String response = restTemplate.getForObject("", String.class);

		//		
	}

}
