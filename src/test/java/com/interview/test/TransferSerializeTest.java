package com.interview.test;

import static io.dropwizard.testing.FixtureHelpers.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import io.dropwizard.jackson.Jackson;

import org.junit.Test;


import com.fasterxml.jackson.databind.ObjectMapper;

import com.interview.Transfer;


public class TransferSerializeTest {

	
	private static final ObjectMapper MAPPER = Jackson.newObjectMapper();
	final Transfer transfer = new Transfer( "1", "2", new BigDecimal("10.00"), "success", "", "Transfer complete", "user1");

	@Test
	public void serializesTransferToJSON() throws Exception {
		

		final String expected = MAPPER
				.writeValueAsString(MAPPER.readValue(fixture("fixtures/transfer.json"), Transfer.class));

		assertThat(MAPPER.writeValueAsString(transfer)).isEqualTo(expected);
	}
	

}
