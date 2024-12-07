package com.automacorp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.automacorp.model.RoomDto
import com.automacorp.ui.theme.AutomacorpTheme
import kotlin.math.round
import com.automacorp.service.RoomService

class RoomActivity : ComponentActivity() {
    companion object {
        const val ROOM_PARAM = "com.automacorp.room.attribute"
    }

    private val viewModel: RoomViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Get the room parameter from the intent
        val param = intent.getStringExtra(ROOM_PARAM)
        Log.d("RoomActivity", "Received param: $param")

        // Load room data into the ViewModel if it's not already set
        if (viewModel.room == null) {
            viewModel.room = RoomService.findByNameOrId(param)
            Log.d("RoomActivity", "Found room: ${viewModel.room}")
        }

        // Define what happens when the room is saved.
        val onRoomSave: () -> Unit = {
            viewModel.room?.let { roomDto ->
                RoomService.updateRoom(roomDto.id, roomDto)
                Toast.makeText(baseContext, "Room ${roomDto.name} was updated", Toast.LENGTH_LONG).show()
                // Navigate back to MainActivity after saving.
                startActivity(Intent(baseContext, MainActivity::class.java))
                finish() // Optionally finish this activity to remove it from the back stack.
            }
        }

        // Define what happens when navigating back
        val navigateBack: () -> Unit = { finish() }

        setContent {
            AutomacorpTheme {
                Scaffold(
                    topBar = { AutomacorpTopAppBar(title = "Room", returnAction = navigateBack) },
                    floatingActionButton = { RoomUpdateButton(onClick = onRoomSave) },
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    if (viewModel.room != null) {
                        RoomDetail(viewModel, Modifier.padding(innerPadding)) // Pass the ViewModel here.
                    } else {
                        NoRoom(Modifier.padding(innerPadding))
                    }
                }
            }
        }
    }
}

@Composable
fun NoRoom(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(id = R.string.act_room_none),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun RoomDetail(model: RoomViewModel, modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(16.dp)) {
        Text(
            text = stringResource(R.string.act_room_name),
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(bottom=4.dp)
        )

        OutlinedTextField(
            value=model.room?.name ?: "",
            onValueChange={ newName ->
                model.room?.let { room ->
                    model.room=room.copy(name=newName)  // Update the room name in the ViewModel.
                }
            },
            label={ Text(text=stringResource(R.string.act_room_name)) },
            modifier=Modifier.fillMaxWidth()
        )

        Text(
            text=stringResource(R.string.act_room_current_temperature)+": ${model.room?.currentTemperature?.let { "%.1f".format(it) } ?: "N/A"} °C",
            style=MaterialTheme.typography.bodyLarge,
            modifier=Modifier.padding(top=8.dp)
        )

        Text(text=stringResource(R.string.act_room_target_temperature))

        Slider(
            value=model.room?.targetTemperature?.toFloat() ?: 18.0f,
            onValueChange={ newTargetTemp ->
                model.room?.let { room ->
                    model.room=room.copy(targetTemperature=newTargetTemp.toDouble())  // Update target temperature in ViewModel.
                }
            },
            valueRange=10f..28f,
            modifier=Modifier.padding(vertical=8.dp)
        )

        Text(
            text="Selected Target Temperature: ${(round((model.room?.targetTemperature ?: 18.0) * 10) / 10)} °C",
            style=MaterialTheme.typography.bodyLarge,
            modifier=Modifier.padding(top=8.dp)
        )
    }
}

@Composable
fun RoomUpdateButton(onClick: () -> Unit) {
    ExtendedFloatingActionButton(
        onClick={onClick()},
        icon={
            Icon(
                Icons.Filled.Done,
                contentDescription= stringResource(R.string.act_room_save),
            )
        },
        text={Text(text=stringResource(R.string.act_room_save))}
    )
}

@Preview(showBackground=true)
@Composable
fun RoomDetailPreview() {
    val roomDto= RoomDto(
        id=1,
        name="Sample Room",
        currentTemperature=22.5,
        targetTemperature=22.0,
        windows= emptyList()
    )
    AutomacorpTheme {
        RoomDetail(RoomViewModel().apply { room=roomDto })  // Pass a sample ViewModel for previewing.
    }
}