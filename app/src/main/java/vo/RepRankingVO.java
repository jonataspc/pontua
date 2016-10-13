package vo;

import java.io.Serializable;
import java.math.BigDecimal;

public class RepRankingVO implements Serializable {

    private static final long serialVersionUID = 5L;

    private EntidadeVO entidade;
    private BigDecimal saldoPontuacao;
    private int posicao;
    private BigDecimal pontuacaoMaximaPossivel;

    public BigDecimal getPontuacaoMaximaPossivel() {
        return pontuacaoMaximaPossivel;
    }

    public void setPontuacaoMaximaPossivel(BigDecimal pontuacaoMaximaPossivel) {
        this.pontuacaoMaximaPossivel = pontuacaoMaximaPossivel;
    }

    public EntidadeVO getEntidade() {
        return entidade;
    }

    public void setEntidade(EntidadeVO entidade) {
        this.entidade = entidade;
    }

    public int getPosicao() {
        return posicao;
    }

    public void setPosicao(int posicao) {
        this.posicao = posicao;
    }

    public BigDecimal getSaldoPontuacao() {
        return saldoPontuacao;
    }

    public void setSaldoPontuacao(BigDecimal saldoPontuacao) {
        this.saldoPontuacao = saldoPontuacao;
    }

    @Override
    public String toString() {
        return this.entidade.getNome() + " - " + String.valueOf(this.saldoPontuacao.doubleValue()) ;
    }

}
