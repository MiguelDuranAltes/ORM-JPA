package gei.id.tutelado;

import gei.id.tutelado.configuracion.Configuracion;
import gei.id.tutelado.configuracion.ConfiguracionJPA;
import gei.id.tutelado.dao.DistribuidorDao;
import gei.id.tutelado.dao.DistribuidorDaoJPA;
import gei.id.tutelado.dao.MarcaDao;
import gei.id.tutelado.dao.MarcaDaoJPA;
import gei.id.tutelado.model.Distribuidor;
import gei.id.tutelado.model.Marca;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.LazyInitializationException;
import org.junit.*;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

public class P02_Distribuidores {
    private Logger log = LogManager.getLogger("gei.id.tutelado");

    private static ProductorDatosPrueba productorDatos = new ProductorDatosPrueba();

    private static Configuracion cfg;
    private static MarcaDao marDao;
    private static DistribuidorDao disDao;

    @Rule
    public TestRule watcher = new TestWatcher() {
        protected void starting(Description description) {
            log.info("");
            log.info("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
            log.info("Iniciando test: " + description.getMethodName());
            log.info("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        }
        protected void finished(Description description) {
            log.info("");
            log.info("-----------------------------------------------------------------------------------------------------------------------------------------");
            log.info("Finalizado test: " + description.getMethodName());
            log.info("-----------------------------------------------------------------------------------------------------------------------------------------");
        }
    };

    @BeforeClass
    public static void init(){
        cfg = new ConfiguracionJPA();
        cfg.start();

        marDao = new MarcaDaoJPA();
        disDao = new DistribuidorDaoJPA();

        marDao.setup(cfg);
        disDao.setup(cfg);

        productorDatos = new ProductorDatosPrueba();
        productorDatos.Setup(cfg);
    }

    @AfterClass
    public static void endclose(){
        cfg.endUp();
    }

    @Before
    public void setUp(){
        log.info("");
        log.info("Limpiando BD --------------------------------------------------------------------------------------------");
        productorDatos.limpiaBD();
    }

    @After
    public void tearDown(){
    }

    @Test
    public void test01_Recuperacion() {

        Distribuidor d;

        log.info("");
        log.info("Configurando situación de partida do test -----------------------------------------------------------------------");

        productorDatos.creaMarcasConDistribuidores();
        productorDatos.almacenaMarcas();

        log.info("");
        log.info("Inicio del test --------------------------------------------------------------------------------------------------");
        log.info("Objetivo: Prueba de recuperación (por nombre) de distribuidor sueltos\n"
                + "\t\t\t\t Casos contemplados:\n"
                + "\t\t\t\t a) Recuperación por nombre existente\n"
                + "\t\t\t\t b) Recuperacion por nombre inexistente\n");

        // Situación de partida:
        // m0 desligado    	

        log.info("Probando recuperacion por nombre EXISTENTE --------------------------------------------------");

        d = disDao.recuperaPorNombre(productorDatos.d0.getNombre());
        Assert.assertEquals(productorDatos.d0.getNombre(),      d.getNombre());
        Assert.assertEquals(productorDatos.d0.getZonaDistribucion(),     d.getZonaDistribucion());

        log.info("");
        log.info("Probando recuperacion por nombre INEXISTENTE -----------------------------------------------");

        d = disDao.recuperaPorNombre("abcdefgh");
        Assert.assertNull (d);

    }

    @Test
    public void test02_Alta() {

        log.info("");
        log.info("Configurando situación de partida del test -----------------------------------------------------------------------");

        productorDatos.creaMarcas();
        productorDatos.almacenaMarcas();
        productorDatos.creaDistribuidores();

        log.info("");
        log.info("Inicio del test --------------------------------------------------------------------------------------------------");
        log.info("Objetivo: Prueba de guardado en la BD de distribuidores sueltos\n"
        + "\t\t\t\t Casos contemplados:\n"
                + "\t\t\t\t a) Primer distribuidor vinculado a una marca\n"
                + "\t\t\t\t b) Nuevo distribuidor para una marca con distribuidores previos\n");

        // Situación de partida:
        // m1 ligado    	
        // d0, d1 transitorios

        productorDatos.m1.nuevoDistribuidor(productorDatos.d0);

        log.info("");
        log.info("Guardando primer distribuidor de una marca --------------------------------------------------------------------");
        Assert.assertNull(productorDatos.d0.getId());
        disDao.almacena(productorDatos.d0);
        Assert.assertNotNull(productorDatos.d0.getId());

        productorDatos.m1.nuevoDistribuidor(productorDatos.d1);

        log.info("");
        log.info("Guardando segundo distribuidor de una marca --------------------------------------------------------------------");
        Assert.assertNull(productorDatos.d1.getId());
        disDao.almacena(productorDatos.d1);
        Assert.assertNotNull(productorDatos.d1.getId());

    }

    @Test
    public void test03_Eliminacion() {

        log.info("");
        log.info("Configurando situación de partida del test -----------------------------------------------------------------------");

        productorDatos.creaMarcasConDistribuidores();
        productorDatos.almacenaMarcas();


        log.info("");
        log.info("Inicio del test --------------------------------------------------------------------------------------------------");
        log.info("Objetivo: Prueba de eliminación de distribuidores sueltos (asignados a una marca)\n");

        // Situación de partida:
        // d0 ligado

        Assert.assertNotNull(disDao.recuperaPorNombre(productorDatos.d0.getNombre()));
        disDao.elimina(productorDatos.d0);
        Assert.assertNull(disDao.recuperaPorNombre(productorDatos.d0.getNombre()));
    }

    @Test
    public void test04_Modificacion() {
        Distribuidor d1, d2;
        String nuevaZona;

        log.info("");
        log.info("Configurando situación de partida del test -----------------------------------------------------------------------");

        productorDatos.creaMarcasConDistribuidores();
        productorDatos.almacenaMarcas();

        log.info("");
        log.info("Inicio del test --------------------------------------------------------------------------------------------------");
        log.info("Objetivo: Prueba de modificación de la información básica de un distribuidor\n");

        // Situación de partida:
        // m0 desligado  

        nuevaZona = "Zona modificada";

        d1 = disDao.recuperaPorNombre(productorDatos.d0.getNombre());

        Assert.assertNotEquals(nuevaZona, d1.getZonaDistribucion());
        d1.setZonaDistribucion(nuevaZona);

        disDao.modifica(d1);

        d2 = disDao.recuperaPorNombre(productorDatos.d0.getNombre());
        Assert.assertEquals (nuevaZona, d2.getZonaDistribucion());

    }

    @Test
    public void test05_LAZY() {

        Marca m;
        Distribuidor d;
        boolean excepcion;

        log.info("");
        log.info("Configurando situación de partida del test -----------------------------------------------------------------------");

        productorDatos.creaMarcasConDistribuidores();
        productorDatos.almacenaMarcas();


        log.info("Inicio del test --------------------------------------------------------------------------------------------------");
        log.info("Objetivo: Prueba da recuperación de propiedades LAZY\n"
                + "\t\t\t\t Casos contemplados:\n"
                + "\t\t\t\t a) Recuperación de marca con colección (LAZY) de distribuidores \n"
                + "\t\t\t\t b) Carga forzada de colección LAZY de dicha coleccion\n"
                + "\t\t\t\t c) Recuperacion de distribuidor con referencia (EAGER) a usuario\n");

        // Situación de partida:
        // m1, d1 desligados

        log.info("Probando (excepcion tras) recuperacion LAZY ---------------------------------------------------------------------");

        m = marDao.recuperaPorNombre(productorDatos.m1.getNombre());

        log.info("Acceso a distribuidores de marcas");
        try	{
            Assert.assertEquals(1, m.getDistribuidores().size());
            Assert.assertEquals(productorDatos.d1, m.getDistribuidores().get(0));
            excepcion=false;
        } catch (LazyInitializationException ex) {
            excepcion=true;
            log.info(ex.getClass().getName());
        }
        Assert.assertTrue(excepcion);

        log.info("");
        log.info("Probando carga forzada de coleccion LAZY ------------------------------------------------------------------------");

        m = marDao.recuperaPorNombre(productorDatos.m1.getNombre()); // Marca m con proxy sin inicializar
        m = marDao.recuperaDistribuidores(m);                        // Marca m con proxy ya inicializado

        Assert.assertEquals(1, m.getDistribuidores().size());
        Assert.assertEquals(productorDatos.d1, m.getDistribuidores().get(0));


		log.info("");
		log.info("Probando acceso a referencia EAGER ------------------------------------------------------------------------------");

        d = disDao.recuperaPorNombre(productorDatos.d1.getNombre());
        Assert.assertEquals(productorDatos.m1, d.getMarca());

    }

    @Test
    public void test06_EAGER() {

        Distribuidor d;
        boolean excepcion;

        log.info("");
        log.info("Configurando situación de partida del test -----------------------------------------------------------------------");

        productorDatos.creaMarcasConDistribuidores();
        productorDatos.almacenaMarcas();


        log.info("Inicio del test --------------------------------------------------------------------------------------------------");
        log.info("Objetivo: Prueba da recuperación de propiedades EAGER\n");

        // Situación de partida:
        // m1, d1 desligados

        log.info("Probando (que no hay excepcion tras) acceso inicial a propiedade EAGER fuera de sesion ----------------------------------------");

        d = disDao.recuperaPorNombre(productorDatos.d1.getNombre());
        log.info("Acceso a marca de distribuidor");
        try	{
            Assert.assertEquals(productorDatos.m1, d.getMarca());
            excepcion=false;
        } catch (LazyInitializationException ex) {
            excepcion=true;
            log.info(ex.getClass().getName());
        }
        Assert.assertFalse(excepcion);
    }

    @Test
    public void test07_Propagacion(){

        Marca m1, m2;
        Distribuidor d1, d2, d3, d4;
        log.info("");
        log.info("Configurando situación de partida del test -----------------------------------------------------------------------");

        productorDatos.creaMarcasConDistribuidores();
        productorDatos.almacenaMarcas();

        log.info("");
        log.info("Inicio del test --------------------------------------------------------------------------------------------------");
        log.info("Objetivo: Prueba de propagación de delete de una marca y sus distribuidores asociados\n");


        m1 = marDao.recuperaPorNombre(productorDatos.m0.getNombre());
        m1 = marDao.recuperaDistribuidores(m1);
        d1 = m1.getDistribuidores().get(0);
        Assert.assertNotNull(d1);

        marDao.elimina(m1);

        d2 = disDao.recuperaPorNombre(d1.getNombre());
        Assert.assertNull(d2);

        m2 = marDao.recuperaPorNombre(productorDatos.m1.getNombre());
        m2 = marDao.recuperaDistribuidores(m2);
        d3 = m2.getDistribuidores().get(0);
        Assert.assertNotNull(d3);

        marDao.elimina(m2);

        d4 = disDao.recuperaPorNombre(d3.getNombre());
        Assert.assertNull(d4);
    }

}
