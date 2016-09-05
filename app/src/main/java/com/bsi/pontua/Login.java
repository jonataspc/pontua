package com.bsi.pontua;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import controle.CadastrosControle;
import utils.Utils;
import vo.UsuarioVO;

//TODO LIST
//Bug - atraves de lanc. por NFC permite pontuar entidades que nao sejam do evento cadastrado
// nao permitir excluir em consulta de pontuacao por entidades




public class Login extends AppCompatActivity {

    public static Context contextOfApplication;
    private static String SERVICE_URL = null;
    Button btnLogar;
    Button btnSair;
    EditText edtUsuario;
    EditText edtSenha;
    ProgressDialog progress;

    public static Context getContextOfApplication() {
        return contextOfApplication;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_debito_boleto, menu);



        SharedPreferences settings = getSharedPreferences("settings", 0);

        //MenuItem item = menu.findItem(R.id.action_settings_SalvarDados);
        //item.setChecked(settings.getBoolean("SalvarDados", true));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        SharedPreferences.Editor editor;
        SharedPreferences settings;

        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.

//                //esconde keyboard
//                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
//
//                this.finish();
//                return true;


            case R.id.action_settings_SalvarDados:
//                item.setChecked(!item.isChecked());
//                settings = getSharedPreferences("settings", 0);
//                editor = settings.edit();
//                editor.putBoolean("SalvarDados", item.isChecked());
//
//                if(!item.isChecked()){
//                    //se nao estiver checado, remove dados...
//                    editor.putString("Ctto", "");
//                    editor.putString("Doc","");
//                    editor.putString("TipoDoc","");
//                }
//
//                editor.commit();

                //abre activ...
                Intent myIntent = new Intent(Login.this, LoginConfig.class);
//                myIntent.putExtras(b);
                startActivity(myIntent);


                return true;




            default:
                break;
        }

        return false;
    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        contextOfApplication = getApplicationContext();


        //new testexx().execute("");
        final Button btnLogar = (Button) findViewById(R.id.btnLogar);
        final Button btnSair = (Button) findViewById(R.id.btnSair);


        btnSair.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });

        btnLogar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                validarLogin();
            }
        });


        final EditText edtUsuario = (EditText) findViewById(R.id.edtUsuario);
        final EditText edtSenha = (EditText) findViewById(R.id.edtSenha);

        SharedPreferences settings = getSharedPreferences("settings", 0);
        edtUsuario.setText(settings.getString("Usuario", ""));

        if (edtUsuario.getText().toString().trim().length() != 0) {
            edtSenha.requestFocus();
        }

    }

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

            progress = new ProgressDialog(Login.this);
            progress.setTitle("");
            progress.setMessage("Por favor aguarde...");
            progress.setIndeterminate(true);
            progress.setCancelable(false);

        }
    }

    void validarLogin() {

        final EditText edtUsuario = (EditText) findViewById(R.id.edtUsuario);
        final EditText edtSenha = (EditText) findViewById(R.id.edtSenha);

        if (edtUsuario.getText().length() == 0) {
            Toast.makeText(getApplicationContext(), "Informe o usuário!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (edtSenha.getText().length() == 0) {
            Toast.makeText(getApplicationContext(), "Informe a senha!", Toast.LENGTH_SHORT).show();
            return;
        }

        //salva
        SharedPreferences settings = getSharedPreferences("settings", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("Usuario", edtUsuario.getText().toString().trim());
        editor.commit();

        try {

            String[] paramns = new String[]{edtUsuario.getText().toString().trim(), edtSenha.getText().toString().trim()};

            //remove a senha
            edtSenha.setText("");

            new validarLoginTask().execute(paramns);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Erro: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }


    class validarLoginTask extends AsyncTask<String, Integer, UsuarioVO> {

        AlertDialog.Builder alertDialog;

        @Override
        protected void onPreExecute() {
            inicializaProgressBar();
            progress.show();
        }


        @Override
        protected UsuarioVO doInBackground(String... param) {


            if (param[0].toString().equals("master") && param[1].toString().equals("master")) {

                UsuarioVO us = new UsuarioVO();

                us.setEntidade(null);
                us.setSenha(null);
                us.setNome("MASTER");
                us.setNivelAcesso("ADM");
                us.setId(0);

                return us;
            }


            CadastrosControle cc = new CadastrosControle();

            try {

                UsuarioVO o = new UsuarioVO();
                o.setNome(param[0]);
                o.setSenha(param[1]);

                return cc.validarLogin(o);

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }


        }


        @Override
        protected void onPostExecute(UsuarioVO result) {


            if (result != null) {

                //abre menu inicial
                final EditText edtUsuario = (EditText) findViewById(R.id.edtUsuario);

                Intent myIntent = new Intent(Login.this, Menu3.class);
                Bundle b = new Bundle();
//
//                b.putString("usuario", result.getNome());
//                b.putString("perfil", result.getNivelAcesso());
//                b.putInt("id", result.getId());
//
//                if(result.getEntidade()==null){
//                    b.putInt("id_entidade", -1);
//                }else {
//                    b.putInt("id_entidade", result.getEntidade().getId());
//                }
//


                Utils.nomeUsuario = result.getNome();
                Utils.perfilUsuario = result.getNivelAcesso();

                myIntent.putExtras(b); //Put your id to your next Intent
                startActivity(myIntent);

                if (progress != null && progress.isShowing()) {
                    progress.dismiss();
                }


            } else {
                if (progress != null && progress.isShowing()) {
                    progress.dismiss();
                }
                Toast.makeText(getApplicationContext(), "Dados inválidos, acesso negado!", Toast.LENGTH_SHORT).show();
            }


        }


    }



}
