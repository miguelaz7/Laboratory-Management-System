package backend;

import java.io.Serializable;
import java.util.ArrayList;

public class Laboratorio implements Serializable {

    private static int proximoId = 1;
    private int id;
    private String nome;
    private String localizacao;
    private ArrayList<Investigador> listaDeInvestigadores;
    private int nrDeProjetosAtivos;

    public Laboratorio(String nome, String localizacao) {
        this.nome = nome;
        this.localizacao = localizacao;
        this.id = proximoId++;
        this.nrDeProjetosAtivos = 0;
        listaDeInvestigadores = new ArrayList<Investigador>();
    }

    public int getId() {
        return id;
    }

    public static void setProximoId(int novo) {
        proximoId = novo;
    }

    public String getNome() {
        return nome;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public ArrayList<Investigador> getListaDeInvestigadores() {
        return listaDeInvestigadores;
    }

    public int getNrDeProjetosAtivos() {
        return nrDeProjetosAtivos;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    public void setNrDeProjetosAtivos(int nrDeProjetosAtivos) {
        this.nrDeProjetosAtivos = nrDeProjetosAtivos;
    }

    public void adicionaInvestigador(Investigador inv) throws InvestigadorJaPertenceException {

        if (listaDeInvestigadores.contains(inv)) {
            throw new InvestigadorJaPertenceException();
        }

        listaDeInvestigadores.add(inv);
    }

    public void removeInvestigador(Investigador inv) {

        listaDeInvestigadores.remove(inv);
    }

    @Override
    public String toString() {
        return "Laboratorio{" + "id=" + id + ", nome=" + nome + ", localizacao=" + localizacao + ", listaDeInvestigadores=" + listaDeInvestigadores + ", nrDeProjetosAtivos=" + nrDeProjetosAtivos + '}';
    }

}
