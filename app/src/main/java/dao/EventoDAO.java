package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import utils.Conexao;
import vo.EventoVO;
import vo.UsuarioVO;

public class EventoDAO {

    private Connection conn;

    public EventoDAO(Connection conn){
        this.conn = conn;
    }

    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    public boolean existeEvento(String nome) throws SQLException {
        try {

            Conexao.validarConn(conn);

            Boolean localizado = false;
            PreparedStatement st;

            st = conn.prepareStatement("SELECT COUNT(*) AS total FROM evento WHERE nome=? ;");
            st.setString(1, nome.trim());

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

    public EventoVO obterPorCodigo(int codigo) throws SQLException {

        try {

            Conexao.validarConn(conn);

            PreparedStatement st = conn.prepareStatement("SELECT * FROM evento WHERE id=?");
            st.setInt(1, codigo);

            ResultSet resultado = st.executeQuery();

            EventoVO o = new EventoVO();

            while (resultado.next()) {
                o.setId(resultado.getInt("id"));
                o.setNome(resultado.getString("nome"));

                try {
                    o.setDataHoraCriacao(  new java.sql.Date(format.parse(resultado.getString("datahora_criacao")).getTime())  );
                }catch (Exception e){
                    o.setDataHoraCriacao(null);
                }


                int usuarioID = resultado.getInt("usuario");

                if(usuarioID != 0){

                    UsuarioDAO oUsuarioVO = new UsuarioDAO(conn);

                    UsuarioVO usuario = oUsuarioVO.obterPorCodigo(usuarioID);

                    if(usuario!=null){
                        o.setUsuario(usuario);
                    }

                }


            }


            return o;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

    }

    public List<EventoVO> listar(String nomePesquisa) throws SQLException {
        try {

            Conexao.validarConn(conn);

            PreparedStatement st;

            if (nomePesquisa.trim() == "") {
                st = conn.prepareStatement("SELECT * FROM evento ORDER BY nome;");
            } else {
                st = conn.prepareStatement("SELECT * FROM evento WHERE nome LIKE ? ORDER BY nome;");
                st.setString(1, "%" + nomePesquisa.trim() + "%");
            }

            ResultSet resultado = st.executeQuery();

            UsuarioDAO oUsuarioVO = new UsuarioDAO(conn);


            List<EventoVO> lista = new ArrayList<EventoVO>(0);
            while (resultado.next()) {

                EventoVO o = new EventoVO();
                o.setId(resultado.getInt("id"));
                o.setNome(resultado.getString("Nome"));


                try {
                    o.setDataHoraCriacao(  new java.sql.Date(format.parse(resultado.getString("datahora_criacao")).getTime())  );
                }catch (Exception e){
                    o.setDataHoraCriacao(null);
                }

                int usuarioID = resultado.getInt("usuario");

                if(usuarioID != 0){
                    UsuarioVO usuario = oUsuarioVO.obterPorCodigo(usuarioID);

                    if(usuario!=null){
                        o.setUsuario(usuario);
                    }
                }


                lista.add(o);

            }




            return lista;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }


    }


    public boolean incluir(EventoVO c) throws SQLException {
        try {

            Conexao.validarConn(conn);

            PreparedStatement st = conn.prepareStatement("INSERT INTO evento (Nome, datahora_criacao, usuario) VALUES (?,?,?) ;", Statement.RETURN_GENERATED_KEYS);

            st.setString(1, c.getNome().toUpperCase().trim());
            st.setTimestamp(2, new java.sql.Timestamp(c.getDataHoraCriacao().getTime()));
            st.setInt(3, c.getUsuario().getId());

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


    public boolean editar(EventoVO c) throws SQLException {
        try {

            Conexao.validarConn(conn);

            PreparedStatement st = conn.prepareStatement("UPDATE evento SET nome=?, datahora_criacao=?, usuario=? WHERE id=?");

            st.setString(1, c.getNome().toUpperCase().trim());
            st.setTimestamp(2, new java.sql.Timestamp(c.getDataHoraCriacao().getTime()));
            st.setInt(3, c.getUsuario().getId());
            st.setInt(4, c.getId());

            st.executeUpdate();

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }


    public boolean excluir(EventoVO c) throws SQLException {
        try {

            Conexao.validarConn(conn);

            PreparedStatement st = conn.prepareStatement("DELETE FROM evento WHERE id=?");

            st.setInt(1, c.getId());

            st.executeUpdate();

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }


}
