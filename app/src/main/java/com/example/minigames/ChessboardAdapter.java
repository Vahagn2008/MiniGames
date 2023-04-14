package com.example.minigames;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.minigames.Fragments.GameFragment;

import java.util.ArrayList;

public class ChessboardAdapter  extends RecyclerView.Adapter<ChessboardAdapter.ViewHolder>{
    private Context context;
    private ArrayList<Bitmap> arrBms;
    private Bitmap bmX,bmO;
    private Animation anim_x_o;

    public ChessboardAdapter(Context context, ArrayList<Bitmap> arrBms) {
        this.context = context;
        this.arrBms = arrBms;
        bmO = BitmapFactory.decodeResource(context.getResources(), R.drawable.o);
        bmX = BitmapFactory.decodeResource(context.getResources(), R.drawable.x);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return  new ViewHolder(LayoutInflater.from(context).inflate(R.layout.cell, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.img_cell_chessboard.setImageBitmap(arrBms.get(position));
        anim_x_o = AnimationUtils.loadAnimation(context, R.anim.anim_x_o);
        holder.img_cell_chessboard.setAnimation(anim_x_o);
        holder.img_cell_chessboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(arrBms.get(position)==null){
                    if(GameFragment.turnO){
                        arrBms.set(position, bmO);
                        GameFragment.turnO = false;
                        GameFragment.txt_turn.setText("turn of X");
                    }else {
                        arrBms.set(position, bmX);
                        GameFragment.turnO = true;
                        GameFragment.txt_turn.setText("turn of O");
                    }
                    holder.img_cell_chessboard.setAnimation(anim_x_o);
                    notifyItemChanged(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrBms.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView img_cell_chessboard;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_cell_chessboard = itemView.findViewById(R.id.img_cell_chessboard);
        }
    }

    public ArrayList<Bitmap> getArrBms() {
        return arrBms;
    }

    public void setArrBms(ArrayList<Bitmap> arrBms) {
        this.arrBms = arrBms;
    }
}
