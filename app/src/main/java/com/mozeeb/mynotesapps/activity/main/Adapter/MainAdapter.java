package com.mozeeb.mynotesapps.activity.main.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mozeeb.mynotesapps.R;
import com.mozeeb.mynotesapps.model.Note;

import java.util.List;


public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MyViewHolder> {

    private List<Note> noteList;
    private Context context;
    private ItemClickListener itemClickListener;

    public MainAdapter(List<Note> noteList, Context context, ItemClickListener itemClickListener) {
        this.noteList = noteList;
        this.context = context;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_note, viewGroup, false);
        return new MyViewHolder(view, itemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        Note note = noteList.get(i);
        myViewHolder.tv_title.setText(note.getTitle());
        myViewHolder.tv_note.setText(note.getNote());
        myViewHolder.tv_date.setText(note.getDate());
        myViewHolder.cardItem.setCardBackgroundColor(note.getColor());

    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_title, tv_note, tv_date;
        CardView cardItem;
        ItemClickListener itemClickListener;

        public MyViewHolder(@NonNull View itemView, ItemClickListener itemClickListener) {
            super(itemView);

            tv_title = itemView.findViewById(R.id.title);
            tv_note = itemView.findViewById(R.id.note);
            tv_date = itemView.findViewById(R.id.date);
            cardItem = itemView.findViewById(R.id.card_item);

            this.itemClickListener = itemClickListener;
            cardItem.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v, getAdapterPosition());

        }
    }

    public interface ItemClickListener {
        void onClick(View view, int position);
    }
}
