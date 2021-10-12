/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package funcionalidades;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author mathe
 */
public class RegistradorTempo {
    
    public String hora(){
     Date data = new Date();
     SimpleDateFormat formatar = new SimpleDateFormat("HH:mm\n");
     String dataFormatada = formatar.format(data);
        return formatar.format(data);
}
}




