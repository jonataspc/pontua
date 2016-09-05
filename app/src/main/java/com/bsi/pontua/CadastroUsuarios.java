package com.bsi.pontua;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;

import java.util.List;
import controle.CadastrosControle;
import utils.Utils;
import vo.UsuarioVO;

public class CadastroUsuarios extends AppCompatActivity {

    ProgressDialog progress;

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
            progress = new ProgressDialog(CadastroUsuarios.this);
            progress.setTitle("");
            progress.setMessage("Aguarde...");
            progress.setIndeterminate(true);
            progress.setCancelable(false);
        }
    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuarios);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Lookup the swipe container view
        final SwipeRefreshLayout swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                //atualiza
                popularSpinnerTask task = new popularSpinnerTask();
                task.isSwipe = true;
                task.execute("");
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_green_light,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);








        final Button btnNovoUsuario = (Button) findViewById(R.id.btnNovo);
        final Button btnEditarUsuario = (Button) findViewById(R.id.btnEditar);
        final Button btnExcluirUsuario = (Button) findViewById(R.id.btnExcluir);
        final Spinner dropdown = (Spinner) findViewById(R.id.spnEntidades);


        btnNovoUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(CadastroUsuarios.this, CadastroUsuariosNovoEditar.class);
                Bundle b = new Bundle();
                b.putString("registro", null);
                myIntent.putExtras(b); //Put your id to your next Intent
                startActivityForResult(myIntent, 1);
            }
        });

        btnEditarUsuario.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(CadastroUsuarios.this, CadastroUsuariosNovoEditar.class);
                Bundle b = new Bundle();
                b.putString("registro", dropdown.getSelectedItem().toString().substring(1, 6));
                myIntent.putExtras(b); //Put your id to your next Intent
                startActivityForResult(myIntent, 1);
            }
        });

        btnExcluirUsuario.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String usuExc = dropdown.getSelectedItem().toString().substring(7,  dropdown.getSelectedItem().toString().length()  );

                if(Utils.nomeUsuario.trim().toUpperCase().equals(usuExc.trim().toUpperCase())){
                    Toast.makeText(getApplicationContext(), "Não é possível excluir seu próprio usuário!", Toast.LENGTH_SHORT).show();
                    return;
                }

                new AlertDialog.Builder(CadastroUsuarios.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("")
                        .setMessage("Deseja realmente excluir o registro selecionado?")
                        .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                //remove
                                String[] paramns = new String[]{dropdown.getSelectedItem().toString().substring(1, 6)};
                                new excluirRegistroTask().execute(paramns);
                            }

                        })
                        .setNegativeButton("Não", null)
                        .show();
            }
        });


        //atualiza lista de usuarios
        new popularSpinnerTask().execute("");

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                //atualiza lista de usuarios
                new popularSpinnerTask().execute("");
            }
        }
    }

    class popularSpinnerTask extends AsyncTask<String, Integer, List> {

        boolean isSwipe = false;

        @Override
        protected void onPreExecute() {

            if(!isSwipe){
                inicializaProgressBar();
                progress.show();
            }

        }

        @Override
        protected List<UsuarioVO> doInBackground(String... param) {

            CadastrosControle cc = new CadastrosControle();

            try {

                List<UsuarioVO> lista = cc.listarUsuario("");
                return lista;

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(List result) {

            //popula o spinner
            Spinner dropdown = (Spinner) findViewById(R.id.spnEntidades);

            List<UsuarioVO> lista = result;
            String[] items = new String[lista.size()];

            int cont = 0;
            for (UsuarioVO item : lista) {
                items[cont] = "[" + String.format("%05d", item.getId()) + "] " + item.getNome();
                cont++;
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(CadastroUsuarios.this, android.R.layout.simple_spinner_dropdown_item, items);
            dropdown.setAdapter(adapter);

            final Button btnNovoUsuario = (Button) findViewById(R.id.btnNovo);
            final Button btnEditarUsuario = (Button) findViewById(R.id.btnEditar);
            final Button btnExcluirUsuario = (Button) findViewById(R.id.btnExcluir);

            dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    btnEditarUsuario.setEnabled(true);
                    btnExcluirUsuario.setEnabled(true);
                    btnEditarUsuario.setClickable(true);
                    btnExcluirUsuario.setClickable(true);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    btnEditarUsuario.setEnabled(false);
                    btnExcluirUsuario.setEnabled(false);
                    btnEditarUsuario.setClickable(false);
                    btnExcluirUsuario.setClickable(false);
                }
            });

            btnEditarUsuario.setEnabled(false);
            btnExcluirUsuario.setEnabled(false);
            btnEditarUsuario.setClickable(false);
            btnExcluirUsuario.setClickable(false);

            if (progress != null && progress.isShowing()) {
                progress.dismiss();
            }

            if(isSwipe){
                final SwipeRefreshLayout swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
                swipeContainer.setRefreshing(false);
            }

        }
    }

    class excluirRegistroTask extends AsyncTask<String, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(String... param) {

            CadastrosControle cc = new CadastrosControle();

            try {

                UsuarioVO o = new UsuarioVO();
                o.setId(Integer.parseInt(param[0]));
                cc.excluirUsuario(o);

                return true;

            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }


        @Override
        protected void onPostExecute(Boolean result) {

            if (result) {
                //atualiza lista de usuarios
                new popularSpinnerTask().execute("");
                Toast.makeText(getApplicationContext(), "Registro removido com sucesso!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Erro ao realizar a exclusão!", Toast.LENGTH_SHORT).show();
            }

        }

    }

}
