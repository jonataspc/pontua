package com.bsi.pontua;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class Menu2 extends AppCompatActivity {
/*

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu2);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        //recupera usuario
        final Bundle b = getIntent().getExtras();

        if(b==null){
            finish();
        }

        String usuario = b.getString("usuario");

        ((TextView) findViewById(R.id.txtUsuario)).setText(usuario);





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

        ((Button) findViewById(R.id.btnRelRkg)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Menu2.this, RelatorioRanking.class);
                myIntent.putExtras(b);
                startActivity(myIntent);
            }

        });

        ((Button) findViewById(R.id.btnRealizarAvaliacaoNFC)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Menu2.this, AvaliacaoNfc.class);
                myIntent.putExtras(b);
                startActivity(myIntent);
            }

        });




        //remover botoes de usuarios que nao tenham permissao!
        if(b.getString("perfil").equals("AVAL")){

            View myView = findViewById(R.id.btnCadastroUsuarios);
            ViewGroup parent = (ViewGroup) myView.getParent();
            parent.removeView(myView);

            myView = findViewById(R.id.btnCadastroEventos);
            parent = (ViewGroup) myView.getParent();
            parent.removeView(myView);

            myView = findViewById(R.id.btnCadastroEntidades);
            parent = (ViewGroup) myView.getParent();
            parent.removeView(myView);

            myView = findViewById(R.id.btnCadastroItensInspecao);
            parent = (ViewGroup) myView.getParent();
            parent.removeView(myView);

            myView = findViewById(R.id.btnConsultarAvaliacoes);
            parent = (ViewGroup) myView.getParent();
            parent.removeView(myView);

            myView = findViewById(R.id.btnRelRkg);
            parent = (ViewGroup) myView.getParent();
            parent.removeView(myView);

        }else if(b.getString("perfil").equals("ENT")){

            View myView = findViewById(R.id.btnCadastroUsuarios);
            ViewGroup parent = (ViewGroup) myView.getParent();
            parent.removeView(myView);

            myView = findViewById(R.id.btnCadastroEventos);
            parent = (ViewGroup) myView.getParent();
            parent.removeView(myView);

            myView = findViewById(R.id.btnCadastroEntidades);
            parent = (ViewGroup) myView.getParent();
            parent.removeView(myView);

            myView = findViewById(R.id.btnCadastroItensInspecao);
            parent = (ViewGroup) myView.getParent();
            parent.removeView(myView);

            myView = findViewById(R.id.btnRelRkg);
            parent = (ViewGroup) myView.getParent();
            parent.removeView(myView);

            myView = findViewById(R.id.btnRealizarAvaliacao);
            parent = (ViewGroup) myView.getParent();
            parent.removeView(myView);

            myView = findViewById(R.id.btnRealizarAvaliacaoNFC);
            parent = (ViewGroup) myView.getParent();
            parent.removeView(myView);
        }





    }
*/
}
