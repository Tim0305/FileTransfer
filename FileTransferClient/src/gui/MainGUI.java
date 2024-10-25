/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gui;

import java.awt.Font;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
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
            try {
                File file = archivo.getSelectedFile();
                Scanner cin = new Scanner(file);
                StringBuilder content = new StringBuilder();

                // Obtener el contenido del archivo
                while (cin.hasNext()) {
                    System.out.println(cin.nextLine());
                    content.append(cin.nextLine());
                    content.append("\n");
                }
                cin.close();
                // Enviar el contenido al servidor
                Servidor servidor = new Servidor("192.168.1.4", 1234);
                try {
                    // Controlador para enviar datos que ocupen mas de un byte
                    DataOutputStream salida = new DataOutputStream(servidor.getSocket().getOutputStream());
                    String contentString = content.toString();
                    String fileName = file.getName();

                    System.out.println("");
                    System.out.println("FileName -> " + fileName);
                    System.out.println("FileName Bytes -> " + fileName.getBytes().length);
                    System.out.println("----------------------------------------");
                    System.out.println(content.toString());
                    System.out.println("Content Bytes -> " + contentString.getBytes().length);

                    salida.writeInt(fileName.getBytes().length); // Enviar la cantidad de bytes del nombre del archivo
                    salida.writeInt(contentString.getBytes().length); // Enviar la cantidad de bytes del contenido
                    servidor.sendString(fileName); // Enviar el nombre del archivo
                    servidor.sendString(content.toString()); // Enviar el contenido del archivo

                    servidor.close();
                    salida.close();
                } catch (IOException ex) {
                    Logger.getLogger(MainGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(MainGUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
