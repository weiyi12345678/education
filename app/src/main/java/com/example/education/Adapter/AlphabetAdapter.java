package com.example.education.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.education.Class.Alphabet;
import com.example.education.Fragment.AlphabetFragment;
import com.example.education.R;

import java.util.ArrayList;

public class AlphabetAdapter extends RecyclerView.Adapter<AlphabetAdapter.MyViewHolder> {

    Context mContext;
    ArrayList<Alphabet> mAlphabet;

    public AlphabetAdapter(Context mContext, ArrayList<Alphabet> mAlphabet) {
        this.mContext = mContext;
        this.mAlphabet = mAlphabet;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.image, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder viewHolder, int i) {
        Alphabet alphabet = mAlphabet.get(i);


        viewHolder.tvAlphabet.setText(alphabet.getTitle());

        Glide.with(mContext).load(alphabet.getImageUrl())
                .apply(new RequestOptions().placeholder(R.drawable.ic_image))
                .into(viewHolder.ivAlphabet);

        //alphabetInfo(viewHolder.ivAlphabet, viewHolder.tvAlphabet);

        viewHolder.ivAlphabet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                editor.putString("alphabetID", alphabet.getID());
                editor.putString("alphabetTitle", alphabet.getTitle());
                editor.apply();


                ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction().replace(R.id.container,
                        new AlphabetFragment()).commit();


            }
        });

        viewHolder.tvAlphabet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                editor.putString("alphabetID",alphabet.getID());
                editor.putString("alphabetTitle",alphabet.getTitle());
                editor.apply();

            }
        });

    }

    @Override
    public int getItemCount() {
        return mAlphabet.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivAlphabet;
        public TextView tvAlphabet;
        public String alphabetID;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAlphabet = itemView.findViewById(R.id.textAlphabet);
            ivAlphabet = itemView.findViewById(R.id.imageAlphabet);
        }
    }
}