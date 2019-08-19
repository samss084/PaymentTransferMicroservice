package com.revolut.resource;

import static com.revolut.dao.PaymentTransferDB.transferAllAmount;
import static com.revolut.dao.PaymentTransferDB.transferAmount;

import javax.annotation.Nonnull;
import javax.annotation.security.PermitAll;
import javax.validation.Validator;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "Transfer management API", description = "Transfer management API")
@Path("/transfer")
@Produces(MediaType.APPLICATION_JSON)
public class TransferRestController {

	private final Validator validator;

	public TransferRestController(Validator validator) {
		this.validator = validator;
	}

	@PUT
	@PermitAll
	@ApiOperation(value = "Transfer all amount")
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Error Occurred while getting data"),
			@ApiResponse(code = 404, message = "Payment Details not found"),
			@ApiResponse(code = 405, message = "Validation exception"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response transfer() {
		return Response.ok(transferAllAmount()).build();
	}

	@PUT
	@Path("/{amount}")
	@PermitAll
	@ApiOperation(value = "Transfer amount")
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Error Occurred while getting data"),
			@ApiResponse(code = 404, message = "Payment Details not found"),
			@ApiResponse(code = 405, message = "Validation exception"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response transfer(@Nonnull@PathParam("amount") Integer amount) {
		return Response.ok(transferAmount(amount)).build();
	}
}
