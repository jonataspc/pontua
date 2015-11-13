package com.bsi.pontua;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class Menu2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu2);


        //recupera usuario
        Bundle b = getIntent().getExtras();

        if(b==null){
            finish();
        }

        String usuario = b.getString("usuario");

        ((TextView) findViewById(R.id.txtUsuario)).setText(usuario);


        ((Button) findViewById(R.id.btnCadastroUsuarios)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Menu2.this, CadastroUsuarios.class);
                Bundle b = new Bundle();
                //b.putString("usuario", edtUsuario.getText().toString().trim());
                myIntent.putExtras(b);
                startActivity(myIntent);
            }

        });

        ((Button) findViewById(R.id.btnCadastroEventos)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Menu2.this, CadastroEventos.class);
                Bundle b = new Bundle();
                //b.putString("usuario", edtUsuario.getText().toString().trim());
                myIntent.putExtras(b);
                startActivity(myIntent);
            }

        });

        ((Button) findViewById(R.id.btnCadastroEntidades)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Menu2.this, CadastroEntidades.class);
                Bundle b = new Bundle();
                //b.putString("usuario", edtUsuario.getText().toString().trim());
                myIntent.putExtras(b);
                startActivity(myIntent);
            }

        });

        ((Button) findViewById(R.id.btnCadastroItensInspecao)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Menu2.this, CadastroItensInspecao.class);
                Bundle b = new Bundle();
                //b.putString("usuario", edtUsuario.getText().toString().trim());
                myIntent.putExtras(b);
                startActivity(myIntent);
            }

        });

/*

        View myView = findViewById(R.id.button);
        ViewGroup parent = (ViewGroup) myView.getParent();
        parent.removeView(myView);

        myView = findViewById(R.id.button2);
        parent = (ViewGroup) myView.getParent();
        parent.removeView(myView);
*/



    }
}
