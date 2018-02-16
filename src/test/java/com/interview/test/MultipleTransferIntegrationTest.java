package com.interview.test;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;


import org.junit.ClassRule;
import org.junit.Test;

import com.interview.Account;
import com.interview.ServiceApplication;
import com.interview.ServiceConfiguration;
import com.interview.Transfer;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

public class MultipleTransferIntegrationTest {

	private static final ObjectMapper MAPPER = Jackson.newObjectMapper();


	@ClassRule
	public static final DropwizardAppRule<ServiceConfiguration> RULE = new DropwizardAppRule<ServiceConfiguration>(
			ServiceApplication.class, ResourceHelpers.resourceFilePath("configuration.yml"));

	Client client = new JerseyClientBuilder(RULE.getEnvironment()).build("Test");
	
	@Test
	public void runMultipleTransferTest() throws Exception {
		final String expectedTransfer1 = MAPPER
				.writeValueAsString(MAPPER.readValue(fixture("fixtures/transfer1Expected.json"),Transfer.class));
		
		final String expectedTransfer2 = MAPPER
				.writeValueAsString(MAPPER.readValue(fixture("fixtures/transfer2Expected.json"),Transfer.class));
		
		final String expectedAccounts = MAPPER
				.writeValueAsString(MAPPER.readValue(fixture("fixtures/accountsTransfers.json"), new TypeReference<List<Account>>(){}));
		
		final String expectedTransferLog = MAPPER
				.writeValueAsString(MAPPER.readValue(fixture("fixtures/transferLog.json"), new TypeReference<List<Transfer>>(){}));
		
		Response response = client.target(String.format("http://localhost:%d/api/user/transfer", RULE.getLocalPort()))
				.request()
				.header("Authorization", "Basic dXNlcjE6dXNlcnBhc3M=")
				.post(Entity.json(MAPPER.readValue(fixture("fixtures/transfer1Body.json"), Transfer.class)));
		
		Response response2 = client.target(String.format("http://localhost:%d/api/user/transfer", RULE.getLocalPort()))
				.request()
				.header("Authorization", "Basic dXNlcjM6dXNlcnBhc3M=")
				.post(Entity.json(MAPPER.readValue(fixture("fixtures/transfer2Body.json"), Transfer.class)));
		
		Response response3 = client.target(String.format("http://localhost:%d/api/admin/accounts/", RULE.getLocalPort())).request().header("Authorization", "Basic QURNSU5VU0VSOkFETUlO").get();
		
		Response response4 = client.target(String.format("http://localhost:%d/api/admin/transfers/all", RULE.getLocalPort())).request().header("Authorization", "Basic QURNSU5VU0VSOkFETUlO").get();
		
		assertThat(response.readEntity(String.class)).isEqualTo(expectedTransfer1);
		
		assertThat(response2.readEntity(String.class)).isEqualTo(expectedTransfer2);
		
		assertThat(response3.readEntity(String.class)).isEqualTo(expectedAccounts);
		
		assertThat(response4.readEntity(String.class)).isEqualTo(expectedTransferLog);
	}
	
	
}
