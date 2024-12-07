package gei.id.tutelado.dao;

import gei.id.tutelado.configuracion.Configuracion;
import gei.id.tutelado.model.ProductosLacteos;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ProductosLacteosDaoJPA implements ProductosLacteosDao{

    private EntityManagerFactory emf;
    private EntityManager em;

    @Override
    public void setup(Configuracion conf) {
        this.emf = (EntityManagerFactory) conf.get("EMF");
    }

    @Override
    public ProductosLacteos recuperarPorNombre(String nombre) {
        List<ProductosLacteos> productos = new ArrayList<>();

        try{
            em = emf.createEntityManager();
            em.getTransaction().begin();

            productos = em.createNamedQuery("ProductosLacteos.recuperaPorNombre",ProductosLacteos.class).setParameter("nombre",nombre).getResultList();

            em.getTransaction().commit();
            em.close();
        }
        catch (Exception e){
            if (em!=null && em.isOpen()) {
                if (em.getTransaction().isActive())
                    em.getTransaction().rollback();
                em.close();
                throw(e);
            }
        }
        return (productos.isEmpty()?null:productos.get(0));
    }

    @Override
    public ProductosLacteos alta(ProductosLacteos prod) {

        try{
            em = emf.createEntityManager();
            em.getTransaction().begin();

            em.persist(prod);

            em.getTransaction().commit();
            em.close();
        }
        catch (Exception e){
            if (em!=null && em.isOpen()) {
                if (em.getTransaction().isActive())
                    em.getTransaction().rollback();
                em.close();
                throw(e);
            }
        }
        return prod;
    }

    @Override
    public void eliminar(ProductosLacteos prod) {

        try {
            em = emf.createEntityManager();
            em.getTransaction().begin();

            ProductosLacteos prodtemporal = em.find(ProductosLacteos.class,prod.getId());
            em.remove(prodtemporal);

            em.getTransaction().commit();
            em.close();
        }
        catch (Exception e){
            if (em!=null && em.isOpen()) {
                if (em.getTransaction().isActive())
                    em.getTransaction().rollback();
                em.close();
                throw(e);
            }
        }
    }

    @Override
    public ProductosLacteos actualizar(ProductosLacteos prod) {

        try {
            em = emf.createEntityManager();
            em.getTransaction().begin();

            prod = em.merge(prod);

            em.getTransaction().commit();
            em.close();
        }
        catch (Exception e){
            if (em!=null && em.isOpen()) {
                if (em.getTransaction().isActive())
                    em.getTransaction().rollback();
                em.close();
                throw(e);
            }
        }
        return prod;
    }

    @Override
    public List<String> recuperarNombreProductosSinMarca() {
        List<String> productos = new ArrayList<>();

        try {
            em = emf.createEntityManager();
            em.getTransaction().begin();

            productos = em.createQuery("SELECT p.nombre FROM ProductosLacteos p LEFT OUTER JOIN p.marcas m WHERE m IS NULL",String.class).getResultList();

            em.getTransaction().commit();
            em.close();
        } catch (Exception e){
            if (em!=null && em.isOpen()) {
                if (em.getTransaction().isActive())
                    em.getTransaction().rollback();
                em.close();
                throw(e);
            }
        }
        return  productos;
    }

    @Override
    public List<ProductosLacteos> recuperarNombreProductosPorFechaFundacionMarca(LocalDateTime fecha) {
        List<ProductosLacteos> productos = new ArrayList<>();

        try {
            em = emf.createEntityManager();
            em.getTransaction().begin();

            productos = em.createQuery("SELECT p " +
                            "FROM ProductosLacteos p " +
                            "WHERE EXISTS (" +
                            "    SELECT m " +
                            "    FROM Marca m " +
                            "    WHERE m MEMBER OF p.marcas " +
                            "    AND m.fechaFundacion < :fecha" +
                            ")", ProductosLacteos.class)
                    .setParameter("fecha", fecha)
                    .getResultList();


            em.getTransaction().commit();
            em.close();
        } catch (Exception e){
            if (em!=null && em.isOpen()) {
                if (em.getTransaction().isActive())
                    em.getTransaction().rollback();
                em.close();
                throw(e);
            }
        }
        return productos;
    }


}
