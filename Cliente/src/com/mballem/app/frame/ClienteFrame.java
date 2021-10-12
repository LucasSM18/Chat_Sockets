/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mballem.app.frame;

import com.mballem.app.bean.ChatMessage;
import com.mballem.app.bean.ChatMessage.Action;
import com.mballem.app.bean.Metodos_Banco;
import com.mballem.app.service.ClienteService;
import com.mballem.app.bean.Mensagens;
import funcionalidades.RegistradorTempo;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;

/**
 *
 * @author User
 */
public class ClienteFrame extends javax.swing.JFrame {

    private Socket socket;
    private ChatMessage message;
    private ClienteService service;    
    private Mensagens m;
    private Metodos_Banco banco;
    private String nome = null;
    private int id = 0;
    private boolean status = false;
    
    
    /**
     * Creates new form ClienteFrame
     * @param nome
     */
    public ClienteFrame(int id, String nome) {
        initComponents();        
        this.nome = nome;
        this.id = id;
    }

    private class ListenerSocket implements Runnable {

        private ObjectInputStream input;

        public ListenerSocket(Socket socket) {
            try {
                this.input = new ObjectInputStream(socket.getInputStream());
            } catch (IOException ex) {
                Logger.getLogger(ClienteFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @Override
        public void run() {
            ChatMessage message = null;
            try {
                while ((message = (ChatMessage) input.readObject()) != null) {
                    Action action = message.getAction();

                    switch (action) {
                        case CONNECT:
                            connected(message);
                            break;
                        case DISCONNECT:
                            disconnected();
                            socket.close();
                            break;
                        case SEND_ONE:
                            receive(message);
                            break;
                        case USER_ONLINE:
                            refreshOnlines(message);
                            break;
                        default:
                            break;
                    }
                }
            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(ClienteFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private void connected(ChatMessage message) {

        if (message.getText().equals("NO")) {
            this.txtName.setText("");
            JOptionPane.showMessageDialog(this, "Conexão não realizada!\nTente novamente com um novo nome.");
            return;
        }
        //Funções para desabilitar ou habilitar os componentes no layout.
        this.message = message;
        this.btnConnectar.setEnabled(false);
        
        this.txtAreaSend.setEnabled(true);
        this.btnSair.setEnabled(true);
        this.btnEnviar.setEnabled(true);
        this.btnLimpar.setEnabled(true);
              
        JOptionPane.showMessageDialog(this, "Você está conectado no chat!");

    }

    private void disconnected() {
        //// Quando clicar sair o socket do cliente é desconectado do servidor.

        this.btnConnectar.setEnabled(true);
       
        this.txtAreaSend.setEnabled(false);
        this.btnSair.setEnabled(false);
        this.btnEnviar.setEnabled(false);
        this.btnLimpar.setEnabled(false);
        
        this.txtAreaReceive.setText("");
        this.txtAreaSend.setText("");
        
        JOptionPane.showMessageDialog(this, "Você saiu da sala de chat!");

    }
    //Metodo que vai receber a mensagem do cliente
    private void receive(ChatMessage message) {
        RegistradorTempo tempo = new RegistradorTempo();
        this.txtAreaReceive.append(message.getNome() + ": " + message.getText() + "\n");
        this.txtAreaReceive.append("(Mensagem recebida)"+"\n");
        this.txtAreaReceive.append(tempo.hora());
        this.txtAreaReceive.append("\n");
    }

    
    private void refreshOnlines(ChatMessage message) {
        System.out.println(message.getSetOnlines().toString());
        
        Set<String> names = message.getSetOnlines();
        
        names.remove(message.getNome());
        
        String[] array = (String[]) names.toArray(new String[names.size()]);
        
        this.listOnline.setListData(array);
        this.listOnline.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.listOnline.setLayoutOrientation(JList.VERTICAL);
    }
   
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        txtName = new javax.swing.JTextField();
        btnConnectar = new javax.swing.JButton();
        btnSair = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        listOnline = new javax.swing.JList<>();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtAreaReceive = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtAreaSend = new javax.swing.JTextArea();
        btnEnviar = new javax.swing.JButton();
        btnLimpar = new javax.swing.JButton();

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Conectar"));

        txtName.setEnabled(false);

        btnConnectar.setText("Conectar");
        btnConnectar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConnectarActionPerformed(evt);
            }
        });

        btnSair.setText("Sair");
        btnSair.setEnabled(false);
        btnSair.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSairActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtName)
                .addGap(18, 18, 18)
                .addComponent(btnConnectar)
                .addGap(18, 18, 18)
                .addComponent(btnSair)
                .addGap(18, 18, 18))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(btnConnectar)
                .addComponent(btnSair))
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Onlines"));

        jScrollPane3.setViewportView(listOnline);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 102, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 256, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        txtAreaReceive.setEditable(false);
        txtAreaReceive.setColumns(20);
        txtAreaReceive.setRows(5);
        txtAreaReceive.setEnabled(false);
        jScrollPane1.setViewportView(txtAreaReceive);

        txtAreaSend.setColumns(20);
        txtAreaSend.setRows(5);
        txtAreaSend.setEnabled(false);
        jScrollPane2.setViewportView(txtAreaSend);

        btnEnviar.setText("Enviar");
        btnEnviar.setEnabled(false);
        btnEnviar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEnviarActionPerformed(evt);
            }
        });

        btnLimpar.setText("Limpar");
        btnLimpar.setEnabled(false);
        btnLimpar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimparActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 358, Short.MAX_VALUE)
                    .addComponent(jScrollPane2)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnLimpar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEnviar)))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnEnviar)
                    .addComponent(btnLimpar))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnConnectarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConnectarActionPerformed
        String name = this.txtName.getText();

        if (!name.isEmpty()) {
            this.message = new ChatMessage();
            this.message.setAction(Action.CONNECT);
            this.message.setNome(name);

            this.service = new ClienteService();
            this.socket = this.service.connect();

            new Thread(new ListenerSocket(this.socket)).start();

            this.service.sendConnection(message);
        }
    }//GEN-LAST:event_btnConnectarActionPerformed

    private void btnSairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSairActionPerformed
        this.message.setAction(Action.DISCONNECT);
        this.service.sendConnection(this.message);
        disconnected();
    }//GEN-LAST:event_btnSairActionPerformed

    private void btnLimparActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimparActionPerformed
        this.txtAreaSend.setText("");
    }//GEN-LAST:event_btnLimparActionPerformed

    private void btnEnviarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEnviarActionPerformed
        String text = this.txtAreaSend.getText();
        String nome = this.message.getNome();
        RegistradorTempo tempo = new RegistradorTempo();
        String status = null;
        this.message = new ChatMessage();
                               
        if(this.listOnline.getSelectedIndex() != -1){
           this.message.setNameReserved((String) this.listOnline.getSelectedValue());
           this.message.setAction(Action.SEND_ONE);
           this.listOnline.clearSelection();
        }else{
           this.message.setAction(Action.SEND_ALL);        
        }
               
        if (!text.isEmpty()) {
            this.message.setNome(nome);
            this.message.setText(text);
            
            this.txtAreaReceive.append("Eu: " + text + "\n");
                        
            this.status = this.service.sendMessage(this.message);
            
             
              if (this.status == true) {
                status = "Recebida";
                this.txtAreaReceive.append("(Mensagem enviada)\n");
                this.txtAreaReceive.append(tempo.hora());
                this.txtAreaReceive.append("\n");
              } else {
                status = "Não Recebida";
              }
            
            banco = new Metodos_Banco(0, null, null, null, null, null);
            this.banco.receiveMessage(id, text, status);
        }
        this.txtAreaSend.setText("");
    }//GEN-LAST:event_btnEnviarActionPerformed

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        // TODO add your handling code here:
        txtName.setText(this.nome);    
    }//GEN-LAST:event_formWindowActivated


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnConnectar;
    private javax.swing.JButton btnEnviar;
    private javax.swing.JButton btnLimpar;
    private javax.swing.JButton btnSair;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JList<String> listOnline;
    private javax.swing.JTextArea txtAreaReceive;
    private javax.swing.JTextArea txtAreaSend;
    private javax.swing.JTextField txtName;
    // End of variables declaration//GEN-END:variables
}
