package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import utils.Conexao;
import vo.EventoVO;

public class EventoDAO {


    public EventoVO obterPorCodigo(int codigo) {

        try {
            Connection conn = Conexao.obterConexao();

            PreparedStatement st = conn.prepareStatement("SELECT * FROM evento WHERE id=?");
            st.setInt(1, codigo);

            ResultSet resultado = st.executeQuery();

            EventoVO o = new EventoVO();

            while (resultado.next()) {
                o.setId(resultado.getInt("id"));
                o.setNome(resultado.getString("nome"));
            }

            conn.close();
            return o;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<EventoVO> listar(String nomePesquisa) {
        try {

            Connection conn = Conexao.obterConexao();

            PreparedStatement st;

            if (nomePesquisa.trim() == "") {
                st = conn.prepareStatement("SELECT * FROM evento ORDER BY nome;");
            } else {
                st = conn.prepareStatement("SELECT * FROM evento WHERE nome LIKE ? ORDER BY nome;");
                st.setString(1, "%" + nomePesquisa.trim() + "%");
            }

            ResultSet resultado = st.executeQuery();

            List<EventoVO> lista = new ArrayList<EventoVO>(0);
            while (resultado.next()) {

                EventoVO o = new EventoVO();
                o.setId(resultado.getInt("id"));
                o.setNome(resultado.getString("Nome"));

                lista.add(o);

            }



            conn.close();
            return lista;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }


    }


    public boolean incluir(EventoVO c) {
        try {

            Connection conn;
            conn = Conexao.obterConexao();

            PreparedStatement st = conn.prepareStatement("INSERT INTO evento (Nome) VALUES (?) ;", Statement.RETURN_GENERATED_KEYS);

            st.setString(1, c.getNome());
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


    public boolean editar(EventoVO c) {
        try {

            Connection conn;
            conn = Conexao.obterConexao();

            PreparedStatement st = conn.prepareStatement("UPDATE evento SET nome=? WHERE id=?");

            st.setString(1, c.getNome());
            st.setInt(2, c.getId());

            st.executeUpdate();
            conn.close();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean excluir(EventoVO c) {
        try {

            Connection conn;
            conn = Conexao.obterConexao();

            PreparedStatement st = conn.prepareStatement("DELETE FROM evento WHERE id=?");

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
