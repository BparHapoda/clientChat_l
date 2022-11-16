import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Objects;

public class ClientService {
    private DataInputStream in;
    private DataOutputStream out;
    private Socket socket;
private Server server;
    public ClientService (Socket socket,Server server) {
        this.server=server;
        this.socket = socket;
        try {
            this.in = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        new Thread(() ->
        {try{
            while (true) {
                String message = null;

                message = in.readUTF();
                server.sendMessageForAllClients(message,this);}}
        catch (IOException e){throw new RuntimeException
                ("Соединение с клиентом "+socket.toString()+"прервано.Клиент отключен");}
            finally {
            closeStreams();
        }

        }).start();
    }
public void sendMessage (String message){
    try {
        out.writeUTF(message);
    } catch (IOException e) {
        throw new RuntimeException(e);
    }
}
public void closeStreams (){
        server.deleteClient(this);
        if (in!=null){
            try {
                in.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    if (out!=null){
        try {
            out.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    if (socket!=null){
        try {
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientService that = (ClientService) o;
        return Objects.equals(in, that.in) && Objects.equals(out, that.out) && Objects.equals(socket, that.socket);
    }

    @Override
    public int hashCode() {
        return Objects.hash(in, out, socket);
    }
}