package com.example.samuel.musicplayer;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Samuel on 15/09/2017.
 */

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongHolder>{

    private Context ctx;
    private ArrayList<Songs>songs;
    OnItemClickListener onItemClickListener;

    public SongAdapter(Context ctx, ArrayList<Songs> songs) {
        this.ctx = ctx;
        this.songs = songs;
    }

    public interface OnItemClickListener{
         void OnItemClick(Button b,View v,Songs s,int position);
    }

    public void setOnItemClickListener(OnItemClickListener click){
        this.onItemClickListener = click;
    }
    @Override
    public SongHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_song,parent,false);
        return new SongHolder(view);
    }

    @Override
    public void onBindViewHolder(final SongHolder holder, final int position) {
        final Songs c = songs.get(position);
        holder.song.setText(c.getName());
        holder.artist.setText(c.getArtist());
        holder.action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onItemClickListener != null){
                    onItemClickListener.OnItemClick(holder.action,view,c,position);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    public class SongHolder extends RecyclerView.ViewHolder{
        TextView artist,song;
        Button action;


        public SongHolder(View itemView) {
            super(itemView);
            artist = itemView.findViewById(R.id.song_artist);
            song = itemView.findViewById(R.id.song_title);
            action = itemView.findViewById(R.id.action_btn);
        }
    }
}
