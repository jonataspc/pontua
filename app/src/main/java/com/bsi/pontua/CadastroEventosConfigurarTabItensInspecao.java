package com.bsi.pontua;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import controle.CadastrosControle;
import vo.AreaVO;
import vo.EventoVO;
import vo.ItemInspecaoVO;
import vo.RelItemInspecaoEventoVO;

public class CadastroEventosConfigurarTabItensInspecao extends Fragment implements View.OnClickListener {

    ProgressDialog progress;
    AlertDialog writeTagAlert;

    //lista de eventos exibidos
    private List<ItemInspecaoVO> listaItemInspecao; // lista total de obj retornados
    private List<ItemInspecaoVO> listaItemInspecaoPesquisadas; // lista contendo um subgrupo do listaItemInspecao que casam com o texto pesquisado
    private List<ItemInspecaoVO> selecionados; // lista de itens checados!

    //evento selecionado
    private EventoVO evento;


    private String TXT_AREA_DEFAULT = "[ÁREAS]";
    List<String> listaAreas;

    //relacao de relacionamentos atuais pesquisados na database
    List<RelItemInspecaoEventoVO> lstRelItemInspecaoEventoVO = new ArrayList<RelItemInspecaoEventoVO>(0);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Lookup the swipe container view
        final SwipeRefreshLayout swipeContainer = (SwipeRefreshLayout) getActivity().findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //atualiza
                carregarItemInspecao task = new carregarItemInspecao();
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

        View vw = inflater.inflate(R.layout.fragment_cadastro_eventos_configurar_tab_itens_inspecao, container, false);

        //salvar
        final Button btnSalvarConfig = (Button) vw.findViewById(R.id.btnSalvarConfigi);
        btnSalvarConfig.setOnClickListener(this);

        final CheckBox chkCheckAll = (CheckBox) vw.findViewById(R.id.chkCheckAlli);
        chkCheckAll.setOnClickListener(this);

        final Spinner dropdown = (Spinner) vw.findViewById(R.id.spnAreas);


        //carrega areas
        AsyncTask cAt =  new carregarAreasTask().execute("");

        /*try {
            //aguarda conclusao
            cAt.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }*/




        //atualiza lista
        new carregarItemInspecao().execute("");


        return vw;
    }

    class carregarAreasTask extends AsyncTask<String, Integer, List> {

        public String itemSelecionado=null;

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected List<AreaVO> doInBackground(String... param) {

            try(CadastrosControle cc = new CadastrosControle()){
                try {

                    List<AreaVO> lista = cc.listarAreas();
                    return lista;

                } catch (Exception e) {
                    e.printStackTrace();
                }

                return null;
            }


        }

        @Override
        protected void onPostExecute(List result) {

            //popula o spinner
            Spinner spnAreas = (Spinner) getActivity().findViewById(R.id.spnAreas);


            List<AreaVO> res2 = result;

            listaAreas = new ArrayList<String>(0);

            for (AreaVO item : res2) {
                listaAreas.add(item.getNome());
            }

            //listaAreas.add(0, TXT_AREA_NOVA);
            listaAreas.add(0, TXT_AREA_DEFAULT);

            String[] items = new String[listaAreas.size()];

            int cont = 0;
            for (String  item : listaAreas) {
                items[cont] =item;
                cont++;
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, items);
            spnAreas.setAdapter(adapter);




            //add evento
            spnAreas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                    if(listaItemInspecao==null || listaItemInspecao.size()==0){
                        return;
                    }

                    //selecionou o [Novo] ?
                    if (parentView.getSelectedItem().toString().equals(TXT_AREA_DEFAULT)) {

                        //default, carrega todos
                        listaItemInspecaoPesquisadas = new ArrayList<ItemInspecaoVO>(0);
                        listaItemInspecaoPesquisadas = listaItemInspecao;

                    } else {
                        //gera nova lista com apenas os casos que conferem com a pesquisa
                        listaItemInspecaoPesquisadas = new ArrayList<ItemInspecaoVO>(0);

                        if(listaItemInspecao==null){
                            return;
                        }

                        //verifica quais os items que casam
                        for(ItemInspecaoVO item : listaItemInspecao){
                            if(item.getArea().getNome().toUpperCase().equals(parentView.getSelectedItem().toString().toUpperCase())){
                                listaItemInspecaoPesquisadas.add(item);
                            }
                        }
                    }

                    //refresh
                    verificarAllChecked();
                    carregaListView();

                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                }
            });

            /*////pre seleciona o inserido
            if(itemSelecionado!=null){
                spnAreas.setSelection(((ArrayAdapter) spnAreas.getAdapter()).getPosition(itemSelecionado.toUpperCase()));
            }*/

        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSalvarConfigi:

                //Toast.makeText(getActivity(), String.valueOf(selecionados.size()), Toast.LENGTH_SHORT).show();

                salvarRelac task = new salvarRelac();
                task.execute("");
                break;


            case R.id.chkCheckAlli:
                //check all

                //se estiver pesquisando e clicar em selecionar todos, o 'todos' deverá ser apenas os exibidos
                //final EditText inputSearch = (EditText) vw.findViewById(R.id.inputSearch);
                //final boolean isSearching = inputSearch.getText()

                final CheckBox chk = (CheckBox) v;
                final ListView lv = (ListView) getActivity().findViewById(R.id.listView1i);

                for(int i=0; i < listaItemInspecaoPesquisadas.size(); i++){
                    LinearLayout itemLayout = (LinearLayout)lv.getChildAt(i);

                    if(itemLayout != null){
                        CheckBox cb = (CheckBox)itemLayout.findViewById(R.id.checkBox1);
                        cb.setChecked(chk.isChecked());
                    }

                    if(chk.isChecked()){
                        if(!selecionados.contains(listaItemInspecaoPesquisadas.get(i))){
                            selecionados.add(listaItemInspecaoPesquisadas.get(i));
                        }
                    } else {
                        if(selecionados.contains(listaItemInspecaoPesquisadas.get(i)))
                            selecionados.remove(listaItemInspecaoPesquisadas.get(i));
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

        if(listaItemInspecaoPesquisadas.size()==0){
            allSelected = false;
        }

        for(ItemInspecaoVO item : listaItemInspecaoPesquisadas){
            if(!selecionados.contains(item)){
                allSelected = false;
            }
        }

        final CheckBox chkCheckAll = (CheckBox) getActivity().findViewById(R.id.chkCheckAlli);
        chkCheckAll.setChecked(allSelected);

    }

    private void carregaListView() {

        final List<ItemInspecaoVO> lst = listaItemInspecaoPesquisadas;

        final ListView lv;
        lv = (ListView) getActivity().findViewById(R.id.listView1i);

        //altera txt do marcar todos
        final CheckBox chkCheckAll = (CheckBox) getActivity().findViewById(R.id.chkCheckAlli);
        chkCheckAll.setText("Selecionar todos (" + lst.size() + ")");

        // Adapter para implementar o layout customizado de cada item
        ArrayAdapter<List> lsvEstadosAdapter = new ArrayAdapter<List>(getActivity(), R.layout.row ) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                LayoutInflater inflater = ((Activity)getActivity()).getLayoutInflater();
                convertView = inflater.inflate(R.layout.row, parent, false);

                CheckBox cb = (CheckBox) convertView.findViewById(R.id.checkBox1);
                cb.setText("[" + lst.get(position).getArea().getNome() + "] " + lst.get(position).getNome());

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

                        ItemInspecaoVO obj = (ItemInspecaoVO) chk.getTag();

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

    class carregarItemInspecao extends AsyncTask<String, Integer, List> {

        boolean isSwipe = false;

        @Override
        protected void onPreExecute() {
            if(!isSwipe){
                inicializaProgressBar();
                progress.show();
            }
        }

        @Override
        protected List<ItemInspecaoVO> doInBackground(String... param) {

            try(CadastrosControle cc = new CadastrosControle()){

                try {

                    List<ItemInspecaoVO> lista = cc.listarItemInspecao("");

                    //obtem lista de eventos
                    lstRelItemInspecaoEventoVO = cc.listarRelItemInspecaoEventoPorEvento(evento);

                    return lista;

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }



        }

        @Override
        protected void onPostExecute(List result) {

            if(result==null){
                return;
            }

            //reinicializa obj
            selecionados = new ArrayList<ItemInspecaoVO>(0);
            listaItemInspecao = result;
            listaItemInspecaoPesquisadas = listaItemInspecao;


            //obtem registros já inseridos em DB e mantem marcados...
            for(RelItemInspecaoEventoVO relItem : lstRelItemInspecaoEventoVO){

                for(ItemInspecaoVO entItem : listaItemInspecao){

                    if(entItem.getId() == relItem.getItemInspecao().getId()){
                        //adiciona em selecionados
                        if(!selecionados.contains(entItem)){
                            selecionados.add(entItem);
                        }
                    }

                }
            }


            //desmarca chkAll
            final CheckBox chkCheckAll = (CheckBox) getActivity().findViewById(R.id.chkCheckAlli);
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

            try(CadastrosControle cc = new CadastrosControle()){
                try {

                    //obtem items selecionados
                    for(ItemInspecaoVO item : selecionados){

                        //instancia novo objeto e define relacionamento
                        RelItemInspecaoEventoVO newRelac = new RelItemInspecaoEventoVO();
                        newRelac.setItemInspecao(item);
                        newRelac.setEvento(evento);

                        //se nao existir, cadastra
                        if(!cc.existeRelItemInspecaoEvento(newRelac)){
                            cc.incluirRelItemInspecaoEvento(newRelac);
                            totalInseridos++;
                        }
                    }


                    //obtem items atuais (atualizado) em BD e remove caso nao esteja no SELECIONADOS
                    lstRelItemInspecaoEventoVO = cc.listarRelItemInspecaoEventoPorEvento(evento);

                    for(RelItemInspecaoEventoVO item : lstRelItemInspecaoEventoVO){

                        boolean localizado = false;

                        for(ItemInspecaoVO entItem : selecionados){
                            if(entItem.getId() == item.getItemInspecao().getId()){
                                localizado = true;
                            }
                        }

                        if(!localizado){
                            //nao esta selecionado, remove do BD
                            cc.excluirRelItemInspecaoEvento(item);
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