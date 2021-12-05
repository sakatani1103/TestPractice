package com.example.testpractice.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Place(
    @ColumnInfo
    val title: String,
    @ColumnInfo
    val comment: String,
    @ColumnInfo
    val hasBeenTo: Boolean = false,
    @ColumnInfo
    val imageUrl: String = "no_image",
    @PrimaryKey
    val placeId: String = UUID.randomUUID().toString()
)