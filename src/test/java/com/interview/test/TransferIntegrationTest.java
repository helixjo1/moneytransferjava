package com.interview.test;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.assertj.core.api.Assertions.assertThat;



import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;

import com.fasterxml.jackson.databind.ObjectMapper;


import org.junit.ClassRule;
import org.junit.Test;

import com.interview.ServiceApplication;
import com.interview.ServiceConfiguration;
import com.interview.Transfer;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

public class TransferIntegrationTest {

	private static final ObjectMapper MAPPER = Jackson.newObjectMapper();


	@ClassRule
	public static final DropwizardAppRule<ServiceConfiguration> RULE = new DropwizardAppRule<ServiceConfiguration>(
			ServiceApplication.class, ResourceHelpers.resourceFilePath("configuration.yml"));

	Client client = new JerseyClientBuilder(RULE.getEnvironment()).build("Test");
	
	@Test
	public void runTransferIntegrationTest() throws Exception {
		final String expected = MAPPER
				.writeValueAsString(MAPPER.readValue(fixture("fixtures/transfer1Expected.json"),Transfer.class));
		Response response = client.target(String.format("http://localhost:%d/api/user/transfer", RULE.getLocalPort()))
				.request()
				.header("Authorization", "Basic dXNlcjE6dXNlcnBhc3M=")
				.post(Entity.json(MAPPER.readValue(fixture("fixtures/transfer1Body.json"), Transfer.class)));
		
		assertThat(response.readEntity(String.class)).isEqualTo(expected);
	}
	
	
}
