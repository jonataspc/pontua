package com.bsi.pontua;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import controle.CadastrosControle;
import dao.RelEntidadeEventoDAO;
import vo.EntidadeVO;
import vo.EventoVO;
import vo.RelEntidadeEventoVO;


public class CadastroEventosConfigurarTabEntidades extends Fragment  implements View.OnClickListener {
//    /*// TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
//
//    private OnFragmentInteractionListener mListener;
//
//    public CadastroEventosConfigurarTabEntidades() {
//        // Required empty public constructor
//    }
//
//    *//**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment CadastroEventosConfigurarTabEntidades.
//     *//*
//    // TODO: Rename and change types and number of parameters
//    public static CadastroEventosConfigurarTabEntidades newInstance(String param1, String param2) {
//        CadastroEventosConfigurarTabEntidades fragment = new CadastroEventosConfigurarTabEntidades();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_cadastro_eventos_configurar_tab_entidades, container, false);
//    }
//
//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }
//
//    *//**
//     * This interface must be implemented by activities that contain this
//     * fragment to allow an interaction in this fragment to be communicated
//     * to the activity and potentially other fragments contained in that
//     * activity.
//     * <p/>
//     * See the Android Training lesson <a href=
//     * "http://developer.android.com/training/basics/fragments/communicating.html"
//     * >Communicating with Other Fragments</a> for more information.
//     *//*
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }*/

    //TableLayout tl;
    //TableRow tr;
    //TextView col1, col2;



    ProgressDialog progress;
    AlertDialog writeTagAlert;

    //lista de eventos exibidos
    private List<EntidadeVO> listaEntidades; // lista total de obj retornados
    private List<EntidadeVO> listaEntidadesPesquisadas; // lista contendo um subgrupo do listaEntidades que casam com o texto pesquisado
    private List<EntidadeVO> selecionados; // lista de itens checados!

    //evento selecionado
    private EventoVO evento;

    //relacao de relacionamentos atuais pesquisados na database
    List<RelEntidadeEventoVO> lstRelEntidadeEventoVO = new ArrayList<RelEntidadeEventoVO>(0);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Lookup the swipe container view
        final SwipeRefreshLayout swipeContainer = (SwipeRefreshLayout) getActivity().findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //atualiza
                carregarEntidades task = new carregarEntidades();
                task.isSwipe = true;
                task.execute("");

            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_green_light,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        //define evento
        evento = (EventoVO) getActivity().getIntent().getSerializableExtra("oEvento");

        View vw = inflater.inflate(R.layout.fragment_cadastro_eventos_configurar_tab_entidades, container, false);

        //salvar
        final Button btnSalvarConfig = (Button) vw.findViewById(R.id.btnSalvarConfig);
        btnSalvarConfig.setOnClickListener(this);

        final CheckBox chkCheckAll = (CheckBox) vw.findViewById(R.id.chkCheckAll);
        chkCheckAll.setOnClickListener(this);

        final EditText inputSearch = (EditText) vw.findViewById(R.id.inputSearch);
        inputSearch.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                //gera nova lista com apenas os casos que conferem com a pesquisa
                listaEntidadesPesquisadas = new ArrayList<EntidadeVO>(0);

                if(listaEntidades==null){
                    return;
                }

                //verifica quais os items que casam
                for(EntidadeVO item : listaEntidades){
                    if(item.getNome().toUpperCase().contains(inputSearch.getText().toString().toUpperCase())){
                        listaEntidadesPesquisadas.add(item);
                    }
                }

                verificarAllChecked();
                carregaListView();

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });



        //atualiza lista
        new carregarEntidades().execute("");


        return vw;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSalvarConfig:

                //Toast.makeText(getActivity(), String.valueOf(selecionados.size()), Toast.LENGTH_SHORT).show();

                salvarRelac task = new salvarRelac();
                task.execute("");
                break;


            case R.id.chkCheckAll:
                //check all

                //se estiver pesquisando e clicar em selecionar todos, o 'todos' deverá ser apenas os exibidos
                //final EditText inputSearch = (EditText) vw.findViewById(R.id.inputSearch);
                //final boolean isSearching = inputSearch.getText()

                final CheckBox chk = (CheckBox) v;
                final ListView lv = (ListView) getActivity().findViewById(R.id.listView1);

                for(int i=0; i < listaEntidadesPesquisadas.size(); i++){
                    LinearLayout itemLayout = (LinearLayout)lv.getChildAt(i);

                    if(itemLayout != null){
                        CheckBox cb = (CheckBox)itemLayout.findViewById(R.id.checkBox1);
                        cb.setChecked(chk.isChecked());
                    }

                    if(chk.isChecked()){
                        if(!selecionados.contains(listaEntidadesPesquisadas.get(i))){
                            selecionados.add(listaEntidadesPesquisadas.get(i));
                        }
                    } else {
                        if(selecionados.contains(listaEntidadesPesquisadas.get(i)))
                            selecionados.remove(listaEntidadesPesquisadas.get(i));
                    }

                }


                break;

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

    private void verificarAllChecked(){
        //verifica se todos os item ja estao checados. Caso estejam, deixa checado o chkAll
        boolean allSelected = true;

        if(listaEntidadesPesquisadas.size()==0){
            allSelected = false;
        }

        for(EntidadeVO item : listaEntidadesPesquisadas){
            if(!selecionados.contains(item)){
                allSelected = false;
            }
        }

        final CheckBox chkCheckAll = (CheckBox) getActivity().findViewById(R.id.chkCheckAll);
        chkCheckAll.setChecked(allSelected);

    }

    private void carregaListView() {

        final List<EntidadeVO> lst = listaEntidadesPesquisadas;

        final ListView lv;
        lv = (ListView) getActivity().findViewById(R.id.listView1);

        //altera txt do marcar todos
        final CheckBox chkCheckAll = (CheckBox) getActivity().findViewById(R.id.chkCheckAll);
        chkCheckAll.setText("Selecionar todas (" + lst.size() + ")");

        // Adapter para implementar o layout customizado de cada item
        ArrayAdapter<List> lsvEstadosAdapter = new ArrayAdapter<List>(getActivity(), R.layout.row ) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                LayoutInflater inflater = ((Activity)getActivity()).getLayoutInflater();
                convertView = inflater.inflate(R.layout.row, parent, false);

                CheckBox cb = (CheckBox) convertView.findViewById(R.id.checkBox1);
                cb.setText(lst.get(position).getNome());

                if(selecionados.contains(lst.get(position))) {
                    cb.setChecked(true);
                } else {
                    cb.setChecked(false);
                }

                cb.setTag(lst.get(position));

                cb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        CheckBox chk = (CheckBox) v;

                        EntidadeVO obj = (EntidadeVO) chk.getTag();

                        if(chk.isChecked()) {
                            if(!selecionados.contains(obj))
                                selecionados.add(obj);
                        } else {
                            if(selecionados.contains(obj))
                                selecionados.remove(obj);
                        }

                        verificarAllChecked();
                    }
                });

                return convertView;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public int getCount() {
                return lst.size();
            }
        };
        lv.setAdapter(lsvEstadosAdapter);


        ///
        lv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                boolean enable = false;
                if(lv != null && lv.getChildCount() > 0){
                    // check if the first item of the list is visible
                    boolean firstItemVisible = lv.getFirstVisiblePosition() == 0;
                    // check if the top of the first item is visible
                    boolean topOfFirstItemVisible = lv.getChildAt(0).getTop() == 0;
                    // enabling or disabling the refresh layout
                    enable = firstItemVisible && topOfFirstItemVisible;
                }
                final SwipeRefreshLayout swipeContainer = (SwipeRefreshLayout) getActivity().findViewById(R.id.swipeContainer);

                swipeContainer.setEnabled(enable);
            }

        });



    }

    class carregarEntidades extends AsyncTask<String, Integer, List> {

        boolean isSwipe = false;

        @Override
        protected void onPreExecute() {
            if(!isSwipe){
                inicializaProgressBar();
                progress.show();
            }
        }

        @Override
        protected List<EntidadeVO> doInBackground(String... param) {

            CadastrosControle cc = new CadastrosControle();

            try {

                List<EntidadeVO> lista = cc.listarEntidade("");

                //obtem lista de eventos
                lstRelEntidadeEventoVO = cc.listarRelEntidadeEventoPorEvento(evento);

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

            //reinicializa obj
            selecionados = new ArrayList<EntidadeVO>(0);
            listaEntidades = result;
            listaEntidadesPesquisadas = listaEntidades;


            //obtem registros já inseridos em DB e mantem marcados...
            for(RelEntidadeEventoVO relItem : lstRelEntidadeEventoVO){

                for(EntidadeVO entItem : listaEntidades){

                    if(entItem.getId() == relItem.getEntidade().getId()){
                        //adiciona em selecionados
                        if(!selecionados.contains(entItem)){
                            selecionados.add(entItem);
                        }
                    }

                }
            }


            //desmarca chkAll
            final CheckBox chkCheckAll = (CheckBox) getActivity().findViewById(R.id.chkCheckAll);
            chkCheckAll.setChecked(false);


            carregaListView();
            verificarAllChecked();

            if (progress != null && progress.isShowing()) {
                progress.dismiss();
            }


            if(isSwipe){
                final SwipeRefreshLayout swipeContainer = (SwipeRefreshLayout) getActivity().findViewById(R.id.swipeContainer);
                swipeContainer.setRefreshing(false);
            }



        }


    }



    class salvarRelac extends AsyncTask<String, Integer, Boolean> {

        private boolean isSwipe = false;
        String errorMsg;

        private int totalInseridos = 0;
        private int totalRemovidos = 0;

        @Override
        protected void onPreExecute() {
            if(!isSwipe){
                inicializaProgressBar();
                progress.show();
            }
        }

        @Override
        protected Boolean doInBackground(String... param) {

            CadastrosControle cc = new CadastrosControle();

            try {

                //obtem items selecionados
                for(EntidadeVO item : selecionados){

                    //instancia novo objeto e define relacionamento
                    RelEntidadeEventoVO newRelac = new RelEntidadeEventoVO();
                    newRelac.setEntidade(item);
                    newRelac.setEvento(evento);

                    //se nao existir, cadastra
                    if(!cc.existeRelEntidadeEvento(newRelac)){
                        cc.incluirRelEntidadeEvento(newRelac);
                        totalInseridos++;
                    }
                }


                //obtem items atuais (atualizado) em BD e remove caso nao esteja no SELECIONADOS
                lstRelEntidadeEventoVO = cc.listarRelEntidadeEventoPorEvento(evento);

                for(RelEntidadeEventoVO item : lstRelEntidadeEventoVO){

                    boolean localizado = false;

                    for(EntidadeVO entItem : selecionados){
                        if(entItem.getId() == item.getEntidade().getId()){
                            localizado = true;
                        }
                    }

                    if(!localizado){
                        //nao esta selecionado, remove do BD
                        cc.excluirRelEntidadeEvento(item);
                        totalRemovidos++;
                    }

                }


            return true;

            } catch (Exception e) {
                e.printStackTrace();
                errorMsg =  e.getMessage();
                return false;
            }

        }

        @Override
        protected void onPostExecute(Boolean result) {

            if(result==null){
                return;
            }

            if (progress != null && progress.isShowing()) {
                progress.dismiss();
            }

            if(result){
                Toast.makeText(getActivity(), "Configurações salvas com sucesso!\nItens inseridos: " + totalInseridos + "\nItens removidos: " + totalRemovidos , Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_SHORT).show();
            }




        }


    }


}