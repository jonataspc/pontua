package controle.regrasNegocios;

import dao.AreaDAO;
import dao.EntidadeDAO;
import vo.AreaVO;
import vo.EntidadeVO;

/**
 * Created by Jonatas on 25/09/2016.
 */
public class RegrasNegocioArea {

    public static void validarArea(AreaVO i, Boolean isInclusao) throws Exception {


        if (i.getNome().length() > 30) {
            throw new Exception("Nome deve conter no máximo 30 caracteres");
        }


        if (isInclusao) {

            AreaDAO daoArea;
            daoArea = new AreaDAO();

            if (daoArea.existeArea(i.getNome())) {
                throw new Exception("Nome de área já existente");
            }
        }


    }
}