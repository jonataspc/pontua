package com.bsi.pontua;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import controle.CadastrosControle;
import vo.EntidadeVO;
import vo.EventoVO;
import vo.UsuarioVO;

public class CadastroEntidadesNovoEditar extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_entidades_novo_editar);


        final Button btnCadastrar = (Button) findViewById(R.id.btnCadastrar);
        final EditText txtNome = (EditText) findViewById(R.id.txtNomeEntidade);
        final TextView tvwTitle = (TextView) findViewById(R.id.tvwTitle);

        btnCadastrar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                salvar();
            }
        });

        tvwTitle.setText("Cadastro de Entidades - novo");


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


        //editar ou novo?
        Bundle b = getIntent().getExtras();

        if(b==null){
            finish();
        }

        String registro = b.getString("registro");

        if(registro!=null){
            //carrega dados do registro a alterar


            try {

                tvwTitle.setText("Cadastro de Entidades - editar");
                String[] paramns = new String[]{registro};
                new carregarRegistroTask().execute(paramns );

            }catch (Exception e)
            {
                Toast.makeText(getApplicationContext(), "Erro: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }


        }

    }

    class carregarEventosTask extends AsyncTask<String, Integer, List> {


        @Override
        protected List<EventoVO> doInBackground(String... param) {

            CadastrosControle cc = new CadastrosControle();

            try {

                List<EventoVO> lista = cc.listarEvento("");
                return lista;

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Erro: " + e.getMessage(), Toast.LENGTH_LONG).show();
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
    }


    class carregarRegistroTask extends AsyncTask<String, Integer, EntidadeVO> {

        @Override
        protected EntidadeVO doInBackground(String... param) {

            CadastrosControle cc = new CadastrosControle();

            try {

                EntidadeVO o = new EntidadeVO();

                o = cc.obterEntidadePorId(Integer.parseInt(param[0]));

                return o;

            }catch (Exception e){
                e.printStackTrace();
            }

            return null;

        }


        @Override
        protected void onPostExecute(EntidadeVO result) {

            final EditText txtNome = (EditText) findViewById(R.id.txtNomeEntidade);


            if(result != null){

                txtNome.setText(result.getNome());

                try {
                    //seleciona o evento
                    Spinner dropdown = (Spinner) findViewById(R.id.spnEventos);
                    dropdown.setSelection(((ArrayAdapter)dropdown.getAdapter()).getPosition("[" + String.format("%05d", result.getEvento().getId()) + "] " + result.getEvento().getNome()));

                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), "Erro ao carregar o registro! - " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }




            }
            else
            {

                Toast.makeText(getApplicationContext(), "Erro ao carregar o registro!", Toast.LENGTH_SHORT).show();
            }




        }


    }

    public void onRadioButtonClicked(View view) {



    }

    void salvar(){

        final EditText txtNome = (EditText) findViewById(R.id.txtNomeEntidade);



        //validacoes
        if( txtNome.getText().toString().trim().length()==0  ){
            Toast.makeText(getApplicationContext(), "Informe o nome!", Toast.LENGTH_SHORT).show();
            txtNome.requestFocus();
            return;
        }



        try {

            //editar ou novo?
            Bundle b = getIntent().getExtras();

            String[] paramns = new String[]{txtNome.getText().toString().trim(), b.getString("registro")};
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

                EntidadeVO o = new EntidadeVO();
                o.setNome(param[0]);

                if(param[1] != null){
                    //editar
                    o.setId( Integer.parseInt(param[1]));
                    if(cc.editarEntidade(o) ){
                        return true;
                    }
                }
                else
                {
                    //novo registro
                    if(cc.inserirEntidade(o)){
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

                Toast.makeText(getApplicationContext(), "Alterações salvas com sucesso.", Toast.LENGTH_SHORT).show();
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
