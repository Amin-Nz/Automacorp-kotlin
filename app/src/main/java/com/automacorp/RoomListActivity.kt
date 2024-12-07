package com.automacorp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.automacorp.model.RoomDto
import com.automacorp.service.RoomService
import com.automacorp.ui.theme.AutomacorpTheme
import com.automacorp.ui.theme.PurpleGrey80
import androidx.compose.ui.platform.LocalContext // Import LocalContext

class RoomListActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AutomacorpTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RoomListScreen() // Call the RoomListScreen to display rooms.
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoomListScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Rooms") }
            )
        }
    ) { innerPadding ->
        // Retrieve context here for navigation
        val context = LocalContext.current
        LazyColumn(
            contentPadding = PaddingValues(4.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(innerPadding),
        ) {
            val rooms = RoomService.findAll() // Fetch the list of rooms from RoomService.
            items(rooms, key = { it.id }) { room ->
                RoomItem(
                    room = room,
                    modifier = Modifier.clickable {
                        // Navigate to the RoomDetailActivity
                        val intent = Intent(context, RoomActivity::class.java).apply {
                            putExtra(RoomActivity.ROOM_PARAM, room.id.toString())
                        }
                        context.startActivity(intent)
                    }
                )
            }
        }
    }
}

@Composable
fun RoomItem(room: RoomDto, modifier: Modifier = Modifier) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        border = BorderStroke(1.dp, PurpleGrey80),
        modifier = modifier.padding(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(20.dp), // Use Modifier here to avoid overriding the parameter
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = room.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Target temperature: ${room.targetTemperature?.toString() ?: "?"}°",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Text(
                text = "${room.currentTemperature?.toString() ?: "?"}°",
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Right
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun RoomItemPreview() {
    AutomacorpTheme {
        RoomItem(
            RoomDto(
                id = 1,
                name = "Living Room",
                currentTemperature = 22.5,
                targetTemperature = 24.0,
                windows = emptyList() // Provide a value for 'windows', e.g., an empty list
            )
        )
    }
}
