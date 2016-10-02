package com.bsi.pontua;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import controle.CadastrosControle;
import utils.SoftRadioButton;
import vo.ItemInspecaoVO;

public class CadastroItensInspecao extends AppCompatActivity {

    TableLayout tl;
    TableRow tr;
    TextView col1, col2, col3, col4, col5;

    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_itens_inspecao);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("Ítens de Pontuação");

        final Button btnNovo = (Button) findViewById(R.id.btnNovo);
        final Button btnEditar = (Button) findViewById(R.id.btnEditar);
        final Button btnExcluir = (Button) findViewById(R.id.btnExcluir);

        // Lookup the swipe container view
        final SwipeRefreshLayout swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //atualiza
                popularGridTask task = new popularGridTask();
                task.isSwipe = true;
                task.execute("");

            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_green_light,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);






        btnNovo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(CadastroItensInspecao.this, CadastroItensInspecaoNovoEditar.class);
                Bundle b = new Bundle();
                b.putString("registro", null);
                myIntent.putExtras(b); //Put your id to your next Intent
                startActivityForResult(myIntent, 1);
            }
        });

        btnEditar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                registro = -1;
                selectedRadio(tl);

                if (registro != -1) {
                    Intent myIntent = new Intent(CadastroItensInspecao.this, CadastroItensInspecaoNovoEditar.class);
                    Bundle b = new Bundle();
                    b.putString("registro", String.valueOf(registro));
                    myIntent.putExtras(b); //Put your id to your next Intent
                    startActivityForResult(myIntent, 1);
                } else {
                    Toast.makeText(getApplicationContext(), "Selecione algum registro a editar!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnExcluir.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                registro = -1;
                selectedRadio(tl);
                if (registro != -1) {
                    new AlertDialog.Builder(CadastroItensInspecao.this)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("")
                            .setMessage("Deseja realmente excluir o registro selecionado?")
                            .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    //remove
                                    String[] paramns = new String[]{String.valueOf(registro)};
                                    new excluirRegistroTask().execute(paramns);
                                }

                            })
                            .setNegativeButton("Não", null)
                            .show();
                } else {
                    Toast.makeText(getApplicationContext(), "Selecione algum registro a excluir!", Toast.LENGTH_SHORT).show();
                }
            }
        });


        //atualiza lista
        new popularGridTask().execute("");




    }

    int registro = -1;

    public void selectedRadio(ViewGroup layout) {

        for (int i = 0; i < layout.getChildCount(); i++) {
            View v = layout.getChildAt(i);

            if (v instanceof ViewGroup) {
                selectedRadio((ViewGroup) v);
            } else if (v instanceof TableRow) {
                selectedRadio((TableRow) v);
            } else if (v instanceof SoftRadioButton) {

                if (((SoftRadioButton) v).isChecked()) {
                    registro = Integer.parseInt(((SoftRadioButton) v).getText().toString());
                }


            }

        }

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
            progress = new ProgressDialog(CadastroItensInspecao.this);
            progress.setTitle("");
            progress.setMessage("Aguarde...");
            progress.setIndeterminate(true);
            progress.setCancelable(false);
        }
    }

//*
//     * This function add the headers to the table
//     *

    public void addHeaders() {

//* Create a TableRow *

        tr = new TableRow(this);
        tr.setLayoutParams(new LayoutParams(
                LayoutParams.FILL_PARENT,
                LayoutParams.WRAP_CONTENT));

//* Creating a TextView to add to the row *

        TextView col1 = new TextView(this);
        col1.setText("Cód");
        col1.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        col1.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        col1.setPadding(5, 5, 5, 0);
        tr.addView(col1);  // Adding textView to tablerow.

//* Creating another textview *

//        TextView col2 = new TextView(this);
//        col2.setText("Evento");
//        col2.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
//        col2.setPadding(5, 5, 5, 0);
//        col2.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
//        tr.addView(col2); // Adding textView to tablerow.

//* Creating another textview *

        TextView ncol2 = new TextView(this);
        ncol2.setText("Área");
        ncol2.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        ncol2.setPadding(50, 5, 5, 0);
        ncol2.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tr.addView(ncol2); // Adding textView to tablerow.

//* Creating another textview *

        TextView ncol3 = new TextView(this);
        ncol3.setText("Nome");
        ncol3.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        ncol3.setPadding(50, 5, 5, 0);
        ncol3.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tr.addView(ncol3); // Adding textView to tablerow.

//* Creating another textview *

        TextView ncol4 = new TextView(this);
        ncol4.setText("Pont. Mín");
        ncol4.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        ncol4.setPadding(50, 5, 5, 0);
        ncol4.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tr.addView(ncol4); // Adding textView to tablerow.

//* Creating another textview *

        TextView ncol5 = new TextView(this);
        ncol5.setText("Pont. Máx");
        ncol5.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        ncol5.setPadding(50, 5, 5, 0);
        ncol5.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tr.addView(ncol5); // Adding textView to tablerow.


        // Add the TableRow to the TableLayout
        tl.addView(tr, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

        // we are adding two textviews for the <span id="IL_AD5" class="IL_AD">divider</span> because we have two columns
        tr = new TableRow(this);
        tr.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));


//* Creating another textview *
//        TextView divider = new TextView(this);
//        divider.setText("-----------------");
//        divider.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
//        divider.setPadding(5, 0, 0, 0);
//        divider.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
//        tr.addView(divider); // Adding textView to tablerow.
//
//        TextView divider2 = new TextView(this);
//        divider2.setText("-------------------------");
//        divider2.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
//        divider2.setPadding(5, 0, 0, 0);
//        divider2.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
//        tr.addView(divider2); // Adding textView to tablerow.


        // Add the TableRow to the TableLayout
        tl.addView(tr, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));


        //separator
        TableRow tr = new TableRow(this);
        tr.setBackgroundColor(Color.GRAY);
        tr.setPadding(0, 0, 0, 2 ); //Border between rows

        TableRow.LayoutParams llp = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        llp.setMargins(0, 0, 2, 0);//2px right-margin

        tl.addView(tr);

    }

//*
//     * This function add the data to the table
//     *

    public void addData(List<ItemInspecaoVO> obj) {

        for (ItemInspecaoVO e : obj) {

//* Create a TableRow dynamically *

            tr = new TableRow(this);
            tr.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

//* Creating a TextView to add to the row *

            final SoftRadioButton chk = new SoftRadioButton(this, "RadioBtn1");
            chk.setText(String.valueOf(e.getId()));
            chk.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
            chk.setPadding(5, 5, 5, 5);
            chk.setTextColor(Color.BLACK);
            tr.addView(chk);  // Adding textView to tablerow.

//            col1 = new TextView(this);
//            col1.setText(e.getEvento().getNome());
//            col1.setTextColor(Color.BLACK);
//            col1.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
//            col1.setPadding(5, 5, 5, 5);
//            tr.addView(col1);

            col2 = new TextView(this);
            col2.setText(e.getArea().getNome());
            col2.setTextColor(Color.BLACK);
            col2.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
            col2.setPadding(50, 5, 5, 5);
            tr.addView(col2);

            col3 = new TextView(this);
            col3.setText(e.getNome());
            col3.setTextColor(Color.BLACK);
            col3.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
            col3.setPadding(50, 5, 5, 5);
            tr.addView(col3);

            col4 = new TextView(this);
            col4.setText(String.valueOf(e.getPontuacaoMinima().doubleValue()));
            col4.setTextColor(Color.RED);
            col4.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
            col4.setPadding(50, 5, 5, 5);
            tr.addView(col4);

            col5 = new TextView(this);
            col5.setText(String.valueOf(e.getPontuacaoMaxima().doubleValue()));
            col5.setTextColor(Color.GREEN);
            col5.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
            col5.setPadding(50, 5, 5, 5);
            tr.addView(col5);

            // Add the TableRow to the TableLayout
            tl.addView(tr, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));




            //separator
            TableRow tr = new TableRow(this);
            tr.setBackgroundColor(Color.GRAY);
            tr.setPadding(0, 0, 0, 2); //Border between rows

            TableRow.LayoutParams llp = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
            llp.setMargins(0, 0, 2, 0);//2px right-margin

            tl.addView(tr);



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

        boolean isSwipe = false;

        @Override
        protected void onPreExecute() {
            if(!isSwipe){
                inicializaProgressBar();
                progress.show();
            }
        }

        @Override
        protected List<ItemInspecaoVO> doInBackground(String... param) {

            CadastrosControle cc = new CadastrosControle();

            try {

                List<ItemInspecaoVO> lista = cc.listarItemInspecao("");
                return lista;

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(List result) {

            tl = (TableLayout) findViewById(R.id.maintable);
            tl.removeAllViewsInLayout();

            addHeaders();
            addData(result);

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

                ItemInspecaoVO o = new ItemInspecaoVO();
                o.setId(Integer.parseInt(param[0]));
                cc.excluirItemInspecao(o);

                return true;

            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {

            if (result) {
                //atualiza lista de ItemInspecao
                AsyncTask t = new popularGridTask().execute("");

                try {
                    t.get();
                }catch (Exception ex){
                    ex.printStackTrace();
                }

                Toast.makeText(getApplicationContext(), "Registro removido com sucesso!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Erro ao realizar a exclusão!", Toast.LENGTH_SHORT).show();
            }

        }

    }

}
