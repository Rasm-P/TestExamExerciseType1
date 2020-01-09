/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import entities.Hobby;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

/**
 *
 * @author rasmu
 */
public class HobbyFacade {

    private static HobbyFacade instance;
    private static EntityManagerFactory emf;

    //Private Constructor to ensure Singleton
    private HobbyFacade() {
    }

    /**
     *
     * @param _emf
     * @return an instance of this facade class.
     */
    public static HobbyFacade getFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new HobbyFacade();
        }
        return instance;
    }

    public List<Hobby> getAllHobbies() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Hobby> query
                    = em.createQuery("SELECT h FROM Hobby h", Hobby.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public Hobby createHobby(Hobby hobby) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(hobby);
            em.getTransaction().commit();
            return hobby;
        } finally {
            em.close();
        }
    }

    public Hobby editHobby(Hobby hobby) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(hobby);
            em.getTransaction().commit();
            return hobby;
        } finally {
            em.close();
        }
    }

    public Hobby removeHobby(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Hobby hobby = em.find(Hobby.class, id);
            em.remove(em.merge(hobby));
            em.getTransaction().commit();
            return hobby;
        } finally {
            em.close();
        }
    }
}
