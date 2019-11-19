import java.io.IOException;
import java.io.Serializable;
import java.util.Vector;

public class HandleRunning extends Thread{
    private Vector<Turtle> vector;
    private boolean carreraEnMarcha = false;
    private Turtle ganador;
    private boolean primerGanador = false;
    private Server server;
    public volatile boolean carreraEnMarchaVolatile = false;    //para pruebas con parametros volatiles


    public void setServer(Server server)
    {
        this.server = server;
    }

    public boolean getCarreraEnMarcha(){
        return this.carreraEnMarcha;
    }

    public HandleRunning(Vector<Turtle> vector){
        this.vector = vector;
        Thread t = new Thread(this);
    }


    public void setGanador(Turtle turtle) throws InterruptedException, IOException {
        if (!primerGanador)
        {
            this.carreraEnMarcha = false;
            this.ganador = turtle;
            primerGanador = true;

            //enviamos mensaje del ganador
            //System.out.println("Ha ganado: " + turtle.getNameTurtle());
            server.sendWinnerMessager(turtle);
        }

        for (int i = 0; i < vector.size(); i++){
            vector.elementAt(i).join();
        }
    }


    public void carrera(){
        carreraEnMarcha = true;
        for (int i = 0; i < vector.size(); i++)
        {
           // vector.elementAt(i).setSemaforoFinalizado(semaforoFinalizado);
           // vector.elementAt(i).setSemaforoIniciarCarrera(semaforoInicializarCarrera);
            //vector.elementAt(i).run();
            vector.elementAt(i).setHandleRunning(this);
            vector.elementAt(i).start();
        }
        carreraEnMarchaVolatile = true;



        //return 0;
    }
}
