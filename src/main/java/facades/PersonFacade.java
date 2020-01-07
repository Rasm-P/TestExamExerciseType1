/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import entities.Hobby;
import entities.Person;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

/**
 *
 * @author Rasmus2
 */
public class PersonFacade {

    private static PersonFacade instance;
    private static EntityManagerFactory emf;

    //Private Constructor to ensure Singleton
    private PersonFacade() {
    }

    /**
     *
     * @param _emf
     * @return an instance of this facade class.
     */
    public static PersonFacade getFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new PersonFacade();
        }
        return instance;
    }

    public List<Person> getAllPersons() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Person> query
                    = em.createQuery("SELECT p FROM Person p", Person.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Person> getPersonsByHobby(String hobby) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Person> query
                    = em.createQuery("SELECT p FROM Person p JOIN p.hobbyList h WHERE h.name = :hobbyName", Person.class);
            query.setParameter("hobbyName", hobby);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public Person createPerson(Person p) {
        EntityManager em = emf.createEntityManager();
        try {
            if (p.getAddress().getId() == null) {
                em.persist(p.getAddress());
            }
            for (Hobby hobby : p.getHobbyList()) {
                if (hobby.getId() == null) {
                    em.persist(hobby);
                }
            }
            em.persist(p);
            p = em.merge(p);
            em.getTransaction().commit();
            return p;
        } finally {
            em.close();
        }
    }

    public Person editPerson(Person person) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(person);
            em.getTransaction().commit();
            return person;
        } finally {
            em.close();
        }
    }

    public Person removePerson(Long id) {
        EntityManager em = emf.createEntityManager();
        Person person = em.find(Person.class, id);
        try {
            em.getTransaction().begin();
            em.remove(em.merge(person));
            em.getTransaction().commit();
            return person;
        } finally {
            em.close();
        }
    }

}
