package com.example.signyourway;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.HashMap;
import java.util.Locale;

public class TextToSign extends AppCompatActivity {
    VideoView videoView;
    TextView textViewCharacter;
    HashMap<Character, Integer> videoMapping = new HashMap<>();
    int currentVideoIndex = 0;
    private char previousChar = '\0'; // Initialize with a non-character value
    TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_text_to_sign);
        videoView = findViewById(R.id.videoView2);
        textViewCharacter = findViewById(R.id.caractere);

        // Initialize videoMapping with your video resources
        videoMapping.put('a', R.raw.a);
        videoMapping.put('b', R.raw.b);
        videoMapping.put('c', R.raw.c);
        videoMapping.put('d', R.raw.d);
        videoMapping.put('e', R.raw.e);
        videoMapping.put('f', R.raw.f);
        videoMapping.put('g', R.raw.g);
        videoMapping.put('h', R.raw.h);
        videoMapping.put('i', R.raw.i);
        videoMapping.put('j', R.raw.j);
        videoMapping.put('k', R.raw.k);
        videoMapping.put('l', R.raw.l);
        videoMapping.put('m', R.raw.m);
        videoMapping.put('n', R.raw.n);
        videoMapping.put('o', R.raw.o);
        videoMapping.put('p', R.raw.p);
        videoMapping.put('q', R.raw.q);
        videoMapping.put('r', R.raw.r);
        videoMapping.put('s', R.raw.s);
        videoMapping.put('t', R.raw.t);
        videoMapping.put('u', R.raw.u);
        videoMapping.put('v', R.raw.v);
        videoMapping.put('w', R.raw.w);
        videoMapping.put('x', R.raw.x);
        videoMapping.put('y', R.raw.y);
        videoMapping.put('z', R.raw.z);


        EditText editTextInput = findViewById(R.id.input);
        Button buttonSubmit = findViewById(R.id.button3);
        ImageButton b1 = findViewById(R.id.btn);

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = editTextInput.getText().toString();
                playSignLanguageVideos(text);
            }
        });
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int status) {
                        if (status == TextToSpeech.SUCCESS) {
                            tts.setLanguage(Locale.US);
                            tts.setSpeechRate(1.0f);
                            tts.speak(editTextInput.getText().toString(), TextToSpeech.QUEUE_ADD, null);
                        }
                    }
                });
            }
        });

    }

    private void playSignLanguageVideos(String text) {
        // Reset the state before starting a new sequence
        currentVideoIndex = 0; // Reset the current video index
        previousChar = '\0'; // Reset the previous character
        textViewCharacter.setText(""); // Clear the TextView

        //MediaController mediaController = new MediaController(this);
        //mediaController.setAnchorView(videoView);
        //videoView.setMediaController(mediaController);

        char[] chars = text.toCharArray();
        if (chars.length > 0) {
            playNextVideo(chars, videoView);
        }
    }


    private void playNextVideo(char[] chars, VideoView videoView) {
        if (currentVideoIndex < chars.length) {
            char c = chars[currentVideoIndex];
            // Check if c is a letter or a digit, excluding special characters
            if (Character.isLetterOrDigit(c)) {
                Integer videoResourceId = videoMapping.get(c);
                if (videoResourceId != null) {
                    String path = "android.resource://" + getPackageName() + "/" + videoResourceId;
                    videoView.setVideoURI(Uri.parse(path));
                    videoView.start();

                    // Get the current text in textViewCharacter and concatenate with the new character
                    String currentText = textViewCharacter.getText().toString();
                    String newText = (previousChar != '\0') ? currentText + c : String.valueOf(c);
                    textViewCharacter.setText(newText);

                    // Update the previous character after setting the text
                    previousChar = c;

                    // Set the completion listener outside the if condition to ensure it's always set
                    videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            currentVideoIndex++;
                            playNextVideo(chars, videoView);
                        }
                    });
                } else {
                    // If no video resource ID is found, just display the character
                    String currentText = textViewCharacter.getText().toString();
                    String newText = (previousChar != '\0') ? currentText + c : String.valueOf(c);
                    textViewCharacter.setText(newText);
                    currentVideoIndex++;
                    playNextVideo(chars, videoView); // Recursively call to play the next video
                }
            } else {
                // If c is not a letter or digit, just display it
                String currentText = textViewCharacter.getText().toString();
                String newText = (previousChar != '\0') ? currentText + c : String.valueOf(c);
                textViewCharacter.setText(newText);
                currentVideoIndex++;
                playNextVideo(chars, videoView); // Recursively call to play the next video
            }
        }
    }




}