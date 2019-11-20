import java.io.IOException;
import java.util.Scanner;

public class MainServer {
    //inicializamos el server

    private static Scanner reader = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
       // Server servidor = new Server();
        //servidor.iniciarServer();

        int puerto = readerInt();
        Server servidor = new Server(puerto);
        servidor.iniciarServer();

    }

    private static int readerInt()
    {
        System.out.println("¿Qué puerto quieres usar?");
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
