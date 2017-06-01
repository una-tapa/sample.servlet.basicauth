package it;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Base64;

import org.apache.cxf.helpers.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;

public class EndpointTest {
	
	public void testEndpoint(String endpoint, String expectedOutput) throws ClientProtocolException, IOException {
		String port = System.getProperty("liberty.test.port");
		String war = System.getProperty("war.context");
		System.out.println("Port: " + port + "\nWar: " + war);
        String url = "http://localhost:" + port + "/" + war + endpoint;
        System.out.println("Testing " + url);
        HttpResponse response;
        if (expectedOutput.equalsIgnoreCase("getAuthType: BASIC")){
        	HttpResponse expectFail;
        	expectFail = sendRequest(url,"GET");
        	response = sendSecureRequest(url, "GET");
        	int responseCodeFail = expectFail.getStatusLine().getStatusCode();
            assertTrue("Incorrect response code (expected 401 unauthorized): " + responseCodeFail,
                    responseCodeFail == 401);
        }
        else {
        	response = sendRequest(url, "GET");
        }
        int responseCode = response.getStatusLine().getStatusCode();
        assertTrue("Incorrect response code: " + responseCode,
                   responseCode == 200);
        String responseString = IOUtils.toString(response.getEntity().getContent());
        assertTrue("Incorrect response, response is: " + responseString + "Expected: " + expectedOutput, responseString.contains(expectedOutput));
	}
	
    public HttpResponse sendRequest(String url, String requestType) throws ClientProtocolException, IOException {
    	HttpPost httppost = new HttpPost(url);
    	System.out.println("Executing request " + httppost.getRequestLine());
    	HttpClient httpclient = HttpClientBuilder.create().build();
    	HttpResponse response = httpclient.execute(httppost);
        return response;
    }
    
    public HttpResponse sendSecureRequest(String url, String requestType) throws ClientProtocolException, IOException {
    	String encoding = Base64.getEncoder().encodeToString(("user1:password").getBytes());
    	HttpPost httppost = new HttpPost(url);
    	httppost.setHeader("Authorization", "Basic " + encoding);
    	System.out.println("Executing secure request " + httppost.getRequestLine());
    	HttpClient httpclient = HttpClientBuilder.create().build();
    	HttpResponse response = httpclient.execute(httppost);
        return response;
    }
	
}