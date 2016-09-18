package dao;

import java.math.BigDecimal;
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
import vo.EventoVO;
import vo.ItemInspecaoVO;

public class ItemInspecaoDAO {



    public boolean existeItemInspecao(String nome) throws SQLException {
        try {

            Connection conn = Conexao.obterConexao();
            Boolean localizado = false;
            PreparedStatement st;

            st = conn.prepareStatement("SELECT COUNT(*) AS total FROM item_inspecao WHERE nome=? ;");
            st.setString(1, nome.trim());

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

    public ItemInspecaoVO obterPorCodigo(int codigo) throws SQLException {

        try {
            Connection conn = Conexao.obterConexao();

            PreparedStatement st = conn.prepareStatement("SELECT * FROM item_inspecao WHERE id=?");
            st.setInt(1, codigo);

            ResultSet resultado = st.executeQuery();

            ItemInspecaoVO o = new ItemInspecaoVO();

            AreaDAO areaDAO = new AreaDAO();

            while (resultado.next()) {
                o.setId(resultado.getInt("id"));
                o.setArea(areaDAO.obterPorId(resultado.getInt("id_area")));
                o.setNome(resultado.getString("nome"));
                o.setPontuacaoMinima(new BigDecimal(resultado.getDouble("pontuacao_minima")));
                o.setPontuacaoMaxima(new BigDecimal(resultado.getDouble("pontuacao_maxima")));
            }

            conn.close();
            return o;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

    }

    public List<ItemInspecaoVO> listar(String nomePesquisa) throws SQLException {
        try {

            Connection conn = Conexao.obterConexao();

            PreparedStatement st;

            if (nomePesquisa.trim() == "") {
                st = conn.prepareStatement("SELECT * FROM item_inspecao ORDER BY id_area, nome;");
            } else {
                st = conn.prepareStatement("SELECT * FROM item_inspecao WHERE nome LIKE ? ORDER BY id_area, nome;");
                st.setString(1, "%" + nomePesquisa.trim() + "%");
            }

            ResultSet resultado = st.executeQuery();

            AreaDAO areaDAO = new AreaDAO();

            List<ItemInspecaoVO> lista = new ArrayList<ItemInspecaoVO>(0);
            while (resultado.next()) {

                ItemInspecaoVO o = new ItemInspecaoVO();
                o.setId(resultado.getInt("id"));
                o.setArea(areaDAO.obterPorId(resultado.getInt("id_area")));
                o.setNome(resultado.getString("nome"));
                o.setPontuacaoMinima(new BigDecimal(resultado.getDouble("pontuacao_minima")));
                o.setPontuacaoMaxima(new BigDecimal(resultado.getDouble("pontuacao_maxima")));

                lista.add(o);

            }

            conn.close();
            return lista;

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }


    }

    public boolean incluir(ItemInspecaoVO c) throws SQLException {
        try {

            Connection conn;
            conn = Conexao.obterConexao();

            PreparedStatement st = conn.prepareStatement("INSERT INTO item_inspecao (id_area, nome, pontuacao_minima, pontuacao_maxima) VALUES (?, ?, ?, ?) ;", Statement.RETURN_GENERATED_KEYS);

            st.setInt(1, c.getArea().getId());
            st.setString(2, c.getNome().toUpperCase());
            st.setDouble(3, c.getPontuacaoMinima().doubleValue());
            st.setDouble(4, c.getPontuacaoMaxima().doubleValue());
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

    public boolean editar(ItemInspecaoVO c) throws SQLException {
        try {

            Connection conn;
            conn = Conexao.obterConexao();

            PreparedStatement st = conn.prepareStatement("UPDATE item_inspecao SET id_area=?, nome=?, pontuacao_minima=?, pontuacao_maxima =? WHERE id=?");

//            if(c.getArea() != null){
//                st.setString(2, c.getArea());
//            }
//            else
//            {
//                st.setNull(2, Types.VARCHAR);
//            }

            st.setInt(1, c.getArea().getId());
            st.setString(2, c.getNome().toUpperCase());
            st.setDouble(3, c.getPontuacaoMinima().doubleValue());
            st.setDouble(4, c.getPontuacaoMaxima().doubleValue());
            st.setInt(5, c.getId());

            st.executeUpdate();
            conn.close();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public boolean excluir(ItemInspecaoVO c) throws SQLException {
        try {

            Connection conn;
            conn = Conexao.obterConexao();

            PreparedStatement st = conn.prepareStatement("DELETE FROM item_inspecao WHERE id=?");

            st.setInt(1, c.getId());

            st.executeUpdate();
            conn.close();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }








    /*public List<ItemInspecaoVO> listarPendentesPorEventoEntidade(EventoVO evt, EntidadeVO ent, String area) {
        try {

            Connection conn = Conexao.obterConexao();

            PreparedStatement st;

            if ( area==null || area.trim() == "") {
                st = conn.prepareStatement("SELECT * FROM item_inspecao ii " +
                        "LEFT JOIN avaliacao av ON av.id_entidade=" + ent.getId() + " AND av.id_item_inspecao=ii.id " +
                        "  WHERE id_evento=" + evt.getId() + "            AND av.id IS NULL ORDER BY id_evento, area, nome;");
            } else {
                st = conn.prepareStatement("SELECT * FROM item_inspecao ii " +
                        "LEFT JOIN avaliacao av ON av.id_entidade=" + ent.getId() + " AND av.id_item_inspecao=ii.id " +
                        "  WHERE id_evento=" + evt.getId() + " AND area=? AND av.id IS NULL ORDER BY id_evento, area, nome;");
                st.setString(1, area);
            }

            ResultSet resultado = st.executeQuery();

            EventoDAO eventoDAO = new EventoDAO();

            List<ItemInspecaoVO> lista = new ArrayList<ItemInspecaoVO>(0);
            while (resultado.next()) {

                ItemInspecaoVO o = new ItemInspecaoVO();
                o.setId(resultado.getInt("id"));
                o.setEvento(eventoDAO.obterPorCodigo(resultado.getInt("id_evento")));
                o.setArea(resultado.getString("area"));
                o.setNome(resultado.getString("nome"));
                o.setPontuacaoMinima(new BigDecimal(resultado.getDouble("pontuacao_minima")));
                o.setPontuacaoMaxima(new BigDecimal(resultado.getDouble("pontuacao_maxima")));

                lista.add(o);

            }

            conn.close();
            return lista;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }


    }*/


    /*public List<ItemInspecaoVO> listarPorEvento(EventoVO evt, String area) {
        try {

            Connection conn = Conexao.obterConexao();

            PreparedStatement st;

            if ( area==null || area.trim() == "") {
                st = conn.prepareStatement("SELECT * FROM item_inspecao ii " +
                        "  " +
                        "  WHERE id_evento=" + evt.getId() + "           ORDER BY id_evento, area, nome;");
            } else {
                st = conn.prepareStatement("SELECT * FROM item_inspecao ii " +
                        "  " +
                        "  WHERE id_evento=" + evt.getId() + " AND area=? ORDER BY id_evento, area, nome;");
                st.setString(1, area);
            }

            ResultSet resultado = st.executeQuery();

            EventoDAO eventoDAO = new EventoDAO();

            List<ItemInspecaoVO> lista = new ArrayList<ItemInspecaoVO>(0);
            while (resultado.next()) {

                ItemInspecaoVO o = new ItemInspecaoVO();
                o.setId(resultado.getInt("id"));
                o.setEvento(eventoDAO.obterPorCodigo(resultado.getInt("id_evento")));
                o.setArea(resultado.getString("area"));
                o.setNome(resultado.getString("nome"));
                o.setPontuacaoMinima(new BigDecimal(resultado.getDouble("pontuacao_minima")));
                o.setPontuacaoMaxima(new BigDecimal(resultado.getDouble("pontuacao_maxima")));

                lista.add(o);

            }

            conn.close();
            return lista;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }


    }
*/

   /* public List<String> listarAreasPorEvento( EventoVO evt) {
        try {

            Connection conn = Conexao.obterConexao();

            PreparedStatement st;

            st = conn.prepareStatement("SELECT area FROM item_inspecao WHERE id_evento=? AND area <> '' AND NOT area IS NULL GROUP BY area ORDER BY area;");
            st.setInt(1, evt.getId());

            ResultSet resultado = st.executeQuery();

            List<String> lista = new ArrayList<String>(0);
            while (resultado.next()) {
                lista.add(resultado.getString("area"));
            }

            conn.close();
            return lista;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }


    }
*/

}
