package services;

import commands.CommandRequest;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.util.Scanner;

import static java.lang.System.in;
import static java.lang.System.out;
import static commands.CommandRequest.*;

public class SocketClient {

    private Socket socket;

    //private ObjectOutputStream output;
    //private CommandRequest request;



    public static void main(String[] args) {

        try {

            Socket socket = new Socket("localhost", 4554);
            ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
            Payload payload = new Payload("LAB2");
            output.writeObject(payload);
            Scanner sn = new Scanner(in);
            out.println("Ligação estabelecida");
            String message = sn.nextLine();



        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void actions(String input) throws IOException {
        CommandRequest message = new CommandRequest();
        String inputLine[]= input.split(" ");
        String aux = " ";
        for (int i = 0; i < inputLine.length; i++) {
            if (i == 0) {
                message.setCmd1(inputLine[0]);
            }else if (i == 1) {
                message.setCmd2(inputLine[1]);
            }else {
                aux = aux.concat(inputLine[i] + " ");
                message.setCmd3(aux.trim());
            }
        }
        if (!input.equals("")) {
            sendMessage(message);
        }
    }
    private void sendMessage(CommandRequest message) throws IOException {
        ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
        output.writeObject(message);
        output.flush();
    }
    public void initSocket() throws IOException {

        while (true) {
            Scanner sn = new Scanner(in);
            out.println("Ligação estabelecida");
            String message = sn.nextLine();

            if (message.equals(R)) {
                actions(message);
            }else if (message.equals(C))  {
                actions(message);
            }else if (message.equals(D)) {
                actions(message);
            }else if (message.equals(L)) {
                actions(message);
            }else if (message.equals(Q)) {
                socket.close();
            }
        }

    }
    public SocketClient() {
        try {
            initSocket();
        } catch (NullPointerException e) {
            System.out.println("without connection");
        } catch (IOException e) {
            System.out.println("failed connection " + e.getMessage());
        }
    }
    private void receiveMessage() throws IOException {


        final DataInputStream input = new DataInputStream(socket.getInputStream());

        final Clientes clientes = new Clientes();


        Thread thread = new Thread() {
            @Override
            public void run() {
                String message;
                try {
                    while ((message = input.readUTF()) != null) {

                        if (message.equals(R)) {
                            clientes.confirmarRegisto(message);
                        } else if (message.equals(C)) {
                            clientes.consulta(message);
                        } else if (message.equals(D)) {
                            clientes.remover(message);
                        } else if (message.contains(L)) {
                            clientes.lista();

                        }
                    }
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }

            }
        };
        thread.start();
    }

}
