package com.example.crossword

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.crossword.data.remote.CrosswordScraper
import com.example.crossword.ui.theme.CrosswordTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        //TODO remove from here , for test purposes only
        Thread {
            try {
                val scraper = CrosswordScraper()
                val model = scraper.fetch(
                    "https://www.funland.bg/crosswords/crossword-11002-2026-02-16/"
                )

                println("TITLE: ${model.title}")
                println("WIDTH: ${model.width}")
                println("HEIGHT: ${model.height}")

                for (word in model.words) {
                    println("CLUE ID: ${word.id} -> ${word.clue}")
                }

                for (cell in model.cells) {
                    println("CELL (${cell.x}, ${cell.y}) hwid=${cell.hwid} vwid=${cell.vwid} CHR=${cell.chr}")
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
        //TODO remove to here

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CrosswordTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Niki",
                        modifier = Modifier.padding(innerPadding)
                    )
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
    CrosswordTheme {
        Greeting("Niki")
    }
}