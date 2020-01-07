/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import dto.AddressDTO;
import dto.HobbyDTO;
import entities.Address;
import facades.AddressFacade;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.Context;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import utils.EMF_Creator;

/**
 * REST Web Service
 *
 * @author rasmu
 */
@OpenAPIDefinition(
        info = @Info(
                title = "TestExamExerciseType1",
                version = "0.1",
                description = "Backend of the Sem3 Exam project"
        ),
        tags = {
            @Tag(name = "Address endpoint", description = "Resource used for adding, editing and deleting address entities")
        },
        servers = {
            @Server(
                    description = "For Local host testing",
                    url = "http://localhost:8080/TestExamExerciseType1"
            ),
            @Server(
                    description = "Server API",
                    url = "https://barfodpraetorius.dk/TestExamExerciseType1"
            )

        }
)
@Path("address")
public class AddressResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.DEV, EMF_Creator.Strategy.CREATE);
    public static final AddressFacade addressFacade = AddressFacade.getFacade(EMF);

    @Context
    SecurityContext securityContext;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("admin")
    @Operation(summary = "Endpoint for admin roles to create an address",
            tags = {"Address endpoint"},
            responses = {
                @ApiResponse(
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = HobbyDTO.class))),
                @ApiResponse(responseCode = "200", description = "An address was created"),
                @ApiResponse(responseCode = "400", description = "User token invalid or not authorized")})
    public AddressDTO createAddress(Address address) {
        Address newAddress = addressFacade.createAddress(address);
        return new AddressDTO(newAddress);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("admin")
    @Operation(summary = "Endpoint for admin roles to edit an address",
            tags = {"Address endpoint"},
            responses = {
                @ApiResponse(
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = HobbyDTO.class))),
                @ApiResponse(responseCode = "200", description = "An address was edited"),
                @ApiResponse(responseCode = "400", description = "User token invalid or not authorized")})
    public AddressDTO editAddress(Address address) {
        Address editAddress = addressFacade.editAddress(address);
        return new AddressDTO(editAddress);
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    @RolesAllowed("admin")
    @Operation(summary = "Endpoint for admin roles to delete an address",
            tags = {"Address endpoint"},
            responses = {
                @ApiResponse(
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = HobbyDTO.class))),
                @ApiResponse(responseCode = "200", description = "An address was deleted"),
                @ApiResponse(responseCode = "400", description = "User token invalid or not authorized")})
    public AddressDTO deleteAddress(@PathParam("id") Long id) {
        Address deletedAddress = addressFacade.removeAddress(id);
        return new AddressDTO(deletedAddress);
    }
}
