package vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class AvaliacaoVO implements Serializable {

    private static final long serialVersionUID = 1001L;


    private int id;
    private RelEntidadeEventoVO relEntidadeEvento;
    private RelItemInspecaoEventoVO relItemInspecaoEvento;
    private UsuarioVO usuario;
    private BigDecimal pontuacao;
    private EnumMetodoAvaliacao metodo;
    private Date dataHora;



    @Override
    public String toString() {
        return "AvaliacaoVO{" +
                "dataHora=" + dataHora +
                ", id=" + id +
                ", relEntidadeEvento=" + relEntidadeEvento.getEntidade().getNome() + "/" + relEntidadeEvento.getEvento().getNome() +
                ", relItemInspecaoEvento=" + relItemInspecaoEvento.getItemInspecao().getNome() + "/" + relItemInspecaoEvento.getEvento().getNome() +
                ", usuario=" + usuario +
                ", pontuacao=" + pontuacao +
                ", metodo=" + metodo.toString() +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public RelEntidadeEventoVO getRelEntidadeEvento() {
        return relEntidadeEvento;
    }

    public void setRelEntidadeEvento(RelEntidadeEventoVO relEntidadeEvento) {
        this.relEntidadeEvento = relEntidadeEvento;
    }

    public RelItemInspecaoEventoVO getRelItemInspecaoEvento() {
        return relItemInspecaoEvento;
    }

    public void setRelItemInspecaoEvento(RelItemInspecaoEventoVO relItemInspecaoEvento) {
        this.relItemInspecaoEvento = relItemInspecaoEvento;
    }

    public UsuarioVO getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioVO usuario) {
        this.usuario = usuario;
    }

    public BigDecimal getPontuacao() {
        return pontuacao;
    }

    public void setPontuacao(BigDecimal pontuacao) {
        this.pontuacao = pontuacao;
    }

    public Date getDataHora() {
        return dataHora;
    }

    public void setDataHora(Date dataHora) {
        this.dataHora = dataHora;
    }

    public EnumMetodoAvaliacao getMetodo() {
        return metodo;
    }

    public void setMetodo(EnumMetodoAvaliacao metodo) {
        this.metodo = metodo;
    }

    public enum EnumMetodoAvaliacao {
        Manual,
        NFC
    }
}
