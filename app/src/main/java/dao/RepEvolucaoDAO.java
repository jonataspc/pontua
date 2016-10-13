package dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import utils.Conexao;
import vo.EventoVO;
import vo.RepEvolucaoVO;
import vo.RepRankingVO;

/**
 * Created by Jonatas on 12/10/2016.
 */

public class RepEvolucaoDAO {

    private Connection conn;

    public RepEvolucaoDAO(Connection conn){
        this.conn = conn;
    }

    public RepEvolucaoVO consultarEvolucao(EventoVO evt) throws SQLException {
        try {

            Conexao.validarConn(conn);

            PreparedStatement st;


            st = conn.prepareStatement("SELECT ( SELECT COUNT(*) FROM rel_entidade_evento ree JOIN rel_item_inspecao_evento rii USING(id_evento) JOIN item_inspecao ii ON ii.id = rii.id_item_inspecao JOIN `area` a ON a.id = ii.id_area LEFT JOIN avaliacao av ON av.id_rel_entidade_evento = ree.id AND av.id_rel_item_inspecao_evento = rii.id /* join com lancamentos */ WHERE ree.id_evento=? ) AS totalLancamentos, ( SELECT COUNT(*) FROM rel_entidade_evento ree JOIN rel_item_inspecao_evento rii USING(id_evento) JOIN item_inspecao ii ON ii.id = rii.id_item_inspecao JOIN `area` a ON a.id = ii.id_area LEFT JOIN avaliacao av ON av.id_rel_entidade_evento = ree.id AND av.id_rel_item_inspecao_evento = rii.id /* join com lancamentos */ WHERE ree.id_evento=? AND av.id IS NULL ) AS totalLancamentosPendentes  ");
            st.setInt(1, evt.getId());
            st.setInt(2, evt.getId());

            ResultSet resultado = st.executeQuery();

            RepEvolucaoVO o = new RepEvolucaoVO();

            while (resultado.next()) {
                o.setTotalLancamentos(resultado.getInt("totalLancamentos"));
                o.setTotalLancamentosPendentes(resultado.getInt("totalLancamentosPendentes"));
            }
            return o;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }


    }

}
