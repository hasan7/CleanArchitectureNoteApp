package com.plcoding.cleanarchitecturenoteapp.feature_note.domain.model

import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.plcoding.cleanarchitecturenoteapp.ui.theme.*

@Entity
data class Note(
    val title: String,
    val content: String,
    val timestamp: Long,
    val color: Int,
    @PrimaryKey val id: Int? = null
){
    companion object {
        val intialColor = LightGray
        val noteColors = listOf(RedOrange, LightGreen, Violet, BabyBlue)
    }
}

class invalideNoteException(message: String): Exception(message)


