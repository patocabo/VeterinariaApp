package com.example.veterinaria.model

data class Veterinario(val nombre: String, val id: Int) {
    override fun toString(): String {
        return nombre
    }
}
