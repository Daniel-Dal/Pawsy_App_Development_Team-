package com.example.pawsy

import android.content.Context
import android.net.Uri
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await

/**
 * Guarda la mascota en "mascotas" y agrega su id al arreglo "mascotas"
 * del documento del usuario. Devuelve el id de la mascota.
 * Lanza una excepción si algo falla (se llama dentro de try/catch).
 */
suspend fun guardarMascota(
    context: Context,
    nombre: String,
    especie: String,
    raza: String,
    edad: String,
    peso: String,
    imagenUri: Uri?
): String {
    val usuario = FirebaseAuth.getInstance().currentUser
        ?: error("Debes crear una cuenta o iniciar sesión.")

    val db = FirebaseFirestore.getInstance()

    val urlImagen = if (imagenUri != null) subirImagen(context, imagenUri) else ""

    val mascotaRef = db.collection("mascotas").document()      // id único automático
    val usuarioRef = db.collection("usuarios").document(usuario.uid)

    val datosMascota = hashMapOf(
        "nombre" to nombre.trim(),
        "especie" to especie.trim(),
        "raza" to raza.trim(),
        "edad" to (edad.trim().toIntOrNull() ?: edad.trim()),
        "peso" to (peso.trim().replace(',', '.').toDoubleOrNull() ?: peso.trim()),
        "urlimagen" to urlImagen,
        "idDueno" to usuario.uid,
        "fecha_registro" to Timestamp.now()
    )

    val batch = db.batch()
    batch.set(mascotaRef, datosMascota)
    batch.set(
        usuarioRef,
        hashMapOf("mascotas" to FieldValue.arrayUnion(mascotaRef.id)),
        SetOptions.merge()
    )
    batch.commit().await()

    return mascotaRef.id
}