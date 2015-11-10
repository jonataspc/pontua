package com.bsi.pontua;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

import controle.CadastrosControle;
import vo.EventoVO;

public class CadastroEventos extends AppCompatActivity {


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
            progress = new ProgressDialog(CadastroEventos.this);
            progress.setTitle("");
            progress.setMessage("Aguarde...");
            progress.setIndeterminate(true);
            progress.setCancelable(false);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_eventos);


        final Button btnNovoEvento = (Button) findViewById(R.id.btnNovoEntidade);
        final Button btnEditarEvento = (Button) findViewById(R.id.btnEditarEntidade);
        final Button btnExcluirEvento = (Button) findViewById(R.id.btnExcluirEvento);
        final ImageButton ibtCadEventoRefresh = (ImageButton) findViewById(R.id.ibtCadEventoRefresh);
        final Spinner dropdown = (Spinner) findViewById(R.id.spnEntidades);

        ibtCadEventoRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //atualiza lista de Eventos
                new popularSpinnerTask().execute("");
            }
        });

        btnNovoEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(CadastroEventos.this, CadastroEventosNovoEditar.class);
                Bundle b = new Bundle();
                b.putString("registro", null);
                myIntent.putExtras(b); //Put your id to your next Intent
                startActivityForResult(myIntent, 1);
            }
        });

        btnEditarEvento.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(CadastroEventos.this, CadastroEventosNovoEditar.class);
                Bundle b = new Bundle();
                b.putString("registro", dropdown.getSelectedItem().toString().substring(1, 6));
                myIntent.putExtras(b); //Put your id to your next Intent
                startActivityForResult(myIntent, 1);
            }
        });

        btnExcluirEvento.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(CadastroEventos.this)
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


        //atualiza lista de Eventos
        new popularSpinnerTask().execute("");
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                //atualiza lista de Eventos
                new popularSpinnerTask().execute("");
            }
        }
    }

    class popularSpinnerTask extends AsyncTask<String, Integer, List> {

        @Override
        protected void onPreExecute() {
            inicializaProgressBar();
            progress.show();
        }

        @Override
        protected List<EventoVO> doInBackground(String... param) {

            CadastrosControle cc = new CadastrosControle();

            try {

                List<EventoVO> lista = cc.listarEvento("");
                return lista;

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Erro: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }

            return null;
        }


        @Override
        protected void onPostExecute(List result) {

            //popula o spinner
            Spinner dropdown = (Spinner) findViewById(R.id.spnEntidades);

            List<EventoVO> lista = result;
            String[] items = new String[lista.size()];

            int cont = 0;
            for (EventoVO item : lista) {
                items[cont] = "[" + String.format("%05d", item.getId()) + "] " + item.getNome();
                cont++;
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(CadastroEventos.this, android.R.layout.simple_spinner_dropdown_item, items);
            dropdown.setAdapter(adapter);

            final Button btnNovoEvento = (Button) findViewById(R.id.btnNovoEntidade);
            final Button btnEditarEvento = (Button) findViewById(R.id.btnEditarEntidade);
            final Button btnExcluirEvento = (Button) findViewById(R.id.btnExcluirEvento);

            dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    btnEditarEvento.setEnabled(true);
                    btnExcluirEvento.setEnabled(true);
                    btnEditarEvento.setClickable(true);
                    btnExcluirEvento.setClickable(true);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    btnEditarEvento.setEnabled(false);
                    btnExcluirEvento.setEnabled(false);
                    btnEditarEvento.setClickable(false);
                    btnExcluirEvento.setClickable(false);
                }
            });

            btnEditarEvento.setEnabled(false);
            btnExcluirEvento.setEnabled(false);
            btnEditarEvento.setClickable(false);
            btnExcluirEvento.setClickable(false);

            if (progress != null && progress.isShowing()) {
                progress.dismiss();
            }
        }
    }

    class excluirRegistroTask extends AsyncTask<String, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(String... param) {

            CadastrosControle cc = new CadastrosControle();

            try {

                EventoVO o = new EventoVO();
                o.setId(Integer.parseInt(param[0]));
                cc.excluirEvento(o);

                return true;

            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }


        @Override
        protected void onPostExecute(Boolean result) {

            if (result) {
                //atualiza lista de Eventos
                new popularSpinnerTask().execute("");
                Toast.makeText(getApplicationContext(), "Registro removido com sucesso!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Erro ao realizar a exclusão!", Toast.LENGTH_SHORT).show();
            }

        }

    }
}
