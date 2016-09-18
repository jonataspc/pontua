package vo;

import java.io.Serializable;
import java.math.BigDecimal;

public class ItemInspecaoVO implements Serializable {

    private static final long serialVersionUID = 4109L;

    private int id;
    private AreaVO area;
    private String nome;
    private BigDecimal pontuacaoMinima;
    private BigDecimal pontuacaoMaxima;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public AreaVO getArea() {
        return area;
    }

    public void setArea(AreaVO area) {
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
