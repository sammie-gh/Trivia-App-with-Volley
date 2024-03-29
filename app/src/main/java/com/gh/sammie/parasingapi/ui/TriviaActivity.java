package com.gh.sammie.parasingapi.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.gh.sammie.parasingapi.R;
import com.gh.sammie.parasingapi.Utils.Prefs;
import com.gh.sammie.parasingapi.data.AnswerListAsyncResponse;
import com.gh.sammie.parasingapi.data.QuestionBank;
import com.gh.sammie.parasingapi.modal.Question;
import com.gh.sammie.parasingapi.modal.Score;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class TriviaActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView questionTextview;
    private TextView questionCounterTextview;
    private Button trueButton;
    private Button falseButton;
    private ImageButton nextButton;
    private ImageButton prevButton;
    private TextView highestScoreTextView;
    private int currentQuestionIndex = 0;
    private List<Question> questionList;
    private TextView scoreText;
    private Button shareButton;

    private int scoreCounter = 0;
    private Score score;
    private Prefs prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trivia);

        score = new Score(); // score object
        prefs = new Prefs(TriviaActivity.this);
        Log.d("Prefs", "onClick: " + prefs.getHighScore());


        nextButton = findViewById(R.id.next_button);
        prevButton = findViewById(R.id.prev_button);
        trueButton = findViewById(R.id.true_button);
        scoreText = findViewById(R.id.score_text);
        falseButton = findViewById(R.id.false_button);
        questionCounterTextview = findViewById(R.id.counter_text);
        questionTextview = findViewById(R.id.question_textview);
        highestScoreTextView = findViewById(R.id.highest_score);

        //click Listeners
        nextButton.setOnClickListener(this);
        prevButton.setOnClickListener(this);
        trueButton.setOnClickListener(this);
        falseButton.setOnClickListener(this);

        scoreText.setText(MessageFormat.format("Current Score: {0}", String.valueOf(score.getScore())));

        //get previous state
        currentQuestionIndex = prefs.getState();
        // Log.d("State", "onCreate: " + prefs.getState());
        highestScoreTextView.setText(MessageFormat.format(" Highest Score: {0}", String.valueOf(prefs.getHighScore())));

        questionList = new QuestionBank().getQuestions(new AnswerListAsyncResponse() {
            @Override
            public void processFinished(ArrayList<Question> questionArrayList) {

                questionTextview.setText(questionArrayList.get(currentQuestionIndex).getAnswer());
                questionCounterTextview.setText(MessageFormat.format("{0} / {1}", currentQuestionIndex, questionArrayList.size())); // 0 / 234
             //   Log.d("Inside", "processFinished: " + questionArrayList);

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.prev_button:
                if (currentQuestionIndex > 0) {
                    currentQuestionIndex = (currentQuestionIndex - 1) % questionList.size();
                    updateQuestion();
                }
                break;
            case R.id.next_button:
//                prefs.saveHighScore(scoreCounter);
//                Log.d("Prefs", "onClick: " +prefs.getHighScore());
//                currentQuestionIndex = (currentQuestionIndex + 1) % questionList.size();
                goNext();
//                updateQuestion();
                break;
            case R.id.true_button:
                checkAnswer(true);
                updateQuestion();
                break;
            case R.id.false_button:
                checkAnswer(false);
                updateQuestion();
                break;

            case R.id.shareButton:
                //sharebutton logic
                shareScore();
                break;
        }

    }

    private void shareScore() {

        String message = "My current score is " + score.getScore() + " and "
                + "My highest score is " + prefs.getHighScore();
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "I am Playing Trivia");
        intent.putExtra(Intent.EXTRA_TEXT, message);
        startActivity(intent);

    }

    private void checkAnswer(boolean userChooseCorrect) {
        boolean answerIsTrue = questionList.get(currentQuestionIndex).isAnswerTrue();
        int toastMessageId;

        if (userChooseCorrect == answerIsTrue) {

            fadeView();
            addPoints();
            toastMessageId = R.string.correct_answer;
            autoNext();
        } else {
            shakeAnimation();
            deductPoints();
            toastMessageId = R.string.wrong_answer;
        }
        Toast.makeText(TriviaActivity.this, toastMessageId,
                Toast.LENGTH_SHORT)
                .show();
    }

    private void addPoints() {
        scoreCounter += 100;
        score.setScore(scoreCounter);
        scoreText.setText(MessageFormat.format("Current Score: {0}", String.valueOf(score.getScore())));
        Log.d("Score", "addPoints: " + score.getScore());
    }


    private void deductPoints() {
        scoreCounter -= 100;
        if (scoreCounter > 0) {
            score.setScore(scoreCounter);
            scoreText.setText(MessageFormat.format("Current Score: {0}", String.valueOf(score.getScore())));
        } else {
            scoreCounter = 0;
            score.setScore(scoreCounter);
            scoreText.setText(MessageFormat.format("Current Score: {0}", String.valueOf(score.getScore())));
            Log.d("Wrong Score", "deductPoints: " + score.getScore());
        }

        Log.d("Score", "addPoints: " + score.getScore());
    }

    private void autoNext() {
        currentQuestionIndex = (currentQuestionIndex + 1) % questionList.size();
        updateQuestion();
    }


    private void updateQuestion() {
        String question = questionList.get(currentQuestionIndex).getAnswer();
        questionTextview.setText(question);
        questionCounterTextview.setText(MessageFormat.format("{0} / {1}", currentQuestionIndex, questionList.size())); // 0 / 234

    }

    private void shakeAnimation() {
        Animation shake = AnimationUtils.loadAnimation(TriviaActivity.this,
                R.anim.shake_animation);
        final CardView cardView = findViewById(R.id.cardView);
        cardView.setAnimation(shake);

        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setCardBackgroundColor(Color.RED);

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setCardBackgroundColor(Color.WHITE);
              goNext();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    private void goNext() {
        currentQuestionIndex = (currentQuestionIndex + 1) % questionList.size();
        updateQuestion();

    }

    private void fadeView() {
        final CardView cardView = findViewById(R.id.cardView);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);

        alphaAnimation.setDuration(350);
        alphaAnimation.setRepeatCount(1);
        alphaAnimation.setRepeatMode(Animation.REVERSE);

        cardView.setAnimation(alphaAnimation);

        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setCardBackgroundColor(Color.GREEN);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setCardBackgroundColor(Color.WHITE);
               goNext();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    @Override
    protected void onPause() {
        prefs.saveHighScore(score.getScore());
        prefs.setState(currentQuestionIndex);
        super.onPause();
    }
}
