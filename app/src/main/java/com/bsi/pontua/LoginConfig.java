package com.bsi.pontua;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginConfig extends AppCompatActivity {

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
                validarLogin();
            }
        });


        final EditText edtServidor = (EditText) findViewById(R.id.edtServidor);

        SharedPreferences settings = getSharedPreferences("settings", 0);
        edtServidor.setText(settings.getString("ServerIP", "mysql.infosgi.com.br:3306"));
        edtServidor.requestFocus();

    }


    void validarLogin() {


        //salva o servidor
        SharedPreferences settings = getSharedPreferences("settings", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("ServerIP", ((EditText) findViewById(R.id.edtServidor)).getText().toString());
        editor.commit();


        Toast.makeText(getApplicationContext(), "Dados salvos com sucesso!", Toast.LENGTH_SHORT).show();

        //volta
        finish();


    }
}
