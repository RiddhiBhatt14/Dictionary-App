package com.example.dictionary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dictionary.ui.theme.DictionaryTheme
import com.example.dictionary.viewmodel.DictionaryViewModel
import androidx.compose.ui.text.font.FontFamily

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DictionaryTheme {
                DictionaryScreen()
            }
        }
    }
}

@Composable
fun DictionaryScreen(
    viewModel: DictionaryViewModel = viewModel()
) {
    var word by remember { mutableStateOf("") }

    val result by viewModel.result.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF0B1120)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            item {
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "DICTIONARY",
                    style = MaterialTheme.typography.displaySmall.copy(
                        fontFamily = FontFamily.Monospace
                    ),
                    color = Color(0xFF38BDF8)
                )
            }

            item {
                OutlinedTextField(
                    value = word,
                    onValueChange = { word = it },
                    label = { Text("Enter a word") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF38BDF8),
                        unfocusedBorderColor = Color(0xFF334155),
                        focusedLabelColor = Color(0xFF38BDF8),
                        unfocusedLabelColor = Color(0xFF94A3B8),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        cursorColor = Color(0xFF38BDF8)
                    ),
                    shape = RoundedCornerShape(14.dp)
                )
            }

            item {
                Button(
                    onClick = {
                        if (word.isBlank()) {
                            viewModel.showError("Please enter a word")
                        } else {
                            viewModel.searchWord(word.trim())
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF0284C7)
                    ),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text(
                        text = "SEARCH",
                        color = Color.White
                    )
                }
            }

            item {
                if (loading) {
                    Box(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(32.dp),
                            color = Color(0xFF38BDF8)
                        )
                    }
                }

                error?.let {
                    Text(
                        text = it,
                        color = Color(0xFFF87171),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            result?.let { dictionary ->

                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF111827)
                        ),
                        shape = RoundedCornerShape(18.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp)
                        ) {

                            Text(
                                text = dictionary.word ?: "",
                                style = MaterialTheme.typography.headlineMedium,
                                color = Color.White
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = dictionary.phonetics
                                    ?.firstOrNull { !it.text.isNullOrBlank() }
                                    ?.text
                                    ?: "No pronunciation available",
                                color = Color(0xFF38BDF8),
                                style = MaterialTheme.typography.bodyLarge
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            dictionary.meanings?.forEach { meaning ->

                                Text(
                                    text = meaning.partOfSpeech ?: "",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Color(0xFF7DD3FC)
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                meaning.definitions?.forEach { definition ->

                                    Text(
                                        text = "• ${definition.definition ?: ""}",
                                        color = Color(0xFFE2E8F0),
                                        style = MaterialTheme.typography.bodyLarge
                                    )

                                    definition.example?.let { example ->
                                        Spacer(modifier = Modifier.height(4.dp))

                                        Text(
                                            text = "Example: $example",
                                            color = Color(0xFF94A3B8),
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(12.dp))
                                }

                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}