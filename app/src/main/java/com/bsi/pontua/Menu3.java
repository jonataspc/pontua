package com.bsi.pontua;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import utils.Utils;

public class Menu3 extends AppCompatActivity {


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_menu3, menu);



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


            case R.id.action_exit:
                finish();
                return true;

            case R.id.action_about:
                    //sobre

                final Context context = this;

                int versionCode = BuildConfig.VERSION_CODE;
                String versionName = BuildConfig.VERSION_NAME;


                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("Sobre"); //Set Alert dialog title here
                alert.setMessage("Permitido o uso sem finalidade comercial\n\nVersão: " + versionName);
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {


                    }
                });
                AlertDialog alertDialog = alert.create();
                alertDialog.show();

                return true;




            default:
                break;
        }

        return false;
    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu3);


        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

//        //recupera usuario
//        final Bundle b = getIntent().getExtras();
//
//        if(b==null){
//            finish();
//        }






        ((TextView) findViewById(R.id.txtUsuario)).setText(Utils.usuarioCorrente.getNome().toUpperCase());


        String perfil=null;

        switch (Utils.usuarioCorrente.getNivelAcesso()){
            case Administrador:
                perfil = "Administrador";
                break;

            case Avaliador:
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

            case Entidade:
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
                startActivity(myIntent);
            }
        };


        ((Button) findViewById(R.id.btnCadUsuarios)).setOnClickListener(m01);



        //entidades
        View.OnClickListener m02 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Menu3.this, CadastroEntidades.class);
                startActivity(myIntent);
            }
        };


        ((Button) findViewById(R.id.btnCadEntidades)).setOnClickListener(m02);


        //eventos
        View.OnClickListener m03 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Menu3.this, CadastroEventos.class);
                startActivity(myIntent);
            }
        };


        ((Button) findViewById(R.id.btnCadEventos)).setOnClickListener(m03);


        //itens de inspecao
        View.OnClickListener m04 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Menu3.this, CadastroItensInspecao.class);
                startActivity(myIntent);
            }
        };


        ((Button) findViewById(R.id.btnCadItensPontuacao)).setOnClickListener(m04);

        //lancar pontuacao
        View.OnClickListener m05 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Menu3.this, Avaliacao.class);
                startActivity(myIntent);
            }
        };


        ((Button) findViewById(R.id.btnRealizarAvaliacao)).setOnClickListener(m05);

        //NFC
        View.OnClickListener m06 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Menu3.this, AvaliacaoNfc.class);
                startActivity(myIntent);
            }
        };


        ((Button) findViewById(R.id.btnAvaliacaoNFC)).setOnClickListener(m06);

        //Consultar Avaliacoes
        View.OnClickListener m07 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Menu3.this, ConsultarAvaliacoes.class);
                startActivity(myIntent);
            }
        };


        ((Button) findViewById(R.id.btnConsultarAvaliacao)).setOnClickListener(m07);

        //relatorios
        View.OnClickListener m08 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Menu3.this, Relatorios.class);
                startActivity(myIntent);
            }
        };


        ((Button) findViewById(R.id.btnRelatorios)).setOnClickListener(m08);

    }
}
