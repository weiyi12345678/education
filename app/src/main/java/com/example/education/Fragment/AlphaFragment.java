package com.example.education.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.education.Adapter.AlphabetAdapter;
import com.example.education.Class.Alphabet;
import com.example.education.LearnActivity;
import com.example.education.MainActivity;
import com.example.education.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AlphaFragment extends Fragment {
    RecyclerView recyclerView;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;

    ArrayList<Alphabet> alphabetList;
    AlphabetAdapter alphabetAdapter;

    String alphabetID,alphabetWord;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_alpha, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_all);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new GridLayoutManager(getContext(),3);
        recyclerView.setLayoutManager(linearLayoutManager);
        alphabetList = new ArrayList<>();
        alphabetAdapter = new AlphabetAdapter(getContext(), alphabetList);
        recyclerView.setAdapter(alphabetAdapter);

        firebaseDatabase = FirebaseDatabase.getInstance("https://educational-app-5b24d-default-rtdb.asia-southeast1.firebasedatabase.app");
        reference = firebaseDatabase.getReference().child("Alphabet");

        SharedPreferences prefs = getContext().getSharedPreferences("PREFS", getContext().MODE_PRIVATE);
        alphabetID = prefs.getString("alphabetID", "none");
        alphabetWord = prefs.getString("alphabetWord", "none");

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                LearnActivity activity = (LearnActivity) getActivity();
                MediaPlayer mediaPlayer = activity.getMediaPlayer();
                int volume = activity.getVolume();
                boolean playing = activity.getPlaying();
                intent.putExtra("duration", mediaPlayer.getCurrentPosition());
                intent.putExtra("volume", volume);
                intent.putExtra("playing", playing);
                mediaPlayer.stop();
                mediaPlayer.release();
                startActivity(intent);
            }
        });
        readAlphabet();
        return view;
    }

    private void readAlphabet() {
        DatabaseReference reference = FirebaseDatabase.getInstance("https://educational-app-5b24d-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Alphabet");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                alphabetList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Alphabet alphabet = snapshot.getValue(Alphabet.class);
                    String data = String.valueOf(alphabet);
                    alphabetList.add(alphabet);
                }
                alphabetAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
