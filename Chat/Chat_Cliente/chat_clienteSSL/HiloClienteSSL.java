package chat_clienteSSL;
import static chat_clienteSSL.VentanaClienteSSL.fentrada;
import static chat_clienteSSL.VentanaClienteSSL.repetir;
import static chat_clienteSSL.VentanaClienteSSL.socket;
import static chat_clienteSSL.VentanaClienteSSL.textarea_chat;
import static chat_clienteSSL.VentanaClienteSSL.texto;
import java.io.IOException;
import javax.swing.JOptionPane;

/**
 *
 * @author David Algás
 */
public class HiloClienteSSL extends Thread
{
    @Override
     public void run() 
     {
         while(repetir)
         {
             try
             {
                 texto=fentrada.readUTF(); //Leemos el Mensaje
                 textarea_chat.setText(texto); //Visualizamos el Mensaje
             }
             catch(IOException e)
             {
                  JOptionPane.showMessageDialog(null, "IMPOSIBLE CONECTAR CON EL SERVIDOR\n" + e.getMessage(),"<<MENSAJE DE ERROR:2>>", JOptionPane.ERROR_MESSAGE);
                  repetir = false; //Para Salir del Bucle
             }
         }
         
         try
         {
             socket.close(); //Cerramos el Socket
             System.exit(0);
         }
         catch(IOException e)
         {
             System.out.println("Error HiloSSL: "+e);
         }
    }     
}