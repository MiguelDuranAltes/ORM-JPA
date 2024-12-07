package gei.id.tutelado.dao;

import gei.id.tutelado.configuracion.Configuracion;
import gei.id.tutelado.model.Distribuidor;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import java.util.ArrayList;
import java.util.List;

public class DistribuidorDaoJPA implements DistribuidorDao {
    private EntityManagerFactory emf;
    private EntityManager em;

    @Override
    public void setup (Configuracion config) {
        this.emf = (EntityManagerFactory) config.get("EMF");
    }

    /* MO4.1 */
    @Override
    public Distribuidor recuperaPorNombre(String nombre) {
        List<Distribuidor> distribuidores = new ArrayList<>();
        try{
            em = emf.createEntityManager();
            em.getTransaction().begin();

            distribuidores = em.createNamedQuery("Distribuidor.recuperaPorNombre", Distribuidor.class).setParameter("nombre", nombre).getResultList();

            em.getTransaction().commit();
            em.close();
        } catch (Exception ex) {
            if (em != null && em.isOpen()) {
                if (em.getTransaction().isActive()) em.getTransaction().rollback();
                em.close();
                throw(ex);
            }
        }
        return (distribuidores.isEmpty()?null:distribuidores.get(0));
    }

    /* MO4.2 */
    @Override
    public Distribuidor almacena(Distribuidor distribuidor) {
        try{
            em = emf.createEntityManager();
            em.getTransaction().begin();

            em.persist(distribuidor);

            em.getTransaction().commit();
            em.close();
        } catch (Exception ex) {
            if (em != null && em.isOpen()) {
                if (em.getTransaction().isActive()) em.getTransaction().rollback();
                em.close();
                throw(ex);
            }
        }
        return distribuidor;
    }

    /* MO4.3 */
    @Override
    public void elimina(Distribuidor distribuidor) {

        try{
            em = emf.createEntityManager();
            em.getTransaction().begin();

            Distribuidor distribuidortemporal = em.find(Distribuidor.class,distribuidor.getId());

            em.remove(distribuidortemporal);

            em.getTransaction().commit();
            em.close();
        } catch (Exception ex){
            if (em != null && em.isOpen()) {
                if (em.getTransaction().isActive()) em.getTransaction().rollback();
                em.close();
                throw(ex);
            }
        }
    }

    /* MO4.4 */
    @Override
    public Distribuidor modifica(Distribuidor distribuidor) {

        try {
            em = emf.createEntityManager();
            em.getTransaction().begin();

            distribuidor = em.merge(distribuidor);

            em.getTransaction().commit();
            em.close();
        } catch (Exception ex){
            if (em != null && em.isOpen()) {
                if (em.getTransaction().isActive()) em.getTransaction().rollback();
                em.close();
                throw(ex);
            }
        }
        return distribuidor;
    }


}
