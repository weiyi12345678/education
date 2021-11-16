package com.example.education.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.education.Class.Ranking;
import com.example.education.R;

import java.util.List;

public class RankingAdapter extends RecyclerView.Adapter<RankingAdapter.RankingViewAdapter> {

    List<Ranking> list;
    Context context;
    int i = 1;

    public RankingAdapter(List<Ranking> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public RankingViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.ranking_layout, parent, false);
        return new RankingViewAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RankingViewAdapter holder, int position) {
        Ranking currentItem = list.get(position);
        holder.name.setText(currentItem.getName());
        holder.score.setText(String.valueOf(currentItem.getScore()));
        holder.rank.setText("#" + String.valueOf(i));
        i++;

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class RankingViewAdapter extends RecyclerView.ViewHolder {

        TextView name, score, rank;

        public RankingViewAdapter(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.r1_name);
            score = itemView.findViewById(R.id.r1_score);
            rank = itemView.findViewById(R.id.r1_ranking);
        }
    }
}
