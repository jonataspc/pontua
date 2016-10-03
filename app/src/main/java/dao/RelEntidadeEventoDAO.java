package dao;

import java.math.BigDecimal;
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
import vo.RelEntidadeEventoVO;

/**
 * Created by Jonatas on 25/09/2016.
 */
public class RelEntidadeEventoDAO {


    public boolean existeItem(RelEntidadeEventoVO o) throws SQLException {
        try {

            Connection conn = Conexao.obterConexao();
            Boolean localizado = false;
            PreparedStatement st;

            st = conn.prepareStatement("SELECT COUNT(*) AS total FROM rel_entidade_evento WHERE id_entidade=? AND id_evento=?;");
            st.setInt(1, o.getEntidade().getId());
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

    public RelEntidadeEventoVO obterPorCodigo(int codigo) throws SQLException {

        try {
            Connection conn = Conexao.obterConexao();

            PreparedStatement st = conn.prepareStatement("SELECT * FROM rel_entidade_evento WHERE id=?");
            st.setInt(1, codigo);

            ResultSet resultado = st.executeQuery();

            RelEntidadeEventoVO o = new RelEntidadeEventoVO();

            EntidadeDAO entidadeDAO = new EntidadeDAO();
            EventoDAO eventoDAO = new EventoDAO();

            while (resultado.next()) {
                o.setId(resultado.getInt("id"));
                o.setEntidade( entidadeDAO.obterPorCodigo(resultado.getInt("id_entidade")) );
                o.setEvento( eventoDAO.obterPorCodigo(resultado.getInt("id_evento")) );
            }

            conn.close();
            return o;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

    }

    public List<RelEntidadeEventoVO> listarPorEvento(EventoVO evento) throws SQLException {
        try {

            Connection conn = Conexao.obterConexao();

            PreparedStatement st;

            if(evento==null) {
                st = conn.prepareStatement("SELECT * FROM rel_entidade_evento ORDER BY id;");
            } else {
                st = conn.prepareStatement("SELECT * FROM rel_entidade_evento WHERE id_evento=? ORDER BY id;");
                st.setInt(1, evento.getId());
            }

            ResultSet resultado = st.executeQuery();

            EntidadeDAO entidadeDAO = new EntidadeDAO();
            EventoDAO eventoDAO = new EventoDAO();

            List<RelEntidadeEventoVO> lista = new ArrayList<RelEntidadeEventoVO>(0);
            while (resultado.next()) {

                RelEntidadeEventoVO o = new RelEntidadeEventoVO();
                o.setId(resultado.getInt("id"));
                o.setEntidade( entidadeDAO.obterPorCodigo(resultado.getInt("id_entidade")) );
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

    public List<AreaVO> listarAreasPendentesPorRelEntidadeEvento(RelEntidadeEventoVO o) throws SQLException {
        try {
            //retorna areas pendentes para uma entidade/evento

            Connection conn = Conexao.obterConexao();

            PreparedStatement st;

            String strSQL = "SELECT a.nome, a.id FROM rel_entidade_evento ree " +
                    " " +
                    "JOIN rel_item_inspecao_evento rii USING(id_evento) " +
                    "JOIN item_inspecao ii ON ii.id = rii.id_item_inspecao " +
                    "JOIN `area` a ON a.id = ii.id_area " +
                    "LEFT JOIN avaliacao av ON av.id_rel_entidade_evento = ree.id AND av.id_rel_item_inspecao_evento = rii.id /* join com lancamentos */ " +
                    " " +
                    "WHERE ree.id_entidade=? AND ree.id_evento=? " +
                    "AND av.id IS NULL  /* somente os nao avaliados (pendentes) */ " +
                    "GROUP BY a.id ORDER BY a.nome";


            st = conn.prepareStatement(strSQL);
            st.setInt(1, o.getEntidade().getId());
            st.setInt(2, o.getEvento().getId());


            ResultSet resultado = st.executeQuery();

            List<AreaVO> lista = new ArrayList<AreaVO>(0);
            while (resultado.next()) {
                AreaVO a = new AreaVO();
                a.setNome(resultado.getString("nome"));
                a.setId(resultado.getInt("id"));
                lista.add(a);
            }

            conn.close();
            return lista;

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }


    }

    public List<ItemInspecaoVO> listarItensPendentesPorRelEntidadeEvento(RelEntidadeEventoVO o, AreaVO a) throws SQLException {
        try {
            //retorna itens pendentes para uma entidade/evento

            Connection conn = Conexao.obterConexao();

            PreparedStatement st;

            String strSQL = "SELECT ii.id FROM rel_entidade_evento ree " +
                    " " +
                    "JOIN rel_item_inspecao_evento rii USING(id_evento) " +
                    "JOIN item_inspecao ii ON ii.id = rii.id_item_inspecao " +
                    "JOIN `area` a ON a.id = ii.id_area " +
                    "LEFT JOIN avaliacao av ON av.id_rel_entidade_evento = ree.id AND av.id_rel_item_inspecao_evento = rii.id /* join com lancamentos */ " +
                    " " +
                    "WHERE ree.id_entidade=? AND ree.id_evento=? " +
                    "AND av.id IS NULL  /* somente os nao avaliados (pendentes) */ " +
                    "";

            if(a!=null){
                strSQL += " AND a.id=? ";
            }


            st = conn.prepareStatement(strSQL);
            st.setInt(1, o.getEntidade().getId());
            st.setInt(2, o.getEvento().getId());

            if(a!=null){
                st.setInt(3, a.getId());
            }

            ResultSet resultado = st.executeQuery();

            ItemInspecaoDAO iiDao = new ItemInspecaoDAO();

            List<ItemInspecaoVO> lista = new ArrayList<>(0);
            while (resultado.next()) {
                ItemInspecaoVO i;
                i = iiDao.obterPorCodigo(resultado.getInt("id"));
                lista.add(i);
            }

            conn.close();
            return lista;

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }


    }



    public boolean incluir(RelEntidadeEventoVO c) throws SQLException {
        try {

            Connection conn;
            conn = Conexao.obterConexao();

            PreparedStatement st = conn.prepareStatement("INSERT INTO rel_entidade_evento (id_entidade, id_evento) VALUES (?, ?) ;", Statement.RETURN_GENERATED_KEYS);

            st.setInt(1, c.getEntidade().getId());
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

//    public boolean editar(ItemInspecaoVO c) throws SQLException {
//        try {
//
//            Connection conn;
//            conn = Conexao.obterConexao();
//
//            PreparedStatement st = conn.prepareStatement("UPDATE item_inspecao SET id_area=?, nome=?, pontuacao_minima=?, pontuacao_maxima =? WHERE id=?");
//
////            if(c.getArea() != null){
////                st.setString(2, c.getArea());
////            }
////            else
////            {
////                st.setNull(2, Types.VARCHAR);
////            }
//
//            st.setInt(1, c.getArea().getId());
//            st.setString(2, c.getNome().toUpperCase());
//            st.setDouble(3, c.getPontuacaoMinima().doubleValue());
//            st.setDouble(4, c.getPontuacaoMaxima().doubleValue());
//            st.setInt(5, c.getId());
//
//            st.executeUpdate();
//            conn.close();
//            return true;
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//            throw e;
//        }
//    }

    public boolean excluir(RelEntidadeEventoVO c) throws SQLException {
        try {

            Connection conn;
            conn = Conexao.obterConexao();

            PreparedStatement st = conn.prepareStatement("DELETE FROM rel_entidade_evento WHERE id=?");

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
