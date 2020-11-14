package services;
import commands.CommandRequest;
import user.User;

import static java.lang.System.out;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.util.HashMap;
import java.util.Map;

import static commands.CommandRequest.*;
public class SocketService extends Thread{

    private static ServerSocket serverSocket;
    private Socket socket;
    static Map MAP_USERS = new HashMap();

    public SocketService(Socket socket) {
        this.socket = socket;
    }


    public void run() {
        try {
            ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
            Object obj = null;

            while (true) {

                if (obj instanceof String) {
                    if (((String) obj).equals("Q")) {
                        break;
                    }else if (((String) obj).equals("T")) {
                        serverSocket.close();
                        break;
                    }
                    out.println("[payload] Unexpected command." );
                } else if (obj instanceof Payload) {
                    out.println("[payload] " + ((Payload) obj).getData());
                    ((Payload) obj).setData("(modification) " + ((Payload) obj).getData());
                    output.writeObject(obj);
                }else {
                    out.println("[payload] Unexpected data.");
                    break;
                }
                socket.close();
                output.close();
                input.close();
            }
        } catch (IOException jb) {
            jb.printStackTrace();
        }
    }

    public class ListenerSocket implements Runnable {
        // receber os dados de saida
        private DataOutputStream output;
        //receber os dados de entrada
        private ObjectInputStream input;
        private User user;
        private Nodes nodes;

        public ListenerSocket(Socket socket) {
            try {
                input = new ObjectInputStream(socket.getInputStream());
                output = new DataOutputStream(socket.getOutputStream());
            }catch (IOException dd) {
                dd.printStackTrace();
            }
        }
        public synchronized void run() {
            CommandRequest message;

            try {
                  while ((message = (CommandRequest) input.readObject()) !=null) {
                   if (message.getCmd1().equals(R)) {
                       out.println("Chave existente.");
                   }else if (user != null) {
                      nodes.put(message, output);
                      out.println("Item registado com sucesso.");
                   }else {
                       out.println("Ocorreu um erro.");
                   }if (message.getCmd1().equals(C)) {
                       nodes.findEntry(message);
                      }else if (message.equals(message)){
                         out.println(message);
                      }else if (message == null) {
                          out.println("Chave inexistente.");
                      }else {
                       out.println("Ocorreu um erro.");
                      }if (message.getCmd1().equals(D)) {
                          nodes.remove(message);
                          out.println("Item removido com sucesso.");
                      }else if(message == null) {
                       out.println("Chave inexistente.");
                      }else {
                       out.println("Ocorreu um erro");
                      }if (message.getCmd1().equals(L)) {
                       nodes.size();
                       out.println(nodes);

                      }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            }
        }
    }
    public static void main(String[] args) throws IOException {
        int port = Integer.parseInt(args[0]);
        out.println(port);
        try {
            serverSocket = new ServerSocket(port);
        }catch (IOException e) {
            e.printStackTrace();
        }
        while (!serverSocket.isClosed()) {
            try {
                Socket socket = serverSocket.accept();
                out.println("Conectado a: " + socket.getInetAddress().getHostName() +
                " @ " + socket.getInetAddress().getHostAddress() +
                        " " + socket.getLocalPort() + " : " + socket.getPort());
                new SocketService(socket).start();
            }catch (IOException jb) {
                if (serverSocket.isClosed()) {
                }else {
                    jb.printStackTrace();
                }
            }
        }
    }

}
