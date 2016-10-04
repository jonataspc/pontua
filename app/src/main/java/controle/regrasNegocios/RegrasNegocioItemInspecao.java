package controle.regrasNegocios;

import java.math.BigDecimal;
import java.sql.Connection;

import dao.EventoDAO;
import dao.ItemInspecaoDAO;
import vo.EventoVO;
import vo.ItemInspecaoVO;

/**
 * Created by Jonatas on 18/09/2016.
 */
public class RegrasNegocioItemInspecao {

    public static void validarItemInspecao(ItemInspecaoVO i, Boolean isInclusao, Connection conn) throws Exception {

//        5.5. Regras de negócio
//        5.5.1. [RN01] Nomes de itens de pontuação devem conter no máximo 30 caracteres;
        if (i.getNome().length() > 30) {
            throw new Exception("Nome deve conter no máximo 30 caracteres");
        }



        if (isInclusao) {

            ItemInspecaoDAO daoItemInspecao;
            daoItemInspecao= new ItemInspecaoDAO(conn);

//        5.5.2. [RN02] Não é permitido duplicar nomes de Itens de pontuação;
            if (daoItemInspecao.existeItemInspecao(i.getNome())) {
                throw new Exception("Nome de item já existente");
            }
        }


//        5.5.3. [RN03] Não é permitido duplicar nomes de Áreas;
        //OK


//        5.5.4. [RN04] A pontuação mínima deve ser maior ou igual a zero;
        if(i.getPontuacaoMinima().doubleValue() < new BigDecimal(0).doubleValue()){
            throw new Exception("A pontuação mínima deve ser maior ou igual a zero");
        }

//        5.5.5. [RN05] A pontuação máxima não deve ultrapassar o valor de “999,99”;
        if(i.getPontuacaoMaxima().doubleValue() > new BigDecimal(999.99).doubleValue()){
            throw new Exception("A pontuação máxima não deve ultrapassar o valor de 999,99");
        }

//        5.5.6. [RN06] Os campos Valor Mínimo e Valor Máximo devem conter cinco dígitos, sendo dois após a virgula;
//        5.5.7. [RN07] Caso não sejam respeitadas as regras [RN01] até [RN06], avisar o usuário via mensagem na tela.
//        5.5.8. O conteúdo das comboBox deve estar classificado em ordem crescente;
//        5.5.9. As comboBox devem ser apresentadas preenchidas, com o primeiro registro encontrado após a ordenação.









    }
}
