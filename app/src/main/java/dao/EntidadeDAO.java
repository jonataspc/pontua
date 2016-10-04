package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import utils.Conexao;
import vo.EntidadeVO;
import vo.EventoVO;

public class EntidadeDAO {

    private Connection conn;

    public EntidadeDAO(Connection conn){
        this.conn = conn;
    }

    public boolean existeEntidade(String nome) throws SQLException {
        try {

            Conexao.validarConn(conn);

            Boolean localizado = false;
            PreparedStatement st;

            st = conn.prepareStatement("SELECT COUNT(*) AS total FROM entidade WHERE nome=? ;");
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



    public EntidadeVO obterPorCodigo(int codigo) throws SQLException {

        try {
            Conexao.validarConn(conn);

            PreparedStatement st = conn.prepareStatement("SELECT * FROM entidade WHERE id=?");
            st.setInt(1, codigo);

            ResultSet resultado = st.executeQuery();

            EntidadeVO o = new EntidadeVO();

            EventoDAO eventoDAO = new EventoDAO(conn);

            boolean acho = false;

            while (resultado.next()) {
                o.setId(resultado.getInt("id"));
                o.setNome(resultado.getString("nome"));
//                o.setEvento(eventoDAO.obterPorCodigo(resultado.getInt("id_evento")));
                acho=true;
            }




            if(acho=false){
                return null;
            }

            return o;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

    }

//    public List<EntidadeVO> listarPorEvento(EventoVO evto) {
//        try {
//
// Conexao.validarConn(conn);
//            PreparedStatement st;
//
//
//            st = conn.prepareStatement("SELECT * FROM entidade WHERE id_evento=? ORDER BY nome;");
//            st.setInt(1,  evto.getId());
//
//
//            ResultSet resultado = st.executeQuery();
//
//            EventoDAO eventoDAO = new EventoDAO();
//
//            List<EntidadeVO> lista = new ArrayList<EntidadeVO>(0);
//            while (resultado.next()) {
//
//                EntidadeVO o = new EntidadeVO();
//                o.setId(resultado.getInt("id"));
//                o.setNome(resultado.getString("Nome"));
////                o.setEvento(eventoDAO.obterPorCodigo(resultado.getInt("id_evento")));
//
//                lista.add(o);
//
//            }
//
//
//
//
//            return lista;
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return null;
//        }
//
//
//    }


    public List<EntidadeVO> listar(String nomePesquisa) throws SQLException {
        try {

            Conexao.validarConn(conn);

            PreparedStatement st;

            if (nomePesquisa.trim() == "") {
                st = conn.prepareStatement("SELECT * FROM entidade ORDER BY nome;");
            } else {
                st = conn.prepareStatement("SELECT * FROM entidade WHERE nome LIKE ? ORDER BY nome;");
                st.setString(1, "%" + nomePesquisa.trim() + "%");
            }

            ResultSet resultado = st.executeQuery();

            List<EntidadeVO> lista = new ArrayList<EntidadeVO>(0);
            while (resultado.next()) {

                EntidadeVO o = new EntidadeVO();
                o.setId(resultado.getInt("id"));
                o.setNome(resultado.getString("Nome"));
                lista.add(o);

            }




            return lista;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }


    }

    public boolean incluir(EntidadeVO c) throws SQLException {
        try {

            Conexao.validarConn(conn);

            PreparedStatement st = conn.prepareStatement("INSERT INTO entidade (nome) VALUES (?) ;", Statement.RETURN_GENERATED_KEYS);

            st.setString(1, c.getNome().toUpperCase());
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

    public boolean editar(EntidadeVO c) throws SQLException {
        try {

            Conexao.validarConn(conn);

            PreparedStatement st = conn.prepareStatement("UPDATE entidade SET nome=? WHERE id=?");

            st.setString(1, c.getNome().toUpperCase());
            st.setInt(2, c.getId());

            st.executeUpdate();

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public boolean excluir(EntidadeVO c) throws SQLException {
        try {

            Conexao.validarConn(conn);

            PreparedStatement st = conn.prepareStatement("DELETE FROM entidade WHERE id=?");

            st.setInt(1, c.getId());

            st.executeUpdate();

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }


}
