package vo;

import java.io.Serializable;
import java.util.Date;

public class EventoVO implements Serializable {

    private static final long serialVersionUID = 3L;

    private int id;
    private String nome;
    private Date dataHoraCriacao;
    private UsuarioVO Usuario;

    public UsuarioVO getUsuario() {
        return Usuario;
    }

    public void setUsuario(UsuarioVO usuario) {
        Usuario = usuario;
    }

    public Date getDataHoraCriacao() {
        return dataHoraCriacao;
    }

    public void setDataHoraCriacao(Date dataHoraCriacao) {
        this.dataHoraCriacao = dataHoraCriacao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return this.nome;
    }
}
