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
        final Bundle b = getIntent().getExtras();

        if(b==null){
            finish();
        }

        String usuario = b.getString("usuario");

        ((TextView) findViewById(R.id.txtUsuario)).setText(usuario);

        //TODO: remover botoes de usuarios que nao tenham permissao!

        ((Button) findViewById(R.id.btnCadastroUsuarios)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Menu2.this, CadastroUsuarios.class);
                myIntent.putExtras(b);
                startActivity(myIntent);
            }

        });

        ((Button) findViewById(R.id.btnCadastroEventos)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Menu2.this, CadastroEventos.class);
                myIntent.putExtras(b);
                startActivity(myIntent);
            }

        });

        ((Button) findViewById(R.id.btnCadastroEntidades)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Menu2.this, CadastroEntidades.class);
                myIntent.putExtras(b);
                startActivity(myIntent);
            }

        });

        ((Button) findViewById(R.id.btnCadastroItensInspecao)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Menu2.this, CadastroItensInspecao.class);
                myIntent.putExtras(b);
                startActivity(myIntent);
            }

        });

        ((Button) findViewById(R.id.btnRealizarAvaliacao)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Menu2.this, Avaliacao.class);
                myIntent.putExtras(b);
                startActivity(myIntent);
            }

        });

        ((Button) findViewById(R.id.btnConsultarAvaliacoes)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Menu2.this, ConsultarAvaliacoes.class);
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
