package frontend;

import backend.Administrador;
import backend.Atividade;
import backend.Coordenador;
import backend.Investigador;
import backend.InvestigadorBiotecnologia;
import backend.InvestigadorEnergia;
import backend.Utilizador;
import backend.InvestigadorRobotica;
import backend.Laboratorio;
import backend.ProjetosLab;
import backend.InvestigadorJaPertenceException;
import backend.InvestigadorNaoPertenceAoProjetoException;
import backend.LaboratorioJaPertenceException;
import backend.SemLaboratoriosException;
import backend.Sistema;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Projeto {

    private final Consola consola;
    private Sistema sistema;

    public Projeto() {
        consola = new Consola();
        sistema = new Sistema();
        this.init();
    }

    public static void main(String[] args) {
        Projeto projeto = new Projeto();

        String opcoes[] = {
            "Login",
            "Registo",
            "Carregar dados de ficheiro",
            "Guardar dados",
            "Sair"
        };

        int opcao;
        do {
            opcao = projeto.consola.lerInteiro(opcoes);

            switch (opcao) {
                case 1:
                    projeto.menuLogin();
                    break;
                case 2:
                    projeto.menuRegisto();
                    break;
                case 3:
                    projeto.carregarDadosOpcional();
                    break;
                case 4:
                    projeto.guardarFicheiroObjetos();
                    break;
                case 5:
                    projeto.consola.escrever("A terminar programa...");
                    break;
            }

        } while (opcao != 5);
    }

    private void menuLogin() {
        String email = consola.lerString("Insira o seu email:");
        String password = consola.lerString("Insira a sua password");
        Utilizador user = sistema.validarLogin(email, password);

        if (user == null) {
            consola.escrever("Email ou password incorretos.");
        } else {
            consola.escrever("Login efetuado com sucesso. Bem-vindo " + user.getNome() + "!");
            menuUtilizador(user);
        }
    }

    private void menuRegisto() {
        String nome = consola.lerString("Insira o seu nome:");
        String email = consola.lerString("Insira o seu E-mail:");
        if (sistema.emailExiste(email)) {
            consola.escrever("Esse email já está registado!");
            return;
        }
        String password = consola.lerString("Insira a sua password:");

        String opcoes[] = {
            "Investigador de Biotecnologia",
            "Investigador de Robótica",
            "Investigador de Energia",
            "Coordenador",
            "Administrador"
        };
        int opcao = consola.lerInteiro(opcoes);
        Utilizador user = null;

        switch (opcao) {
            case 1:
                String areaDeEspecializacao = consola.lerString("Insira a sua Área De Especialização:");
                user = new InvestigadorBiotecnologia(nome, email, password, areaDeEspecializacao);
                break;
            case 2:
                String tipoDeSistemaTrabalhado = consola.lerString("Insira o seu tipo de Sistema Trabalhado");
                user = new InvestigadorRobotica(nome, email, password, tipoDeSistemaTrabalhado);
                break;
            case 3:
                String fonte = consola.lerString("Insira a fonte energética principal estudada ");
                user = new InvestigadorEnergia(nome, email, password, fonte);
                break;
            case 4:
                user = new Coordenador(nome, email, password);
                break;
            case 5:
                user = new Administrador(nome, email, password);
                break;
        }
        if (user != null) {
            consola.escrever(user.getTipo() + " " + user.getNome() + " criado com sucesso!");
            sistema.adicionaUtilizador(user);
        }
    }

    public void mostraMenuAdministrador(Administrador admin) {
        String opcoes[] = {
            "Criar Laboratorio",
            "Gerir Laboratórios",
            "Listar Laboratórios",
            "Voltar"
        };
        int opcao;
        do {
            opcao = consola.lerInteiro(opcoes);
            switch (opcao) {
                case 1:
                    String nome = consola.lerString("Insira o nome para atribuir ao Laboratório:");
                    String localizacao = consola.lerString("Insira a localização:");

                    Laboratorio lab = new Laboratorio(nome, localizacao);
                    try {
                        admin.adicionaLab(lab);
                    } catch (LaboratorioJaPertenceException e) {
                        consola.escreverErro("Esse laboratorio já pertence ao admin!");
                    }
                    // associa ao admin
                    sistema.adicionaLaboratorio(lab); // mete no sistema
                    consola.escrever("Laboratorio " + lab.getNome() + " criado com sucesso!");
                    break;

                case 2:
                    menuEscolhaLaboratorio(admin);
                    break;
                case 3:
                    ArrayList<Laboratorio> labs = admin.getListaDeLaboratorios();
                    if (labs.isEmpty()) {
                        consola.escrever("Não existem laboratórios associados!");
                    } else {
                        consola.escrever("Lista de laboratórios do Administrador " + admin.getNome());
                        for (Laboratorio labAMostrar : labs) {
                            consola.escrever("Id: " + labAMostrar.getId() + " Nome: " + labAMostrar.getNome() + " Localização: " + labAMostrar.getLocalizacao());
                        }
                    }
                    break;
                case 4:
                    consola.escrever("A sair do menu de Administrador...");
                    break;
            }
        } while (opcao != 4);
    }

    public void menuEscolhaLaboratorio(Administrador admin) {
        ArrayList<Laboratorio> labs = admin.getListaDeLaboratorios();

        if (labs.isEmpty()) {
            consola.escreverErro("Não tem laboratórios associados. Deverá criar um no menu prinicpal");
            return;
        }
        Laboratorio labSelecionado = null;

        if (labs.size() == 1) {
            labSelecionado = labs.get(0);
            consola.escrever("Tem apenas um laboratório. A gerir " + labSelecionado.getNome());
            mostraMenuGestaoLaboratorio(admin, labSelecionado);
        } else {
            consola.escrever("Selecione um laboratório para gerir:");
            for (int i = 0; i < labs.size(); i++) {
                consola.escrever((i + 1) + " - " + labs.get(i).getNome());
            }
            int escolha = consola.lerInteiro("Escolha um laboratório para gerir :");
            if (escolha < 1 || escolha > labs.size()) {
                consola.escreverErro("Escolha inválida");
                return;
            }
            labSelecionado = labs.get(escolha - 1);
            mostraMenuGestaoLaboratorio(admin, labSelecionado);
        }

    }

    public void mostraMenuGestaoLaboratorio(Administrador admin, Laboratorio lab) {

        String opcoes[] = {
            "Listar Investigadores",
            "Adicionar Investigador",
            "Remover Investigador",
            "Voltar"
        };

        int opcao;

        do {
            opcao = consola.lerInteiro(opcoes);

            switch (opcao) {

                case 1:
                    ArrayList<Investigador> lista = lab.getListaDeInvestigadores();

                    if (lista.isEmpty()) {
                        consola.escrever("Este laboratório não tem investigadores.");
                    } else {
                        consola.escrever("Investigadores do laboratório " + lab.getNome() + ":");
                        for (int i = 0; i < lista.size(); i++) {
                            consola.escrever((i + 1) + " - " + lista.get(i).getNome());
                        }
                    }
                    break;

                case 2:
                    try {

                        ArrayList<Investigador> todosInv = sistema.getInvestigadores();

                        if (todosInv.isEmpty()) {
                            consola.escreverErro("Não existem investigadores registados.");
                            break;
                        }

                        consola.escrever("Selecione um investigador para adicionar:");
                        for (int i = 0; i < todosInv.size(); i++) {
                            consola.escrever((i + 1) + " - " + todosInv.get(i).getNome());
                        }

                        int escolhaAdd = consola.lerInteiro("Escolha: ");
                        if (escolhaAdd < 1 || escolhaAdd > todosInv.size()) {
                            consola.escreverErro("Escolha inválida.");
                            break;
                        }

                        Investigador invAdd = todosInv.get(escolhaAdd - 1);

                        admin.adicionaInvestigadorAoLaboratorio(invAdd, lab);
                        consola.escrever("Investigador adicionado com sucesso");

                    } catch (InvestigadorJaPertenceException e) {
                        consola.escrever("O investigador já pertence ao laboratorio");
                    }
                    break;

                case 3:
                    ArrayList<Investigador> listaLab = lab.getListaDeInvestigadores();

                    if (listaLab.isEmpty()) {
                        consola.escreverErro("Este laboratório não tem investigadores para remover.");
                        break;
                    }

                    consola.escrever("Selecione um investigador para remover:");
                    for (int i = 0; i < listaLab.size(); i++) {
                        consola.escrever((i + 1) + " - " + listaLab.get(i).getNome());
                    }

                    int escolhaRem = consola.lerInteiro("Escolha: ");
                    if (escolhaRem < 1 || escolhaRem > listaLab.size()) {
                        consola.escreverErro("Escolha inválida.");
                        break;
                    }

                    Investigador invRem = listaLab.get(escolhaRem - 1);

                    admin.removerInvestigadorAoLaboratorio(invRem, lab);
                    consola.escrever("Investigador removido com sucesso");

                    break;

                case 4:
                    return;
            }

        } while (opcao != 4);
    }

    public void lerFicheiroObjetos() {
        String ficheiro = consola.lerString("Introduza o nome do ficheiro de onde quer carregar os dados");
        try {
            sistema = sistema.carregarFicheiroObjetos(ficheiro);
            consola.escrever("Ficheiro carregado com sucesso!");
        } catch (Exception ex) {
            consola.escreverErro("Ocorreu um erro ao carregar ficheiro de dados: " + ex.getMessage());
        }
    }

    public void guardarFicheiroObjetos() {
        String ficheiro = consola.lerString("Introduza o nome do ficheiro onde quer guardar os dados: ");
        try {
            sistema.guardarFicheiroObjetos(ficheiro);
            consola.escrever("Ficheiro guardado com sucesso!");
        } catch (Exception ex) {
            consola.escreverErro(ex.getMessage());
        }
    }

    public void mostraMenuCoordenador(Coordenador coordenador) {
        String opcoes[] = {
            "Criar projeto",
            "Gerir projeto",
            "Ver estado dos projetos",
            "Listar projetos",
            "Voltar"
        };

        int opcao;

        do {
            opcao = consola.lerInteiro(opcoes);

            switch (opcao) {
                case 1:
                    dadosCriarProjeto(coordenador);
                    break;

                case 2:
                    mostraListaDeProjetosParaEscolher(coordenador);
                    break;

                case 3:
                    mostrarEstadoDosProjetos(coordenador);
                    break;

                case 4:
                    listarProjetos(coordenador);
                    break;
            }

        } while (opcao != 5);
    }

    public void dadosCriarProjeto(Coordenador coordenador) {
        String titulo = consola.lerString("Insira o titulo do Projeto");
        String area = consola.lerString("Insira a area cientifica");

        ProjetosLab novo = coordenador.criaProjeto(titulo, area);
        sistema.adicionaProjeto(novo);
        consola.escrever("Projeto " + titulo + " criado com sucesso!");
    }

    private void mostraListaDeProjetosParaEscolher(Coordenador coordenador) {

        ArrayList<ProjetosLab> projetos = coordenador.getListaDeProjetos();

        if (projetos.isEmpty()) {
            consola.escrever("O coordenador " + coordenador.getNome() + " não tem projetos criados!");
            return;
        }

        if (projetos.size() == 1) {
            consola.escrever("A gerir o único projeto: " + projetos.get(0).getTituloProjeto());
            gerirProjeto(coordenador, projetos.get(0));
            return;
        }

        consola.escrever("Selecione um projeto para gerir:");
        for (int i = 0; i < projetos.size(); i++) {
            consola.escrever((i + 1) + " - " + projetos.get(i).getTituloProjeto());
        }

        int opcao = consola.lerInteiro("Escolha um projeto: ");

        if (opcao < 1 || opcao > projetos.size()) {
            consola.escrever("Opção inválida!");
            return;
        }

        gerirProjeto(coordenador, projetos.get(opcao - 1));
    }

    private void gerirProjeto(Coordenador coordenador, ProjetosLab projeto) {

        String opcoes[] = {
            "Listar Investigadores",
            "Adicionar Investigador",
            "Remover Investigador",
            "Adicionar Laboratório",
            "Registar Atividade",
            "Suspender Projeto",
            "Retomar Projeto",
            "Concluir Projeto",
            "Voltar"
        };

        int opcao;

        do {
            consola.escrever("\n--- Gestão do Projeto: " + projeto.getTituloProjeto() + " ---");
            consola.escrever("Estado atual: " + projeto.getEstadoDoProjeto());

            opcao = consola.lerInteiro(opcoes);

            switch (opcao) {

                case 1:
                    listarInvestigadoresProjeto(projeto);
                    break;

                case 2:
                    adicionarInvestigadorProjeto(coordenador, projeto);
                    break;

                case 3:
                    removerInvestigadorProjeto(coordenador, projeto);
                    break;

                case 4:
                    adicionarLaboratorioProjeto(coordenador, projeto);
                    break;

                case 5:
                    criarAtividadeProjeto(coordenador, projeto);
                    break;

                case 6:
                    try {
                        projeto.suspender();
                        consola.escrever("Projeto suspenso com sucesso!");
                    } catch (IllegalStateException e) {
                        consola.escreverErro(e.getMessage());
                    }
                    break;

                case 7:
                    try {
                        projeto.retomar();
                        consola.escrever("Projeto retomado com sucesso!");
                    } catch (IllegalStateException e) {
                        consola.escreverErro(e.getMessage());
                    }
                    break;

                case 8:
                    try {
                        projeto.concluir();
                        consola.escrever("Projeto concluído com sucesso!");
                    } catch (IllegalStateException e) {
                        consola.escreverErro(e.getMessage());

                    }

                    break;

                case 9:
                    consola.escrever("A voltar ao menu anterior...");
                    break;
            }

        } while (opcao != 9);
    }

    private void listarInvestigadoresProjeto(ProjetosLab projeto) {
        ArrayList<Investigador> lista = projeto.getInvestigadoresEnvolvidos();

        if (lista.isEmpty()) {
            consola.escrever("Este projeto não tem investigadores ainda.");
            return;
        }

        consola.escrever("Investigadores envolvidos:");
        for (int i = 0; i < lista.size(); i++) {
            consola.escrever((i + 1) + " - " + lista.get(i).getNome());
        }
    }

    private void adicionarInvestigadorProjeto(Coordenador coordenador, ProjetosLab projeto) {

        // Obter todos os investigadores do sistema
        ArrayList<Investigador> investigadores = sistema.getInvestigadores();

        if (investigadores.isEmpty()) {
            consola.escrever("Não existem investigadores registados no sistema.");
            return;
        }

        // Seleciona investigador
        Investigador inv = selecionarInvestigadorDaLista(investigadores,
                "Selecione o investigador a adicionar:");

        if (inv == null) {
            consola.escrever("Escolha inválida.");
            return;
        }

        // Tenta adicionar
        try {
            coordenador.adicionaInvestigadorAoProjeto(inv, projeto);
            consola.escrever("Investigador adicionado com sucesso!");

        } catch (IllegalStateException e) {
            consola.escreverErro(e.getMessage());
        }

    }

    private Investigador selecionarInvestigadorDaLista(ArrayList<Investigador> lista, String mensagem) {

        consola.escrever(mensagem);

        for (int i = 0; i < lista.size(); i++) {
            consola.escrever((i + 1) + " - " + lista.get(i).getNome());
        }

        int escolha = consola.lerInteiro("Escolha: ");

        if (escolha < 1 || escolha > lista.size()) {
            return null;
        }

        return lista.get(escolha - 1);
    }

    private void removerInvestigadorProjeto(Coordenador coordenador, ProjetosLab projeto) {

        ArrayList<Investigador> investigadores = projeto.getInvestigadoresEnvolvidos();

        if (investigadores.isEmpty()) {
            consola.escrever("Este projeto não tem investigadores para remover.");
            return;
        }

        Investigador inv = selecionarInvestigadorDaLista(
                investigadores,
                "Selecione o investigador a remover:"
        );

        if (inv == null) {
            consola.escrever("Escolha inválida.");
            return;
        }

        try {
            coordenador.removerInvestigadorDoProjeto(projeto, inv);
            consola.escrever("Investigador removido com sucesso!");
        } catch (IllegalStateException e) {
            consola.escreverErro(e.getMessage());
        } catch (IllegalArgumentException e) {
            consola.escreverErro(e.getMessage());

        }

    }

    private void adicionarLaboratorioProjeto(Coordenador coordenador, ProjetosLab projeto) {

        List<Laboratorio> labs = sistema.getLaboratorios();

        if (labs.isEmpty()) {
            consola.escrever("Não existem laboratórios no sistema.");
            return;
        }

        consola.escrever("Selecione um laboratório para adicionar:");

        for (int i = 0; i < labs.size(); i++) {
            consola.escrever((i + 1) + " - " + labs.get(i).getNome());
        }

        int escolha = consola.lerInteiro("Escolha: ");

        if (escolha < 1 || escolha > labs.size()) {
            consola.escrever("Opção inválida.");
            return;
        }

        Laboratorio labEscolhido = labs.get(escolha - 1);

        try {
            coordenador.adicionarLaboratorioAoProjeto(projeto, labEscolhido);
            consola.escrever("Laboratório adicionado com sucesso!");
        } catch (IllegalArgumentException e) {
            consola.escreverErro(e.getMessage());
        } catch (IllegalStateException e) {
            consola.escreverErro(e.getMessage());

        }

    }

    private void criarAtividadeProjeto(Coordenador coordenador, ProjetosLab projeto) {

        consola.escrever("\n--- Criar Atividade para o Projeto: " + projeto.getTituloProjeto() + " ---");

        // Lista de investigadores do projeto
        ArrayList<Investigador> investigadores = projeto.getInvestigadoresEnvolvidos();

        if (investigadores.isEmpty()) {
            consola.escreverErro("Não é possível criar atividade: o projeto não tem investigadores!");
            return;
        }

        consola.escrever("Selecione o investigador responsável:");
        for (int i = 0; i < investigadores.size(); i++) {
            consola.escrever((i + 1) + " - " + investigadores.get(i).getNome());
        }

        int escolhaInv = consola.lerInteiro("Escolha: ");
        if (escolhaInv < 1 || escolhaInv > investigadores.size()) {
            consola.escreverErro("Escolha inválida!");
            return;
        }

        Investigador investigadorEscolhido = investigadores.get(escolhaInv - 1);

        // Tipo de atividade
        String tipo = consola.lerString("Tipo de atividade (experiência, teste, simulação, publicação...): ");

        // Data
        LocalDate data = lerData("Insira a data da atividade");

        // Duração
        int duracao = consola.lerInteiro("Duração em horas: ");
        if (duracao <= 0) {
            consola.escreverErro("A duração deve ser maior que 0.");
            return;
        }
        try {
            coordenador.criaAtividade(projeto, investigadorEscolhido, tipo, data, duracao);
            consola.escrever("Atividade criada com sucesso!");
        } catch (InvestigadorNaoPertenceAoProjetoException e) {
            consola.escreverErro("O investigador não pertence ao projeto");
        }

    }

    private LocalDate lerData(String mensagem) {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");// ter a data no formato pertendido, fui ao chatGPT - Miguel

        while (true) {
            try {
                String input = consola.lerString(mensagem + " (dd/mm/aaaa): ");
                return LocalDate.parse(input, formato);

            } catch (Exception e) {
                consola.escreverErro("Data inválida! Tente novamente.");
            }
        }
    }

    private void menuEditarInformacoes(Utilizador user) {

        String[] opcoes = {
            "Alterar nome",
            "Alterar password",
            "Voltar"
        };

        int opcao;

        do {
            consola.escrever("\n--- Editar Informações Pessoais ---");
            consola.escrever("Nome atual: " + user.getNome());
            consola.escrever("Email (não editável): " + user.getEmail());

            opcao = consola.lerInteiro(opcoes);

            switch (opcao) {

                case 1:
                    String novoNome = consola.lerString("Insira o novo nome: ");
                    if (!novoNome.isBlank()) {
                        user.setNome(novoNome);
                        consola.escrever("Nome atualizado com sucesso!");
                    } else {
                        consola.escreverErro("O nome não pode estar vazio.");
                    }
                    break;

                case 2:
                    String novaPass = consola.lerString("Insira a nova password: ");
                    if (!novaPass.isBlank()) {
                        user.setPassword(novaPass);
                        consola.escrever("Password atualizada com sucesso!");
                    } else {
                        consola.escreverErro("A password não pode estar vazia.");
                    }
                    break;

                case 3:
                    return;
            }

        } while (opcao != 3);
    }

    private void abrirMenuPorTipo(Utilizador user) {

        switch (user.getTipo()) {

            case "Administrador":
                mostraMenuAdministrador((Administrador) user);
                break;

            case "Coordenador":
                mostraMenuCoordenador((Coordenador) user);
                break;
            default:
                mostraMenuInvestigador((Investigador) user);
                break;
        }
    }

    private void menuUtilizador(Utilizador user) {

        String[] opcoes = {
            "Ver / Editar informações pessoais",
            "Ir para menu de " + user.getTipo(),
            "Logout"
        };

        int opcao;

        do {
            consola.escrever("\n===== Área do Utilizador =====");
            consola.escrever("Utilizador: " + user.getNome() + " (" + user.getTipo() + ")");
            consola.escrever("Email: " + user.getEmail());
            consola.escrever("===============================");

            opcao = consola.lerInteiro(opcoes);

            switch (opcao) {

                case 1:
                    menuEditarInformacoes(user);
                    break;

                case 2:
                    abrirMenuPorTipo(user);
                    break;

                case 3:
                    consola.escrever("Sessão terminada.");
                    return;
            }

        } while (opcao != 3);
    }

    private void mostraMenuInvestigador(Investigador inv) {

        String opcoes[] = {
            "Ver projetos onde participo",
            "Ver minhas atividades",
            "Ver estatísticas",
            "Ver Laboratórios com os quais estou envolvido",
            "Voltar"
        };

        int opcao;

        do {
            consola.escrever("\n--- Menu do Investigador: " + inv.getNome() + " ---");
            opcao = consola.lerInteiro(opcoes);

            switch (opcao) {

                case 1:
                    verProjetosDoInvestigador(inv);
                    break;

                case 2:
                    verAtividadesDoInvestigador(inv);
                    break;

                case 3:
                    verEstatisticasDoInvestigador(inv);
                    break;
                case 4:
                    try {
                        verLaboratoriosDoInvestigador(inv);
                    } catch (SemLaboratoriosException e) {
                        consola.escreverErro("Sem laboratorios para mostrar");
                    }
            }

        } while (opcao != 5);
    }

    private void verProjetosDoInvestigador(Investigador inv) {
        ArrayList<ProjetosLab> projetos = sistema.getProjetosDoInvestigador(inv);

        if (projetos.isEmpty()) {
            consola.escrever("Não estás envolvido em nenhum projeto.");
            return;
        }

        consola.escrever("Projetos onde participas:");
        for (ProjetosLab p : projetos) {
            consola.escrever(" - " + p.getTituloProjeto() + " (" + p.getEstadoDoProjeto() + ")");
        }
    }

    private void verAtividadesDoInvestigador(Investigador inv) {
        ArrayList<Atividade> atividades = sistema.getAtividadesDoInvestigador(inv);

        if (atividades.isEmpty()) {
            consola.escrever("Ainda não foram registadas atividades relaizadas por ti.");
            return;
        }

        consola.escrever("Atividades realizadas:");
        for (Atividade a : atividades) {
            consola.escrever(a.toString());
        }
    }

    private void verEstatisticasDoInvestigador(Investigador inv) {
        consola.escrever("\n--- Estatísticas pessoais ---");
        consola.escrever(sistema.gerarEstatisticasInvestigador(inv));
    }

    public void carregarDadosOpcional() {
        String ficheiro = consola.lerString("Introduza o nome do ficheiro a carregar:");

        try {
            sistema = sistema.carregarFicheiroObjetos(ficheiro);
            consola.escrever("Dados carregados com sucesso!");
        } catch (java.io.FileNotFoundException e) {
            consola.escreverErro("O ficheiro não existe! (Normal se for a primeira vez a correr o programa)");
        } catch (Exception e) {
            consola.escreverErro("Erro ao carregar ficheiro: " + e.getMessage());
        }
    }

    private void mostrarEstadoDosProjetos(Coordenador coordenador) {

        ArrayList<ProjetosLab> projetos = coordenador.getListaDeProjetos();

        if (projetos.isEmpty()) {
            consola.escrever("Ainda não existe nenhum projeto criado.");
            return;
        }

        consola.escrever("\n--- Estado dos Projetos ---");

        for (ProjetosLab p : projetos) {
            consola.escrever(
                    "Projeto #" + p.getIdProjeto()
                    + " - " + p.getTituloProjeto()
                    + " | Estado: " + p.getEstadoDoProjeto()
            );
        }
    }

    private void listarProjetos(Coordenador coordenador) {

        ArrayList<ProjetosLab> projetos = coordenador.getListaDeProjetos();

        if (projetos.isEmpty()) {
            consola.escrever("Não existem projetos a listar.");
            return;
        }

        consola.escrever("\n--- Lista de Projetos ---");

        for (ProjetosLab p : projetos) {

            consola.escrever(
                    "\nProjeto #" + p.getIdProjeto()
                    + "\nTítulo: " + p.getTituloProjeto()
                    + "\nÁrea Científica: " + p.getAreaCientifica()
                    + "\nEstado: " + p.getEstadoDoProjeto()
                    + "\nInvestigadores envolvidos: " + p.getNumeroDeInvestigadores()
                    + "\nLaboratórios envolvidos: " + p.getNumeroDeLaboratorios()
                    + "\nTotal de atividades: " + p.getTotalDeAtividades()
                    + "\nTotal de horas: " + p.getTotalHorasAtividades()
            );
        }
    }

    private void init() {

        Investigador inv = new InvestigadorRobotica("joao", "joao@gmail.com", "joao", "robos");
        sistema.adicionaUtilizador(inv);
        Laboratorio lab = new Laboratorio("lab", "guimaraes");
        sistema.adicionaLaboratorio(lab);
        Administrador admin = new Administrador("admin", "admin@gmail.com", "admin");
        sistema.adicionaUtilizador(admin);
        try {
            admin.adicionaLab(lab);
        } catch (LaboratorioJaPertenceException e) {
            consola.escreverErro("Laboratorio já pertence ao Admin!");
        }
        try {
            admin.adicionaInvestigadorAoLaboratorio(inv, lab);
        } catch (InvestigadorJaPertenceException e) {
            System.out.println("Erro ao iniciar sistema");
        }
    }

    private void verLaboratoriosDoInvestigador(Investigador inv) throws SemLaboratoriosException {
        ArrayList<Laboratorio> listaLabs = new ArrayList<>();
        for (ProjetosLab p : inv.getListaDeProjetos()) {
            for (Laboratorio l : p.getLaboratoriosEnvolvidos()) {
                if (!listaLabs.contains(l)) {
                    listaLabs.add(l);
                }
            }
        }

        if (listaLabs.isEmpty()) {
            throw new SemLaboratoriosException();
        }
        System.out.println("Lista de Laboratórios com os quais estás envolvido:");
        for (Laboratorio l : listaLabs) {
            System.out.println("ID: " + l.getId() + " - " + l.getNome());
        }
    }
}
