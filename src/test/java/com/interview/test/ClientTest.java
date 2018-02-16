package com.interview.test;

import io.dropwizard.testing.junit.DropwizardClientRule;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.junit.ClassRule;
import org.junit.Test;


public class ClientTest {

	@Path("/ping")
    public static class PingResource {
        @GET
        public String ping() {
            return "pong";
        }
    }
	
	@ClassRule
    public static final DropwizardClientRule dropwizard = new DropwizardClientRule(new PingResource());

    @Test
    public void shouldPing() throws IOException {
        final URL url = new URL(dropwizard.baseUri() + "/ping");
        final String response = new BufferedReader(new InputStreamReader(url.openStream())).readLine();
        assertEquals("pong", response);
    }
}
