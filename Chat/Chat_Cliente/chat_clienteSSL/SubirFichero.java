package chat_clienteSSL;
import java.io.*;
import java.net.*;
import javax.swing.JFileChooser;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

/**
 *
 * @author David Algás
 */
public class SubirFichero 
{
    public SubirFichero()
    {
        FTPClient cliente = new FTPClient();
        String servidor = "127.0.0.1";
        String user = "usuario";
        String pasw = "clave";
        
        JFileChooser f = new JFileChooser();
        File file;
        String archivo = "";
        String nombreArchivo = "";
       
        f.setFileSelectionMode(JFileChooser.FILES_ONLY);
        f.setDialogTitle("Selecciona el fichero a subir al servidor FTP");
       
       int returnVal = f.showDialog(f, "cargar");
       
       if(returnVal == JFileChooser.APPROVE_OPTION)
       {
           file = f.getSelectedFile();
           archivo = file.getAbsolutePath();
           nombreArchivo = file.getName();
       }
       
       try
       {
           System.out.println("Conectandose a " + servidor);
           cliente.connect(servidor);
           boolean login = cliente.login(user, pasw);
           String direc = "C:/Program Files (x86)/FileZilla Server/Compartida";
           
           if(login)
           {
               cliente.changeWorkingDirectory(direc);
               cliente.setFileType(FTP.BINARY_FILE_TYPE);

               //Stream de Entrada con el Fichero a Subir
               BufferedInputStream in = new BufferedInputStream(new FileInputStream(archivo));
               cliente.storeFile(nombreArchivo, in);
               
               in.close(); //Cerramos el Flujo
               cliente.logout(); //Desconexion del Usuario
               cliente.disconnect(); //Desconexion del Servidor
            }
        }
        catch(ConnectException ex)
        {}
        catch(IOException ioe)
        {
            System.out.println("Error FPT: "+ioe);
        }        
    }  
}