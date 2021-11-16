package com.example.education.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.education.Adapter.WordAdapter;
import com.example.education.Class.Alphabet;
import com.example.education.Class.Word;
import com.example.education.LearnActivity;
import com.example.education.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AlphabetFragment extends Fragment {
    ImageView ivAlphabet;
    TextView tvAlphabet;

    private List<String> myAlphabet;

    FirebaseAuth fAuth;

    FirebaseUser firebaseUser;
    String alphabetID,alphabetWord,alphabetCapWord;

    RecyclerView recyclerView;
    //AlphabetAdapter alphabetAdapter;
    WordAdapter wordAdapter;
    List<Alphabet> mAlphabet;

    List<Word> wordList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_alphabet, container, false);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        ivAlphabet = view.findViewById(R.id.ivAlphabet);
        tvAlphabet = view.findViewById(R.id.tvAlphabet);

        SharedPreferences prefs = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        alphabetID = prefs.getString("alphabetID", "none");
        alphabetWord = prefs.getString("alphabetID", "none");
        alphabetCapWord = alphabetWord.toUpperCase();

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        if(getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        }
        else{
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        }
        // LinearLayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);
        //  recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        wordList = new ArrayList<>();
        wordAdapter = new WordAdapter(getContext(), wordList);
        recyclerView.setAdapter(wordAdapter);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LearnActivity.fragmentManager.beginTransaction().replace(R.id.container, new AlphaFragment(), null)
                        .addToBackStack(null).commit();
            }
        });

        alphabetInfo();
        myAlphabet();

        return view;
    }

    private void alphabetInfo() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Alphabet").child(alphabetCapWord);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (getContext() == null) {
                    return;
                }
                Alphabet alphabet = dataSnapshot.getValue(Alphabet.class);

                Glide.with(getContext()).load(alphabet.getImageUrl())
                        .apply(new RequestOptions().placeholder(R.drawable.ic_image))
                        .into(ivAlphabet);
                tvAlphabet.setText(alphabet.getTitle());

                ivAlphabet.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        /*MediaPlayer mediaPlayer = new MediaPlayer();

                        try{
                            mediaPlayer.setDataSource("gs://educational-app-5b24d.appspot.com/sound/a/Airplane.mp3");
                            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                @Override
                                public void onPrepared(MediaPlayer mediaPlayer) {
                                    mediaPlayer.start();
                                }
                            });
                            mediaPlayer.prepare();
                        }catch (IOException e)
                        {
                            e.printStackTrace();
                        }*/

                        Toast.makeText(getContext(), "Alphabet Sound: " + alphabet.getTitle(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void myAlphabet() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Word");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                wordList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Word word = snapshot.getValue(Word.class);
                    if (word.getID().equals(alphabetWord)) {
                        wordList.add(word);
                    }
                }
                //Collections.reverse(postList);
                wordAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
