package services;
import static java.lang.System.out;
import static commands.CommandResponse.*;
public class Clientes {

    public void confirmarRegisto(String resposta) {
        if (resposta.equals(R)) {
           out.println("Item registado com sucesso.");
        }else if (resposta.contentEquals(RN)) {
            out.println("Chave existente.");
        }else if (resposta != resposta) {
            out.println();
        }

    }

    public void consulta(String message) {
    }

    public void remover(String message) {
    }

    public void lista() {
    }
}
