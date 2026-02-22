package backend;

import java.io.Serializable;
import java.time.LocalDate;

public class Atividade implements Serializable {

    private static int proximoId = 1;

    private int idAtividade;
    private ProjetosLab projetoAssociado;
    private Investigador investigadorResponsavel;
    private String tipoDeAtividade;
    private LocalDate data;
    private int duracaoHoras;

    public Atividade(ProjetosLab projeto, Investigador investigador, String tipo, LocalDate data, int duracaoHoras) {
        this.idAtividade = proximoId++;
        this.projetoAssociado = projeto;
        this.investigadorResponsavel = investigador;
        this.tipoDeAtividade = tipo;
        this.data = data;
        this.duracaoHoras = duracaoHoras;
    }

    public int getIdAtividade() {
        return idAtividade;
    }

    public static void setProximoId(int novo) {
        proximoId = novo;
    }

    public ProjetosLab getProjeto() {
        return projetoAssociado;
    }

    public Investigador getInvestigadorResponsavel() {
        return investigadorResponsavel;
    }

    public String getTipoDeAtividade() {
        return tipoDeAtividade;
    }

    public LocalDate getData() {
        return data;
    }

    public int getDuracaoHoras() {
        return duracaoHoras;
    }

    @Override
    public String toString() {
        return "Atividade #" + idAtividade +
               " | Tipo: " + tipoDeAtividade +
               " | Data: " + data +
               " | Duração: " + duracaoHoras + "h" +
               " | Investigador: " + investigadorResponsavel.getNome();
    }
}
