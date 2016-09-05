package com.bsi.pontua;

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

public class Avaliacao extends AppCompatActivity {

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
    private String TXT_AREA_QUALQUER = "[Qualquer]";

    private EventoVO _eventoAtual = null;
    private EntidadeVO _entidadeAtual = null;
    private ItemInspecaoVO _itemInspecaoAtual = null;
    private UsuarioVO _usuarioAtual = null;

    Bundle b=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avaliacao);
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

           */
/*
           List<EventoVO> lista = result;

           String[] items = new String[lista.size()+1];

            int cont = 0;

            items[cont] = TXT_MSG_SELECIONE;
            cont++;

            for (EventoVO item : lista) {
                items[cont] = "[" + String.format("%05d", item.getId()) + "] " + item.getNome();
                cont++;
            }*//*


            ArrayAdapter<String> adapter = new ArrayAdapter<String>(Avaliacao.this, android.R.layout.simple_spinner_dropdown_item, result);
            spnEventos.setAdapter(adapter);
            spnEventos.requestFocus();


            spnEventos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    //carrega entidades

                    if (((EventoVO) parentView.getSelectedItem()).getNome().equals(TXT_MSG_SELECIONE)) {
                        resetaUi();

                        _eventoAtual = null;
                        _entidadeAtual = null;
                        _itemInspecaoAtual = null;

                        //zera entidade
                        Spinner spnEntidades = (Spinner) findViewById(R.id.spnEntidades);
                        spnEntidades.setAdapter(new ArrayAdapter<String>(Avaliacao.this, android.R.layout.simple_spinner_dropdown_item, new ArrayList<String>()));

                        //zera area
                        Spinner spnAreas = (Spinner) findViewById(R.id.spnAreas);
                        spnAreas.setAdapter(new ArrayAdapter<String>(Avaliacao.this, android.R.layout.simple_spinner_dropdown_item, new ArrayList<String>()));

                        //zera itens
                        Spinner spnItens = (Spinner) findViewById(R.id.spnItens);
                        spnItens.setAdapter(new ArrayAdapter<String>(Avaliacao.this, android.R.layout.simple_spinner_dropdown_item, new ArrayList<String>()));


                    } else {

                        _eventoAtual = ((EventoVO) parentView.getSelectedItem());
                        _entidadeAtual = null;
                        _itemInspecaoAtual = null;


                        //carrega entidades em spinner
                        String[] paramns = new String[]{String.valueOf(((EventoVO) parentView.getSelectedItem()).getId())};
                        new carregarEntidadesTask().execute(paramns);
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

    class carregarEntidadesTask extends AsyncTask<String, Integer, List> {

        @Override
        protected void onPreExecute() {
            inicializaProgressBar();
            progress.show();
        }

        @Override
        protected List<EntidadeVO> doInBackground(String... param) {

            CadastrosControle cc = new CadastrosControle();

            try {

                List<EntidadeVO> lista = cc.listarEntidadePorEvento(cc.obterEventoPorId(Integer.parseInt(param[0])));

                return lista;

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(List result) {

            //popula o spinner
            Spinner spnEntidades = (Spinner) findViewById(R.id.spnEntidades);


            EntidadeVO newItem = new EntidadeVO();
            newItem.setNome(TXT_MSG_SELECIONE);
            newItem.setId(-1);
            result.add(0, newItem);

            */
/*
            List<EntidadeVO> lista = result;

            String[] items = new String[lista.size()+1];

            int cont = 0;

            items[cont] = TXT_MSG_SELECIONE;
            cont++;

            for (EntidadeVO item : lista) {
                items[cont] = item.getNome();
                cont++;
            }

            *//*


            ArrayAdapter<String> adapter = new ArrayAdapter<String>(Avaliacao.this, android.R.layout.simple_spinner_dropdown_item, result);
            spnEntidades.setAdapter(adapter);
            spnEntidades.requestFocus();


            spnEntidades.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    //carrega areas

                    if (((EntidadeVO) parentView.getSelectedItem()).getNome().equals(TXT_MSG_SELECIONE)) {
                        resetaUi();

                        _entidadeAtual = null;
                        _itemInspecaoAtual = null;

                        //zera area
                        Spinner spnAreas = (Spinner) findViewById(R.id.spnAreas);
                        spnAreas.setAdapter(new ArrayAdapter<String>(Avaliacao.this, android.R.layout.simple_spinner_dropdown_item, new ArrayList<String>()));

                        //zera itens
                        Spinner spnItens = (Spinner) findViewById(R.id.spnItens);
                        spnItens.setAdapter(new ArrayAdapter<String>(Avaliacao.this, android.R.layout.simple_spinner_dropdown_item, new ArrayList<String>()));


                    } else {

                        _entidadeAtual = (EntidadeVO) parentView.getSelectedItem();
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

            CadastrosControle cc = new CadastrosControle();

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


            ArrayAdapter<String> adapter = new ArrayAdapter<String>(Avaliacao.this, android.R.layout.simple_spinner_dropdown_item, result);
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
                        spnItens.setAdapter(new ArrayAdapter<String>(Avaliacao.this, android.R.layout.simple_spinner_dropdown_item, new ArrayList<String>()));


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

    void atualizarItens(){

        //carrega itens em spinner
        String strArea = ((Spinner) findViewById(R.id.spnAreas)).getSelectedItem().toString();

        if (strArea.equals(TXT_AREA_QUALQUER)) {
            strArea = null;
        }


        String[] paramns = new String[]{
                String.valueOf(_eventoAtual.getId()),
                String.valueOf(_entidadeAtual.getId()),
                strArea
        };

        new carregarItensTask().execute(paramns);

    }

    class carregarItensTask extends AsyncTask<String, Integer, List> {

        @Override
        protected void onPreExecute() {
            inicializaProgressBar();
            progress.show();
        }

        @Override
        protected List<ItemInspecaoVO> doInBackground(String... param) {

            CadastrosControle cc = new CadastrosControle();

            try {

                List<ItemInspecaoVO> lista = cc.listarItemInspecaoPendentesPorEventoEntidadeArea(
                        cc.obterEventoPorId(Integer.parseInt(param[0])),
                        cc.obterEntidadePorId(Integer.parseInt(param[1])),
                        param[2]
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


            */
/*
            List<ItemInspecaoVO> lista = result;

            String[] items = new String[lista.size()];

            int cont = 0;

            for (ItemInspecaoVO item : lista) {
                items[cont] = item.getNome() ;
                cont++;
            }
            *//*



            ArrayAdapter<String> adapter = new ArrayAdapter<String>(Avaliacao.this, android.R.layout.simple_spinner_dropdown_item, result);
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


                    //carrega detalhes do item...
                    */
/*
                        Spinner spnEventos = (Spinner) findViewById(R.id.spnEventos);
                        Spinner spnAreas= (Spinner) findViewById(R.id.spnAreas);

                        String[] paramns = new String[]{ ((EventoVO)  spnEventos.getSelectedItem()).getNome(),
                                spnAreas.getSelectedItem().toString()};

                        new carregarItensTask().execute(paramns);

                        *//*


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

    void lancarPontuacao() {

        //valida se spinners estao selecionados
        if (_eventoAtual==null || _entidadeAtual==null || _itemInspecaoAtual==null) {
            Toast.makeText(getApplicationContext(), "Selecione um evento, entidade e item a pontuar!", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getApplicationContext(), "Pontuação lançada deve ser superior/igual à mínima!", Toast.LENGTH_LONG).show();
            txtPontuacao.requestFocus();
            return;
        }

        if( Double.parseDouble(txtPontuacao.getText().toString()) > Double.parseDouble(_itemInspecaoAtual.getPontuacaoMaxima().toString())){
            Toast.makeText(getApplicationContext(), "Pontuação lançada deve ser inferior/igual à máxima!", Toast.LENGTH_LONG).show();
            txtPontuacao.requestFocus();
            return;
        }




        //efetua lancamento
        AvaliacaoVO[] paramns = new AvaliacaoVO[1];

        paramns[0] = new AvaliacaoVO();
        paramns[0].setEntidade(_entidadeAtual);
        paramns[0].setItemInspecao(_itemInspecaoAtual);
        paramns[0].setPontuacao(new BigDecimal(Double.parseDouble(txtPontuacao.getText().toString())));
        paramns[0].setForma_automatica(0);
        paramns[0].setUsuario(_usuarioAtual);

        new efetuarLancamentoTask().execute(paramns);

    }



    class obterUsuarioPorIdTask extends AsyncTask<Integer, Integer, List> {

        @Override
        protected List<UsuarioVO> doInBackground(Integer... param) {

            CadastrosControle cc = new CadastrosControle();

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

    class efetuarLancamentoTask extends AsyncTask<AvaliacaoVO, Integer, Boolean> {

        @Override
        protected void onPreExecute() {
            inicializaProgressBar();
            progress.show();
        }

        @Override
        protected Boolean doInBackground(AvaliacaoVO... param) {

            CadastrosControle cc = new CadastrosControle();

            try {

                Boolean retorno;
                retorno = cc.inserirAvaliacao(param[0]);
                return retorno ;

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Boolean result) {

            if (progress != null && progress.isShowing()) {
                progress.dismiss();
            }

            if(result){
                resetaUi();
                atualizarItens();


                Toast.makeText(getApplicationContext(), "Avaliação realizada com sucesso", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getApplicationContext(), "Erro ao realizar a avaliação" , Toast.LENGTH_LONG).show();
            }





        }
    }

*/

}
