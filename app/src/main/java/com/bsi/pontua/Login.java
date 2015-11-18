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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;


//TODO LIST
//Bug - atraves de lanc. por NFC permite pontuar entidades que nao sejam do evento cadastrado

public class Login extends AppCompatActivity {

    public static Context contextOfApplication;
    Button btnLogar;
    Button btnSair;
    EditText edtUsuario;
    EditText edtSenha;
    ProgressDialog progress;

    private static String SERVICE_URL=null;


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
        SharedPreferences settings = getSharedPreferences("settings", 0);
        String serverIP = settings.getString("ServerIP", "192.168.25.1:3307");
        edtServidor.setText(serverIP);

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
        editor.commit();




        //login rest
        String httphost=((EditText) findViewById(R.id.edtServidor)).getText().toString().replace(":3307","");

        SERVICE_URL = "http://" + httphost + ":8080/PontuaWeb/api/v1/login";

        postData();

  /*      try {

            String[] paramns = new String[]{edtUsuario.getText().toString().trim(), edtSenha.getText().toString().trim()};

            //remove a senha
            edtSenha.setText("");

            new validarLoginTask().execute(paramns);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Erro: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }*/

    }



    public void postData() {

        WebServiceTask wst = new WebServiceTask(WebServiceTask.POST_TASK, this, "Posting data...");


        final EditText edtUsuario = (EditText) findViewById(R.id.edtUsuario);
        final EditText edtSenha = (EditText) findViewById(R.id.edtSenha);


        wst.addNameValuePair("nome", edtUsuario.getText().toString().trim());
        wst.addNameValuePair("senha",  edtSenha.getText().toString().trim());


        // the passed String is the URL we will POST to
        wst.execute(new String[] { SERVICE_URL });

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

        private ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

        private ProgressDialog pDlg = null;

        public WebServiceTask(int taskType, Context mContext, String processMessage) {

            this.taskType = taskType;
            this.mContext = mContext;
            this.processMessage = processMessage;
        }

        public void addNameValuePair(String name, String value) {

            params.add(new BasicNameValuePair(name, value));
        }



        @Override
        protected void onPreExecute() {
            inicializaProgressBar();
            progress.show();
        }


        private  String convertInputStreamToString(InputStream inputStream) throws IOException{
            BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
            String line = "";
            String result = "";
            while((line = bufferedReader.readLine()) != null)
                result += line;

            inputStream.close();
            return result;

        }

        protected UsuarioVO doInBackground(String... urls) {

            String url = urls[0];
            UsuarioVO result = null;

            HttpResponse response = doResponse(url);



           if (response == null) {
                return result;
            } else {

                try {
                    InputStream inputStream = null;

                    if(response.getEntity()==null){
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
        protected void onPostExecute(UsuarioVO response) {


            try {

                //JSONObject jso = new JSONObject(response);
                //String nome = jso.getString("firstName");

                //realiza login!




            } catch (Exception e) {
                Log.e("", e.getLocalizedMessage(), e);
            }



            try {
                pDlg.dismiss();
            }catch (Exception e){

            }



            if (progress != null && progress.isShowing()) {
                progress.dismiss();
            }



        }

        // Establish connection and socket (data retrieval) timeouts
        private HttpParams getHttpParams() {

            HttpParams htpp = new BasicHttpParams();

            HttpConnectionParams.setConnectionTimeout(htpp, CONN_TIMEOUT);
            HttpConnectionParams.setSoTimeout(htpp, SOCKET_TIMEOUT);

            return htpp;
        }

        private HttpResponse doResponse(String url) {

            // Use our connection and data timeouts as parameters for our
            // DefaultHttpClient
            HttpClient httpclient = new DefaultHttpClient(getHttpParams());

            HttpResponse response = null;

            try {
                switch (taskType) {

                    case POST_TASK:
                        HttpPost httppost = new HttpPost(url);
                        // Add parameters
                        //httppost.setEntity(new UrlEncodedFormEntity(params));

                        String json = "";

                        // 3. build jsonObject
                        JSONObject jsonObject = new JSONObject();

                        for(NameValuePair x: params){
                            jsonObject.accumulate(x.getName(),  x.getValue() );
                        }

                        //jsonObject.accumulate("name", person.getName());
                        //jsonObject.accumulate("country", person.getCountry());
                        //jsonObject.accumulate("twitter", person.getTwitter());

                        // 4. convert JSONObject to JSON to String
                        json = jsonObject.toString();

                        // ** Alternative way to convert Person object to JSON string usin Jackson Lib
                        // ObjectMapper mapper = new ObjectMapper();
                        // json = mapper.writeValueAsString(person);

                        // 5. set json to StringEntity
                        StringEntity se = new StringEntity(json);

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




/*

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

                Intent myIntent = new Intent(Login.this, Menu2.class);
                Bundle b = new Bundle();

                b.putString("usuario", result.getNome());
                b.putString("perfil", result.getNivelAcesso());
                b.putInt("id", result.getId());

                if(result.getEntidade()==null){
                    b.putInt("id_entidade", -1);
                }else {
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


    }
*/


}
