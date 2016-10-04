package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import utils.Conexao;
import vo.EntidadeVO;
import vo.UsuarioVO;

public class UsuarioDAO {

    private Connection conn;

    public UsuarioDAO(Connection conn){
        this.conn = conn;
    }

    public UsuarioVO obterPorCodigo(int codigo) throws SQLException {

        try {
            Conexao.validarConn(conn);

            PreparedStatement st = conn.prepareStatement("SELECT * FROM usuario WHERE id=?");
            st.setInt(1, codigo);

            ResultSet resultado = st.executeQuery();

            UsuarioVO o = new UsuarioVO();

            EntidadeDAO entidadeDAO = new EntidadeDAO(conn);

            while (resultado.next()) {

                UsuarioVO.EnumNivelAcesso perfil=null;

                switch (resultado.getString("nivel_acesso").toUpperCase().trim()){
                    case "ADM":
                        perfil = UsuarioVO.EnumNivelAcesso.Administrador;
                        break;
                    case "AVAL":
                        perfil = UsuarioVO.EnumNivelAcesso.Avaliador;
                        break;
                    case "ENT":
                        perfil = UsuarioVO.EnumNivelAcesso.Entidade;
                        break;
                }


                o.setId(resultado.getInt("id"));
                o.setNome(resultado.getString("nome"));
                o.setSenha(resultado.getString("senha"));
                o.setNivelAcesso(perfil);
                o.setEntidade(entidadeDAO.obterPorCodigo(resultado.getInt("id_entidade")));

                if(o.getEntidade().getId()==0){
                    o.setEntidade(null);
                }
            }


            return o;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }


    public UsuarioVO obterPorEntidade(EntidadeVO e) throws SQLException {

        try {
            Conexao.validarConn(conn);

            PreparedStatement st = conn.prepareStatement("SELECT * FROM usuario WHERE id_entidade=?");
            st.setInt(1, e.getId());

            ResultSet resultado = st.executeQuery();

            UsuarioVO o = new UsuarioVO();

            EntidadeDAO entidadeDAO = new EntidadeDAO(conn);

            while (resultado.next()) {

                UsuarioVO.EnumNivelAcesso perfil=null;

                switch (resultado.getString("nivel_acesso").toUpperCase().trim()){
                    case "ADM":
                        perfil = UsuarioVO.EnumNivelAcesso.Administrador;
                        break;
                    case "AVAL":
                        perfil = UsuarioVO.EnumNivelAcesso.Avaliador;
                        break;
                    case "ENT":
                        perfil = UsuarioVO.EnumNivelAcesso.Entidade;
                        break;
                }

                o.setId(resultado.getInt("id"));
                o.setNome(resultado.getString("nome"));
                o.setSenha(resultado.getString("senha"));
                o.setNivelAcesso(perfil);
                o.setEntidade(entidadeDAO.obterPorCodigo(resultado.getInt("id_entidade")));
            }


            return o;
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

    public List<UsuarioVO> listar(String nomePesquisa) throws SQLException {
        try {

            Conexao.validarConn(conn);

            PreparedStatement st;

            if (nomePesquisa.trim() == "") {
                st = conn.prepareStatement("SELECT * FROM usuario ORDER BY nome;");
            } else {
                st = conn.prepareStatement("SELECT * FROM usuario WHERE nome LIKE ? ORDER BY nome;");
                st.setString(1, "%" + nomePesquisa.trim() + "%");
            }

            ResultSet resultado = st.executeQuery();

            EntidadeDAO entidadeDAO = new EntidadeDAO(conn);

            List<UsuarioVO> lista = new ArrayList<UsuarioVO>(0);
            while (resultado.next()) {

                UsuarioVO.EnumNivelAcesso perfil=null;

                switch (resultado.getString("nivel_acesso").toUpperCase().trim()){
                    case "ADM":
                        perfil = UsuarioVO.EnumNivelAcesso.Administrador;
                        break;
                    case "AVAL":
                        perfil = UsuarioVO.EnumNivelAcesso.Avaliador;
                        break;
                    case "ENT":
                        perfil = UsuarioVO.EnumNivelAcesso.Entidade;
                        break;
                }

                UsuarioVO o = new UsuarioVO();
                o.setId(resultado.getInt("id"));
                o.setNome(resultado.getString("nome"));
                o.setSenha(resultado.getString("senha"));
                o.setNivelAcesso(perfil);

                if(resultado.getInt("id_entidade") != 0){
                    o.setEntidade(entidadeDAO.obterPorCodigo(resultado.getInt("id_entidade")));
                }


                lista.add(o);

            }




            return lista;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }


    }

    public boolean existeUsuario(String nome) throws SQLException {
        try {

            Conexao.validarConn(conn);

            Boolean localizado = false;
            PreparedStatement st;

            st = conn.prepareStatement("SELECT COUNT(*) AS total FROM usuario WHERE nome=? ;");
            st.setString(1, nome.trim());

            ResultSet resultado = st.executeQuery();

            while (resultado.next()) {
                if(resultado.getInt("total") == 0){
                    localizado=false;
                } else {
                    localizado=true;
                }
            }


            return localizado;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

    }



    public UsuarioVO validarLogin(UsuarioVO c) throws SQLException {

        try {

            Conexao.validarConn(conn);

            PreparedStatement st;

            st = conn.prepareStatement("SELECT * FROM usuario WHERE nome=? AND senha=SHA(?);");
            st.setString(1, c.getNome());
            st.setString(2, c.getSenha());

            ResultSet resultado = st.executeQuery();

            int cod=-1;

            if (resultado.next()) {
                cod = resultado.getInt("id");
            }



            if(cod != -1){
                return obterPorCodigo(cod);
            }else {
                return null;
            }


        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }


    }


    public boolean incluir(UsuarioVO c) throws SQLException {
        try {

            Conexao.validarConn(conn);

            PreparedStatement st = conn.prepareStatement("INSERT INTO usuario (id_entidade, nome, senha, nivel_acesso) VALUES (?, ?, SHA(?), ?) ;", Statement.RETURN_GENERATED_KEYS);

            if(c.getEntidade()==null){
                st.setNull(1, Types.INTEGER);
            }
            else
            {
                st.setInt(1, c.getEntidade().getId());
            }


            String perfil="";

            switch (c.getNivelAcesso()){
                case Administrador:
                    perfil = "ADM";
                    break;
                case Avaliador:
                    perfil = "AVAL";
                    break;
                case Entidade:
                    perfil = "ENT";
                    break;
            }

            st.setString(2, c.getNome().toUpperCase());
            st.setString(3, c.getSenha());
            st.setString(4, perfil);
            st.executeUpdate();

            ResultSet rs = st.getGeneratedKeys();
            if (rs.next()){
                c.setId(rs.getInt(1));
            }



            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

    }


    public boolean editar(UsuarioVO c) throws SQLException {
        try {

            Conexao.validarConn(conn);

            PreparedStatement st = conn.prepareStatement("UPDATE usuario SET id_entidade=?, nome=?, senha=SHA(?), nivel_acesso=? WHERE id=?");

            if(c.getEntidade()==null){
                st.setNull(1, Types.INTEGER);
            }
            else
            {
                st.setInt(1, c.getEntidade().getId());
            }




            String perfil="";

            switch (c.getNivelAcesso()){
                case Administrador:
                    perfil = "ADM";
                    break;
                case Avaliador:
                    perfil = "AVAL";
                    break;
                case Entidade:
                    perfil = "ENT";
                    break;
            }

            st.setString(2, c.getNome().toUpperCase());
            st.setString(3, c.getSenha());
            st.setString(4, perfil);
            st.setInt(5, c.getId());

            st.executeUpdate();

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }


    public boolean excluir(UsuarioVO c) throws SQLException {
        try {

            Conexao.validarConn(conn);

            PreparedStatement st = conn.prepareStatement("DELETE FROM usuario WHERE id=?");

            st.setInt(1, c.getId());

            st.executeUpdate();

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }


}
