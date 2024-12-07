package gei.id.tutelado.model;


import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@TableGenerator(name="generadorIdsProductosLacteos", table="tabla_ids",
        pkColumnName="nombre_id", pkColumnValue="idProductoLacteo",
        valueColumnName="ultimo_valor_id",
        initialValue=0, allocationSize=1)

@NamedQueries({
        @NamedQuery(name="ProductosLacteos.recuperaPorNombre",
        query="SELECT prod FROM ProductosLacteos prod WHERE prod.nombre=:nombre")
})

@Entity
@Inheritance(strategy = InheritanceType.JOINED)

public class ProductosLacteos {

    @Id
    @GeneratedValue(generator = "generadorIdsProductosLacteos")
    private Long id;

    @Column(nullable = false, unique = true)
    private String nombre;

    @Column(nullable = false, unique = false)
    private LocalDateTime fechaExpiracion;

    @Column(nullable = false, unique = false)
    private String origen;

    @Column(nullable = false, unique = false)
    private Double precio;

    @Column(nullable = true)
    @ManyToMany
    @JoinTable (name = "t_prods_marcas", joinColumns = @JoinColumn(name = "id_prod"),
                inverseJoinColumns = @JoinColumn(name = "id_marca"))
    private List<Marca> marcas = new ArrayList<>();

    public ProductosLacteos(){
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

    public LocalDateTime getFechaExpiracion() {
        return fechaExpiracion;
    }

    public void setFechaExpiracion(LocalDateTime fechaExpiracion) {
        this.fechaExpiracion = fechaExpiracion;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public List<Marca> getMarcas() {
        return marcas;
    }

    public void setMarcas(List<Marca> marcas) {
        this.marcas = marcas;
    }

    public void asociarMarcas(Marca marca){
        if (this.getMarcas().contains(marca)) throw new RuntimeException("");
        this.marcas.add(marca);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductosLacteos that = (ProductosLacteos) o;
        if(nombre == null){
            return that.nombre == null;
        } else if (!nombre.equals(that.nombre))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 23;
        int result = 1;
        result = prime * result + (nombre != null ? nombre.hashCode() : 0);
        return result;
    }
}
