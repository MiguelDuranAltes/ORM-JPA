package gei.id.tutelado;

import gei.id.tutelado.configuracion.Configuracion;
import gei.id.tutelado.configuracion.ConfiguracionJPA;
import gei.id.tutelado.dao.ProductosLacteosDao;
import gei.id.tutelado.dao.ProductosLacteosDaoJPA;
import gei.id.tutelado.model.ProductosLacteos;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.*;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

public class P04_ProductosLacteos {
    private Logger log = LogManager.getLogger("gei.id.tutelado");

    private static ProductorDatosPrueba productorDatos = new ProductorDatosPrueba();

    private static Configuracion cfg;
    private static ProductosLacteosDao productoDao;


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
    public static void init() {
        cfg = new ConfiguracionJPA();
        cfg.start();

        productoDao = new ProductosLacteosDaoJPA();
        productoDao.setup(cfg);

        productorDatos = new ProductorDatosPrueba();
        productorDatos.Setup(cfg);
    }

    @AfterClass
    public static void endclose() {
        cfg.endUp();
    }

    @Before
    public void setUp() {
        log.info("");
        log.info("Limpiando BD --------------------------------------------------------------------------------------------");
        productorDatos.limpiaBD();
    }

    @After
    public void tearDown() {
    }


    @Test
    public void test01_Recuperacion(){
        ProductosLacteos p;

        log.info("");
        log.info("Configurando situación de partida do test -----------------------------------------------------------------------");

        productorDatos.creaProductoLeche();
        productorDatos.almacenaProductos();


        log.info("");
        log.info("Inicio del test --------------------------------------------------------------------------------------------------");
        log.info("Objetivo: Prueba de recuperación desde la BD de productoslacteos (sin entradas asociadas) por nombre\n"
                + "\t\t\t\t Casos contemplados:\n"
                + "\t\t\t\t a) Recuperación por nombre existente\n"
                + "\t\t\t\t b) Recuperacion por nombre inexistente\n");

        // Situación de partida:
        // p1 desligado

        log.info("Probando recuperacion por nombre EXISTENTE --------------------------------------------------");

        p = productoDao.recuperarPorNombre(productorDatos.p1.getNombre());
        Assert.assertEquals(productorDatos.p1.getNombre(),p.getNombre());
        Assert.assertEquals(productorDatos.p1.getFechaExpiracion(),p.getFechaExpiracion());
        Assert.assertEquals(productorDatos.p1.getOrigen(),p.getOrigen());
        Assert.assertEquals(productorDatos.p1.getPrecio(),p.getPrecio());

        log.info("");
        log.info("Probando recuperacion por nombre INEXISTENTE -----------------------------------------------");

        p = productoDao.recuperarPorNombre("abcdefgh");
        Assert.assertNull (p);
    }

    @Test
    public void test02_Alta() {

        log.info("");
        log.info("Configurando situación de partida del test -----------------------------------------------------------------------");

        productorDatos.creaProductoLeche();

        log.info("");
        log.info("Inicio del test --------------------------------------------------------------------------------------------------");
        log.info("Objetivo: Prueba de guardado en la BD de nueva marca\n");

        // Situación de partida:
        //p1 transitorio

        Assert.assertNull(productorDatos.p1.getId());
        productoDao.alta(productorDatos.p1);
        Assert.assertNotNull(productorDatos.p1.getId());
    }

    @Test
    public void test03_Eliminacion() {

        log.info("");
        log.info("Configurando situación de partida del test -----------------------------------------------------------------------");

        productorDatos.creaProductoLeche();
        productorDatos.almacenaProductos();


        log.info("");
        log.info("Inicio del test --------------------------------------------------------------------------------------------------");
        log.info("Objetivo: Prueba de eliminación de la BD de marcas sin distribuidores asociados\n");

        // Situación de partida:
        // m0 desligado

        Assert.assertNotNull(productoDao.recuperarPorNombre(productorDatos.p1.getNombre()));
        productoDao.eliminar(productorDatos.p1);
        Assert.assertNull(productoDao.recuperarPorNombre(productorDatos.p1.getNombre()));
    }

    @Test
    public void test04_Modificacion() {
        ProductosLacteos p2, p3;
        String nuevoOrigen;

        log.info("");
        log.info("Configurando situación de partida del test -----------------------------------------------------------------------");

        productorDatos.creaProductoLeche();
        productorDatos.almacenaProductos();

        log.info("");
        log.info("Inicio del test --------------------------------------------------------------------------------------------------");
        log.info("Objetivo: Prueba de modificación de la información básica de un producto lacteo ");


        nuevoOrigen= "OrigenMod";

        p2 = productoDao.recuperarPorNombre(productorDatos.p1.getNombre());
        Assert.assertNotEquals(nuevoOrigen, p2.getOrigen());
        p2.setOrigen(nuevoOrigen);

        productoDao.actualizar(p2);

        p3 = productoDao.recuperarPorNombre(productorDatos.p1.getNombre());
        Assert.assertEquals (nuevoOrigen, p3.getOrigen());
    }
}
