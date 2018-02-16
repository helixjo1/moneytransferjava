package com.interview;

import io.dropwizard.Application;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.flyway.FlywayBundle;
import io.dropwizard.flyway.FlywayFactory;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.jdbi.bundles.DBIExceptionsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.skife.jdbi.v2.DBI;

/**
 * http://www.dropwizard.io/1.0.6/docs/manual/core.html#application
 */
public class ServiceApplication extends Application<ServiceConfiguration> {
    public static void main(String[] args) throws Exception {
        new ServiceApplication().run(args);
    }

    @Override
    public void initialize(Bootstrap<ServiceConfiguration> bootstrap) {
        super.initialize(bootstrap);
        bootstrap.setConfigurationSourceProvider(
        	    new SubstitutingSourceProvider(
        	        bootstrap.getConfigurationSourceProvider(),
        	        new EnvironmentVariableSubstitutor())
        	);
        bootstrap.addBundle(new FlywayBundle<ServiceConfiguration>() {
            @Override
            public DataSourceFactory getDataSourceFactory(ServiceConfiguration configuration) {
                return configuration.getDataSourceFactory();
            }

            @Override
            public FlywayFactory getFlywayFactory(ServiceConfiguration configuration) {
                return configuration.getFlywayFactory();
            }
        });
        bootstrap.addBundle(new DBIExceptionsBundle());
       
    }

    @Override
    public void run(ServiceConfiguration config, Environment env) throws Exception {

        // cleans database
        config.getFlywayFactory().build(config.getDataSourceFactory().build(env.metrics(), "db")).clean();

        // re-run flyway migrations
        config.getFlywayFactory().build(config.getDataSourceFactory().build(env.metrics(), "db")).migrate();


        // Datasource factory, jdbi connections
        final DBIFactory factory = new DBIFactory();
        final DBI dbi = factory.build(env, config.getDataSourceFactory(), "postgresql");

        // Register Result Set Mapper for POJO Account with UUID id.
        dbi.registerMapper(new AccountResultMapper());
        dbi.registerMapper(new UserResultMapper());
        dbi.registerMapper(new TransferResultMapper());
        ServiceDAO serviceDAO = new ServiceDAO(dbi);
        UserDAO userDAO = new UserDAO(dbi);
        ServiceResource serviceResource = new ServiceResource(serviceDAO);
        TransferResource transferResource = new TransferResource(serviceDAO);
        
        
        env.jersey().register(new AuthDynamicFeature(new BasicCredentialAuthFilter.Builder<User>()
                .setAuthenticator(new AppBasicAuthenticator(userDAO))
                .setAuthorizer(new UserAuthorizer())
                .setRealm("BASIC-AUTH-REALM")
                .buildAuthFilter()));
        env.jersey().register(RolesAllowedDynamicFeature.class);
        env.jersey().register(new AuthValueFactoryProvider.Binder<>(User.class));

        
        //register your API resource here
        env.jersey().register(serviceResource);
        env.jersey().register(transferResource);
    }
}
