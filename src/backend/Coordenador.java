package backend;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

public class Coordenador extends Utilizador implements Serializable {

    private ArrayList<ProjetosLab> listaDeProjetos;

    public Coordenador(String nome, String email, String password) {
        super(nome, email, password);
        listaDeProjetos = new ArrayList<>();
    }

    public void adicionarProjeto(ProjetosLab projeto) {
        if (projeto != null && !listaDeProjetos.contains(projeto)) {
            listaDeProjetos.add(projeto);
        }
    }

    public ProjetosLab criaProjeto(String titulo, String areaCientifica) {
        ProjetosLab novo = new ProjetosLab(titulo, areaCientifica, this);
        adicionarProjeto(novo);
        return novo;
    }

    public ArrayList<ProjetosLab> getListaDeProjetos() {
        return listaDeProjetos;
    }

    public void adicionaInvestigadorAoProjeto(Investigador inv, ProjetosLab projeto) {

        if (inv == null) {
            throw new IllegalArgumentException("Investigador inválido.");
        }

        if (projeto == null || !listaDeProjetos.contains(projeto)) {
            throw new IllegalStateException("O projeto não pertence a este coordenador.");
        }

        projeto.adicionarInvestigador(inv);
    }

    public void removerInvestigadorDoProjeto(ProjetosLab projeto, Investigador inv) {

        if (projeto == null || !listaDeProjetos.contains(projeto)) {
            throw new IllegalStateException("O projeto não pertence a este coordenador.");
        }

        projeto.removerInvestigador(inv);
    }

    public void adicionarLaboratorioAoProjeto(ProjetosLab projeto, Laboratorio lab) {

        if (projeto == null || !listaDeProjetos.contains(projeto)) {
            throw new IllegalStateException("O projeto não pertence a este coordenador.");
        }

        projeto.adicionarLaboratorio(lab);
    }

    public void removerLaboratorioDoProjeto(ProjetosLab projeto, Laboratorio lab) {

        if (projeto == null || !listaDeProjetos.contains(projeto)) {
            throw new IllegalStateException("O projeto não pertence a este coordenador.");
        }

        projeto.removerLaboratorio(lab);
    }

    public void criaAtividade(ProjetosLab projeto, Investigador investigador, String tipo, LocalDate data, int duracaoHoras)
            throws InvestigadorNaoPertenceAoProjetoException {

        if (projeto == null || !listaDeProjetos.contains(projeto)) {
            throw new IllegalStateException("O projeto não pertence a este coordenador.");
        }

        if (investigador == null) {
            throw new IllegalArgumentException("Investigador inválido.");
        }

        if (duracaoHoras <= 0) {
            throw new IllegalArgumentException("A duração deve ser maior que zero.");
        }

        if (!projeto.getInvestigadoresEnvolvidos().contains(investigador)) {
            throw new InvestigadorNaoPertenceAoProjetoException("O investigador não pertence ao projeto!");
        }

        Atividade nova = new Atividade(projeto, investigador, tipo, data, duracaoHoras);
        projeto.adicionarAtividade(nova);
    }

    @Override
    public String getTipo() {
        return "Coordenador";
    }

}
