package com.bsi.pontua;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.InputFilter;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
import java.util.concurrent.ExecutionException;

import controle.CadastrosControle;
import utils.DecimalDigitsInputFilter;
import vo.AreaVO;
import vo.ItemInspecaoVO;


public class CadastroItensInspecaoNovoEditar extends AppCompatActivity {

    private String TXT_AREA_NOVA = "[Nova área...]";
    private String TXT_AREA_DEFAULT = "[SELECIONE]";

    // this context will use when we work with Alert Dialog
    final Context context = this;
    List<String> listaAreas;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_itens_inspecao_novo_editar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Button btnCadastrar = (Button) findViewById(R.id.btnSalvar);
        final EditText txtNome = (EditText) findViewById(R.id.txtNome);
        final TextView tvwTitle = (TextView) findViewById(R.id.tvwTitle);

        final EditText txtPontMin = (EditText) findViewById(R.id.edtPontMinima);
        final EditText txtPontMax = (EditText) findViewById(R.id.edtPontMaxima);

        txtPontMin.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(10,2)});
        txtPontMax.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(10,2)});



        inicializaProgressBar();
        progress.show();


        btnCadastrar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                salvar();
            }
        });

        tvwTitle.setText("Incluir ítem de inspeção");


//        //carrega eventos em spinner
//        AsyncTask cEt =  new carregarEventosTask().execute("");

        //carrega areas
        AsyncTask cAt =  new carregarAreasTask().execute("");



        try {
            //aguarda conclusao
            //cEt.get();
            cAt.get();
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

                tvwTitle.setText("Editar ítem de inspeção");
                String[] paramns = new String[]{registro};
                new carregarRegistroTask().execute(paramns );

            }catch (Exception e)
            {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Erro: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }


        } else {

            if (progress != null && progress.isShowing()) {
                progress.dismiss();
            }
        }


    }

//    class carregarEventosTask extends AsyncTask<String, Integer, List> {
//
//        @Override
//        protected void onPreExecute() {
//            inicializaProgressBar();
//            progress.show();
//        }
//
//        @Override
//        protected List<EventoVO> doInBackground(String... param) {
//
//            CadastrosControle cc = new CadastrosControle();
//
//            try {
//
//                List<EventoVO> lista = cc.listarEvento("");
//                return lista;
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(List result) {
//
//            //popula o spinner
//            Spinner dropdown = (Spinner) findViewById(R.id.spnEventos);
//
//            List<EventoVO> lista = result;
//            String[] items = new String[lista.size()];
//
//            int cont = 0;
//            for (EventoVO item : lista) {
//                items[cont] = "[" + String.format("%05d", item.getId()) + "] " + item.getNome();
//                cont++;
//            }
//
//            ArrayAdapter<String> adapter = new ArrayAdapter<String>(CadastroItensInspecaoNovoEditar.this, android.R.layout.simple_spinner_dropdown_item, items);
//            dropdown.setAdapter(adapter);
//
//
//
//            if (progress != null && progress.isShowing()) {
//                progress.dismiss();
//            }
//        }
//    }

    class carregarAreasTask extends AsyncTask<String, Integer, List> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected List<AreaVO> doInBackground(String... param) {

            CadastrosControle cc = new CadastrosControle();

            try {

                List<AreaVO> lista = cc.listarAreas();
                return lista;

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(List result) {

            //popula o spinner
            Spinner spnAreas = (Spinner) findViewById(R.id.spnAreas);


            List<AreaVO> res2 = result;

            listaAreas = new ArrayList<String>(0);

            for (AreaVO item : res2) {
                listaAreas.add(item.getNome());
            }



            listaAreas.add(0, TXT_AREA_NOVA);
            listaAreas.add(0, TXT_AREA_DEFAULT);



            String[] items = new String[listaAreas.size()];



            int cont = 0;
            for (String  item : listaAreas) {
                items[cont] =item;
                cont++;
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(CadastroItensInspecaoNovoEditar.this, android.R.layout.simple_spinner_dropdown_item, items);
            spnAreas.setAdapter(adapter);


            //add evento
            spnAreas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    //selecionou o [Novo] ?
                    if (parentView.getSelectedItem().toString().equals(TXT_AREA_NOVA)) {

 //Alert Dialog Code Start

                        AlertDialog.Builder alert = new AlertDialog.Builder(context);
                        alert.setTitle("Nova área de inspeção"); //Set Alert dialog title here
                        alert.setMessage("");

                        final EditText input = new EditText(context);

                        int maxLength = 30;
                        InputFilter[] fArray = new InputFilter[1];
                        fArray[0] = new InputFilter.LengthFilter(maxLength);
                        input.setFilters(fArray);
                        input.setHint("Insira o nome da área");
                        alert.setView(input);

                        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                String srt = input.getEditableText().toString();
                                //Toast.makeText(context,srt,Toast.LENGTH_LONG).show();


                                if (srt.trim().length() != 0) {
                                    listaAreas.add(srt);

                                    String[] items = new String[listaAreas.size()];

                                    int cont = 0;
                                    for (String item : listaAreas) {
                                        items[cont] = item;
                                        cont++;
                                    }

                                    //adiciona nova area...
                                    new incluirArea().execute( srt );

                                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(CadastroItensInspecaoNovoEditar.this, android.R.layout.simple_spinner_dropdown_item, items);
                                    Spinner spnAreas = (Spinner) findViewById(R.id.spnAreas);
                                    spnAreas.setAdapter(adapter);

                                    //pre seleciona o inserido
                                    spnAreas.setSelection(((ArrayAdapter) spnAreas.getAdapter()).getPosition(srt));
                                }
                                else
                                {
                                    //seleciona 'nenhuma'
                                    Spinner spnAreas = (Spinner) findViewById(R.id.spnAreas);
                                    spnAreas.setSelection(((ArrayAdapter) spnAreas.getAdapter()).getPosition(TXT_AREA_DEFAULT));
                                }


                            }
                        });

                        alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                                //seleciona 'nenhuma'
                                Spinner spnAreas = (Spinner) findViewById(R.id.spnAreas);
                                spnAreas.setSelection(((ArrayAdapter) spnAreas.getAdapter()).getPosition(TXT_AREA_DEFAULT));

                                dialog.cancel();
                            }
                        });

                        AlertDialog alertDialog = alert.create();
                        alertDialog.show();
 //Alert Dialog Code End



                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {

                }
            });


//            if (progress != null && progress.isShowing()) {
//                progress.dismiss();
//            }


        }
    }

    class carregarRegistroTask extends AsyncTask<String, Integer, ItemInspecaoVO> {

        @Override
        protected void onPreExecute() {
            inicializaProgressBar();
            progress.show();
        }

        @Override
        protected ItemInspecaoVO doInBackground(String... param) {

            CadastrosControle cc = new CadastrosControle();
            //ConjuntoEntidadeUsuario retorno = new ConjuntoEntidadeUsuario();

            try {

                ItemInspecaoVO o = new ItemInspecaoVO();
                o = cc.obterItemInspecaoPorId(Integer.parseInt(param[0]));

                //UsuarioVO u = new UsuarioVO();
                //u = cc.obterUsuarioPorEntidade(o);

                //retorno.setUsuario(u);
                //retorno.setEntidade(o);
                return o;//retorno;

            }catch (Exception e){
                e.printStackTrace();
            }

            return null;

        }

        @Override
        protected void onPostExecute(ItemInspecaoVO result) {

            final EditText txtNome = (EditText) findViewById(R.id.txtNome);
            //final Spinner spnEventos = (Spinner) findViewById(R.id.spnEventos);
            final Spinner spnArea = (Spinner) findViewById(R.id.spnAreas);
            final EditText txtPontMin = (EditText) findViewById(R.id.edtPontMinima);
            final EditText txtPontMax = (EditText) findViewById(R.id.edtPontMaxima);


            if(result != null){

                //EntidadeVO e = result.getEntidade();
                //UsuarioVO u = result.getUsuario();

                txtNome.setText(result.getNome());
                txtPontMin.setText(String.valueOf(result.getPontuacaoMinima().doubleValue()));
                txtPontMax.setText(String.valueOf(result.getPontuacaoMaxima().doubleValue()));


                try {
                    //seleciona o evento
                   // spnEventos.setSelection(((ArrayAdapter)spnEventos.getAdapter()).getPosition("[" + String.format("%05d", result.getEvento().getId()) + "] " + result.getEvento().getNome()));
                    spnArea.setSelection(((ArrayAdapter)spnArea.getAdapter()).getPosition(result.getArea().getNome()));

                }catch (Exception ex){
                    ex.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Erro ao carregar o registro! - " + ex.getMessage(), Toast.LENGTH_SHORT).show();

                }




            }
            else
            {

                Toast.makeText(getApplicationContext(), "Erro ao carregar o registro!", Toast.LENGTH_SHORT).show();
            }



            if (progress != null && progress.isShowing()) {
                progress.dismiss();
            }


        }


    }

    public void onRadioButtonClicked(View view) {



    }

    void salvar(){

        final EditText txtNome = (EditText) findViewById(R.id.txtNome);
        //final Spinner spnEventos = (Spinner) findViewById(R.id.spnEventos);
        final Spinner spnArea = (Spinner) findViewById(R.id.spnAreas);
        final EditText txtPontMin = (EditText) findViewById(R.id.edtPontMinima);
        final EditText txtPontMax = (EditText) findViewById(R.id.edtPontMaxima);



        //validacoes
        if( txtNome.getText().toString().trim().length()==0  ){
            Toast.makeText(getApplicationContext(), "Informe o nome!", Toast.LENGTH_SHORT).show();
            txtNome.requestFocus();
            return;
        }

        if(spnArea.getSelectedItem().toString().length() == 0 ||
                spnArea.getSelectedItem().toString() == TXT_AREA_DEFAULT ){
            Toast.makeText(getApplicationContext(), "Selecione a área!", Toast.LENGTH_SHORT).show();
            spnArea.requestFocus();
            return;
        }

        if( txtPontMax.getText().toString().trim().length()==0  ){
            Toast.makeText(getApplicationContext(), "Informe a pontuação máxima!", Toast.LENGTH_SHORT).show();
            txtPontMax.requestFocus();
            return;
        }

        if( txtPontMin.getText().toString().trim().length()==0  ){
            Toast.makeText(getApplicationContext(), "Informe a pontuação mínima!", Toast.LENGTH_SHORT).show();
            txtPontMin.requestFocus();
            return;
        }

        if( Double.parseDouble(txtPontMax.getText().toString()) == 0   ){
            Toast.makeText(getApplicationContext(), "Pontuação máxima deve ser maior que zero!", Toast.LENGTH_SHORT).show();
            txtPontMax.requestFocus();
            return;
        }

        if( Double.parseDouble(txtPontMax.getText().toString()) <= Double.parseDouble(txtPontMin.getText().toString())    ){
            Toast.makeText(getApplicationContext(), "Pontuação máxima deve ser superior à mínima!", Toast.LENGTH_SHORT).show();
            txtPontMax.requestFocus();
            return;
        }






        try {

            //editar ou novo?
            Bundle b = getIntent().getExtras();

            String[] paramns = new String[]{
                    b.getString("registro"),
                    txtNome.getText().toString().trim(),
                    null , //spnEventos.getSelectedItem().toString().substring(1, 6),
                    spnArea.getSelectedItem().toString(),
                    txtPontMin.getText().toString(),
                    txtPontMax.getText().toString()
            };



            new salvarTask().execute(paramns );

        }catch (Exception e)
        {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Erro: " + e.getMessage() , Toast.LENGTH_SHORT).show();

        }


    }

    class salvarTask extends AsyncTask<String, Integer, Boolean> {

        String errorMsg;

        @Override
        protected void onPreExecute() {
            inicializaProgressBar();
            progress.show();
        }

        @Override
        protected Boolean doInBackground(String... param) {

            boolean retorno=false;

            CadastrosControle cc = new CadastrosControle();

            try {

                ItemInspecaoVO o = new ItemInspecaoVO();
                o.setNome(param[1]);
             //   o.setEvento(cc.obterEventoPorId(Integer.parseInt(param[2])));
                o.setArea(  cc.obterAreaPorNome(param[3])   );
                o.setPontuacaoMinima(new BigDecimal(param[4]));
                o.setPontuacaoMaxima( new BigDecimal(param[5]));


                if(param[0] != null){
                    //editar
                    o.setId( Integer.parseInt(param[0]));
                    if(cc.editarItemInspecao(o) ){
                        retorno=true;
                    }
                }
                else
                {
                    //novo registro
                    if(cc.inserirItemInspecao(o)){
                        retorno=true;
                    }
                }



            }catch (Exception e){
                e.printStackTrace();
                errorMsg =  e.getMessage();
            }

            return retorno;


        }

        @Override
        protected void onPostExecute(Boolean result) {



            if (progress != null && progress.isShowing()) {
                progress.dismiss();
            }


            if(result){

                Toast.makeText(getApplicationContext(), "Alterações salvas com sucesso", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                setResult(Activity.RESULT_OK, intent);
                finish();

            }
            else
            {

                Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_SHORT).show();
            }




        }


    }


    class incluirArea  extends AsyncTask<String, Integer, Boolean> {

        String errorMsg;

        @Override
        protected void onPreExecute() {
            inicializaProgressBar();
            progress.show();
        }

        @Override
        protected Boolean doInBackground(String... param) {

            CadastrosControle cc = new CadastrosControle();

            try {

                AreaVO o = new AreaVO();
                o.setNome(param[0]);

                return cc.incluirArea(o);

            }catch (Exception e){
                e.printStackTrace();
                errorMsg =  e.getMessage();
            }

            return false;


        }

        @Override
        protected void onPostExecute(Boolean result) {



            if (progress != null && progress.isShowing()) {
                progress.dismiss();
            }

//
//            if(result){
//
//                Toast.makeText(getApplicationContext(), "Alterações salvas com sucesso", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent();
//                setResult(Activity.RESULT_OK, intent);
//                finish();
//
//            }
//            else
//            {
//
//                Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_SHORT).show();
//            }
//



        }


    }
}
