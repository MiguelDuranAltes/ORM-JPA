package gei.id.tutelado.dao;

import gei.id.tutelado.configuracion.Configuracion;
import gei.id.tutelado.model.Marca;

import java.time.LocalDateTime;
import java.util.List;

public interface MarcaDao {
    void setup (Configuracion config);

    // OPERACIONES CRUD BASICAS
    /* MO4.1 */ Marca recuperaPorNombre (String nombre);
    /* MO4.2 */ Marca almacena (Marca marca);
    /* MO4.3 */ void elimina (Marca marca);
    /* MO4.4 */ Marca modifica (Marca marca);

    // OPERACIONES POR ATRIBUTOS LAZY
    /* MO4.5 */ Marca recuperaDistribuidores(Marca marca);
            // Recibe una marca con la coleccion de distribuidores como proxy SIN INICIALIZAR
            // Devuelve una copia de la marca con la coleccion de distribuidores INICIALIZADA
    // CONSULTAS JPQL
    /* MO4.6.a */ List<Marca> recuperaPorZonaDistribucion (String zona);
    /* MO4.6.c */ int contarMarcasRecientes(LocalDateTime fechaFundacion);

}
