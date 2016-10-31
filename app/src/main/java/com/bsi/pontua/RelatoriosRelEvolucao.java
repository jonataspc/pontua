package com.bsi.pontua;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import controle.CadastrosControle;
import utils.Utils;
import vo.EntidadeVO;
import vo.EventoVO;
import vo.RepEvolucaoVO;
import vo.RepRankingVO;

import static android.R.attr.type;


public class RelatoriosRelEvolucao extends Fragment {

    TableLayout tl;
    TableRow tr;
    TextView col1, col2, col3, col4;
    private PieChart mChart;

    ProgressDialog progress;

    //evento selecionado
    public EventoVO evento;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        /*// Lookup the swipe container view
        final SwipeRefreshLayout swipeContainer = (SwipeRefreshLayout) getActivity().findViewById(R.id.swipeContainer);
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
                android.R.color.holo_red_light);*/


        View vw = inflater.inflate(R.layout.fragment_relatorios_rel_evolucao, container, false);

        //atualiza lista
        popularGridTask task = new popularGridTask();
        task.execute("");

        return vw;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    public void onStop() {
        //evita erro de leak
        super.onStop();

        if (progress != null) {
            progress.dismiss();
            progress = null;
        }
    }

    void inicializaProgressBar() {

        if (progress == null) {
            progress = new ProgressDialog(getActivity());
            progress.setTitle("");
            progress.setMessage("Aguarde...");
            progress.setIndeterminate(true);
            progress.setCancelable(false);
        }
    }


    public void addHeaders() {

        /** Create a TableRow **/
        tr = new TableRow(getActivity());
        tr.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.FILL_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT));

        /** Creating a TextView to add to the row **/
        TextView col1 = new TextView(getActivity());
        col1.setText("Total de Avaliações");
        col1.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        col1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        col1.setPadding(5, 5, 15, 0);
        tr.addView(col1);  // Adding textView to tablerow.


        TextView col2 = new TextView(getActivity());
        col2.setText("Avaliações pendentes");
        col2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        col2.setPadding(5, 5, 15, 0);
        col2.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tr.addView(col2); // Adding textView to tablerow.


        TextView ncol3 = new TextView(getActivity());
        ncol3.setText("% Concluído");
        ncol3.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        ncol3.setPadding(5, 5, 15, 0);
        ncol3.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tr.addView(ncol3); // Adding textView to tablerow.
//
//
//        TextView ncol4 = new TextView(getActivity());
//        ncol4.setText("# Posição");
//        ncol4.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
//        ncol4.setPadding(5, 5, 15, 0);
//        ncol4.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
//        tr.addView(ncol4); // Adding textView to tablerow.


        // Add the TableRow to the TableLayout
        tl.addView(tr, new TableLayout.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

        // we are adding two textviews for the <span id="IL_AD5" class="IL_AD">divider</span> because we have two columns
        tr = new TableRow(getActivity());
        tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));


        // Add the TableRow to the TableLayout
        tl.addView(tr, new TableLayout.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));


        //separator
        TableRow tr = new TableRow(getActivity());
        tr.setBackgroundColor(Color.GRAY);
        tr.setPadding(0, 0, 0, 2); //Border between rows

        TableRow.LayoutParams llp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        llp.setMargins(0, 0, 2, 0);//2px right-margin

        tl.addView(tr);

    }

    public void addData(RepEvolucaoVO e) {

        /** Create a TableRow dynamically **/
        tr = new TableRow(getActivity());
        tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));


        col1 = new TextView(getActivity());
        col1.setText(String.valueOf(e.getTotalLancamentos()));
        col1.setTextColor(Color.BLACK);
        col1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        col1.setPadding(5, 5, 5, 5);
        tr.addView(col1);

        col2 = new TextView(getActivity());
        col2.setText(String.valueOf(e.getTotalLancamentosPendentes()));
        col2.setTextColor(Color.BLACK);
        col2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        col2.setPadding(5, 5, 5, 5);
        tr.addView(col2);

        BigDecimal perc = new BigDecimal(0);

        try {
            double d = 0;
            d = 100 * ( Double.valueOf(e.getTotalLancamentos()) - Double.valueOf(e.getTotalLancamentosPendentes())) / Double.valueOf(e.getTotalLancamentos()) ;
            perc = new BigDecimal(d).setScale(2, RoundingMode.HALF_EVEN);
        } catch (Exception exc){

        }


        col3 = new TextView(getActivity());
        col3.setText(Utils.formatarDoubleDecimal(perc.doubleValue()) + "%");
        col3.setTextColor(Color.BLACK);
        col3.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        col3.setPadding(5, 5, 5, 5);
        tr.addView(col3);

//            col4 = new TextView(getActivity());
//            col4.setText(String.valueOf(e.getPosicao()) + "/" + obj.size() );
//            col4.setTextColor(Color.BLACK);
//            col4.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
//            col4.setPadding(5, 5, 5, 5);
//            tr.addView(col4);


        // Add the TableRow to the TableLayout
        tl.addView(tr, new TableLayout.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));


        //separator
        TableRow tr = new TableRow(getActivity());
        tr.setBackgroundColor(Color.GRAY);
        tr.setPadding(0, 0, 0, 2); //Border between rows

        TableRow.LayoutParams llp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        llp.setMargins(0, 0, 2, 0);//2px right-margin

        tl.addView(tr);


    }

    class popularGridTask extends AsyncTask<String, Integer, RepEvolucaoVO> {

        @Override
        protected void onPreExecute() {
            inicializaProgressBar();
            progress.show();
        }

        @Override
        protected RepEvolucaoVO doInBackground(String... param) {

            CadastrosControle cc = new CadastrosControle();

            try {
                return cc.consultarEvolucao(evento);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(RepEvolucaoVO result) {

            tl = (TableLayout) getActivity().findViewById(R.id.maintable);
            tl.removeAllViewsInLayout();

            addHeaders();
            if (result != null) {
                addData(result);


                /////

                mChart = (PieChart) getActivity().findViewById(R.id.chart1);
                mChart.setVisibility(View.VISIBLE);
                mChart.setUsePercentValues(true);
                mChart.getDescription().setEnabled(false);
                mChart.setExtraOffsets(5, 10, 5, 5);

                mChart.setDragDecelerationFrictionCoef(0.95f);

                //mChart.setCenterTextTypeface(mTfLight);
                //mChart.setCenterText(generateCenterSpannableText());

                mChart.setDrawHoleEnabled(false);
                //mChart.setHoleColor(Color.WHITE);

                mChart.setTransparentCircleColor(Color.WHITE);
                mChart.setTransparentCircleAlpha(110);

                //mChart.setHoleRadius(58f);
                mChart.setTransparentCircleRadius(61f);

                mChart.setDrawCenterText(true);

                mChart.setRotationAngle(0);
                // enable rotation of the chart by touch
                mChart.setRotationEnabled(true);
                mChart.setHighlightPerTapEnabled(true);

                // mChart.setUnit(" €");
                // mChart.setDrawUnitsInChart(true);

                // add a selection listener
                //mChart.setOnChartValueSelectedListener(getActivity());




                ArrayList<PieEntry> entries = new ArrayList<PieEntry>();




                entries.add(new PieEntry((float) (result.getTotalLancamentos() -  result.getTotalLancamentosPendentes()), "Realizadas"));
                entries.add(new PieEntry((float) result.getTotalLancamentosPendentes(), "Pendentes"));


                PieDataSet dataSet = new PieDataSet(entries, "");
                dataSet.setSliceSpace(3f);
                dataSet.setSelectionShift(5f);

                // add a lot of colors

                ArrayList<Integer> colors = new ArrayList<Integer>();

                for (int c : ColorTemplate.VORDIPLOM_COLORS)
                    colors.add(c);

                for (int c : ColorTemplate.JOYFUL_COLORS)
                    colors.add(c);

                for (int c : ColorTemplate.COLORFUL_COLORS)
                    colors.add(c);

                for (int c : ColorTemplate.LIBERTY_COLORS)
                    colors.add(c);

                for (int c : ColorTemplate.PASTEL_COLORS)
                    colors.add(c);

                colors.add(ColorTemplate.getHoloBlue());

                //dataSet.setColors(colors);
                dataSet.setColors(ColorTemplate.MATERIAL_COLORS);

                //dataSet.setSelectionShift(0f);

                PieData data = new PieData(dataSet);
                data.setValueFormatter(new PercentFormatter());
                data.setValueTextSize(16f);
                data.setValueTextColor(Color.BLUE);
                //data.setValueTypeface(mTfLight);
                mChart.setData(data);

                // undo all highlights
                mChart.highlightValues(null);

                //mChart.invalidate();
                ///


                mChart.getLegend().setEnabled(false);


                mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
// entry label styling
                mChart.setEntryLabelColor(Color.BLUE);
                //mChart.setEntryLabelTypeface(mTfRegular);
                mChart.setEntryLabelTextSize(18f);




                /////



            }

            if (progress != null && progress.isShowing()) {
                progress.dismiss();
            }
        }
    }


}