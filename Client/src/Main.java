import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.*;
import java.util.Base64;
import java.util.Scanner;

public class Main {
    public static void main(String args[]) {
        Socket socket = null;
        InputStreamReader inputStreamReader = null;
        OutputStreamWriter outputStreamWriter = null;
        BufferedReader bufferedReader = null;
        BufferedWriter bufferedWriter = null;
        try {
            socket = new Socket("localhost", 1234);
            inputStreamReader = new InputStreamReader(socket.getInputStream());
            outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());
            bufferedReader = new BufferedReader(inputStreamReader);
            bufferedWriter = new BufferedWriter(outputStreamWriter);

            Scanner scanner = new Scanner(System.in);

            while (true){
                String message = scanner.nextLine();
                SignerUser  signer = new SignerUser ();
                byte[] sign = signMessage(message.getBytes(), signer.getPrivateKey());

                PublicKey pubKey = signer.getPubKey();

                byte[] pubSend = pubKey.getEncoded();
                String pubToSend = Base64.getEncoder().withoutPadding().encodeToString(pubSend);
                String signedMessage = Base64.getEncoder().withoutPadding().encodeToString(sign);

                String msgToSend =pubToSend +
                        ","+signedMessage +
                        ","+message +
                        "," + "1";

                bufferedWriter.write(msgToSend);
                bufferedWriter.newLine();
                bufferedWriter.flush();

                SendToThird sendToThird = new SendToThird();
                sendToThird.sendTo(msgToSend);

                System.out.println("Se: "+ bufferedReader.readLine());

                if(msgToSend.equalsIgnoreCase("BYE")){
                    break;
                }
            }
        } catch (IOException | NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {
            e.printStackTrace();
        } finally {
            try {
                if(socket != null){
                    socket.close();
                }
                if(inputStreamReader != null){
                    inputStreamReader.close();
                }
                if (outputStreamWriter != null){
                    outputStreamWriter.close();
                }
                if (bufferedReader != null){
                    bufferedReader.close();
                }
                if (bufferedWriter != null){
                    bufferedWriter.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }



    public static void validateMessageSignature(PublicKey publicKey, byte[] message, byte[] signature) throws
            NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature clientSig = Signature.getInstance("DSA");
        clientSig.initVerify(publicKey);
        clientSig.update(message);
        if (clientSig.verify(signature)) {
            System.out.println("The message is properly signed.");
        } else {
            System.err.println("It is not possible to validate the signature.");
        }
    }

    public static byte[] signMessage(byte[] message,PrivateKey privateKey) throws NoSuchAlgorithmException,
            InvalidKeyException, SignatureException {
        Signature sig = Signature.getInstance("DSA");
        sig.initSign(privateKey);
        sig.update(message);
        byte[] sign= sig.sign();
        return sign;
    }



}
