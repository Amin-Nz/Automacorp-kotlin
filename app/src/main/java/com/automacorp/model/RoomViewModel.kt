package com.automacorp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.automacorp.model.RoomDto
import com.automacorp.service.ApiServices
import kotlinx.coroutines.launch

class RoomViewModel : ViewModel() {
    var rooms by mutableStateOf<List<RoomDto>>(emptyList()) // State for the list of rooms
    var room by mutableStateOf<RoomDto?>(null) // State for a single room
    var isLoading by mutableStateOf(false) // Loading state
    var errorMessage by mutableStateOf<String?>(null) // Error message state

    // Function to fetch all rooms
    fun fetchAllRooms() {
        isLoading = true // Set loading state to true
        viewModelScope.launch {
            try {
                rooms = ApiServices.roomsApiService.getAllRooms() // Fetch all rooms from API
                errorMessage = null // Clear any previous error message
            } catch (e: Exception) {
                errorMessage = "Failed to load rooms: ${e.message}" // Set error message on failure
            } finally {
                isLoading = false // Reset loading state
            }
        }
    }

    // Function to fetch a room by ID
    fun fetchRoomById(id: Long) {
        isLoading = true // Set loading state to true
        viewModelScope.launch {
            try {
                room = ApiServices.roomsApiService.getRoomById(id) // Fetch room by ID from API
                errorMessage = null // Clear any previous error message
            } catch (e: Exception) {
                errorMessage = "Failed to load room: ${e.message}" // Set error message on failure
            } finally {
                isLoading = false // Reset loading state
            }
        }
    }

    // Function to create a new room (example)
    fun createRoom(newRoom: RoomDto) {
        viewModelScope.launch {
            try {
                val createdRoom = ApiServices.roomsApiService.createRoom(newRoom)
                rooms = rooms + createdRoom // Add the newly created room to the list
                errorMessage = null // Clear any previous error message
            } catch (e: Exception) {
                errorMessage = "Failed to create room: ${e.message}" // Set error message on failure
            }
        }
    }

    // Function to update a room (example)
    fun updateRoom(updatedRoom: RoomDto) {
        viewModelScope.launch {
            try {
                val updatedResponse = ApiServices.roomsApiService.updateRoom(updatedRoom.id, updatedRoom)
                room = updatedResponse // Update the current room state with the response
                errorMessage = null // Clear any previous error message
            } catch (e: Exception) {
                errorMessage = "Failed to update room: ${e.message}" // Set error message on failure
            }
        }
    }

    // Function to delete a room by ID (example)
    fun deleteRoom(id: Long) {
        viewModelScope.launch {
            try {
                ApiServices.roomsApiService.deleteRoom(id) // Call API to delete the room
                rooms = rooms.filterNot { it.id == id } // Remove deleted room from the list
                errorMessage = null // Clear any previous error message
            } catch (e: Exception) {
                errorMessage = "Failed to delete room: ${e.message}" // Set error message on failure
            }
        }
    }
}