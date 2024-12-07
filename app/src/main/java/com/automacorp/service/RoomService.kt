package com.automacorp.service

import androidx.core.text.isDigitsOnly
import com.automacorp.model.RoomDto
import com.automacorp.model.WindowDto
import com.automacorp.model.WindowStatus

object RoomService {
    val ROOM_KIND: List<String> = listOf("Room", "Meeting", "Laboratory", "Office", "Boardroom")
    val ROOM_NUMBER: List<Char> = ('A'..'Z').toList()
    val WINDOW_KIND: List<String> = listOf("Sliding", "Bay", "Casement", "Hung", "Fixed")

    fun generateWindow(id: Long, roomId: Long, roomName: String): WindowDto {
        return WindowDto(
            id = id,
            name = "${WINDOW_KIND.random()} Window $id",
            roomName = roomName,
            roomId = roomId,
            windowStatus = WindowStatus.values().random()
        )
    }

    fun generateRoom(id: Long): RoomDto {
        val roomName = "${ROOM_NUMBER.random()}$id ${ROOM_KIND.random()}"
        val windows = (1..(1..6).random()).map { generateWindow(it.toLong(), id, roomName) }
        return RoomDto(
            id = id,
            name = roomName,
            currentTemperature = (15..30).random().toDouble(),
            targetTemperature = (15..22).random().toDouble(),
            windows = windows
        )
    }

    // Create 50 rooms
    val ROOMS = (1..50).map { generateRoom(it.toLong()) }.toMutableList()

    // Find all rooms sorted by name
    fun findAll(): List<RoomDto> {
        return ROOMS.sortedBy { it.name }
    }

    // Find a room by its ID
    fun findById(id: Long): RoomDto? {
        return ROOMS.find { it.id == id }
    }

    // Find a room by its name
    fun findByName(name: String): RoomDto? {
        return ROOMS.find { it.name == name }
    }

    // Update a room with the given ID and return the updated room, or throw an exception if not found
    fun updateRoom(id: Long, room: RoomDto): RoomDto? {
        val index = ROOMS.indexOfFirst { it.id == id }
        if (index != -1) {
            // Create a new RoomDto by copying the existing one and updating the properties
            val updatedRoom = ROOMS[index].copy(
                name = room.name,
                targetTemperature = room.targetTemperature,
                currentTemperature = room.currentTemperature
            )
            ROOMS[index] = updatedRoom // Update the room at the found index
            return updatedRoom
        } else {
            // If no room with the given id is found, throw an exception
            throw IllegalArgumentException("Room with ID $id not found")
        }
    }

    // Find a room by name or ID (can handle both)
    fun findByNameOrId(nameOrId: String?): RoomDto? {
        if (nameOrId != null) {
            return if (nameOrId.isDigitsOnly()) {
                findById(nameOrId.toLong())
            } else {
                findByName(nameOrId)
            }
        }
        return null
    }
}
