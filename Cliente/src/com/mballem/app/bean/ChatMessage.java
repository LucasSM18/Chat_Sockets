/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mballem.app.bean;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author User
 */
public class ChatMessage implements Serializable{
    //Variaveis em relação ao cliente e a menssagem
    private String nome;
    private String text;
    private String nameReserved;    
    /*Lista que irá armazenar todos os clientes que estão
    conectadas ao servidor*/
    private Set<String> setOnlines = new HashSet<String>();   
    //Action que irá fazer as tarefas do chat. EX: Conectar
    private Action action;
 
    /**
     * @return the nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * @param nome the nome to set
     */
    public void setNome(String nome) {
        this.nome = nome;
    }
    

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getNameReserved() {
        return nameReserved;
    }

    public void setNameReserved(String nameReserved) {
        this.nameReserved = nameReserved;
    }

    public Set<String> getSetOnlines() {
        return setOnlines;
    }

    public void setSetOnlines(Set<String> setOnlines) {
        this.setOnlines = setOnlines;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }
    
    //enum está connectada a Action    
    public enum Action{
        CONNECT, DISCONNECT, SEND_ONE, SEND_ALL, USER_ONLINE    
    }
}
