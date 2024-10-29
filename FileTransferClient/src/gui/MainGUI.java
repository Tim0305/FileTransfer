/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gui;

import java.awt.Font;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import lz77.compresor.Compresor;
import lz77.descompresor.Descompresor;
import servidor.Servidor;

/**
 *
 * @author Timoteo
 */
public class MainGUI extends JFrame {

    private JFileChooser jFileChooser;
    private JButton cmdSeleccionarArchivo;
    private int width;
    private int height;
    // Cantidad de bytes que se envian por paquetes
    private final int bufferSize = 1000;

    public MainGUI() {
        super();

        width = 500;
        height = 500;
        cmdSeleccionarArchivo = new JButton("Seleccionar Archivo");

        config();
    }

    private void config() {

        setTitle("File Transfer");
        setSize(width, height);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        cmdSeleccionarArchivo.setFont(new Font("Arial", Font.PLAIN, 30));
        cmdSeleccionarArchivo.setFocusPainted(false);
        cmdSeleccionarArchivo.addActionListener(e -> onClickSeleccionarArchivo());

        GroupLayout gl = new GroupLayout(getContentPane());
        gl.setAutoCreateContainerGaps(true);
        gl.setAutoCreateGaps(true);

        gl.setHorizontalGroup(
                gl.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(cmdSeleccionarArchivo)
                        .addGap(500)
        );
        gl.setVerticalGroup(
                gl.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(cmdSeleccionarArchivo)
                        .addGap(400)
        );

        setLayout(gl);
    }

    private void onClickSeleccionarArchivo() {
        JFileChooser archivo = new JFileChooser();

        int resultado;

        resultado = archivo.showOpenDialog(null);
        if (resultado == JFileChooser.APPROVE_OPTION) {

            File file = archivo.getSelectedFile();
            Servidor servidor = new Servidor("127.0.0.1", 1234);

            String fileName = file.getName();

            try (FileInputStream fis = new FileInputStream(file)) {

                byte[] buffer = new byte[bufferSize];

                DataOutputStream dos = new DataOutputStream(servidor.getSocket().getOutputStream());

                // Enviar los bytes del nombre del archivo
                dos.writeInt(fileName.getBytes().length);

                // Enviar el nombre del archivo
                dos.write(fileName.getBytes());

                List<List<Character>> listaPaquetes = new ArrayList<>();

                // Leer los bytes del archivo y almacenarlos en paquetes definidos por el bufferSize
                int readedBytes = 0;
                while ((readedBytes = fis.read(buffer)) != -1) {
                    // Si el último bloque es menor a 1000 bytes, ajustamos el tamaño
                    if (readedBytes < bufferSize) {
                        byte[] newBuffer = new byte[readedBytes];
                        System.arraycopy(buffer, 0, newBuffer, 0, readedBytes);
                        listaPaquetes.add(Compresor.comprimir(Base64.getEncoder().encodeToString(newBuffer)));
                    } else {
                        listaPaquetes.add(Compresor.comprimir(Base64.getEncoder().encodeToString(buffer)));
                    }
                }

                // Enviar la cantidad de lineas comprimidas del archivo
                System.out.println(listaPaquetes.size());
                dos.writeInt(listaPaquetes.size());

                // Enviar cada paquete individualmente
                for (List<Character> listaPaquete : listaPaquetes) {

                    // Tamanio del paquete
                    dos.writeInt(listaPaquete.size());
                    System.out.println(listaPaquete.size());

                    // Contenido del paquete
                    for (Character paquete : listaPaquete) {
                        dos.writeChar(paquete);
                    }
                }

            } catch (IOException ex) {
                Logger.getLogger(MainGUI.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }
}
