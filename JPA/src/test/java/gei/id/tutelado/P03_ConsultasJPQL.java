package gei.id.tutelado;

import gei.id.tutelado.configuracion.Configuracion;
import gei.id.tutelado.configuracion.ConfiguracionJPA;
import gei.id.tutelado.dao.*;
import gei.id.tutelado.model.Marca;
import gei.id.tutelado.model.ProductosLacteos;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.checkerframework.checker.units.qual.A;
import org.junit.*;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class P03_ConsultasJPQL {

    private Logger log = LogManager.getLogger("gei.id.tutelado");

    private static ProductorDatosPrueba productorDatos = new ProductorDatosPrueba();

    private static Configuracion cfg;
    private static MarcaDao marDao;
    private static DistribuidorDao disDao;
    private static ProductosLacteosDao prodDao;


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
    public static void init() throws Exception {
        cfg = new ConfiguracionJPA();
        cfg.start();

        marDao = new MarcaDaoJPA();
        disDao = new DistribuidorDaoJPA();
        prodDao = new ProductosLacteosDaoJPA();

        marDao.setup(cfg);
        disDao.setup(cfg);
        prodDao.setup(cfg);

        productorDatos = new ProductorDatosPrueba();
        productorDatos.Setup(cfg);
    }

    @AfterClass
    public static void endclose() throws Exception {
        cfg.endUp();
    }


    @Before
    public void setUp() throws Exception {
        log.info("");
        log.info("Limpando BD -----------------------------------------------------------------------------------------------------");
        productorDatos.limpiaBD();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test08_INNER_JOIN() {

        List<Marca> listaM;

        log.info("");
        log.info("Configurando situación de partida do test -----------------------------------------------------------------------");

        productorDatos.creaMarcasConDistribuidores();
        productorDatos.almacenaMarcas();


        log.info("");
        log.info("Inicio de test --------------------------------------------------------------------------------------------------");
        log.info("Objetivo: Prueba da consulta Marca.recuperaPorZonaDistribucion\n");

        // Situación de partida:
        // m0, m1 desligados

        listaM = marDao.recuperaPorZonaDistribucion("Asturias");
        Assert.assertEquals(0, listaM.size());

        listaM = marDao.recuperaPorZonaDistribucion("Galicia");
        Assert.assertEquals(2, listaM.size());
        Assert.assertEquals(productorDatos.m0, listaM.get(0));
        Assert.assertEquals(productorDatos.m1, listaM.get(1));
    }


    @Test
    public void test08_OUTER_JOIN(){
        List<String> listaP;

        log.info("");
        log.info("Configurando situación de partida do test -----------------------------------------------------------------------");

        productorDatos.asociarMarcasaProdctos();
        productorDatos.almacenaMarcas();
        productorDatos.almacenaProductos();

        log.info("");
        log.info("Inicio de test --------------------------------------------------------------------------------------------------");
        log.info("Objetivo: Prueba da consulta que recupera nome de productos lacteos sen marca\n");

        listaP = prodDao.recuperarNombreProductosSinMarca();
        Assert.assertEquals(4,listaP.size());

    }


    @Test
    public void test08_Subconsulta(){
        List <ProductosLacteos> listaProd;

        log.info("");
        log.info("Configurando situación de partida do test -----------------------------------------------------------------------");

        productorDatos.asociarMarcasaProdctos();
        productorDatos.almacenaMarcas();
        productorDatos.almacenaProductos();


        log.info("");
        log.info("Inicio de test --------------------------------------------------------------------------------------------------");
        log.info("Objetivo: Prueba da consulta de recuperacion do nome dos productos lacteos que teñen fecha unha determinada data de fundacion da marca\n");

        listaProd = prodDao.recuperarNombreProductosPorFechaFundacionMarca(LocalDateTime.of(2000, 1, 1, 0, 0).truncatedTo(ChronoUnit.SECONDS));
        Assert.assertEquals(2,listaProd.size());

        listaProd = prodDao.recuperarNombreProductosPorFechaFundacionMarca(LocalDateTime.of(1950, 1, 1, 0, 0).truncatedTo(ChronoUnit.SECONDS));
        Assert.assertEquals(1,listaProd.size());
        Assert.assertEquals(productorDatos.p1,listaProd.get(0));
    }


    @Test
    public void test08_Agregacion(){

        int marcasRecientes;

        log.info("");
        log.info("Configurando situación de partida do test -----------------------------------------------------------------------");

        productorDatos.creaMarcasConDistribuidores();
        productorDatos.almacenaMarcas();

        log.info("");
        log.info("Inicio de test --------------------------------------------------------------------------------------------------");
        log.info("Objetivo: Prueba da consulta Marca.contarMarcasRecientes\n");


        marcasRecientes = marDao.contarMarcasRecientes(LocalDateTime.of(1990, 1, 1, 0, 0));
        Assert.assertEquals(0, marcasRecientes);
        marcasRecientes = marDao.contarMarcasRecientes(LocalDateTime.of(1975, 1, 1, 0, 0));
        Assert.assertEquals(2, marcasRecientes);
        marcasRecientes = marDao.contarMarcasRecientes(LocalDateTime.of(1955, 1, 1, 0, 0));
        Assert.assertEquals(4, marcasRecientes);
    }
}
