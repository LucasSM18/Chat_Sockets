/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mballem.app.service;

import com.mballem.app.bean.ChatMessage;
import com.mballem.app.bean.ChatMessage.Action;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author User
 */
public class ServidorService {

    //vaiaveis de socket
    private ServerSocket serverSocket;
    private Socket socket;
    /*Todo o usuário que se conectarem ao chat do servidor deve 
    ser adcionado a esta lista*/
    private Map<String, ObjectOutputStream> mapOnlines = new HashMap<String, ObjectOutputStream>();

    //´metodo construtor que irá conectar o cliente com o servidor
    public ServidorService() {
        try {
            //servidor vai ser inicializado a partir da porta 5555
            serverSocket = new ServerSocket(5555);

            System.out.println("Servidor on!");

            /*se for verdade o accept irá liberar o acesso ao servidor
            enquanto o cliente estiver conectado
             */
            while (true) {
                socket = serverSocket.accept();

                //uma nova thread para o cliente mandar mensagens será criada
                new Thread(new ListenerSocket(socket)).start();
            }

        } catch (IOException ex) {
            Logger.getLogger(ServidorService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private class ListenerSocket implements Runnable {

        //executa a saida e o envio de mensagens do servidor
        private ObjectOutputStream output;
        //recebe as mensagens enviadas pelo cliente
        private ObjectInputStream input;

        //método construtor que implementa os métodos de input e output
        public ListenerSocket(Socket socket) {
            try {
                this.output = new ObjectOutputStream(socket.getOutputStream());
                this.input = new ObjectInputStream(socket.getInputStream());
            } catch (IOException ex) {
                Logger.getLogger(ServidorService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @Override
        public void run() {
            ChatMessage message = null;
            /* realiza uma ação de acordo com algum pedido do cliente, no caso
            as condições presentes no bloco try/ catch
             */
            try {

                while ((message = (ChatMessage) input.readObject()) != null) {
                    Action action = message.getAction();

                    if (action.equals(Action.CONNECT)) {
                        boolean isConnect = connect(message, output);
                        if (isConnect) {
                            mapOnlines.put(message.getNome(), output);
                            sendOnlines();
                        }
                    } else if (action.equals(Action.DISCONNECT)) {
                        disconnect(message, output);
                        sendOnlines();
                        return;
                    } else if (action.equals(Action.SEND_ALL)) {
                        sendAll(message);
                    } else if (action.equals(Action.SEND_ONE)) {
                        sendOne(message);
                    }
                }

            } catch (IOException ex) {
                disconnect(message, output);
                sendOnlines();
                System.out.println(message.getNome() + " deixou o chat");
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ServidorService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private boolean connect(ChatMessage message, ObjectOutputStream output) {
        if (mapOnlines.size() == 0) {
            message.setText("YES");
            send(message, output);
            return true;
        }

        for (Map.Entry<String, ObjectOutputStream> kv : mapOnlines.entrySet()) {
            if (kv.getKey().equals(message.getNome())) {
                message.setText("NO");
                send(message, output);
                return false;
            } else {
                message.setText("YES");
                send(message, output);
                return true;
            }
        }

        return false;

    }
    //remove o cliente da lista de conexão após ele clicar em sair.

    private void disconnect(ChatMessage message, ObjectOutputStream output) {
        mapOnlines.remove(message.getNome());

        message.setText("até logo!");

        message.setAction(Action.SEND_ONE);

        sendAll(message);

        System.out.println("User: " + message.getNome() + " saiu da sala");
    }

    private void send(ChatMessage message, ObjectOutputStream output) {
        try {
            output.writeObject(message);
        } catch (IOException ex) {
            Logger.getLogger(ServidorService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void sendOne(ChatMessage message) {
        for (Map.Entry<String, ObjectOutputStream> kv : mapOnlines.entrySet()) {
            if (kv.getKey().equals(message.getNameReserved())) {
                try {
                    kv.getValue().writeObject(message);
                } catch (IOException ex) {
                    Logger.getLogger(ServidorService.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    private void sendAll(ChatMessage message) {
        //Metodo para Enviar mensagem para todos os clientes.
        for (Map.Entry<String, ObjectOutputStream> kv : mapOnlines.entrySet()) {
            if (!kv.getKey().equals(message.getNome())) {
                message.setAction(Action.SEND_ONE);
                try {
                    kv.getValue().writeObject(message);
                } catch (IOException ex) {
                    Logger.getLogger(ServidorService.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    private void sendOnlines() {
        Set<String> setNames = new HashSet<String>();
        for (Map.Entry<String, ObjectOutputStream> kv : mapOnlines.entrySet()) {
            setNames.add(kv.getKey());
        }

        ChatMessage message = new ChatMessage();
        message.setAction(Action.USER_ONLINE);
        message.setSetOnlines(setNames);
        for (Map.Entry<String, ObjectOutputStream> kv : mapOnlines.entrySet()) {
            message.setNome(kv.getKey());
            try {                
                kv.getValue().writeObject(message);
            } catch (IOException ex) {
                Logger.getLogger(ServidorService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
