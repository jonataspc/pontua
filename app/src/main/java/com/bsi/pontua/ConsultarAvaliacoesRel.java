package com.bsi.pontua;

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

public class ConsultarAvaliacoesRel extends AppCompatActivity {

    Bundle b=null;

    TableLayout tl;
    TableRow tr;
    TextView col1, col2, col3, col4, col5, col6, col7, col8;

    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultar_avaliacoes_rel);


        final Button btnExcluir = (Button) findViewById(R.id.btnExcluir);
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

        btnExcluir.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                registro = -1;
                selectedRadio(tl);
                if (registro != -1) {
                    new AlertDialog.Builder(ConsultarAvaliacoesRel.this)
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
                }
            }
        });

        refresh();
    }

    void refresh(){


        //atualiza

        String objEnt=null;

        //todos?
        if(b.getInt("id_entidade") != -2 ){
            objEnt = String.valueOf(b.getInt("id_entidade"));
        }

        String[] paramns = new String[]{ String.valueOf(b.getInt("id_evento")), objEnt  };

        new popularGridTask().execute(paramns);

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
            progress = new ProgressDialog(ConsultarAvaliacoesRel.this);
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
        col1.setText("Cód");
        col1.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        col1.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        col1.setPadding(5, 5, 15, 0);
        tr.addView(col1);  // Adding textView to tablerow.

        /** Creating another textview **/
        TextView col2 = new TextView(this);
        col2.setText("Evento");
        col2.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        col2.setPadding(5, 5, 15, 0);
        col2.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tr.addView(col2); // Adding textView to tablerow.

        /** Creating another textview **/
        TextView ncol2 = new TextView(this);
        ncol2.setText("Área");
        ncol2.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        ncol2.setPadding(5, 5, 15, 0);
        ncol2.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tr.addView(ncol2); // Adding textView to tablerow.

        /** Creating another textview **/
        TextView ncol3 = new TextView(this);
        ncol3.setText("Ítem");
        ncol3.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        ncol3.setPadding(5, 5, 15, 0);
        ncol3.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tr.addView(ncol3); // Adding textView to tablerow.

        /** Creating another textview **/
        TextView ncol4 = new TextView(this);
        ncol4.setText("Entidade");
        ncol4.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        ncol4.setPadding(5, 5, 15, 0);
        ncol4.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tr.addView(ncol4); // Adding textView to tablerow.

        /** Creating another textview **/
        TextView ncol5 = new TextView(this);
        ncol5.setText("Pontuação");
        ncol5.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        ncol5.setPadding(5, 5, 15, 0);
        ncol5.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tr.addView(ncol5); // Adding textView to tablerow.



        /** Creating another textview **/
        TextView ncol6 = new TextView(this);
        ncol6.setText("Método");
        ncol6.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        ncol6.setPadding(5, 5, 15, 0);
        ncol6.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tr.addView(ncol6); // Adding textView to tablerow.

        /** Creating another textview **/
        TextView ncol7 = new TextView(this);
        ncol7.setText("Usuário");
        ncol7.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        ncol7.setPadding(5, 5, 15, 0);
        ncol7.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tr.addView(ncol7); // Adding textView to tablerow.

        /** Creating another textview **/
        TextView ncol8 = new TextView(this);
        ncol8.setText("Data/Hora");
        ncol8.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        ncol8.setPadding(5, 5, 15, 0);
        ncol8.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tr.addView(ncol8); // Adding textView to tablerow.


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

    public void addData(List<AvaliacaoVO> obj) {

        for (AvaliacaoVO e : obj) {

            /** Create a TableRow dynamically **/
            tr = new TableRow(this);
            tr.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

            /** Creating a TextView to add to the row **/
            final SoftRadioButton chk = new SoftRadioButton(this, "RadioBtn1");
            chk.setText(String.valueOf(e.getId()));
            chk.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
            chk.setPadding(5, 5, 5, 5);
            chk.setTextColor(Color.GRAY);
            tr.addView(chk);  // Adding textView to tablerow.

            col1 = new TextView(this);
            col1.setText(e.getItemInspecao().getEvento().getNome());
            col1.setTextColor(Color.BLACK);
            col1.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
            col1.setPadding(5, 5, 5, 5);
            tr.addView(col1);

            col2 = new TextView(this);
            col2.setText(e.getItemInspecao().getArea());
            col2.setTextColor(Color.GRAY);
            col2.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
            col2.setPadding(5, 5, 5, 5);
            tr.addView(col2);

            col3 = new TextView(this);
            col3.setText(e.getItemInspecao().getNome());
            col3.setTextColor(Color.BLACK);
            col3.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
            col3.setPadding(5, 5, 5, 5);
            tr.addView(col3);

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

            col6.setTextColor(Color.GRAY);
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
            tr.addView(col8);

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
        protected List<AvaliacaoVO> doInBackground(String... param) {

            CadastrosControle cc = new CadastrosControle();

            try {

                if(param[1] == null){
                    List<AvaliacaoVO> lista = cc.listarAvaliacao(  (EventoVO) cc.obterEventoPorId(Integer.parseInt(param[0])) , null);
                    return lista;
                }else{
                    List<AvaliacaoVO> lista = cc.listarAvaliacao(  (EventoVO) cc.obterEventoPorId(Integer.parseInt(param[0])) ,
                                                                   (EntidadeVO) cc.obterEntidadePorId(Integer.parseInt(param[1]))     );
                    return lista;
                }





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

    class excluirRegistroTask extends AsyncTask<String, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(String... param) {

            CadastrosControle cc = new CadastrosControle();

            try {

                AvaliacaoVO o = new AvaliacaoVO();
                o.setId(Integer.parseInt(param[0]));
                cc.excluirAvaliacao(o);

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
                refresh();



                Toast.makeText(getApplicationContext(), "Registro removido com sucesso!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Erro ao realizar a exclusão!", Toast.LENGTH_SHORT).show();
            }

        }

    }

}
