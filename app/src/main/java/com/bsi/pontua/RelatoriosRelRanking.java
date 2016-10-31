package com.bsi.pontua;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import controle.CadastrosControle;
import utils.Utils;
import utils.charts.DayAxisValueFormatter;
import utils.charts.MyAxisValueFormatter;
import vo.EntidadeVO;
import vo.EventoVO;
import vo.RelEntidadeEventoVO;
import vo.RepRankingVO;


public class RelatoriosRelRanking extends Fragment {

    TableLayout tl;
    TableRow tr;
    TextView col1, col2, col3, col4;

    ProgressDialog progress;

    private BarChart mChart;

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


        View vw = inflater.inflate(R.layout.fragment_relatorios_rel_ranking, container, false);

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
        col1.setText("Entidade");
        col1.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        col1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        col1.setPadding(5, 5, 15, 0);
        tr.addView(col1);  // Adding textView to tablerow.


        TextView col2 = new TextView(getActivity());
        col2.setText("Saldo");
        col2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        col2.setPadding(5, 5, 15, 0);
        col2.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tr.addView(col2); // Adding textView to tablerow.


        TextView ncol3 = new TextView(getActivity());
        ncol3.setText("Máx. Possível");
        ncol3.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        ncol3.setPadding(5, 5, 15, 0);
        ncol3.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tr.addView(ncol3); // Adding textView to tablerow.


        TextView ncol4 = new TextView(getActivity());
        ncol4.setText("# Posição");
        ncol4.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        ncol4.setPadding(5, 5, 15, 0);
        ncol4.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tr.addView(ncol4); // Adding textView to tablerow.


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
        tr.setPadding(0, 0, 0, 2 ); //Border between rows

        TableRow.LayoutParams llp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        llp.setMargins(0, 0, 2, 0);//2px right-margin

        tl.addView(tr);

    }

    public void addData(List<RepRankingVO> obj) {

        for (RepRankingVO e : obj) {

            /** Create a TableRow dynamically **/
            tr = new TableRow(getActivity());
            tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));


            col1 = new TextView(getActivity());
            col1.setText(e.getEntidade().getNome());
            col1.setTextColor(Color.BLACK);
            col1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            col1.setPadding(5, 5, 5, 5);
            tr.addView(col1);

            col2 = new TextView(getActivity());
            col2.setText(Utils.formatarDoubleDecimal(e.getSaldoPontuacao().doubleValue()));
            col2.setTextColor(Color.BLACK);
            col2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            col2.setPadding(5, 5, 5, 5);
            tr.addView(col2);



            double d = 100 * e.getSaldoPontuacao().doubleValue() / e.getPontuacaoMaximaPossivel().doubleValue();
            BigDecimal perc = new BigDecimal(d).setScale(2, RoundingMode.HALF_EVEN);


            col3 = new TextView(getActivity());
            col3.setText(Utils.formatarDoubleDecimal(e.getPontuacaoMaximaPossivel().doubleValue()) + " (" + Utils.formatarDoubleDecimal(perc.doubleValue()) + "%)" );
            col3.setTextColor(Color.BLACK);
            col3.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            col3.setPadding(5, 5, 5, 5);
            tr.addView(col3);

            col4 = new TextView(getActivity());
            col4.setText(String.valueOf(e.getPosicao()) + "/" + obj.size() );
            col4.setTextColor(Color.BLACK);
            col4.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            col4.setPadding(5, 5, 5, 5);
            tr.addView(col4);


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
     }

    class popularGridTask extends AsyncTask<String, Integer, List> {

        @Override
        protected void onPreExecute() {
            inicializaProgressBar();
            progress.show();
        }

        @Override
        protected List<RepRankingVO> doInBackground(String... param) {

            CadastrosControle cc = new CadastrosControle();

            try {

                List<RepRankingVO> lista = cc.listarRelRanking(evento);
                return lista;

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(List result) {

            tl = (TableLayout) getActivity().findViewById(R.id.maintable);
            tl.removeAllViewsInLayout();

            addHeaders();
            if(result != null){
                addData(result);



                mChart = (BarChart) getActivity().findViewById(R.id.chart1);
                mChart.setVisibility(View.VISIBLE);
                //mChart.setOnChartValueSelectedListener(this);

                mChart.setDrawBarShadow(false);
                mChart.setDrawValueAboveBar(true);

                mChart.getDescription().setEnabled(false);



                mChart.setMaxVisibleValueCount(60);

                // scaling can now only be done on x- and y-axis separately
                mChart.setPinchZoom(false);

                mChart.setDrawGridBackground(false);
                // mChart.setDrawYLabels(false);

                IAxisValueFormatter xAxisFormatter = new DayAxisValueFormatter(mChart);

                XAxis xAxis = mChart.getXAxis();
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                //xAxis.setTypeface(mTfLight);
                xAxis.setDrawGridLines(false);
                xAxis.setGranularity(1f); // only intervals of 1 day
                xAxis.setLabelCount(7);
                xAxis.setValueFormatter(xAxisFormatter);

                IAxisValueFormatter custom = new MyAxisValueFormatter();

                YAxis leftAxis = mChart.getAxisLeft();
                //leftAxis.setTypeface(mTfLight);
                leftAxis.setLabelCount(8, false);
                leftAxis.setValueFormatter(custom);
                leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
                leftAxis.setSpaceTop(15f);
                leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

                YAxis rightAxis = mChart.getAxisRight();
                rightAxis.setDrawGridLines(false);
                //rightAxis.setTypeface(mTfLight);
                rightAxis.setLabelCount(8, false);
                rightAxis.setValueFormatter(custom);
                rightAxis.setSpaceTop(15f);
                rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

                Legend l = mChart.getLegend();
                l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
                l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
                l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
                l.setDrawInside(false);
                l.setForm(Legend.LegendForm.SQUARE);
                l.setFormSize(9f);
                l.setTextSize(11f);
                l.setXEntrySpace(4f);
                // l.setExtra(ColorTemplate.VORDIPLOM_COLORS, new String[] { "abc",
                // "def", "ghj", "ikl", "mno" });
                // l.setCustom(ColorTemplate.VORDIPLOM_COLORS, new String[] { "abc",
                // "def", "ghj", "ikl", "mno" });

//                XYMarkerView mv = new XYMarkerView(this, xAxisFormatter);
//                mv.setChartView(mChart); // For bounds control
//                mChart.setMarker(mv); // Set the marker to the chart


                //setData(12, 50);


                ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();



                List<RepRankingVO>  lst =  result;

                int i = 1;
                for (RepRankingVO e : lst) {

                    float val = (float) e.getSaldoPontuacao().floatValue();

                    yVals1.add(new BarEntry(i, val));

                    i++;
                }



                BarDataSet set1;

                if (mChart.getData() != null &&
                        mChart.getData().getDataSetCount() > 0) {
                    set1 = (BarDataSet) mChart.getData().getDataSetByIndex(0);
                    set1.setValues(yVals1);
                    mChart.getData().notifyDataChanged();
                    mChart.notifyDataSetChanged();
                } else {
                    set1 = new BarDataSet(yVals1, "");



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

                    set1.setColors(colors);

                    //set1.setColors(ColorTemplate.MATERIAL_COLORS);

                    ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
                    dataSets.add(set1);

                    BarData data = new BarData(dataSets);
                    data.setValueTextSize(10f);
                    //data.setValueTypeface(mTfLight);
                    data.setBarWidth(0.9f);

                    mChart.setData(data);
                }


                mChart.getLegend().setEnabled(false);

                mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);



            }

            if (progress != null && progress.isShowing()) {
                progress.dismiss();
            }
        }
    }



}