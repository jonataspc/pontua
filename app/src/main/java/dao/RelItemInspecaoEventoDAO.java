package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import utils.Conexao;
import vo.AreaVO;
import vo.EventoVO;
import vo.ItemInspecaoVO;
import vo.RelItemInspecaoEventoVO;

/**
 * Created by Jonatas on 02/10/2016.
 */
public class RelItemInspecaoEventoDAO {

    private Connection conn;

    public RelItemInspecaoEventoDAO(Connection conn){
        this.conn = conn;
    }

    public boolean existeItem(RelItemInspecaoEventoVO o) throws SQLException {
        try {

            Conexao.validarConn(conn);
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


            return localizado;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

    }

    public RelItemInspecaoEventoVO obterPorCodigo(int codigo) throws SQLException {

        try {
            Conexao.validarConn(conn);

            PreparedStatement st = conn.prepareStatement("SELECT * FROM rel_item_inspecao_evento WHERE id=?");
            st.setInt(1, codigo);

            ResultSet resultado = st.executeQuery();

            RelItemInspecaoEventoVO o = new RelItemInspecaoEventoVO();

            ItemInspecaoDAO ItemInspecaoDAO = new ItemInspecaoDAO(conn);
            EventoDAO eventoDAO = new EventoDAO(conn);

            while (resultado.next()) {
                o.setId(resultado.getInt("id"));
                o.setItemInspecao( ItemInspecaoDAO.obterPorCodigo(resultado.getInt("id_item_inspecao")) );
                o.setEvento( eventoDAO.obterPorCodigo(resultado.getInt("id_evento")) );
            }


            return o;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

    }

    public int qtdItensPorEvento(EventoVO evento) throws SQLException {
        try {

            Conexao.validarConn(conn);

            PreparedStatement st;
            st = conn.prepareStatement("SELECT COUNT(*) AS qtd FROM rel_item_inspecao_evento WHERE id_evento=? ORDER BY id;");
            st.setInt(1, evento.getId());

            ResultSet resultado = st.executeQuery();

            int result=0;

            while (resultado.next()) {
                result = resultado.getInt("qtd");
            }

            return result;

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

    }

    public List<ItemInspecaoVO> listarItensPorEvento(EventoVO e, AreaVO a, boolean somentePendentes) throws SQLException {
        try {
            //retorna itens pendentes para um evento

            Conexao.validarConn(conn);

            PreparedStatement st;

            String strSQLAux = "";

            if(somentePendentes){
                strSQLAux = " AND av.id IS NULL  /* somente os nao avaliados (pendentes) */ ";
            }


            String strSQL = "SELECT ii.id FROM rel_entidade_evento ree " +
                    " " +
                    "JOIN rel_item_inspecao_evento rii USING(id_evento) " +
                    "JOIN item_inspecao ii ON ii.id = rii.id_item_inspecao " +
                    "JOIN `area` a ON a.id = ii.id_area " +
                    "LEFT JOIN avaliacao av ON av.id_rel_entidade_evento = ree.id AND av.id_rel_item_inspecao_evento = rii.id /* join com lancamentos */ " +
                    " " +
                    "WHERE ree.id_evento=? " + strSQLAux +
                    "" ;


            if(a!=null){
                strSQL += " AND a.id=? ";
            }

            strSQL += " GROUP BY ii.id;";


            st = conn.prepareStatement(strSQL);
            st.setInt(1, e.getId());

            if(a!=null){
                st.setInt(2, a.getId());
            }

            ResultSet resultado = st.executeQuery();

            ItemInspecaoDAO iiDao = new ItemInspecaoDAO(conn);

            List<ItemInspecaoVO> lista = new ArrayList<>(0);
            while (resultado.next()) {
                ItemInspecaoVO i;
                i = iiDao.obterPorCodigo(resultado.getInt("id"));
                lista.add(i);
            }


            return lista;

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw ex;
        }


    }

    public List<RelItemInspecaoEventoVO> listarPorEvento(EventoVO evento) throws SQLException {
        try {

            Conexao.validarConn(conn);

            PreparedStatement st;

            if(evento==null) {
                st = conn.prepareStatement("SELECT * FROM rel_item_inspecao_evento ORDER BY id;");
            } else {
                st = conn.prepareStatement("SELECT * FROM rel_item_inspecao_evento WHERE id_evento=? ORDER BY id;");
                st.setInt(1, evento.getId());
            }

            ResultSet resultado = st.executeQuery();

            ItemInspecaoDAO ItemInspecaoDAO = new ItemInspecaoDAO(conn);
            EventoDAO eventoDAO = new EventoDAO(conn);

            List<RelItemInspecaoEventoVO> lista = new ArrayList<RelItemInspecaoEventoVO>(0);
            while (resultado.next()) {

                RelItemInspecaoEventoVO o = new RelItemInspecaoEventoVO();
                o.setId(resultado.getInt("id"));
                o.setItemInspecao( ItemInspecaoDAO.obterPorCodigo(resultado.getInt("id_item_inspecao")) );
                o.setEvento( eventoDAO.obterPorCodigo(resultado.getInt("id_evento")) );
                lista.add(o);
            }


            return lista;

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }


    }

    public boolean incluir(RelItemInspecaoEventoVO c) throws SQLException {
        try {

            Conexao.validarConn(conn);

            PreparedStatement st = conn.prepareStatement("INSERT INTO rel_item_inspecao_evento (id_item_inspecao, id_evento) VALUES (?, ?) ;", Statement.RETURN_GENERATED_KEYS);

            st.setInt(1, c.getItemInspecao().getId());
            st.setInt(2, c.getEvento().getId());
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

    public boolean excluir(RelItemInspecaoEventoVO c) throws SQLException {
        try {

            Conexao.validarConn(conn);

            PreparedStatement st = conn.prepareStatement("DELETE FROM rel_item_inspecao_evento WHERE id=?");

            st.setInt(1, c.getId());

            st.executeUpdate();

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

}
