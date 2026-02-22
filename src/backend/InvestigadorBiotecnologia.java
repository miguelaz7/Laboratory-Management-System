
package backend;

import java.io.Serializable;

public class InvestigadorBiotecnologia extends Investigador implements Serializable{
    private String AreaDeEspecializacao; 

    public InvestigadorBiotecnologia(String nome, String email, String password, String AreaDeEspecializacao) {
        super(nome,email,password);
        this.AreaDeEspecializacao = AreaDeEspecializacao;
    }
        @Override
    public String getTipo() {
        return "Investigador de Biotecnologia";
    }
    
}
