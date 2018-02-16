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

public class AuthTest {

	private static final ObjectMapper MAPPER = Jackson.newObjectMapper();


	@ClassRule
	public static final DropwizardAppRule<ServiceConfiguration> RULE = new DropwizardAppRule<ServiceConfiguration>(
			ServiceApplication.class, ResourceHelpers.resourceFilePath("configuration.yml"));

	Client client = new JerseyClientBuilder(RULE.getEnvironment()).build("Test");
	
	@Test
	public void runGetAuthTest() throws Exception {
		
		Response response = client.target(String.format("http://localhost:%d/api/user/transfer", RULE.getLocalPort()))
				.request()
				.post(Entity.json(MAPPER.readValue(fixture("fixtures/transfer1Body.json"), Transfer.class)));
		
		Response response2 = client.target(String.format("http://localhost:%d/api/admin/transfers/all", RULE.getLocalPort()))
				.request()
				.get();
		
		Response response3 = client.target(String.format("http://localhost:%d/api/admin/transfers/all", RULE.getLocalPort()))
				.request()
				.header("Authorization", "Basic dXNlcjE6dXNlcnBhc3M=")
				.get();
		

		Response response4 = client.target(String.format("http://localhost:%d/api/admin/accounts", RULE.getLocalPort()))
				.request()
				.header("Authorization", "Basic dXNlcjE6dXNlcnBhc3M=")
				.get();
		
		Response response5 = client.target(String.format("http://localhost:%d/api/admin/accounts/2", RULE.getLocalPort()))
				.request()
				.header("Authorization", "Basic dXNlcjE6dXNlcnBhc3M=")
				.get();
		
		Response response6 = client.target(String.format("http://localhost:%d/api/user/account", RULE.getLocalPort()))
				.request()
				.header("Authorization", "Basic dXNlcjE6dXNlcnBhc3M=")
				.get();
		
		Response response7 = client.target(String.format("http://localhost:%d/api/admin/accounts", RULE.getLocalPort()))
				.request()
				.header("Authorization", "Basic QURNSU5VU0VSOkFETUlO")
				.get();
		
		Response response8 = client.target(String.format("http://localhost:%d/api/admin/accounts/2", RULE.getLocalPort()))
				.request()
				.header("Authorization", "Basic QURNSU5VU0VSOkFETUlO")
				.get();
		
		assertThat(response.getStatus()).isEqualTo(401);
		assertThat(response2.getStatus()).isEqualTo(401);
		assertThat(response3.getStatus()).isEqualTo(403);
		assertThat(response4.getStatus()).isEqualTo(403);
		assertThat(response5.getStatus()).isEqualTo(403);
		assertThat(response6.getStatus()).isEqualTo(200);
		assertThat(response7.getStatus()).isEqualTo(200);
		assertThat(response8.getStatus()).isEqualTo(200);
		
	}
	
	
}
