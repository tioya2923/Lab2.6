package user;

import java.util.ArrayList;
import java.util.List;

public class User {
    //private String login;
    private String enderecoIp;
    private String porta;
    private List participantes = new ArrayList();

    public String toString() {
        return "Participante{" + "Usuário= " + enderecoIp +
                "Endereço Ip" + participantes + "=  Participantes" + "}";
    }
}
