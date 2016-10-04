package controle.regrasNegocios;


import android.widget.Toast;

import vo.AvaliacaoVO;

/**
 * Created by Jonatas on 03/10/2016.
 */

public class RegrasNegocioAvaliacao {
    public static void validarAvaliacao(AvaliacaoVO a, Boolean isInclusao) throws Exception {

        //6.5.5. A pontuação lançada deverá ser um valor entre a pontuação mínima e máxima configurada no item a ser avaliado.
        if( Double.parseDouble(a.getPontuacao().toString()) < Double.parseDouble(a.getRelItemInspecaoEvento().getItemInspecao().getPontuacaoMinima().toString())){
            throw new Exception("Pontuação lançada deve ser superior/igual à mínima!");
        }

        if( Double.parseDouble(a.getPontuacao().toString()) > Double.parseDouble(a.getRelItemInspecaoEvento().getItemInspecao().getPontuacaoMaxima().toString())){
            throw new Exception("Pontuação lançada deve ser inferior/igual à máxima!");
        }


    }
}
