package com.bsi.pontua;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import testes.Testes;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        new testexx().execute("");

    }

    class testexx extends AsyncTask<String, Integer, String> {




        @Override
        protected void onPreExecute() {
            final TextView lbl = (TextView) findViewById(R.id.txt1);
            lbl.setText("WAIT");
        }


        @Override
        protected String doInBackground(String... param) {



            Testes test = new Testes();

            try {
                return  test.Testar();


            }catch (Exception e){

                return e.getMessage() + "\n" + e.getStackTrace();
            }



        }

        /*
        @Override
        protected void onProgressUpdate(Integer... values) {
            mProgressBar.setProgress(values[0]);
        }
        */

        @Override
        protected void onPostExecute(String result) {



            final TextView lbl = (TextView) findViewById(R.id.txt1);
lbl.setText(result);

        }


    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
