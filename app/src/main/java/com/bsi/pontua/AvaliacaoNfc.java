package com.bsi.pontua;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import vo.EventoVO;
import vo.ItemInspecaoVO;
import vo.UsuarioVO;

public class AvaliacaoNfc extends AppCompatActivity {

/*

    ProgressDialog progress;
    Bundle b=null;
    private String TXT_MSG_SELECIONE = "Selecione...";
    private String TXT_AREA_QUALQUER = "[Qualquer]";
    private EventoVO _eventoAtual = null;
    private ItemInspecaoVO _itemInspecaoAtual = null;
    private UsuarioVO _usuarioAtual = null;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avaliacao_nfc);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final EditText txtPontuacao = (EditText) findViewById(R.id.txtPontuacao);
        txtPontuacao.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(10, 2)});


        resetaUi();

        //recupera usuario
        b = getIntent().getExtras();

        if(b==null){
            finish();
        }

        final Button btnLancar = (Button) findViewById(R.id.btnLancar);
        btnLancar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                lancarPontuacao();
            }

        });


        //carrega eventos em spinner
        AsyncTask cEt = new carregarEventosTask().execute("");

        //obtem o usuario
        AsyncTask oUsTask =  new obterUsuarioPorIdTask().execute(new Integer[]{b.getInt("id") });

    }

    void resetaUi() {

        //apaga instr
        final TextView lblInstrucoes = (TextView) findViewById(R.id.lblInstrucoes);
        lblInstrucoes.setText("");

        //zera e disable txtPont
        final EditText txtPontuacao = (EditText) findViewById(R.id.txtPontuacao);
        txtPontuacao.setText("0");
        txtPontuacao.setEnabled(false);

        //disable btn
        final Button btnLancar = (Button) findViewById(R.id.btnLancar);
        btnLancar.setEnabled(false);




    }

    void atualizarItens(){

        //carrega itens em spinner
        String strArea = ((Spinner) findViewById(R.id.spnAreas)).getSelectedItem().toString();

        if (strArea.equals(TXT_AREA_QUALQUER)) {
            strArea = null;
        }


        String[] paramns = new String[]{
                String.valueOf(_eventoAtual.getId()),
                strArea
        };

        new carregarItensTask().execute(paramns);

    }

    void lancarPontuacao() {

        //valida se spinners estao selecionados
        if (_eventoAtual==null || _itemInspecaoAtual==null) {
            Toast.makeText(getApplicationContext(), "Selecione um evento e item a pontuar!", Toast.LENGTH_SHORT).show();
            return;
        }

        //pontuacao preenchida
        final EditText txtPontuacao = (EditText) findViewById(R.id.txtPontuacao);

        if( txtPontuacao.getText().toString().trim().length()==0  ){
            Toast.makeText(getApplicationContext(), "Informe a pontuação!", Toast.LENGTH_SHORT).show();
            txtPontuacao.requestFocus();
            return;
        }

        //valida pontuacao maxima/minima
        if( Double.parseDouble(txtPontuacao.getText().toString()) < Double.parseDouble(_itemInspecaoAtual.getPontuacaoMinima().toString())){
            Toast.makeText(getApplicationContext(), "Pontuação lançada deve ser superior/igual à mínima!", Toast.LENGTH_SHORT).show();
            txtPontuacao.requestFocus();
            return;
        }

        if( Double.parseDouble(txtPontuacao.getText().toString()) > Double.parseDouble(_itemInspecaoAtual.getPontuacaoMaxima().toString())){
            Toast.makeText(getApplicationContext(), "Pontuação lançada deve ser inferior/igual à máxima!", Toast.LENGTH_SHORT).show();
            txtPontuacao.requestFocus();
            return;
        }




        //efetua lancamento
        AvaliacaoVO param;
        param = new AvaliacaoVO();
        param.setEntidade(null);
        param.setItemInspecao(_itemInspecaoAtual);
        param.setPontuacao(new BigDecimal(Double.parseDouble(txtPontuacao.getText().toString())));
        param.setForma_automatica(1);
        param.setUsuario(_usuarioAtual);


        Intent myIntent = new Intent(AvaliacaoNfc.this, AvaliacaoNfcLer.class);
        //myIntent.putExtras(b);
        myIntent.putExtra("objAvaliacaoVO", param );
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

            //            try(CadastrosControle cc = new CadastrosControle()){

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


            ArrayAdapter<String> adapter = new ArrayAdapter<String>(AvaliacaoNfc.this, android.R.layout.simple_spinner_dropdown_item, result);
            spnEventos.setAdapter(adapter);
            spnEventos.requestFocus();


            spnEventos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {


                    if (((EventoVO) parentView.getSelectedItem()).getNome().equals(TXT_MSG_SELECIONE)) {
                        resetaUi();

                        _eventoAtual = null;
                        _itemInspecaoAtual = null;


                        //zera area
                        Spinner spnAreas = (Spinner) findViewById(R.id.spnAreas);
                        spnAreas.setAdapter(new ArrayAdapter<String>(AvaliacaoNfc.this, android.R.layout.simple_spinner_dropdown_item, new ArrayList<String>()));

                        //zera itens
                        Spinner spnItens = (Spinner) findViewById(R.id.spnItens);
                        spnItens.setAdapter(new ArrayAdapter<String>(AvaliacaoNfc.this, android.R.layout.simple_spinner_dropdown_item, new ArrayList<String>()));


                    } else {

                        _eventoAtual = ((EventoVO) parentView.getSelectedItem());
                        _itemInspecaoAtual = null;



                        //carrega areas em spinner
                        Spinner spnEventos = (Spinner) findViewById(R.id.spnEventos);

                        String[] paramns = new String[]{String.valueOf(((EventoVO) spnEventos.getSelectedItem()).getId())};
                        new carregarAreasTask().execute(paramns);
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    resetaUi();
                }
            });


            if (progress != null && progress.isShowing()) {
                progress.dismiss();
            }
        }
    }

     class carregarAreasTask extends AsyncTask<String, Integer, List> {

        @Override
        protected void onPreExecute() {
            inicializaProgressBar();
            progress.show();
        }

        @Override
        protected List<String> doInBackground(String... param) {

            //            try(CadastrosControle cc = new CadastrosControle()){

            try {

                List<String> lista = cc.listarAreas(cc.obterEventoPorId(Integer.parseInt(param[0])));

                return lista;

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(List result) {

            //popula o spinner de areas
            Spinner spnAreas = (Spinner) findViewById(R.id.spnAreas);


            result.add(0, TXT_AREA_QUALQUER);

            result.add(0, TXT_MSG_SELECIONE);


            ArrayAdapter<String> adapter = new ArrayAdapter<String>(AvaliacaoNfc.this, android.R.layout.simple_spinner_dropdown_item, result);
            spnAreas.setAdapter(adapter);
            spnAreas.requestFocus();


            spnAreas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    //carrega itens

                    if (((String) parentView.getSelectedItem()).equals(TXT_MSG_SELECIONE)) {
                        resetaUi();

                        //zera itens
                        Spinner spnItens = (Spinner) findViewById(R.id.spnItens);
                        spnItens.setAdapter(new ArrayAdapter<String>(AvaliacaoNfc.this, android.R.layout.simple_spinner_dropdown_item, new ArrayList<String>()));


                    } else {
                        atualizarItens();
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    resetaUi();
                }
            });


            if (progress != null && progress.isShowing()) {
                progress.dismiss();
            }
        }
    }

    class carregarItensTask extends AsyncTask<String, Integer, List> {

        @Override
        protected void onPreExecute() {
            inicializaProgressBar();
            progress.show();
        }

        @Override
        protected List<ItemInspecaoVO> doInBackground(String... param) {

            //            try(CadastrosControle cc = new CadastrosControle()){

            try {

                List<ItemInspecaoVO> lista = cc.listarItemInspecaoPorEvento(
                        cc.obterEventoPorId(Integer.parseInt(param[0])),
                        param[1]
                );

                return lista;

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(List result) {

            //popula o spinner de areas
            Spinner spnItens = (Spinner) findViewById(R.id.spnItens);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(AvaliacaoNfc.this, android.R.layout.simple_spinner_dropdown_item, result);
            spnItens.setAdapter(adapter);
            spnItens.requestFocus();


            spnItens.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {


                    ItemInspecaoVO item = (ItemInspecaoVO) parentView.getSelectedItem();
                    final TextView lblInstrucoes = (TextView) findViewById(R.id.lblInstrucoes);
                    final EditText txtPontuacao = (EditText) findViewById(R.id.txtPontuacao);
                    final Button btnLancar = (Button) findViewById(R.id.btnLancar);


                    _itemInspecaoAtual = item;


                    //habilita campos

                    //instr
                    lblInstrucoes.setText("Pontuação mínima: " + item.getPontuacaoMinima().doubleValue() +
                            "\nPontuação máxima: " + item.getPontuacaoMaxima().doubleValue());

                    //zera e disable txtPont
                    txtPontuacao.setText("");
                    txtPontuacao.setEnabled(true);

                    //disable btn
                    btnLancar.setEnabled(true);


                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    resetaUi();
                }
            });


            if (progress != null && progress.isShowing()) {
                progress.dismiss();
            }
        }
    }

    class obterUsuarioPorIdTask extends AsyncTask<Integer, Integer, List> {

        @Override
        protected List<UsuarioVO> doInBackground(Integer... param) {

            //            try(CadastrosControle cc = new CadastrosControle()){

            try {

                List<UsuarioVO> lista = new ArrayList<UsuarioVO>(0);
                lista.add(cc.obterUsuarioPorId(param[0]));

                return lista;

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(List result) {

            if(result==null || result.size()==0){
                _usuarioAtual=null;
            }else{
                _usuarioAtual = (UsuarioVO) result.get(0);
            }


        }
    }


*/

}
