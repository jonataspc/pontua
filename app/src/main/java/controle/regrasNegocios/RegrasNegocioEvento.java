package controle.regrasNegocios;

import java.sql.Connection;

import dao.EntidadeDAO;
import dao.EventoDAO;
import vo.EntidadeVO;
import vo.EventoVO;

/**
 * Created by Jonatas on 17/09/2016.
 */
public class RegrasNegocioEvento {

    public static void validarEvento(EventoVO i, Boolean isInclusao, Connection conn) throws Exception {


        //3.5. Regras de negócio
//        3.5.1. [RN01] O nome do evento deve conter no máximo 30 caracteres;
        if (i.getNome().length() > 30) {
            throw new Exception("Nome deve conter no máximo 30 caracteres");
        }



        if (isInclusao) {

            EventoDAO daoEvento;
            daoEvento= new EventoDAO(conn);

//        3.5.3. [RN03] Não é permitido duplicar nomes de eventos;
            if (daoEvento.existeEvento(i.getNome())) {
                throw new Exception("Nome de evento já existente");
            }
        }



    }
}
