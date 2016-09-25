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


public class RepRankingDAO {

//
//    public List<RelRankingVO> relatorioRanking( EventoVO evt) {
//        try {
//
//            Connection conn = Conexao.obterConexao();
//
//            PreparedStatement st;
//
//
//            st = conn.prepareStatement("SELECT id_entidade, SUM(pontuacao) AS saldoPontos FROM avaliacao JOIN item_inspecao ON item_inspecao.id=avaliacao.id_item_inspecao WHERE id_evento=? GROUP BY id_entidade ORDER BY saldoPontos DESC;");
//            st.setInt(1, evt.getId());
//
//
//            ResultSet resultado = st.executeQuery();
//
//            EntidadeDAO entidadeDAO = new EntidadeDAO();
//
//            List<RelRankingVO> lista = new ArrayList<RelRankingVO>(0);
//
//            int rkg = 1;
//            while (resultado.next()) {
//
//                RelRankingVO o = new RelRankingVO();
//                o.setEntidade(entidadeDAO.obterPorCodigo(resultado.getInt("id_entidade")));
//                o.setSaldoPontuacao(new BigDecimal(resultado.getDouble("saldoPontos")));
//                o.setPosicao(rkg);
//                lista.add(o);
//
//                rkg++;
//
//            }
//
//            conn.close();
//            return lista;
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return null;
//        }
//
//
//    }

}
