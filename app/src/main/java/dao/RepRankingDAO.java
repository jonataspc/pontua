package dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import utils.Conexao;
import vo.AvaliacaoVO;
import vo.EntidadeVO;
import vo.EventoVO;
import vo.RepRankingVO;


public class RepRankingDAO {

    private Connection conn;

    public RepRankingDAO(Connection conn){
        this.conn = conn;
    }


    public List<RepRankingVO> relatorioRanking( EventoVO evt) throws SQLException {
        try {

            Conexao.validarConn(conn);

            PreparedStatement st;


            st = conn.prepareStatement("SELECT id_entidade, SUM(pontuacao) AS saldoPontos, ( SELECT SUM(pontuacao_maxima) FROM item_inspecao ii JOIN rel_item_inspecao_evento riie ON riie.id_item_inspecao = ii.id WHERE riie.id_evento=ree.id_evento) AS pontuacao_maxima FROM avaliacao a JOIN rel_entidade_evento ree ON ree.id = a.id_rel_entidade_evento JOIN evento e ON e.id = ree.id_evento WHERE ree.id_evento = ? GROUP BY id_entidade ORDER BY saldoPontos DESC;");
            st.setInt(1, evt.getId());

            ResultSet resultado = st.executeQuery();

            EntidadeDAO entidadeDAO = new EntidadeDAO(conn);

            List<RepRankingVO> lista = new ArrayList<RepRankingVO>(0);

            int rkg = 1;
            while (resultado.next()) {

                RepRankingVO o = new RepRankingVO();
                o.setEntidade(entidadeDAO.obterPorCodigo(resultado.getInt("id_entidade")));
                o.setSaldoPontuacao(new BigDecimal(resultado.getDouble("saldoPontos")));
                o.setPontuacaoMaximaPossivel(new BigDecimal(resultado.getDouble("pontuacao_maxima")));
                o.setPosicao(rkg);
                lista.add(o);
                rkg++;
            }

            return lista;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }


    }

}
