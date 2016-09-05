package com.bsi.pontua;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import controle.CadastrosControle;
import utils.DecimalDigitsInputFilter;
import vo.AvaliacaoVO;
import vo.EntidadeVO;
import vo.EventoVO;
import vo.ItemInspecaoVO;
import vo.UsuarioVO;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import controle.CadastrosControle;
import utils.DecimalDigitsInputFilter;
import vo.AvaliacaoVO;
import vo.EntidadeVO;
import vo.EventoVO;
import vo.ItemInspecaoVO;
import vo.UsuarioVO;

public class RelatorioRanking extends AppCompatActivity {

/*


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

    private String TXT_MSG_SELECIONE = "Selecione...";

    Bundle b=null;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relatorio_ranking);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //recupera usuario
        b = getIntent().getExtras();

        if(b==null){
            finish();
        }

        final Button btnConsAvaliacoes = (Button) findViewById(R.id.btnConsAvaliacoes);
        btnConsAvaliacoes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                irRelatorio();
            }

        });
        btnConsAvaliacoes.setEnabled(false);

        //carrega eventos em spinner
        AsyncTask cEt = new carregarEventosTask().execute("");

    }

    void irRelatorio(){

        //abre nova activity com relatoio
        Spinner spnEventos = (Spinner) findViewById(R.id.spnEventos);

        Intent myIntent = new Intent(RelatorioRanking.this, RelatorioRankingRel.class);
        b.putInt("id_evento", ((EventoVO) spnEventos.getSelectedItem()).getId());
        myIntent.putExtras(b);
        startActivity(myIntent);

    }

    class carregarEventosTask extends AsyncTask<String, Integer, List> {

        @Override
        protected void onPreExecute() {
            inicializaProgressBar();
            progress.show();
        }

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
            Spinner spnEventos = (Spinner) findViewById(R.id.spnEventos);

            EventoVO newItem = new EventoVO();
            newItem.setNome(TXT_MSG_SELECIONE);
            newItem.setId(-1);
            result.add(0, newItem);



            ArrayAdapter<String> adapter = new ArrayAdapter<String>(RelatorioRanking.this, android.R.layout.simple_spinner_dropdown_item, result);
            spnEventos.setAdapter(adapter);
            spnEventos.requestFocus();


            spnEventos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                    if (((EventoVO) parentView.getSelectedItem()).getNome().equals(TXT_MSG_SELECIONE)) {

                        final Button btnConsAvaliacoes = (Button) findViewById(R.id.btnConsAvaliacoes);
                        btnConsAvaliacoes.setEnabled(false);

                    } else {

                        final Button btnConsAvaliacoes = (Button) findViewById(R.id.btnConsAvaliacoes);
                        btnConsAvaliacoes.setEnabled(true);
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
*/

}
