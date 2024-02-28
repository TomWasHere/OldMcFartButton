package com.tomnelson.oldmacfartbutton2;

import android.animation.ObjectAnimator;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {
    private static final float SCALE_FACTOR = 0.9f; // 10% scale factor
    private boolean isEvenIteration;
    private MediaPlayer mediaPlayer;
    private int currentNoteIndex = 0;

    private final float[] pitchArray = {1.0f, 1.0f, 1.0f, 0.7f, 0.8f, 0.8f, 0.7f, 1.2f, 1.2f, 1.1f, 1.1f, 1.0f};
    private final float[] durationArray = {1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.8f, 1.0f, 1.0f, 1.0f, 1.0f, 0.7f};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        ImageView imageView = findViewById(R.id.imageView);

        final GestureDetector gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                return true;
            }
        });

        imageView.setOnTouchListener((v, event) -> {
            gestureDetector.onTouchEvent(event);

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    // Button pressed, scale in
                    scaleButton(v, SCALE_FACTOR);
                    playFartSound();
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    // Button released or touch canceled, return to normal size
                    scaleButton(v, 1.0f);
                    break;
            }
            return true;
        });
    }

    private void scaleButton(View view, float scaleFactor) {
        ObjectAnimator scaleAnimatorX = ObjectAnimator.ofFloat(view, "scaleX", scaleFactor);
        ObjectAnimator scaleAnimatorY = ObjectAnimator.ofFloat(view, "scaleY", scaleFactor);

        int animationDuration = 5;
        scaleAnimatorX.setDuration(animationDuration);
        scaleAnimatorY.setDuration(animationDuration);

        scaleAnimatorX.start();
        scaleAnimatorY.start();
    }

    private void playFartSound() {
        releaseMediaPlayer();

        int soundResource = isEvenIteration ? R.raw.fart2 : R.raw.fart9;

        mediaPlayer = MediaPlayer.create(this, soundResource);

        // Set pitch and duration based on the arrays
        float pitch = pitchArray[currentNoteIndex];
        float duration = durationArray[currentNoteIndex];

        mediaPlayer.setPlaybackParams(mediaPlayer.getPlaybackParams().setPitch(pitch));
        mediaPlayer.setPlaybackParams(mediaPlayer.getPlaybackParams().setSpeed(duration));
        mediaPlayer.setVolume(1.0f, 1.0f); // Setting both left and right volume to full
        mediaPlayer.start();

        currentNoteIndex = (currentNoteIndex + 1) % pitchArray.length;
        if (currentNoteIndex == 0) {
            isEvenIteration = !isEvenIteration;
        }
    }

    private void releaseMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseMediaPlayer();
    }
}