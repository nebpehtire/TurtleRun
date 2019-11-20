import java.io.IOException;

public class MainTurtleRun {
    public static void main(String[] args) throws IOException {

        System.out.println("Bienvenido a Turtle Run");
        //TODO solicitar qué puerto se quiere usar??
        TurtleRun  cliente = new TurtleRun();
        cliente.iniciarCliente();
    }
}
