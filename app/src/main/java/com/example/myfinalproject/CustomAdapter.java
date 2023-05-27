package com.example.myfinalproject;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private Context context;
    private Activity activity;
    private ArrayList id, book_time, date, the_reason, address;
    private SelectListener listener;

    CustomAdapter(Activity activity, Context context, ArrayList id, ArrayList book_time, ArrayList date,
                  ArrayList the_reason, ArrayList address, SelectListener listener){
        this.activity = activity;
        this.context = context;
        this.id = id;
        this.book_time = book_time;
        this.date = date;
        this.the_reason = the_reason;
        this.address= address;
        this.listener= listener;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row, parent, false);
        return new MyViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.id_txt.setText(String.valueOf(id.get(position)));
        holder.time_txt.setText(String.valueOf(book_time.get(position)));
        holder.date_txt.setText(String.valueOf(date.get(position)));
        holder.the_reason_txt.setText(String.valueOf(the_reason.get(position)));
        holder.address_txt.setText(String.valueOf(address.get(position)));


        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

            listener.onItemLongClick(String.valueOf(date.get( holder.getAdapterPosition())),String.valueOf(id.get( holder.getAdapterPosition())));
            return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return id.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView id_txt, time_txt, date_txt, the_reason_txt,address_txt;
        LinearLayout mainLayout;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            id_txt = itemView.findViewById(R.id.id_txt);
            time_txt = itemView.findViewById(R.id.time_txt);
            date_txt = itemView.findViewById(R.id.date_txt);
            the_reason_txt = itemView.findViewById(R.id.the_reason_txt);
            address_txt= itemView.findViewById(R.id.address_txt);
            mainLayout = itemView.findViewById(R.id.mainLayout);
            //Animate Recyclerview
//            Animation translate_anim = AnimationUtils.loadAnimation(context, R.anim.translate_anim);
//            mainLayout.setAnimation(translate_anim);
        }

    }

}