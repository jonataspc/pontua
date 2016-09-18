package controle;

import java.sql.SQLException;
import java.util.List;
import java.util.regex.Pattern;

import controle.regrasNegocios.RegrasNegocioEntidade;
import controle.regrasNegocios.RegrasNegocioEvento;
import controle.regrasNegocios.RegrasNegocioUsuario;
import dao.AreaDAO;
import dao.AvaliacaoDAO;
import dao.EntidadeDAO;
import dao.EventoDAO;
import dao.ItemInspecaoDAO;
import dao.RelRankingDAO;
import dao.UsuarioDAO;
import vo.AreaVO;
import vo.AvaliacaoVO;
import vo.EntidadeVO;
import vo.EventoVO;
import vo.ItemInspecaoVO;
import vo.RelRankingVO;
import vo.UsuarioVO;

public class CadastrosControle {

    private EventoDAO daoEvento;
    private EntidadeDAO daoEntidade;
    private UsuarioDAO daoUsuario;
    private ItemInspecaoDAO daoItemInspecao;
    private AvaliacaoDAO daoAvaliacao;
    private RelRankingDAO daoRelRanking;
    private AreaDAO daoArea;

    public CadastrosControle() {
        daoEvento = new EventoDAO();
        daoEntidade = new EntidadeDAO();
        daoUsuario = new UsuarioDAO();
        daoItemInspecao = new ItemInspecaoDAO();
        daoAvaliacao = new AvaliacaoDAO();
        daoRelRanking = new RelRankingDAO();
        daoArea= new AreaDAO();
    }


    //relatorio rkg
    public List<RelRankingVO> listarRelRanking(EventoVO e) throws Exception {
        return daoRelRanking.relatorioRanking(e);
    }


    //evento
    public boolean inserirEvento(EventoVO i) throws Exception {
        RegrasNegocioEvento.validarEvento(i, true);
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

    public boolean editarEvento(EventoVO o) throws Exception {
        RegrasNegocioEvento.validarEvento(o, false);
        return daoEvento.editar(o);
    }


    //entidade
    public boolean inserirEntidade(EntidadeVO i) throws Exception {
        RegrasNegocioEntidade.validarEntidade(i, true);
        return daoEntidade.incluir(i);
    }



    public List<EntidadeVO> listarEntidade(String nomePesquisa) throws Exception {
        return daoEntidade.listar(nomePesquisa);
    }

//    public List<EntidadeVO> listarEntidadePorEvento(EventoVO ev) throws Exception {
//        return daoEntidade.listarPorEvento(ev);
//    }

    public boolean excluirEntidade(EntidadeVO o) throws Exception {
        return daoEntidade.excluir(o);
    }

    public EntidadeVO obterEntidadePorId(int a) throws Exception {
        return daoEntidade.obterPorCodigo(a);
    }

    public boolean editarEntidade(EntidadeVO o) throws Exception {
        RegrasNegocioEntidade.validarEntidade(o, false);
        return daoEntidade.editar(o);
    }

    //usuario
    public void validarInclusaoUsuario(UsuarioVO i) throws Exception {
        RegrasNegocioUsuario.validarUsuario(i, true);
    }

    public boolean inserirUsuario(UsuarioVO i) throws Exception {
        RegrasNegocioUsuario.validarUsuario(i, true);
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
        RegrasNegocioUsuario.validarUsuario(o, false);
        return daoUsuario.editar(o);
    }

    public UsuarioVO validarLogin(UsuarioVO o) throws Exception {
        return daoUsuario.validarLogin(o);
    }


    //itemINspecao
    public boolean inserirItemInspecao(ItemInspecaoVO i) throws SQLException {
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
        return daoItemInspecao.excluir(o);
    }

    public ItemInspecaoVO obterItemInspecaoPorId(int a) throws Exception {
        return daoItemInspecao.obterPorCodigo(a);
    }

    public boolean editarItemInspecao(ItemInspecaoVO o) throws Exception {
        return daoItemInspecao.editar(o);
    }

    public List<AreaVO> listarAreas() throws Exception {
            return daoArea.listar();
    }






    //Avaliacao
    public boolean inserirAvaliacao(AvaliacaoVO i) {
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

}
