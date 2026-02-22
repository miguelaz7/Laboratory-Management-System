
package backend;

import java.io.Serializable;

public class InvestigadorRobotica extends Investigador implements Serializable {
    private String tipoDeSistemaTrabalhado;
    
    public InvestigadorRobotica(String nome, String email, String password, String tipoDeSistemaTrabalhado){
        super(nome,email,password);
        this.tipoDeSistemaTrabalhado=tipoDeSistemaTrabalhado;
    }
        @Override
    public String getTipo() {
        return "Investigador de Robótica";
    }
}
