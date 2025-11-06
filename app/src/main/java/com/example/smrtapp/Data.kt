package com.example.smrtapp

import androidx.annotation.DrawableRes

data class Specialization(val name: String, @DrawableRes val imageRes: Int)
data class Doctor(
    val id: String, 
    val name: String, 
    val specialization: String, 
    val clinic: String, 
    val location: String, 
    val contact: String, 
    val bookingFee: Int,
    @DrawableRes val imageRes: Int,
    val isAvailable: Boolean = true
)

object DataSource {
    val doctors = listOf(
        Doctor("1", "Dr. Daniel Medson", "Dentist", "Ali Clinic", "Lilongwe", "+265 881 770 119", 1500, R.drawable.doc1),
        Doctor("2", "Dr. Bless Katumbi", "Surgeon", "Happy Teeth Dental", "Los Angeles", "098-765-4321", 2000, R.drawable.doc2),
        Doctor("3", "Dr Mercy chiponde", "Optometry", "Mercy Clinic", "Blantyre", "111-222-3333", 1800, R.drawable.doc3),
        Doctor("4", "Dr hestings chapotera", "Psychotherapist", "Chapotera Clinic", "Zomba", "444-555-6666", 2500, R.drawable.doc4),
        Doctor("5", "Dr Robert chikwe", "Dentist", "Chikwe Clinic", "Zomba", "444-555-6666", 2500, R.drawable.doc1, false),
        Doctor("6", "Dr Ronald banda", "Surgeon", "Banda Clinic", "Zomba", "444-555-6666", 2500, R.drawable.doc2, false),
        Doctor("7", "Dr Moses Makawa", "Psychotherapist", "Makawa Clinic", "Zomba", "444-555-6666", 2500, R.drawable.doc4, false),
        Doctor("8", "Dr Liam Phiri", "Optometry", "Phiri Clinic", "Zomba", "444-555-6666", 2500, R.drawable.doc3, false)
    )

    val specializations = listOf(
        Specialization("Dentist", R.drawable.dentist),
        Specialization("Surgeon", R.drawable.surgeon),
        Specialization("Physicist", R.drawable.physicist),
        Specialization("Optometry", R.drawable.optometry),
        Specialization("Psychotherapist", R.drawable.psychotherapist)
    )
}
