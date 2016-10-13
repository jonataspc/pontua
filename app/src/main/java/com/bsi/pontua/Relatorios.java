package com.bsi.pontua;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.List;

import controle.CadastrosControle;
import vo.EventoVO;

public class Relatorios extends AppCompatActivity {



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
            progress = new ProgressDialog(this);
            progress.setTitle("");
            progress.setMessage("Aguarde...");
            progress.setIndeterminate(true);
            progress.setCancelable(false);
        }
    }

    private String TXT_MSG_SELECIONE = "[Selecione]";
    private int ID_MSG_SELECIONE = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relatorios);

        setTitle("Relatórios");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Button btnConsultar = (Button) findViewById(R.id.btnConsultar);
        btnConsultar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                irRelatorio();
            }

        });
        btnConsultar.setEnabled(false);

        //carrega eventos em spinner
        AsyncTask cEt = new carregarEventosTask().execute("");

    }

    void irRelatorio(){

        //abre nova activity com relatoio
        Spinner spnEventos = (Spinner) findViewById(R.id.spnEventos);

        Intent myIntent = new Intent(Relatorios.this, RelatoriosRel.class);
        myIntent.putExtra(EventoVO.class.getName(), (EventoVO) spnEventos.getSelectedItem());
        startActivityForResult(myIntent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            /*if (resultCode == Activity.RESULT_OK) {
                //atualiza lista
                new popularGridTask().execute("");
            }*/
        }
    }

    class carregarEventosTask extends AsyncTask<String, Integer, List> {

        @Override
        protected void onPreExecute() {
            inicializaProgressBar();
            progress.show();
        }

        @Override
        protected List<EventoVO> doInBackground(String... param) {

            try {
                try(CadastrosControle cc = new CadastrosControle()){
                    List<EventoVO> lista = cc.listarEvento("");
                    return lista;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(List result) {

            //popula o spinner
            Spinner spnEventos = (Spinner) findViewById(R.id.spnEventos);

            EventoVO newItem = new EventoVO();
            newItem.setNome(TXT_MSG_SELECIONE);
            newItem.setId(ID_MSG_SELECIONE);
            result.add(0, newItem);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(Relatorios.this, android.R.layout.simple_spinner_dropdown_item, result);
            spnEventos.setAdapter(adapter);
            spnEventos.requestFocus();


            spnEventos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                    if (((EventoVO) parentView.getSelectedItem()).getId() == ID_MSG_SELECIONE) {
                        ((Button) findViewById(R.id.btnConsultar)).setEnabled(false);
                    } else {
                        ((Button) findViewById(R.id.btnConsultar)).setEnabled(true);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                }
            });


            if (progress != null && progress.isShowing()) {
                progress.dismiss();
            }
        }
    }

}
