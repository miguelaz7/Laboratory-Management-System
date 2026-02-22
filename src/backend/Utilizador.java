package backend;

import java.io.Serializable;

public abstract class Utilizador implements Serializable{

    private static int proximoId = 1;
    
    private String nome;
    private String email;
    private int id;
    private String password;
    
    
    public Utilizador(String nome, String email, String password){
        this.id=proximoId++;
        this.nome=nome;
        this.email=email;
        this.password=password;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static void setProximoId(int proximoId) {
        Utilizador.proximoId = proximoId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /*@Override
    public String toString() {
        return "Utilizador{" + "nome=" + nome + ", email=" + email + ", id=" + id + '}';
    }*/

    public abstract String getTipo();
        @Override
    public String toString() {
        return "ID: " + this.getId() + " - " + this.getNome() + "\n";
    }
    
}
