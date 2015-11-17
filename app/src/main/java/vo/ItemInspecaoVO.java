package vo;

import java.io.Serializable;
import java.math.BigDecimal;

public class ItemInspecaoVO implements Serializable {

    private static final long serialVersionUID = 4L;

    private int id;
    private EventoVO evento;
    private String area;
    private String nome;
    private BigDecimal pontuacaoMinima;
    private BigDecimal pontuacaoMaxima;

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

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public BigDecimal getPontuacaoMinima() {
        return pontuacaoMinima;
    }

    public void setPontuacaoMinima(BigDecimal pontuacaoMinima) {
        this.pontuacaoMinima = pontuacaoMinima;
    }

    public BigDecimal getPontuacaoMaxima() {
        return pontuacaoMaxima;
    }

    public void setPontuacaoMaxima(BigDecimal pontuacaoMaxima) {
        this.pontuacaoMaxima = pontuacaoMaxima;
    }

    @Override
    public String toString() {
        return this.nome;
    }
}
