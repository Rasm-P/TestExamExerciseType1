/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import dto.HobbyDTO;
import entities.Hobby;
import facades.HobbyFacade;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.Context;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
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
@Path("hobby")
public class HobbyResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.DEV, EMF_Creator.Strategy.CREATE);
    public static final HobbyFacade hobbyFacade = HobbyFacade.getFacade(EMF);

    @Context
    SecurityContext securityContext;
    
    @GET
    @Path("/allHobbies")
    @Produces(MediaType.APPLICATION_JSON)
    public List<HobbyDTO> getAllHobbies() {
        List<Hobby> hobby = hobbyFacade.getAllHobbies();
        List<HobbyDTO> dto = new ArrayList<>();
        for (Hobby h : hobby) {
            dto.add(new HobbyDTO(h));
        }
        return dto;
    }
    
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("admin")
    public HobbyDTO createHobby(Hobby hobby) {
        Hobby newHobby = hobbyFacade.createHobby(hobby);
        return new HobbyDTO(newHobby);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("admin")
    public HobbyDTO editHobby(Hobby hobby) {
        Hobby editHobby = hobbyFacade.editHobby(hobby);
        return new HobbyDTO(editHobby);
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    @RolesAllowed("admin")
    public HobbyDTO deleteHobby(@PathParam("id") Long id) {
        Hobby deletedHobby = hobbyFacade.removeHobby(id);
        return new HobbyDTO(deletedHobby);
    }
}
