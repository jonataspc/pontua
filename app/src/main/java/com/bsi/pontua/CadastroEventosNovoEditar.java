package com.bsi.pontua;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import controle.CadastrosControle;
import vo.EventoVO;

public class CadastroEventosNovoEditar extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_eventos_novo_editar);

        final Button btnCadastrar = (Button) findViewById(R.id.btnCadastrar);
        final EditText txtNovoEvento = (EditText) findViewById(R.id.txtNovoEvento);
        final TextView tvwEventoTitle = (TextView) findViewById(R.id.tvwTitle);

        btnCadastrar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                salvar();
            }
        });

        tvwEventoTitle.setText("Cadastro de Eventos - novo");


        //editar ou novo?
        Bundle b = getIntent().getExtras();

        if(b==null){
            finish();
        }

        String registro = b.getString("registro");

        if(registro!=null){
            //carrega dados do registro a alterar


            try {

                tvwEventoTitle.setText("Cadastro de Eventos - editar");
                String[] paramns = new String[]{registro};
                new carregarRegistroTask().execute(paramns );

            }catch (Exception e)
            {
                Toast.makeText(getApplicationContext(), "Erro: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }


        }


    }

    class carregarRegistroTask extends AsyncTask<String, Integer, EventoVO> {

        @Override
        protected EventoVO doInBackground(String... param) {

            CadastrosControle cc = new CadastrosControle();

            try {

                EventoVO o = new EventoVO();

                o = cc.obterEventoPorId(Integer.parseInt(param[0]));

                return o;

            }catch (Exception e){
                e.printStackTrace();
            }

            return null;

        }


        @Override
        protected void onPostExecute(EventoVO result) {

            final EditText txtNovoEvento = (EditText) findViewById(R.id.txtNovoEvento);


            if(result != null){

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

        final EditText txtNovoEvento = (EditText) findViewById(R.id.txtNovoEvento);



        //validacoes
        if( txtNovoEvento.getText().toString().trim().length()==0  ){
            Toast.makeText(getApplicationContext(), "Informe o nome do evento!", Toast.LENGTH_SHORT).show();
            txtNovoEvento.requestFocus();
            return;
        }



        try {

            //editar ou novo?
            Bundle b = getIntent().getExtras();

            String[] paramns = new String[]{txtNovoEvento.getText().toString().trim(), b.getString("registro")};
            new salvarTask().execute(paramns );

        }catch (Exception e)
        {
            Toast.makeText(getApplicationContext(), "Erro: " + e.getMessage() , Toast.LENGTH_SHORT).show();
        }


    }

    class salvarTask extends AsyncTask<String, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(String... param) {

            CadastrosControle cc = new CadastrosControle();

            try {

                EventoVO o = new EventoVO();
                o.setNome(param[0]);

                if(param[1] != null){
                    //editar
                    o.setId( Integer.parseInt(param[1]));
                    if(cc.editarEvento(o) ){
                        return true;
                    }
                }
                else
                {
                    //novo registro
                    if(cc.inserirEvento(o)){
                        return true;
                    }
                }




            }catch (Exception e){
                e.printStackTrace();
            }

            return false;

        }


        @Override
        protected void onPostExecute(Boolean result) {



            if(result){

                Toast.makeText(getApplicationContext(), "Alterações salvas com sucesso", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                setResult(Activity.RESULT_OK, intent);
                finish();

            }
            else
            {

                Toast.makeText(getApplicationContext(), "Erro ao realizar a operação!", Toast.LENGTH_SHORT).show();
            }




        }


    }

}
