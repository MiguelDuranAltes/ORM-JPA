package gei.id.tutelado.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@TableGenerator(name="generadorIdsMarcas", table="tabla_ids",
        pkColumnName="nombre_id", pkColumnValue="idMarca",
        valueColumnName="ultimo_valor_id",
        initialValue=0, allocationSize=1)

@NamedQueries ({
        @NamedQuery (name="Marca.recuperaPorNombre",
                query="SELECT m FROM Marca m where m.nombre=:nombre")
})

@Entity
public class Marca {
    @Id
    @GeneratedValue(generator="generadorIdsMarcas")
    private Long id;

    @Column(nullable = false, unique = true)
    private String nombre;

    @Column(nullable = false, unique = false)
    private LocalDateTime fechaFundacion;

    @ElementCollection
    @Column(nullable = true, unique = false)
    private List<String> esloganes;

    @OneToMany(mappedBy = "marca", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @Column(nullable = true, unique = false)
    private List<Distribuidor> distribuidores = new ArrayList<>();


    public Marca(){
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public LocalDateTime getFechaFundacion() {
        return fechaFundacion;
    }
    public void setFechaFundacion(LocalDateTime fechaFundacion) {
        this.fechaFundacion = fechaFundacion;
    }
    public List<String> getEsloganes() {
        return esloganes;
    }
    public void setEsloganes(List<String> esloganes) {
        this.esloganes = esloganes;
    }
    public List<Distribuidor> getDistribuidores() {
        return this.distribuidores;
    }
    public void setDistribuidores(List<Distribuidor> distribuidores) {
        this.distribuidores = distribuidores;
    }


    // Metodo de conveniencia para asegurarnos de que actualizamos os dous extremos da asociación ao mesmo tempo

    public void nuevoDistribuidor(Distribuidor distribuidor){
        if(distribuidor.getMarca()!=null) throw new RuntimeException("");
        distribuidor.setMarca(this);
        this.distribuidores.add(distribuidor);
    }

    public void borrarDistribuidor(Distribuidor distribuidor){

        if(!this.distribuidores.contains(distribuidor)) throw new RuntimeException("");
        if(!distribuidor.getMarca().equals(this)) throw new RuntimeException("");
        distribuidor.setMarca(null);
        this.distribuidores.remove(distribuidor);

    }
    @Override
    public int hashCode() {
        final int prime = 23;
        int result = 1;
        result = prime * result + (nombre != null ? nombre.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Marca other = (Marca) obj;
        if (nombre == null) {
            return other.nombre == null;
        } else if (!nombre.equals(other.nombre))
            return false;
        return true;
    }
}
