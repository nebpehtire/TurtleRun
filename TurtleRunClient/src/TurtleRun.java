import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.Vector;

public class TurtleRun {

    //declaramos las variables
    private final int PORT = 60000;
    private final String HOST = "localhost";   //apuntamos a la propia máquina pues el server está en local
    private Socket socket;
    private static boolean exitMenu = false;


    //creamos objeto de lectura de datos
    private static Scanner reader = new Scanner(System.in);


    public TurtleRun() throws IOException
    {
        try {
           socket = new Socket(HOST, PORT);
        } catch (Exception e) {
            System.out.println("Algo ha ido mal");
        }


    }

    public void iniciarCliente()
    {

        do {
            preguntamosOpciones();
            Integer decision = devolvemosIntegerDeReaderMenuPrincipal();
            opcionElegida(decision);
        } while (!exitMenu);
        reader.close();
        System.exit(0);
    }


    private void opcionElegida(int opcion)
    {
        switch (opcion)
        {
            case 1:
                choicedOne();
                break;
            case 2:
                choicedTwo();
                break;
            case 3:
                choicedThree();
                break;
            case 4:
                choicedFour();
                break;
            case 5:
                closeTurtleRun();
                break;
            default:
                System.out.println("Si has llegado hasta aquí algo malo ha ocurrido");
                break;
        }
    }

    private void choicedOne(){
        System.out.println("Introduce el nombre de la Tortuga:");
        String newName = reader.nextLine();
        System.out.println("Introduce el Dorsal de la Tortuga:");
        int dorsal = devolvemosIngegerDeReaderDorsal();
        Turtle turtle = new Turtle(newName,dorsal);
        HandleOptionConnection hc = new HandleOptionConnection(1,turtle);

        // enviamos el objeto handleopctionconnection
        try {
            ObjectOutputStream outStream = new ObjectOutputStream(socket.getOutputStream());
            outStream.writeObject(hc);
            DataInputStream entradaCliente = new DataInputStream(socket.getInputStream());
            System.out.println(entradaCliente.readUTF());
        } catch (Exception e) {
            //manejamos la excepction
            System.out.println(e);
        }
    }



    private void choicedTwo(){
        HandleOptionConnection hc = new HandleOptionConnection(2);
        try {
            //enviamos objeto pidiendo eliminación
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject(hc);
            //esperamos recibir objeto vector de tortugas
            ObjectInputStream entradaClienteObject = new ObjectInputStream(socket.getInputStream());
            //realizamos el try casteando el objecto
            try{
                Vector<Turtle> vt = (Vector<Turtle>)entradaClienteObject.readObject();
                //pintamos el vector y preguntamos cuál desea eliminar
                //si no hay tortugas no hay nada que pintar ni eliminar
                if (vt.size() == 0)
                    throw new Exception();
                pintamosVector(vt);
                //TODO añadimos una última opción por si se ha equivocado y no quiere eliminar tortugas??
                System.out.println("¿Qué tortuga deseas eliminar?");
                int decision = devolvemosIntegerDeDimensionDeVector(vt.size());
                //creamos objeto q pasará opción de eliminar indicando que sí queremos eliminar y la posición a eliminar

                HandleOptionConnection _todelete = new HandleOptionConnection(2,true, decision);
                //lo enviamos al server que está esperando elemento
                ObjectOutputStream ostd = new ObjectOutputStream(socket.getOutputStream());
                ostd.writeObject(_todelete);



                DataInputStream entradaCliente = new DataInputStream(socket.getInputStream());
                System.out.println(entradaCliente.readUTF());



            } catch (Exception e) {
                    System.out.println("No hay tortugas para Eliminar.\n\n");
            }


           // DataInputStream entradaCliente = new DataInputStream((socket.getInputStream()));
            //System.out.println(entradaCliente.readUTF());
        } catch (Exception e) {}


    }
    private void choicedThree()
    {
        HandleOptionConnection hc = new HandleOptionConnection(3);
        try
        {
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject(hc);

            //recibiremos vector de turtles
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            try
            {
                Vector<Turtle> vt = (Vector<Turtle>)ois.readObject();
                if (vt.size() == 0) {
                    System.out.println("No hay tortugas para pintar");
                    return;
                }
                pintamosVector(vt);
            } catch (Exception e){
                System.out.println("por qué estoy aquí?");
            }

        } catch (Exception e) {

        }




    }
    private void choicedFour(){
        HandleOptionConnection hc = new HandleOptionConnection(4);
        try
        {
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject(hc);

            DataInputStream data = new DataInputStream(socket.getInputStream());
            System.out.println(data.readUTF());
        } catch (Exception e){}

    }

    private static int devolvemosIntegerDeDimensionDeVector(int cantidad)
    {
        Integer decision = 0;
        boolean exit = false;
        do {

            try {
                decision = Integer.parseInt((reader.nextLine()));
                if (decision < 1 || decision > cantidad)
                   throw new Exception();
                else
                    exit = true;
            } catch (Exception e){
                if (cantidad == 1)
                    System.out.println("Solo hay una tortuga en la lista... dale al uno si quieres borrarla");
                else
                    System.out.println("Introduce un número entre 1 y " + cantidad);
            }

        } while (!exit);
        System.out.println("saliendo");
        return (decision - 1);
    }

    private static int devolvemosIngegerDeReaderDorsal(){
        Integer decision = 0;
        boolean exit = false;
        do {
            try{
                decision = Integer.parseInt(reader.nextLine());
                if (decision < 1)
                    throw new Exception();
                else
                    exit = true;
            } catch (Exception e) {
                System.out.print("Introduce un número entero positivo:");

            }
        } while (!exit);
        return  decision;
    }

    private static int devolvemosIntegerDeReaderMenuPrincipal()
    {
        Integer decision = 0;
        boolean exit = false;
        do {

                try
                {
                    System.out.print("Opción:");
                    decision = Integer.parseInt(reader.nextLine());
                   // decision = reader.nextInt();
                    if (decision < 1 || decision > 5)
                        throw new Exception();
                    else
                        exit = true;
                } catch (Exception e){
                    System.out.println("Introduce un número entero entre 1 y 5" +
                            "\n-----------------------------------------------");

                    preguntamosOpciones();
                }
            } while (!exit) ;
        return decision;
    }

    private static void preguntamosOpciones()
    {
        System.out.println("1. Introducir nueva tortuga" +
                "\n2. Eliminar una tortuga" +
                "\n3. Mostrar tortugas" +
                "\n4. Iniciar carrera" +
                "\n5. Salir.");
    }

    private void closeTurtleRun() {


        //mandamos mensaje de cierre al servidor
        HandleOptionConnection hc = new HandleOptionConnection(5);
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream((socket.getOutputStream()));
            outputStream.writeObject(hc);
            DataInputStream entradaCliente = new DataInputStream((socket.getInputStream()));
            System.out.println(entradaCliente.readUTF());
        } catch (Exception e) {}



        System.out.println("Gracias por ponerme un 10.......  :)");
        exitMenu = true;
    }


    private static void pintamosVector(Vector<Turtle> vectorTurtles)
    {
        System.out.println("Listado de Tortugas\n- - - - - - - - - - - - -");
        for ( int i = 0; i < vectorTurtles.size(); i++){
            System.out.println("Tortuga " + (i+1) + " Nombre: " + vectorTurtles.elementAt(i).getName() + " con Dorsal: " + vectorTurtles.elementAt(i).getDorsal());
        }
        if (vectorTurtles.size() == 0)
            System.out.println("No se ha inscrito ninguna tortuga a esta gran carrera..... :(");
        System.out.println("- - - - - - - - - - - - -");

    }

}
