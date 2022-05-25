import java.io.*;
import java.math.BigInteger;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SignatureException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Random;
import java.util.Scanner;

public class Sender {

    Sender(String message){
        Socket socket = null;
        InputStreamReader inputStreamReader = null;
        OutputStreamWriter outputStreamWriter = null;
        BufferedReader bufferedReader = null;
        BufferedWriter bufferedWriter = null;
        try {
            socket = new Socket("localhost", 4444);
            inputStreamReader = new InputStreamReader(socket.getInputStream());
            outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());
            bufferedReader = new BufferedReader(inputStreamReader);
            bufferedWriter = new BufferedWriter(outputStreamWriter);

            String msg = message;

            String [] actual = msg.split(",");
            String key = actual[0];
            String signed = actual[1];
            String mess = actual[2];
            String I = actual[3];

            System.out.println("Options: ");
            System.out.println("a: Send the same message");
            System.out.println("b: Change Name");
            System.out.println("c: Change sign array");
            Scanner scan = new Scanner(System.in);
            String choice = scan.nextLine();
            do {
                switch (choice){
                    case "a":
                        String msgToSend = key +
                                ","+ signed +
                                "," + mess +
                                "," + "2";

                        bufferedWriter.write(msgToSend);
                        bufferedWriter.newLine();
                        bufferedWriter.flush();
                        String bb = bufferedReader.readLine();
                        System.out.println("Response: "+ bb);
                        choice = "q";
                        break;
                    case "b":
                        System.out.println("Current name: " + mess);
                        System.out.println("Your message choice: ");
                        String mess2 = scan.nextLine();

                        String msgToSend1 = key +
                                ","+ signed +
                                "," + mess2 +
                                "," + "2";

                        bufferedWriter.write(msgToSend1);
                        bufferedWriter.newLine();
                        bufferedWriter.flush();
                        bb = bufferedReader.readLine();
                        System.out.println("Response: "+ bb);
                        choice = "q";
                        break;
                    case "c":

                        byte [] arr = Base64.getDecoder().decode(signed);
                        System.out.println("Current Byte array: \n" + Arrays.toString(arr));
                        Random random = new Random();
                        long answer = random.nextInt(10) + 1;
                        System.out.println("Byte added to array: " + answer);
                        byte [] arr2 = BigInteger.valueOf(answer).toByteArray();


                        byte[] result = new byte[arr.length + arr2.length];
                        System.arraycopy(arr, 0, result, 0, arr.length);
                        System.arraycopy(arr2, 0, result, arr.length, arr2.length);
                        System.out.println("New Byte array: \n" + Arrays.toString(result));

                        String signedMessage = Base64.getEncoder().withoutPadding().encodeToString(result);

                        String msgToSend2 = key +
                                ","+ signedMessage +
                                "," + mess +
                                "," + "2";

                        bufferedWriter.write(msgToSend2);
                        bufferedWriter.newLine();
                        bufferedWriter.flush();
                        bb = bufferedReader.readLine();
                        System.out.println("Response: "+ bb);
                        choice = "q";
                        break;
                }  }while (choice != "q");

        } catch (IOException e) {
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
}
