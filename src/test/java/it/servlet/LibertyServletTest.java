package it.servlet;

import it.EndpointTest;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.junit.Test;

public class LibertyServletTest extends EndpointTest {

    @Test
    public void testDeployment() throws ClientProtocolException, IOException {
    	testEndpoint("/servlet", "getAuthType: BASIC");
    }
}