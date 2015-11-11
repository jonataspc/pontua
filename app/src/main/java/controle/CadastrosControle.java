package controle;

import java.util.List;

import dao.AvaliacaoDAO;
import dao.EntidadeDAO;
import dao.EventoDAO;
import dao.ItemInspecaoDAO;
import dao.UsuarioDAO;
import vo.AvaliacaoVO;
import vo.EntidadeVO;
import vo.EventoVO;
import vo.ItemInspecaoVO;
import vo.UsuarioVO;

public class CadastrosControle {

	private EventoDAO daoEvento;
	private EntidadeDAO daoEntidade;
	private UsuarioDAO daoUsuario;
	private ItemInspecaoDAO daoItemInspecao;
	private AvaliacaoDAO daoAvaliacao;

	public CadastrosControle() {
 		daoEvento = new EventoDAO();
		daoEntidade = new EntidadeDAO();
		daoUsuario= new UsuarioDAO();
		daoItemInspecao = new ItemInspecaoDAO();
		daoAvaliacao = new AvaliacaoDAO();

	}


	//evento
	public boolean inserirEvento( EventoVO i){
		return daoEvento.incluir(i);
	}

	public    List<EventoVO> listarEvento(String nomePesquisa) throws Exception{
		return daoEvento.listar(nomePesquisa);
	}

	public     boolean excluirEvento(EventoVO o) throws Exception{
		return daoEvento.excluir(o);
	}

	public     EventoVO obterEventoPorId(int a) throws Exception{
		return daoEvento.obterPorCodigo(a);
	}

	public     boolean editarEvento(EventoVO  o) throws Exception{
		return daoEvento.editar(o);
	}




	//entidade
	public boolean inserirEntidade( EntidadeVO i){
		return daoEntidade.incluir(i);
	}

	public    List<EntidadeVO> listarEntidade(String nomePesquisa) throws Exception{
		return daoEntidade.listar(nomePesquisa);
	}

	public     boolean excluirEntidade(EntidadeVO o) throws Exception{
		return daoEntidade.excluir(o);
	}

	public     EntidadeVO obterEntidadePorId(int a) throws Exception{
		return daoEntidade.obterPorCodigo(a);
	}

	public     boolean editarEntidade(EntidadeVO  o) throws Exception{
		return daoEntidade.editar(o);
	}

	//usuario
	public boolean inserirUsuario( UsuarioVO i){
		return daoUsuario.incluir(i);
	}

	public    List<UsuarioVO> listarUsuario(String nomePesquisa) throws Exception{
		return daoUsuario.listar(nomePesquisa);
	}

	public     boolean excluirUsuario(UsuarioVO o) throws Exception{
		return daoUsuario.excluir(o);
	}

	public     UsuarioVO obterUsuarioPorId(int a) throws Exception{
		return daoUsuario.obterPorCodigo(a);
	}

	public     UsuarioVO obterUsuarioPorEntidade(EntidadeVO e) throws Exception{
		return daoUsuario.obterPorEntidade(e);
	}

	public     boolean editarUsuario(UsuarioVO  o) throws Exception{
		return daoUsuario.editar(o);
	}

	public     boolean validarLogin(UsuarioVO  o) throws Exception{
		return daoUsuario.validarLogin(o);
	}



	//itemINspecao
	public boolean inserirItemInspecao( ItemInspecaoVO i){
		return daoItemInspecao.incluir(i);
	}

	public    List<ItemInspecaoVO> listarItemInspecao(String nomePesquisa) throws Exception{
		return daoItemInspecao.listar(nomePesquisa);
	}

	public     boolean excluirItemInspecao(ItemInspecaoVO o) throws Exception{
		return daoItemInspecao.excluir(o);
	}

	public     ItemInspecaoVO obterItemInspecaoPorId(int a) throws Exception{
		return daoItemInspecao.obterPorCodigo(a);
	}

	public     boolean editarItemInspecao(ItemInspecaoVO  o) throws Exception{
		return daoItemInspecao.editar(o);
	}




	//Avaliacao
	public boolean inserirAvaliacao( AvaliacaoVO i){
		return daoAvaliacao.incluir(i);
	}

	public    List<AvaliacaoVO> listarAvaliacao() throws Exception{
		return daoAvaliacao.listar();
	}

	public    List<AvaliacaoVO> listarAvaliacao(UsuarioVO usuario) throws Exception{
		return daoAvaliacao.listar(usuario);
	}

	public    List<AvaliacaoVO> listarAvaliacao(ItemInspecaoVO itemInspecao) throws Exception{
		return daoAvaliacao.listar(itemInspecao);
	}

	public    List<AvaliacaoVO> listarAvaliacao(EntidadeVO entidade) throws Exception{
		return daoAvaliacao.listar(entidade);
	}

	public     boolean excluirAvaliacao(AvaliacaoVO o) throws Exception{
		return daoAvaliacao.excluir(o);
	}

	public     AvaliacaoVO obterAvaliacaoPorId(int a) throws Exception{
		return daoAvaliacao.obterPorCodigo(a);
	}

	public     boolean editarAvaliacao(AvaliacaoVO o) throws Exception{
		return daoAvaliacao.editar(o);
	}

}
