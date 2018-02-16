package com.interview.test;

import static io.dropwizard.testing.FixtureHelpers.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import io.dropwizard.jackson.Jackson;

import org.junit.Test;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.interview.Account;


public class AccountSerializeTest {

	
	private static final ObjectMapper MAPPER = Jackson.newObjectMapper();
	final Account account = new Account("2", new BigDecimal("120.00"));

	@Test
	public void serializesAccountToJSON() throws Exception {
		

		final String expected = MAPPER
				.writeValueAsString(MAPPER.readValue(fixture("fixtures/account.json"), Account.class));

		assertThat(MAPPER.writeValueAsString(account)).isEqualTo(expected);
	}
	

}
