package chat_serverSSL;
import java.awt.event.ActionEvent;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.net.ssl.*;
import javax.swing.*;

/**
 *
 * @author David Algás
 */
public class VentanaServidorSSL extends javax.swing.JFrame 
{
    private static final long serialVersionUID= 1L;
    static SSLServerSocket servidorSSL;
    static ResourceBundle rb;
    static int PUERTO; //Puerto por el que escucha
    static int CONEXIONES = 0; //Cuenta las conexiones
    static int ACTUALES = 0; //Nº de conexiones actuales activas
    static int MAXIMO; //Nºmáximo de conexiones permitidas
  
    JButton desconectar = new JButton("Salir");
    static Socket tabla[]=new Socket[10]; //almacena sockets de clientes
    static ArrayList <String> nombres = new ArrayList <>();
    
    
    public VentanaServidorSSL() throws UnknownHostException,IOException 
    {
        super("Ventana Servidor ChatSSL ┤"+InetAddress.getLocalHost().getHostAddress()+":"+PUERTO+"├");
        setIconImage(new ImageIcon(getClass().getResource("../imagenes/Icon_Server.png")).getImage());
        initComponents();
        setResizable(false);
        setLocationRelativeTo(null);
        
        txt_conexiones.setEditable(false);       
        textarea_chat.setEditable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
   

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        textarea_chat = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        textarea_usuarios = new javax.swing.JTextArea();
        txt_errores = new javax.swing.JTextField();
        btn_desconectar = new javax.swing.JButton();
        lbl_usuarios = new javax.swing.JLabel();
        txt_conexiones = new javax.swing.JTextField();
        lbl_fondo = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        textarea_chat.setEditable(false);
        textarea_chat.setColumns(20);
        textarea_chat.setRows(5);
        textarea_chat.setOpaque(false);
        jScrollPane1.setViewportView(textarea_chat);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 51, 354, 290));

        textarea_usuarios.setEditable(false);
        textarea_usuarios.setColumns(20);
        textarea_usuarios.setRows(5);
        textarea_usuarios.setOpaque(false);
        jScrollPane2.setViewportView(textarea_usuarios);

        getContentPane().add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(382, 51, 138, 290));

        txt_errores.setEditable(false);
        getContentPane().add(txt_errores, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 359, 354, -1));

        btn_desconectar.setText("Desconectar");
        btn_desconectar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_desconectarActionPerformed(evt);
            }
        });
        getContentPane().add(btn_desconectar, new org.netbeans.lib.awtextra.AbsoluteConstraints(405, 359, -1, -1));

        lbl_usuarios.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lbl_usuarios.setForeground(new java.awt.Color(255, 255, 255));
        lbl_usuarios.setText("USUARIOS:");
        getContentPane().add(lbl_usuarios, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 20, -1, -1));

        txt_conexiones.setEditable(false);
        getContentPane().add(txt_conexiones, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 354, -1));

        lbl_fondo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/fondoChat.jpg"))); // NOI18N
        getContentPane().add(lbl_fondo, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 540, 420));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_desconectarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_desconectarActionPerformed
        System.exit(0);
        System.out.println("Servidor desconectado...");
    }//GEN-LAST:event_btn_desconectarActionPerformed

    public static void main(String args[]) throws IOException
    {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(VentanaServidorSSL.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VentanaServidorSSL.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VentanaServidorSSL.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VentanaServidorSSL.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        cargarPropiedades();
        System.setProperty("javax.net.ssl.keyStore", "AlmacenSSL");
        System.setProperty("javax.net.ssl.keyStorePassword", "1234567");
        
        SSLServerSocketFactory sfact = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
        servidorSSL = (SSLServerSocket) sfact.createServerSocket(PUERTO);
        System.out.println("Servidor iniciado...");
        
        VentanaServidorSSL vservidorSeguro = new VentanaServidorSSL();
        vservidorSeguro.setBounds(0, 0, 540, 420);
        vservidorSeguro.setVisible(true);
        txt_conexiones.setText("NUMERO DE CONEXIONES ACTUALES: " + 0);
        
        //Solo se Admiten 10 conexiones maximas
        while(CONEXIONES < MAXIMO)
        {
            SSLSocket s = null;
            try
            {
                s = (SSLSocket) servidorSSL.accept(); //esperando cliente
                tabla[CONEXIONES] = s; //almacenar socket
                CONEXIONES++;
                ACTUALES++;
                
                HiloServidorSSL hilo = new HiloServidorSSL(s);
                hilo.start(); //lanzar hilo
            }
            catch(SSLException ns)
            {
                //sale por aqui si pulsamos botón Salir y no se ejecuta todo el bucle
                break;
            }
        }
    }
    
    public void actionPerformed(ActionEvent e) 
    {
        if(e.getSource() == desconectar)//Se pulsa salir
        { 
            try
            {
                servidorSSL.close();
            }
            catch(IOException e1)
            {
                System.out.println("Error VentServSSL 181: "+e1);
            }
            System.exit(0);
        }
    }
    
    //Cargamos las propiedades del archivo misPropiedades
    static void cargarPropiedades()
    {
        rb = ResourceBundle.getBundle("properties/misPropiedades");
        Enumeration<String> claves = rb.getKeys();

        while (claves.hasMoreElements())
        {
            String clave = claves.nextElement();

            if ("puertoSeguro".equals(clave))
            {
                PUERTO = Integer.parseInt(rb.getString(clave));
            }
            if ("usuariosMAX".equals(clave)) 
            {
                MAXIMO = Integer.parseInt(rb.getString(clave));
            }
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_desconectar;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lbl_fondo;
    private javax.swing.JLabel lbl_usuarios;
    public static javax.swing.JTextArea textarea_chat;
    public static javax.swing.JTextArea textarea_usuarios;
    public static javax.swing.JTextField txt_conexiones;
    private javax.swing.JTextField txt_errores;
    // End of variables declaration//GEN-END:variables
}