
package backend;

import java.io.Serializable;

public class InvestigadorEnergia extends Investigador implements Serializable {
    private String fonteDeEnergiaEstudada;
    
    public InvestigadorEnergia(String nome, String email, String password, String fonteDeEnergiaEstudada){
        super(nome,email,password);
        this.fonteDeEnergiaEstudada=fonteDeEnergiaEstudada;
    }
    
        @Override
    public String getTipo() {
        return "Investigador de Energia";
    }
}
