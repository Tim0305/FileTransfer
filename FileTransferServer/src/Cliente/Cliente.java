/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Cliente;

import java.io.DataInputStream;
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
                    int numListaPaquetes = entrada.readInt();
                    List<List<Character>> listaPaquetes = new ArrayList<>();

                    for (int i = 0; i < numListaPaquetes; i++) {
                        int sizePaquetes = entrada.readInt();

                        List<Character> paquetes = new ArrayList<>();
                        for (int j = 0; j < sizePaquetes; j++) {
                            paquetes.add(entrada.readChar());
                        }

                        listaPaquetes.add(paquetes);
                    }

                    System.out.println(listaPaquetes.size());

                    try (FileOutputStream fos = new FileOutputStream("files/" + fileNameString)) {
                        // Escribimos los bytes del archivo
                        for (List<Character> listaPaquete : listaPaquetes) {
                            System.out.println(listaPaquete.size());
                            String content = Descompresor.descomprimir(listaPaquete);
                            fos.write(Base64.getDecoder().decode(content));
                        }

                        System.out.println("Archivo creado correctamente -> " + fileNameString);
                    } catch (IOException e) {
                        throw new RuntimeException(e.getMessage());
                    }

                    // Terminar el hilo del servidor
                    entrada.close();
                    s.close();
                    return;
                }
            }

        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
