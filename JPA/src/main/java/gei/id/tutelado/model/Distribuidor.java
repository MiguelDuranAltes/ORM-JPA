package gei.id.tutelado.model;

import javax.persistence.*;


@TableGenerator(name="generadorIdsDistribuidor", table="tabla_ids",
        pkColumnName="nombre_id", pkColumnValue="idDistribuidor",
        valueColumnName="ultimo_valor_id",
        initialValue=0, allocationSize=1)


@NamedQueries ({
        @NamedQuery (name="Distribuidor.recuperaPorNombre",
                query="SELECT d FROM Distribuidor d where d.nombre=:nombre")
})

@Entity
public class Distribuidor {
    @Id
    @GeneratedValue(generator="generadorIdsDistribuidor")
    private Long id;

    @Column(nullable = false, unique = true)
    private String nombre;

    @Column(nullable = false, unique = false)
    private String zonaDistribucion;

    @ManyToOne(cascade={}, fetch=FetchType.EAGER)
    @JoinColumn(nullable = false, unique = false)
    private Marca marca;

    public Distribuidor(){
        nombre = "ManoloDelivery";
        zonaDistribucion = "Galicia";
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
    public String getZonaDistribucion() {
        return zonaDistribucion;
    }
    public void setZonaDistribucion(String zonaDistribucion) {
        this.zonaDistribucion = zonaDistribucion;
    }
    public Marca getMarca() {
        return marca;
    }
    public void setMarca(Marca marca) {
        this.marca = marca;
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
        Distribuidor other = (Distribuidor) obj;
        if (nombre == null) {
            return other.nombre == null;
        } else if (!nombre.equals(other.nombre))
            return false;
        return true;
    }
}
