package vo;

import java.io.Serializable;

public class EntidadeVO implements Serializable {

    private static final long serialVersionUID = 2L;

    private int id;
    private EventoVO evento;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public EventoVO getEvento() {
        return evento;
    }

    public void setEvento(EventoVO evento) {
        this.evento = evento;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    private String nome;

    @Override
    public String toString() {
        return this.nome;
    }

}
