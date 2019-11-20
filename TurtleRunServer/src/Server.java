import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class Server {
    // declaramos las variables
    private final int PORT = 60000;      //puerto del server por defecto (usaremos la pregunta al usuario)
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

    public Server(int puerto) throws  IOException {
        System.out.println("Inicializando servidor de TurtleRun");
        srvSocket = new ServerSocket(puerto);
        socket = new Socket();

    }


    /**
     * Inicia el servidor
     * @throws IOException
     */
    public void iniciarServer() throws IOException
    {

        while (true)
        {
            socket = srvSocket.accept();
            // esperando mensaje de un cliente
            System.out.println("lectura de datos de cliente");
            boolean connected = true;

            do {
                System.out.println("Esperando objeto de opciones de cliente - time=" + System.currentTimeMillis());
                InputStream inputStream = socket.getInputStream();
                ObjectInputStream is = new ObjectInputStream(inputStream);
                try {
                    HandleOptionConnection hc = (HandleOptionConnection)is.readObject();
                    optionsHandle(hc);

                } catch (Exception e){
                    cliente = new DataOutputStream(socket.getOutputStream());
                    cliente.writeUTF("Ha ocurrido un error al manejar el objeto HandleOptionConnection");
                    System.out.println("Error manejando el HandleOptionConnection enviado. - time = " + System.currentTimeMillis());
                }
            } while (connected);

        }

    }

    /**
     * @param hc parametro enviado por el cliente
     * @throws IOException
     */
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
                System.out.println("Ha ocurrido un error en la opcion de HandleOptionConnection - time: " + System.currentTimeMillis());
                break;
        }
    }

    /**
     * Cierra el servidor
     * @throws IOException
     */
    private void optionClose() throws IOException {
        System.out.println("Se inicia apagado del servidor - time = " + System.currentTimeMillis());
        cliente = new DataOutputStream(socket.getOutputStream());
        cliente.writeUTF("Se está apagando el servidor, ¡Hasta otra!");
        connected = false;
        socket.close();
        srvSocket.close();
        System.exit(0);
    }

    /**
     * Añade el turtle y responde al cliente
     * @param turtle
     * @throws IOException
     */
    private void optionOne(Turtle turtle) throws IOException {

        //TODO habría que verificar si se ha usado el mismo dorsal
        //añadimos la tortuga al vector
        vectorTurtles.add(turtle);
        DataOutputStream clienteT = new DataOutputStream(socket.getOutputStream());
        clienteT.writeUTF("se ha creado la tortuga:" + turtle.getNameTurtle() + " con el dorsal:" + turtle.getDorsal());
        System.out.println("Se ha añadido una tortuga. - time = " + System.currentTimeMillis());
        pintamosVector();
    }


    /**
     * Envía listado al cliente
     * Espera respuesta y elimina el objeto deseado
     * @throws IOException
     */
    private void optionTwo() throws  IOException {
        System.out.println("Ha seleccionado la opción de eliminar tortugas.");
        //cliente espera lancemos el vector de tortugas
        ObjectOutputStream clienteVector = new ObjectOutputStream(socket.getOutputStream());
        clienteVector.writeObject(vectorTurtles);
        //si el vector está vacío no esperaremos que nos manden nada
        if (vectorTurtles.size() == 0)
            return;
        System.out.println(vectorTurtles.size());
        //si el vector tiene objetos esperamos elección del cliente
        ObjectInputStream is = new ObjectInputStream(socket.getInputStream());

        //casteamos a handleoptionconnection

        try {

            HandleOptionConnection hc = (HandleOptionConnection)is.readObject();


            if (hc.toDelete == true)
            {
                System.out.println("Se elimina la Tortuga " + hc.positionVectorToDelete + " - time = " + System.currentTimeMillis());
                vectorTurtles.removeElementAt(hc.positionVectorToDelete);
                DataOutputStream clienteT = new DataOutputStream(socket.getOutputStream());
                clienteT.writeUTF("Se ha eliminado la tortuga.");
                pintamosVector();
            }

        } catch (Exception e) {

        }
    }


    /**
     * Envia el vector tortugas al cliente
     * @throws IOException
     */
    private void optionThree() throws IOException{
        System.out.println("Seleccionado Mostrar Tortugas  ---> enviamos vector. - time = " + System.currentTimeMillis());
        ObjectOutputStream clienteVector = new ObjectOutputStream(socket.getOutputStream());
        clienteVector.writeObject(vectorTurtles);
    }


    /**
     * verifica cuántas tortugas hay
     * si es mayor de 1 inicia la carrera
     * @throws IOException
     */
    private void optionFour() throws IOException {
        // si num de tortugas <= 1 => debemos indicar que no se puede iniciar una carera

        if (vectorTurtles.size() <= 1)
        {
            DataOutputStream clienteT = new DataOutputStream(socket.getOutputStream());
            cliente.writeUTF("No hay tortugas suficientes para iniciar una carera.\n");
            System.out.println("Intentan iniciar carrera sin tortugas suficientes - time = " + System.currentTimeMillis());

        } else {


            HandleRunning hr = new HandleRunning(vectorTurtles);
            hr.setServer(this);
            hr.carrera();
        }
    }


    /**
     * Se envia mensaje al cliente indicando la tortuga ganadora
     * @param turle
     * @throws IOException
     */
    public void sendWinnerMessager(Turtle turle) throws IOException {
        DataOutputStream clienteT = new DataOutputStream(socket.getOutputStream());
        clienteT.writeUTF("Ha ganado la tortuga " + turle.getNameTurtle() + " con el Dorsal: " + turle.getDorsal());
        System.out.println("Enviados datos de tortuga ganadora- time = " + System.currentTimeMillis());
    }


    /**
     * Muestra los datos del vector de tortugas
     * si no hay tortugas lo indica
     */
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
