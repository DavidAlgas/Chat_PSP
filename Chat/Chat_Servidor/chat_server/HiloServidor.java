package chat_server;
import static chat_server.VentanaServidor.nombres;
import static chat_server.VentanaServidor.textarea_usuarios;
import java.io.*;
import java.net.*;
import java.util.Iterator;

/**
 *
 * @author David Algás
 */
public class HiloServidor extends Thread
{
    DataInputStream fentrada;
    Socket socket = null;
    String nik = "";
    
    public HiloServidor(Socket s)
    {
        socket = s;
        try
        {
            //Creamos el Flujo de Entrada
            fentrada = new DataInputStream(socket.getInputStream());
        }
        catch(IOException e)
        {
            System.out.println("ERROR DE E/S");
        }
    }
    
    @Override
    public void run()
    {
        VentanaServidor.txt_conexiones.setText("NUMERO DE CONEXIONES ACTUALES: " + VentanaServidor.ACTUALES);
        
        //Nada mas Conectarse el Cliente le envio todos los Mensajes
        String texto = VentanaServidor.textarea_chat.getText();
        EnviarMensajes(texto);
        
        while(true)
        {
            texto = VentanaServidor.textarea_chat.getText();
            EnviarMensajes(texto);
            String cadena = "";
            
            try
            {
                cadena = fentrada.readUTF();
                
                if(cadena.trim().equals("*"))
                {
                    VentanaServidor.ACTUALES--;
                    VentanaServidor.txt_conexiones.setText("NUMERO DE CONEXIONES ACTUALES: "+ VentanaServidor.ACTUALES);
                    break;
                }
                VentanaServidor.textarea_chat.append(cadena + "\n");
                
                //Compuebo si existen los usuarios para Añadirlos a la lista
                String aux = cadena.replaceAll(" ", "").substring(0, 17);
                System.out.println(aux);
                
                if( nik == "" || aux.equalsIgnoreCase(">EntraenelChat...")==true)
                {
                    System.out.println(nik);
                    nik = cadena.substring(22, cadena.length());
                    VentanaServidor.textarea_usuarios.append(nik+ "\n");
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
                        VentanaServidor.nombres.remove(borrarnum);
                    }
                    
                    it = nombres.iterator();
                    String aux1="";
                    while(it.hasNext())
                    {
                        aux1=aux1+(String) it.next()+"\n";

                    }
                    textarea_usuarios.setText(aux1);
                }
                     
                texto = VentanaServidor.textarea_chat.getText();
                EnviarMensajes(texto); //Envio texto a todos los clientes  
            }
            catch(Exception e)
            {
                System.out.println("Error: "+e);
            }
        }
    }
    
    //Envia todos los mensajes a todos los Clientes del Chat
    private void EnviarMensajes(String texto)
    {
        int i;
        
        //Recorremos la tabla de sockets para enviarles los mensajes
        for(i=0; i<VentanaServidor.CONEXIONES; i++)
        {
            Socket s1 = VentanaServidor.tabla[i]; //Obtengo una lista de usuarios conectados(sockets) 
            try
            {
                DataOutputStream fsalida=new DataOutputStream(s1.getOutputStream());
                fsalida.writeUTF(texto);
            }
            catch(SocketException se)
            {
            }
            catch(IOException e)
            {
                System.out.println("Error HiloServer: "+e);
            }
        }
    }
}