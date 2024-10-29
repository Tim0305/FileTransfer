/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Cliente;

import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import lz77.descompresor.Descompresor;

/**
 *
 * @author Timoteo
 */
public class Cliente extends Thread {

    private Socket s;

    public Cliente(Socket s) {
        super();
        this.s = s;
    }

    @Override
    public void run() {
        try {
            DataInputStream entrada = new DataInputStream(s.getInputStream());

            while (true) {
                // Logica de comunicacion con el cliente para recibir el archivo
                if (entrada.available() > 0) {

                    // Leer el nombre del archivo
                    int numBytesFileName = entrada.readInt();
                    byte[] fileName = new byte[numBytesFileName];
                    entrada.read(fileName);
                    String fileNameString = new String(fileName);

                    // Leer el contenido del archivo
                    int numPaquetes = entrada.readInt();
                    List<Character> paquetes = new ArrayList<>();

                    for (int i = 0; i < numPaquetes; i++) {
                        paquetes.add(entrada.readChar());
                    }

                    System.out.println(paquetes.size());

                    // Controlador para escribir archivos
                    FileOutputStream fos = new FileOutputStream("files/" + fileNameString);
                    // Escribimos los bytes del archivo

                    String content = Descompresor.descomprimir(paquetes);
                    fos.write(Base64.getDecoder().decode(content));

                    System.out.println("Archivo creado correctamente -> " + fileNameString);

                    // Terminar el hilo del servidor
                    entrada.close();
                    fos.close();
                    s.close();
                    return;
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);

        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
