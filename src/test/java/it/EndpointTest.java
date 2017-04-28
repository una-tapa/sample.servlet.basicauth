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

import org.apache.cxf.helpers.IOUtils;
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
        String url = "http://localhost:" + port + "/" + war + endpoint;
        System.out.println("Testing " + url);
        HttpResponse response = sendRequest(url, "GET");
        int responseCode = response.getStatusLine().getStatusCode();
        assertTrue("Incorrect response code: " + responseCode,
                   responseCode == 200);
        String responseString = IOUtils.toString(response.getEntity().getContent());
        assertTrue("Incorrect response, response is: " + responseString + "Expected: " + expectedOutput, responseString.contains(expectedOutput));
    }

    public HttpResponse sendRequest(String url, String requestType) throws ClientProtocolException, IOException {
    	String encoding = Base64.getEncoder().encodeToString(("user1:password").getBytes());
    	HttpPost httppost = new HttpPost(url);
    	httppost.setHeader("Authorization", "Basic " + encoding);
    	System.out.println("Executing request " + httppost.getRequestLine());
    	HttpClient httpclient = HttpClientBuilder.create().build();
    	HttpResponse response = httpclient.execute(httppost);
        return response;
    }
}
