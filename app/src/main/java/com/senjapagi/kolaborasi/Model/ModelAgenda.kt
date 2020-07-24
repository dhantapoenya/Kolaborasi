package com.senjapagi.kolaborasi.Model

data class ModelAgenda(
    val id: String,
    val nama: String,
    val deskripsi: String,
    val kuota: String,
    val id_divisi: String,
    val id_entitas: String,
    val nama_divisi: String,
    val status: String,
    val open_gate: String,
    val close_gate: String,
    val lokasi: String) {
}