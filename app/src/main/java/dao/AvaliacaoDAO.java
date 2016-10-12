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
import vo.AreaVO;
import vo.AvaliacaoVO;
import vo.EntidadeVO;
import vo.EventoVO;
import vo.ItemInspecaoVO;
import vo.RelItemInspecaoEventoVO;
import vo.UsuarioVO;

public class AvaliacaoDAO {

    private Connection conn;

    public AvaliacaoDAO(Connection conn){
        this.conn = conn;
    }

    public boolean localizarAvaliacaoRealizada(AvaliacaoVO o) throws SQLException {
        //retorna o ID de uma avaliacao, consultando pelos RELs

        completarAvaliacaoVO(o);

        boolean retorno = false;

        try {
            Conexao.validarConn(conn);

            PreparedStatement st = conn.prepareStatement("SELECT avaliacao.id FROM avaliacao WHERE id_rel_entidade_evento=? AND id_rel_item_inspecao_evento=? ");
            st.setInt(1, o.getRelEntidadeEvento().getId());
            st.setInt(2, o.getRelItemInspecaoEvento().getId());

            ResultSet resultado = st.executeQuery();


            while (resultado.next()) {
                //define o ID retornado no objeto enviado como parametro
                o.setId(resultado.getInt("id"));
                retorno = true;
            }


            return retorno;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public AvaliacaoVO obterPorCodigo(int codigo) throws SQLException {

        try {
            Conexao.validarConn(conn);

            PreparedStatement st = conn.prepareStatement("SELECT * FROM avaliacao WHERE id=?");
            st.setInt(1, codigo);

            ResultSet resultado = st.executeQuery();

            AvaliacaoVO o = new AvaliacaoVO();

            UsuarioDAO usuarioDAO = new UsuarioDAO(conn);

            RelEntidadeEventoDAO oRelEntidadeEventoDAO = new RelEntidadeEventoDAO(conn);
            RelItemInspecaoEventoDAO oRelItemInspecaoEventoDAO = new RelItemInspecaoEventoDAO(conn);

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


            return o;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public List<AvaliacaoVO> listar( UsuarioVO usuario) throws SQLException {
        try {

            Conexao.validarConn(conn);

            PreparedStatement st;
            st = conn.prepareStatement("SELECT * FROM avaliacao WHERE id_usuario=?");
            st.setInt(1, usuario.getId());

            ResultSet resultado = st.executeQuery();

            UsuarioDAO usuarioDAO = new UsuarioDAO(conn);

            RelEntidadeEventoDAO oRelEntidadeEventoDAO = new RelEntidadeEventoDAO(conn);
            RelItemInspecaoEventoDAO oRelItemInspecaoEventoDAO = new RelItemInspecaoEventoDAO(conn);

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


            return lista;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }


    }

    public List<AvaliacaoVO> listar( ItemInspecaoVO itemInspecao) throws SQLException {
        try {

            Conexao.validarConn(conn);

            PreparedStatement st;
            st = conn.prepareStatement("SELECT * FROM avaliacao WHERE id_item_inspecao=?");
            st.setInt(1, itemInspecao.getId());

            ResultSet resultado = st.executeQuery();

            UsuarioDAO usuarioDAO = new UsuarioDAO(conn);

            RelEntidadeEventoDAO oRelEntidadeEventoDAO = new RelEntidadeEventoDAO(conn);
            RelItemInspecaoEventoDAO oRelItemInspecaoEventoDAO = new RelItemInspecaoEventoDAO(conn);


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


            return lista;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }


    }

    public List<AvaliacaoVO> listar( EntidadeVO entidade) throws SQLException {
        try {

            Conexao.validarConn(conn);

            PreparedStatement st;
            st = conn.prepareStatement("SELECT * FROM avaliacao JOIN rel_entidade_evento ON rel_entidade_evento.id=avaliacao.id_rel_entidade_evento WHERE id_entidade=?");
            st.setInt(1, entidade.getId());

            ResultSet resultado = st.executeQuery();

            RelEntidadeEventoDAO oRelEntidadeEventoDAO = new RelEntidadeEventoDAO(conn);
            RelItemInspecaoEventoDAO oRelItemInspecaoEventoDAO = new RelItemInspecaoEventoDAO(conn);

            UsuarioDAO usuarioDAO = new UsuarioDAO(conn);

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

            Conexao.validarConn(conn);

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

            RelEntidadeEventoDAO oRelEntidadeEventoDAO = new RelEntidadeEventoDAO(conn);
            RelItemInspecaoEventoDAO oRelItemInspecaoEventoDAO = new RelItemInspecaoEventoDAO(conn);

            UsuarioDAO usuarioDAO = new UsuarioDAO(conn);

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


            return lista;

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }


    }

    public List<AvaliacaoVO> listar(EventoVO evt, EntidadeVO ent, AreaVO area, ItemInspecaoVO item, UsuarioVO usuario) throws SQLException {
        try {

            Conexao.validarConn(conn);

            PreparedStatement st;

            String strSQL = "SELECT a.* FROM avaliacao a JOIN rel_entidade_evento ree ON ree.id = a.id_rel_entidade_evento JOIN rel_item_inspecao_evento rie ON rie.id = a.id_rel_item_inspecao_evento JOIN item_inspecao ii ON ii.id = rie.id_item_inspecao JOIN `area` ar ON ar.id = ii.id_area JOIN evento e ON e.id = ree.id_evento JOIN usuario u ON u.id = a.id_usuario WHERE 1=1 ";

            //evento
            if(evt!=null && evt.getId() > 0 ){
                strSQL += " AND ree.id_evento = " + evt.getId() + " "  ;
            }

            //entidade
            if(ent!=null && ent.getId() > 0 ){
                strSQL += " AND ree.id_entidade = " + ent.getId() + " "  ;
            }

            //area
            if(area!=null && area.getId() > 0 ){
                strSQL += " AND ii.id_area = " + area.getId() + " "  ;
            }

            //item
            if(item!=null && item.getId() > 0 ){
                strSQL += " AND rie.id_item_inspecao = " + item.getId() + " "  ;
            }

            //usuario
            if(usuario!=null && usuario.getId() > 0 ){
                strSQL += " AND a.id_usuario = " + usuario.getId() + " "  ;
            }


            st = conn.prepareStatement(strSQL);


            ResultSet resultado = st.executeQuery();

            RelEntidadeEventoDAO oRelEntidadeEventoDAO = new RelEntidadeEventoDAO(conn);
            RelItemInspecaoEventoDAO oRelItemInspecaoEventoDAO = new RelItemInspecaoEventoDAO(conn);

            UsuarioDAO usuarioDAO = new UsuarioDAO(conn);

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

            return lista;

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }


    }

    public List<AvaliacaoVO> listar() throws SQLException {
        try {

            Conexao.validarConn(conn);

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

            RelEntidadeEventoDAO oRelEntidadeEventoDAO = new RelEntidadeEventoDAO(conn);
            RelItemInspecaoEventoDAO oRelItemInspecaoEventoDAO = new RelItemInspecaoEventoDAO(conn);

            UsuarioDAO usuarioDAO = new UsuarioDAO(conn);

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


            return lista;

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }


    }

    private void completarAvaliacaoVO(AvaliacaoVO c) throws SQLException {
        Conexao.validarConn(conn);

        //procura os IDs dos rels com base nos objetos

        if(c.getRelEntidadeEvento().getId()==0){
            //define id
            PreparedStatement st = conn.prepareStatement("SELECT id FROM rel_entidade_evento WHERE id_entidade=? AND id_evento=?");
            st.setInt(1, c.getRelEntidadeEvento().getEntidade().getId());
            st.setInt(2, c.getRelEntidadeEvento().getEvento().getId());

            ResultSet resultado = st.executeQuery();

            while (resultado.next()) {
                c.getRelEntidadeEvento().setId(resultado.getInt("id"));
            }

            resultado.close();
            st.close();
        }

        if(c.getRelItemInspecaoEvento().getId()==0){
            //define id
            PreparedStatement st = conn.prepareStatement("SELECT id FROM rel_item_inspecao_evento WHERE id_item_inspecao=? AND id_evento=?");
            st.setInt(1, c.getRelItemInspecaoEvento().getItemInspecao().getId());
            st.setInt(2, c.getRelItemInspecaoEvento().getEvento().getId());

            ResultSet resultado = st.executeQuery();

            while (resultado.next()) {
                c.getRelItemInspecaoEvento().setId(resultado.getInt("id"));
            }

            resultado.close();
            st.close();
        }
    }

    public boolean incluir(AvaliacaoVO c) throws SQLException {
        try {

            completarAvaliacaoVO(c);


            PreparedStatement st = conn.prepareStatement("INSERT INTO avaliacao (id_rel_entidade_evento, id_rel_item_inspecao_evento, id_usuario, pontuacao, metodo, data_hora) VALUES (?, ?, ?, ?, ?, NOW()) ;", Statement.RETURN_GENERATED_KEYS);

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



            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

    }

    public boolean editar(AvaliacaoVO c) throws SQLException {
        try {

            Conexao.validarConn(conn);

            PreparedStatement st = conn.prepareStatement("UPDATE avaliacao SET id_rel_entidade_evento=?, id_rel_item_inspecao_evento=?, id_usuario=?, pontuacao=?, metodo=? WHERE id=?");

            st.setInt(1, c.getRelEntidadeEvento().getId());
            st.setInt(2, c.getRelItemInspecaoEvento().getId());
            st.setInt(3, c.getUsuario().getId());
            st.setDouble(4, c.getPontuacao().doubleValue());
            st.setInt(5, (c.getMetodo() == AvaliacaoVO.EnumMetodoAvaliacao.Manual) ?  0 : 1 );
            st.setInt(6, c.getId());

            st.executeUpdate();

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public boolean excluir(AvaliacaoVO c) throws SQLException {
        try {

            Conexao.validarConn(conn);

            PreparedStatement st = conn.prepareStatement("DELETE FROM avaliacao WHERE id=?");

            st.setInt(1, c.getId());

            st.executeUpdate();

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

}
