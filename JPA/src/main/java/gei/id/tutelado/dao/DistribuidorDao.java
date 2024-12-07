package gei.id.tutelado.dao;

import gei.id.tutelado.configuracion.Configuracion;
import gei.id.tutelado.model.Distribuidor;



public interface DistribuidorDao {

    void setup (Configuracion config);

    // OPERACIONES CRUD BASICAS
    /* MO4.1 */ Distribuidor recuperaPorNombre (String nombre);
    /* MO4.2 */ Distribuidor almacena (Distribuidor distribuidor);
    /* MO4.3 */ void elimina (Distribuidor distribuidor);
    /* MO4.4 */ Distribuidor modifica (Distribuidor distribuidor);


}
