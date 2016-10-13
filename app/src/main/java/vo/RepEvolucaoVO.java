package vo;

import java.io.Serializable;

/**
 * Created by Jonatas on 12/10/2016.
 */

public class RepEvolucaoVO implements Serializable   {
    private static final long serialVersionUID = 5111L;

    private int totalLancamentos;

    public int getTotalLancamentos() {
        return totalLancamentos;
    }

    public void setTotalLancamentos(int totalLancamentos) {
        this.totalLancamentos = totalLancamentos;
    }

    public int getTotalLancamentosPendentes() {
        return totalLancamentosPendentes;
    }

    public void setTotalLancamentosPendentes(int totalLancamentosPendentes) {
        this.totalLancamentosPendentes = totalLancamentosPendentes;
    }

    private int totalLancamentosPendentes;

    @Override
    public String toString() {
        return String.valueOf(this.totalLancamentosPendentes) + "/" + String.valueOf(this.totalLancamentos) ;
    }

}
