package commands;

import java.io.Serializable;

public class CommandRequest implements Serializable {

    public static final String R = "R";
    public static final String cn = "cn";
    public static final String C = "C";
    public static final String L = "L";
    public static final String D = "D";
    public static final String Q = "Q";

    private String cmd1;
    private String cmd2;
    private String cmd3;

    public String getCmd1() {
        return cmd1;
    }

    public String getCmd2() {
        return cmd2;
    }

    public String getCmd3() {
        return cmd3;
    }

    public void setCmd1(String cmd1) {
        this.cmd1 = cmd1;
    }

    public void setCmd2(String cmd2) {
        this.cmd2 = cmd2;
    }

    public void setCmd3(String cmd3) {
        this.cmd3 = cmd3;
    }

    public static void help() {

        System.out.println("R -Item registado com sucesso");
        System.out.println("C -Consulta o valor de uma chave");
        System.out.println("L -Lista todos os participantes");
        System.out.println("D -Elimina um par-chave valor");
        System.out.println("Q -Termina o programa");

    }



}

