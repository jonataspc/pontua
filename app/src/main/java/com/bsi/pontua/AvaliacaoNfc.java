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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import controle.CadastrosControle;
import utils.DecimalDigitsInputFilter;
import utils.Utils;
import vo.AreaVO;
import vo.AvaliacaoVO;
import vo.EntidadeVO;
import vo.EventoVO;
import vo.ItemInspecaoVO;
import vo.RelEntidadeEventoVO;
import vo.RelItemInspecaoEventoVO;
import vo.UsuarioVO;

public class AvaliacaoNfc extends AppCompatActivity {


    ProgressDialog progress;
    Bundle b=null;
    private String TXT_MSG_SELECIONE = "Selecione...";
    private String TXT_AREA_QUALQUER = "[Todas]";

    private EventoVO _eventoAtual = null;
    private ItemInspecaoVO _itemInspecaoAtual = null;

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

        setTitle("Avaliação por NFC");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final EditText txtPontuacao = (EditText) findViewById(R.id.txtPontuacao);
        txtPontuacao.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(10, 2)});


        resetaUi();

        //recupera usuario
        b = getIntent().getExtras();

      /*  if(b==null){
            finish();
        }*/

        final Button btnLancar = (Button) findViewById(R.id.btnLancar);
        btnLancar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                lancarPontuacao();
            }

        });


        //carrega eventos em spinner
        AsyncTask cEt = new carregarEventosTask().execute("");


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

        new carregarItensTask().execute(strArea);

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
            Toast.makeText(getApplicationContext(), "Pontuação informada deve ser superior/igual à mínima!", Toast.LENGTH_SHORT).show();
            txtPontuacao.requestFocus();
            return;
        }

        if( Double.parseDouble(txtPontuacao.getText().toString()) > Double.parseDouble(_itemInspecaoAtual.getPontuacaoMaxima().toString())){
            Toast.makeText(getApplicationContext(), "Pontuação informada deve ser inferior/igual à máxima!", Toast.LENGTH_SHORT).show();
            txtPontuacao.requestFocus();
            return;
        }




        //controi objetos para lancamento posterior
        RelEntidadeEventoVO oRelEntidadeEvento = new RelEntidadeEventoVO();
        oRelEntidadeEvento.setEvento(_eventoAtual);
        oRelEntidadeEvento.setEntidade(null); // sera obtido atraves da leitura NFC

        RelItemInspecaoEventoVO oRelItemInspecaoEvento = new RelItemInspecaoEventoVO();
        oRelItemInspecaoEvento.setEvento(_eventoAtual);
        oRelItemInspecaoEvento.setItemInspecao(_itemInspecaoAtual);


        AvaliacaoVO o = new AvaliacaoVO();
        o.setMetodo(AvaliacaoVO.EnumMetodoAvaliacao.NFC);
        o.setDataHora(new Date());
        o.setPontuacao(new BigDecimal(Double.parseDouble(txtPontuacao.getText().toString())));
        o.setRelEntidadeEvento(oRelEntidadeEvento);
        o.setRelItemInspecaoEvento(oRelItemInspecaoEvento);
        o.setUsuario(Utils.usuarioCorrente);


        Intent myIntent = new Intent(AvaliacaoNfc.this, AvaliacaoNfcLer.class);
        myIntent.putExtra("objAvaliacaoVO", o );

        final CheckBox cks = (CheckBox) findViewById(R.id.ckSobrescrever);
        myIntent.putExtra("sobrescrever", cks.isChecked());

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


            try(CadastrosControle cc = new CadastrosControle()){

                try {

                    List<EventoVO> lista = cc.listarEvento("");
                    return lista;

                } catch (Exception e) {
                    e.printStackTrace();
                }

                return null;

            }


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
                        new carregarAreasTask().execute("");
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
        protected List<AreaVO> doInBackground(String... param) {

            try(CadastrosControle cc = new CadastrosControle()){

                try {

                    List<AreaVO> lista = cc.listarAreasPendentesPorEvento(_eventoAtual);

                    return lista;

                } catch (Exception e) {
                    e.printStackTrace();
                }

                return null;
            }

        }

        @Override
        protected void onPostExecute(List result) {

            //popula o spinner de areas
            Spinner spnAreas = (Spinner) findViewById(R.id.spnAreas);

            //ordena
            Collections.sort (result, new Comparator<AreaVO>() {
                public int compare (AreaVO p1, AreaVO p2) {
                    return p1.getNome().compareTo(p2.getNome());
                }
            });

            //somente adiciona caso existam itens validos..

            if(result.size()!=0){
                AreaVO a1 = new AreaVO();
                a1.setNome(TXT_AREA_QUALQUER);

                AreaVO a2 = new AreaVO();
                a2.setNome(TXT_MSG_SELECIONE);

                result.add(0, a1);
                result.add(0, a2);
            } else {

                //limpa itens
                //zera itens
                ((Spinner) findViewById(R.id.spnItens)).setAdapter(new ArrayAdapter<String>(AvaliacaoNfc.this, android.R.layout.simple_spinner_dropdown_item, new ArrayList<String>()));
                resetaUi();


            }



            ArrayAdapter<String> adapter = new ArrayAdapter<String>(AvaliacaoNfc.this, android.R.layout.simple_spinner_dropdown_item, result);
            spnAreas.setAdapter(adapter);
            spnAreas.requestFocus();


            spnAreas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    //carrega itens

                    if (((AreaVO) parentView.getSelectedItem()).getNome().equals(TXT_MSG_SELECIONE)) {
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

            try(CadastrosControle cc = new CadastrosControle()){
                try {

                    AreaVO area = null;

                    //só manda area caso nao seja QUALQUER
                    if(param[0] != null){
                        area = cc.obterAreaPorNome(param[0]);
                    }

                    List<ItemInspecaoVO> lista = cc.listarItensPendentesPorEvento(_eventoAtual, area);

                    return lista;

                } catch (Exception e) {
                    e.printStackTrace();
                }

                return null;
            }

        }

        @Override
        protected void onPostExecute(List result) {

            //popula o spinner de areas
            Spinner spnItens = (Spinner) findViewById(R.id.spnItens);

            //ordena
            Collections.sort (result, new Comparator<ItemInspecaoVO>() {
                public int compare (ItemInspecaoVO p1, ItemInspecaoVO p2) {
                    return p1.getNome().compareTo(p2.getNome());
                }
            });


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




}
