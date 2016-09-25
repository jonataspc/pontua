package dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import utils.Conexao;
import vo.AvaliacaoVO;
import vo.EntidadeVO;
import vo.EventoVO;
import vo.ItemInspecaoVO;
import vo.UsuarioVO;

public class AvaliacaoDAO {


    public AvaliacaoVO obterPorCodigo(int codigo) {

        try {
            Connection conn = Conexao.obterConexao();

            PreparedStatement st = conn.prepareStatement("SELECT * FROM Avaliacao WHERE id=?");
            st.setInt(1, codigo);

            ResultSet resultado = st.executeQuery();

            AvaliacaoVO o = new AvaliacaoVO();

            EventoDAO eventoDAO = new EventoDAO();
            EntidadeDAO entidadeDAO = new EntidadeDAO();
            ItemInspecaoDAO itemInspecaoDAO = new ItemInspecaoDAO();
            UsuarioDAO usuarioDAO = new UsuarioDAO();


            while (resultado.next()) {
                o.setId(resultado.getInt("id"));
                o.setDataHora(resultado.getDate("data_hora"));
                o.setEntidade(entidadeDAO.obterPorCodigo(resultado.getInt("id_entidade")));
                o.setItemInspecao(itemInspecaoDAO.obterPorCodigo(resultado.getInt("id_item_inspecao")));
                o.setUsuario(usuarioDAO.obterPorCodigo(resultado.getInt("id_usuario")));
                o.setPontuacao(new BigDecimal(resultado.getDouble("pontuacao")));
                o.setForma_automatica(resultado.getInt("forma_automatica"));
            }

            conn.close();
            return o;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<AvaliacaoVO> listar( UsuarioVO usuario) {
        try {

            Connection conn = Conexao.obterConexao();

            PreparedStatement st;
            st = conn.prepareStatement("SELECT * FROM Avaliacao WHERE id_usuario=?");
            st.setInt(1, usuario.getId());

            ResultSet resultado = st.executeQuery();

            EventoDAO eventoDAO = new EventoDAO();
            EntidadeDAO entidadeDAO = new EntidadeDAO();
            ItemInspecaoDAO itemInspecaoDAO = new ItemInspecaoDAO();
            UsuarioDAO usuarioDAO = new UsuarioDAO();

            List<AvaliacaoVO> lista = new ArrayList<AvaliacaoVO>(0);
            while (resultado.next()) {

                AvaliacaoVO o = new AvaliacaoVO();
                o.setId(resultado.getInt("id"));
                o.setDataHora(resultado.getDate("data_hora"));
                o.setEntidade(entidadeDAO.obterPorCodigo(resultado.getInt("id_entidade")));
                o.setItemInspecao(itemInspecaoDAO.obterPorCodigo(resultado.getInt("id_item_inspecao")));
                o.setUsuario(usuarioDAO.obterPorCodigo(resultado.getInt("id_usuario")));
                o.setPontuacao(new BigDecimal(resultado.getDouble("pontuacao")));
                o.setForma_automatica(resultado.getInt("forma_automatica"));

                lista.add(o);

            }

            conn.close();
            return lista;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }


    }

    public List<AvaliacaoVO> listar( ItemInspecaoVO itemInspecao) {
        try {

            Connection conn = Conexao.obterConexao();

            PreparedStatement st;
            st = conn.prepareStatement("SELECT * FROM Avaliacao WHERE id_item_inspecao=?");
            st.setInt(1, itemInspecao.getId());

            ResultSet resultado = st.executeQuery();

            EventoDAO eventoDAO = new EventoDAO();
            EntidadeDAO entidadeDAO = new EntidadeDAO();
            ItemInspecaoDAO itemInspecaoDAO = new ItemInspecaoDAO();
            UsuarioDAO usuarioDAO = new UsuarioDAO();

            List<AvaliacaoVO> lista = new ArrayList<AvaliacaoVO>(0);
            while (resultado.next()) {

                AvaliacaoVO o = new AvaliacaoVO();
                o.setId(resultado.getInt("id"));
                o.setDataHora(resultado.getDate("data_hora"));
                o.setEntidade(entidadeDAO.obterPorCodigo(resultado.getInt("id_entidade")));
                o.setItemInspecao(itemInspecaoDAO.obterPorCodigo(resultado.getInt("id_item_inspecao")));
                o.setUsuario(usuarioDAO.obterPorCodigo(resultado.getInt("id_usuario")));
                o.setPontuacao(new BigDecimal(resultado.getDouble("pontuacao")));
                o.setForma_automatica(resultado.getInt("forma_automatica"));

                lista.add(o);

            }

            conn.close();
            return lista;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }


    }

    public List<AvaliacaoVO> listar( EntidadeVO entidade) {
        try {

            Connection conn = Conexao.obterConexao();

            PreparedStatement st;
            st = conn.prepareStatement("SELECT * FROM Avaliacao WHERE id_entidade=?");
            st.setInt(1, entidade.getId());

            ResultSet resultado = st.executeQuery();

            EventoDAO eventoDAO = new EventoDAO();
            EntidadeDAO entidadeDAO = new EntidadeDAO();
            ItemInspecaoDAO itemInspecaoDAO = new ItemInspecaoDAO();
            UsuarioDAO usuarioDAO = new UsuarioDAO();

            List<AvaliacaoVO> lista = new ArrayList<AvaliacaoVO>(0);
            while (resultado.next()) {

                AvaliacaoVO o = new AvaliacaoVO();
                o.setId(resultado.getInt("id"));
                o.setDataHora(resultado.getDate("data_hora"));
                o.setEntidade(entidadeDAO.obterPorCodigo(resultado.getInt("id_entidade")));
                o.setItemInspecao(itemInspecaoDAO.obterPorCodigo(resultado.getInt("id_item_inspecao")));
                o.setUsuario(usuarioDAO.obterPorCodigo(resultado.getInt("id_usuario")));
                o.setPontuacao(new BigDecimal(resultado.getDouble("pontuacao")));
                o.setForma_automatica(resultado.getInt("forma_automatica"));

                lista.add(o);

            }

            conn.close();
            return lista;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }


    }



    public List<AvaliacaoVO> listar( EventoVO evt, EntidadeVO ent) {
        try {

            Connection conn = Conexao.obterConexao();

            PreparedStatement st;

            if(ent==null){
                // todas as entidades
                st = conn.prepareStatement("SELECT * FROM avaliacao JOIN item_inspecao ON item_inspecao.id=avaliacao.id_item_inspecao WHERE id_evento=?                   ORDER BY id_evento, area, item_inspecao.nome, id_entidade;");
                st.setInt(1, evt.getId());
            }else{
                st = conn.prepareStatement("SELECT * FROM avaliacao JOIN item_inspecao ON item_inspecao.id=avaliacao.id_item_inspecao WHERE id_evento=? AND id_entidade=? ORDER BY id_evento, area, item_inspecao.nome, id_entidade;");
                st.setInt(1, evt.getId());
                st.setInt(2, ent.getId());
            }


            ResultSet resultado = st.executeQuery();

            EventoDAO eventoDAO = new EventoDAO();
            EntidadeDAO entidadeDAO = new EntidadeDAO();
            ItemInspecaoDAO itemInspecaoDAO = new ItemInspecaoDAO();
            UsuarioDAO usuarioDAO = new UsuarioDAO();

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            List<AvaliacaoVO> lista = new ArrayList<AvaliacaoVO>(0);
            while (resultado.next()) {

                AvaliacaoVO o = new AvaliacaoVO();
                o.setId(resultado.getInt("id"));

                try {
                    o.setDataHora(  new java.sql.Date(format.parse(resultado.getString("data_hora")).getTime())  );
                }catch (Exception e){
                    o.setDataHora(null);
                }

                o.setEntidade(entidadeDAO.obterPorCodigo(resultado.getInt("id_entidade")));
                o.setItemInspecao(itemInspecaoDAO.obterPorCodigo(resultado.getInt("id_item_inspecao")));
                o.setUsuario(usuarioDAO.obterPorCodigo(resultado.getInt("id_usuario")));
                o.setPontuacao(new BigDecimal(resultado.getDouble("pontuacao")));
                o.setForma_automatica(resultado.getInt("forma_automatica"));

                lista.add(o);

            }

            conn.close();
            return lista;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }


    }

    public List<AvaliacaoVO> listar() {
        try {

            Connection conn = Conexao.obterConexao();

            PreparedStatement st;
            st = conn.prepareStatement("SELECT * FROM Avaliacao;");

            /*if (nomePesquisa.trim() == "") {
                st = conn.prepareStatement("SELECT * FROM Avaliacao ORDER BY nome;");
            } else {
                st = conn.prepareStatement("SELECT * FROM Avaliacao WHERE nome LIKE ? ORDER BY nome;");
                st.setString(1, "%" + nomePesquisa.trim() + "%");
            }
*/
            ResultSet resultado = st.executeQuery();

            EventoDAO eventoDAO = new EventoDAO();
            EntidadeDAO entidadeDAO = new EntidadeDAO();
            ItemInspecaoDAO itemInspecaoDAO = new ItemInspecaoDAO();
            UsuarioDAO usuarioDAO = new UsuarioDAO();

            List<AvaliacaoVO> lista = new ArrayList<AvaliacaoVO>(0);
            while (resultado.next()) {

                AvaliacaoVO o = new AvaliacaoVO();
                o.setId(resultado.getInt("id"));
                o.setDataHora(resultado.getDate("data_hora"));
                o.setEntidade(entidadeDAO.obterPorCodigo(resultado.getInt("id_entidade")));
                o.setItemInspecao(itemInspecaoDAO.obterPorCodigo(resultado.getInt("id_item_inspecao")));
                o.setUsuario(usuarioDAO.obterPorCodigo(resultado.getInt("id_usuario")));
                o.setPontuacao(new BigDecimal(resultado.getDouble("pontuacao")));
                o.setForma_automatica(resultado.getInt("forma_automatica"));

                lista.add(o);

            }

            conn.close();
            return lista;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }


    }



    public boolean incluir(AvaliacaoVO c) {
        try {

            Connection conn;
            conn = Conexao.obterConexao();

            PreparedStatement st = conn.prepareStatement("INSERT INTO Avaliacao (id_entidade, id_item_inspecao, id_usuario, pontuacao, forma_automatica) VALUES (?, ?, ?, ?, ?) ;", Statement.RETURN_GENERATED_KEYS);

            st.setInt(1, c.getEntidade().getId());
            st.setInt(2, c.getItemInspecao().getId());
            st.setInt(3, c.getUsuario().getId());
            st.setDouble(4, c.getPontuacao().doubleValue());
            st.setInt(5, c.getForma_automatica());
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


    public boolean editar(AvaliacaoVO c) {
        try {

            Connection conn;
            conn = Conexao.obterConexao();

            PreparedStatement st = conn.prepareStatement("UPDATE Avaliacao SET id_entidade=?, id_item_inspecao=?, id_usuario=?, pontuacao=?, forma_automatica=? WHERE id=?");

            st.setInt(1, c.getEntidade().getId());
            st.setInt(2, c.getItemInspecao().getId());
            st.setInt(3, c.getUsuario().getId());
            st.setDouble(4, c.getPontuacao().doubleValue());
            st.setInt(5, c.getForma_automatica());
            st.setInt(6, c.getId());

            st.executeUpdate();
            conn.close();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean excluir(AvaliacaoVO c) {
        try {

            Connection conn;
            conn = Conexao.obterConexao();

            PreparedStatement st = conn.prepareStatement("DELETE FROM Avaliacao WHERE id=?");

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
