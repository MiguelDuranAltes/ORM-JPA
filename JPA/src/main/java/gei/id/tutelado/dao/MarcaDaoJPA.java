package gei.id.tutelado.dao;

import gei.id.tutelado.configuracion.Configuracion;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import gei.id.tutelado.model.Marca;
import org.hibernate.LazyInitializationException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MarcaDaoJPA implements MarcaDao {

    private EntityManagerFactory emf;
    private EntityManager em;

    @Override
    public void setup (Configuracion config) {
        this.emf = (EntityManagerFactory) config.get("EMF");
    }

    /* MO4.1 */
    @Override
    public Marca recuperaPorNombre(String nombre) {
            List<Marca> marcas = new ArrayList<>();
            try{
                em = emf.createEntityManager();
                em.getTransaction().begin();

                marcas = em.createNamedQuery("Marca.recuperaPorNombre", Marca.class).setParameter("nombre", nombre).getResultList();

                em.getTransaction().commit();
                em.close();
            } catch (Exception ex) {
                if (em != null && em.isOpen()) {
                    if (em.getTransaction().isActive()) em.getTransaction().rollback();
                    em.close();
                    throw(ex);
                }
            }
        return (marcas.isEmpty()?null:marcas.get(0));
    }


    /* MO4.2 */
    @Override
    public Marca almacena(Marca marca) {

        try{
            em = emf.createEntityManager();
            em.getTransaction().begin();

            em.persist(marca);

            em.getTransaction().commit();
            em.close();
        } catch (Exception ex) {
            if (em != null && em.isOpen()) {
                if (em.getTransaction().isActive()) em.getTransaction().rollback();
                em.close();
                throw(ex);
            }
        }
        return marca;
    }

    /* MO4.3 */
    @Override
    public void elimina(Marca marca) {
        try{
            em = emf.createEntityManager();
            em.getTransaction().begin();

            Marca marcaTmp = em.find(Marca.class, marca.getId());
            em.remove(marcaTmp);

            em.getTransaction().commit();
            em.close();

        } catch (Exception ex) {
            if (em != null && em.isOpen()) {
                if (em.getTransaction().isActive()) em.getTransaction().rollback();
                em.close();
                throw (ex);
            }
        }
    }

    /* MO4.4 */
    @Override
    public Marca modifica(Marca marca) {
            try{
                em = emf.createEntityManager();
                em.getTransaction().begin();

                marca = em.merge(marca);

                em.getTransaction().commit();
                em.close();
            } catch (Exception ex) {
                if (em != null && em.isOpen()) {
                    if (em.getTransaction().isActive()) em.getTransaction().rollback();
                    em.close();
                    throw(ex);
            }
        }
        return marca;
    }

    @Override
    public Marca recuperaDistribuidores(Marca marca) {
        // Devuelve el objeto marca con la coleccion de distribuidores cargada (si no lo estaba ya)

        try {
            em = emf.createEntityManager();
            em.getTransaction().begin();

            try {
                marca.getDistribuidores().size();

            } catch (Exception ex2) {
                if (ex2 instanceof LazyInitializationException)

                {

                    marca = em.merge(marca);
                    marca.getDistribuidores().size();

                } else {
                    throw ex2;
                }
            }
            em.getTransaction().commit();
            em.close();
        }
        catch (Exception ex ) {
            if (em!=null && em.isOpen()) {
                if (em.getTransaction().isActive()) em.getTransaction().rollback();
                em.close();
                throw(ex);
            }
        }

        return (marca);
    }

    /* MO4.6.a */
    @Override
    public List<Marca> recuperaPorZonaDistribucion (String zona) {

        List<Marca> marcas = new ArrayList<>();

        try{

            em = emf.createEntityManager();
            em.getTransaction().begin();

            marcas = em.createQuery("SELECT DISTINCT m FROM Marca m INNER JOIN m.distribuidores d WHERE " +
                    "d.zonaDistribucion =:zona", Marca.class).setParameter("zona", zona)
                    .getResultList();

            em.getTransaction().commit();
            em.close();


        }catch(Exception ex){
            if (em != null && em.isOpen()) {
                if (em.getTransaction().isActive()) em.getTransaction().rollback();
                em.close();
                throw(ex);
            }        }
        return marcas;
    }

    /* MO4.6.d */
    @Override
    public int contarMarcasRecientes(LocalDateTime fechaFundacion){
        Long marcasRecientes = null;
        try{
            em = emf.createEntityManager();
            em.getTransaction().begin();

            marcasRecientes = em.createQuery("SELECT COUNT(m) FROM Marca m WHERE " +
                    "m.fechaFundacion> :fechaFundacion", Long.class).setParameter("fechaFundacion",fechaFundacion).getSingleResult();

            em.getTransaction().commit();
            em.close();

        }catch (Exception ex){
            if (em != null && em.isOpen()) {
                if (em.getTransaction().isActive()) em.getTransaction().rollback();
                em.close();
                throw(ex);
            }
        }
        return marcasRecientes!=null?marcasRecientes.intValue():0;
    }

}
