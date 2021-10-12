/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mballem.app.bean;

import com.mballem.app.frame.ClienteFrame;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author User
 */
public class Metodos_Banco extends Usuario{
    //variáveis para fazer a conexão e os comandos SQL com o banco
    private static Connection conn = null;
    private static final String url = "jdbc:oracle:thin:@localhost:1521:XE";
    private static final String name = "Lucas";
    private static final String password = "Missalia";
    private static String sql = null;
    //objeto do frame ClienteFrame
    private ClienteFrame cFrame;
    //objetos de classes
    private Usuario user;
    private Mensagens message;

    public Metodos_Banco(int idade, String sexo, String email, String senha, String telefone, String celular) {
        super(idade, sexo, email, senha, telefone, celular);
    }  
     
    //método que pucha o driver para fazer a conexão
    public Connection getConnection(){    
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            conn = DriverManager.getConnection(url, name, password);
        } catch (ClassNotFoundException ex) {
             JOptionPane.showMessageDialog(null, "Driver para conexão ao Banco " + 
             "de Dados não localizado!", "ERRO", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "O banco não foi localizado!", 
            "ERRO", JOptionPane.ERROR_MESSAGE);
        }   
        return conn;
    }  
    
    //método que fecha a conexão
    public void closeConnection(Connection conn){
        if(conn != null){
          try {
	    conn.close();
          }catch (SQLException e) {
	    System.out.println("Erro ao fechar a conexão!");
     	  }
        }
    }
       
    //método que consulta o banco de dados
    public TableModel Consulta(String tabela){
        sql = "SELECT * FROM " + tabela + " ORDER BY CD_ID";
        
        DefaultTableModel tableModel = null;
        conn = getConnection();
                
        try{           
           Statement stmt = conn.createStatement();
           ResultSet rs = stmt.executeQuery(sql);
               
             if(tabela.equals("Usuarios")){
               Object[] columnNames = new Object[8];
               columnNames[0] = "ID";
               columnNames[1] = "Nome";
               columnNames[2] = "Idade";
               columnNames[3] = "Sexo";
               columnNames[4] = "Email";
               columnNames[5] = "Senha";
               columnNames[6] = "Telefone";
               columnNames[7] = "Celular";
               
                tableModel = new DefaultTableModel(columnNames, 0);
               
               while(rs.next()){
                     int id = rs.getInt("CD_ID");
                     String nome = rs.getString("NM_USUARIO");
                     int idade = rs.getInt("NR_IDADE");
                     String sexo = rs.getString("DS_SEXO");
                     String email = rs.getString("DS_EMAIL");
                     String senha = rs.getString("DS_SENHA");
                     String telefone = rs.getString("DS_TELEFONE");
                     String celular = rs.getString("DS_CELULAR");
                     
                     Object[] data = {id, nome, idade, sexo, email, senha, telefone, celular};
                     
                     tableModel.addRow(data);                        
               }                  
             }else if (tabela.equals("Mensagens")){
                Object[] columnNames = new Object[4];
                columnNames[0] = "ID da Mensagem";
                columnNames[1] = "ID do Fornecedor";
                columnNames[2] = "Mensagem";
                columnNames[3] = "Status";
                               
                tableModel = new DefaultTableModel(columnNames, 0);
               
               while(rs.next()){
                     int id = rs.getInt("CD_IDMESSAGE");
                     int id_usuario = rs.getInt("CD_ID");
                     String mensagem = rs.getString("DS_MESSAGE");
                     String status = rs.getString("DS_STATUS");
                                          
                     Object[] data = {id, id_usuario, mensagem, status};
                     
                     tableModel.addRow(data);                        
               }            
             }
        }catch(SQLException ex){
           JOptionPane.showMessageDialog(null,"Erro durante a coleta:\n" + ex.toString(), "ERRO", JOptionPane.ERROR_MESSAGE);
        }finally{
           closeConnection(conn);
        }       
        return tableModel;
    }
    
    //método para setar a menssagem e os seus dados para o banco
    public void receiveMessage(int id, String mensagem, String status){
        sql = "INSERT INTO MENSAGENS(CD_ID ,DS_MESSAGE, DS_STATUS)" +
              "VALUES (?, ?, ?)";
        
        conn = getConnection();
        
        try{        
            message = new Mensagens(mensagem);
                       
            PreparedStatement stmt = conn.prepareStatement(sql);
            
            stmt.setInt(1, id);
            stmt.setString(2, message.getMensagem());
            stmt.setString(3, status);
            
            stmt.executeUpdate();
            stmt.close();
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(null, "Erro:\n"
                        + ex.toString(), "ERRO", JOptionPane.ERROR_MESSAGE);            
        }finally{
            closeConnection(conn);
        }
    
    }
    
    //método que faz o login do usuário
    public void login(){
        sql = "SELECT CD_ID, NM_USUARIO, DS_SENHA FROM USUARIOS WHERE NM_USUARIO = '" + getNome() + 
              "' AND DS_SENHA = '" + getSenha() + "'";
       
        
        conn = getConnection();
                             
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            message = new Mensagens(null);
                   
            
            if(rs.next()){
                int id = rs.getInt("CD_ID");
                message.setId_usuario(id);
                JOptionPane.showMessageDialog(null,"Seja bem vindo " + getNome() + "!");               
                cFrame = new ClienteFrame(message.getId_usuario(), getNome());
                cFrame.setVisible(true);                               
            }else{
             JOptionPane.showMessageDialog(null,"Usário ou senha incorretos!\n", "ERRO", JOptionPane.ERROR_MESSAGE);            
            }
                       
            rs.close();
            stmt.close();            
        } catch (SQLException e) {
             JOptionPane.showMessageDialog(null, "Erro na operacao requisitada\n"
                        + e.toString(), "ERRO", JOptionPane.ERROR_MESSAGE);
        }finally{
          closeConnection(conn);
        }        
    }
    
    //médoto que cadastra o usuário no banco
    public void Cadastro(){
       sql = "INSERT INTO USUARIOS (NM_USUARIO, NR_IDADE, DS_SEXO, DS_EMAIL, DS_SENHA, DS_TELEFONE, DS_CELULAR) " + 
             "VALUES(?,?,?,?,?,?,?)";
       
        conn = getConnection();
       
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            
            stmt.setString(1, getNome());
            stmt.setInt(2, getIdade());
            stmt.setString(3, getSexo());
            stmt.setString(4, getEmail());
            stmt.setString(5, getSenha());
            stmt.setString(6, getTelefone());
            stmt.setString(7, getCelular());
            
            stmt.executeUpdate();
            stmt.close();
            JOptionPane.showMessageDialog(null, "Dados cadastrados com sucesso!");
                           
        } catch(SQLException e){
             JOptionPane.showMessageDialog(null, "Erro na operacao requisitada\n"
                        + e.toString(), "ERRO", JOptionPane.ERROR_MESSAGE);
        }finally{
             
             closeConnection(conn);      
        }
    }
}
