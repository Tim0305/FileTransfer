package lz77.compresor;

import lz77.PaqueteService;
import java.util.ArrayList;
import java.util.List;

public class Compresor {

    private static int startIndexArray;
    private static int endIndexArray;
    private static int index;
    private static int length;

    public static List<Character> comprimir(String texto) {

        List<DatosIteracionCompresion> listDatosIteracionCompresions = new ArrayList<>();

        // Agregamos un caracter nulo al final del string
        texto = texto + (char) 0;

        length = texto.length();
        index = 0;
        startIndexArray = 0;
        endIndexArray = index + 5;

        int posicionCaracter = 0;
        int repeticiones = 0;
        char caracter = 0;

        // Length - 1 porque el maximo al que puede llegar es l - 1 y es necesario para que se corte el ciclo
        // ya que en un for se para el ciclo hasta que el iterador no cumpla la condicion
        for (index = 0; index < length - 1; recorrerArreglo(repeticiones + 1)) {

            /*
            Cuerpo del metodo para comprimir el texto de acuerdo a los indices

            Arreglo de comparacion = texto[startIndexArray] -> texto [i - 1]
            Arreglo del texto original = texto[i] -> texto[endIndexArray]
             */
            repeticiones = 0;
            caracter = texto.charAt(index);
            posicionCaracter = existsInArray(texto, startIndexArray, index - 1, caracter);

            if (posicionCaracter != -1) {
                posicionCaracter = getPosition(posicionCaracter, index - 1);
                repeticiones = getRepeticiones(startIndexArray, endIndexArray, index, texto);

                listDatosIteracionCompresions.add(new DatosIteracionCompresion(texto.charAt(repeticiones + index), posicionCaracter, repeticiones));

            } else {
                listDatosIteracionCompresions.add(new DatosIteracionCompresion(caracter, 0, 0));
            }
        }
        List<Character> listaPaquetes = PaqueteService.empaquetar(listDatosIteracionCompresions);
        return listaPaquetes;
    }

    private static int getPosition(int index, int endIndex) {
        return endIndex - index + 1;
    }

    private static int existsInArray(String array, int startIndex, int endIndex, char c) {
        for (int i = startIndex; i <= endIndex; i++) {
            if (array.charAt(i) == c) {
                return i;
            }
        }
        return -1;
    }

    private static int getRepeticiones(int startIndex, int endIndex, int index, String text) {
        int repeticiones = 0;
        for (int i = index, j = startIndex; i <= endIndex; i++, j++) {
            if (text.charAt(i) != text.charAt(j)) {
                break;
            } else {
                repeticiones++;
            }
        }

        return repeticiones;
    }

    private static void recorrerArreglo(int pos) {

        endIndexArray += pos;

        if (endIndexArray >= length - 1) {
            endIndexArray = length - 1;
        }

        index += pos;

        if (index >= endIndexArray) {
            index = endIndexArray;
        }

        /*
            Cuando el indice del arreglo que se utiliza para comparar llegue a mas qu 7 se incrementa el indice de
            inicio del mismo array para que en total sean 7 caracteres a comparar
         */
        if (index > 7) {
            startIndexArray += pos;
        }
        /*
            Cuando el indice del final del arreglo de la cadena original llegue al final del string
            tenemos que evitar que el indice incremente
         */
    }
}
