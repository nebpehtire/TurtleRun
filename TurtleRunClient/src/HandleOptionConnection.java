import java.io.Serializable;

public class HandleOptionConnection implements Serializable {
    private static final long serialVersionUID = -53996051224903L;

    public int opcion;
    public Turtle turtle;

    public int positionVectorToDelete = 0;
    public boolean toDelete = false;

    public HandleOptionConnection(int opcion, Turtle turtle){
        this.opcion = opcion;
        this.turtle = turtle;
    }

    public HandleOptionConnection(int opcion)
    {
        this.opcion = opcion;
        this.turtle = new Turtle("nulo", 0); //se crea nulo para que no haya errores TODO mejorar esto
    }

    public HandleOptionConnection(int opcion, int positionVectorToDelete)
    {
        this.opcion = opcion;
        this.positionVectorToDelete = positionVectorToDelete;
    }

    public HandleOptionConnection(int opcion, boolean toDelete, int positionVectorToDelete)
    {
        this.opcion = opcion;
        this.toDelete = toDelete;
        this.positionVectorToDelete = positionVectorToDelete;
    }

}