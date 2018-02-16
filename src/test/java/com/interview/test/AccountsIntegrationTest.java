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


import javax.ws.rs.client.Client;
import javax.ws.rs.core.Response;

public class AccountsIntegrationTest {

	private static final ObjectMapper MAPPER = Jackson.newObjectMapper();


	@ClassRule
	public static final DropwizardAppRule<ServiceConfiguration> RULE = new DropwizardAppRule<ServiceConfiguration>(
			ServiceApplication.class, ResourceHelpers.resourceFilePath("configuration.yml"));

	Client client = new JerseyClientBuilder(RULE.getEnvironment()).build("Test");
	
	@Test
	public void runGetAccountsTest() throws Exception {
		final String expected = MAPPER
				.writeValueAsString(MAPPER.readValue(fixture("fixtures/accounts.json"), new TypeReference<List<Account>>(){}));
		
		final String expected2 = MAPPER
				.writeValueAsString(MAPPER.readValue(fixture("fixtures/account.json"), Account.class));
		
		Response response = client.target(String.format("http://localhost:%d/api/admin/accounts/", RULE.getLocalPort())).request().header("Authorization", "Basic QURNSU5VU0VSOkFETUlO").get();
		
		Response response2 = client.target(String.format("http://localhost:%d/api/user/account/", RULE.getLocalPort())).request().header("Authorization", "Basic dXNlcjI6dXNlcnBhc3M=").get();
		
		Response response3 = client.target(String.format("http://localhost:%d/api/admin/accounts/2", RULE.getLocalPort())).request().header("Authorization", "Basic QURNSU5VU0VSOkFETUlO").get();
		
		assertThat(response.readEntity(String.class)).isEqualTo(expected);
		
		assertThat(response2.readEntity(String.class)).isEqualTo(expected2);
		
		assertThat(response3.readEntity(String.class)).isEqualTo(expected2);
	}
	
	
}
