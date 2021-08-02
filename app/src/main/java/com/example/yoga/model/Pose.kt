package com.example.yoga.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Pose(
    val file_reference: String,
    val pose_name: String,
    val sanskrit_name: String,
    val translation: List<String>,
    val alt_name: String?,
    val category: String,
    val difficulty: String,
    val description: List<String>,
    val benefits: String
): Parcelable
