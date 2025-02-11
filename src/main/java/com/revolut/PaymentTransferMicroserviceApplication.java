package com.revolut;
Do Not Forward
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.revolut.configuration.ApplicationConfiguration;
import com.revolut.resource.PaymentRestController;
import com.revolut.resource.TransferRestController;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;

public class PaymentTransferMicroserviceApplication extends Application<ApplicationConfiguration> {

	private static final Logger LOGGER = LoggerFactory.getLogger(PaymentTransferMicroserviceApplication.class);

	@Override
	public void initialize(Bootstrap<ApplicationConfiguration> bootstrap) {
		bootstrap.addBundle(new SwaggerBundle<ApplicationConfiguration>() {
			@Override
			protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(ApplicationConfiguration configuration) {
				return configuration.swaggerBundleConfiguration;
			}
		});
	}

	@Override
	public void run(ApplicationConfiguration c, Environment e) throws Exception {
		LOGGER.info("Registering REST resources");
		e.jersey().register(new PaymentRestController(e.getValidator()));
		e.jersey().register(new TransferRestController(e.getValidator()));
	}

	public static void main(String[] args) throws Exception {
		new PaymentTransferMicroserviceApplication().run(args);
	}
}
