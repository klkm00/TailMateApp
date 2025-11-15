package com.example.baseproject.model

data class User(
    val name: String,
    val password: String,
    val createdAt: Long = System.currentTimeMillis()
)
//usuarioi