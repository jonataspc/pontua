package com.bsi.pontua;

import android.app.ProgressDialog;
import android.content.Intent;
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
import java.util.Date;
import java.util.List;

import controle.CadastrosControle;
import utils.DecimalDigitsInputFilter;
import utils.Utils;
import vo.AvaliacaoVO;
import vo.EventoVO;
import vo.ItemInspecaoVO;
import vo.UsuarioVO;

public class AvaliacaoNfcLer extends AppCompatActivity {



    ProgressDialog progress;
    Bundle b=null;

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

    AvaliacaoVO objAvaliacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avaliacao_nfc_ler);




        Intent intent = getIntent();



        //recupera obj avaliacao
        objAvaliacao = (AvaliacaoVO) intent.getSerializableExtra("objAvaliacaoVO");

        //labels
        final TextView lblEvento = (TextView) findViewById(R.id.lblEvento);
        lblEvento.setText(objAvaliacao.getItemInspecao().getEvento().getNome());

        final TextView lblItem = (TextView) findViewById(R.id.lblItem);
        lblItem.setText(objAvaliacao.getItemInspecao().getNome());

        final TextView lblValor = (TextView) findViewById(R.id.lblValor);
        lblValor.setText(String.valueOf(objAvaliacao.getPontuacao().doubleValue()));


        startLeitura();

    }

    void writeLog(String txt){

        String dthr;
        Date now = new Date();

         dthr = "[" + Utils.formatarHora(now) + "]";

        final TextView txtLog = (TextView) findViewById(R.id.lblLog);
        txtLog.setText(dthr + " " + txt + "\n" + txtLog.getText());

    }

    void startLeitura(){

        writeLog("Aguardando leituras...");


        //start nfc

    }




    void lancarPontuacao(int idEntidade) {

        //efetua lancamento
        Integer[] paramns = new Integer[]{ idEntidade};

       new efetuarLancamentoTask().execute(paramns);
    }

    class efetuarLancamentoTask extends AsyncTask<Integer, Integer, Boolean> {

        @Override
        protected void onPreExecute() {
            inicializaProgressBar();
            progress.show();
        }

        @Override
        protected Boolean doInBackground(Integer... param) {

            CadastrosControle cc = new CadastrosControle();

            try {

                Boolean retorno;

                objAvaliacao.setEntidade( cc.obterEntidadePorId(param[0]) );

                retorno = cc.inserirAvaliacao(objAvaliacao);
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


            String strMsg;

            if(result){
                strMsg = "Lanç. efetuado (" + objAvaliacao.getEntidade().getNome()  + ") ";
            }else{
                strMsg = "Erro (" + objAvaliacao.getEntidade().getNome()  + ") ";
            }

            writeLog(strMsg);
            Toast.makeText(getApplicationContext(), strMsg, Toast.LENGTH_SHORT).show();





        }
    }



}
