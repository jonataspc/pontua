package testes;

import java.math.BigDecimal;
import java.util.List;

import controle.CadastrosControle;
import dao.EventoDAO;
import vo.AvaliacaoVO;
import vo.EntidadeVO;
import vo.EventoVO;
import vo.ItemInspecaoVO;
import vo.UsuarioVO;

/**
 * Created by Jonatas on 15/09/2015.
 */
public class Testes {

///*
//
//   public   String Testar() throws Exception  {
//
//       String retorno="";
//
//            try(CadastrosControle cc = new CadastrosControle()){
//
//
//
//      */
///*
//        //testes de evento
//       EventoVO o = new EventoVO();
//
//       o.setNome("teste novo ");
//       o.setId(2);
//
//    //   return String.valueOf(cc.editarEvento( o ));
//
//       //return String.valueOf(cc.excluirEvento((o)));
//        return String.valueOf(cc.listarEvento("").size());
//        //      return String.valueOf( cc.inserirEvento(o));
//
//*//*
//
//
//*/
///*
//
//       //testes de entidade
//       EntidadeVO o = new EntidadeVO();
//
//       o.setEvento(cc.obterEventoPorId(2));
//       o.setNome("Entidade teste 02");
//
//        retorno += "Inclusao: " +   cc.inserirEntidade(o) + "\n";
//
//       retorno += "Total existentes: " +   cc.listarEntidade("").size() + "\n";
//*//*
//
//
//
//       //testes de usuario
//       UsuarioVO o = new UsuarioVO();
//
//       o.setEntidade(cc.obterEntidadePorId(3));
//       o.setNome("usr03");
//       o.setSenha("blablabla");
//       o.setNivelAcesso("ADM");
//
//       retorno += "Inclusao: " +   cc.inserirUsuario(o) + "\n";
//
//       retorno += "Total existentes: " +   cc.listarUsuario("").size() + "\n";
//
//
//
//*/
///*
//       //testes de item_inspecao
//       ItemInspecaoVO o = new ItemInspecaoVO();
//
//
//
//       o.setEvento(cc.obterEventoPorId(2));
//       o.setArea("Cozinha");
//       o.setNome("Item 01");
//       o.setPontuacaoMinima(new BigDecimal(0));
//       o.setPontuacaoMaxima(new BigDecimal(9.99));
//
//
//       retorno += "Inclusao: " +   cc.inserirItemInspecao(o) + "\n";
//
//       retorno += "Total existentes: " +   cc.listarItemInspecao("").size() + "\n";*//*
//
//
//
//*/
///*
//
//       //testes de avaliacao
//       AvaliacaoVO o = new AvaliacaoVO();
//
//       o.setEntidade(cc.obterEntidadePorId(1));
//       o.setItemInspecao(cc.obterItemInspecaoPorId(1));
//       o.setUsuario(cc.obterUsuarioPorId(1));
//       o.setPontuacao(new BigDecimal(1.71));
//       o.setForma_automatica(0);
//
//       retorno += "Inclusao: " +   cc.inserirAvaliacao(o) + "\n";
//
//       retorno += "Total existentes: " +   cc.listarAvaliacao().size() + "\n";
//*//*
//
//
//
//       return retorno;
//
//
//    }
//*/
}
