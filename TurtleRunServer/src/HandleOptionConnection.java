import java.io.Serializable;


/**
 * Clase para el manejo de mensajes entre cliente y servidor
 */
public class HandleOptionConnection implements Serializable {
    private static final long serialVersionUID = -53996051224903L;

    public int opcion;
    public Turtle turtle;

    public int positionVectorToDelete = 0;
    public boolean toDelete = false;

    /** Opción creación recibe opcion 1 con turtle
     * @param opcion
     * @param turtle
     */
    public HandleOptionConnection(int opcion, Turtle turtle){
        this.opcion = opcion;
        this.turtle = turtle;
    }


    /** únicamente indica opción: inicio de eliminar, mostrar tortugas, iniciar carrera, salir
     * @param opcion
     */
    public HandleOptionConnection(int opcion)
    {
        this.opcion = opcion;
        this.turtle = new Turtle("nulo", 0); //se crea nulo para que no haya errores TODO mejorar esto
    }

    /**
     * Opción eliminar, indica la posición a eliminar
     * @param opcion
     * @param positionVectorToDelete
     */
    public HandleOptionConnection(int opcion, int positionVectorToDelete)
    {
        this.opcion = opcion;
        this.positionVectorToDelete = positionVectorToDelete;
    }


    /**
     * Opción eliminar con semáforo
     * @param opcion
     * @param toDelete
     * @param positionVectorToDelete
     */
    public HandleOptionConnection(int opcion, boolean toDelete, int positionVectorToDelete)
    {
        this.opcion = opcion;
        this.toDelete = toDelete;
        this.positionVectorToDelete = positionVectorToDelete;
    }

}
