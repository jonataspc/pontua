package controle.regrasNegocios;

import java.sql.Connection;
import java.util.regex.Pattern;

import dao.UsuarioDAO;
import vo.UsuarioVO;

/**
 * Created by Jonatas on 28/08/2016.
 */
public class RegrasNegocioUsuario {



    public static void validarUsuario(UsuarioVO i, Boolean isInclusao, Connection conn) throws Exception {
        //regras de negocios
        //2.5.1. [RN01] O campo “Nome” deve conter entre 5 e 15 caracteres;
        //2.5.3. [RN03] Nomes de usuários não podem conter acentuação nem pontuação;
        if(!Pattern.compile("^[a-zA-Z0-9]{5,15}$").matcher(i.getNome()).find()){
            throw new Exception("Nome de usuário deve conter de 5 a 15 caracteres (sem acentos, pontuação ou espaços)");
        }

        if(isInclusao){

            UsuarioDAO daoUsuario;
            daoUsuario = new UsuarioDAO(conn);

            //2.5.2. [RN02] Não é permitido duplicar nomes de usuários;
            if(daoUsuario.existeUsuario(i.getNome())){
                throw new Exception("Nome de usuário já existente");
            }
        }

        //2.5.4. [RN04] O campo “Senha” deve ter no mínimo cinco caracteres, combinando letras e números;
        if(!Pattern.compile("^.{5,100}$").matcher( i.getSenha()).find() ||
                !Pattern.compile("[a-zA-Z]+").matcher(i.getSenha()).find() ||
                !Pattern.compile("[0-9]+").matcher(i.getSenha()).find()  ){
            throw new Exception("Senha deve conter no mínimo 5 caracteres, combinando letras e números");
        }

        //2.5.5. [RN05] Senhas não poderão conter acentuação nem pontuação;
        if(!Pattern.compile("^[a-zA-Z0-9]+$").matcher(i.getSenha()).find()){
            throw new Exception("Senha não poderá conter acentuação nem pontuação");
        }

        //2.5.12. O nome de usuário não pode conter espaços em branco.
        if(i.getNome().contains(" ")){
            throw new Exception("Nome de usuário não pode conter espaços");
        }

    }


}
