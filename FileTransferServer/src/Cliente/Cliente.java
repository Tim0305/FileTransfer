/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Cliente;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

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

            while (true) {
                if (inputStream.available() > 0) {
                    // Logica de comunicacion con el cliente para recibir el archivo
                    int numBytesFileName = inputStream.read();
                    int numBytesContent = inputStream.read();
                    byte[] fileName = new byte[numBytesFileName];
                    byte[] dataContent = new byte[numBytesContent];
                    inputStream.read(fileName);
                    inputStream.read(dataContent);

                    String fileNameString = fileName.toString();

                    try (FileOutputStream fos = new FileOutputStream(fileNameString)) {
                        // Escribimos los bytes del archivo
                        fos.write(dataContent);
                        System.out.println("Archivo creado correctamente -> " + fileNameString);
                    } catch (IOException e) {
                        throw new RuntimeException(e.getMessage());
                    }

                    // Terminar el hilo del servidor
                    return;
                }
            }

        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
