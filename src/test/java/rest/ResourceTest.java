package rest;

import entities.Address;
import entities.Hobby;
import entities.Person;
import entities.Role;
import entities.User;
import utils.EMF_Creator;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.parsing.Parser;
import java.net.URI;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.UriBuilder;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;

//@Disabled
public class ResourceTest {

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";

    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory emf;

    static HttpServer startServer() {
        ResourceConfig rc = ResourceConfig.forApplication(new ApplicationConfig());
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
    }

    @BeforeAll
    public static void setUpClass() {
        EMF_Creator.startREST_TestWithDB();
        emf = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.TEST, EMF_Creator.Strategy.CREATE);

        httpServer = startServer();
        RestAssured.baseURI = SERVER_URL;
        RestAssured.port = SERVER_PORT;
        RestAssured.defaultParser = Parser.JSON;
    }

    @AfterAll
    public static void closeTestServer() {
        EMF_Creator.endREST_TestWithDB();
        httpServer.shutdownNow();
    }

    User admin = new User("admin", "test");
    Role adminRole = new Role("admin");

    Person person = new Person("ole.o@gmail.com", 34235623, "Ole", "Olsen");
    Hobby hobby = new Hobby("Swimming", "Swimming in warter og in a pool");
    Address address = new Address("Laymans street", "Oletown", "2342");

    Person person2 = new Person("Mads.m@gmail.com", 23445657, "Mads", "Madsen");
    Hobby hobby2 = new Hobby("Tennins", "Hitting a ball with a ketcher");
    Address address2 = new Address("Mads street", "Madstown", "3453");

    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        try {
            admin.addRole(adminRole);

            em.getTransaction().begin();
            em.createQuery("delete from User").executeUpdate();
            em.createQuery("delete from Role").executeUpdate();
            em.createQuery("delete from Person").executeUpdate();
            em.createQuery("delete from Address").executeUpdate();
            em.createQuery("delete from Hobby").executeUpdate();

            em.persist(adminRole);
            em.persist(admin);

            person.addHobby(hobby);
            person.setAddress(address);
            em.persist(hobby);
            em.persist(address);
            em.persist(person);

            person2.addHobby(hobby);
            person2.addHobby(hobby2);
            address2.addPerson(person2);
            person2.setAddress(address2);

            em.persist(hobby2);
            em.persist(address2);
            em.persist(person2);

            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    private static String securityToken;

    private static void login(String role, String password) {
        String json = String.format("{username: \"%s\", password: \"%s\"}", role, password);
        securityToken = given()
                .contentType("application/json")
                .body(json)
                //.when().post("/api/login")
                .when().post("/login")
                .then()
                .extract().path("token");
        System.out.println("TOKEN ---> " + securityToken);
    }

    private void logOut() {
        securityToken = null;
    }

}
