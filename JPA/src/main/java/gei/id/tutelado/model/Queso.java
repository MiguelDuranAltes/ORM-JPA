package gei.id.tutelado.model;


import javax.persistence.*;

@Entity
@DiscriminatorValue("QUESO")
@Table(name = "t_queso")
@PrimaryKeyJoinColumn(name = "idProductoLacteo")
public class Queso extends  ProductosLacteos{

    @Column(nullable = false, unique = false)
    private String tipo;

    @Column(nullable = true, unique = false)
    private String maduracion;

    @Column(nullable = true, unique = false)
    private Float peso;


    public Queso(){

    }


    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getMaduracion() {
        return maduracion;
    }

    public void setMaduracion(String maduracion) {
        this.maduracion = maduracion;
    }

    public Float getPeso() {
        return peso;
    }

    public void setPeso(Float peso) {
        this.peso = peso;
    }
}
