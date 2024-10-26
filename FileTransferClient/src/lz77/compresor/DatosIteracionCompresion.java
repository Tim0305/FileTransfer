package lz77.compresor;

public record DatosIteracionCompresion(char caracter, int posicion, int repeticiones) {

    @Override
    public String toString() {
        return "DatosIteracionCompresion{" + "character=" + caracter + ", posicion=" + posicion + ", repeticiones=" + repeticiones + '}';
    }

}
