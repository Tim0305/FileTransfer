/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Cliente;

import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.ArrayList;
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
            InputStream inputStream = s.getInputStream();
            DataInputStream entrada = new DataInputStream(inputStream);

            while (true) {
                if (inputStream.available() > 0) {
                    // Logica de comunicacion con el cliente para recibir el archivo
                    int numBytesFileName = entrada.readInt();
                    long numBytesContent = entrada.readLong();
                    byte[] fileName = new byte[numBytesFileName];
                    List<Character> paquetes = new ArrayList<>();

                    inputStream.read(fileName);
                    System.out.println(numBytesContent);
                    for (int i = 0; i < numBytesContent; i++) {
                        Character c = entrada.readChar();
                        paquetes.add(c);
                    }

                    String fileNameString = new String(fileName);

                    try (FileOutputStream fos = new FileOutputStream("files/" + fileNameString)) {
                        // Escribimos los bytes del archivo
                        fos.write(Descompresor.descomprimir(paquetes).getBytes());
                        System.out.println("Archivo creado correctamente -> " + fileNameString);
                    } catch (IOException e) {
                        throw new RuntimeException(e.getMessage());
                    }

                    // Terminar el hilo del servidor
                    inputStream.close();
                    entrada.close();
                    return;
                }
            }

        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
