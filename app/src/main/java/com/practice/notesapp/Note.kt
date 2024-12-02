package com.practice.notesapp

data class Note(
    val id: Int,
    val title: String,
    val content: String,
    val lastEditedDate: Long? = null
)
