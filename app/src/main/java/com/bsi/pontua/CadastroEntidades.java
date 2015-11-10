package com.bsi.pontua;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TableRow.LayoutParams;
import android.widget.Toast;

import java.util.List;

import controle.CadastrosControle;
import utils.SoftRadioButton;
import vo.EntidadeVO;

public class CadastroEntidades extends AppCompatActivity {

    TableLayout tl;
    TableRow tr;
    TextView col1,col2;

    String registro ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_entidades);

        ///



        final Button btnNovoEntidade = (Button) findViewById(R.id.btnNovoEntidade);
        final Button btnEditarEntidade = (Button) findViewById(R.id.btnEditarEntidade);
        final Button btnExcluirEntidade = (Button) findViewById(R.id.btnExcluirEntidade);
        final ImageButton ibtCadEntidadeRefresh = (ImageButton) findViewById(R.id.ibtCadEntidadeRefresh);

        ibtCadEntidadeRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //atualiza grid
                new popularGridTask().execute("");
            }
        });

        btnNovoEntidade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent myIntent = new Intent(CadastroEntidades.this, CadastroEntidadesNovoEditar.class);
                //Bundle b = new Bundle();
                //b.putString("registro", null);
                //myIntent.putExtras(b); //Put your id to your next Intent
                //startActivityForResult(myIntent, 1);
            }
        });

        btnEditarEntidade.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Intent myIntent = new Intent(CadastroEntidades.this, CadastroEntidadesNovoEditar.class);
                //Bundle b = new Bundle();
                //b.putString("registro", registro);
                //myIntent.putExtras(b); //Put your id to your next Intent
                //startActivityForResult(myIntent, 1);
            }
        });

        btnExcluirEntidade.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(CadastroEntidades.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("")
                        .setMessage("Deseja realmente excluir o registro selecionado?")
                        .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                //remove
                                String[] paramns = new String[]{registro};
                                new excluirRegistroTask().execute(paramns);
                            }

                        })
                        .setNegativeButton("Não", null)
                        .show();
            }
        });


        //atualiza lista
        new popularGridTask().execute("");




    }



    /** This function add the headers to the table **/
    public void addHeaders(){

        /** Create a TableRow **/
        tr = new TableRow(this);
        tr.setLayoutParams(new LayoutParams(
                LayoutParams.FILL_PARENT,
                LayoutParams.WRAP_CONTENT));

        /** Creating a TextView to add to the row **/
        TextView col1 = new TextView(this);
        col1.setText("Cód");
        //col1.setTextColor(Color.GRAY);
        col1.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        col1.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
        col1.setPadding(5, 5, 5, 0);
        tr.addView(col1);  // Adding textView to tablerow.

        /** Creating another textview **/
        TextView col2 = new TextView(this);
        col2.setText("Nome");
        //col2.setTextColor(Color.GRAY);
        col2.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        col2.setPadding(5, 5, 5, 0);
        col2.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tr.addView(col2); // Adding textView to tablerow.

        /** Creating another textview **/
        TextView ncol2 = new TextView(this);
        ncol2.setText("Evento");
        //ncol2.setTextColor(Color.GRAY);
        ncol2.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
        ncol2.setPadding(5, 5, 5, 0);
        ncol2.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tr.addView(ncol2); // Adding textView to tablerow.



        // Add the TableRow to the TableLayout
        tl.addView(tr, new TableLayout.LayoutParams(
                LayoutParams.FILL_PARENT,
                LayoutParams.WRAP_CONTENT));

        // we are adding two textviews for the <span id="IL_AD5" class="IL_AD">divider</span> because we have two columns
        tr = new TableRow(this);
        tr.setLayoutParams(new LayoutParams(
                LayoutParams.FILL_PARENT,
                LayoutParams.WRAP_CONTENT));




/*
        *//** Creating another textview **//*
        TextView divider = new TextView(this);
        divider.setText("-----------------");
        divider.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        divider.setPadding(5, 0, 0, 0);
        divider.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tr.addView(divider); // Adding textView to tablerow.

        TextView divider2 = new TextView(this);
        divider2.setText("-------------------------");
        divider2.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
        divider2.setPadding(5, 0, 0, 0);
        divider2.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tr.addView(divider2); // Adding textView to tablerow.*/

        // Add the TableRow to the TableLayout
        tl.addView(tr, new TableLayout.LayoutParams(
                LayoutParams.FILL_PARENT,
                LayoutParams.WRAP_CONTENT));
    }

    /** This function add the data to the table **/
    public void addData(List<EntidadeVO> obj){

        for(EntidadeVO e: obj){

            /** Create a TableRow dynamically **/
            tr = new TableRow(this);
            tr.setLayoutParams(new LayoutParams(
                    LayoutParams.FILL_PARENT,
                    LayoutParams.WRAP_CONTENT));

            /** Creating a TextView to add to the row **/

            final  SoftRadioButton chk = new SoftRadioButton(this, "g1" );
            chk.setText(String.valueOf(e.getId()));
            chk.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
            chk.setPadding(5, 5, 5, 5);
            chk.setTextColor(Color.BLACK);
           /* chk.setOnClickListener(new View.OnClickListener() {

               @Override
                public void onClick(View v) {

                   Toast.makeText(getApplicationContext(), "CLicado! " + chk.getText(), Toast.LENGTH_SHORT).show();
                    btnEditarEntidade.setEnabled(true);
                    btnExcluirEntidade.setEnabled(true);
                    btnEditarEntidade.setClickable(true);
                    btnExcluirEntidade.setClickable(true);
                }


            });*/

            tr.addView(chk);  // Adding textView to tablerow.


            col1 = new TextView(this);
            col1.setText(e.getNome());
            col1.setTextColor(Color.BLACK);
            col1.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
            col1.setPadding(5, 5, 5, 5);
            tr.addView(col1);  // Adding textView to tablerow.

            /** Creating another textview **/
            col2 = new TextView(this);
            col2.setText(e.getEvento().getNome());
            col2.setTextColor(Color.BLACK);
            col2.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
            col2.setPadding(5, 5, 5, 5);
            tr.addView(col2); // Adding textView to tablerow.

            // Add the TableRow to the TableLayout
            tl.addView(tr, new TableLayout.LayoutParams(
                    LayoutParams.FILL_PARENT,
                    LayoutParams.WRAP_CONTENT));
        }
    }

////

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        Toast.makeText(getApplicationContext(),  String.valueOf(checked), Toast.LENGTH_SHORT).show();

    }


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
            progress = new ProgressDialog(CadastroEntidades.this);
            progress.setTitle("");
            progress.setMessage("Aguarde...");
            progress.setIndeterminate(true);
            progress.setCancelable(false);
        }
    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                //atualiza lista
                new popularGridTask().execute("");
            }
        }
    }

    class popularGridTask extends AsyncTask<String, Integer, List> {

        @Override
        protected void onPreExecute() {
            inicializaProgressBar();
            progress.show();
        }

        @Override
        protected List<EntidadeVO> doInBackground(String... param) {

            CadastrosControle cc = new CadastrosControle();

            try {

                List<EntidadeVO> lista = cc.listarEntidade("");
                return lista;

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Erro: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }

            return null;
        }


        @Override
        protected void onPostExecute(List result) {

            List<EntidadeVO> lista = result;
            String[] items = new String[lista.size()];
/*
            int cont = 0;
            for (EntidadeVO item : lista) {
                items[cont] = "[" + String.format("%05d", item.getId()) + "] " + item.getNome();
                cont++;
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(CadastroEntidades.this, android.R.layout.simple_spinner_dropdown_item, items);
            dropdown.setAdapter(adapter);
*/


            final Button btnNovoEntidade = (Button) findViewById(R.id.btnNovoEntidade);
            final Button btnEditarEntidade = (Button) findViewById(R.id.btnEditarEntidade);
            final Button btnExcluirEntidade = (Button) findViewById(R.id.btnExcluirEntidade);



            btnEditarEntidade.setEnabled(false);
            btnExcluirEntidade.setEnabled(false);
            btnEditarEntidade.setClickable(false);
            btnExcluirEntidade.setClickable(false);


            /////
            tl = (TableLayout) findViewById(R.id.maintable);

            tl.removeAllViewsInLayout();

            addHeaders();
            addData(result);
            /////


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

                EntidadeVO o = new EntidadeVO();
                o.setId(Integer.parseInt(param[0]));
                cc.excluirEntidade(o);

                return true;

            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }


        @Override
        protected void onPostExecute(Boolean result) {

            if (result) {
                //atualiza lista de Entidades
                new popularGridTask().execute("");
                Toast.makeText(getApplicationContext(), "Registro removido com sucesso!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Erro ao realizar a exclusão!", Toast.LENGTH_SHORT).show();
            }

        }

    }

}
