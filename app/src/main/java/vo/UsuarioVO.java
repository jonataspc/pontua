package vo;

import java.io.Serializable;

public class UsuarioVO implements Serializable {

    private static final long serialVersionUID = 6L;


    private int id;
    private EntidadeVO entidade;
    private String nome;
    private String senha;
    private EnumNivelAcesso nivelAcesso;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public EntidadeVO getEntidade() {
        return entidade;
    }

    public void setEntidade(EntidadeVO entidade) {
        this.entidade = entidade;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public EnumNivelAcesso getNivelAcesso() {
        return nivelAcesso;
    }

    public void setNivelAcesso(EnumNivelAcesso nivelAcesso) {
        this.nivelAcesso = nivelAcesso;
    }

    public enum EnumNivelAcesso {
        Administrador,
        Avaliador,
        Entidade
    }

    @Override
    public String toString() {
        return this.nome;
    }
}
