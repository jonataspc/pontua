package controle.regrasNegocios;

import java.util.regex.Pattern;

import dao.EntidadeDAO;
import dao.UsuarioDAO;
import vo.EntidadeVO;
import vo.UsuarioVO;

/**
 * Created by Jonatas on 04/09/2016.
 */
public class RegrasNegocioEntidade {

    public static void validarEntidade(EntidadeVO i, Boolean isInclusao) throws Exception {


//        4.5. Regras de negócio
//        4.5.1. [RN01] O campo “Nome da Entidade” deve conter no máximo 30 caracteres;
        if (i.getNome().length() > 30) {
            throw new Exception("Nome de entidade deve conter no máximo 30 caracteres");
        }


        if (isInclusao) {

            EntidadeDAO daoEntidade;
            daoEntidade = new EntidadeDAO();

//        4.5.2. [RN02] Não é permitido duplicar nomes de Entidades;
            if (daoEntidade.existeEntidade(i.getNome())) {
                throw new Exception("Nome de entidade já existente");
            }
        }


//        4.5.3. [RN03] Nomes de usuários não podem conter acentuação nem pontuação;
        //JA validado no UsuarioDAO

//        4.5.4. [RN04] O campo “Senha” deve ter no mínimo cinco caracteres, combinando letras e números;
        //JA validado no UsuarioDAO

//        4.5.5. [RN05] Senhas não poderão conter acentuação nem pontuação;
        //JA validado no UsuarioDAO

//        4.5.6. [RN06] A senha não deve ser mostrada na tela, mostrar somente asteriscos ou bolinhas;
        //JA validado no UsuarioDAO

//        4.5.7. [RN07] Caso não sejam respeitadas as regras [RN01] até [RN06], avisar o usuário via mensagem na tela.
        //OK

//        4.5.8. O conteúdo das comboBox deve estar classificado em ordem crescente;
        //OK

//        4.5.9. As comboBox devem ser apresentadas preenchidas, com o primeiro registro encontrado após a ordenação.
        //OK


    }
}
