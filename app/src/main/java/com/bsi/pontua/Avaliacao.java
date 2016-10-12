package com.bsi.pontua;

import android.app.ProgressDialog;
import android.content.Context;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
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

public class Avaliacao extends AppCompatActivity {

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

    private String TXT_AREA_QUALQUER = "[Todas]";
    private int ID_AREA_QUALQUER = -2;

    private EventoVO _eventoAtual = null;
    private EntidadeVO _entidadeAtual = null;
    private ItemInspecaoVO _itemInspecaoAtual = null;

    private AreaVO areaSelecionadaNoLancamento = null;

    Bundle b=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avaliacao);

        setTitle("Avaliação");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final EditText txtPontuacao = (EditText) findViewById(R.id.txtPontuacao);
        txtPontuacao.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(10, 2)});


        //scrolla act todo para cima qdo aparecer o keyboard
        final View activityRootView = getWindow().getDecorView().getRootView();
        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        int heightView = activityRootView.getHeight();
                        int widthView = activityRootView.getWidth();
                        if (1.0 * widthView / heightView > 3) {
                            //Make changes for Keyboard not visible
                        } else {
                            //Make changes for keyboard visible

                            final ScrollView scrollview = ((ScrollView) findViewById(R.id.scrollView));
                            scrollview.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    scrollview.fullScroll(ScrollView.FOCUS_DOWN);
                                }
                            }, 1);

                        }
                    }
                });







        resetaUi();

        //recupera usuario
        b = getIntent().getExtras();

//        if(b==null){
//            finish();
//        }

        final Button btnLancar = (Button) findViewById(R.id.btnLancar);
        btnLancar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                lancarPontuacao();
            }

        });

        final Button btnMinimo = (Button) findViewById(R.id.btnMinimo);
        btnMinimo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                txtPontuacao.setText(String.valueOf(_itemInspecaoAtual.getPontuacaoMinima().doubleValue()));
                lancarPontuacao();
            }

        });

        final Button btnMaximo = (Button) findViewById(R.id.btnMaximo);
        btnMaximo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                txtPontuacao.setText(String.valueOf(_itemInspecaoAtual.getPontuacaoMaxima().doubleValue()));
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
        ((Button) findViewById(R.id.btnLancar)).setEnabled(false);
        ((Button) findViewById(R.id.btnMinimo)).setEnabled(false);
        ((Button) findViewById(R.id.btnMaximo)).setEnabled(false);
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


            //ordena
            Collections.sort (result, new Comparator<EventoVO>() {
                public int compare (EventoVO p1, EventoVO p2) {
                    return p1.getNome().compareTo(p2.getNome());
                }
            });

            EventoVO newItem = new EventoVO();
            newItem.setNome(TXT_MSG_SELECIONE);
            newItem.setId(ID_MSG_SELECIONE);
            result.add(0, newItem);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(Avaliacao.this, android.R.layout.simple_spinner_dropdown_item, result);
            spnEventos.setAdapter(adapter);
            spnEventos.requestFocus();


            spnEventos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    //carrega entidades

                    if (((EventoVO) parentView.getSelectedItem()).getId() == ID_MSG_SELECIONE) {
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
                        new carregarEntidadesTask().execute("");
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
        protected List<RelEntidadeEventoVO> doInBackground(String... param) {

            try(CadastrosControle cc = new CadastrosControle())
            {

                try {

                    List<RelEntidadeEventoVO> lista = cc.listarRelEntidadesPendentesPorEvento(_eventoAtual);

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
            Spinner spnEntidades = (Spinner) findViewById(R.id.spnEntidades);

            if(result==null){
                if (progress != null && progress.isShowing()) {
                    progress.dismiss();
                }
                return;
            }

            //ordena por entidade...
            Collections.sort (result, new Comparator<RelEntidadeEventoVO>() {
                public int compare (RelEntidadeEventoVO p1, RelEntidadeEventoVO p2) {
                    return p1.getEntidade().getNome().compareTo(p2.getEntidade().getNome());
                }
            });

            if(result.size()!=0){
                EntidadeVO newItem = new EntidadeVO();
                newItem.setNome(TXT_MSG_SELECIONE);
                newItem.setId(ID_MSG_SELECIONE);

                RelEntidadeEventoVO o = new RelEntidadeEventoVO();
                o.setEntidade(newItem);
                o.setEvento(_eventoAtual);

                result.add(0, o);
            }


            ArrayAdapter<String> adapter = new ArrayAdapter<String>(Avaliacao.this, android.R.layout.simple_spinner_dropdown_item, result);
            spnEntidades.setAdapter(adapter);
            spnEntidades.requestFocus();


            spnEntidades.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    //carrega areas

                    if (((RelEntidadeEventoVO) parentView.getSelectedItem()).getEntidade().getId() == ID_MSG_SELECIONE) {
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

                        _entidadeAtual = ((RelEntidadeEventoVO) parentView.getSelectedItem()).getEntidade();
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

                    RelEntidadeEventoVO o = new RelEntidadeEventoVO();
                    o.setEntidade(_entidadeAtual);
                    o.setEvento(_eventoAtual);

                    List<AreaVO> lista = cc.listarAreasPendentesPorRelEntidadeEvento(o);

                    return lista;

                } catch (Exception e) {
                    e.printStackTrace();
                }

                return null;
            }


        }

        @Override
        protected void onPostExecute(List result) {

            if(result==null){
                return;
            }

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
                a1.setId(ID_AREA_QUALQUER);

                AreaVO a2 = new AreaVO();
                a2.setNome(TXT_MSG_SELECIONE);
                a2.setId(ID_MSG_SELECIONE);

                result.add(0, a1);
                result.add(0, a2);
            } else {
                //limpa itens
                //zera itens
                ((Spinner) findViewById(R.id.spnItens)).setAdapter(new ArrayAdapter<String>(Avaliacao.this, android.R.layout.simple_spinner_dropdown_item, new ArrayList<String>()));
                resetaUi();

                //recarrega entidades
                //TODO
                //carrega entidades (ainda pendentes) em spinner
                new carregarEntidadesTask().execute("");
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(Avaliacao.this, android.R.layout.simple_spinner_dropdown_item, result);
            spnAreas.setAdapter(adapter);
            spnAreas.requestFocus();


            spnAreas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    //carrega itens

                    if (((AreaVO) parentView.getSelectedItem()).getId() == ID_MSG_SELECIONE) {
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


            //pos avaliacao, recarregou areas pendentes
            if(areaSelecionadaNoLancamento != null){

                //encontra mesmo item...
                for(AreaVO i : (List<AreaVO>) result){

                    if(i.getNome().equals(areaSelecionadaNoLancamento.getNome())){
                        int pos = ((ArrayAdapter) spnAreas.getAdapter()).getPosition(i);

                        if(pos>=0) {
                            spnAreas.setSelection(pos);
                        }

                    }
                }

                //reseta
                areaSelecionadaNoLancamento = null;
            }


            if (progress != null && progress.isShowing()) {
                progress.dismiss();
            }
        }
    }

    void atualizarItens(){

        //carrega itens em spinner
        AreaVO area = (AreaVO)((Spinner) findViewById(R.id.spnAreas)).getSelectedItem();

        if (area.getId() == ID_AREA_QUALQUER) {
            area = null;
        }

        carregarItensTask x = new carregarItensTask();
        x.area = area;
        x.execute("");

    }

    class carregarItensTask extends AsyncTask<String, Integer, List> {

        public AreaVO area=null;

        @Override
        protected void onPreExecute() {
            inicializaProgressBar();
            progress.show();
        }

        @Override
        protected List<ItemInspecaoVO> doInBackground(String... param) {

            try(CadastrosControle cc = new CadastrosControle()){
                try {

                    RelEntidadeEventoVO o = new RelEntidadeEventoVO();
                    o.setEntidade(_entidadeAtual);
                    o.setEvento(_eventoAtual);

                    List<ItemInspecaoVO> lista = cc.listarItensPendentesPorRelEntidadeEvento(o, area);

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


            ArrayAdapter<String> adapter = new ArrayAdapter<String>(Avaliacao.this, android.R.layout.simple_spinner_dropdown_item, result);
            spnItens.setAdapter(adapter);
            spnItens.requestFocus();


            spnItens.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {


                    ItemInspecaoVO item = (ItemInspecaoVO) parentView.getSelectedItem();
                    final TextView lblInstrucoes = (TextView) findViewById(R.id.lblInstrucoes);
                    final EditText txtPontuacao = (EditText) findViewById(R.id.txtPontuacao);

                    _itemInspecaoAtual = item;

                    //habilita campos

                    //instr
                    lblInstrucoes.setText("Pontuação mínima: " + item.getPontuacaoMinima().doubleValue() +
                            "\nPontuação máxima: " + item.getPontuacaoMaxima().doubleValue());

                    //zera e disable txtPont
                    txtPontuacao.setText("");
                    txtPontuacao.setEnabled(true);

                    //enable btn
                    ((Button) findViewById(R.id.btnLancar)).setEnabled(true);
                    ((Button) findViewById(R.id.btnMinimo)).setEnabled(true);
                    ((Button) findViewById(R.id.btnMaximo)).setEnabled(true);
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



        //efetua lancamento
        RelEntidadeEventoVO oRelEntidadeEvento = new RelEntidadeEventoVO();
        oRelEntidadeEvento.setEvento(_eventoAtual);
        oRelEntidadeEvento.setEntidade(_entidadeAtual);


        RelItemInspecaoEventoVO oRelItemInspecaoEvento = new RelItemInspecaoEventoVO();
        oRelItemInspecaoEvento.setEvento(_eventoAtual);
        oRelItemInspecaoEvento.setItemInspecao(_itemInspecaoAtual);


        AvaliacaoVO o = new AvaliacaoVO();
        o.setMetodo(AvaliacaoVO.EnumMetodoAvaliacao.Manual);
        o.setDataHora(new Date());
        o.setPontuacao(new BigDecimal(Double.parseDouble(txtPontuacao.getText().toString())));
        o.setRelEntidadeEvento(oRelEntidadeEvento);
        o.setRelItemInspecaoEvento(oRelItemInspecaoEvento);
        o.setUsuario(Utils.usuarioCorrente);

        //start
        efetuarLancamentoTask task = new efetuarLancamentoTask();
        task.oAvaliacaoVO = o;
        task.execute("");

    }

    class efetuarLancamentoTask extends AsyncTask<String, Integer, Boolean> {

        public AvaliacaoVO oAvaliacaoVO;
        private String errorMsg=null;

        @Override
        protected void onPreExecute() {
            inicializaProgressBar();
            progress.show();
        }

        @Override
        protected Boolean doInBackground(String... param) {

            try(CadastrosControle cc = new CadastrosControle()){

                try {

                    Boolean retorno;
                    retorno = cc.inserirAvaliacao(this.oAvaliacaoVO);
                    return retorno ;

                } catch (Exception e) {
                    e.printStackTrace();
                    errorMsg = e.getMessage();
                    return null;
                }
            }

        }

        @Override
        protected void onPostExecute(Boolean result) {

//            if (progress != null && progress.isShowing()) {
//                progress.dismiss();
//            }

            if(result==null || result==false){

                if (progress != null && progress.isShowing()) {
                    progress.dismiss();
                }

                Toast.makeText(getApplicationContext(), errorMsg , Toast.LENGTH_LONG).show();
                final EditText txtPontuacao = (EditText) findViewById(R.id.txtPontuacao);
                txtPontuacao.requestFocus();
            }else{

                Toast.makeText(getApplicationContext(), "Avaliação realizada com sucesso", Toast.LENGTH_SHORT).show();
                resetaUi();


                //atualiza areas e itens pendentes

                areaSelecionadaNoLancamento = (AreaVO) ((Spinner) findViewById(R.id.spnAreas)).getSelectedItem();


                new carregarAreasTask().execute("");
                atualizarItens();


            }


        }
    }


}
