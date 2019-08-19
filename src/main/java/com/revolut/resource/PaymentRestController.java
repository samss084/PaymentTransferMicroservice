package com.revolut.resource;

import static com.revolut.dao.PaymentTransferDB.addAmount;
import static com.revolut.dao.PaymentTransferDB.getTotalAmountBySource;
import static com.revolut.dao.PaymentTransferDB.withdrawPayment;
import static com.revolut.util.SourceID.AMAZON_PAY_SOURCE_ID;
import static com.revolut.util.SourceID.GOOGLE_PAY_SOURCE_ID;
import static java.util.Objects.nonNull;

import java.util.UUID;

import javax.annotation.security.PermitAll;
import javax.validation.Valid;
import javax.validation.Validator;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.revolut.dto.Payment;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "Payment operations API", description = "Payment operations API")
@Path("/payment")
@Produces(MediaType.APPLICATION_JSON)
public class PaymentRestController {

	private final Validator validator;

	public PaymentRestController(Validator validator) {
		this.validator = validator;
	}

	@GET
	@Path("/{source-id}")
	@PermitAll
	@ApiOperation(value = "Get total amount by source id")
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Error Occurred while getting data"),
			@ApiResponse(code = 404, message = "Payment Details not found"),
			@ApiResponse(code = 405, message = "Validation exception"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getPaymentById(@PathParam("source-id") Integer sourceId) {
		if (!nonNull(sourceId) || !(sourceId.equals(AMAZON_PAY_SOURCE_ID.getIdValue()) || sourceId.equals(GOOGLE_PAY_SOURCE_ID.getIdValue()))) {
			throw new RuntimeException("Unknown Source with id -" + sourceId);
		}
		return Response.ok(getTotalAmountBySource(sourceId)).build();
	}

	@POST
	@PermitAll
	@ApiOperation(value = "Create payment")
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Error Occurred while getting data"),
			@ApiResponse(code = 404, message = "Payment Details not found"),
			@ApiResponse(code = 405, message = "Validation exception"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createPayment(Payment Payment) {
		try {
			addAmount(Payment);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		return Response.ok(getTotalAmountBySource(AMAZON_PAY_SOURCE_ID.getIdValue())).build();
	}

	@DELETE
	@Path("/{transactionId}")
	@PermitAll
	@ApiOperation(value = "Delete payment details")
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Error Occurred while getting data"),
			@ApiResponse(code = 404, message = "Payment Details not found"),
			@ApiResponse(code = 405, message = "Validation exception"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response removePaymentById(@PathParam("transactionId") UUID transactionId) {
		try {
			withdrawPayment(transactionId);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		return Response.ok(getTotalAmountBySource(AMAZON_PAY_SOURCE_ID.getIdValue())).build();
	}

}
