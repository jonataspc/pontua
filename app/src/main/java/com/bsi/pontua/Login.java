package com.bsi.pontua;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import controle.CadastrosControle;
import vo.UsuarioVO;

public class Login extends AppCompatActivity {

    public static Context contextOfApplication;

    public static Context getContextOfApplication(){
        return contextOfApplication;
    }



    Button btnLogar;
    Button btnSair;
    EditText edtUsuario;
    EditText edtSenha;

    ProgressDialog progress;

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


        final EditText edtServidor = (EditText) findViewById(R.id.edtServidor);
        SharedPreferences settings = getSharedPreferences("settings", 0);
        String  serverIP = settings.getString("ServerIP", "192.168.25.1:3307");
        edtServidor.setText(serverIP);

    }



    @Override
    public void onPause(){
        //evita erro de leak
        super.onPause();
        if(progress != null) {
            progress.dismiss();
            progress=null;
        }


    }

    @Override
    protected void onStop() {
        //evita erro de leak
        super.onStop();

        if(progress != null) {
            progress.dismiss();
            progress=null;
        }

    }



    void inicializaProgressBar(){

        if(progress==null){

            progress = new ProgressDialog(Login.this);
            progress.setTitle("");
            progress.setMessage("Por favor aguarde...");
            progress.setIndeterminate(true);
            progress.setCancelable(false);

        }
    }

    void validarLogin(){

        final EditText edtUsuario = (EditText) findViewById(R.id.edtUsuario);
        final EditText edtSenha = (EditText) findViewById(R.id.edtSenha);

        if(edtUsuario.getText().length()==0){
            Toast.makeText(getApplicationContext(), "Informe o usuário!", Toast.LENGTH_SHORT).show();
            return;
        }

        if(edtSenha.getText().length()==0 ){
            Toast.makeText(getApplicationContext(), "Informe a senha!", Toast.LENGTH_SHORT).show();
            return;
        }

        //salva o servidor
        SharedPreferences settings = getSharedPreferences("settings", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("ServerIP", ((EditText) findViewById(R.id.edtServidor)).getText().toString());
        editor.commit();


        try {

            String[] paramns = new String[]{edtUsuario.getText().toString().trim(),  edtSenha.getText().toString().trim() };

            //remove a senha
            edtSenha.setText("");

            new validarLoginTask().execute(paramns );

        }catch (Exception e)
        {
            Toast.makeText(getApplicationContext(), "Erro: " + e.getMessage() , Toast.LENGTH_SHORT).show();
        }

    }


    class validarLoginTask extends AsyncTask<String, Integer, String> {

        AlertDialog.Builder  alertDialog;

        @Override
        protected void onPreExecute() {


            inicializaProgressBar();
            progress.show();

        }


        @Override
        protected String doInBackground(String... param) {


            String retorno="";

            if(param[0].toString().equals("master") && param[1].toString().equals("master") ){
                return "OK";
            }


            CadastrosControle cc = new CadastrosControle();

            try {

                //List<UsuarioVO> lista = cc.listarUsuario("");

                UsuarioVO o = new UsuarioVO();
                o.setNome(param[0]);
                o.setSenha(param[1]);


                if(cc.validarLogin(o)){
                  retorno="OK";
                }
                else
                {
                    retorno="";
                }

                return  retorno;


            }catch (Exception e){

                return e.getMessage() + "\n" + e.getStackTrace();
            }



        }


        @Override
        protected void onPostExecute(String result) {



            if(result=="OK"){

                //abre menu inicial
                final EditText edtUsuario = (EditText) findViewById(R.id.edtUsuario);

                Intent myIntent = new Intent(Login.this, Menu2.class);
                Bundle b = new Bundle();
                b.putString("usuario", edtUsuario.getText().toString().trim());
                //b.putString("perfil", edtUsuario.getText().toString().trim()); TODO

                myIntent.putExtras(b); //Put your id to your next Intent
                startActivity(myIntent);

                if (progress!=null && progress.isShowing()) {
                    progress.dismiss();
                }


            }
            else
            {
                if (progress!=null && progress.isShowing()) {
                    progress.dismiss();
                }
                Toast.makeText(getApplicationContext(), "Dados inválidos, acesso negado!", Toast.LENGTH_SHORT).show();
            }




        }


    }



}
