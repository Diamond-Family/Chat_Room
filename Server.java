import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server
{

    private ServerSocket serverSocket;

    public Server(ServerSocket serverSocket)
    {
        this.serverSocket= serverSocket;
    }
    public void StartServer()
    {
        try
        {
            while(!serverSocket.isClosed())
            {
                Socket socket = serverSocket.accept();
                ClientHandler clienthandler = new ClientHandler(socket);
                System.out.print(clienthandler.UserName);
                System.out.println(" Is Connected");
                Thread thread = new Thread(clienthandler);
                thread.start();

            }
        }catch(IOException e)
        {
            closeServerSocket();
        }
    }
    public void closeServerSocket() {
        try{
            if (serverSocket != null) {
                serverSocket.close();
            }
        }catch (IOException e){ e.printStackTrace();}
    }
    //                                                             Main Function
    public static void main(String[] args) throws IOException {
        System.out.println();
        ServerSocket serverSocket = new ServerSocket(1802);
        Server server = new Server(serverSocket);
        server.StartServer();
    }
}

class ClientHandler implements Runnable {
    public static ArrayList<ClientHandler> clientHandler = new ArrayList<>();
    private Socket socket;
    private BufferedReader bufferedreader;
    private BufferedWriter bufferedwriter;
    private String clientUserName;
    public String UserName;
    public ClientHandler(Socket socket)
    {
        try{
            this.socket = socket;
            this.bufferedwriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedreader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.clientUserName = bufferedreader.readLine();
            clientHandler.add(this);
            broadcastMessage("\n-----------------------------\nSERVER has Added ["+clientUserName+"] User \n-----------------------------");            //Send message to group of Chat
            this.UserName = clientUserName;
        }catch (IOException e)
        {
            closeEverything(socket, bufferedreader , bufferedwriter);
        }

    }
   
