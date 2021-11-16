package com.example.education;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.education.Class.Alphabet;
import com.example.education.Class.Player;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Random;

public class GameMatchActivity extends AppCompatActivity {

    final static int TIME_COUNTER = 60000; // 1 mins
    private ImageView ivClose;
    private Button btnForfeit, btnSGPlay, btnSGBack;
    private TextView tvTitle, tvContent;
    private CountDownTimer countdown;
    private long timeLeftInMillis;

    private TextView tvResultContent;
    private Button btnPlayAgain, btnLeave, btnUpload;

    private TextView tvUploadScore;
    private Button btnUploadDB;
    private EditText etUploadName;
    private boolean uploaded;

    private ImageView ivQuestion;
    private RelativeLayout rl1, rl2, rl3, rl4, rl5;
    private ImageView iv1, iv2, iv3, iv4, iv5;
    private TextView tv1, tv2, tv3, tv4, tv5;
    private TextView tvTimeCounter, tvScore;
    private AlertDialog resultDialog, startDialog, pauseDialog, uploadDialog;

    private String gameTitle, gameContent;
    private long dbSize;
    private int difficulty, score;
    private CustomDialog customDialog;

    private LinkedList<Alphabet> easy, medium, hard, gameItems;
    private Alphabet ansItem;
    private LinkedList<Alphabet> all;

    private int checkAns;
    MediaPlayer correct, wrong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_match);
        initialize();
        //use to check if the first possible answer is correct or not
        iv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameChecking(0);
            }
        });
        //use to check if the second possible answer is correct or not
        iv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameChecking(1);
            }
        });
        //use to check if the third possible answer is correct or not
        iv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameChecking(2);
            }
        });
        //use to check if the fourth possible answer is correct or not
        iv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameChecking(3);
            }
        });
        //use to check if the five possible answer is correct or not
        iv5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameChecking(4);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu topMenu) {
        getMenuInflater().inflate(R.menu.toobar_game, topMenu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_pause:
                pauseGame();
                callPauseDialog();
                break;
        }
        return true;
    }

    //method use to set the question and possible answer
    private void initialize() {
        //method for connect the variable in game match java file to game match xml file as well
        //as set the difficulty get from difficulty dialog
        itemInitialize();
        //method use to set the gameTitle and gameContent
        gameContentInitialize();
        //method use to set number of possible answer using the difficulty
        gameLayoutInitialize(difficulty);
        //set the question and provide possible answer using the data get from firebase
        firebaseInitialize();
        //call the start dialog to ask player play or go back to game selection menu
        if (score == 0)
            callStartDialog();
    }

    private void itemInitialize() {
        //to connect the relative layout for possible answer in game match xml file
        rl1 = findViewById(R.id.match_card_1);
        rl2 = findViewById(R.id.match_card_2);
        rl3 = findViewById(R.id.match_card_3);
        rl4 = findViewById(R.id.match_card_4);
        rl5 = findViewById(R.id.match_card_5);

        //to connect imageview for possible answer in game match xml file
        iv1 = findViewById(R.id.match_iv_1);
        iv2 = findViewById(R.id.match_iv_2);
        iv3 = findViewById(R.id.match_iv_3);
        iv4 = findViewById(R.id.match_iv_4);
        iv5 = findViewById(R.id.match_iv_5);

        //to connect the textview for possible answer game match in xml file
        tv1 = findViewById(R.id.match_tv_1);
        tv2 = findViewById(R.id.match_tv_2);
        tv3 = findViewById(R.id.match_tv_3);
        tv4 = findViewById(R.id.match_tv_4);
        tv5 = findViewById(R.id.match_tv_5);

        //to connect the imageview for question in game match xml file
        ivQuestion = findViewById(R.id.match_iv_question);

        //to connect the time counter textview in game match xml file
        tvTimeCounter = findViewById(R.id.game_tv_timecounter);

        //to connect the score textview in game match xml file
        tvScore = findViewById(R.id.match_tv_score);

        //to get the difficulty choose by the player(easy,
        Intent intentItem = getIntent();
        difficulty = intentItem.getIntExtra("Difficulty", 0);
        customDialog = new CustomDialog(GameMatchActivity.this);
        customDialog.callLoadingDialog();
        all = new LinkedList<>();
        easy = new LinkedList<>();
        medium = new LinkedList<>();
        hard = new LinkedList<>();
        score = 0;
        uploaded = false;
        tvScore.setText("Score: " + score);

        correct = MediaPlayer.create(this,R.raw.correct);
        wrong = MediaPlayer.create(this,R.raw.wrong);

    }

    private void gameContentInitialize() {
        String difficultyTxt;
        switch (difficulty) {
            case 1:
                difficultyTxt = "Easy";
                break;
            case 2:
                difficultyTxt = "Medium";
                break;
            case 3:
                difficultyTxt = "Hard";
                break;
            default:
                difficultyTxt = null;
                break;

        }
        gameTitle = "Select the correct word that represent the image!";
        gameContent = "Select correct word to earn score.\nDifficulty: " + difficultyTxt;
    }

    private void gameLayoutInitialize(int difficulty) {
        //set relative layout 1, 2 and 3 to become visible in default
        rl1.setVisibility(View.VISIBLE);
        rl2.setVisibility(View.VISIBLE);
        rl3.setVisibility(View.VISIBLE);

        switch (difficulty) {
            //when value in difficulty is one, set relative layout 4 and 5 become gone
            case 1:
                rl4.setVisibility(View.GONE);
                rl5.setVisibility(View.GONE);
                break;
            //when value in difficulty is two, set relative layout 4 to be visible and set relative layout 5 become gone
            case 2:
                rl4.setVisibility(View.VISIBLE);
                rl5.setVisibility(View.GONE);
                break;
            //when value in difficulty is three, set relative layout 4 and 5 become visible
            case 3:
                rl4.setVisibility(View.VISIBLE);
                rl5.setVisibility(View.VISIBLE);
                break;
            default:
                Toast.makeText(this, "Difficulty out of bound: " + difficulty, Toast.LENGTH_SHORT).show();
        }
    }

    private void gameItemInitialize(int difficulty) {
        Log.e("Initialize", "Running");
        //use difficulty to set the possible answer
        switch (difficulty) {
            case 1:
                easyGameSetup();
                break;
            case 2:
                mediumGameSetup();
                break;
            case 3:
                hardGameSetup();
                break;
            default:
                Toast.makeText(this, "Difficulty out of bound: " + difficulty, Toast.LENGTH_SHORT).show();
                break;
        }

        //use to delay 1 second to set the color of each possible answer to light blue
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                iv1.setImageResource(R.drawable.white_2);
                iv2.setImageResource(R.drawable.white_2);
                iv3.setImageResource(R.drawable.white_2);
                iv4.setImageResource(R.drawable.white_2);
                iv5.setImageResource(R.drawable.white_2);
            }
        }, 1000); // 1s
    }

    private void gameChecking(int id) {
        //check if the selected answer is correct or not
        if (ansItem.getTitle().equals(gameItems.get(id).getTitle())) {
            score++;
            tvScore.setText("Score: " + score);
            gameItemInitialize(difficulty);
            //play the correct sound
            correct.start();
        }
        //check if the selected answer is first possible answer
        else if (checkAns == 0) {
            wrong.start();
            iv1.setImageResource(R.drawable.green);
            iv2.setImageResource(R.drawable.red);
            iv3.setImageResource(R.drawable.red);
            iv4.setImageResource(R.drawable.red);
            iv5.setImageResource(R.drawable.red);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    iv1.setImageResource(R.drawable.white_2);
                    iv2.setImageResource(R.drawable.white_2);
                    iv3.setImageResource(R.drawable.white_2);
                    iv4.setImageResource(R.drawable.white_2);
                    iv5.setImageResource(R.drawable.white_2);
                    gameItemInitialize(difficulty);
                }
            }, 1000); // 1s
        }
        //check if the selected answer is second possible answer
        else if(checkAns == 1) {
            //play the wrong sound
            wrong.start();
            //set the correct answer
            iv1.setImageResource(R.drawable.red);
            iv2.setImageResource(R.drawable.green);
            iv3.setImageResource(R.drawable.red);
            iv4.setImageResource(R.drawable.red);
            iv5.setImageResource(R.drawable.red);
            Handler handler = new Handler();
            //after on second all the imageView will change to blue light colour
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    iv1.setImageResource(R.drawable.white_2);
                    iv2.setImageResource(R.drawable.white_2);
                    iv3.setImageResource(R.drawable.white_2);
                    iv4.setImageResource(R.drawable.white_2);
                    iv5.setImageResource(R.drawable.white_2);
                    gameItemInitialize(difficulty);
                }
            }, 1000); // 1s
        }
        //check if the selected answer is third possible answer
        else if(checkAns == 2) {
            wrong.start();
            iv1.setImageResource(R.drawable.red);
            iv2.setImageResource(R.drawable.red);
            iv3.setImageResource(R.drawable.green);
            iv4.setImageResource(R.drawable.red);
            iv5.setImageResource(R.drawable.red);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    iv1.setImageResource(R.drawable.white_2);
                    iv2.setImageResource(R.drawable.white_2);
                    iv3.setImageResource(R.drawable.white_2);
                    iv4.setImageResource(R.drawable.white_2);
                    iv5.setImageResource(R.drawable.white_2);
                    gameItemInitialize(difficulty);
                }
            }, 1000); // 1s
        }
        //check if the selected answer is fourth possible answer
        else if(checkAns == 3) {
            wrong.start();
            iv1.setImageResource(R.drawable.red);
            iv2.setImageResource(R.drawable.red);
            iv3.setImageResource(R.drawable.red);
            iv4.setImageResource(R.drawable.green);
            iv5.setImageResource(R.drawable.red);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    iv1.setImageResource(R.drawable.white_2);
                    iv2.setImageResource(R.drawable.white_2);
                    iv3.setImageResource(R.drawable.white_2);
                    iv4.setImageResource(R.drawable.white_2);
                    iv5.setImageResource(R.drawable.white_2);
                    gameItemInitialize(difficulty);
                }
            }, 1000); // 1s
        }
        //check if the selected answer is fifth possible answer
        else if(checkAns == 4) {
            wrong.start();
            iv1.setImageResource(R.drawable.red);
            iv2.setImageResource(R.drawable.red);
            iv3.setImageResource(R.drawable.red);
            iv4.setImageResource(R.drawable.red);
            iv5.setImageResource(R.drawable.green);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    iv1.setImageResource(R.drawable.white_2);
                    iv2.setImageResource(R.drawable.white_2);
                    iv3.setImageResource(R.drawable.white_2);
                    iv4.setImageResource(R.drawable.white_2);
                    iv5.setImageResource(R.drawable.white_2);
                    gameItemInitialize(difficulty);
                }
            }, 1000); // 1s
        }
    }

    private void easyGameSetup() {
        gameItems = new LinkedList<>();
        Random rand = new Random();

        for (int i = 0; i < 3; i++) {
            int position = rand.nextInt(easy.size());
            gameItems.add(easy.get(position));
            easy.remove(position);
        }

        int position2 = rand.nextInt(gameItems.size());
        ansItem = gameItems.get(position2);

        Picasso.get().load(ansItem.getGameImage()).fit().centerCrop().into(ivQuestion);

        int shuffleCounter = rand.nextInt(10) + 2;

        for (int i = 0; i < shuffleCounter; i++) {
            Collections.shuffle(gameItems);
        }

        for(int i = 0; i < gameItems.size(); i++){
            if(ansItem.getTitle().equals(gameItems.get(i).getTitle()))
            {
                checkAns = i;
            }
        }

        tv1.setText(gameItems.get(0).getTitle());
        tv2.setText(gameItems.get(1).getTitle());
        tv3.setText(gameItems.get(2).getTitle());

        firebaseInitialize();

        customDialog.closeDialog();
    }

    private void mediumGameSetup() {
        gameItems = new LinkedList<>();
        Random rand = new Random();
        for (int i = 0; i < 2; i++) {
            int position = rand.nextInt(easy.size());
            gameItems.add(easy.get(position));
            easy.remove(position);
        }

        for (int i = 0; i < 2; i++) {
            int position = rand.nextInt(medium.size());
            gameItems.add(medium.get(position));
            medium.remove(position);
        }

        int position2 = rand.nextInt(gameItems.size());
        ansItem = gameItems.get(position2);

        Picasso.get().load(ansItem.getGameImage()).fit().centerCrop().into(ivQuestion);

        int shuffleCounter = rand.nextInt(10) + 2;

        for (int i = 0; i < shuffleCounter; i++) {
            Collections.shuffle(gameItems);
        }

        for(int i = 0; i < gameItems.size(); i++){
            if(gameItems.get(i).getTitle().equals(ansItem.getTitle()))
            {
                checkAns = i;
            }
        }

        tv1.setText(gameItems.get(0).getTitle());
        tv2.setText(gameItems.get(1).getTitle());
        tv3.setText(gameItems.get(2).getTitle());
        tv4.setText(gameItems.get(3).getTitle());

        firebaseInitialize();

        customDialog.closeDialog();
    }

    private void hardGameSetup() {
        //declare a new linked list to store the word
        gameItems = new LinkedList<>();
        Random rand = new Random();
        //store one easy difficulty word into gameItems linked list using word from easy linked list with random integer
        for (int i = 0; i < 1; i++) {
            int position = rand.nextInt(easy.size());
            gameItems.add(easy.get(position));
            easy.remove(position);
        }
        //store two medium difficulty word into gameItems linked list using word from medium linked list with random integer
        for (int i = 0; i < 2; i++) {
            int position = rand.nextInt(medium.size());
            gameItems.add(medium.get(position));
            medium.remove(position);
        }
        //store two hard difficulty word into gameItems linked list using word from hard linked list with random integer
        for (int i = 0; i < 2; i++) {
            int position = rand.nextInt(hard.size());
            gameItems.add(hard.get(position));
            hard.remove(position);
        }

        //assign random integer to position2 and set question using position2
        int position2 = rand.nextInt(gameItems.size());
        ansItem = gameItems.get(position2);
        //set the question image in xml file using the ansItem
        Picasso.get().load(ansItem.getGameImage()).fit().centerCrop().into(ivQuestion);

        //randomly shuffle the gameItems
        int shuffleCounter = rand.nextInt(10) + 2;
        for (int i = 0; i < shuffleCounter; i++) {
            Collections.shuffle(gameItems);
        }
        //assign the answer to checkAns
        for(int i = 0; i < gameItems.size(); i++){
            if(gameItems.get(i).getTitle().equals(ansItem.getTitle()))
            {
                checkAns = i;
            }
        }

        //set the text for all possible answer
        tv1.setText(gameItems.get(0).getTitle());
        tv2.setText(gameItems.get(1).getTitle());
        tv3.setText(gameItems.get(2).getTitle());
        tv4.setText(gameItems.get(3).getTitle());
        tv5.setText(gameItems.get(4).getTitle());

        //get new easy, hard and medium linked list
        firebaseInitialize();

        customDialog.closeDialog();
    }

    private void callStartDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(GameMatchActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View lView = inflater.inflate(R.layout.start_game_dialog, null);
        builder.setView(lView);

        tvTitle = lView.findViewById(R.id.startgame_tv_title);
        tvContent = lView.findViewById(R.id.startgame_tv_content);
        btnSGPlay = lView.findViewById(R.id.btnSGPlay);
        btnSGBack = lView.findViewById(R.id.btnSGBack);

        tvTitle.setText(gameTitle);
        tvContent.setText(gameContent);
        btnSGPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame();
                startDialog.dismiss();
            }
        });
        btnSGBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), GameMenuActivity.class));
            }
        });

        startDialog = builder.create();
        startDialog.setCanceledOnTouchOutside(false);
        startDialog.show();
    }

    private void callPauseDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(GameMatchActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View lView = inflater.inflate(R.layout.pause_dialog, null);
        builder.setView(lView);
        ivClose = lView.findViewById(R.id.pause_iv_close);
        btnForfeit = lView.findViewById(R.id.btnPauseForfeit);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                continueGame();
                pauseDialog.dismiss();
            }
        });
        btnForfeit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Forfeit clicked", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), GameMenuActivity.class));
            }
        });
        pauseDialog = builder.create();
        pauseDialog.setCanceledOnTouchOutside(false);
        pauseDialog.show();
    }

    private void callResultDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(GameMatchActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View lView = inflater.inflate(R.layout.result_dialog, null);
        builder.setView(lView);
        tvResultContent = lView.findViewById(R.id.result_tv_score_content);
        btnPlayAgain = lView.findViewById(R.id.btnResultAgain);
        btnLeave = lView.findViewById(R.id.btnResultBack);
        btnUpload = lView.findViewById(R.id.btnResultUpload);
        tvResultContent.setText("Final Score: " + score);
        btnPlayAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initialize();
                resultDialog.dismiss();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uploaded)
                    Toast.makeText(getApplicationContext(), "You uploaded just now!", Toast.LENGTH_SHORT).show();
                else if (score == 0)
                    Toast.makeText(getApplicationContext(), "Your score is too low", Toast.LENGTH_SHORT).show();
                else
                    callUploadDialog();
            }
        });

        btnLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), GameMenuActivity.class));
            }
        });
        resultDialog = builder.create();
        resultDialog.setCanceledOnTouchOutside(false);
        resultDialog.show();
    }

    private void callUploadDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(GameMatchActivity.this);
        LayoutInflater inflaterNew = getLayoutInflater();
        //connect view with upload score dialog and setView for builder with view
        View view = inflaterNew.inflate(R.layout.upload_score_dialog, null);
        builder.setView(view);
        //connect variable in java file with the component in xml file
        tvUploadScore = view.findViewById(R.id.upload_score_tv_score);
        ivClose = view.findViewById(R.id.upload_score_iv_close);
        btnUploadDB = view.findViewById(R.id.btn_uploadscore_upload);
        etUploadName = view.findViewById(R.id.upload_score_et_name);
        tvUploadScore.setText("" + score);
        btnUploadDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etUploadName.getText().toString();
                //check to see if the name is empty or not
                if (name.isEmpty() || name.equals(" ")){
                    customDialog.closeDialog();
                    Toast.makeText(getApplicationContext(), "Invalid name", Toast.LENGTH_SHORT).show();
                }
                //if the name is not empty, the name, score and game type will be store in RankingMatch realtime database
                else {
                    customDialog.callLoadingDialog();
                    Player player = new Player(name, score, 2);
                    DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                    db.child("RankingMatch").push().setValue(player).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            customDialog.closeDialog();
                            Toast.makeText(getApplicationContext(), "Uploaded!", Toast.LENGTH_SHORT).show();
                            uploaded = true;
                            uploadDialog.dismiss();
                        }
                    });
                }
            }
        });
        uploadDialog = builder.create();
        uploadDialog.show();
    }

    public void startGame() {
        startTimer();
    }

    public void pauseGame() {
        pauseTimer();
        callPauseDialog();
    }

    public void continueGame() {
        continueTimer();
    }

    private void firebaseInitialize() {
        //to get the data from realtime database name Word from firebase
        DatabaseReference alphabetDB = FirebaseDatabase.getInstance().getReference().child("Word");
        //add each word by following the word difficulty set in firebase
        alphabetDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dtSnapshot : snapshot.getChildren()) {
                    Alphabet alphabet = dtSnapshot.getValue(Alphabet.class);
                    //all word in the realtime firebase database will be store in alphabet linkedList name all
                    all.add(alphabet);
                    switch (alphabet.getDifficulty()) {
                        //easy difficulty word will be added into alphabet linkedList name easy
                        case 1:
                            easy.add(alphabet);
                            break;
                        //medium difficulty word will be added into alphabet linkedList name medium
                        case 2:
                            medium.add(alphabet);
                            break;
                        //hard difficulty word will be added into alphabet linkedList name hard
                        case 3:
                            hard.add(alphabet);
                            break;
                        default:
                            Log.e("Difficulty", "Out of bound: " + alphabet.getDifficulty());
                    }
                    //get the size of the database
                    dbSize = snapshot.getChildrenCount();
                    //method use to print the size of easy, medium and hard linked list in log
                    //it is also use to set the possible answer using the easy, medium and hard linked list
                    checkFinishRetrieving();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase Data Error", "Error: " + error.toException());
            }

        });
    }

    private void updateCountDownText() {
        int minutes = (int) timeLeftInMillis / 1000 / 60;
        int seconds = (int) timeLeftInMillis / 1000 % 60;
        String timeLeft = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        tvTimeCounter.setText(timeLeft);
    }

    private void startTimer(){
        timeLeftInMillis = TIME_COUNTER;
        countdown = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                timeLeftInMillis = 0;
                updateCountDownText();
                callResultDialog();
            }
        }.start();
    }

    private void continueTimer(){
        countdown = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                timeLeftInMillis = 0;
                updateCountDownText();
                callResultDialog();
            }
        }.start();
    }

    private void pauseTimer(){
        countdown.cancel();
    }

    private void checkFinishRetrieving() {
        //check whether the word store in all linked list is equal to size of realtime database
        //and check the realtime database to see if it is 0 or not
        if ((all.size() == dbSize) && (dbSize != 0)) {
            Log.e("Easy", "Size: " + easy.size());
            Log.e("Medium", "Size: " + medium.size());
            Log.e("Hard", "Size: " + hard.size());
            //method use to set the possible answer
            gameItemInitialize(difficulty);
        }
    }
}