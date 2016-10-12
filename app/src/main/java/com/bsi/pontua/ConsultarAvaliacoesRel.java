package com.bsi.pontua;

import android.content.SharedPreferences;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import controle.CadastrosControle;
import dao.EntidadeDAO;
import utils.SoftRadioButton;
import utils.Utils;
import vo.AreaVO;
import vo.AvaliacaoVO;
import vo.EntidadeVO;
import vo.EventoVO;
import vo.ItemInspecaoVO;
import vo.UsuarioVO;

public class ConsultarAvaliacoesRel extends AppCompatActivity implements View.OnClickListener {

    TableLayout tl;
    TableRow tr;
    TextView col1, col2, col3, col4, col5, col6, col7, col8;

    ProgressDialog progress;

    private boolean hasChecked = false;

    private final String TXT_MENU_EXC_AVAL = "Excluir avaliações";
    private final int ID_MENU_EXC_AVAL = 998;

    //itens a remover
    private List<AvaliacaoVO> lstExcluir = null;

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        if (hasChecked )
        {
            menu.add(Menu.NONE, ID_MENU_EXC_AVAL, Menu.NONE, TXT_MENU_EXC_AVAL).setIcon(android.R.drawable.ic_delete)
                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }

        return true;
    }


    @Override
    public void onClick(View v) {

        hasChecked = false;

        if(v instanceof CheckBox && v.getTag()!=null && v.getTag() instanceof AvaliacaoVO  ){
            //usr clicou no checkbox

            //zera lst
            lstExcluir = new ArrayList<>();

            //checa se tem preeenchidos
            checkAvalExcl(tl);

            if(lstExcluir.size()!=0){
                //tem algum marcado, habilita opcao no menu
                hasChecked = true;
            }

        }

        //recria menu
        invalidateOptionsMenu();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultar_avaliacoes_rel);

        setTitle("Consultar Avaliações");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        final Button btnExcluir = (Button) findViewById(R.id.btnExcluir);
        final ImageButton ibtCadRefresh = (ImageButton) findViewById(R.id.ibtRefresh);


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



        //atualiza
        popularGridTask o = new popularGridTask();
        o.execute("");

    }

    public void checkAvalExcl(ViewGroup layout) {

        for (int i = 0; i < layout.getChildCount(); i++) {
            View v = layout.getChildAt(i);

            if (v instanceof ViewGroup) {
                checkAvalExcl((ViewGroup) v);
            } else if (v instanceof TableRow) {
                checkAvalExcl((TableRow) v);
            } else if (v instanceof CheckBox) {

                if (((CheckBox) v).isChecked()) {
                    lstExcluir.add( (AvaliacaoVO) v.getTag()  );
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

// * Create a TableRow *

        tr = new TableRow(this);
        tr.setLayoutParams(new LayoutParams(
                LayoutParams.FILL_PARENT,
                LayoutParams.WRAP_CONTENT));



        TextView col1 = new TextView(this);
        col1.setText("Cód");
        col1.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        col1.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        col1.setPadding(5, 5, 15, 0);
        tr.addView(col1);  // Adding textView to tablerow.



        TextView col2 = new TextView(this);
        col2.setText("Evento");
        col2.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        col2.setPadding(5, 5, 15, 0);
        col2.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tr.addView(col2); // Adding textView to tablerow.



        TextView ncol2 = new TextView(this);
        ncol2.setText("Área");
        ncol2.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        ncol2.setPadding(5, 5, 15, 0);
        ncol2.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tr.addView(ncol2); // Adding textView to tablerow.



        TextView ncol3 = new TextView(this);
        ncol3.setText("Item");
        ncol3.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        ncol3.setPadding(5, 5, 15, 0);
        ncol3.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tr.addView(ncol3); // Adding textView to tablerow.



        TextView ncol4 = new TextView(this);
        ncol4.setText("Entidade");
        ncol4.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        ncol4.setPadding(5, 5, 15, 0);
        ncol4.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tr.addView(ncol4); // Adding textView to tablerow.



        TextView ncol5 = new TextView(this);
        ncol5.setText("Pontuação");
        ncol5.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        ncol5.setPadding(5, 5, 15, 0);
        ncol5.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tr.addView(ncol5); // Adding textView to tablerow.



// * Creating another textview *

        TextView ncol6 = new TextView(this);
        ncol6.setText("Método");
        ncol6.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        ncol6.setPadding(5, 5, 15, 0);
        ncol6.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tr.addView(ncol6); // Adding textView to tablerow.



        TextView ncol7 = new TextView(this);
        ncol7.setText("Usuário");
        ncol7.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        ncol7.setPadding(5, 5, 15, 0);
        ncol7.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tr.addView(ncol7); // Adding textView to tablerow.



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

    public void addData(List<AvaliacaoVO> obj) {

        for (AvaliacaoVO e : obj) {

//* Create a TableRow dynamically *

            tr = new TableRow(this);
            tr.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

//* Creating a TextView to add to the row *


            if(Utils.usuarioCorrente.getNivelAcesso() == UsuarioVO.EnumNivelAcesso.Entidade){
                //entidade, apenas visualiza

                final TextView chk = new TextView(this);
                chk.setText(String.valueOf(e.getId()));
                chk.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
                chk.setPadding(5, 5, 5, 5);
                chk.setTextColor(Color.GRAY);
                tr.addView(chk);  // Adding textView to tablerow.
            }else {
                //adm, pode excluir

                final CheckBox chk = new CheckBox(this);
                chk.setText(String.valueOf(e.getId()));
                chk.setTag(e);
                chk.setOnClickListener(this);
                chk.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
                chk.setPadding(5, 5, 5, 5);
                chk.setTextColor(Color.GRAY);
                tr.addView(chk);  // Adding textView to tablerow.
            }



            col1 = new TextView(this);
            col1.setText(e.getRelEntidadeEvento().getEvento().getNome().toUpperCase().trim());
            col1.setTextColor(Color.BLACK);
            col1.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
            col1.setPadding(5, 5, 5, 5);
            tr.addView(col1);

            col2 = new TextView(this);
            col2.setText(e.getRelItemInspecaoEvento().getItemInspecao().getArea().getNome().toUpperCase().trim());
            col2.setTextColor(Color.GRAY);
            col2.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
            col2.setPadding(5, 5, 5, 5);
            tr.addView(col2);

            col3 = new TextView(this);
            col3.setText(e.getRelItemInspecaoEvento().getItemInspecao().getNome().toUpperCase().trim());
            col3.setTextColor(Color.BLACK);
            col3.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
            col3.setPadding(5, 5, 5, 5);
            tr.addView(col3);

            col4 = new TextView(this);
            col4.setText(e.getRelEntidadeEvento().getEntidade().getNome().toUpperCase().trim());
            col4.setTextColor(Color.GRAY);
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
            col6.setText(e.getMetodo().toString());
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //here
                setResult(1, new Intent().putExtra("teste", "from c"));
                this.finish();
                return true;

            case ID_MENU_EXC_AVAL:
                //remove
                removerItens();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    void removerItens(){
        new AlertDialog.Builder(ConsultarAvaliacoesRel.this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("")
                .setMessage("Deseja realmente excluir o(s) registro(s) selecionado(s)?")
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //remove
                        excluirRegistroTask o = new excluirRegistroTask();
                        o.execute("");
                    }

                })
                .setNegativeButton("Não", null)
                .show();
    }

    class popularGridTask extends AsyncTask<String, Integer, List> {

        boolean isSwipe = false;

        @Override
        protected void onPreExecute() {
            if(isSwipe) {
                final SwipeRefreshLayout swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
                swipeContainer.setRefreshing(true);
            } else {
                inicializaProgressBar();
                progress.show();
            }
        }

        @Override
        protected List<AvaliacaoVO> doInBackground(String... param) {

            try {

                try(CadastrosControle cc = new CadastrosControle()){

                    EventoVO evt = (EventoVO) getIntent().getSerializableExtra(EventoVO.class.getName());
                    EntidadeVO ent = (EntidadeVO) getIntent().getSerializableExtra(EntidadeVO.class.getName());
                    AreaVO area = (AreaVO) getIntent().getSerializableExtra(AreaVO.class.getName());
                    ItemInspecaoVO item = (ItemInspecaoVO) getIntent().getSerializableExtra(ItemInspecaoVO.class.getName());
                    UsuarioVO usuario = (UsuarioVO) getIntent().getSerializableExtra(UsuarioVO.class.getName());

                    List<AvaliacaoVO> lista = cc.listarAvaliacao(evt, ent, area, item, usuario);
                    return lista;

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(List result) {

            //order by
            switch (getIntent().getSerializableExtra("OrderBy").toString()){
                case Utils.lstPrderByEntidade:

                    Collections.sort (result, new Comparator<AvaliacaoVO>() {
                        public int compare (AvaliacaoVO p1, AvaliacaoVO p2) {
                            return p1.getRelEntidadeEvento().getEntidade().getNome().compareTo(p2.getRelEntidadeEvento().getEntidade().getNome());
                        }
                    });

                    break;

                case Utils.lstPrderByArea :

                    Collections.sort (result, new Comparator<AvaliacaoVO>() {
                        public int compare (AvaliacaoVO p1, AvaliacaoVO p2) {
                            return p1.getRelItemInspecaoEvento().getItemInspecao().getArea().getNome().compareTo(p2.getRelItemInspecaoEvento().getItemInspecao().getArea().getNome());
                        }
                    });

                    break;

                case Utils.lstPrderByItem:

                    Collections.sort (result, new Comparator<AvaliacaoVO>() {
                        public int compare (AvaliacaoVO p1, AvaliacaoVO p2) {
                            return p1.getRelItemInspecaoEvento().getItemInspecao().getNome().compareTo(p2.getRelItemInspecaoEvento().getItemInspecao().getNome());
                        }
                    });

                    break;

                case Utils.lstPrderByUsuario :

                    Collections.sort (result, new Comparator<AvaliacaoVO>() {
                        public int compare (AvaliacaoVO p1, AvaliacaoVO p2) {
                            return p1.getUsuario().getNome().compareTo(p2.getUsuario().getNome());
                        }
                    });

                    break;

                case Utils.lstPrderByDataHora :

                    Collections.sort (result, new Comparator<AvaliacaoVO>() {
                        public int compare (AvaliacaoVO p1, AvaliacaoVO p2) {
                            return p1.getDataHora().compareTo(p2.getDataHora());
                        }
                    });

                    break;

            }


            tl = (TableLayout) findViewById(R.id.maintable);
            tl.removeAllViewsInLayout();

            addHeaders();
           if(result != null){
               addData(result);
           }

            if (progress != null && progress.isShowing()) {
                progress.dismiss();
            }

            if(isSwipe){
                final SwipeRefreshLayout swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
                swipeContainer.setRefreshing(false);
            }

            if(result.size()==0){
                Toast.makeText(getApplicationContext(), "Não foram localizados resultados", Toast.LENGTH_SHORT).show();
            }

        }
    }

    class excluirRegistroTask extends AsyncTask<String, Integer, Boolean> {

        @Override
        protected void onPreExecute() {
            inicializaProgressBar();
            progress.show();
        }


        @Override
        protected Boolean doInBackground(String... param) {

            try {

                try(CadastrosControle cc = new CadastrosControle()){

                    for(AvaliacaoVO item : lstExcluir){
                        cc.excluirAvaliacao(item);
                    }
                }

                return true;

            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {


            if (progress != null && progress.isShowing()) {
                progress.dismiss();
            }


            if (result) {

                Toast.makeText(getApplicationContext(), "Registro(s) removido(s) com sucesso!", Toast.LENGTH_SHORT).show();

                //atualiza lista de ItemInspecao
                popularGridTask o = new popularGridTask();
                o.isSwipe = true;
                o.execute("");

            } else {
                Toast.makeText(getApplicationContext(), "Erro ao realizar a exclusão!", Toast.LENGTH_SHORT).show();
            }

        }

    }

}
