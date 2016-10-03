package com.bsi.pontua;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import controle.CadastrosControle;
import vo.UsuarioVO;

public class CadastroUsuariosNovoEditar extends AppCompatActivity {

    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuarios_novo_editar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Button btnCadastrar = (Button) findViewById(R.id.btnSalvar);
        final EditText txtNovoUsuario = (EditText) findViewById(R.id.edtNovoUsuario);
        final EditText txtNovoSenha = (EditText) findViewById(R.id.edtNovoSenha);
        final RadioButton rbtAdm =  (RadioButton) findViewById(R.id.rbtAdm);
        final RadioButton rbtAval =  (RadioButton) findViewById(R.id.rbtAval);
        final RadioButton rbtEnt =  (RadioButton) findViewById(R.id.rbtEnt);

        btnCadastrar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                salvar();
            }
        });

        setTitle("Incluir usuário");


        //editar ou novo?
        Bundle b = getIntent().getExtras();

        if(b==null){
            finish();
        }

        String registro = b.getString("registro");

        if(registro!=null){
            //carrega dados do registro a alterar


            try {

                setTitle("Editar usuário");
                String[] paramns = new String[]{registro};
                new carregarRegistroTask().execute(paramns );

            }catch (Exception e)
            {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Erro: " + e.getMessage() , Toast.LENGTH_SHORT).show();
            }


        }
        else
        {
            //nao habilita o ENT caso seja novo
            ((RadioButton) findViewById(R.id.rbtEnt)).setEnabled(false);
        }


    }

    class carregarRegistroTask extends AsyncTask<String, Integer, UsuarioVO> {


        @Override
        protected void onPreExecute() {
            inicializaProgressBar();
            progress.show();
        }

        @Override
        protected UsuarioVO doInBackground(String... param) {
            CadastrosControle cc = new CadastrosControle();

            try {

                UsuarioVO o = new UsuarioVO();

                o = cc.obterUsuarioPorId(Integer.parseInt(param[0]));

                return o;

            }catch (Exception e){
                e.printStackTrace();
            }

            return null;

        }


        @Override
        protected void onPostExecute(UsuarioVO result) {


            if (progress != null && progress.isShowing()) {
                progress.dismiss();
            }

            final EditText txtNovoUsuario = (EditText) findViewById(R.id.edtNovoUsuario);
            final EditText txtNovoSenha = (EditText) findViewById(R.id.edtNovoSenha);
            final RadioButton rbtAdm =  (RadioButton) findViewById(R.id.rbtAdm);
            final RadioButton rbtAval =  (RadioButton) findViewById(R.id.rbtAval);
            final RadioButton rbtEnt =  (RadioButton) findViewById(R.id.rbtEnt);


            if(result != null){

                txtNovoUsuario.setText(result.getNome());
                txtNovoSenha.setText("");

                switch (result.getNivelAcesso()){
                    case "ADM":
                        rbtAdm.setChecked(true);
                        ((RadioButton) findViewById(R.id.rbtEnt)).setEnabled(false);
                        break;

                    case "AVAL":
                        rbtAval.setChecked(true);
                        ((RadioButton) findViewById(R.id.rbtEnt)).setEnabled(false);
                        break;

                    case "ENT":
                        rbtEnt.setChecked(true);
                        // caso seja entidade, nao permite edicao de perfil
                        rbtAdm.setEnabled(false);
                        rbtAval.setEnabled(false);
                        break;
                }




            }
            else
            {

                Toast.makeText(getApplicationContext(), "Erro ao carregar o usuário!", Toast.LENGTH_SHORT).show();
            }




        }


    }

    public void onRadioButtonClicked(View view) {

    }

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
            progress = new ProgressDialog(CadastroUsuariosNovoEditar.this);
            progress.setTitle("");
            progress.setMessage("Aguarde...");
            progress.setIndeterminate(true);
            progress.setCancelable(false);
        }
    }

    void salvar(){

        final EditText txtNovoUsuario = (EditText) findViewById(R.id.edtNovoUsuario);
        final EditText txtNovoSenha = (EditText) findViewById(R.id.edtNovoSenha);
        final RadioButton rbtAdm =  (RadioButton) findViewById(R.id.rbtAdm);
        final RadioButton rbtAval =  (RadioButton) findViewById(R.id.rbtAval);
        final RadioButton rbtEnt =  (RadioButton) findViewById(R.id.rbtEnt);


        //validacoes
        if( txtNovoUsuario.getText().toString().trim().length()==0  ){
            Toast.makeText(getApplicationContext(), "Informe o usuário!", Toast.LENGTH_SHORT).show();
            txtNovoUsuario.requestFocus();
            return;
        }

        if( txtNovoSenha.getText().toString().trim().length()==0  ){
            Toast.makeText(getApplicationContext(), "Informe a senha!", Toast.LENGTH_SHORT).show();
            txtNovoSenha.requestFocus();
            return;
        }

        String nivel=null;

        if(rbtAdm.isChecked()){
            nivel = "ADM";
        }else if(rbtAval.isChecked()){
            nivel = "AVAL";
        }else if(rbtEnt.isChecked()) {
            nivel = "ENT";
        }


        try {

            //editar ou novo?
            Bundle b = getIntent().getExtras();

            String[] paramns = new String[]{txtNovoUsuario.getText().toString().trim(),  txtNovoSenha.getText().toString().trim(), nivel , b.getString("registro")};
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
            CadastrosControle cc = new CadastrosControle();

            try {

                UsuarioVO o = new UsuarioVO();
                o.setEntidade(null);
                o.setNome(param[0]);
                o.setSenha(param[1]);
                o.setNivelAcesso(param[2]);

                if(param[3] != null){
                    //editar
                    o.setId( Integer.parseInt(param[3]));

                    //entidade?
                    UsuarioVO usAtual = cc.obterUsuarioPorId(Integer.parseInt(param[3]));
                    if(usAtual.getEntidade() != null){
                        o.setEntidade(usAtual.getEntidade());
                    }

                    if(cc.editarUsuario(o) ){
                        return true;
                    }
                }
                else
                {
                    //novo registro
                    if(cc.inserirUsuario(o) ){
                        return true;
                    }
                }




            }catch (Exception e){
                e.printStackTrace();
                errorMsg =  e.getMessage();
                return false;
            }

            return false;

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
