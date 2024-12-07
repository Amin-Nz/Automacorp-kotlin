package com.automacorp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.automacorp.model.RoomDto
import com.automacorp.service.RoomService
import com.automacorp.ui.theme.AutomacorpTheme

class RoomDetailActivity : ComponentActivity() {
    private val viewModel: RoomViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get the room ID from the intent
        val roomId = intent.getLongExtra(RoomActivity.ROOM_PARAM, -1)
        if (roomId != -1L) {
            viewModel.room = RoomService.findById(roomId) // Load room details into ViewModel
        }

        setContent {
            AutomacorpTheme {
                // Pass a lambda to handle back navigation
                RoomDetailScreen(viewModel) { finish() }
            }
        }
    }
}

@Composable
fun RoomDetailScreen(viewModel: RoomViewModel, onBack: () -> Unit) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Room Name: ${viewModel.room?.name ?: "N/A"}", style = MaterialTheme.typography.headlineMedium)

        Text(text = "Current Temperature: ${viewModel.room?.currentTemperature?.toString() ?: "N/A"} °C", style = MaterialTheme.typography.bodyLarge)

        Text(text = "Target Temperature: ${viewModel.room?.targetTemperature?.toString() ?: "N/A"} °C", style = MaterialTheme.typography.bodyLarge)

        // Back button that calls the provided onBack function
        Button(onClick = { onBack() }) { // Call the back navigation function passed as a parameter
            Text("Back")
        }
    }
}