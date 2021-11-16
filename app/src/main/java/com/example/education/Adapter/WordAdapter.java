package com.example.education.Adapter;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.education.Class.Word;
import com.example.education.R;

import java.time.LocalDateTime;
import java.util.List;

public class WordAdapter extends RecyclerView.Adapter<WordAdapter.ViewHolder>{

    private Context mContext;
    private List<Word> mWord;

    public WordAdapter(Context context, List<Word> word){
        mContext = context;
        mWord = word;
    }

    @NonNull
    @Override
    public WordAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.imageword, viewGroup, false);
        return new WordAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final WordAdapter.ViewHolder holder, final int i) {
        final Word word = mWord.get(i);

        Glide.with(mContext).load(word.getImageUrl()).into(holder.ivWord);

        holder.tvWord.setText(word.getTitle());

        holder.ivWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                editor.putString("wordID", word.getID());
                editor.apply();

                Toast.makeText(view.getContext(), "Word Sound: " + word.getTitle(), Toast.LENGTH_SHORT).show();

                // Intent intent = new Intent(mContext, .class);
                //   mContext.startActivity(intent);
            }
        });


        holder.tvWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                editor.putString("wordID", word.getID());
                editor.apply();


                //   Intent intent = new Intent(mContext, .class);
                //   mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mWord.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivWord,ivAlphabet;
        public TextView tvWord,tvAlphabet;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivAlphabet = itemView.findViewById(R.id.imageAlphabet);
            tvAlphabet = itemView.findViewById(R.id.textAlphabet);
            tvWord = itemView.findViewById(R.id.textWord);
            ivWord = itemView.findViewById(R.id.imageWord);
        }
    }
}
