package com.bsi.pontua;

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
import android.view.ViewTreeObserver;
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
import java.util.Iterator;
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

public class ConsultarAvaliacoes extends AppCompatActivity {


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

    private String TXT_TODAS = "[Todas]";
    private String TXT_TODOS = "[Todos]";
    private int ID_TODAS = -2;


    private EventoVO _eventoAtual = null;
    private EntidadeVO _entidadeAtual = null;
    private ItemInspecaoVO _itemInspecaoAtual = null;
    private AreaVO _areaAtual = null;

//    private AreaVO areaSelecionadaNoLancamento = null;

    Bundle b=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultar_avaliacoes);

        setTitle("Consultar Avaliações");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        resetaUi();



        final Button btnConsultar = (Button) findViewById(R.id.btnConsultar);
        btnConsultar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                gotoRel();
            }

        });


        //carrega eventos em spinner
        AsyncTask cEt = new ConsultarAvaliacoes.carregarEventosTask().execute("");
    }

    void resetaUi() {

        //disable btn
        ((Button) findViewById(R.id.btnConsultar)).setEnabled(false);

        //esvazia spinners

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

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(ConsultarAvaliacoes.this, android.R.layout.simple_spinner_dropdown_item, result);
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
                        spnEntidades.setAdapter(new ArrayAdapter<String>(ConsultarAvaliacoes.this, android.R.layout.simple_spinner_dropdown_item, new ArrayList<String>()));

                        //zera area
                        Spinner spnAreas = (Spinner) findViewById(R.id.spnAreas);
                        spnAreas.setAdapter(new ArrayAdapter<String>(ConsultarAvaliacoes.this, android.R.layout.simple_spinner_dropdown_item, new ArrayList<String>()));

                        //zera itens
                        Spinner spnItens = (Spinner) findViewById(R.id.spnItens);
                        spnItens.setAdapter(new ArrayAdapter<String>(ConsultarAvaliacoes.this, android.R.layout.simple_spinner_dropdown_item, new ArrayList<String>()));


                    } else {

                        _eventoAtual = ((EventoVO) parentView.getSelectedItem());
                        _entidadeAtual = null;
                        _itemInspecaoAtual = null;

                        //zera entidade
                        Spinner spnEntidades = (Spinner) findViewById(R.id.spnEntidades);
                        spnEntidades.setAdapter(new ArrayAdapter<String>(ConsultarAvaliacoes.this, android.R.layout.simple_spinner_dropdown_item, new ArrayList<String>()));

                        //zera area
                        Spinner spnAreas = (Spinner) findViewById(R.id.spnAreas);
                        spnAreas.setAdapter(new ArrayAdapter<String>(ConsultarAvaliacoes.this, android.R.layout.simple_spinner_dropdown_item, new ArrayList<String>()));

                        //zera itens
                        Spinner spnItens = (Spinner) findViewById(R.id.spnItens);
                        spnItens.setAdapter(new ArrayAdapter<String>(ConsultarAvaliacoes.this, android.R.layout.simple_spinner_dropdown_item, new ArrayList<String>()));



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

                    List<RelEntidadeEventoVO> lista = cc.listarRelEntidadeEventoPorEvento(_eventoAtual);

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
                //todas
                EntidadeVO newItem2 = new EntidadeVO();
                newItem2.setNome(TXT_TODAS);
                newItem2.setId(ID_TODAS);

                RelEntidadeEventoVO o2 = new RelEntidadeEventoVO();
                o2.setEntidade(newItem2);
                o2.setEvento(_eventoAtual);

                result.add(0, o2);
            }


            ArrayAdapter<String> adapter = new ArrayAdapter<String>(ConsultarAvaliacoes.this, android.R.layout.simple_spinner_dropdown_item, result);
            spnEntidades.setAdapter(adapter);
            spnEntidades.requestFocus();


            spnEntidades.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    //carrega areas

//                    if (((RelEntidadeEventoVO) parentView.getSelectedItem()).getEntidade().getNome().equals(TXT_MSG_SELECIONE)) {
//                        resetaUi();
//
//                        _entidadeAtual = null;
//                        _itemInspecaoAtual = null;
//
//                        //zera area
//                        Spinner spnAreas = (Spinner) findViewById(R.id.spnAreas);
//                        spnAreas.setAdapter(new ArrayAdapter<String>(ConsultarAvaliacoes.this, android.R.layout.simple_spinner_dropdown_item, new ArrayList<String>()));
//
//                        //zera itens
//                        Spinner spnItens = (Spinner) findViewById(R.id.spnItens);
//                        spnItens.setAdapter(new ArrayAdapter<String>(ConsultarAvaliacoes.this, android.R.layout.simple_spinner_dropdown_item, new ArrayList<String>()));
//
//
//                    } else {

                        _entidadeAtual = ((RelEntidadeEventoVO) parentView.getSelectedItem()).getEntidade();
                        _itemInspecaoAtual = null;


                        //carrega areas em spinner
                        new carregarAreasTask().execute("");

//                    }

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

                    List<AreaVO> lista = null;

                    if(_entidadeAtual.getId()==ID_TODAS){
                        //todas entidades

                        lista = cc.listarAreasPorEvento(_eventoAtual);

                    } else {
                        RelEntidadeEventoVO o = new RelEntidadeEventoVO();
                        o.setEntidade(_entidadeAtual);
                        o.setEvento(_eventoAtual);

                        lista = cc.listarAreasPorRelEntidadeEvento(o);
                    }



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
                a1.setNome(TXT_TODAS);
                a1.setId(ID_TODAS);

                result.add(0, a1);

            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(ConsultarAvaliacoes.this, android.R.layout.simple_spinner_dropdown_item, result);
            spnAreas.setAdapter(adapter);
            spnAreas.requestFocus();


            spnAreas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    //carrega itens

//                    if (((AreaVO) parentView.getSelectedItem()).getNome().equals(TXT_MSG_SELECIONE)) {
//                        resetaUi();
//
//                        //zera itens
//                        Spinner spnItens = (Spinner) findViewById(R.id.spnItens);
//                        spnItens.setAdapter(new ArrayAdapter<String>(ConsultarAvaliacoes.this, android.R.layout.simple_spinner_dropdown_item, new ArrayList<String>()));
//
//
//                    } else {


                    AreaVO item = (AreaVO) parentView.getSelectedItem();

                    _areaAtual = item;


                    atualizarItens();
//                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    resetaUi();
                }
            });


//            //pos avaliacao, recarregou areas pendentes
//            if(areaSelecionadaNoLancamento != null){
//
//                //encontra mesmo item...
//                for(AreaVO i : (List<AreaVO>) result){
//
//                    if(i.getNome().equals(areaSelecionadaNoLancamento.getNome())){
//                        int pos = ((ArrayAdapter) spnAreas.getAdapter()).getPosition(i);
//
//                        if(pos>=0) {
//                            spnAreas.setSelection(pos);
//                        }
//
//                    }
//                }
//
//                //reseta
//                areaSelecionadaNoLancamento = null;
//            }


            if (progress != null && progress.isShowing()) {
                progress.dismiss();
            }
        }
    }

    void atualizarItens(){

        //carrega itens em spinner
        String strArea = ((Spinner) findViewById(R.id.spnAreas)).getSelectedItem().toString();

        if (strArea.equals(TXT_TODAS)) {
            strArea = null;
        }

        new carregarItensTask().execute(strArea);

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

                    List<ItemInspecaoVO> lista = cc.listarItensPorEvento(_eventoAtual, area);

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

            if(result==null ){
                return;
            }


            if(result!=null && result.size()!=0){

                //ordena
                Collections.sort (result, new Comparator<ItemInspecaoVO>() {
                    public int compare (ItemInspecaoVO p1, ItemInspecaoVO p2) {
                        return p1.getNome().compareTo(p2.getNome());
                    }
                });

                //todas
                ItemInspecaoVO o2 = new ItemInspecaoVO();
                o2.setId(ID_TODAS);
                o2.setNome(TXT_TODOS);
                result.add(0, o2);
            }


            ArrayAdapter<String> adapter = new ArrayAdapter<String>(ConsultarAvaliacoes.this, android.R.layout.simple_spinner_dropdown_item, result);
            spnItens.setAdapter(adapter);
            spnItens.requestFocus();


            spnItens.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {


                    ItemInspecaoVO item = (ItemInspecaoVO) parentView.getSelectedItem();

                    _itemInspecaoAtual = item;

                    //habilita campos
                    //enable btn
                    ((Button) findViewById(R.id.btnConsultar)).setEnabled(true);

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

    void gotoRel() {

        //valida se spinners estao selecionados
        if (_eventoAtual==null || _entidadeAtual==null || _areaAtual==null || _itemInspecaoAtual==null) {
            Toast.makeText(getApplicationContext(), "Selecione filtros suficientes para realizar a consulta!", Toast.LENGTH_SHORT).show();
            return;
        }

        String txt="Evt: " + _eventoAtual.toString() + "\nEntid: " + _entidadeAtual.toString() + "\nArea: " + _areaAtual.toString() + "\nItem: " + _itemInspecaoAtual.toString();

        Toast.makeText(getApplicationContext(), txt, Toast.LENGTH_SHORT).show();





    }


}
