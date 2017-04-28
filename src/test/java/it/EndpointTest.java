/*******************************************************************************
 * Copyright (c) 2016 IBM Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/ 
package it;

import java.io.IOException;
import java.util.Base64;
import static org.junit.Assert.assertTrue;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;

public class EndpointTest {

    public void testEndpoint(String endpoint, String expectedOutput) throws ClientProtocolException, IOException {
        String port = System.getProperty("liberty.test.port");
        String war = System.getProperty("war.context");
    	String apploc = System.getProperty("appLocation");
    	System.out.println("Port: " + port + "\nWar: " + war + "\nApploc: " + apploc);
        //String url = "http://user1:password@localhost:" + port + "/" + war + endpoint;
    	String url = "http://localhost:" + port + "/" + war + endpoint;
        System.out.println("Testing 1" + url);
        Response response = sendRequest(url, "GET");
        int responseCode = response.getStatus();
        System.out.println("Test 1 response is " + response.getHeaders().toString() + "And response code is " + responseCode);
        assertTrue("Incorrect response code: " + responseCode + "But the response is + " + response.readEntity(String.class),
                   responseCode == 200);
        String responseString = response.readEntity(String.class);
        response.close();
        assertTrue("Incorrect response, response is " + responseString, responseString.contains(expectedOutput));
    }

    public Response sendRequest(String url, String requestType) throws ClientProtocolException, IOException {
    	String encoding = Base64.getEncoder().encodeToString(("test1:test1").getBytes());
    	HttpPost httppost = new HttpPost("http://user1:password@localhost:9080/servlet/servlet");
    	httppost.setHeader("Authorization", "Basic " + encoding);
    	System.out.println("executing request " + httppost.getRequestLine());
    	HttpClient httpclient = HttpClientBuilder.create().build();
    	HttpResponse response1 = httpclient.execute(httppost);
    	HttpEntity entity = response1.getEntity();
        Client client = ClientBuilder.newClient();
        System.out.println("Testing 2" + url);
        WebTarget target = client.target(url);
        Invocation.Builder invoBuild = target.request();
        Response response = invoBuild.build(requestType).invoke();
        System.out.println("Test 2 response is " + response.getStringHeaders().toString());
        return response;
    }
}
