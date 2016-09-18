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
import vo.AreaVO;

/**
 * Created by Jonatas on 17/09/2016.
 */
public class AreaDAO {

    public AreaVO obterPorId(int codigo) throws SQLException {
        try {

            Connection conn = Conexao.obterConexao();

            PreparedStatement st;

            st = conn.prepareStatement("SELECT id, nome FROM area WHERE id=? ORDER BY nome;");
            st.setInt(1, codigo);
            ResultSet resultado = st.executeQuery();


            AreaVO o = new AreaVO();
            while (resultado.next()) {
                o.setId(resultado.getInt("id"));
                o.setNome(resultado.getString("nome"));
            }

            conn.close();
            return o;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }


    }


    public AreaVO obterPorNome(String nome) throws SQLException {
        try {

            Connection conn = Conexao.obterConexao();

            PreparedStatement st;

            st = conn.prepareStatement("SELECT id, nome FROM area WHERE nome=? ORDER BY nome;");
            st.setString(1, nome);
            ResultSet resultado = st.executeQuery();


            AreaVO o = new AreaVO();
            while (resultado.next()) {
                o.setId(resultado.getInt("id"));
                o.setNome(resultado.getString("nome"));
            }

            conn.close();
            return o;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }


    }

    public void removerAreasOrfas()throws SQLException {

        try {

            Connection conn = Conexao.obterConexao();

            PreparedStatement st;

            st = conn.prepareStatement("DELETE `area` FROM `area` LEFT JOIN item_inspecao ON item_inspecao.id_area = area.id WHERE item_inspecao.id IS NULL;");
            st.executeUpdate();

            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

    }

    public List<AreaVO> listar() throws SQLException {
        try {

            Connection conn = Conexao.obterConexao();

            PreparedStatement st;

            st = conn.prepareStatement("SELECT id, nome FROM area ORDER BY nome;");

            ResultSet resultado = st.executeQuery();

            List<AreaVO> lista = new ArrayList<AreaVO>(0);

            while (resultado.next()) {
                AreaVO o = new AreaVO();
                o.setId(resultado.getInt("id"));
                o.setNome(resultado.getString("nome"));
                lista.add(o);
            }

            conn.close();
            return lista;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }


    }



    public boolean incluir(AreaVO c) throws SQLException {
        try {

            Connection conn;
            conn = Conexao.obterConexao();

            PreparedStatement st = conn.prepareStatement("INSERT INTO area (nome) VALUES (?) ;", Statement.RETURN_GENERATED_KEYS);
            st.setString(1, c.getNome().toUpperCase());
            st.executeUpdate();

            ResultSet rs = st.getGeneratedKeys();
            if (rs.next()){
                c.setId(rs.getInt(1));
            }

            conn.close();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

    }


    public boolean editar(AreaVO c) throws SQLException {
        try {

            Connection conn;
            conn = Conexao.obterConexao();

            PreparedStatement st = conn.prepareStatement("UPDATE area SET nome=? WHERE id=?");

            st.setString(1, c.getNome());
            st.setInt(2, c.getId());

            st.executeUpdate();
            conn.close();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }


    public boolean excluir(AreaVO c) throws SQLException {
        try {

            Connection conn;
            conn = Conexao.obterConexao();

            PreparedStatement st = conn.prepareStatement("DELETE FROM area WHERE id=?");

            st.setInt(1, c.getId());

            st.executeUpdate();
            conn.close();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

}
