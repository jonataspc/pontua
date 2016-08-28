package com.bsi.pontua;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;

import controle.CadastrosControle;
import utils.Conexao;
import vo.UsuarioVO;

public class LoginConfig extends AppCompatActivity {

    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_config);

         //new testexx().execute("");
        final Button btnSalvarLoginConfig = (Button) findViewById(R.id.btnSalvarLoginConfig);
        final Button btnRetornarLoginConfig = (Button) findViewById(R.id.btnRetornarLoginConfig);


        btnRetornarLoginConfig.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();

            }
        });

        btnSalvarLoginConfig.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new salvarDadosTask().execute();;
            }
        });


        final EditText edtServidor = (EditText) findViewById(R.id.edtServidor);

        final CheckBox ckSSl = (CheckBox) findViewById(R.id.ckSsl);

        SharedPreferences settings = getSharedPreferences("settings", 0);
        edtServidor.setText(settings.getString("ServerIP", "mysql.infosgi.com.br:3306"));
        ckSSl.setChecked(settings.getBoolean("UsarSSL", false));


        edtServidor.requestFocus();

    }


    void inicializaProgressBar() {

        if (progress == null) {

            progress = new ProgressDialog(LoginConfig.this);
            progress.setTitle("");
            progress.setMessage("Por favor aguarde...");
            progress.setIndeterminate(true);
            progress.setCancelable(false);

        }
    }


    class salvarDadosTask extends AsyncTask<String, String, Boolean> {

        String txtMsg;

        @Override
        protected void onPreExecute() {
            inicializaProgressBar();
            progress.show();

            //salva o servidor
            SharedPreferences settings = getSharedPreferences("settings", 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("ServerIP", ((EditText) findViewById(R.id.edtServidor)).getText().toString());
            editor.putBoolean("UsarSSL", ((CheckBox) findViewById(R.id.ckSsl)).isChecked());
            editor.commit();


        }

        @Override
        protected Boolean doInBackground(String... param) {
            //valida dos dados para servidor MySQL
            try {
                Connection conn = Conexao.obterConexao();
                txtMsg = "Conexão MySQL estabelecida com sucesso!\nDados foram salvos com sucesso!";
                return true;
            }catch(Exception e){
                txtMsg = "Ocorreu erro de conexão ao servidor: \n" + e.getMessage();
                return false;
            }
        }


        @Override
        protected void onPostExecute(Boolean result) {
            progress.dismiss();



            if(result){
                //sucesso
                Toast.makeText(getApplicationContext(), txtMsg, Toast.LENGTH_LONG).show();

                //volta
                finish();
            }else{

                new android.app.AlertDialog.Builder(LoginConfig.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("")
                        .setMessage(txtMsg)
                        .setNegativeButton("Ok", null)
                        .show();
            }
        }


    }


}
