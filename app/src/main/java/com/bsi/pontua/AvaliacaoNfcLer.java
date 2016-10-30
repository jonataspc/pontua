package com.bsi.pontua;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
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
import android.os.Parcelable;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Date;

import controle.CadastrosControle;
import utils.Utils;
import vo.AvaliacaoVO;

public class AvaliacaoNfcLer extends AppCompatActivity {

    PendingIntent mNfcPendingIntent;
    IntentFilter[] mWriteTagFilters;
    IntentFilter[] mNdefExchangeFilters;
    ProgressDialog progress;
    Bundle b = null;


    AvaliacaoVO objAvaliacao;
    boolean deveSobrescrever = false;

    private boolean mWriteMode = false;
    private NfcAdapter mNfcAdapter;
    private String techListsArray[][];

    @Override
    protected void onResume() {
        super.onResume();


       /* // Sticky notes received from Android
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
            NdefMessage[] messages = getNdefMessages(getIntent());
            byte[] payload = messages[0].getRecords()[0].getPayload();
            setNoteBody(new String(payload));
            setIntent(new Intent()); // Consume this intent.
        }*/

        //mNfcAdapter.enableForegroundNdefPush(StickyNotesActivity.this, getNoteAsNdef());
        mNfcAdapter.enableForegroundDispatch(this, mNfcPendingIntent, mNdefExchangeFilters, techListsArray);


    }

    private void disableTagWriteMode() {
        mWriteMode = false;
        mNfcAdapter.disableForegroundDispatch(this);
    }

//    boolean writeTag(NdefMessage message, Tag tag) {
//        int size = message.toByteArray().length;
//
//        try {
//            Ndef ndef = Ndef.get(tag);
//            if (ndef != null) {
//                ndef.connect();
//
//                if (!ndef.isWritable()) {
//                    toast("Tag is read-only.");
//                    return false;
//                }
//                if (ndef.getMaxSize() < size) {
//                    toast("Tag capacity is " + ndef.getMaxSize() + " bytes, message is " + size
//                            + " bytes.");
//                    return false;
//                }
//
//                ndef.writeNdefMessage(message);
//                toast("Wrote message to pre-formatted tag.");
//                return true;
//            } else {
//                NdefFormatable format = NdefFormatable.get(tag);
//                if (format != null) {
//                    try {
//                        format.connect();
//                        format.format(message);
//                        toast("Formatted tag and wrote message");
//                        return true;
//                    } catch (IOException e) {
//                        toast("Failed to format tag.");
//                        return false;
//                    }
//                } else {
//                    toast("Tag doesn't support NDEF.");
//                    return false;
//                }
//            }
//        } catch (Exception e) {
//            toast("Failed to write tag");
//        }
//
//        return false;
//    }

    @Override
    public void onPause() {

        mNfcAdapter.disableForegroundNdefPush(this);
        mNfcAdapter.disableForegroundDispatch(this);


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
            progress = new ProgressDialog(this);
            progress.setTitle("");
            progress.setMessage("Aguarde...");
            progress.setIndeterminate(true);
            progress.setCancelable(false);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avaliacao_nfc_ler);

        setTitle("Avaliação por NFC - Leituras");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Intent intent = getIntent();


        //recupera obj avaliacao
        objAvaliacao = (AvaliacaoVO) intent.getSerializableExtra("objAvaliacaoVO");
        deveSobrescrever = (boolean) intent.getSerializableExtra("sobrescrever");


        //labels
        final TextView lblEvento = (TextView) findViewById(R.id.lblEvento);
        lblEvento.setText(objAvaliacao.getRelEntidadeEvento().getEvento().getNome());

        final TextView lblItem = (TextView) findViewById(R.id.lblItem);
        lblItem.setText(objAvaliacao.getRelItemInspecaoEvento().getItemInspecao().getArea().getNome() + " -  " + objAvaliacao.getRelItemInspecaoEvento().getItemInspecao().getNome() );

        final TextView lblValor = (TextView) findViewById(R.id.lblValor);
        lblValor.setText(Utils.formatarDoubleDecimal(objAvaliacao.getPontuacao().doubleValue()));


        startLeitura();

    }


    void writeLog(String txt) {

        String dthr;
        Date now = new Date();

        dthr = "[" + Utils.formatarHora(now) + "]";

        final TextView txtLog = (TextView) findViewById(R.id.lblLog);
        txtLog.setText(dthr + " " + txt + "\n" + txtLog.getText());

    }

    void startLeitura() {

        writeLog("Aguardando leituras...");


        //start nfc
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (mNfcAdapter == null) {
            // Stop here, we definitely need NFC
            Toast.makeText(this, "Este dispositivo não suporta NFC.", Toast.LENGTH_LONG).show();
            finish();
            return;

        }

        if (!mNfcAdapter.isEnabled()) {
            Toast.makeText(this, "NFC está desativado. Ligue-o e tente novamente.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }


        // Handle all of our received NFC intents in this activity.
        mNfcPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        // Intent filters for reading a note from a tag or exchanging over p2p.
        IntentFilter ndefDetected = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            ndefDetected.addDataType("*/*");
        } catch (MalformedMimeTypeException e) {
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


    }

    @Override
    protected void onNewIntent(Intent intent) {
        // NDEF exchange mode
        if (!mWriteMode && (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction()) ||
                            NfcAdapter.ACTION_TECH_DISCOVERED.equals(intent.getAction()) )) {
            NdefMessage[] msgs = getNdefMessages(intent);
            //promptForContent(msgs[0]);
            String body = new String(msgs[0].getRecords()[0].getPayload());

            //TODO: trata
            if(body.trim().length()==0) {
                body = "33"; //fake;
            }


            if(body.trim().length()==0){
                writeLog("Tag vazia");
            }else{

                int idEnt=-1;

                try {
                    idEnt=Integer.parseInt(body);
                }catch (Exception e){
                    e.printStackTrace();
                }

                if(idEnt==-1){
                    writeLog("Tag não numérica (" + body + ")");
                }else{
                    lancarPontuacao(idEnt);
                }



            }


        }
/*
        // Tag writing mode
        if (mWriteMode && NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
            Tag detectedTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            writeTag(getNoteAsNdef(), detectedTag);
        }*/

    }

/*    private void promptForContent(final NdefMessage msg) {
        new AlertDialog.Builder(this).setTitle("Replace current content?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        String body = new String(msg.getRecords()[0].getPayload());
                        setNoteBody(body);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                }).show();
    }*/

/*    private void setNoteBody(String body) {
        writeLog(body);
    }*/


    NdefMessage[] getNdefMessages(Intent intent) {
        // Parse the intent
        NdefMessage[] msgs = null;
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            if (rawMsgs != null) {
                msgs = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                }
            } else {
                // Unknown tag type
                byte[] empty = new byte[]{};
                NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN, empty, empty, empty);
                NdefMessage msg = new NdefMessage(new NdefRecord[]{
                        record
                });
                msgs = new NdefMessage[]{
                        msg
                };
            }
        } else {
            Log.d("Pontua", "Unknown intent.");
            finish();
        }
        return msgs;
    }

    private NdefMessage getNoteAsNdef() {
        byte[] textBytes = "blablal".toString().getBytes();
        NdefRecord textRecord = new NdefRecord(NdefRecord.TNF_MIME_MEDIA, "text/plain".getBytes(),
                new byte[]{}, textBytes);
        return new NdefMessage(new NdefRecord[]{
                textRecord
        });
    }

    void lancarPontuacao(int idEntidade) {

        //efetua lancamento
        Integer[] paramns = new Integer[]{idEntidade};

        new efetuarLancamentoTask().execute(paramns);
    }

//    private class NdefReaderTask extends AsyncTask<Tag, Void, String> {
//
//        @Override
//        protected String doInBackground(Tag... params) {
//            Tag tag = params[0];
//
//            Ndef ndef = Ndef.get(tag);
//            if (ndef == null) {
//                // NDEF is not supported by this Tag.
//                return null;
//            }
//
//            NdefMessage ndefMessage = ndef.getCachedNdefMessage();
//
//            NdefRecord[] records = ndefMessage.getRecords();
//            for (NdefRecord ndefRecord : records) {
//                if (ndefRecord.getTnf() == NdefRecord.TNF_WELL_KNOWN && Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_TEXT)) {
//                    try {
//                        return readText(ndefRecord);
//                    } catch (UnsupportedEncodingException e) {
//                        Log.e("Pontua", "Unsupported Encoding", e);
//                    }
//                }
//            }
//
//            return null;
//        }
//
//        private String readText(NdefRecord record) throws UnsupportedEncodingException {
//        /*
//         * See NFC forum specification for "Text Record Type Definition" at 3.2.1
//         *
//         * http://www.nfc-forum.org/specs/
//         *
//         * bit_7 defines encoding
//         * bit_6 reserved for future use, must be 0
//         * bit_5..0 length of IANA language code
//         */
//
//            byte[] payload = record.getPayload();
//
//            // Get the Text Encoding
//            String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";
//
//            // Get the Language Code
//            int languageCodeLength = payload[0] & 0063;
//
//            // String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");
//            // e.g. "en"
//
//            // Get the Text
//            return new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            if (result != null) {
//                // mTextView.setText("Read content: " + result);
//                writeLog(result);
//            }
//        }
//    }

    class efetuarLancamentoTask extends AsyncTask<Integer, Integer, Boolean> {

        private String errorMsg=null;

        @Override
        protected void onPreExecute() {
            inicializaProgressBar();
            progress.show();
        }

        @Override
        protected Boolean doInBackground(Integer... param) {

            try(CadastrosControle cc = new CadastrosControle()){

                try {

                    Boolean retorno=false;

                    //preenche entidade
                    objAvaliacao.getRelEntidadeEvento().setEntidade(cc.obterEntidadePorId(param[0]));

                    //verifica se existe avaliacao...
                    boolean oExiste = cc.localizarAvaliacaoRealizada(objAvaliacao);

                    if(oExiste){
                        //verifica o que faz???
                        if(deveSobrescrever){
                            //remove e continua...
                            cc.excluirAvaliacao(objAvaliacao);
                        } else {
                            //msg de alerta
                            errorMsg = "Avaliação já realizada";
                            return false;
                        }
                    }

                    try{
                        retorno = cc.inserirAvaliacao(objAvaliacao);
                    }catch (Exception ee){
                        //se ocorreu erro, pode ser de FK.
                        //neste caso, verifica se preenchei o ID do getRelEntidadeEvento
                        //caso seja ZERO, a entidade nao faz parte deste evento

                        if(objAvaliacao.getRelEntidadeEvento().getId()==0){
                            errorMsg = "Entidade não faz parte deste evento";
                            return null;
                        } else {
                            throw ee; // propaga
                        }

                    }


                    return retorno;

                } catch (Exception e) {
                    e.printStackTrace();
                    errorMsg = e.getMessage();
                    return null;
                }


            }

        }

        @Override
        protected void onPostExecute(Boolean result) {

            if (progress != null && progress.isShowing()) {
                progress.dismiss();
            }


            String strMsg;

            if (result!=null && result==true) {
                strMsg = "Lanç. efetuado (" + objAvaliacao.getRelEntidadeEvento().getEntidade().getNome().toUpperCase().trim() + ") ";


                //som
                try {
                    Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                    r.play();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //vibra
                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                // Vibrate for X milliseconds
                v.vibrate(100);



            } else {
                strMsg = "Erro (" + objAvaliacao.getRelEntidadeEvento().getEntidade().getNome() + ") - " + errorMsg;
            }

            writeLog(strMsg);
            Toast.makeText(getApplicationContext(), strMsg, Toast.LENGTH_SHORT).show();


        }
    }


}
