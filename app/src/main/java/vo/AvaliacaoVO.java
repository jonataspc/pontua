package vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class AvaliacaoVO implements Serializable {

    private static final long serialVersionUID = 1L;


    private int id;
    private EntidadeVO entidade;
    private ItemInspecaoVO itemInspecao;
    private UsuarioVO usuario;
    private BigDecimal pontuacao;
    private int forma_automatica;
    private Date dataHora;

    public Date getDataHora() {
        return dataHora;
    }

    public void setDataHora(Date dataHora) {
        this.dataHora = dataHora;
    }

    public int getForma_automatica() {
        return forma_automatica;
    }

    public void setForma_automatica(int forma_automatica) {
        this.forma_automatica = forma_automatica;
    }

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

    public ItemInspecaoVO getItemInspecao() {
        return itemInspecao;
    }

    public void setItemInspecao(ItemInspecaoVO itemInspecao) {
        this.itemInspecao = itemInspecao;
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

    @Override
    public String toString() {
        return "AvaliacaoVO{" +
                "dataHora=" + dataHora +
                ", id=" + id +
                ", entidade=" + entidade +
                ", itemInspecao=" + itemInspecao +
                ", usuario=" + usuario +
                ", pontuacao=" + pontuacao +
                ", forma_automatica=" + forma_automatica +
                '}';
    }

    public void setPontuacao(BigDecimal pontuacao) {
        this.pontuacao = pontuacao;
    }



}
