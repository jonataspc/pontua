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


    public ItemInspecaoVO obterPorCodigo(int codigo) {

        try {
            Connection conn = Conexao.obterConexao();

            PreparedStatement st = conn.prepareStatement("SELECT * FROM item_inspecao WHERE id=?");
            st.setInt(1, codigo);

            ResultSet resultado = st.executeQuery();

            ItemInspecaoVO o = new ItemInspecaoVO();

            EventoDAO eventoDAO = new EventoDAO();

            while (resultado.next()) {
                o.setId(resultado.getInt("id"));
                o.setEvento(eventoDAO.obterPorCodigo(resultado.getInt("id_evento")));
                o.setArea(resultado.getString("area"));
                o.setNome(resultado.getString("nome"));
                o.setPontuacaoMinima(new BigDecimal(resultado.getDouble("pontuacao_minima")));
                o.setPontuacaoMaxima(new BigDecimal(resultado.getDouble("pontuacao_maxima")));
            }

            conn.close();
            return o;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<ItemInspecaoVO> listar(String nomePesquisa) {
        try {

            Connection conn = Conexao.obterConexao();

            PreparedStatement st;

            if (nomePesquisa.trim() == "") {
                st = conn.prepareStatement("SELECT * FROM item_inspecao ORDER BY id_evento, area, nome;");
            } else {
                st = conn.prepareStatement("SELECT * FROM item_inspecao WHERE nome LIKE ? ORDER BY id_evento, area, nome;");
                st.setString(1, "%" + nomePesquisa.trim() + "%");
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
            return null;
        }


    }


    public List<ItemInspecaoVO> listarPendentesPorEventoEntidade(EventoVO evt, EntidadeVO ent, String area) {
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
            return null;
        }


    }


    public List<ItemInspecaoVO> listarPorEvento(EventoVO evt, String area) {
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
            return null;
        }


    }


    public List<String> listarAreas() {
        try {

            Connection conn = Conexao.obterConexao();

            PreparedStatement st;

            st = conn.prepareStatement("SELECT area FROM item_inspecao WHERE area <> '' AND NOT area IS NULL GROUP BY area ORDER BY area;");

            ResultSet resultado = st.executeQuery();

            List<String> lista = new ArrayList<String>(0);
            while (resultado.next()) {
                lista.add(resultado.getString("area"));
            }

            conn.close();
            return lista;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }


    }

    public List<String> listarAreasPorEvento( EventoVO evt) {
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
            return null;
        }


    }

    public boolean incluir(ItemInspecaoVO c) {
        try {

            Connection conn;
            conn = Conexao.obterConexao();

            PreparedStatement st = conn.prepareStatement("INSERT INTO item_inspecao (id_evento, area, nome, pontuacao_minima, pontuacao_maxima) VALUES (?, ?, ?, ?, ?) ;", Statement.RETURN_GENERATED_KEYS);

            st.setInt(1, c.getEvento().getId());
            st.setString(2, c.getArea());
            st.setString(3, c.getNome());
            st.setDouble(4, c.getPontuacaoMinima().doubleValue());
            st.setDouble(5, c.getPontuacaoMaxima().doubleValue());
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


    public boolean editar(ItemInspecaoVO c) {
        try {

            Connection conn;
            conn = Conexao.obterConexao();

            PreparedStatement st = conn.prepareStatement("UPDATE item_inspecao SET id_evento=?, area=?, nome=?, pontuacao_minima=?, pontuacao_maxima =? WHERE id=?");

            st.setInt(1, c.getEvento().getId());

            if(c.getArea() != null){
                st.setString(2, c.getArea());
            }
            else
            {
                st.setNull(2, Types.VARCHAR);
            }



            st.setString(3, c.getNome());
            st.setDouble(4, c.getPontuacaoMinima().doubleValue());
            st.setDouble(5, c.getPontuacaoMaxima().doubleValue());
            st.setInt(6, c.getId());

            st.executeUpdate();
            conn.close();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean excluir(ItemInspecaoVO c) {
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
            return false;
        }
    }


}
