package com.bsi.pontua;

import android.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

import vo.EventoVO;

public class RelatoriosRel extends AppCompatActivity {

    ProgressDialog progress;
    EventoVO eventoVO = null;
    private final String TAG_FRAG = "tagFrag";


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //here
                setResult(1, new Intent().putExtra("teste", "from c"));
                this.finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relatorios_rel);


        eventoVO = (EventoVO) getIntent().getSerializableExtra(EventoVO.class.getName());

        setTitle("Relatórios - Evento " + eventoVO.getNome());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //spinner
        ((Spinner) findViewById(R.id.spnRelatorios)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                FragmentManager fm = getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction = fm.beginTransaction();

                //carrega fragments
                switch (parentView.getSelectedItem().toString()){
                    case "Ranking de entidades":
                        RelatoriosRelRanking f = new RelatoriosRelRanking();
                        f.evento = eventoVO;
                        //fragmentTransaction.add(R.id.frag_container, f, TAG_FRAG);
                        fragmentTransaction.replace(R.id.frag_container, f, TAG_FRAG);
                        break;
                    case "Evolução do evento":
                        RelatoriosRelEvolucao f2 = new RelatoriosRelEvolucao();
                        f2.evento = eventoVO;
                        //fragmentTransaction.add(R.id.frag_container, f2, TAG_FRAG);
                        fragmentTransaction.replace(R.id.frag_container, f2, TAG_FRAG);
                        break;
                    default:
                        //remove..
                        android.support.v4.app.Fragment frag = fm.findFragmentByTag(TAG_FRAG);
                        if(frag!=null) {
                            fragmentTransaction.remove(frag);
                        }
                        break;
                }

                fragmentTransaction.commit();

            }



            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });



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
            progress = new ProgressDialog(RelatoriosRel.this);
            progress.setTitle("");
            progress.setMessage("Aguarde...");
            progress.setIndeterminate(true);
            progress.setCancelable(false);
        }
    }


}


