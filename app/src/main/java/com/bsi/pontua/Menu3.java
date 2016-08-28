package com.bsi.pontua;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

public class Menu3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu3);


        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        //recupera usuario
        final Bundle b = getIntent().getExtras();

        if(b==null){
            finish();
        }

        String usuario = b.getString("usuario");

        ((TextView) findViewById(R.id.txtUsuario)).setText(usuario.toUpperCase());


        String perfil=null;

        switch (b.getString("perfil")){
            case "ADM":
                perfil = "Administrador";
                break;

            case "AVAL":
                perfil = "Avaliador";

                //remove todos exceto
                //realizar avaliacao 5
                //avaliacao por nfc 6
                ((LinearLayout) findViewById(R.id.m1)).setVisibility(View.GONE);
                ((LinearLayout) findViewById(R.id.m2)).setVisibility(View.GONE);
                ((LinearLayout) findViewById(R.id.m3)).setVisibility(View.GONE);
                ((LinearLayout) findViewById(R.id.m4)).setVisibility(View.GONE);
                ((LinearLayout) findViewById(R.id.m5)).setVisibility(View.VISIBLE);
                ((LinearLayout) findViewById(R.id.m6)).setVisibility(View.VISIBLE);
                ((LinearLayout) findViewById(R.id.m7)).setVisibility(View.GONE);
                ((LinearLayout) findViewById(R.id.m8)).setVisibility(View.GONE);


                break;

            case "ENT":
                perfil = "Entidade";

                //remove todos exceto
                //consultar avaliacoes
                ((LinearLayout) findViewById(R.id.m1)).setVisibility(View.GONE);
                ((LinearLayout) findViewById(R.id.m2)).setVisibility(View.GONE);
                ((LinearLayout) findViewById(R.id.m3)).setVisibility(View.GONE);
                ((LinearLayout) findViewById(R.id.m4)).setVisibility(View.GONE);
                ((LinearLayout) findViewById(R.id.m5)).setVisibility(View.GONE);
                ((LinearLayout) findViewById(R.id.m6)).setVisibility(View.GONE);
                ((LinearLayout) findViewById(R.id.m7)).setVisibility(View.VISIBLE);
                ((LinearLayout) findViewById(R.id.m8)).setVisibility(View.GONE);



                break;
        }

        ((TextView) findViewById(R.id.txtPerfil)).setText(perfil);



        //insere acoes do menu

        //usuarios
         View.OnClickListener m01 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Menu3.this, CadastroUsuarios.class);
                myIntent.putExtras(b);
                startActivity(myIntent);
            }
        };

        ((ImageButton) findViewById(R.id.imgCadUsuarios)).setOnClickListener(m01);
        ((Button) findViewById(R.id.btnCadUsuarios)).setOnClickListener(m01);



    }
}
