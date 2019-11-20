import java.io.Serializable;

/**
 * Clase Tortuga básica
 */
public class Turtle implements Serializable {
    private static final long serialVersionUID = -539960512249039L;

    private String name;
    private int dorsal;

    public Turtle(String name, int dorsal){
        this.name = name;
        this.dorsal = dorsal;
    }

    //solo creo getters pues no está previsto modificación
    public String getName()
    {
        return this.name;
    }
    public int getDorsal()
    {
        return this.dorsal;
    }
}
