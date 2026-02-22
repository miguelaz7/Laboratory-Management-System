package backend;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Sistema implements Serializable {

    private List<Utilizador> utilizadores;
    private List<Laboratorio> laboratorios;
    private List<ProjetosLab> projetos;

    public Sistema() {
        utilizadores = new ArrayList<Utilizador>();
        laboratorios = new ArrayList<Laboratorio>();
        projetos = new ArrayList<ProjetosLab>();
    }

    public List<Laboratorio> getLaboratorios() {
        return laboratorios;
    }

    public void adicionaProjeto(ProjetosLab projeto) {
        if (projeto != null && !projetos.contains(projeto)) {
            projetos.add(projeto);
        }
    }

    public void adicionaLaboratorio(Laboratorio lab) {
        laboratorios.add(lab);
    }

    public List<ProjetosLab> getProjetos() {
        return projetos;
    }

    public List<Utilizador> getUtilizadores() {
        return this.utilizadores;
    }

    public void adicionaUtilizador(Utilizador user) {
        utilizadores.add(user);
    }

    public boolean emailExiste(String email) {
        for (Utilizador u : utilizadores) {
            if (u.getEmail().equalsIgnoreCase(email)) {
                return true;
            }
        }
        return false;
    }

    public Utilizador validarLogin(String email, String password) {
        for (Utilizador u : utilizadores) {
            if (u.getEmail().equalsIgnoreCase(email)
                    && u.getPassword().equals(password)) {
                return u;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "Sistema{" + "utilizadores=" + utilizadores + '}';
    }

    // Método para guardar os dados em ficheiro (serialização)
    public void guardarFicheiroObjetos(String ficheiro) throws Exception {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ficheiro))) {

            oos.writeObject(utilizadores);   // Guarda utilizadores
            oos.writeObject(laboratorios);   // Guarda laboratórios
            oos.writeObject(projetos);       // Guarda projetos 
        }
    }

    
    public Sistema carregarFicheiroObjetos(String ficheiro) throws Exception {
        Sistema novoSistema = new Sistema();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ficheiro))) {

            novoSistema.utilizadores = (ArrayList<Utilizador>) ois.readObject();
            novoSistema.laboratorios = (ArrayList<Laboratorio>) ois.readObject();
            novoSistema.projetos = (ArrayList<ProjetosLab>) ois.readObject(); 
        }

        // Recalcular IDs automáticos
        novoSistema.atualizarTodosOsProximosIds();

        return novoSistema;
    }

    public void atualizarTodosOsProximosIds() {

        //  Atualizar ID dos utilizadores
        int maxUserId = 0;
        for (Utilizador u : utilizadores) {
            if (u.getId() > maxUserId) {
                maxUserId = u.getId();
            }
        }
        Utilizador.setProximoId(maxUserId + 1);

        //  Atualizar ID dos laboratórios
        int maxLabId = 0;
        for (Laboratorio l : laboratorios) {
            if (l.getId() > maxLabId) {
                maxLabId = l.getId();
            }
        }
        Laboratorio.setProximoId(maxLabId + 1);

        //  Atualizar ID dos projetos
        int maxProjId = 0;
        for (Utilizador u : utilizadores) {
            if (u instanceof Coordenador coord) {
                for (ProjetosLab p : coord.getListaDeProjetos()) {
                    if (p.getIdProjeto() > maxProjId) {
                        maxProjId = p.getIdProjeto();
                    }
                }
            }
        }
        ProjetosLab.setProximoId(maxProjId + 1);

        //  Atualizar ID das atividades
        int maxAtividadeId = 0;

        for (Utilizador u : utilizadores) {
            if (u instanceof Coordenador coord) {
                for (ProjetosLab p : coord.getListaDeProjetos()) {

                    for (Atividade a : p.getListaDeAtividades()) {

                        if (a.getIdAtividade() > maxAtividadeId) {
                            maxAtividadeId = a.getIdAtividade();
                        }
                    }
                }
            }
        }

        Atividade.setProximoId(maxAtividadeId + 1);
    }

    public ArrayList<ProjetosLab> getProjetosDoInvestigador(Investigador inv) {
        ArrayList<ProjetosLab> lista = new ArrayList<>();
        for (ProjetosLab p : projetos) {
            if (p.getInvestigadoresEnvolvidos().contains(inv)) {
                lista.add(p);
            }
        }
        return lista;
    }

    public ArrayList<Atividade> getAtividadesDoInvestigador(Investigador inv) {
        ArrayList<Atividade> lista = new ArrayList<>();
        for (ProjetosLab p : projetos) {
            for (Atividade a : p.getListaDeAtividades()) {
                if (a.getInvestigadorResponsavel().equals(inv)) {
                    lista.add(a);
                }
            }
        }
        return lista;
    }

    public ArrayList<Investigador> getInvestigadores() {
        ArrayList<Investigador> lista = new ArrayList<>();
        for (Utilizador u : utilizadores) {
            if (u instanceof Investigador inv) {
                lista.add(inv);
            }
        }
        return lista;
    }

    public String gerarEstatisticasInvestigador(Investigador inv) {
        ArrayList<ProjetosLab> projetosInv = getProjetosDoInvestigador(inv);
        ArrayList<Atividade> atividadesInv = getAtividadesDoInvestigador(inv);

        int totalHoras = 0;
        LocalDate primeiraData = null;
        LocalDate ultimaData = null;

        for (Atividade a : atividadesInv) {
            totalHoras += a.getDuracaoHoras();

            if (primeiraData == null || a.getData().isBefore(primeiraData)) {
                primeiraData = a.getData();
            }
            if (ultimaData == null || a.getData().isAfter(ultimaData)) {
                ultimaData = a.getData();
            }
        }

        return "Projetos envolvidos: " + projetosInv.size() + "\n"
                + "Atividades realizadas: " + atividadesInv.size() + "\n"
                + "Total de horas trabalhadas: " + totalHoras + "\n"
                + "Primeira atividade: " + (primeiraData == null ? "N/A" : primeiraData) + "\n"
                + "Última atividade: " + (ultimaData == null ? "N/A" : ultimaData);
    }

}
