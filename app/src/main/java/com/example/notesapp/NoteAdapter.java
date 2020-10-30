package com.example.notesapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notesapp.databinding.NoteBinding;
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
        NoteBinding noteBinding = NoteBinding.inflate(LayoutInflater.from(context), parent, false);
        return new NoteViewHolder(noteBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        holder.noteBinding.setNote(list.get(position));
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
        NoteBinding noteBinding;

        public NoteViewHolder(@NonNull NoteBinding noteBinding) {
            super(noteBinding.getRoot());
            this.noteBinding = noteBinding;
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
