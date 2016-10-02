package vo;

import java.io.Serializable;

/**
 * Created by Jonatas on 02/10/2016.
 */
public class RelItemInspecaoEventoVO implements Serializable {

    private static final long serialVersionUID = 41091L;

    private int id;
    private ItemInspecaoVO itemInspecao;
    private EventoVO evento;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ItemInspecaoVO getItemInspecao() {
        return itemInspecao;
    }

    public void setItemInspecao(ItemInspecaoVO itemInspecao) {
        this.itemInspecao = itemInspecao;
    }

    public EventoVO getEvento() {
        return evento;
    }

    public void setEvento(EventoVO evento) {
        this.evento = evento;
    }

    @Override
    public String toString() {
        return this.evento.getNome() + " - " + this.itemInspecao.getNome() ;
    }


}
