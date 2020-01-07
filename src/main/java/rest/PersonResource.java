/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import dto.PersonDTO;
import entities.Person;
import facades.PersonFacade;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@OpenAPIDefinition(
        info = @Info(
                title = "TestExamExerciseType1",
                version = "0.1",
                description = "Backend of the Sem3 Exam project"
        ),
        tags = {
            @Tag(name = "Person endpoint", description = "Resource used for reading, adding, editing and deleting person entities")
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
@Path("person")
public class PersonResource {
    
    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.DEV, EMF_Creator.Strategy.CREATE);
    public static final PersonFacade personFacade = PersonFacade.getFacade(EMF);
    
    @Context
    SecurityContext securityContext;
    
    @GET
    @Path("/allperons")
    @Produces(MediaType.APPLICATION_JSON)
    public List<PersonDTO> getAllPersons() {
        List<Person> person = personFacade.getAllPersons();
        List<PersonDTO> dto = new ArrayList<>();
        for (Person p : person) {
            dto.add(new PersonDTO(p));
        }
        return dto;
    }
    
    @GET
    @Path("/allperonsbyhobby/{hobby}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<PersonDTO> getPersonsByHobby(@PathParam("hobby") String hobby) {
        List<Person> person = personFacade.getPersonsByHobby(hobby);
        List<PersonDTO> dto = new ArrayList<>();
        for (Person p : person) {
            dto.add(new PersonDTO(p));
        }
        return dto;
    }
    
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("admin")
    public PersonDTO createPerson(Person person) {
        Person newPerson = personFacade.createPerson(person);
        return new PersonDTO(newPerson);
    }
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("admin")
    public PersonDTO editPerson(Person person) {
        Person editPerson = personFacade.editPerson(person);
        return new PersonDTO(editPerson);
    }
    
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    @RolesAllowed("admin")
    public PersonDTO deletePerson(@PathParam("id") Long id) {
        Person deletedPerson = personFacade.removePerson(id);
        return new PersonDTO(deletedPerson);
    }
    
}
