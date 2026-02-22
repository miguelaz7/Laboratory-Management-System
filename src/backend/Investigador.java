package backend;

import java.io.Serializable;
import java.util.ArrayList;

public class Investigador extends Utilizador implements Serializable {

    private ArrayList<ProjetosLab> listaDeProjetos;

    public Investigador(String nome, String email, String password) {
        super(nome, email, password);
        listaDeProjetos = new ArrayList<ProjetosLab>();
    }

    public void adicionarProjeto(ProjetosLab projeto) {
        if (projeto != null && !listaDeProjetos.contains(projeto)) {
            listaDeProjetos.add(projeto);
        }
    }

    public ArrayList<ProjetosLab> getListaDeProjetos() {
        return listaDeProjetos;
    }

    public void removerProjeto(ProjetosLab projeto) {
        if (projeto == null) {
            throw new IllegalArgumentException("Projeto inválido");
        }
        listaDeProjetos.remove(projeto);

    }

    @Override
    public String getTipo() {
        return "Investigador";
    }

}
