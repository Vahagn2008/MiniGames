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
    private ArrayList<Bitmap> arrBms, arrStrokes;
    private Bitmap bmX,bmO;
    private Animation anim_x_o, anim_stroke;

    public ChessboardAdapter(Context context, ArrayList<Bitmap> arrBms) {
        this.context = context;
        this.arrBms = arrBms;
        bmO = BitmapFactory.decodeResource(context.getResources(), R.drawable.o);
        bmX = BitmapFactory.decodeResource(context.getResources(), R.drawable.x);
        arrStrokes = new ArrayList<>();
        arrStrokes.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.stroke1));
        arrStrokes.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.stroke2));
        arrStrokes.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.stroke3));
        arrStrokes.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.stroke4));
        arrStrokes.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.stroke5));
        arrStrokes.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.stroke6));
        arrStrokes.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.stroke7));
        arrStrokes.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.stroke8));
        anim_stroke = AnimationUtils.loadAnimation(context, R.anim.anim_stroke);
        GameFragment.img_stroke.setAnimation(anim_stroke);
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
                if(arrBms.get(position)==null&&!checkwin()){
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
                    if (checkwin()){
                        win();
                    }
                    notifyItemChanged(position);
                }
            }
        });
    }

    private void win() {
        GameFragment.img_stroke.startAnimation(anim_stroke);
    }

    private boolean checkwin() {
        if (arrBms.get(0)==arrBms.get(3)&&arrBms.get(3)==arrBms.get(6)&&arrBms.get(0)!=null){
            GameFragment.img_stroke.setImageBitmap(arrStrokes.get(2));
            return true;
        }else if (arrBms.get(1)==arrBms.get(4)&&arrBms.get(4)==arrBms.get(7)&&arrBms.get(1)!=null){
            GameFragment.img_stroke.setImageBitmap(arrStrokes.get(3));
            return true;
        }else if (arrBms.get(2)==arrBms.get(5)&&arrBms.get(5)==arrBms.get(8)&&arrBms.get(2)!=null){
            GameFragment.img_stroke.setImageBitmap(arrStrokes.get(4));
            return true;
        }else if (arrBms.get(0)==arrBms.get(1)&&arrBms.get(1)==arrBms.get(2)&&arrBms.get(0)!=null){
            GameFragment.img_stroke.setImageBitmap(arrStrokes.get(5));
            return true;
        }else if (arrBms.get(3)==arrBms.get(4)&&arrBms.get(4)==arrBms.get(5)&&arrBms.get(3)!=null){
            GameFragment.img_stroke.setImageBitmap(arrStrokes.get(6));
            return true;
        }else if (arrBms.get(6)==arrBms.get(7)&&arrBms.get(7)==arrBms.get(8)&&arrBms.get(6)!=null){
            GameFragment.img_stroke.setImageBitmap(arrStrokes.get(7));
            return true;
        }else if (arrBms.get(0)==arrBms.get(4)&&arrBms.get(4)==arrBms.get(8)&&arrBms.get(0)!=null){
            GameFragment.img_stroke.setImageBitmap(arrStrokes.get(1));
            return true;
        }else if (arrBms.get(2)==arrBms.get(4)&&arrBms.get(4)==arrBms.get(6)&&arrBms.get(2)!=null){
            GameFragment.img_stroke.setImageBitmap(arrStrokes.get(0));
            return true;
        }
        return false;
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
