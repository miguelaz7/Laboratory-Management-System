package backend;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProjetosLab implements Serializable {

    private static int proximoId = 1;

    private String tituloProjeto;
    private String areaCientifica;
    private ArrayList<Laboratorio> laboratoriosEnvolvidos;
    private ArrayList<Investigador> investigadoresEnvolvidos;
    private Coordenador coordenador;
    private String estadoDoProjeto;
    private int idProjeto;
    private ArrayList<Atividade> listaDeAtividades;

    public ProjetosLab(String tituloProjeto, String areaCientifica, Coordenador coordenador) {
        this.idProjeto = proximoId++;
        this.tituloProjeto = tituloProjeto;
        this.areaCientifica = areaCientifica;
        this.coordenador = coordenador;
        this.estadoDoProjeto = "Em curso";

        laboratoriosEnvolvidos = new ArrayList<>();
        investigadoresEnvolvidos = new ArrayList<>();
        listaDeAtividades = new ArrayList<>();
    }

    public String getTituloProjeto() {
        return tituloProjeto;
    }

    public String getAreaCientifica() {
        return areaCientifica;
    }

    public ArrayList<Laboratorio> getLaboratoriosEnvolvidos() {
        return laboratoriosEnvolvidos;
    }

    public ArrayList<Investigador> getInvestigadoresEnvolvidos() {
        return investigadoresEnvolvidos;
    }

    public static void setProximoId(int novo) {
        proximoId = novo;
    }

    public ArrayList<Atividade> getListaDeAtividades() {
        return listaDeAtividades;
    }

    public Coordenador getCoordenador() {
        return coordenador;
    }

    public int getIdProjeto() {
        return idProjeto;
    }

    public String getEstadoDoProjeto() {
        return estadoDoProjeto;
    }

    public void adicionarInvestigador(Investigador inv) {
        if (inv == null) {
            throw new IllegalArgumentException("Investigador nulo!");
        }
        if (investigadoresEnvolvidos.contains(inv)) {
            throw new IllegalStateException("O investigador já pertence ao projeto!");
        }
        inv.adicionarProjeto(this);
        investigadoresEnvolvidos.add(inv);

    }

    public void removerInvestigador(Investigador inv) {

        if (inv == null) {
            throw new IllegalArgumentException("Investigador inválido.");
        }

        if (!investigadoresEnvolvidos.contains(inv)) {
            throw new IllegalStateException("O investigador não pertence ao projeto.");
        }

        investigadoresEnvolvidos.remove(inv);
        inv.removerProjeto(this);
    }

    public void adicionarLaboratorio(Laboratorio lab) {

        if (lab == null) {
            throw new IllegalArgumentException("Laboratório inválido.");
        }

        if (laboratoriosEnvolvidos.contains(lab)) {
            throw new IllegalStateException("O laboratório já está associado ao projeto.");
        }

        laboratoriosEnvolvidos.add(lab);
    }

    public void removerLaboratorio(Laboratorio lab) {

        if (lab == null) {
            throw new IllegalArgumentException("Laboratório inválido.");
        }

        if (!laboratoriosEnvolvidos.contains(lab)) {
            throw new IllegalStateException("O laboratório não está associado ao projeto.");
        }

        laboratoriosEnvolvidos.remove(lab);
    }

    public void adicionarAtividade(Atividade nova) {

        if (nova == null) {
            throw new IllegalArgumentException("Atividade inválida.");
        }

        if (listaDeAtividades.contains(nova)) {
            throw new IllegalStateException("A atividade já existe no projeto.");
        }

        if (nova.getProjeto() != this) {
            throw new IllegalStateException("A atividade não pertence a este projeto.");
        }

        listaDeAtividades.add(nova);
    }

    public int getTotalDeAtividades() {
        return listaDeAtividades.size();
    }

    public int getNumeroDeInvestigadores() {
        return investigadoresEnvolvidos.size();
    }

    public int getNumeroDeLaboratorios() {
        return laboratoriosEnvolvidos.size();
    }

    public int getTotalHorasAtividades() {
        int total = 0;
        for (Atividade a : listaDeAtividades) {
            total += a.getDuracaoHoras();
        }
        return total;
    }

    public void suspender() throws IllegalStateException {
        if (estadoDoProjeto.equals("Concluído")) {
            throw new IllegalStateException("Não pode suspender um projeto que ja foi concluido!");
        }
        if (estadoDoProjeto.equals("Suspenso")) {
            throw new IllegalStateException("Não pode suspender um projeto que ja esta suspenso!");
        }
        estadoDoProjeto = "Suspenso";
    }

    public void retomar() throws IllegalStateException {
        if (estadoDoProjeto.equals("Concluído")) {
            throw new IllegalStateException("Não pode retomar um projeto que ja foi concluido!");
        }
        if (estadoDoProjeto.equals("Em curso")) {
            throw new IllegalStateException("Não pode retomar um projeto que ja está em curso!");
        }
        estadoDoProjeto = "Em curso";
    }

    public void concluir() throws IllegalStateException {
        if (estadoDoProjeto.equals("Concluído")) {
            throw new IllegalStateException("Não pode concluir um projeto que ja foi concluido!");
        }
        estadoDoProjeto = "Concluído";
    }

    @Override
    public String toString() {
        return "Projeto #" + idProjeto + " - " + tituloProjeto
                + " (" + areaCientifica + ") - Estado: " + estadoDoProjeto;
    }
}
