package lz77.descompresor;

import java.util.List;
import lz77.PaqueteService;
import lz77.compresor.DatosIteracionCompresion;

public class Descompresor {

    public static String descomprimir(List<Character> listPaquetes) {
        int index = 0;

        List<DatosIteracionCompresion> listDatosIteracionCompresion = PaqueteService.desempaquetar(listPaquetes);
        StringBuilder stringBuilder = new StringBuilder();

        for (DatosIteracionCompresion datosIteracionCompresion : listDatosIteracionCompresion) {
            if (datosIteracionCompresion.posicion() != 0) {
                if (datosIteracionCompresion.repeticiones() != 0) {
                    for (int i = 0, j = index - datosIteracionCompresion.posicion(); i < datosIteracionCompresion.repeticiones(); i++, index++, j++) {
                        stringBuilder.append(stringBuilder.toString().charAt(j));
                    }
                    stringBuilder.append(datosIteracionCompresion.caracter());
                } else {
                    stringBuilder.append(stringBuilder.toString().charAt(index - datosIteracionCompresion.posicion()));
                }
            } else {
                stringBuilder.append(datosIteracionCompresion.caracter());
            }
            index++;
        }

        return stringBuilder.toString();
    }
}
