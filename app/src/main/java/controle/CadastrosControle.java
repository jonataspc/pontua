package controle;

import android.util.Log;

import java.io.Closeable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import controle.regrasNegocios.RegrasNegocioArea;
import controle.regrasNegocios.RegrasNegocioAvaliacao;
import controle.regrasNegocios.RegrasNegocioEntidade;
import controle.regrasNegocios.RegrasNegocioEvento;
import controle.regrasNegocios.RegrasNegocioItemInspecao;
import controle.regrasNegocios.RegrasNegocioUsuario;
import dao.AreaDAO;
import dao.AvaliacaoDAO;
import dao.EntidadeDAO;
import dao.EventoDAO;
import dao.ItemInspecaoDAO;
import dao.RelEntidadeEventoDAO;
import dao.RelItemInspecaoEventoDAO;
import dao.RepRankingDAO;
import dao.UsuarioDAO;
import utils.Conexao;
import vo.AreaVO;
import vo.AvaliacaoVO;
import vo.EntidadeVO;
import vo.EventoVO;
import vo.ItemInspecaoVO;
import vo.RelEntidadeEventoVO;
import vo.RelItemInspecaoEventoVO;
import vo.UsuarioVO;

public class CadastrosControle implements Closeable {

    private EventoDAO daoEvento;
    private EntidadeDAO daoEntidade;
    private UsuarioDAO daoUsuario;
    private ItemInspecaoDAO daoItemInspecao;
    private AvaliacaoDAO daoAvaliacao;
    private RepRankingDAO daoRepRanking;
    private AreaDAO daoArea;
    private RelEntidadeEventoDAO daoRelEntidadeEvento;
    private RelItemInspecaoEventoDAO daoRelItemInspecaoEvento;

    private Connection conn;

    @Override public void close()  {
        try {
            if(conn!=null && !conn.isClosed()) {
                conn.close();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

//    protected void finalize() throws Throwable {
//        try {
//           this.close();
//
//        } finally {
//            super.finalize();
//        }
//    }

    public CadastrosControle() {


        //abre conexao comum  para todos
        try {
            conn = Conexao.obterConexao();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        daoEvento = new EventoDAO(conn);
        daoEntidade = new EntidadeDAO(conn);
        daoUsuario = new UsuarioDAO(conn);
        daoItemInspecao = new ItemInspecaoDAO(conn);
        daoAvaliacao = new AvaliacaoDAO(conn);
        daoRepRanking = new RepRankingDAO(conn);
        daoArea= new AreaDAO(conn);
        daoRelEntidadeEvento = new RelEntidadeEventoDAO(conn);
        daoRelItemInspecaoEvento = new RelItemInspecaoEventoDAO(conn);
    }

//
//    //relatorio rkg
//    public List<RelRankingVO> listarRelRanking(EventoVO e) throws Exception {
//        return daoRepRanking.relatorioRanking(e);
//    }


    //evento
    public boolean inserirEvento(EventoVO i) throws Exception {
        RegrasNegocioEvento.validarEvento(i, true, conn);
        return daoEvento.incluir(i);
    }

    public List<EventoVO> listarEvento(String nomePesquisa) throws Exception {
        return daoEvento.listar(nomePesquisa);
    }

    public boolean excluirEvento(EventoVO o) throws Exception {
        return daoEvento.excluir(o);
    }

    public EventoVO obterEventoPorId(int a) throws Exception {
        return daoEvento.obterPorCodigo(a);
    }

    public boolean editarEvento(EventoVO o, boolean alterouNomeOriginal) throws Exception {
        RegrasNegocioEvento.validarEvento(o, alterouNomeOriginal, conn);
        return daoEvento.editar(o);
    }


    //entidade
    public boolean inserirEntidade(EntidadeVO i) throws Exception {
        RegrasNegocioEntidade.validarEntidade(i, true, conn);
        return daoEntidade.incluir(i);
    }



    public List<EntidadeVO> listarEntidade(String nomePesquisa) throws Exception {
        return daoEntidade.listar(nomePesquisa);
    }


    public boolean excluirEntidade(EntidadeVO o) throws Exception {
        return daoEntidade.excluir(o);
    }

    public EntidadeVO obterEntidadePorId(int a) throws Exception {
        return daoEntidade.obterPorCodigo(a);
    }

    public boolean editarEntidade(EntidadeVO o) throws Exception {
        RegrasNegocioEntidade.validarEntidade(o, false, conn);
        return daoEntidade.editar(o);
    }

    //usuario
    public void validarInclusaoUsuario(UsuarioVO i) throws Exception {
        RegrasNegocioUsuario.validarUsuario(i, true, conn);
    }

    public boolean inserirUsuario(UsuarioVO i) throws Exception {
        RegrasNegocioUsuario.validarUsuario(i, true, conn);
        return daoUsuario.incluir(i);
    }

    public List<UsuarioVO> listarUsuario(String nomePesquisa) throws Exception {
        return daoUsuario.listar(nomePesquisa);
    }

    public boolean excluirUsuario(UsuarioVO o) throws Exception {
        return daoUsuario.excluir(o);
    }

    public UsuarioVO obterUsuarioPorId(int a) throws Exception {
        return daoUsuario.obterPorCodigo(a);
    }

    public UsuarioVO obterUsuarioPorEntidade(EntidadeVO e) throws Exception {
        return daoUsuario.obterPorEntidade(e);
    }

    public boolean editarUsuario(UsuarioVO o) throws Exception {
        RegrasNegocioUsuario.validarUsuario(o, false, conn);
        return daoUsuario.editar(o);
    }

    public UsuarioVO validarLogin(UsuarioVO o) throws Exception {
        return daoUsuario.validarLogin(o);
    }


    //itemINspecao
    public boolean inserirItemInspecao(ItemInspecaoVO i) throws Exception {
        RegrasNegocioItemInspecao.validarItemInspecao(i, true, conn);
        return daoItemInspecao.incluir(i);
    }

    public List<ItemInspecaoVO> listarItemInspecao(String nomePesquisa) throws Exception {
        return daoItemInspecao.listar(nomePesquisa);
    }

//    public List<ItemInspecaoVO> listarItemInspecaoPendentesPorEventoEntidadeArea(EventoVO evt, EntidadeVO ent, String area) throws Exception {
//        return daoItemInspecao.listarPendentesPorEventoEntidade(evt, ent, area);
//    }
//
//    public List<ItemInspecaoVO> listarItemInspecaoPorEvento(EventoVO evt, String area) throws Exception {
//        return daoItemInspecao.listarPorEvento(evt, area);
//    }

    public boolean excluirItemInspecao(ItemInspecaoVO o) throws Exception {
        //remove orfas..
        boolean retorno = daoItemInspecao.excluir(o);
        daoArea.removerAreasOrfas();

        return retorno;

    }

    public ItemInspecaoVO obterItemInspecaoPorId(int a) throws Exception {
        return daoItemInspecao.obterPorCodigo(a);
    }

    public boolean editarItemInspecao(ItemInspecaoVO o) throws Exception {
        RegrasNegocioItemInspecao.validarItemInspecao(o, false, conn);
        return daoItemInspecao.editar(o);
    }

    public List<AreaVO> listarAreas() throws Exception {
            return daoArea.listar();
    }

    public AreaVO obterAreaPorId(int a) throws Exception {
        return daoArea.obterPorId(a);
    }

    public AreaVO obterAreaPorNome(String a) throws Exception {
        return daoArea.obterPorNome(a);
    }

    public boolean incluirArea(AreaVO a) throws Exception {
        RegrasNegocioArea.validarArea(a, true, conn);
        return daoArea.incluir(a);
    }


    //RelEntidadeEvento
    public boolean incluirRelEntidadeEvento(RelEntidadeEventoVO a) throws Exception {
        return daoRelEntidadeEvento.incluir(a);
    }

    public boolean excluirRelEntidadeEvento(RelEntidadeEventoVO o) throws Exception {
        return daoRelEntidadeEvento.excluir(o);
    }

    public List<RelEntidadeEventoVO> listarRelEntidadeEventoPorEvento(EventoVO evento) throws Exception {
        return daoRelEntidadeEvento.listarPorEvento(evento);
    }

    public int obterQtdEntidadesPorEvento(EventoVO evento) throws Exception {
        return daoRelEntidadeEvento.qtdEntidadesPorEvento(evento);
    }

    public List<RelEntidadeEventoVO> listarRelEntidadesPendentesPorEvento(EventoVO evento) throws Exception {
        return daoRelEntidadeEvento.listarEntidadesPendentesPorEvento(evento);
    }



    public RelEntidadeEventoVO obterRelEntidadeEventoPorCodigo(int cod) throws Exception {
        return daoRelEntidadeEvento.obterPorCodigo(cod);
    }

    public boolean existeRelEntidadeEvento(RelEntidadeEventoVO o) throws Exception {
        return daoRelEntidadeEvento.existeItem(o);
    }

    public List<AreaVO> listarAreasPendentesPorRelEntidadeEvento(RelEntidadeEventoVO o) throws Exception {
        return daoRelEntidadeEvento.listarAreasPorRelEntidadeEvento(o, true);
    }

    public List<AreaVO> listarAreasPorRelEntidadeEvento(RelEntidadeEventoVO o) throws Exception {
        return daoRelEntidadeEvento.listarAreasPorRelEntidadeEvento(o, false);
    }

    public List<AreaVO> listarAreasPendentesPorEvento(EventoVO o) throws Exception {
        return daoRelEntidadeEvento.listarAreasPorEvento(o, true);
    }

    public List<AreaVO> listarAreasPorEvento(EventoVO o) throws Exception {
        return daoRelEntidadeEvento.listarAreasPorEvento(o, false);
    }

    public List<ItemInspecaoVO> listarItensPendentesPorRelEntidadeEvento(RelEntidadeEventoVO o, AreaVO a) throws Exception {
        return daoRelEntidadeEvento.listarItensPorRelEntidadeEvento(o, a, true);
    }

    public List<ItemInspecaoVO> listarItensPorRelEntidadeEvento(RelEntidadeEventoVO o, AreaVO a) throws Exception {
        return daoRelEntidadeEvento.listarItensPorRelEntidadeEvento(o, a, false);
    }





    //RelItemInspecaoEvento
    public List<ItemInspecaoVO> listarItensPendentesPorEvento(EventoVO o, AreaVO a) throws Exception {
        return daoRelItemInspecaoEvento.listarItensPorEvento(o, a, true);
    }

    public List<ItemInspecaoVO> listarItensPorEvento(EventoVO o, AreaVO a) throws Exception {
        return daoRelItemInspecaoEvento.listarItensPorEvento(o, a, false);
    }

    public boolean incluirRelItemInspecaoEvento(RelItemInspecaoEventoVO a) throws Exception {
        return daoRelItemInspecaoEvento.incluir(a);
    }

    public boolean excluirRelItemInspecaoEvento(RelItemInspecaoEventoVO o) throws Exception {
        return daoRelItemInspecaoEvento.excluir(o);
    }

    public List<RelItemInspecaoEventoVO> listarRelItemInspecaoEventoPorEvento(EventoVO evento) throws Exception {
        return daoRelItemInspecaoEvento.listarPorEvento(evento);
    }

    public int obterQtdItensPorEvento(EventoVO evento) throws Exception {
        return daoRelItemInspecaoEvento.qtdItensPorEvento(evento);
    }

    public RelItemInspecaoEventoVO obterRelItemInspecaoEventoPorCodigo(int cod) throws Exception {
        return daoRelItemInspecaoEvento.obterPorCodigo(cod);
    }

    public boolean existeRelItemInspecaoEvento(RelItemInspecaoEventoVO o) throws Exception {
        return daoRelItemInspecaoEvento.existeItem(o);
    }



    //Avaliacao
    public boolean inserirAvaliacao(AvaliacaoVO i) throws Exception {
        RegrasNegocioAvaliacao.validarAvaliacao(i, true);
        return daoAvaliacao.incluir(i);
    }

    public List<AvaliacaoVO> listarAvaliacao() throws Exception {
        return daoAvaliacao.listar();
    }

    public List<AvaliacaoVO> listarAvaliacao(UsuarioVO usuario) throws Exception {
        return daoAvaliacao.listar(usuario);
    }

    public List<AvaliacaoVO> listarAvaliacao(ItemInspecaoVO itemInspecao) throws Exception {
        return daoAvaliacao.listar(itemInspecao);
    }

    public List<AvaliacaoVO> listarAvaliacao(EntidadeVO entidade) throws Exception {
        return daoAvaliacao.listar(entidade);
    }

    public List<AvaliacaoVO> listarAvaliacao(EventoVO evt, EntidadeVO ent) throws Exception {
        return daoAvaliacao.listar(evt, ent);
    }

    public boolean excluirAvaliacao(AvaliacaoVO o) throws Exception {
        return daoAvaliacao.excluir(o);
    }

    public AvaliacaoVO obterAvaliacaoPorId(int a) throws Exception {
        return daoAvaliacao.obterPorCodigo(a);
    }

    public boolean editarAvaliacao(AvaliacaoVO o) throws Exception {
        return daoAvaliacao.editar(o);
    }


    public boolean localizarAvaliacaoRealizada(AvaliacaoVO o) throws Exception {
        return daoAvaliacao.localizarAvaliacaoRealizada(o);
    }


}
