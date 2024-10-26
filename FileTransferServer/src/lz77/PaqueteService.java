package lz77;

import lz77.compresor.DatosIteracionCompresion;
import java.util.ArrayList;
import java.util.List;

public class PaqueteService {

    public static List<Character> empaquetar(List<DatosIteracionCompresion> datosIteracionCompresion) {
        List<Character> listPaquetes = new ArrayList<>();

        for (DatosIteracionCompresion dto : datosIteracionCompresion) {
            char paquete = 0;
            paquete += dto.caracter() << 8;
            paquete += dto.posicion() << 5;
            paquete += dto.repeticiones();
            listPaquetes.add(paquete);
        }

        return listPaquetes;
    }

    public static List<DatosIteracionCompresion> desempaquetar(List<Character> listPaquetes) {
        List<DatosIteracionCompresion> listaDatosIteracionCompresions = new ArrayList<>();

        for (Character paquete : listPaquetes) {
            char caracter = getCaracter(paquete);
            int posicion = getPosicion(paquete);
            int repeticiones = getRepeticiones(paquete);
            listaDatosIteracionCompresions.add(new DatosIteracionCompresion(caracter, posicion, repeticiones));
        }

        return listaDatosIteracionCompresions;
    }

    public static char getCaracter(char paquete) {
        char caracter = (char) ((paquete & 0xFF00) >> 8);
        return caracter;
    }

    public static int getPosicion(char paquete) {
        int posicion = (paquete & 0xE0) >> 5;
        return posicion;
    }

    public static int getRepeticiones(char paquete) {
        int repeticion = (paquete & 0x1F);
        return repeticion;
    }
}
