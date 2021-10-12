/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mballem.app.service;

import com.mballem.app.bean.ChatMessage;
import com.mballem.app.bean.Mensagens;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author User
 */
public class ClienteService {
    
    //vaiaveis de que possuem relção com o socket
    private Socket socket;
    private ObjectOutputStream output;
    //variavel de confirmação se a mensagem foi recebida
    private final Mensagens mensagem = new Mensagens(null);
    
    
    //método que conecta o cliente ao endereço que está o servidor
    public Socket connect(){
        try {
            this.socket = new Socket("localhost", 5555);
            this.output = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(ClienteService.class.getName()).log(Level.SEVERE, null, ex);
        }
        
       return socket;
    }
    
    //método que irá mandar a conexão do cliente ao servidor
    public void sendConnection(ChatMessage message){
        try {    
            output.writeObject(message);
        }catch (IOException ex) {
             Logger.getLogger(ClienteService.class.getName()).log(Level.SEVERE, null, ex);   
        }
    }    
    
    //método que irá mandar a mensagem do cliente ao servidor
    public boolean sendMessage(ChatMessage message){
        boolean Confirmar = false;
        try {    
            output.writeObject(message);
            Confirmar = true;
        }catch (IOException ex) {
             Logger.getLogger(ClienteService.class.getName()).log(Level.SEVERE, null, ex);
             Confirmar = false;
        }finally{
             return Confirmar;
        }
    }    
}
