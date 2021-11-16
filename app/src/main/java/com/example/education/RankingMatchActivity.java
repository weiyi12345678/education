package com.example.education;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.education.Adapter.RankingAdapter;
import com.example.education.Class.MusicStatus;
import com.example.education.Class.Ranking;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RankingMatchActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    private RecyclerView rv_match;
    List<Ranking> list;
    RankingAdapter adapter;
    DatabaseReference reference;
    MusicStatus musicStatus;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking_match);
        Intent getIntent = getIntent();
        int duration = getIntent.getIntExtra("duration", -1);
        int volume = getIntent.getIntExtra("volume", -1);
        boolean playing = getIntent.getBooleanExtra("playing", false);
        musicStatus = new MusicStatus(duration, volume, playing);
        if (musicStatus.isPlaying()){
            mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.background);
            mediaPlayer.setVolume(musicStatus.getVolume(), musicStatus.getVolume());
            mediaPlayer.seekTo(duration);
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        }

        //Initialize and assign variable
        bottomNavigationView = findViewById(R.id.bottomNav);
        //Set home selected
        bottomNavigationView.setSelectedItemId(R.id.matchWord);
        //Perform ItemSelectedListener
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
            {
                switch (menuItem.getItemId())
                {
                    case R.id.matchWord:
                        Toast.makeText(RankingMatchActivity.this, "Match Word Ranking page!", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.memoryCard:
                        Intent intent = new Intent(getApplicationContext(), RankingMemoryActivity.class);
                        intent.putExtra("duration", mediaPlayer.getCurrentPosition());
                        intent.putExtra("volume", musicStatus.getVolume());
                        intent.putExtra("playing", musicStatus.isPlaying());
                        startActivity(intent);
                        mediaPlayer.stop();
                        mediaPlayer.release();
                        finish();
                        overridePendingTransition(0,0);
                        Toast.makeText(RankingMatchActivity.this, "Memory Card Ranking page!", Toast.LENGTH_SHORT).show();
                        return true;
                }
                return false;
            }
        });

        rv_match = findViewById(R.id.recyclerView_matchWordRanking);

        reference = FirebaseDatabase.getInstance().getReference().child("RankingMatch");
        list = new ArrayList<>();
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setReverseLayout(true);
        manager.setStackFromEnd(true);
        rv_match.setLayoutManager(manager);
        rv_match.setHasFixedSize(true);

        reference.orderByChild("score").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Ranking ranking = snapshot.getValue(Ranking.class);
                    list.add(ranking);
                }
                adapter = new RankingAdapter(list, RankingMatchActivity.this);
                rv_match.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(RankingMatchActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu topMenu) {
        getMenuInflater().inflate(R.menu.toolbar_base, topMenu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            default:
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("duration", mediaPlayer.getCurrentPosition());
                intent.putExtra("volume", musicStatus.getVolume());
                intent.putExtra("playing", musicStatus.isPlaying());
                startActivity(intent);
                mediaPlayer.stop();
                mediaPlayer.release();
                finish();
                break;
        }
        return true;
    }
}