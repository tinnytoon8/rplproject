/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import controller.exceptions.NonexistentEntityException;
import controller.exceptions.PreexistingEntityException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import model.Mahasiswa;

/**
 *
 * @author User
 */
public class MahasiswaJpaController implements Serializable {

    public MahasiswaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Mahasiswa mahasiswa) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(mahasiswa);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findMahasiswa(mahasiswa.getNpm()) != null) {
                throw new PreexistingEntityException("Mahasiswa " + mahasiswa + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Mahasiswa mahasiswa) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            mahasiswa = em.merge(mahasiswa);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = mahasiswa.getNpm();
                if (findMahasiswa(id) == null) {
                    throw new NonexistentEntityException("The mahasiswa with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Mahasiswa mahasiswa;
            try {
                mahasiswa = em.getReference(Mahasiswa.class, id);
                mahasiswa.getNpm();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The mahasiswa with id " + id + " no longer exists.", enfe);
            }
            em.remove(mahasiswa);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Mahasiswa> findMahasiswaEntities() {
        return findMahasiswaEntities(true, -1, -1);
    }

    public List<Mahasiswa> findMahasiswaEntities(int maxResults, int firstResult) {
        return findMahasiswaEntities(false, maxResults, firstResult);
    }

    private List<Mahasiswa> findMahasiswaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Mahasiswa.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Mahasiswa findMahasiswa(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Mahasiswa.class, id);
        } finally {
            em.close();
        }
    }

    public int getMahasiswaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Mahasiswa> rt = cq.from(Mahasiswa.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public void destroy(String npm) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public Mahasiswa findMahasiswa(String value) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
