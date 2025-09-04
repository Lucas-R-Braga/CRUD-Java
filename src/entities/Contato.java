package entities;

public class Contato {

    private String nome;
    private long telefone;

    public Contato(String nome, long telefone) {

        this.nome = nome;
        this.telefone = telefone;

    }

    public String getNome() {
        return nome;
    }
    
    public long getTelefone() {
        return telefone;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public void setTelefone(long telefone) {
        this.telefone = telefone;
    }

    public String toString() {
        return "Nome: " + nome + ", Telefone: " + telefone;
    }

}
