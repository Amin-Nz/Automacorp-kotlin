package com.automacorp.model

enum class WindowStatus { OPENED, CLOSED}

public data class WindowDto(
    val id: Long,
    val name: String,
    val roomName: String,
    val roomId: Long,
    val windowStatus: WindowStatus
)