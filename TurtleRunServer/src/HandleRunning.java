import java.io.IOException;
import java.util.Vector;

/**
 * Clase encargada de manejar la carrera,
 * Indicar: se creará una copia de las tortugas (usando otro constructor de las mismas)
 * pues al usar las del mismo vector se modifica y al volver a mandar a cliente NO las
 * reconoce.
 */
public class HandleRunning extends Thread{
    private Vector<Turtle> vector;
    private boolean carreraEnMarcha = false;
    private Turtle ganador;
    private boolean primerGanador = false;
    private Server server;
    public volatile boolean carreraEnMarchaVolatile = false;    //para pruebas con parametros volatiles


    /** Pasamos el server donde está corriendo
     * @param server
     */
    public void setServer(Server server)
    {
        this.server = server;
    }

    public boolean getCarreraEnMarcha(){
        return this.carreraEnMarcha;
    }

    public HandleRunning(Vector<Turtle> vector){

        //todo tenemos problemas por el tipo de objetos turtle que mandamos...
        // no se ha solucionado haciendo copia del vector así que creo un nuevo vector
        // que es el que se usará para la carrera y NO modificar el vector de carrera
        // futuro: cambiar el handle y crear una clase que sea la que tenga el thread
        // tortugaEnCarrera por ejemplo
        Vector<Turtle> nuevoVector = new Vector<Turtle>();
        //rellenamos con los datos de vector haciendo una copia
        for (int i = 0; i < vector.size(); i++)
        {
            nuevoVector.add(new Turtle(vector.elementAt(i).getNameTurtle(), vector.elementAt(i).getDorsal(), this));
        }
        this.vector = nuevoVector;

    }


    /**
     * Cuando una tortuga gana llama a esta función para finalizar la carrera
     * manda mensaje al cliente
     * finaliza todos los hilos
     * @param turtle
     * @throws InterruptedException
     * @throws IOException
     */
    public void setGanador(Turtle turtle) throws InterruptedException, IOException {
        if (!primerGanador)
        {
            this.carreraEnMarcha = false;
            this.ganador = turtle;
            primerGanador = true;

            server.sendWinnerMessager(turtle);
        }

        for (int i = 0; i < vector.size(); i++){
            vector.elementAt(i).join();
        }
    }


    /**
     * iniciamos la carrera
     * a cada elemento del vector se le indica cuál es su HC
     *
     *
     */
    public void carrera(){
        carreraEnMarcha = true;
        for (int i = 0; i < vector.size(); i++)
        {
            vector.elementAt(i).start();
        }
        carreraEnMarchaVolatile = true;
    }
}
