package com.lyadirga.adresdefterim;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;


public class KisiListesiFragment extends Fragment {

    private FloatingActionButton fab;
    private TextView tv_bos;
    private ListView liste;
    private CustomAdapter customAdapter;

    private KisiListesiFragmentDinleyicisi dinleyici;



    public interface KisiListesiFragmentDinleyicisi
    {
        // bir eleman seçildiğinde çağrılır
        public void elemanSecildi(long id);

        //Kişi ekleneceği zaman çağrılır, kişi ekleneceğini bildirir
        public void kisiEklermisinKardes();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.kisi_listesi_fragment,container,false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setRetainInstance(true);
        setHasOptionsMenu(true);
        // herhangi bir yapılandırma  değişikliği olduğunda
       // (ekran çevrildiğinde...) yeniden oluşturmak yerine alıkoyar.

        liste= (ListView) getActivity().findViewById(R.id.kisi_list);
        tv_bos= (TextView) getActivity().findViewById(R.id.tv_bos);
        fab= (FloatingActionButton) getActivity().findViewById(R.id.fab);

        liste.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        liste.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              dinleyici.elemanSecildi(id);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dinleyici.kisiEklermisinKardes();
            }
        });

        Veritabani veritabani=new Veritabani(getActivity());
        List<KisiModel> modelList = veritabani.getTumKisiler();

        if (modelList==null){
            tv_bos.setText("Şu anda herhangi bir kayıt bulunmuyor. Alttaki + butonuna dokunarak kişi kaydı ekleyebilirsiniz.");
        }else{
            customAdapter = new CustomAdapter(getActivity(),modelList);
            liste.setAdapter(customAdapter);
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        dinleyici= (KisiListesiFragmentDinleyicisi) context;

    }

    @Override
    public void onDetach() {
        super.onDetach();
        dinleyici=null;
    }



    public void guncelle() {
        Veritabani veritabani=new Veritabani(getActivity());
        List<KisiModel> modelList = veritabani.getTumKisiler();

        if (modelList==null){
            tv_bos.setText("Şu anda herhangi bir kayıt bulunmuyor. Alttaki + butonuna dokunarak kişi kaydı ekleyebilirsiniz.");
        }else{
            customAdapter = new CustomAdapter(getActivity(),modelList);
            liste.setAdapter(customAdapter);
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_kisi_list,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if (item.getItemId()==R.id.ayarlar)
            startActivity(new Intent(getActivity(),AyarlarActivity.class));

            return super.onOptionsItemSelected(item);

    }
}
