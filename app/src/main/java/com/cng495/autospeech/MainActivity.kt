package com.cng495.autospeech

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.cng495.autospeech.ui.theme.AutoSpeechTheme
import com.google.cloud.translate.v3.LocationName
import com.google.cloud.translate.v3.TranslateTextRequest
import com.google.cloud.translate.v3.TranslateTextResponse
import com.google.cloud.translate.v3.TranslationServiceClient


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Thread {
            //Do some Network Request

            try {
                var project_id = "apt-impact-407816"

                var client = TranslationServiceClient.create()
                var parent = LocationName.of(project_id, "global")

                val request = TranslateTextRequest.newBuilder()
                    .setParent(parent.toString())
                    .setMimeType("text/plain")
                    .setTargetLanguageCode("tr")
                    .addContents("Hello world")
                    .build()

                val response: TranslateTextResponse = client.translateText(request)

                for (x in response.translationsList) {
                    println(x.translatedText)
                }

                // v2
//                var tr = TranslateOptions.newBuilder().setProjectId(project_id).build().service
//                var result = tr.translate("Hello world")
//                print(result.translatedText)
            } catch (e: Exception) {
                print(e)
            }

            print("next!")

            runOnUiThread({
                //Update UI
            })
        }.start()

        setContent {
            AutoSpeechTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AutoSpeechTheme {
        Greeting("Android")
    }
}
