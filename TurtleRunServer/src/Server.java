import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class Server {
    // declaramos las variables
    private final int PORT = 60000;      //puerto del server TODO añadir opción de elegir puerto??
    private static ServerSocket srvSocket;      // Socket del server
    private Socket socket;              // Socket del cliente
    private DataOutputStream cliente;
    private boolean connected = false;




    //usaré un vector pues es más fácil de añadir/quitar/redimensionar
    private static Vector<Turtle> vectorTurtles = new Vector<Turtle>();


    // Constructor de la clase
    public Server() throws IOException {
        System.out.println("Inicializando servidor de TurtleRun");
        srvSocket = new ServerSocket(PORT);
        socket = new Socket();



    }


    public void iniciarServer() throws IOException
    {

        while (true)
        {
            socket = srvSocket.accept();
            // esperando mensaje de un cliente
            System.out.println("lectura de datos de cliente");
            boolean connected = true;

            do {
                InputStream inputStream = socket.getInputStream();
                ObjectInputStream is = new ObjectInputStream(inputStream);
                try {
                    HandleOptionConnection hc = (HandleOptionConnection)is.readObject();
                    optionsHandle(hc);

                } catch (Exception e){
                    cliente = new DataOutputStream(socket.getOutputStream());
                    cliente.writeUTF("Ha ocurrido un error al manejar el objeto HandleOptionConnection");
                }
            } while (connected);



        }

    }

    private void optionsHandle(HandleOptionConnection hc) throws IOException {
        switch (hc.opcion)
        {
            case 1:
                optionOne(hc.turtle);
                break;
            case 2:
                optionTwo();
                break;
            case 3:
                optionThree();
                break;
            case 4:
                optionFour();
                break;
            case 5:
                optionClose();
                break;
            default:
                System.out.println("Ha ocurrido un error en la opcion de HandleOptionConnection");
                break;
        }
    }

    private void optionClose() throws IOException {
        cliente = new DataOutputStream(socket.getOutputStream());
        cliente.writeUTF("Se está apagando el servidor, ¡Hasta otra!");
        System.out.println("Apagando el servidor");
        connected = false;
        socket.close();
        srvSocket.close();
        System.exit(0);
    }

    private void optionOne(Turtle turtle) throws IOException {

        //TODO habría que verificar si se ha usado el mismo dorsal
        //añadimos la tortuga al vector
        vectorTurtles.add(turtle);
        DataOutputStream clienteT = new DataOutputStream(socket.getOutputStream());
        clienteT.writeUTF("se ha creado la tortuga:" + turtle.getNameTurtle() + " con el dorsal:" + turtle.getDorsal());
        pintamosVector();
    }


    private void optionTwo() throws  IOException {
        System.out.println("Ha seleccionado la opción de eliminar tortugas.");
        //DataOutputStream clienteT = new DataOutputStream(socket.getOutputStream());
        //cliente espera lancemos el vector de tortugas
        ObjectOutputStream clienteVector = new ObjectOutputStream(socket.getOutputStream());
        clienteVector.writeObject(vectorTurtles);
        //si el vector está vacío no esperaremos que nos manden nada
        if (vectorTurtles.size() == 0)
            return;
        System.out.println(vectorTurtles.size());
        //si el vector tiene objetos esperamos elección del cliente
        //InputStream inputStream = socket.getInputStream();
        ObjectInputStream is = new ObjectInputStream(socket.getInputStream());

        //casteamos a handleoptionconnection

        try {

            HandleOptionConnection hc = (HandleOptionConnection)is.readObject();


            if (hc.toDelete == true)
            {
                vectorTurtles.removeElementAt(hc.positionVectorToDelete);
                DataOutputStream clienteT = new DataOutputStream(socket.getOutputStream());
                clienteT.writeUTF("Se ha eliminado la tortuga.");
                pintamosVector();
            }

        } catch (Exception e) {

        }
    }
    private void optionThree() throws IOException{
        System.out.println("Ha seleccionado la opción de mostrar tortugas  ---> enviamos vector.");
        ObjectOutputStream clienteVector = new ObjectOutputStream(socket.getOutputStream());
        clienteVector.writeObject(vectorTurtles);
    }
    private void optionFour()
    {
        /*
        * crear un hilo para cada tortuga
        * ¿crear un handlerun para tenerlo ordenado ?
        *
        *
        * */
        HandleRunning hr = new HandleRunning(vectorTurtles);
        hr.setServer(this);
       // System.out.println(hr.carrera());
        hr.carrera();


    }

    public void sendWinnerMessager(Turtle turle) throws IOException {
        DataOutputStream clienteT = new DataOutputStream(socket.getOutputStream());
        clienteT.writeUTF("Ha ganado la tortuga " + turle.getNameTurtle() + " con el Dorsal: " + turle.getDorsal());
    }

    private static void pintamosVector()
    {
        System.out.println("Listado de Tortugas\n- - - - - - - - - - - - -");
        for ( int i = 0; i < vectorTurtles.size(); i++){
            System.out.println("Tortuga " + (i+1) + " Nombre: " + vectorTurtles.elementAt(i).getNameTurtle() + " con Dorsal: " + vectorTurtles.elementAt(i).getDorsal());
        }
        if (vectorTurtles.size() == 0)
            System.out.println("No se ha inscrito ninguna tortuga a esta gran carrera..... :(");
        System.out.println("- - - - - - - - - - - - -");

    }




}
