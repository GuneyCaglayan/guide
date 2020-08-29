package com.lyadirga.adresdefterim;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by kiosk on 29/10/2016.
 */

public class CustomAdapter extends BaseAdapter {

    private Context context;
    private List<KisiModel> modelList;

    public CustomAdapter(Context context, List<KisiModel> modelList) {

        this.context = context;
        this.modelList = modelList;

    }

    @Override
    public int getCount() {

        if (modelList!=null)
        return modelList.size();

        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (modelList!=null)
        return modelList.get(position);

        return null;
    }

    @Override
    public long getItemId(int position) {

        if (modelList!=null)
        return modelList.get(position).getId();

        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (modelList==null)
        return null;

        LinearLayout container= (LinearLayout) ((Activity)context).getLayoutInflater().inflate(R.layout.custom_listview,null);

        ImageView profil= (ImageView) container.findViewById(R.id.profil_custom_list);
        TextView adSoyad= (TextView) container.findViewById(R.id.adCustomList);
        TextView mail= (TextView) container.findViewById(R.id.mailCustomList);
        TextView telefon= (TextView) container.findViewById(R.id.telefonCustomList);

        KisiModel model = modelList.get(position);

        adSoyad.setText(model.getAd());
        mail.setText(model.getMail());
        telefon.setText(model.getTelefon());

        if (model.getProfilFoto()!=null){

            byte [] bytes = model.getProfilFoto();
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
            profil.setImageBitmap(bitmap);
        }

        return container;
    }
}
