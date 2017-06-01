package it;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.junit.Test;

public class TestApplication extends EndpointTest {

    @Test
    public void testDeployment() throws ClientProtocolException, IOException {
        testEndpoint("/index.html", "<h1>Welcome to your Liberty Application</h1>");
    }
}