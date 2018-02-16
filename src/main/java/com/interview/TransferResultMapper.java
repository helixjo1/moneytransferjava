package com.interview;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TransferResultMapper implements ResultSetMapper<Transfer> {

    @Override
    public Transfer map(int index, ResultSet r, StatementContext ctx) throws SQLException {
        return new Transfer( r.getString("originatorId"),  r.getString("destinationId"), r.getBigDecimal("amount"), r.getString("result"),  r.getString("error"),  r.getString("message"),  r.getString("user_id"));
    }
}
