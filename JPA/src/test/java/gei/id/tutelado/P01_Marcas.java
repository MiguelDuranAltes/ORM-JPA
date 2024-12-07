package gei.id.tutelado;

import gei.id.tutelado.configuracion.Configuracion;
import gei.id.tutelado.configuracion.ConfiguracionJPA;
import gei.id.tutelado.dao.MarcaDao;
import gei.id.tutelado.dao.MarcaDaoJPA;
import gei.id.tutelado.model.Marca;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.*;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import java.time.LocalDateTime;

public class P01_Marcas {

    private Logger log = LogManager.getLogger("gei.id.tutelado");

    private static ProductorDatosPrueba productorDatos = new ProductorDatosPrueba();

    private static Configuracion cfg;
    private static MarcaDao marDao;

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

        marDao = new MarcaDaoJPA();
        marDao.setup(cfg);

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
    public void test01_Recuperacion() {

        Marca m;

        log.info("");
        log.info("Configurando situación de partida do test -----------------------------------------------------------------------");

        productorDatos.creaMarcas();
        productorDatos.almacenaMarcas();

        log.info("");
        log.info("Inicio del test --------------------------------------------------------------------------------------------------");
        log.info("Objetivo: Prueba de recuperación desde la BD de marca (sin entradas asociadas) por nombre\n"
                + "\t\t\t\t Casos contemplados:\n"
                + "\t\t\t\t a) Recuperación por nombre existente\n"
                + "\t\t\t\t b) Recuperacion por nombre inexistente\n");

        // Situación de partida:
        // m0 desligado    	

        log.info("Probando recuperacion por nombre EXISTENTE --------------------------------------------------");

        m = marDao.recuperaPorNombre(productorDatos.m0.getNombre());
        Assert.assertEquals(productorDatos.m0.getNombre(),      m.getNombre());


        log.info("");
        log.info("Probando recuperacion por nombre INEXISTENTE -----------------------------------------------");

        m = marDao.recuperaPorNombre("abcdefgh");
        Assert.assertNull (m);

    }

    @Test
    public void test02_Alta() {

        log.info("");
        log.info("Configurando situación de partida del test -----------------------------------------------------------------------");

        productorDatos.creaMarcas();

        log.info("");
        log.info("Inicio del test --------------------------------------------------------------------------------------------------");
        log.info("Objetivo: Prueba de guardado en la BD de nueva marca\n");

        // Situación de partida:
        // m0 transitorio    	

        Assert.assertNull(productorDatos.m0.getId());
        marDao.almacena(productorDatos.m0);
        Assert.assertNotNull(productorDatos.m0.getId());
    }

    @Test
    public void test03_Eliminacion() {

        log.info("");
        log.info("Configurando situación de partida del test -----------------------------------------------------------------------");

        productorDatos.creaMarcas();
        productorDatos.almacenaMarcas();


        log.info("");
        log.info("Inicio del test --------------------------------------------------------------------------------------------------");
        log.info("Objetivo: Prueba de eliminación de la BD de marcas sin distribuidores asociados\n");

        // Situación de partida:
        // m0 desligado  

        Assert.assertNotNull(marDao.recuperaPorNombre(productorDatos.m0.getNombre()));
        marDao.elimina(productorDatos.m0);
        Assert.assertNull(marDao.recuperaPorNombre(productorDatos.m0.getNombre()));
    }

    @Test
    public void test04_Modificacion() {
        Marca m1, m2;
        LocalDateTime nuevaFechaFundacion;

        log.info("");
        log.info("Configurando situación de partida del test -----------------------------------------------------------------------");

        productorDatos.creaMarcas();
        productorDatos.almacenaMarcas();

        log.info("");
        log.info("Inicio del test --------------------------------------------------------------------------------------------------");
        log.info("Objetivo: Prueba de modificación de la información básica de una marca sin distribuidores\n");

        // Situación de partida:
        // m0 desligado  

        nuevaFechaFundacion = LocalDateTime.of(2020, 1, 1, 0, 0, 0);

        m1 = marDao.recuperaPorNombre(productorDatos.m0.getNombre());
        Assert.assertNotEquals(nuevaFechaFundacion, m1.getFechaFundacion());
        m1.setFechaFundacion(nuevaFechaFundacion);
        marDao.modifica(m1);


        m2 = marDao.recuperaPorNombre(productorDatos.m0.getNombre());
        Assert.assertEquals (nuevaFechaFundacion, m2.getFechaFundacion());

    }
}
