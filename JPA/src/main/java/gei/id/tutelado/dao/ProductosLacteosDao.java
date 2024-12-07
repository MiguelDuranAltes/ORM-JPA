package gei.id.tutelado.dao;

import gei.id.tutelado.configuracion.Configuracion;
import gei.id.tutelado.model.ProductosLacteos;

import java.time.LocalDateTime;
import java.util.List;

public interface ProductosLacteosDao {

    void setup (Configuracion conf);

    /* MO4.1 */ProductosLacteos recuperarPorNombre(String nombre);
    /* MO4.2 */ProductosLacteos alta(ProductosLacteos prod);
    /* MO4.3 */void eliminar(ProductosLacteos prod);
    /* MO4.4 */ProductosLacteos actualizar(ProductosLacteos prod);

    //CONSULTAS JPQL
    /* M04.6.b */List<String> recuperarNombreProductosSinMarca();
    /* MO4.6.c */List<ProductosLacteos> recuperarNombreProductosPorFechaFundacionMarca(LocalDateTime fechafundacion);

}
