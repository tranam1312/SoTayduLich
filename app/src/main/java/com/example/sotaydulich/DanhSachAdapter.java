package com.example.sotaydulich;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.utils.widget.ImageFilterView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sotaydulich.model.ViTri;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DanhSachAdapter extends RecyclerView.Adapter<ViewHolder> {

    private ArrayList <ViTri > viTris = new ArrayList<>();
    private onClickItem onClickItem ;

    public DanhSachAdapter( onClickItem onClickItem) {
        this.onClickItem = onClickItem;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View heroView = inflater.inflate(R.layout.item, parent, false);
        ViewHolder viewHolder = new ViewHolder(heroView);
        return viewHolder ;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        byte[] data  =  viTris.get(position).getUriImg();
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        holder.img.setImageBitmap(bitmap);
        holder. diaDanh.setText(viTris.get(position).getDiaDiem());
        holder.moTa.setText(viTris.get(position).getMoTa());
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickItem.onClick(viTris.get(position));
            }});
    }

    @Override
    public int getItemCount() {
        return  viTris.size();
    }

    public  void  setAdpater(ArrayList<ViTri> viTris){
        this.viTris = viTris;
        notifyDataSetChanged();
    }


}

interface  onClickItem {
    void  onClick(ViTri viTri);
}
class  ViewHolder extends  RecyclerView.ViewHolder {
    ImageFilterView img;
     AppCompatTextView diaDanh, moTa;
    ConstraintLayout layout;

    public ViewHolder(@NonNull View itemView) {
        super (itemView);
        layout = itemView.findViewById(R.id.layout);
        img = itemView.findViewById(R.id.imageFilterView);
        diaDanh = itemView.findViewById(R.id.tv_dia_danh);
        moTa = itemView.findViewById(R.id.tv_mo_ta);

    }
}


