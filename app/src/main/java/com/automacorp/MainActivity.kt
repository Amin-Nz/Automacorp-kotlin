package com.automacorp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.automacorp.ui.theme.AutomacorpTheme

class MainActivity : ComponentActivity() {
    companion object {
        const val ROOM_PARAM = "com.automacorp.room.attribute"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AutomacorpTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting(onClick = { name ->
                        if (name.isNotBlank()) {
                            Log.d("MainActivity", "Opening room with name: $name")
                            val intent = Intent(this@MainActivity, RoomActivity::class.java).apply {
                                putExtra(ROOM_PARAM, name)
                            }
                            startActivity(intent)
                        } else {
                            Log.w("MainActivity", "Attempted to open room with blank name")
                            Toast.makeText(this, "Please enter a room name", Toast.LENGTH_SHORT).show()
                        }
                    })
                }
            }
        }
    }
}

@Composable
fun Greeting(onClick: (name: String) -> Unit, modifier: Modifier = Modifier) {
    var name by remember { mutableStateOf("") }

    Column(modifier = modifier.padding(16.dp)) {
        Text("Welcome to Automacorp!")

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Enter Room Name") },
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Button(
            onClick = { onClick(name) },
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Text("Open Room")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AutomacorpTheme {
        Greeting(onClick = {})
    }
}