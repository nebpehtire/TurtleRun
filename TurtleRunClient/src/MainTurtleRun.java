import java.io.IOException;
import java.util.Scanner;

public class MainTurtleRun {

    private static Scanner reader = new Scanner(System.in);



    public static void main(String[] args) throws IOException {

        System.out.println("Bienvenido a Turtle Run");
        //TODO solicitar qué puerto se quiere usar??
       // TurtleRun  cliente = new TurtleRun();
        //cliente.iniciarCliente();

        int puerto = readerInt();
        TurtleRun cliente = new TurtleRun(puerto);
        cliente.iniciarCliente();

    }


    private static int readerInt()
    {
        System.out.println("Selecciona el puerto donde has iniciado el servidor");
        boolean exit = false;
        int puerto = 0;
        while (!exit)
        {
            try
            {
                puerto = Integer.parseInt(reader.nextLine());
                if (puerto < 1 || puerto > 65535)
                    System.out.println(puerto + " no es un puerto válido, debe estar entre 1 y 65535\n¿Qué puerto quieres usar?");
                else
                    exit = true;
            } catch (Exception e){
                System.out.println("Debes insertar un número entre 1 y 65535\n¿Qué puerto quieres usar?");
            }
        }
        return puerto;

    }
}
