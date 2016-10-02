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
import vo.RelItemInspecaoEventoVO;

/**
 * Created by Jonatas on 02/10/2016.
 */
public class RelItemInspecaoEventoDAO {


    public boolean existeItem(RelItemInspecaoEventoVO o) throws SQLException {
        try {

            Connection conn = Conexao.obterConexao();
            Boolean localizado = false;
            PreparedStatement st;

            st = conn.prepareStatement("SELECT COUNT(*) AS total FROM rel_item_inspecao_evento WHERE id_item_inspecao=? AND id_evento=?;");
            st.setInt(1, o.getItemInspecao().getId());
            st.setInt(2, o.getEvento().getId());

            ResultSet resultado = st.executeQuery();

            while (resultado.next()) {
                if(resultado.getInt("total") == 0){
                    localizado=false;
                } else {
                    localizado=true;
                }
            }

            conn.close();
            return localizado;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

    }

    public RelItemInspecaoEventoVO obterPorCodigo(int codigo) throws SQLException {

        try {
            Connection conn = Conexao.obterConexao();

            PreparedStatement st = conn.prepareStatement("SELECT * FROM rel_item_inspecao_evento WHERE id=?");
            st.setInt(1, codigo);

            ResultSet resultado = st.executeQuery();

            RelItemInspecaoEventoVO o = new RelItemInspecaoEventoVO();

            ItemInspecaoDAO ItemInspecaoDAO = new ItemInspecaoDAO();
            EventoDAO eventoDAO = new EventoDAO();

            while (resultado.next()) {
                o.setId(resultado.getInt("id"));
                o.setItemInspecao( ItemInspecaoDAO.obterPorCodigo(resultado.getInt("id_item_inspecao")) );
                o.setEvento( eventoDAO.obterPorCodigo(resultado.getInt("id_evento")) );
            }

            conn.close();
            return o;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

    }

    public List<RelItemInspecaoEventoVO> listarPorEvento(EventoVO evento) throws SQLException {
        try {

            Connection conn = Conexao.obterConexao();

            PreparedStatement st;

            if(evento==null) {
                st = conn.prepareStatement("SELECT * FROM rel_item_inspecao_evento ORDER BY id;");
            } else {
                st = conn.prepareStatement("SELECT * FROM rel_item_inspecao_evento WHERE id_evento=? ORDER BY id;");
                st.setInt(1, evento.getId());
            }

            ResultSet resultado = st.executeQuery();

            ItemInspecaoDAO ItemInspecaoDAO = new ItemInspecaoDAO();
            EventoDAO eventoDAO = new EventoDAO();

            List<RelItemInspecaoEventoVO> lista = new ArrayList<RelItemInspecaoEventoVO>(0);
            while (resultado.next()) {

                RelItemInspecaoEventoVO o = new RelItemInspecaoEventoVO();
                o.setId(resultado.getInt("id"));
                o.setItemInspecao( ItemInspecaoDAO.obterPorCodigo(resultado.getInt("id_item_inspecao")) );
                o.setEvento( eventoDAO.obterPorCodigo(resultado.getInt("id_evento")) );
                lista.add(o);
            }

            conn.close();
            return lista;

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }


    }

    public boolean incluir(RelItemInspecaoEventoVO c) throws SQLException {
        try {

            Connection conn;
            conn = Conexao.obterConexao();

            PreparedStatement st = conn.prepareStatement("INSERT INTO rel_item_inspecao_evento (id_item_inspecao, id_evento) VALUES (?, ?) ;", Statement.RETURN_GENERATED_KEYS);

            st.setInt(1, c.getItemInspecao().getId());
            st.setInt(2, c.getEvento().getId());
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

    public boolean excluir(RelItemInspecaoEventoVO c) throws SQLException {
        try {

            Connection conn;
            conn = Conexao.obterConexao();

            PreparedStatement st = conn.prepareStatement("DELETE FROM rel_item_inspecao_evento WHERE id=?");

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
