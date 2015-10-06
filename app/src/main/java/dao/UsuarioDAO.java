package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import utils.Conexao;
import vo.UsuarioVO;

public class UsuarioDAO {


    public UsuarioVO obterPorCodigo(int codigo) {

        try {
            Connection conn = Conexao.obterConexao();

            PreparedStatement st = conn.prepareStatement("SELECT * FROM usuario WHERE id=?");
            st.setInt(1, codigo);

            ResultSet resultado = st.executeQuery();

            UsuarioVO o = new UsuarioVO();

            EntidadeDAO entidadeDAO = new EntidadeDAO();

            while (resultado.next()) {
                o.setId(resultado.getInt("id"));
                o.setNome(resultado.getString("nome"));
                o.setSenha(resultado.getString("senha"));
                o.setNivelAcesso(resultado.getString("nivel_acesso"));
                o.setEntidade(entidadeDAO.obterPorCodigo(resultado.getInt("id_entidade")));
            }

            conn.close();
            return o;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<UsuarioVO> listar(String nomePesquisa) {
        try {

            Connection conn = Conexao.obterConexao();

            PreparedStatement st;

            if (nomePesquisa.trim() == "") {
                st = conn.prepareStatement("SELECT * FROM Usuario ORDER BY nome;");
            } else {
                st = conn.prepareStatement("SELECT * FROM Usuario WHERE nome LIKE ? ORDER BY nome;");
                st.setString(1, "%" + nomePesquisa.trim() + "%");
            }

            ResultSet resultado = st.executeQuery();

            EntidadeDAO entidadeDAO = new EntidadeDAO();

            List<UsuarioVO> lista = new ArrayList<UsuarioVO>(0);
            while (resultado.next()) {

                UsuarioVO o = new UsuarioVO();
                o.setId(resultado.getInt("id"));
                o.setNome(resultado.getString("nome"));
                o.setSenha(resultado.getString("senha"));
                o.setNivelAcesso(resultado.getString("nivel_acesso"));
                o.setEntidade(entidadeDAO.obterPorCodigo(resultado.getInt("id_entidade")));

                lista.add(o);

            }



            conn.close();
            return lista;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }


    }

    public boolean validarLogin(UsuarioVO c) {
        try {

            Connection conn = Conexao.obterConexao();

            PreparedStatement st;

            st = conn.prepareStatement("SELECT * FROM Usuario WHERE nome=? AND senha=?;");
            st.setString(1, c.getNome());
            st.setString(2, c.getSenha());

            ResultSet resultado = st.executeQuery();

            if (resultado.next()) {
                return true;
            }

            conn.close();
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }


    }


    public boolean incluir(UsuarioVO c) {
        try {

            Connection conn;
            conn = Conexao.obterConexao();

            PreparedStatement st = conn.prepareStatement("INSERT INTO usuario (id_entidade, nome, senha, nivel_acesso) VALUES (?, ?, ?, ?) ;", Statement.RETURN_GENERATED_KEYS);

            st.setInt(1, c.getEntidade().getId());
            st.setString(2, c.getNome());
            st.setString(3, c.getSenha());
            st.setString(4, c.getNivelAcesso());
            st.executeUpdate();

            ResultSet rs = st.getGeneratedKeys();
            if (rs.next()){
                c.setId(rs.getInt(1));
            }


            conn.close();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }


    public boolean editar(UsuarioVO c) {
        try {

            Connection conn;
            conn = Conexao.obterConexao();

            PreparedStatement st = conn.prepareStatement("UPDATE usuario SET id_entidade=?, nome=?, senha=?, nivel_acesso=? WHERE id=?");

            st.setInt(1, c.getEntidade().getId());
            st.setString(2, c.getNome());
            st.setString(3, c.getSenha());
            st.setString(4, c.getNivelAcesso());
            st.setInt(5, c.getId());

            st.executeUpdate();
            conn.close();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean excluir(UsuarioVO c) {
        try {

            Connection conn;
            conn = Conexao.obterConexao();

            PreparedStatement st = conn.prepareStatement("DELETE FROM usuario WHERE id=?");

            st.setInt(1, c.getId());

            st.executeUpdate();
            conn.close();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


}