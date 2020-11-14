package services;

import commands.CommandRequest;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import static commands.CommandRequest.*;
import static java.lang.System.in;
import static java.lang.System.out;

public class SocketClient1 {

    public static void main(String[] args) {
        new SocketClient1();
    }

    public SocketClient1() {
        String testeServerName = "localhost";
        int port = 4555;

        try {
            Socket socket = new Socket(testeServerName, port);
            String result = writerRead(socket, "\n\n");
            out.println(result);
            socket.close();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String writerRead(Socket socket, String message) throws IOException {
        ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
        output.writeObject(message);
        output.flush();

        while (true) {
            Scanner sn = new Scanner(in);
            out.println("Ligação estabelecida");
            String message1 = sn.nextLine();

            if (message.equals(R)) {
                actions(message1);
            }else if (message.equals(C))  {
                actions(message1);
            }else if (message.equals(D)) {
                actions(message1);
            }else if (message.equals(L)) {
                actions(message1);
            }else if (message.equals(Q)) {
                socket.close();
            }
            receiveMessage();
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
        Socket socket = new Socket();
        ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
        //ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
        output.writeObject(message);
        output.flush();
    }

    private void receiveMessage() throws IOException {
        Socket socket = new Socket();
        //recebe os dados enviados pelo servidor
        final DataInputStream input = new DataInputStream(socket.getInputStream());

        final Clientes clientes = new Clientes();

        //thread que fica escutando o servidor
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
