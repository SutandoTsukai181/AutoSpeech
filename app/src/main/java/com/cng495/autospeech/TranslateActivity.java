package com.cng495.autospeech;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.nl.languageid.LanguageIdentification;
import com.google.mlkit.nl.languageid.LanguageIdentifier;

import javax.annotation.Nullable;

public class TranslateActivity extends AppCompatActivity {

    EditText textInput  = (EditText) findViewById(R.id.text_to_translate_input);
    TextView detectedLanguage = findViewById(R.id.detected_language);
    TextView translatedView = findViewById(R.id.translateTextView);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate);

        textInput.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                getTextLanguage(s.toString());
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
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
                            }
                        });
    }
}