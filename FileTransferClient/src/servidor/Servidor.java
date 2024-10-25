package servidor;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Timoteo
 */
public class Servidor {

    private Socket s;

    public Servidor(String ip, int puerto) {
        try {
            s = new Socket(ip, puerto);
        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex.getMessage());
        }
    }

    public void sendString(String message) {
        try {
            s.getOutputStream().write(message.getBytes());
        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void sendInt(int dato) {
        try {
            s.getOutputStream().write(dato);
        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int getInt() {
        try {
            return s.getInputStream().read();
        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex.getMessage());
        }
    }

    public Socket getSocket() {
        return s;
    }

    public void close() {
        try {
            s.close();
        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
