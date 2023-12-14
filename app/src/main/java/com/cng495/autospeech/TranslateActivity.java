package com.cng495.autospeech;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import com.google.mlkit.nl.languageid.LanguageIdentification;
import com.google.mlkit.nl.languageid.LanguageIdentifier;

import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.Nullable;

public class TranslateActivity extends Activity {

    EditText textInput;
    TextView detectedLanguage ;
    TextView translatedView;
    private boolean connected;
    Translate translate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate_old);

        textInput = findViewById(R.id.text_to_translate_input);
        detectedLanguage = findViewById(R.id.detected_language);
        translatedView = findViewById(R.id.translated_text_view);



        textInput.addTextChangedListener(new TextWatcher() {
            private Timer timer;
            @Override
            public void afterTextChanged(Editable arg0) {
                getTextLanguage(arg0.toString());
                // user typed: start the timer
                timer = new Timer();

                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (checkInternetConnection()) {

                            //If there is internet connection, get translate service and start translation:
                            getTranslateService();
                            translate();

                        } else {

                            //If not, display "no connection" warning:
                            translatedView.setText(getResources().getString(R.string.no_connection));
                        }
                    }
                }, 600); // 600ms delay before the timer executes the „run“ method from TimerTask
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // nothing to do here
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // user is typing: reset already started timer (if existing)
                if (timer != null) {
                    timer.cancel();
                }
            }
        });




    }

    private void getTextLanguage(String text)
    {
        LanguageIdentifier languageIdentifier = LanguageIdentification.getClient();

        languageIdentifier.identifyLanguage(text)
                .addOnSuccessListener(
                        new OnSuccessListener<String>() {
                            @Override
                            public void onSuccess(@Nullable String languageCode) {
                                if (languageCode.equals("und"))
                                {
                                    Log.i("Test", "Can't identify language.");
                                }
                                else
                                {
                                    Log.i("Test", "Language: " + languageCode);

                                    detectedLanguage.setText(languageCode);



                                }
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Model couldn’t be loaded or other internal error.
                                // ...
                                translatedView.setText(getResources().getString(R.string.language_detection_error));
                            }
                        });
    }



    public void getTranslateService() {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try (InputStream is = getResources().openRawResource(R.raw.credentials)) {

            //Get credentials:
            final GoogleCredentials myCredentials = GoogleCredentials.fromStream(is);

            //Set credentials and get translate service:
            TranslateOptions translateOptions = TranslateOptions.newBuilder().setCredentials(myCredentials).build();
            translate = translateOptions.getService();

        } catch (IOException ioe) {
            ioe.printStackTrace();

        }
    }

    public void translate() {

        //Get input text to be translated:
        String originalText = textInput.getText().toString();
        Translation translation = translate.translate(originalText, Translate.TranslateOption.targetLanguage("tr"),
                Translate.TranslateOption.model("base"));
        String translatedText = translation.getTranslatedText();

        //Translated text and original text are set to TextViews:
        translatedView.setText(translatedText);

    }

    public boolean checkInternetConnection() {

        //Check internet connection:
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        //Means that we are connected to a network (mobile or wi-fi)
        connected = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;

        return connected;
    }
}