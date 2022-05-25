import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;

public class Main {

    public static void main(String[] args) throws IOException {

        Socket socket = null;
        InputStreamReader inputStreamReader = null;
        OutputStreamWriter outputStreamWriter = null;
        BufferedReader bufferedReader = null;
        BufferedWriter bufferedWriter = null;
        ServerSocket serverSocket = null;

        byte[] sign1 = new byte[0];
        byte[] sign2= new byte[1];
        String mess1="";
        String mess2 ="";

        serverSocket = new ServerSocket(4444);

        while (true){
            try{
                socket = serverSocket.accept();

                inputStreamReader = new InputStreamReader(socket.getInputStream());
                outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());

                bufferedReader = new BufferedReader(inputStreamReader);
                bufferedWriter = new BufferedWriter(outputStreamWriter);

                while (true){

                    String msg = bufferedReader.readLine();
                    if (msg == null)
                        break;
                    String [] actual = msg.split(",");
                    String key = actual[0];
                    String signed = actual[1];
                    String mess = actual[2];
                    String I = actual[3];
                    if (I.equals("1")){
                        System.out.println("First message: \n" +
                                key+signed+mess);
                        sign1 = Base64.getDecoder().decode(signed);
                        mess1 = mess;
                        byte[] key1 = Base64.getDecoder().decode(key);
                        PublicKey pub = KeyFactory.getInstance("DSA").generatePublic(new X509EncodedKeySpec(key1));
                        byte[] sign = Base64.getDecoder().decode(signed);
                        String aa = validateMessageSignature(pub, mess.getBytes(), sign);
                         bufferedWriter.write(aa);

                    }else if (I.equals("2")){
                        System.out.println("Second message: \n" +
                                key+signed+mess);
                        sign2 = Base64.getDecoder().decode(signed);
                        mess2=mess;
                    }

                    bufferedWriter.write("Received");
                    bufferedWriter.newLine();
                    bufferedWriter.flush();

                    if (I.equals("2")) {
                        System.out.println("______________________________________");
                        System.out.println("ARRAYS: ");
                        System.out.println(Arrays.toString(sign1) + "\n" + Arrays.toString(sign2));
                        System.out.println("______________________________________");
                        System.out.println("Message: ");
                        System.out.println(mess1 + "\n" + mess2);
                        System.out.println("______________________________________");
                        if (Arrays.equals(sign1, sign2) && mess1.equals(mess2)) {
                            System.out.println("Messages ARE the same");
                        } else {
                            System.err.println("Messages are NOT the same");
                        }
                        System.out.println("______________________________________");
                    }
                }

                socket.close();
                inputStreamReader.close();
                outputStreamWriter.close();
                bufferedReader.close();
                bufferedWriter.close();

            } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException | InvalidKeyException | SignatureException exception) {
                exception.printStackTrace();
            }
        }
    }

    public static String validateMessageSignature(PublicKey publicKey, byte[] message, byte[] signature) throws
            NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature clientSig = Signature.getInstance("DSA");
        clientSig.initVerify(publicKey);
        clientSig.update(message);
        if (clientSig.verify(signature)) {
            System.out.println("The First message is properly signed.");
            return "The message is properly signed.";
        } else {
            System.err.println("It is not possible to validate the signature.");
            return "It is not possible to validate the signature.";
        }
    }
}
