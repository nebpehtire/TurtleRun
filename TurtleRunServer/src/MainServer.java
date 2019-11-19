import java.io.IOException;

public class MainServer {
    //inicializamos el server

    public static void main(String[] args) throws IOException {
        Server servidor = new Server();
        servidor.iniciarServer();

    }
}
