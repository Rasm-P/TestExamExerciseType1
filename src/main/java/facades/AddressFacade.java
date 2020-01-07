/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import entities.Address;
import entities.Person;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author rasmu
 */
public class AddressFacade {

    private static AddressFacade instance;
    private static EntityManagerFactory emf;

    //Private Constructor to ensure Singleton
    private AddressFacade() {
    }

    /**
     *
     * @param _emf
     * @return an instance of this facade class.
     */
    public static AddressFacade getFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new AddressFacade();
        }
        return instance;
    }

    public Address createAddress(Address address) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(address);
            em.getTransaction().commit();
            return address;
        } finally {
            em.close();
        }
    }

    public Address editAddress(Address address) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(address);
            em.getTransaction().commit();
            return address;
        } finally {
            em.close();
        }
    }

    public Address removeAddress(Long id) {

        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Address address = em.find(Address.class, id);

            for (Person p : PersonFacade.getFacade(emf).getAllPersons()) {
                if (p.getAddress().getId().equals(address.getId())) {
                    p.setAddress(null);
                    em.merge(p);
                }
            }

            em.remove(em.merge(address));
            em.getTransaction().commit();
            return address;
        } finally {
            em.close();
        }
    }

}
