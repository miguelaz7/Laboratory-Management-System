package backend;

import frontend.Consola;
import java.io.Serializable;
import java.util.ArrayList;

public class Administrador extends Utilizador implements Serializable {

    private ArrayList<Laboratorio> listaDeLaboratorios;
    private ArrayList<ProjetosLab> listaDeProjetos;

    public Administrador(String nome, String email, String password) {
        super(nome, email, password);
        listaDeProjetos = new ArrayList<ProjetosLab>();
        listaDeLaboratorios = new ArrayList<Laboratorio>();
    }

    public ArrayList<Laboratorio> getListaDeLaboratorios() {
        return listaDeLaboratorios;
    }

    public void adicionaLab(Laboratorio lab) throws LaboratorioJaPertenceException {
        if (listaDeLaboratorios.contains(lab)) {
            throw new LaboratorioJaPertenceException();
        }
        listaDeLaboratorios.add(lab);

    }

    public void adicionaInvestigadorAoLaboratorio(Investigador inv, Laboratorio lab) throws InvestigadorJaPertenceException {

        lab.adicionaInvestigador(inv);
    }

    public void removerInvestigadorAoLaboratorio(Investigador inv, Laboratorio lab) {

        lab.removeInvestigador(inv);
    }

    @Override
    public String getTipo() {
        return "Administrador";
    }
}
