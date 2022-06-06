import java.net.Socket;
import java.io.*;
import java.util.Scanner;

public class Client
{
    private Socket socket;                 //commoncation link between two program run in a local computer or network
    private BufferedReader bufferedReader; // read text using buffer
    private BufferedWriter bufferedWriter; // write text to outputstream
    private String username;
    public Client(Socket socket , String username)
    {
        try {
            this.socket = socket;                        // store char to byte     // return output for socket
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.username = username;
        }catch(IOException e){
            closeEverything(socket , bufferedReader, bufferedWriter);
        }
    }
    
    public void sendMessage()
    {
        try{
            bufferedWriter.write(username);
            bufferedWriter.newLine();
            bufferedWriter.flush();       // send or write all contents of buffer to distanition and buffer make empty

            Scanner scanner = new Scanner(System.in);
            
            while(socket.isConnected())
            {       
                System.out.print(username+"==> ");
                String messageToSend = scanner.nextLine();
                bufferedWriter.write("\n"+username +"==> "+messageToSend);
                bufferedWriter.newLine();
                bufferedWriter.flush();
                
            }
        }catch (IOException e){
            closeEverything(socket , bufferedReader , bufferedWriter);
        }
    }
   
    public void listenToMessage()
    {                  //instance can run as thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                String msgFromGroupChat;
                while(socket.isConnected())
                {
                    try{
                        msgFromGroupChat = bufferedReader.readLine();
                        System.out.println(msgFromGroupChat);
                    }catch(IOException e)
                    {
                        closeEverything(socket , bufferedReader , bufferedWriter);
                    }
                }
            }
        }).start();
    }
    public void closeEverything(Socket socket , BufferedReader bufferReader , BufferedWriter bufferedWritter)
    {
        try
        {
            if(bufferReader != null)
            {
                bufferReader.close();
            }
            if(bufferedWritter != null)
            {
                bufferedWritter.close();
            }
            if(socket != null)
            {
                socket.close();
            }
        }catch (IOException e)
        {
            e.printStackTrace(); // to handle error and exception
        }
    }
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your username group Chat: ");
        String username = scanner.nextLine();
        System.out.print("Enter port number for peer2peer connection: ");
        int port_no = scanner.nextInt();
        System.out.println("-------------------------------------\n");
        Socket socket = new Socket("localhost", port_no);
        Client client = new Client( socket , username );
        client.listenToMessage();
        client.sendMessage();
    }
}
