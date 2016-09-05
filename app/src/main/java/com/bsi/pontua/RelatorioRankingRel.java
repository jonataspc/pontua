package com.bsi.pontua;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import dao.EntidadeDAO;
import utils.SoftRadioButton;
import utils.Utils;
import vo.AvaliacaoVO;
import vo.EntidadeVO;
import vo.EventoVO;
import vo.ItemInspecaoVO;
import vo.RelRankingVO;

public class RelatorioRankingRel extends AppCompatActivity {

    Bundle b=null;

    TableLayout tl;
    TableRow tr;
    TextView col1, col2, col3;

    ProgressDialog progress;

    String nomeEvento = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relatorio_ranking_rel);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final ImageButton ibtCadRefresh = (ImageButton) findViewById(R.id.ibtRefresh);

        ibtCadRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //atualiza grid
                refresh();
            }
        });


        //recupera usuario
        b = getIntent().getExtras();

        if(b==null){
            finish();
        }

        refresh();
    }

    void refresh(){

        //atualiza
        String[] paramns = new String[]{ String.valueOf(b.getInt("id_evento")) };

        new popularGridTask().execute(paramns);

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
            progress = new ProgressDialog(RelatorioRankingRel.this);
            progress.setTitle("");
            progress.setMessage("Aguarde...");
            progress.setIndeterminate(true);
            progress.setCancelable(false);
        }
    }

    public void addHeaders() {

        /** Create a TableRow **/
        tr = new TableRow(this);
        tr.setLayoutParams(new LayoutParams(
                LayoutParams.FILL_PARENT,
                LayoutParams.WRAP_CONTENT));

        /** Creating a TextView to add to the row **/
        TextView col1 = new TextView(this);
        col1.setText("Entidade");
        col1.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        col1.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        col1.setPadding(5, 5, 15, 0);
        tr.addView(col1);  // Adding textView to tablerow.

        /** Creating another textview **/
        TextView col2 = new TextView(this);
        col2.setText("Saldo");
        col2.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        col2.setPadding(5, 5, 15, 0);
        col2.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tr.addView(col2); // Adding textView to tablerow.

        /** Creating another textview **/
        TextView ncol2 = new TextView(this);
        ncol2.setText("# Posição");
        ncol2.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        ncol2.setPadding(5, 5, 15, 0);
        ncol2.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tr.addView(ncol2); // Adding textView to tablerow.

      /*/*  *//** Creating another textview **//*
        TextView ncol3 = new TextView(this);
        ncol3.setText("");
        ncol3.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        ncol3.setPadding(5, 5, 15, 0);
        ncol3.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tr.addView(ncol3); // Adding textView to tablerow.
*//*
        *//** Creating another textview **//*
        TextView ncol4 = new TextView(this);
        ncol4.setText("Entidade");
        ncol4.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        ncol4.setPadding(5, 5, 15, 0);
        ncol4.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tr.addView(ncol4); // Adding textView to tablerow.

        *//** Creating another textview **//*
        TextView ncol5 = new TextView(this);
        ncol5.setText("Pontuação");
        ncol5.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        ncol5.setPadding(5, 5, 15, 0);
        ncol5.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tr.addView(ncol5); // Adding textView to tablerow.



        *//** Creating another textview **//*
        TextView ncol6 = new TextView(this);
        ncol6.setText("Método");
        ncol6.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        ncol6.setPadding(5, 5, 15, 0);
        ncol6.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tr.addView(ncol6); // Adding textView to tablerow.

        *//** Creating another textview **//*
        TextView ncol7 = new TextView(this);
        ncol7.setText("Usuário");
        ncol7.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        ncol7.setPadding(5, 5, 15, 0);
        ncol7.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tr.addView(ncol7); // Adding textView to tablerow.

        *//** Creating another textview **//*
        TextView ncol8 = new TextView(this);
        ncol8.setText("Data/Hora");
        ncol8.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        ncol8.setPadding(5, 5, 15, 0);
        ncol8.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tr.addView(ncol8); // Adding textView to tablerow.*/


        // Add the TableRow to the TableLayout
        tl.addView(tr, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

        // we are adding two textviews for the <span id="IL_AD5" class="IL_AD">divider</span> because we have two columns
        tr = new TableRow(this);
        tr.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

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
        tl.addView(tr, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));


        //separator
        TableRow tr = new TableRow(this);
        tr.setBackgroundColor(Color.GRAY);
        tr.setPadding(0, 0, 0, 2 ); //Border between rows

        TableRow.LayoutParams llp = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        llp.setMargins(0, 0, 2, 0);//2px right-margin

        tl.addView(tr);

    }

    public void addData(List<RelRankingVO> obj) {

        for (RelRankingVO e : obj) {

            /** Create a TableRow dynamically **/
            tr = new TableRow(this);
            tr.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

          /*  *//** Creating a TextView to add to the row **//*
            final SoftRadioButton chk = new SoftRadioButton(this, "RadioBtn1");
            chk.setText(String.valueOf(e.getId()));
            chk.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
            chk.setPadding(5, 5, 5, 5);
            chk.setTextColor(Color.GRAY);
            tr.addView(chk);  // Adding textView to tablerow.*/

            col1 = new TextView(this);
            col1.setText(e.getEntidade().getNome());
            col1.setTextColor(Color.BLACK);
            col1.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
            col1.setPadding(5, 5, 5, 5);
            tr.addView(col1);

            col2 = new TextView(this);
            col2.setText(String.valueOf(e.getSaldoPontuacao().doubleValue()));
            col2.setTextColor(Color.BLACK);
            col2.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
            col2.setPadding(5, 5, 5, 5);
            tr.addView(col2);

            col3 = new TextView(this);
            col3.setText(String.valueOf(e.getPosicao()));
            col3.setTextColor(Color.BLACK);
            col3.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
            col3.setPadding(5, 5, 5, 5);
            tr.addView(col3);



/*
            col4 = new TextView(this);
            col4.setText(e.getEntidade().getNome());
            col4.setTextColor(Color.BLUE);
            col4.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
            col4.setPadding(5, 5, 5, 5);
            tr.addView(col4);

            col5 = new TextView(this);
            col5.setText(String.valueOf(e.getPontuacao().doubleValue()));
            col5.setTextColor(Color.BLACK);
            col5.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
            col5.setPadding(5, 5, 5, 5);
            tr.addView(col5);





            col6 = new TextView(this);

            if(e.getForma_automatica()==0){
                col6.setText("Manual");
            }else{
                col6.setText("NFC");
            }

            col6.setTextColor(Color.GREEN);
            col6.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
            col6.setPadding(5, 5, 5, 5);
            tr.addView(col6);


            col7 = new TextView(this);
            col7.setText(e.getUsuario().getNome());
            col7.setTextColor(Color.BLACK);
            col7.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
            col7.setPadding(5, 5, 5, 5);
            tr.addView(col7);


            col8 = new TextView(this);
            col8.setText( Utils.formatarData(e.getDataHora()));
            col8.setTextColor(Color.GRAY);
            col8.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
            col8.setPadding(5, 5, 5, 5);
            tr.addView(col8);*/

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

    class popularGridTask extends AsyncTask<String, Integer, List> {

        @Override
        protected void onPreExecute() {
            inicializaProgressBar();
            progress.show();
        }

        @Override
        protected List<RelRankingVO> doInBackground(String... param) {

            CadastrosControle cc = new CadastrosControle();

            try {

                    List<RelRankingVO> lista = cc.listarRelRanking(  (EventoVO) cc.obterEventoPorId(Integer.parseInt(param[0]))  );
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
            if(result != null){
                addData(result);
            }

            if (progress != null && progress.isShowing()) {
                progress.dismiss();
            }
        }
    }

}


