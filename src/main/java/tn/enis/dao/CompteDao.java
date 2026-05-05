package tn.enis.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tn.enis.entity.Compte;

@Repository
public class CompteDao {

    @PersistenceContext
    private EntityManager entityManager;

    public void save(Compte compte) {
        entityManager.persist(compte);
    }

    public void update(Compte compte) {
        entityManager.merge(compte);
    }

    public void remove(Compte compte) {
        entityManager.remove(compte);
    }

    public Compte findById(Integer rib) {
        return entityManager.find(Compte.class, rib);
    }

    public List<Compte> findAll() {
        return entityManager.createQuery("select c from Compte c", Compte.class).getResultList();
    }

    public List<Compte> findAllByClientCin(String cin) {
        return entityManager.createQuery("select c from Compte c where c.client.cin = :cin", Compte.class)
                .setParameter("cin", cin)
                .getResultList();
    }
}