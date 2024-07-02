package com.example.ameacaambiental;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Base64;
import java.util.List;

public class AmeacaAdapter extends ArrayAdapter<Ameaca> {
    private final Context context;
    public int mResource;

    public AmeacaAdapter(Context context,int resource, List<Ameaca> ameacas) {
        super(context,resource, ameacas);
        this.context = context;
        mResource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(mResource, parent, false);
        }

        Ameaca ameaca = getItem(position);

        if (ameaca != null) {
            TextView textViewDescricao = convertView.findViewById(R.id.descricao);
            textViewDescricao.setText(ameaca.getDescricao());

            TextView textViewData = convertView.findViewById(R.id.txtData);
            textViewData.setText(ameaca.getData());
            ImageView image = convertView.findViewById(R.id.imageView2);
            if(ameaca.getImage()!= null) {
                byte[] imageData= Base64.getDecoder().decode(ameaca.getImage().replaceAll("[^A-Za-z0-9+/=]", ""));
                Bitmap img = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
                image.setImageBitmap(img);
            }

        }

        return convertView;
    }
}