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
import vo.RelItemInspecaoEventoVO;
import vo.UsuarioVO;

public class AvaliacaoDAO {


    public AvaliacaoVO obterPorCodigo(int codigo) throws SQLException {

        try {
            Connection conn = Conexao.obterConexao();

            PreparedStatement st = conn.prepareStatement("SELECT * FROM avaliacao WHERE id=?");
            st.setInt(1, codigo);

            ResultSet resultado = st.executeQuery();

            AvaliacaoVO o = new AvaliacaoVO();

            UsuarioDAO usuarioDAO = new UsuarioDAO();

            RelEntidadeEventoDAO oRelEntidadeEventoDAO = new RelEntidadeEventoDAO();
            RelItemInspecaoEventoDAO oRelItemInspecaoEventoDAO = new RelItemInspecaoEventoDAO();

            while (resultado.next()) {
                o.setId(resultado.getInt("id"));
                o.setDataHora(resultado.getDate("data_hora"));
                o.setRelEntidadeEvento(oRelEntidadeEventoDAO.obterPorCodigo(resultado.getInt("id_rel_entidade_evento")));
                o.setRelItemInspecaoEvento(oRelItemInspecaoEventoDAO.obterPorCodigo(resultado.getInt("id_rel_item_inspecao_evento")));
                o.setUsuario(usuarioDAO.obterPorCodigo(resultado.getInt("id_usuario")));
                o.setPontuacao(new BigDecimal(resultado.getDouble("pontuacao")));

                AvaliacaoVO.EnumMetodoAvaliacao metodo = null;

                if(resultado.getInt("metodo") == 0){
                    metodo = AvaliacaoVO.EnumMetodoAvaliacao.Manual;
                } else {
                    metodo = AvaliacaoVO.EnumMetodoAvaliacao.NFC;
                }

                o.setMetodo(metodo);
            }

            conn.close();
            return o;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public List<AvaliacaoVO> listar( UsuarioVO usuario) throws SQLException {
        try {

            Connection conn = Conexao.obterConexao();

            PreparedStatement st;
            st = conn.prepareStatement("SELECT * FROM avaliacao WHERE id_usuario=?");
            st.setInt(1, usuario.getId());

            ResultSet resultado = st.executeQuery();

            UsuarioDAO usuarioDAO = new UsuarioDAO();

            RelEntidadeEventoDAO oRelEntidadeEventoDAO = new RelEntidadeEventoDAO();
            RelItemInspecaoEventoDAO oRelItemInspecaoEventoDAO = new RelItemInspecaoEventoDAO();

            List<AvaliacaoVO> lista = new ArrayList<AvaliacaoVO>(0);
            while (resultado.next()) {

                AvaliacaoVO o = new AvaliacaoVO();
                o.setId(resultado.getInt("id"));
                o.setDataHora(resultado.getDate("data_hora"));
                o.setRelEntidadeEvento(oRelEntidadeEventoDAO.obterPorCodigo(resultado.getInt("id_rel_entidade_evento")));
                o.setRelItemInspecaoEvento(oRelItemInspecaoEventoDAO.obterPorCodigo(resultado.getInt("id_rel_item_inspecao_evento")));
                o.setUsuario(usuarioDAO.obterPorCodigo(resultado.getInt("id_usuario")));
                o.setPontuacao(new BigDecimal(resultado.getDouble("pontuacao")));

                AvaliacaoVO.EnumMetodoAvaliacao metodo = null;

                if(resultado.getInt("metodo") == 0){
                    metodo = AvaliacaoVO.EnumMetodoAvaliacao.Manual;
                } else {
                    metodo = AvaliacaoVO.EnumMetodoAvaliacao.NFC;
                }

                o.setMetodo(metodo);

                lista.add(o);

            }

            conn.close();
            return lista;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }


    }

    public List<AvaliacaoVO> listar( ItemInspecaoVO itemInspecao) throws SQLException {
        try {

            Connection conn = Conexao.obterConexao();

            PreparedStatement st;
            st = conn.prepareStatement("SELECT * FROM avaliacao WHERE id_item_inspecao=?");
            st.setInt(1, itemInspecao.getId());

            ResultSet resultado = st.executeQuery();

            UsuarioDAO usuarioDAO = new UsuarioDAO();

            RelEntidadeEventoDAO oRelEntidadeEventoDAO = new RelEntidadeEventoDAO();
            RelItemInspecaoEventoDAO oRelItemInspecaoEventoDAO = new RelItemInspecaoEventoDAO();


            List<AvaliacaoVO> lista = new ArrayList<AvaliacaoVO>(0);
            while (resultado.next()) {

                AvaliacaoVO o = new AvaliacaoVO();
                o.setId(resultado.getInt("id"));
                o.setDataHora(resultado.getDate("data_hora"));
                o.setRelEntidadeEvento(oRelEntidadeEventoDAO.obterPorCodigo(resultado.getInt("id_rel_entidade_evento")));
                o.setRelItemInspecaoEvento(oRelItemInspecaoEventoDAO.obterPorCodigo(resultado.getInt("id_rel_item_inspecao_evento")));
                o.setUsuario(usuarioDAO.obterPorCodigo(resultado.getInt("id_usuario")));
                o.setPontuacao(new BigDecimal(resultado.getDouble("pontuacao")));
                AvaliacaoVO.EnumMetodoAvaliacao metodo = null;

                if(resultado.getInt("metodo") == 0){
                    metodo = AvaliacaoVO.EnumMetodoAvaliacao.Manual;
                } else {
                    metodo = AvaliacaoVO.EnumMetodoAvaliacao.NFC;
                }

                o.setMetodo(metodo);

                lista.add(o);

            }

            conn.close();
            return lista;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }


    }

    public List<AvaliacaoVO> listar( EntidadeVO entidade) throws SQLException {
        try {

            Connection conn = Conexao.obterConexao();

            PreparedStatement st;
            st = conn.prepareStatement("SELECT * FROM avaliacao JOIN rel_entidade_evento ON rel_entidade_evento.id=avaliacao.id_rel_entidade_evento WHERE id_entidade=?");
            st.setInt(1, entidade.getId());

            ResultSet resultado = st.executeQuery();

            RelEntidadeEventoDAO oRelEntidadeEventoDAO = new RelEntidadeEventoDAO();
            RelItemInspecaoEventoDAO oRelItemInspecaoEventoDAO = new RelItemInspecaoEventoDAO();

            UsuarioDAO usuarioDAO = new UsuarioDAO();

            List<AvaliacaoVO> lista = new ArrayList<AvaliacaoVO>(0);
            while (resultado.next()) {

                AvaliacaoVO o = new AvaliacaoVO();
                o.setId(resultado.getInt("id"));
                o.setDataHora(resultado.getDate("data_hora"));
                o.setRelEntidadeEvento(oRelEntidadeEventoDAO.obterPorCodigo(resultado.getInt("id_rel_entidade_evento")));
                o.setRelItemInspecaoEvento(oRelItemInspecaoEventoDAO.obterPorCodigo(resultado.getInt("id_rel_item_inspecao_evento")));
                o.setUsuario(usuarioDAO.obterPorCodigo(resultado.getInt("id_usuario")));
                o.setPontuacao(new BigDecimal(resultado.getDouble("pontuacao")));

                AvaliacaoVO.EnumMetodoAvaliacao metodo = null;

                if(resultado.getInt("metodo") == 0){
                    metodo = AvaliacaoVO.EnumMetodoAvaliacao.Manual;
                } else {
                    metodo = AvaliacaoVO.EnumMetodoAvaliacao.NFC;
                }

                o.setMetodo(metodo);

                lista.add(o);

            }

            conn.close();
            return lista;

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }


    }

    public List<AvaliacaoVO> listar( EventoVO evt, EntidadeVO ent) throws SQLException {
        try {

            if(evt==null){
                throw new IllegalArgumentException();
            }

            Connection conn = Conexao.obterConexao();

            PreparedStatement st;

            if(ent==null){
                // todas as entidades do eventos
                st = conn.prepareStatement("SELECT * FROM avaliacao JOIN rel_entidade_evento ON rel_entidade_evento.id=avaliacao.id_rel_entidade_evento WHERE id_evento=?;");
                st.setInt(1, evt.getId());
            }else{
                st = conn.prepareStatement("SELECT * FROM avaliacao JOIN rel_entidade_evento ON rel_entidade_evento.id=avaliacao.id_rel_entidade_evento WHERE id_evento=? AND id_entidade=?;");
                st.setInt(1, evt.getId());
                st.setInt(2, ent.getId());
            }



            ResultSet resultado = st.executeQuery();

            RelEntidadeEventoDAO oRelEntidadeEventoDAO = new RelEntidadeEventoDAO();
            RelItemInspecaoEventoDAO oRelItemInspecaoEventoDAO = new RelItemInspecaoEventoDAO();

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

                o.setRelEntidadeEvento(oRelEntidadeEventoDAO.obterPorCodigo(resultado.getInt("id_rel_entidade_evento")));
                o.setRelItemInspecaoEvento(oRelItemInspecaoEventoDAO.obterPorCodigo(resultado.getInt("id_rel_item_inspecao_evento")));
                o.setUsuario(usuarioDAO.obterPorCodigo(resultado.getInt("id_usuario")));
                o.setPontuacao(new BigDecimal(resultado.getDouble("pontuacao")));

                AvaliacaoVO.EnumMetodoAvaliacao metodo = null;

                if(resultado.getInt("metodo") == 0){
                    metodo = AvaliacaoVO.EnumMetodoAvaliacao.Manual;
                } else {
                    metodo = AvaliacaoVO.EnumMetodoAvaliacao.NFC;
                }

                o.setMetodo(metodo);

                lista.add(o);

            }

            conn.close();
            return lista;

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }


    }

    public List<AvaliacaoVO> listar() throws SQLException {
        try {

            Connection conn = Conexao.obterConexao();

            PreparedStatement st;
            st = conn.prepareStatement("SELECT * FROM avaliacao;");

            /*if (nomePesquisa.trim() == "") {
                st = conn.prepareStatement("SELECT * FROM Avaliacao ORDER BY nome;");
            } else {
                st = conn.prepareStatement("SELECT * FROM Avaliacao WHERE nome LIKE ? ORDER BY nome;");
                st.setString(1, "%" + nomePesquisa.trim() + "%");
            }
*/
            ResultSet resultado = st.executeQuery();

            RelEntidadeEventoDAO oRelEntidadeEventoDAO = new RelEntidadeEventoDAO();
            RelItemInspecaoEventoDAO oRelItemInspecaoEventoDAO = new RelItemInspecaoEventoDAO();

            UsuarioDAO usuarioDAO = new UsuarioDAO();

            List<AvaliacaoVO> lista = new ArrayList<AvaliacaoVO>(0);
            while (resultado.next()) {

                AvaliacaoVO o = new AvaliacaoVO();
                o.setId(resultado.getInt("id"));
                o.setDataHora(resultado.getDate("data_hora"));
                o.setRelEntidadeEvento(oRelEntidadeEventoDAO.obterPorCodigo(resultado.getInt("id_rel_entidade_evento")));
                o.setRelItemInspecaoEvento(oRelItemInspecaoEventoDAO.obterPorCodigo(resultado.getInt("id_rel_item_inspecao_evento")));
                o.setUsuario(usuarioDAO.obterPorCodigo(resultado.getInt("id_usuario")));
                o.setPontuacao(new BigDecimal(resultado.getDouble("pontuacao")));

                AvaliacaoVO.EnumMetodoAvaliacao metodo = null;

                if(resultado.getInt("metodo") == 0){
                    metodo = AvaliacaoVO.EnumMetodoAvaliacao.Manual;
                } else {
                    metodo = AvaliacaoVO.EnumMetodoAvaliacao.NFC;
                }

                o.setMetodo(metodo);

                lista.add(o);

            }

            conn.close();
            return lista;

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }


    }

    public boolean incluir(AvaliacaoVO c) throws SQLException {
        try {

            Connection conn;
            conn = Conexao.obterConexao();

            PreparedStatement st = conn.prepareStatement("INSERT INTO avaliacao (id_rel_entidade_evento, id_rel_item_inspecao_evento, id_usuario, pontuacao, metodo, data_hora) VALUES (?, ?, ?, ?, ?, ?, NOW()) ;", Statement.RETURN_GENERATED_KEYS);

            st.setInt(1, c.getRelEntidadeEvento().getId());
            st.setInt(2, c.getRelItemInspecaoEvento().getId());
            st.setInt(3, c.getUsuario().getId());
            st.setDouble(4, c.getPontuacao().doubleValue());
            st.setInt(5, (c.getMetodo() == AvaliacaoVO.EnumMetodoAvaliacao.Manual) ?  0 : 1 );
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

    public boolean editar(AvaliacaoVO c) throws SQLException {
        try {

            Connection conn;
            conn = Conexao.obterConexao();

            PreparedStatement st = conn.prepareStatement("UPDATE avaliacao SET id_rel_entidade_evento=?, id_rel_item_inspecao_evento=?, id_usuario=?, pontuacao=?, metodo=? WHERE id=?");

            st.setInt(1, c.getRelEntidadeEvento().getId());
            st.setInt(2, c.getRelItemInspecaoEvento().getId());
            st.setInt(3, c.getUsuario().getId());
            st.setDouble(4, c.getPontuacao().doubleValue());
            st.setInt(5, (c.getMetodo() == AvaliacaoVO.EnumMetodoAvaliacao.Manual) ?  0 : 1 );
            st.setInt(6, c.getId());

            st.executeUpdate();
            conn.close();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public boolean excluir(AvaliacaoVO c) throws SQLException {
        try {

            Connection conn;
            conn = Conexao.obterConexao();

            PreparedStatement st = conn.prepareStatement("DELETE FROM avaliacao WHERE id=?");

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
