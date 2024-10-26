package lz77;

import java.util.ArrayList;
import java.util.List;
import lz77.descompresor.Descompresor;
import lz77.compresor.Compresor;

public class Lz77Compresor {

    public static String comprimir(String texto) {

        List<Character> listaPaquetes = Compresor.comprimir(texto);
        StringBuilder stringBuilder = new StringBuilder();
        for (Character paquete : listaPaquetes) {
            int caracter = (paquete & 0xFF00) >> 8;
            int constantes = paquete & 0xFF;
            stringBuilder.append(caracter);
            stringBuilder.append(",");
            stringBuilder.append(constantes);
            stringBuilder.append(" ");
        }

        return stringBuilder.toString().trim();
    }

    public static String descomprimir(String textoComprimido) {
        String[] componentes = textoComprimido.split(" ");

        List<Character> listaPaquetes = new ArrayList<>();

        for (String componente : componentes) {
            String[] paquete = componente.split(",");
            int caracter = Integer.parseInt(paquete[0]);
            int constante = Integer.parseInt(paquete[1]);
            char p = (char) ((caracter << 8) + constante);
            listaPaquetes.add(p);
        }

        return Descompresor.descomprimir(listaPaquetes);
    }
}
