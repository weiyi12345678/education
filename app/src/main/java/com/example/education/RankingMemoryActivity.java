package com.example.education;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.education.Adapter.RankingAdapter;
import com.example.education.Class.MusicStatus;
import com.example.education.Class.Player;
import com.example.education.Class.Ranking;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RankingMemoryActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    private RecyclerView rv_memory;
    List<Ranking> list;
    RankingAdapter adapter;
    DatabaseReference reference;
    MusicStatus musicStatus;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking_memory);
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
        bottomNavigationView.setSelectedItemId(R.id.memoryCard);
        //Perform ItemSelectedListener
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
            {
                switch (menuItem.getItemId())
                {
                    case R.id.memoryCard:
                        Toast.makeText(RankingMemoryActivity.this, "Welcome to Memory Card Ranking page!", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.matchWord:
                        Intent intent = new Intent(getApplicationContext(), RankingMatchActivity.class);
                        intent.putExtra("duration", mediaPlayer.getCurrentPosition());
                        intent.putExtra("volume", musicStatus.getVolume());
                        intent.putExtra("playing", musicStatus.isPlaying());
                        startActivity(intent);
                        mediaPlayer.stop();
                        mediaPlayer.release();
                        finish();
                        overridePendingTransition(0,0);
                        Toast.makeText(RankingMemoryActivity.this, "Welcome to Match Word Ranking page!", Toast.LENGTH_SHORT).show();
                        return true;
                }
                return false;
            }
        });

        rv_memory = findViewById(R.id.recyclerView_memoryCardRanking);

        reference = FirebaseDatabase.getInstance().getReference().child("RankingMemory");
        list = new ArrayList<>();
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setReverseLayout(true);
        manager.setStackFromEnd(true);
        rv_memory.setLayoutManager(manager);
        rv_memory.setHasFixedSize(true);

        reference.orderByChild("score").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Ranking ranking = snapshot.getValue(Ranking.class);
                        list.add(ranking);
                    }
                    adapter = new RankingAdapter(list, RankingMemoryActivity.this);
                    rv_memory.setAdapter(adapter);
                }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(RankingMemoryActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
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