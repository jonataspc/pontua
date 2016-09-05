package com.bsi.pontua;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcB;
import android.nfc.tech.NfcF;
import android.nfc.tech.NfcV;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import controle.CadastrosControle;
import utils.SoftRadioButton;
import vo.EntidadeVO;

public class CadastroEntidades extends AppCompatActivity {

    TableLayout tl;
    TableRow tr;
    TextView col1, col2;

    ProgressDialog progress;
    AlertDialog writeTagAlert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_entidades);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        final Button btnNovo = (Button) findViewById(R.id.btnNovo);
        final Button btnEditar = (Button) findViewById(R.id.btnEditar);
        final Button btnExcluir = (Button) findViewById(R.id.btnExcluir);
        final Button btnWriteNfcTag = (Button) findViewById(R.id.btnWriteNfcTag);

        btnWriteNfcTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Write to a tag for as long as the dialog is shown.



                if (mNfcAdapter == null) {
                    // Stop here, we definitely need NFC
                    Toast.makeText(CadastroEntidades.this, "Este dispositivo não suporta NFC.", Toast.LENGTH_LONG).show();
                    return;

                }

                if (!mNfcAdapter.isEnabled()) {
                    Toast.makeText(CadastroEntidades.this, "NFC está desativado. Ligue-o e tente novamente.", Toast.LENGTH_LONG).show();
                    return;
                }



                registro = -1;
                selectedRadio(tl);

                if (registro != -1) {

                    disableNdefExchangeMode();
                    enableTagWriteMode();

                    writeTagAlert = new AlertDialog.Builder(CadastroEntidades.this).setTitle("Aproxime a tag para a gravação")
                            .setOnCancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    disableTagWriteMode();
                                    enableNdefExchangeMode();
                                }
                            }).create();

                    writeTagAlert.show();

                } else {
                    Toast.makeText(getApplicationContext(), "Selecione algum registro!", Toast.LENGTH_SHORT).show();
                }


            }
        });

        //nfc

        // Handle all of our received NFC intents in this activity.
        mNfcPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        // Intent filters for reading a note from a tag or exchanging over p2p.
        IntentFilter ndefDetected = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            ndefDetected.addDataType("*/*");
        } catch (IntentFilter.MalformedMimeTypeException e) {
        }
        mNdefExchangeFilters = new IntentFilter[]{ndefDetected};

        // Intent filters for writing to a tag
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        mWriteTagFilters = new IntentFilter[]{tagDetected};


        techListsArray = new String[][]{new String[]{NdefFormatable.class.getName()},
                new String[]{MifareClassic.class.getName()},
                new String[]{NfcA.class.getName()},
                new String[]{Ndef.class.getName()},
                new String[]{NfcB.class.getName()},
                new String[]{NfcF.class.getName()},
                new String[]{Ndef.class.getName()},
                new String[]{NfcV.class.getName()}};



        //end nfc

        // Lookup the swipe container view
        final SwipeRefreshLayout swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeContainer.setRefreshing(false);

                //atualiza lista de usuarios
                new popularGridTask().execute("");
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
                Intent myIntent = new Intent(CadastroEntidades.this, CadastroEntidadesNovoEditar.class);
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
                    Intent myIntent = new Intent(CadastroEntidades.this, CadastroEntidadesNovoEditar.class);
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
                    new AlertDialog.Builder(CadastroEntidades.this)
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

        mResumed = false;

        if(mNfcAdapter!=null){
            mNfcAdapter.disableForegroundDispatch(this);
        }


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

    /**
     * This function add the headers to the table
     **/
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
        col1.setPadding(5, 5, 5, 0);
        tr.addView(col1);  // Adding textView to tablerow.

//        /** Creating another textview **/
//        TextView col2 = new TextView(this);
//        col2.setText("Evento");
//        col2.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
//        col2.setPadding(5, 5, 5, 0);
//        col2.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
//        tr.addView(col2); // Adding textView to tablerow.

        /** Creating another textview **/
        TextView ncol2 = new TextView(this);
        ncol2.setText("Nome");
        ncol2.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        ncol2.setPadding(5, 5, 5, 0);
        ncol2.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tr.addView(ncol2); // Adding textView to tablerow.


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
        tr.setPadding(0, 0, 0, 2); //Border between rows

        TableRow.LayoutParams llp = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        llp.setMargins(0, 0, 2, 0);//2px right-margin

        tl.addView(tr);


    }

    /**
     * This function add the data to the table
     **/
    public void addData(List<EntidadeVO> obj) {

        for (EntidadeVO e : obj) {

            /** Create a TableRow dynamically **/
            tr = new TableRow(this);
            tr.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

            /** Creating a TextView to add to the row **/
            final SoftRadioButton chk = new SoftRadioButton(this, "RadioBtn1");
            chk.setText(String.valueOf(e.getId()));
            chk.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
            chk.setPadding(5, 5, 5, 5);
            chk.setTextColor(Color.BLACK);
            chk.setWidth(200);
            tr.addView(chk);  // Adding textView to tablerow.

            col1 = new TextView(this);
            col1.setText(e.getNome());
            col1.setTextColor(Color.BLACK);
            col1.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
            col1.setPadding(5, 5, 5, 5);
            tr.addView(col1);

//            col2 = new TextView(this);
//            col2.setText(e.getNome());
//            col2.setTextColor(Color.BLACK);
//            col2.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
//            col2.setPadding(5, 5, 5, 5);
//            tr.addView(col2);

            // Add the TableRow to the TableLayout
            tl.addView(tr, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));


            //separator
            TableRow tr = new TableRow(this);
            tr.setBackgroundColor(Color.GRAY);
            tr.setPadding(0, 0, 0, 2); //Border between rows

            TableRow.LayoutParams llp = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
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
                e.printStackTrace();
                return null;
            }


        }

        @Override
        protected void onPostExecute(List result) {

            if(result==null){
                return;
            }

            tl = (TableLayout) findViewById(R.id.maintable);
            tl.removeAllViewsInLayout();

            addHeaders();
            addData(result);

            if (progress != null && progress.isShowing()) {
                progress.dismiss();
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

            CadastrosControle cc = new CadastrosControle();

            try {

                EntidadeVO o = new EntidadeVO();
                o.setId(Integer.parseInt(param[0]));
                cc.excluirEntidade(o);

                return true;

            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }


        }

        @Override
        protected void onPostExecute(Boolean result) {

            if (progress != null && progress.isShowing()) {
                progress.dismiss();
            }

            if (result) {
                //atualiza lista de Entidades
                AsyncTask t = new popularGridTask().execute("");

                try {
                    t.get();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                Toast.makeText(getApplicationContext(), "Registro removido com sucesso!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Erro ao realizar a exclusão!", Toast.LENGTH_SHORT).show();
            }

        }

    }


    //region "Gravacao de tag"
    private boolean mResumed = false;
    private boolean mWriteMode = false;
    NfcAdapter mNfcAdapter;
    PendingIntent mNfcPendingIntent;
    IntentFilter[] mWriteTagFilters;
    IntentFilter[] mNdefExchangeFilters;
    private String techListsArray[][];

    private void disableNdefExchangeMode() {
        mNfcAdapter.disableForegroundNdefPush(this);
        mNfcAdapter.disableForegroundDispatch(this);
    }

    private void enableTagWriteMode() {
        mWriteMode = true;
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        mWriteTagFilters = new IntentFilter[]{
                tagDetected
        };
        mNfcAdapter.enableForegroundDispatch(this, mNfcPendingIntent, mWriteTagFilters, null);
    }

    private void disableTagWriteMode() {
        mWriteMode = false;
        mNfcAdapter.disableForegroundDispatch(this);
    }

    private void enableNdefExchangeMode() {
        //mNfcAdapter.enableForegroundNdefPush(StickyNotesActivity.this, getNoteAsNdef());
        mNfcAdapter.enableForegroundDispatch(this, mNfcPendingIntent, mNdefExchangeFilters, techListsArray);
    }

    @Override
    protected void onNewIntent(Intent intent) {

/*// NDEF exchange mode
        if (!mWriteMode && NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            NdefMessage[] msgs = getNdefMessages(intent);
            promptForContent(msgs[0]);
        }

        if (!mWriteMode && NfcAdapter.ACTION_TECH_DISCOVERED.equals(intent.getAction())) {
            NdefMessage[] msgs = getNdefMessages(intent);
            promptForContent(msgs[0]);
        }*/
        // Tag writing mode
        if (mWriteMode && NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
            Tag detectedTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            writeTag(getNoteAsNdef(), detectedTag);
        }
    }


    boolean writeTag(NdefMessage message, Tag tag) {
        int size = message.toByteArray().length;

        try {
            Ndef ndef = Ndef.get(tag);
            if (ndef != null) {
                ndef.connect();

                if (!ndef.isWritable()) {
                    toast("Tag is read-only.");
                    return false;
                }
                if (ndef.getMaxSize() < size) {
                    toast("Tag capacity is " + ndef.getMaxSize() + " bytes, message is " + size
                            + " bytes.");
                    return false;
                }

                ndef.writeNdefMessage(message);
                toast("Tag gravada com sucesso!");

                if (writeTagAlert != null) {
                    writeTagAlert.dismiss();
                    writeTagAlert = null;
                }


                return true;
            } else {
                NdefFormatable format = NdefFormatable.get(tag);
                if (format != null) {
                    try {
                        format.connect();
                        format.format(message);
                        toast("Formatted tag and wrote message");
                        return true;
                    } catch (IOException e) {
                        toast("Failed to format tag.");
                        return false;
                    }
                } else {
                    toast("Tag doesn't support NDEF.");
                    return false;
                }
            }
        } catch (Exception e) {
            toast("Failed to write tag");
        }

        return false;
    }

    private NdefMessage getNoteAsNdef() {
        byte[] textBytes = String.valueOf(registro).getBytes();
        NdefRecord textRecord = new NdefRecord(NdefRecord.TNF_MIME_MEDIA, "text/plain".getBytes(),
                new byte[]{}, textBytes);
        return new NdefMessage(new NdefRecord[]{
                textRecord
        });
    }

    private void toast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    //endregion


}
