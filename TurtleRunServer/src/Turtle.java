import java.io.IOException;
import java.io.Serializable;
import java.util.Random;

/**
 * Clase Tortuga
 */
public class Turtle extends Thread implements Serializable, Runnable {
    private static final long serialVersionUID = -539960512249039L;

    private String name;
    private int dorsal;
    private HandleRunning handleRunning;

    /**
     * Constructor inicial para comunicación con el cliente
     * @param name
     * @param dorsal
     */
    public Turtle(String name, int dorsal){
        this.name = name;
        this.dorsal = dorsal;
    }

    /** Constructor para el HandleRunning
     * @param name
     * @param dorsal
     * @param handleRunning
     */
    public Turtle(String name, int dorsal, HandleRunning handleRunning){
        this.name = name;
        this.dorsal = dorsal;
        this.handleRunning = handleRunning;
        Thread t = new Thread(this);
        t.start();
    }
    //solo creo getters pues no está indicado que se pueda modificar variables
    public String getNameTurtle()
    {
        return this.name;
    }
    public int getDorsal()
    {
        return this.dorsal;
    }


    /**
     * Movimiento de las tortugas
     * @throws InterruptedException
     * @throws IOException
     */
    private void carreraHastaMeta() throws InterruptedException, IOException {
        int positionRun = 0;
        Random random = new Random();

        while (handleRunning.getCarreraEnMarcha())
        {
            Thread.sleep(5);                //hacemos una pausa mínima
            int recorrido = random.nextInt(9) + 1;
            positionRun += recorrido;
               // System.out.println(this.name + "   está en : " + positionRun + " timer: " + System.currentTimeMillis());
            if (positionRun >= 500) {
                    this.handleRunning.setGanador(this);
            }
        }
    }


    @Override
    public void run(){
        try
        {
            /** OJO: while para esperar que todos los hilos estén creados **/
            while (!handleRunning.carreraEnMarchaVolatile){}
            carreraHastaMeta();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

