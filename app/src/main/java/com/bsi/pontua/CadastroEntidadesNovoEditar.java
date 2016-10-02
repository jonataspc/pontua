package com.bsi.pontua;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import controle.CadastrosControle;
import vo.EntidadeVO;
import vo.UsuarioVO;

public class CadastroEntidadesNovoEditar extends AppCompatActivity {

    ProgressDialog progress;


    @Override
    public void onPause() {
        //evita erro de leak
        super.onPause();
        if (progress != null) {
            progress.dismiss();
            progress = null;
        }
    }

    @Override
    protected void onStop() {
        //evita erro de leak
        super.onStop();

        if (progress != null) {
            progress.dismiss();
            progress = null;
        }
    }

    void inicializaProgressBar() {

        if (progress == null) {
            progress = new ProgressDialog(CadastroEntidadesNovoEditar.this);
            progress.setTitle("");
            progress.setMessage("Aguarde...");
            progress.setIndeterminate(true);
            progress.setCancelable(false);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_entidades_novo_editar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Button btnCadastrar = (Button) findViewById(R.id.btnSalvar);
        final EditText txtNome = (EditText) findViewById(R.id.edtNomeEntidade);

        btnCadastrar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                salvar();
            }
        });

        setTitle("Incluir entidade");

/*
        //carrega eventos em spinner
         AsyncTask cet =  new carregarEventosTask().execute("");


        try {
            //aguarda conclusao
            cet.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
*/

        changeUIchkUsuario();

        //editar ou novo?
        Bundle b = getIntent().getExtras();

        if(b==null){
            finish();
        }

        String registro = b.getString("registro");

        if(registro!=null){
            //carrega dados do registro a alterar


            try {

                setTitle("Editar entidade");
                String[] paramns = new String[]{registro};
                new carregarRegistroTask().execute(paramns );

            }catch (Exception e)
            {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Erro: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }


        }

    }

   /* class carregarEventosTask extends AsyncTask<String, Integer, List> {


        @Override
        protected List<EventoVO> doInBackground(String... param) {

            CadastrosControle cc = new CadastrosControle();

            try {

                List<EventoVO> lista = cc.listarEvento("");
                return lista;

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(List result) {

            //popula o spinner
            Spinner dropdown = (Spinner) findViewById(R.id.spnEventos);

            List<EventoVO> lista = result;
            String[] items = new String[lista.size()];

            int cont = 0;
            for (EventoVO item : lista) {
                items[cont] = "[" + String.format("%05d", item.getId()) + "] " + item.getNome();
                cont++;
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(CadastroEntidadesNovoEditar.this, android.R.layout.simple_spinner_dropdown_item, items);
            dropdown.setAdapter(adapter);


        }
    }*/

    class ConjuntoEntidadeUsuario{
        private EntidadeVO entidade;
        private UsuarioVO usuario;

        public EntidadeVO getEntidade() {
            return entidade;
        }

        public void setEntidade(EntidadeVO e) {
            this.entidade = e;
        }

        public UsuarioVO getUsuario() {
            return usuario;
        }

        public void setUsuario(UsuarioVO e) {
            this.usuario = e;
        }

    }


    class carregarRegistroTask extends AsyncTask<String, Integer, ConjuntoEntidadeUsuario> {

        @Override
        protected void onPreExecute() {
            inicializaProgressBar();
            progress.show();
        }

        @Override
        protected ConjuntoEntidadeUsuario doInBackground(String... param) {

            CadastrosControle cc = new CadastrosControle();
            ConjuntoEntidadeUsuario retorno = new ConjuntoEntidadeUsuario();

            try {

                EntidadeVO o = new EntidadeVO();
                o = cc.obterEntidadePorId(Integer.parseInt(param[0]));

                UsuarioVO u = new UsuarioVO();
                u = cc.obterUsuarioPorEntidade(o);

                retorno.setUsuario(u);
                retorno.setEntidade(o);
                return retorno;

            }catch (Exception e){
                e.printStackTrace();
                return null;
            }



        }


        @Override
        protected void onPostExecute(ConjuntoEntidadeUsuario result) {

            if (progress != null && progress.isShowing()) {
                progress.dismiss();
            }

            final EditText txtNome = (EditText) findViewById(R.id.edtNomeEntidade);
            final EditText edtUsuarioConsulta = (EditText) findViewById(R.id.edtUsuarioConsulta);
//            final EditText edtUsuarioConsultaSenha = (EditText) findViewById(R.id.edtUsuarioConsultaSenha);


            if(result != null){

                EntidadeVO e = result.getEntidade();
                UsuarioVO u = result.getUsuario();

                txtNome.setText(e.getNome());
                edtUsuarioConsulta.setText(u.getNome());
                //edtUsuarioConsultaSenha.setText(u.getSenha());

/*

                try {
                    //seleciona o evento
                    Spinner dropdown = (Spinner) findViewById(R.id.spnEventos);
                    dropdown.setSelection(((ArrayAdapter)dropdown.getAdapter()).getPosition("[" + String.format("%05d", e.getEvento().getId()) + "] " + e.getEvento().getNome()));

                }catch (Exception ex){
                    ex.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Erro ao carregar o registro! - " + ex.getMessage(), Toast.LENGTH_SHORT).show();

                }

*/



            }
            else
            {

                Toast.makeText(getApplicationContext(), "Erro ao carregar o registro!", Toast.LENGTH_SHORT).show();
            }




        }


    }

    public void onRadioButtonClicked(View view) {



    }

    public void onchkUsuarioClicked(View view) {
        changeUIchkUsuario();
    }

    void changeUIchkUsuario(){

        final CheckBox chkUsuario = (CheckBox) findViewById(R.id.chkUsuario);
        final EditText edtUsuarioConsulta = (EditText) findViewById(R.id.edtUsuarioConsulta);
        final EditText edtUsuarioConsultaSenha = (EditText) findViewById(R.id.edtUsuarioConsultaSenha);

        if(chkUsuario.isChecked()){
            edtUsuarioConsulta.setEnabled(true);
            edtUsuarioConsultaSenha.setEnabled(true);
        } else {
            edtUsuarioConsulta.setEnabled(false);
            edtUsuarioConsultaSenha.setEnabled(false);
        }

    }

    void salvar(){

        final EditText txtNome = (EditText) findViewById(R.id.edtNomeEntidade);
//        final Spinner dropdown = (Spinner) findViewById(R.id.spnEventos);
        final EditText edtUsuarioConsulta = (EditText) findViewById(R.id.edtUsuarioConsulta);
        final EditText edtUsuarioConsultaSenha = (EditText) findViewById(R.id.edtUsuarioConsultaSenha);
        final CheckBox chkUsuario = (CheckBox) findViewById(R.id.chkUsuario);

        //validacoes
        if( txtNome.getText().toString().trim().length()==0  ){
            Toast.makeText(getApplicationContext(), "Informe o nome!", Toast.LENGTH_SHORT).show();
            txtNome.requestFocus();
            return;
        }

//        if(dropdown.getSelectedItem().toString().length() == 0 ){
//            Toast.makeText(getApplicationContext(), "Selecione o evento!", Toast.LENGTH_SHORT).show();
//            dropdown.requestFocus();
//            return;
//        }

        if(chkUsuario.isChecked()){

            if( edtUsuarioConsulta.getText().toString().trim().length()==0  ){
                Toast.makeText(getApplicationContext(), "Informe o usuário!", Toast.LENGTH_SHORT).show();
                edtUsuarioConsulta.requestFocus();
                return;
            }

            if( edtUsuarioConsultaSenha.getText().toString().trim().length()==0  ){
                Toast.makeText(getApplicationContext(), "Informe a senha!", Toast.LENGTH_SHORT).show();
                edtUsuarioConsultaSenha.requestFocus();
                return;
            }

        }




        try {

            //editar ou novo?
            Bundle b = getIntent().getExtras();

            String[] paramns = new String[]{
                    b.getString("registro"),
                    txtNome.getText().toString().trim(),
                    String.valueOf(chkUsuario.isChecked()),
                    edtUsuarioConsulta.getText().toString(),
                    edtUsuarioConsultaSenha.getText().toString()
                    };

            new salvarTask().execute(paramns );

        }catch (Exception e)
        {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Erro: " + e.getMessage() , Toast.LENGTH_SHORT).show();

        }


    }

    class salvarTask extends AsyncTask<String, Integer, Boolean> {

        String errorMsg;

        @Override
        protected void onPreExecute() {
            inicializaProgressBar();
            progress.show();
        }


        @Override
        protected Boolean doInBackground(String... param) {

            boolean retorno=false;

            CadastrosControle cc = new CadastrosControle();

            try {

//                EventoVO oEvt = new EventoVO();
//                oEvt = cc.obterEventoPorId(Integer.parseInt(param[2]));

//                if(oEvt==null){
//                    throw new Exception("Evento nao existente!");
//                }


                EntidadeVO o = new EntidadeVO();
                o.setNome(param[1]);






                //valida dados de usuario...
                boolean chkUsuario = Boolean.parseBoolean(param[2].trim());
                if(chkUsuario){

                    // remove usuario da entidade, caso seja edicao
                    if(param[0] != null) {
                        UsuarioVO usuEntid = cc.obterUsuarioPorEntidade( cc.obterEntidadePorId(Integer.parseInt(param[0])));

                        if(usuEntid!=null){
                            cc.excluirUsuario(usuEntid);
                        }
                    }

                    UsuarioVO newUs = new UsuarioVO();
                    newUs.setEntidade(o);
                    newUs.setNome(param[3].trim());
                    newUs.setSenha(param[4].trim());
                    newUs.setNivelAcesso("ENT");

                    cc.validarInclusaoUsuario(newUs);
                }





                if(param[0] != null){
                    //editar
                    o.setId( Integer.parseInt(param[0]));
                    if(cc.editarEntidade(o) ){
                        retorno=true;
                    }
                }
                else
                {
                    //novo registro
                    if(cc.inserirEntidade(o)){
                        retorno=true;
                    }
                }






                if(chkUsuario){

                    // remove usuario da entidade, caso seja edicao
                    if(param[0] != null) {
                        UsuarioVO usuEntid = cc.obterUsuarioPorEntidade( cc.obterEntidadePorId(Integer.parseInt(param[0])));

                        if(usuEntid!=null){
                            cc.excluirUsuario(usuEntid);
                        }
                    }

                    //cadastra novo usuario
                    UsuarioVO newUs = new UsuarioVO();
                    newUs.setEntidade(o);
                    newUs.setNome(param[3].trim());
                    newUs.setSenha(param[4].trim());
                    newUs.setNivelAcesso("ENT");

                    if(!cc.inserirUsuario(newUs) ){
                        return false;
                        //throw new Exception("Erro no cadastro do novo usuario");
                    }

                }




                return retorno;

            }catch (Exception e){
                e.printStackTrace();
                errorMsg =  e.getMessage();
                return false;
            }



        }


        @Override
        protected void onPostExecute(Boolean result) {

            if (progress != null && progress.isShowing()) {
                progress.dismiss();
            }


            if(result){

                Toast.makeText(getApplicationContext(), "Dados salvos com sucesso", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                setResult(Activity.RESULT_OK, intent);
                finish();

            }
            else
            {

                Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
            }




        }


    }
}
