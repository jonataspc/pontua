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
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

import controle.CadastrosControle;
import utils.Utils;
import vo.EventoVO;
import vo.UsuarioVO;

public class CadastroEventosNovoEditar extends AppCompatActivity {


    ProgressDialog progress;
    private String nomeOriginalEdicao;


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
            progress = new ProgressDialog(CadastroEventosNovoEditar.this);
            progress.setTitle("");
            progress.setMessage("Aguarde...");
            progress.setIndeterminate(true);
            progress.setCancelable(false);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_eventos_novo_editar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Button btnCadastrar = (Button) findViewById(R.id.btnSalvar);
        final EditText txtNovoEvento = (EditText) findViewById(R.id.edtNomeEvento);

        btnCadastrar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                salvar();
            }
        });

        setTitle("Incluir evento");


        //editar ou novo?
        Bundle b = getIntent().getExtras();

        if(b==null){
            finish();
        }

        String registro = b.getString("registro");

        if(registro!=null){
            //carrega dados do registro a alterar


            try {

                setTitle("Editar evento");
                String[] paramns = new String[]{registro};
                new carregarRegistroTask().execute(paramns );

            }catch (Exception e)
            {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Erro: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }


        }


    }

    class carregarRegistroTask extends AsyncTask<String, Integer, EventoVO> {

        @Override
        protected void onPreExecute() {
            inicializaProgressBar();
            progress.show();
        }

        @Override
        protected EventoVO doInBackground(String... param) {

            try(CadastrosControle cc = new CadastrosControle()){
                try {

                    EventoVO o = new EventoVO();

                    o = cc.obterEventoPorId(Integer.parseInt(param[0]));

                    return o;

                }catch (Exception e){
                    e.printStackTrace();
                }

                return null;
            }

        }


        @Override
        protected void onPostExecute(EventoVO result) {

            final EditText txtNovoEvento = (EditText) findViewById(R.id.edtNomeEvento);

            if (progress != null && progress.isShowing()) {
                progress.dismiss();
            }

            if(result != null){

                nomeOriginalEdicao = result.getNome();
                txtNovoEvento.setText(result.getNome());

            }
            else
            {

                Toast.makeText(getApplicationContext(), "Erro ao carregar o evento!", Toast.LENGTH_SHORT).show();
            }




        }


    }

    public void onRadioButtonClicked(View view) {



    }

    void salvar(){

        final EditText txtNovoEvento = (EditText) findViewById(R.id.edtNomeEvento);



        //validacoes
        if( txtNovoEvento.getText().toString().trim().length()==0  ){
            Toast.makeText(getApplicationContext(), "Informe o nome do evento!", Toast.LENGTH_SHORT).show();
            txtNovoEvento.requestFocus();
            return;
        }



        try {

            //editar ou novo?
            Bundle b = getIntent().getExtras();

            //String[] paramns = new String[]{, };
            //new salvarTask().execute(paramns );

            salvarTask oSalvarTask = new salvarTask();

            EventoVO o = new EventoVO();
            o.setNome(txtNovoEvento.getText().toString().trim());
            o.setDataHoraCriacao(new Date());
            o.setUsuario(Utils.usuarioCorrente);

            if(b.getString("registro") != null) {
                o.setId( Integer.parseInt(b.getString("registro")));
                oSalvarTask.IsEditar = true;
            }

            oSalvarTask.evento = o;
            oSalvarTask.execute("");

        }catch (Exception e)
        {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Erro: " + e.getMessage() , Toast.LENGTH_SHORT).show();
        }


    }

    class salvarTask extends AsyncTask<String, Integer, Boolean> {

        String errorMsg;
        public EventoVO evento;
        public boolean IsEditar;

        @Override
        protected void onPreExecute() {
            inicializaProgressBar();
            progress.show();
        }

        @Override
        protected Boolean doInBackground(String... param) {

            try(CadastrosControle cc = new CadastrosControle()){
                try {

                    if(IsEditar){
                        //editar

                        boolean alterouNomeOriginal=false;

                        if(!evento.getNome().toLowerCase().trim().equals(nomeOriginalEdicao.toLowerCase().trim()) ){
                            alterouNomeOriginal = true;
                        }

                        if(cc.editarEvento(evento, alterouNomeOriginal) ){
                            return true;
                        }
                    }
                    else
                    {
                        //novo registro
                        if(cc.inserirEvento(evento)){
                            return true;
                        }
                    }

                    return false;


                }catch (Exception e){
                    e.printStackTrace();
                    errorMsg =  e.getMessage();
                    return false;
                }
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
