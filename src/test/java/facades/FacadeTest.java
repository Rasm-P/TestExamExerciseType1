package facades;

import entities.Address;
import entities.Hobby;
import entities.Person;
import entities.Role;
import entities.User;
import errorhandling.AuthenticationException;
import java.util.List;
import utils.EMF_Creator;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.EMF_Creator.DbSelector;
import utils.EMF_Creator.Strategy;

//@Disabled
public class FacadeTest {

    private static EntityManagerFactory emf;
    private static AddressFacade addressFacade;
    private static HobbyFacade hobbyFacade;
    private static PersonFacade personFacade;
    private static UserFacade userFacade;

    public FacadeTest() {
    }

    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactory(DbSelector.TEST, Strategy.DROP_AND_CREATE);
        addressFacade = AddressFacade.getFacade(emf);
        hobbyFacade = HobbyFacade.getFacade(emf);
        personFacade = PersonFacade.getFacade(emf);
        userFacade = UserFacade.getUserFacade(emf);
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
            person2.setAddress(address2);

            em.persist(hobby2);
            em.persist(address2);
            em.persist(person2);

            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @AfterEach
    public void tearDown() {
    }

    @Test
    public void testCreateAddress() {
        Address a1 = new Address("new", "new", "new");
        Address a2 = addressFacade.createAddress(a1);
        assertEquals(a1.getId(), a2.getId());
    }

    @Test
    public void testEditAddress() {
        address.setCity("NewCity");
        Address a = addressFacade.editAddress(address);
        assertEquals(a.getCity(), "NewCity");
    }

    @Test
    public void testRemoveAddress() {
        Address a = addressFacade.removeAddress(address2.getId());
        EntityManager em = emf.createEntityManager();
        Address a2;
        try {
            a2 = em.find(Address.class, a.getId());
        } finally {
            em.close();
        }
        assertEquals(a2, null);
    }

    @Test
    public void testGetAllHobbies() {
        List<Hobby> hobbyList = hobbyFacade.getAllHobbies();
        assertEquals(hobbyList.size(), 2);
    }

    @Test
    public void testCreateHobby() {
        Hobby h1 = new Hobby("new", "new");
        Hobby h2 = hobbyFacade.createHobby(h1);
        assertEquals(h1.getId(), h2.getId());
    }

    @Test
    public void testEditHobby() {
        hobby.setDescription("NewDescription");
        Hobby h = hobbyFacade.editHobby(hobby);
        assertEquals(h.getDescription(), "NewDescription");
    }

    @Test
    public void testRemoveHobby() {
        Hobby h = hobbyFacade.removeHobby(hobby2.getId());
        EntityManager em = emf.createEntityManager();
        Hobby h2;
        try {
            h2 = em.find(Hobby.class, h.getId());
        } finally {
            em.close();
        }
        assertEquals(hobbyFacade.getAllHobbies().size(), 1);
        assertEquals(h2, null);
    }

    @Test
    public void testGetAllPersons() {
        List<Person> personList = personFacade.getAllPersons();
        assertEquals(personList.size(), 2);
    }

    @Test
    public void testGetPersonsByHobby() {
        List<Person> personList = personFacade.getPersonsByHobby(hobby.getName());
        assertEquals(personList.size(), 2);
    }

    @Test
    public void testCreatePerson() {
        Person p1 = new Person("new", 1111111, "new", "new");
        p1.setAddress(address);
        p1.addHobby(hobby);
        Person p2 = personFacade.createPerson(p1);
        assertEquals(p1.getId(), p2.getId());
    }

    @Test
    public void testEditPerson() {
        person.setEmail("NewEmail");
        Person p = personFacade.editPerson(person);
        assertEquals(p.getEmail(), "NewEmail");
    }

    @Test
    public void testRemovePerson() {
        Person p = personFacade.removePerson(person2.getId());
        EntityManager em = emf.createEntityManager();
        Person p2;
        try {
            p2 = em.find(Person.class, p.getId());
        } finally {
            em.close();
        }
        assertEquals(personFacade.getAllPersons().size(), 1);
        assertEquals(p2, null);
    }

    @Test
    public void testGetVeryfiedUser() throws AuthenticationException {
        User u = userFacade.getVeryfiedUser(admin.getUserName(), "test");
        assertEquals(u.getUserName(), admin.getUserName());
    }

}
