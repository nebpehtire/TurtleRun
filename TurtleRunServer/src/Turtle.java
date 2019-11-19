import java.io.IOException;
import java.io.Serializable;
import java.util.Random;

public class Turtle extends Thread implements Serializable, Runnable {
    private static final long serialVersionUID = -539960512249039L;

    private String name;
    private int dorsal;
    private HandleRunning handleRunning;

    public Turtle(String name, int dorsal){
        this.name = name;
        this.dorsal = dorsal;
    }

    public Turtle(String name, int dorsal, boolean b){
        this.name = name;
        this.dorsal = dorsal;
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

    public void setHandleRunning(HandleRunning handleRunning)
    {
        this.handleRunning = handleRunning;
    }

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
        this.join();
        this.handleRunning = null;
    }


    @Override
    public void run(){
        try
        {
            while (!handleRunning.carreraEnMarchaVolatile){}
            carreraHastaMeta();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

