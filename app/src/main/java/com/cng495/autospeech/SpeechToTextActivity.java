package com.cng495.autospeech;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SpeechToTextActivity extends AppCompatActivity {

    public static final Integer RecordAudioRequestCode = 1;

    private TextView speechToTextView;
    private Button speakButton;
    private TextView textToTranslateView;

    private SpeechRecognizer speechRecognizer;

    private boolean isSpeaking;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech_to_text);

        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED)
            checkPermission();
        //else
        //Toast.makeText(this,"Permission Granted",Toast.LENGTH_SHORT).show();


        speechToTextView = findViewById(R.id.speechToTextView);
        speakButton = findViewById(R.id.speakButton);
        textToTranslateView = findViewById(R.id.translateTextView);
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        isSpeaking = false;

        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {
            }

            @Override
            public void onBeginningOfSpeech() {
                speechToTextView.setText("");
                speechToTextView.setHint("Listening...");
            }

            @Override
            public void onRmsChanged(float v) {
            }
            @Override
            public void onBufferReceived(byte[] bytes) {
            }
            @Override
            public void onEndOfSpeech() {
                speechToTextView.setText("END");
            }
            @Override
            public void onError(int i) {
            }

            @Override
            public void onResults(Bundle bundle) {
                //micButton.setImageResource(R.drawable.ic_mic_black_off);
                ArrayList<String> data = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

                speechToTextView.setText(data.get(0));
                speechToTextView.setText("Language: "+"\n");//+speechRecognizer.DETECTED_LANGUAGE+
            }

            @Override
            public void onPartialResults(Bundle bundle) {
            }

            @Override
            public void onEvent(int i, Bundle bundle) {
            }
        });

        speak();

    }

    private void speak()
    {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        //intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        //intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Start Speaking");
        Activity currentActivity = this;

        speakButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isSpeaking)
                {
                    speechRecognizer.stopListening();
                    Toast.makeText(currentActivity,"Stopped Listening",Toast.LENGTH_SHORT).show();
                    isSpeaking = false;
                }
                else
                {
                    speechRecognizer.startListening(intent);
                    Toast.makeText(currentActivity,"Started Listening",Toast.LENGTH_SHORT).show();
                    isSpeaking = true;
                    //speechToTextView.setText("Something to say");
                }

            }
        });



    }







    @Override
    protected void onDestroy() {
        super.onDestroy();
        //speechRecognizer.destroy();
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECORD_AUDIO}, RecordAudioRequestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RecordAudioRequestCode && grantResults.length > 0 ){
            //if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
            //Toast.makeText(this,"Permission Granted",Toast.LENGTH_SHORT).show();
        }
    }
}