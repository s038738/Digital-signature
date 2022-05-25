import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

    public static void main(String[] args) throws IOException {
        Socket socket = null;
        InputStreamReader inputStreamReader = null;
        OutputStreamWriter outputStreamWriter = null;
        BufferedReader bufferedReader = null;
        BufferedWriter bufferedWriter = null;
        ServerSocket serverSocket = null;
        serverSocket = new ServerSocket(1234);
        while (true){
            try{
                socket = serverSocket.accept();

                inputStreamReader = new InputStreamReader(socket.getInputStream());
                outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());

                bufferedReader = new BufferedReader(inputStreamReader);
                bufferedWriter = new BufferedWriter(outputStreamWriter);
                while (true){
                    String msg = bufferedReader.readLine();
                    System.out.println("CL: " + msg);
                    bufferedWriter.write("Recieved");
                    Sender sender = new Sender(msg);
                    bufferedWriter.newLine();
                    bufferedWriter.flush();

                    if (msg.equalsIgnoreCase("BYE"))
                        break;

                }
                socket.close();
                inputStreamReader.close();
                outputStreamWriter.close();
                bufferedReader.close();
                bufferedWriter.close();

            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }
}
