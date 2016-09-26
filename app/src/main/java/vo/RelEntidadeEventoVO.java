package vo;

import java.io.Serializable;

/**
 * Created by Jonatas on 25/09/2016.
 */
public class RelEntidadeEventoVO implements Serializable {

    private static final long serialVersionUID = 4109L;

    private int id;
    private EntidadeVO entidade;
    private EventoVO evento;

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

    public EventoVO getEvento() {
        return evento;
    }

    public void setEvento(EventoVO evento) {
        this.evento = evento;
    }

    @Override
    public String toString() {
        return this.evento.getNome() + " - " + this.entidade.getNome() ;
    }

}
