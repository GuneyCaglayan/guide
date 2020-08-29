package com.lyadirga.adresdefterim;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;


public class KisiEkleDuzenleFragment extends Fragment {

    private EditText ad,telefon,adres,mail;
    private Button kaydet;
    private TextInputLayout adSoyadInputLayout;
    private ImageView imageView;
    private Bundle arguments;
    private long id;
    private Bitmap bitmap;

    //keep track of camera capture intent
    final int CAMERA_CAPTURE = 1;
    final int PIC_CROP=2;
    //captured picture uri
    private Uri picUri;

    private KisiEkleDuzenleFragmentDinleyicisi dinleyici;

    public interface KisiEkleDuzenleFragmentDinleyicisi{

        // kişi ekleme veya düzenleme işlemi yapıldığında çağrılır.
        public void kisiEkleDuzenleIslemiYapildi(long id);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        dinleyici= (KisiEkleDuzenleFragmentDinleyicisi) context;

    }

    @Override
    public void onDetach() {
        super.onDetach();
        dinleyici=null;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.ekle_duzenle_fragment,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ad= (EditText) getActivity().findViewById(R.id.adSoyadEt);
        mail= (EditText) getActivity().findViewById(R.id.mailEt);
        adres= (EditText) getActivity().findViewById(R.id.adresEt);
        telefon= (EditText) getActivity().findViewById(R.id.telefonEt);
        kaydet= (Button) getActivity().findViewById(R.id.kaydet);
        adSoyadInputLayout= (TextInputLayout) getActivity().findViewById(R.id.adSoyadTextInputLayout);
        imageView= (ImageView) getActivity().findViewById(R.id.profile_image);


        arguments=getArguments();

        if (arguments!=null){
            //kişi düzenlenecekse
           id=arguments.getLong(MainActivity.ID);
           ad.setText(arguments.getString("ad"));
           mail.setText(arguments.getString("mail"));
           telefon.setText(arguments.getString("telefon"));
           adres.setText(arguments.getString("adres"));

            if (arguments.getParcelable("resim")!=null){
                bitmap=arguments.getParcelable("resim");
                imageView.setImageBitmap(bitmap);
            }

        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,CAMERA_CAPTURE);
            }
        });

        kaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ad.getText().toString().trim().length()!=0){
                    kaydetVeyaGuncelle();
                    dinleyici.kisiEkleDuzenleIslemiYapildi(id);

                }else{
                    adSoyadInputLayout.setError("Lütfen bir ad giriniz...");
                    ad.requestFocus();
                }
            }
        });
    }

    private void kaydetVeyaGuncelle() {

        KisiModel model=new KisiModel(ad.getText().toString(),mail.getText().toString(),telefon.getText().toString(),adres.getText().toString());
        Veritabani veritabani=new Veritabani(getActivity());

        if (bitmap!=null){
            byte [] byteArray = getByteArray(bitmap);
            model.setProfilFoto(byteArray);
        }

        if (arguments==null){
            //Yeni kayıt girilecek
            id = veritabani.kaydet(model);

        }else{
            //Güncelleme yapılacak
            veritabani.guncelle(id,model);
        }
    }

    private byte[] getByteArray(Bitmap bitmap) {

        ByteArrayOutputStream bos=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,0,bos);
        return bos.toByteArray();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode==getActivity().RESULT_OK){

            if (requestCode==CAMERA_CAPTURE){

                picUri = data.getData();
                kirpmaIstegi();
            }else if (requestCode==PIC_CROP){
                Bundle bundle=data.getExtras();
                bitmap=bundle.getParcelable("data");
                imageView.setImageBitmap(bitmap);
            }
        }
    }

    private void kirpmaIstegi() {

        try {

            //call the standard crop action intent (the user device may not support it)
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            //indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
            //set crop properties
            cropIntent.putExtra("crop", "true");
            //indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            //indicate output X and Y
            cropIntent.putExtra("outputX", 256);
            cropIntent.putExtra("outputY", 256);
            //retrieve data on return
            cropIntent.putExtra("return-data", true);
            //start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, PIC_CROP);

        }
        catch(ActivityNotFoundException anfe){
            //display an error message
            String errorMessage = "Telefonunuz fotoğraf kırpmayı desteklemiyor.";
            Toast toast = Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }

    }
}
