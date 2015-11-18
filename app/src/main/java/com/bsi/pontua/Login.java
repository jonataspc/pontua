package com.bsi.pontua;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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
        final EditText edtUsuario = (EditText) findViewById(R.id.edtUsuario);
        final EditText edtSenha = (EditText) findViewById(R.id.edtSenha);

        SharedPreferences settings = getSharedPreferences("settings", 0);
        edtServidor.setText(settings.getString("ServerIP", "192.168.25.1:3307"));
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

        //salva o servidor
        SharedPreferences settings = getSharedPreferences("settings", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("ServerIP", ((EditText) findViewById(R.id.edtServidor)).getText().toString());
        editor.putString("Usuario", edtUsuario.getText().toString().trim());
        editor.commit();


        //login restfull
        String httphost = ((EditText) findViewById(R.id.edtServidor)).getText().toString().replace(":3307", "");

        SERVICE_URL = "http://" + httphost + ":8080/PontuaWeb/api/v1/login";

        WebServiceTask wst = new WebServiceTask(WebServiceTask.POST_TASK, this, "Posting data...");

        UsuarioVO o = new UsuarioVO();
        o.setNome(edtUsuario.getText().toString().trim());
        o.setSenha(edtSenha.getText().toString().trim());
        wst.setUsuario(o);

        //remove a senha
        edtSenha.setText("");

        // the passed String is the URL we will POST to
        wst.execute(new String[]{SERVICE_URL});

    }

    private class WebServiceTask extends AsyncTask<String, Integer, UsuarioVO> {

        public static final int POST_TASK = 1;
        public static final int GET_TASK = 2;

        private static final String TAG = "PontuaTag";

        // connection timeout, in milliseconds (waiting to connect)
        private static final int CONN_TIMEOUT = 3000;

        // socket timeout, in milliseconds (waiting for data)
        private static final int SOCKET_TIMEOUT = 5000;

        private int taskType = GET_TASK;
        private Context mContext = null;
        private String processMessage = "Processing...";

        private UsuarioVO objUsuario;

        public WebServiceTask(int taskType, Context mContext, String processMessage) {

            this.taskType = taskType;
            this.mContext = mContext;
            this.processMessage = processMessage;
        }

        public void setUsuario(UsuarioVO u) {
            objUsuario = u;
        }

        @Override
        protected void onPreExecute() {
            inicializaProgressBar();
            progress.show();
        }

        private String convertInputStreamToString(InputStream inputStream) throws IOException {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            String result = "";
            while ((line = bufferedReader.readLine()) != null)
                result += line;

            inputStream.close();
            return result;

        }

        protected UsuarioVO doInBackground(String... urls) {

            String url = urls[0];
            UsuarioVO result = null;

            //realiza chamada http
            HttpResponse response = doResponse(url);

            if (response == null) {
                return result;
            } else {

                try {
                    InputStream inputStream = null;

                    if (response.getEntity() == null) {
                        return null;
                    }

                    inputStream = response.getEntity().getContent();
                    String strJson = convertInputStreamToString(inputStream);

                    //converte string json em object
                    Gson gson = new Gson();
                    result = gson.fromJson(strJson, UsuarioVO.class);

                } catch (IllegalStateException e) {
                    Log.e(TAG, e.getLocalizedMessage(), e);

                } catch (IOException e) {
                    Log.e(TAG, e.getLocalizedMessage(), e);
                }

            }

            return result;
        }

        @Override
        protected void onPostExecute(UsuarioVO result) {

            if (result != null) {

                //abre menu inicial
                Intent myIntent = new Intent(Login.this, Menu2.class);
                Bundle b = new Bundle();

                b.putString("usuario", result.getNome());
                b.putString("perfil", result.getNivelAcesso());
                b.putInt("id", result.getId());

                if (result.getEntidade() == null) {
                    b.putInt("id_entidade", -1);
                } else {
                    b.putInt("id_entidade", result.getEntidade().getId());
                }

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

        private HttpParams getHttpParams() {

            HttpParams htpp = new BasicHttpParams();

            HttpConnectionParams.setConnectionTimeout(htpp, CONN_TIMEOUT);
            HttpConnectionParams.setSoTimeout(htpp, SOCKET_TIMEOUT);

            return htpp;
        }

        private HttpResponse doResponse(String url) {

            HttpClient httpclient = new DefaultHttpClient(getHttpParams());
            HttpResponse response = null;

            try {
                switch (taskType) {

                    case POST_TASK:
                        HttpPost httppost = new HttpPost(url);

                        //converte object em json
                        Gson gson = new Gson();
                        String strObjJson = gson.toJson(this.objUsuario, UsuarioVO.class);

                        // 5. set json to StringEntity
                        StringEntity se = new StringEntity(strObjJson);

                        // 6. set httpPost Entity
                        httppost.setEntity(se);

                        // 7. Set some headers to inform server about the type of the content
                        httppost.setHeader("Accept", "application/json");
                        httppost.setHeader("Content-type", "application/json");

                        response = httpclient.execute(httppost);
                        break;
                    case GET_TASK:
                        HttpGet httpget = new HttpGet(url);
                        response = httpclient.execute(httpget);
                        break;
                }
            } catch (Exception e) {
                Log.e(TAG, e.getLocalizedMessage(), e);
                e.printStackTrace();
            }

            return response;
        }

        private String inputStreamToString(InputStream is) {

            String line = "";
            StringBuilder total = new StringBuilder();

            // Wrap a BufferedReader around the InputStream
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));

            try {
                // Read response until the end
                while ((line = rd.readLine()) != null) {
                    total.append(line);
                }
            } catch (IOException e) {
                Log.e(TAG, e.getLocalizedMessage(), e);
            }

            // Return full string
            return total.toString();
        }

    }

}
