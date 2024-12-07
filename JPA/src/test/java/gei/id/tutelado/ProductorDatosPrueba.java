package gei.id.tutelado;

import gei.id.tutelado.configuracion.Configuracion;
import gei.id.tutelado.model.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ProductorDatosPrueba {

    private EntityManagerFactory emf=null;

    public Distribuidor d0, d1, d2, d3;
    public List<Distribuidor> listaD;

    public Marca m0, m1, m2,m3, m4;
    public List<Marca> listaM;

    public ProductosLacteos p1,p2,p3,p4,p5,p6;
    public List<ProductosLacteos> listaP;

    public void Setup (Configuracion config) {
        this.emf=(EntityManagerFactory) config.get("EMF");
    }

    public void creaProductoLeche(){
        Leche l1;
        l1 = new Leche();
        l1.setNombre("President");
        l1.setFechaExpiracion(LocalDateTime.now().plusYears(1).truncatedTo(ChronoUnit.SECONDS));
        l1.setOrigen("Francia");
        l1.setPrecio(1.1);
        l1.setVolumen(1F);
        l1.setTipo("Desnatada");
        this.p1 = l1;

        Leche l2;
        l2= new Leche();
        l2.setNombre("Feiraco");
        l2.setFechaExpiracion(LocalDateTime.now().plusYears(1).truncatedTo(ChronoUnit.SECONDS));
        l2.setOrigen("Galicia");
        l2.setPrecio(1.0);
        l2.setVolumen(1F);
        l2.setTipo("Entera");
        this.p2 = l2;

        Leche l3;
        l3 = new Leche();
        l3.setNombre("Central Lechera Asturiana");
        l3.setFechaExpiracion(LocalDateTime.now().plusYears(1).truncatedTo(ChronoUnit.SECONDS));
        l3.setOrigen("Asturias");
        l3.setPrecio(1.05);
        l3.setVolumen(1.5F);
        l3.setTipo("SemiDesnatada");
        this.p3 = l3;

        this.listaP = new ArrayList<>();
        listaP.add(0,p1);
        listaP.add(1,p2);
        listaP.add(2,p3);

        Queso q1;
        q1= new Queso();
        q1.setNombre("Arzua Ulloa");
        q1.setFechaExpiracion(LocalDateTime.now().plusYears(1).truncatedTo(ChronoUnit.SECONDS));
        q1.setOrigen("Galicia");
        q1.setPrecio(10.0);
        q1.setTipo("Tetilla");
        q1.setMaduracion("No");
        q1.setPeso(1.0F);
        this.p4 = q1;

        Queso q2;
        q2= new Queso();
        q2.setNombre("Roquefort");
        q2.setFechaExpiracion(LocalDateTime.now().plusYears(1).truncatedTo(ChronoUnit.SECONDS));
        q2.setOrigen("Francia");
        q2.setPrecio(25.0);
        q2.setTipo("Azul");
        q2.setMaduracion("Si");
        q2.setPeso(1.5F);
        this.p5 = q2;

        Queso q3;
        q3 = new Queso();
        q3.setNombre("Mozzarella");
        q3.setFechaExpiracion(LocalDateTime.now().plusYears(1).truncatedTo(ChronoUnit.SECONDS));
        q3.setOrigen("Italia");
        q3.setPrecio(15.0);
        q3.setTipo("Buffalla");
        q3.setMaduracion("No");
        q3.setPeso(1.0F);
        this.p6 = q3;

        listaP.add(3,p4);
        listaP.add(4,p5);
        listaP.add(5,p6);
    }

    public void creaMarcas(){
        this.m0 = new Marca();
        this.m0.setNombre("Larsa");
        this.m0.setFechaFundacion(LocalDateTime.of(1980, 1, 1, 0, 0));
        this.m0.setEsloganes(List.of("Larsa, tu marca de confianza"));

        this.m1 = new Marca();
        this.m1.setNombre("Danone");
        this.m1.setFechaFundacion(LocalDateTime.of(1970, 1, 1, 0, 0));
        this.m1.setEsloganes(List.of("Crece con Danone", "La vida es más fácil con un Danone"));

        this.m2 = new Marca();
        this.m2.setNombre("Nestle");
        this.m2.setFechaFundacion(LocalDateTime.of(1960, 1, 1, 0, 0));
        this.m2.setEsloganes(List.of("Nestle, la marca de la felicidad"));

        this.m3 = new Marca();
        this.m3.setNombre("President");
        this.m3.setFechaFundacion(LocalDateTime.of(1940, 1, 1, 0, 0).truncatedTo(ChronoUnit.SECONDS));
        this.m3.setEsloganes(List.of("President, en confianza"));

        this.m4 = new Marca();
        this.m4.setNombre("Feiraco");
        this.m4.setFechaFundacion(LocalDateTime.of(1990, 1, 1, 0, 0));
        this.m4.setEsloganes(List.of("Feiraco, da nosa terra"));

        this.listaM = new ArrayList<>();
        this.listaM.add(0, m0);
        this.listaM.add(1, m1);
        this.listaM.add(2, m2);
        this.listaM.add(3, m3);
        this.listaM.add(4, m4);


    }

    public void creaDistribuidores(){
        this.d0 = new Distribuidor();
        this.d0.setNombre("ManoloDelivery");
        this.d0.setZonaDistribucion("Galicia");

        this.d1 = new Distribuidor();
        this.d1.setNombre("PacoExpress");
        this.d1.setZonaDistribucion("Galicia");

        this.d2 = new Distribuidor();
        this.d2.setNombre("JuanitoTransportes");
        this.d2.setZonaDistribucion("Cantabria");

        this.d3 = new Distribuidor();
        this.d3.setNombre("PepeMensajeria");
        this.d3.setZonaDistribucion("Madrid");

        this.listaD = new ArrayList<>();
        this.listaD.add(0, d0);
        this.listaD.add(1, d1);
        this.listaD.add(2, d2);
        this.listaD.add(3, d3);
    }

    public void creaMarcasConDistribuidores(){
        this.creaMarcas();
        this.creaDistribuidores();
        this.m0.nuevoDistribuidor(this.d0);
        this.m1.nuevoDistribuidor(this.d1);
        this.m0.nuevoDistribuidor(this.d2);
        this.m0.nuevoDistribuidor(this.d3);
    }

    public void asociarMarcasaProdctos(){
        this.creaProductoLeche();
        this.creaMarcas();
        this.p1.asociarMarcas(this.m3);
        this.p2.asociarMarcas(this.m4);
    }

    public void almacenaMarcas(){
        EntityManager em=null;
        try{
            em = emf.createEntityManager();
            em.getTransaction().begin();

            Iterator<Marca> itM = this.listaM.iterator();
            while (itM.hasNext()) {
                Marca m = itM.next();
                em.persist(m);
                // DESCOMENTAR SE A PROPAGACION DO PERSIST NON ESTA ACTIVADA
				/*
				Iterator<EntradaLog> itEL = u.getEntradasLog().iterator();
				while (itEL.hasNext()) {
					em.persist(itEL.next());
				}
				*/
            }
            em.getTransaction().commit();
            em.close();
        } catch (Exception ex){
            if (em!=null && em.isOpen()) {
                if (em.getTransaction().isActive()) em.getTransaction().rollback();
                em.close();
                throw (ex);
            }
        }
    }

    public void almacenaProductos(){
        EntityManager em=null;
        try{
            em = emf.createEntityManager();
            em.getTransaction().begin();

            Iterator<ProductosLacteos> itP = this.listaP.iterator();
            while (itP.hasNext()) {
                ProductosLacteos p = itP.next();
                em.persist(p);
            }
            em.getTransaction().commit();
            em.close();
        } catch (Exception ex){
            if (em!=null && em.isOpen()) {
                if (em.getTransaction().isActive()) em.getTransaction().rollback();
                em.close();
                throw (ex);
            }
        }
    }

    public void limpiaBD () {
        EntityManager em=null;
        try {
            em = emf.createEntityManager();
            em.getTransaction().begin();

            Iterator <Marca> itM = em.createQuery("SELECT m from Marca m", Marca.class).getResultList().iterator();
            Iterator <ProductosLacteos> itP = em.createQuery("SELECT p from ProductosLacteos p", ProductosLacteos.class).getResultList().iterator();
            while (itM.hasNext()) em.remove(itM.next());
			/*
			// Non é necesario porque establecemos  propagacion do remove
			// Se desactivamos propagación, descomentar
			Iterator <EntradaLog> itL = em.createQuery("SELECT e from EntradaLog e", EntradaLog.class).getResultList().iterator();
			while (itL.hasNext()) em.remove(itL.next());
			*/
            while (itP.hasNext()) em.remove(itP.next());

            em.createNativeQuery("UPDATE tabla_ids SET ultimo_valor_id=0 WHERE nombre_id='idMarca'" ).executeUpdate();
            em.createNativeQuery("UPDATE tabla_ids SET ultimo_valor_id=0 WHERE nombre_id='idDistribuidor'" ).executeUpdate();
            em.createNativeQuery("UPDATE tabla_ids SET ultimo_valor_id=0 WHERE nombre_id='idProductoLacteo'" ).executeUpdate();

            em.getTransaction().commit();
            em.close();
        } catch (Exception e) {
            if (em!=null && em.isOpen()) {
                if (em.getTransaction().isActive()) em.getTransaction().rollback();
                em.close();
                throw (e);
            }
        }
    }
}
