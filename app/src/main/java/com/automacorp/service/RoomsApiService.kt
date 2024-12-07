package com.automacorp.service

import com.automacorp.model.RoomDto
import okhttp3.Response
import retrofit2.http.*

interface RoomsApiService {

    @GET("rooms")
    suspend fun getAllRooms(): List<RoomDto> // Replace RoomDto with your data class

    @GET("rooms/{id}")
    suspend fun getRoomById(@Path("id") id: Long): RoomDto

    @PUT("rooms/{id}")
    suspend fun updateRoom(@Path("id") id: Long, @Body room: RoomDto): RoomDto

    @POST("rooms")
    suspend fun createRoom(@Body room: RoomDto): RoomDto

    @DELETE("rooms/{id}")
    suspend fun deleteRoom(@Path("id") id: Long): Response // Use Response for status code
}