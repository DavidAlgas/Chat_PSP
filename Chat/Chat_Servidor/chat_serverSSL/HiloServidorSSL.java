package chat_serverSSL;
import static chat_serverSSL.VentanaServidorSSL.nombres;
import static chat_serverSSL.VentanaServidorSSL.textarea_usuarios;
import java.io.*;
import java.util.Iterator;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSocket;

/**
 *
 * @author David Algás
 */
public class HiloServidorSSL extends Thread
{
    DataInputStream fentrada;
    SSLSocket socketSSL = null;
    String nik = "";
    
    public HiloServidorSSL(SSLSocket s)
    {
        socketSSL = s;
        try
        {
            //Se crea el Flujo de Entrada
            fentrada = new DataInputStream(socketSSL.getInputStream());
        }
        catch(IOException e)
        {
            System.out.println("Error HiloSSL: "+e);
        }
    }
    
    @Override
    public void run()
    {
        VentanaServidorSSL.txt_conexiones.setText("NUMERO DE CONEXIONES ACTUALES: " + VentanaServidorSSL.ACTUALES);
        
        //Nada mas Conectarse el Cliente le envio todos los Mensajes
        String texto = VentanaServidorSSL.textarea_chat.getText();
        EnviarMensajes(texto);
        
        while(true)
        {
            texto = VentanaServidorSSL.textarea_chat.getText();
            EnviarMensajes(texto);
            String cadena = "";
            try
            {
                cadena = fentrada.readUTF();
                if(cadena.trim().equals("*"))
                {
                    VentanaServidorSSL.ACTUALES--;
                    VentanaServidorSSL.txt_conexiones.setText("NUMERO DE CONEXIONES ACTUALES: "+ VentanaServidorSSL.ACTUALES);
                    break;
                }
                VentanaServidorSSL.textarea_chat.append(cadena + "\n");

                 
                //Compuebo si existen los usuarios para Añadirlos a la lista
                String aux = cadena.replaceAll(" ", "").substring(0, 17);
                System.out.println(aux);
                if( nik == "" || aux.equalsIgnoreCase(">EntraenelChat...")==true)
                {
                    System.out.println(nik);
                    nik = cadena.substring(22, cadena.length());
                    VentanaServidorSSL.textarea_usuarios.append(nik+ "\n");
                    nombres.add(nik);
                }
                
                System.out.println("aux en el momento de salir: "+aux);
                
                //Elimino a los usuarios de la lista segun vayan desconectando
                if(aux.equalsIgnoreCase(">Abandonaelchat..")==true)
                {
                    nik=cadena.substring(23,cadena.length());
                    Iterator it = nombres.iterator();
                    String nom=nik;
                    int cuenta=0;
                    int borrarnum=-1;
                    boolean borrar=false;
                    String nomb="";
                    
                    while(it.hasNext())
                    {
                        nomb=it.next().toString();
                        if(nom.equals(nomb))
                        {
                            borrar=true;
                            borrarnum=cuenta;
                        }
                        cuenta++;
                    }
                    if (borrar==true)
                    {
                        VentanaServidorSSL.nombres.remove(borrarnum);
                    }
                    
                    it = nombres.iterator();
                    String aux1="";
                    while(it.hasNext())
                    {
                        aux1=aux1+(String) it.next()+"\n";

                    }
                    textarea_usuarios.setText(aux1);
                }
                     
                texto = VentanaServidorSSL.textarea_chat.getText();
                EnviarMensajes(texto); //Envio texto a todos los clientes  
            }
            catch(Exception e)
            {
                System.out.println("Error HiloSSL 113: "+e);
            }
        }
    }
    
    //Envia todos los mensajes a todos los Clientes del Chat
    private void EnviarMensajes(String texto)
    {
        int i;
        //Recorremos la tabla de sockets para enviarles los mensajes
        for(i=0; i<VentanaServidorSSL.CONEXIONES; i++)
        {
            SSLSocket s1 = (SSLSocket) VentanaServidorSSL.tabla[i]; //Obtengo una lista de usuarios conectados(sockets) 
            try
            {
                DataOutputStream fsalida=new DataOutputStream(s1.getOutputStream());
                fsalida.writeUTF(texto);
            }
            catch(SSLException se)
            {}
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}