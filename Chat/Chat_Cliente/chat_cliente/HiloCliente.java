package chat_cliente;
import static chat_cliente.VentanaCliente.fentrada;
import static chat_cliente.VentanaCliente.repetir;
import static chat_cliente.VentanaCliente.socket;
import static chat_cliente.VentanaCliente.textarea_chat;
import static chat_cliente.VentanaCliente.texto;
import java.io.IOException;
import javax.swing.JOptionPane;

/**
 *
 * @author David Algás
 */
public class HiloCliente extends Thread
{
    @Override
    public void run() 
    {
        while(repetir)
        {
            try
            {
                 texto=fentrada.readUTF(); //Leer Mensajes
                 textarea_chat.setText(texto); //Mostrar Mensajes
             }
            catch(IOException e)
            {
                  JOptionPane.showMessageDialog(null, "IMPOSIBLE CONECTAR CON EL SERVIDOR\n" + e.getMessage(),"<<MENSAJE DE ERROR:2>>", JOptionPane.ERROR_MESSAGE);
                  repetir = false; //Para Salir del Bucle
            }
         }
         try
         {
             socket.close(); //cerrar socket
             System.exit(0);
         }
         catch(IOException e)
         {
             System.out.println("Error: "+e);
         }
    }  
}