package com.example.madlevel4task1

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "productTable")
data class Product(
    @ColumnInfo(name = "quantity")
    var quantity: Int,
    @ColumnInfo(name = "name")
    var name: String,
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long? = null
)