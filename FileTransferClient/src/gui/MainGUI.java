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
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import lz77.compresor.Compresor;
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

            try (FileInputStream fis = new FileInputStream(file)) {
                long fileLength = file.length();
                byte[] bytes = new byte[(int) fileLength];

                // Leer los bytes del archivo
                long readedBytes = fis.read(bytes);

                // Comprobar si se leyeron todos los bytes
                if (readedBytes == fileLength) {
                    // Enviar los bytes al servidor
                    Servidor servidor = new Servidor("192.168.1.4", 1234);

                    try {
                        // Controlador para enviar datos que ocupen mas de un byte
                        DataOutputStream salida = new DataOutputStream(servidor.getSocket().getOutputStream());
                        String fileName = file.getName();
                        String fileContent = new String(bytes);
                        List<Character> paquetes = Compresor.comprimir(fileContent);

                        salida.writeInt(fileName.getBytes().length); // Enviar la cantidad de bytes del nombre del archivo
                        salida.writeLong(paquetes.size()); // Enviar la cantidad de bytes del contenido
                        servidor.sendString(fileName); // Enviar el nombre del archivo

                        for (Character paquete : paquetes) {
                            // Enviar el contenido del archivo
                            salida.writeChar(paquete);
                        }

                        System.out.println("File Lenght -> " + fileLength);
                        System.out.println("File Compressed Length -> " + paquetes.size());

                        servidor.close();
                        salida.close();

                    } catch (IOException ex) {
                        Logger.getLogger(MainGUI.class
                                .getName()).log(Level.SEVERE, null, ex);
                    }

                } else {
                    throw new RuntimeException("No se leyeron todos los bytes del archivo");
                }

            } catch (IOException ex) {
                Logger.getLogger(MainGUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
