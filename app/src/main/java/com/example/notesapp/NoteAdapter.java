package com.example.notesapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notesapp.room.Note;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    public Context context;
    public List<Note> list = new ArrayList<>();
    public onItemClickListener listener;
    public onItemLongCLickListener longCLickListener;

    public NoteAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.note, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        final Note n = list.get(position);
        holder.title.setText(n.getTitle());
        holder.descr.setText(n.getDescription());
    }

    void setList(List<Note> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setOnItemClickListener(onItemClickListener onItemClickListener) {
        listener = onItemClickListener;
    }

    public void setOnItemLongClickListener(onItemLongCLickListener onItemLongClickListener) {
        longCLickListener = onItemLongClickListener;
    }

    public interface onItemClickListener {
        void onItemClick(Note note);
    }

    public interface onItemLongCLickListener {
        void onItemLongClick(Note note);
    }

    class NoteViewHolder extends RecyclerView.ViewHolder {

        TextView title, descr;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            descr = itemView.findViewById(R.id.descr);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION)
                        listener.onItemClick(list.get(position));
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = getAdapterPosition();
                    if (longCLickListener != null && position != RecyclerView.NO_POSITION) {
                        longCLickListener.onItemLongClick(list.get(position));
                        return true;
                    } else
                        return false;
                }
            });
        }

    }

}
