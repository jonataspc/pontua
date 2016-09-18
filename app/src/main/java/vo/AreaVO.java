package vo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by Jonatas on 17/09/2016.
 */
public class AreaVO implements Serializable {

    private static final long serialVersionUID = 412L;

    private int id;
    private String nome;

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

