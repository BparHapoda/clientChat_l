
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {


    private ServerSocket serverSocket;
    private int port;

    private ArrayList <ClientService> clients =new ArrayList<>();
    public Server (int port) {

        this.port = port;

        try (ServerSocket serverSocket =new ServerSocket(port))
        {
            System.out.println("Сервер стартовал на порту " + port);
            while (true) {
                Socket socket = serverSocket.accept();

                System.out.println("Клиент успешно подключился");
                addClient(new ClientService(socket,this));
            }
        }catch (IOException e){throw new RuntimeException("Подключение клиента не удалось");}
    }

public void addClient(ClientService client){clients.add(client);}

    public void deleteClient (ClientService client){
        clients.remove(client);
    }

    public int getPort() {
        return port;
    }
    public void sendMessageForAllClients (String message,ClientService someself){
        for (ClientService client:clients
             ) {
            if (!client.equals(someself))
            client.sendMessage(message);
        }
    }
}