package gei.id.tutelado.model;


import javax.persistence.*;

@Entity
@DiscriminatorValue("LECHE")
@Table(name = "t_leche")
@PrimaryKeyJoinColumn(name = "idProductoLacteo")
public class Leche extends ProductosLacteos{

    @Column(nullable = true, unique = false)
    private Float volumen;

    @Column(nullable = false, unique = false)
    private String tipo;

    public Leche(){

    }

    public Float getVolumen() {
        return volumen;
    }

    public void setVolumen(Float volumen) {
        this.volumen = volumen;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }


}
