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
    val benefits: String,
    val pose_direction: String?,
    val pose_angles: PoseAngles?

): Parcelable

@Parcelize
data class PoseAngles(
    val right_hip_1: Int?,   //Lh Rh Rs
    val right_hip_2: Int?,   //Lh Rh Rk
    val left_hip_1: Int?,   //Rh Lh Ls
    val left_hip_2: Int?,   //Rh Lh Rk
    val right_knee: Int?,   //Rh Rk Ra
    val left_knee: Int?,   //lh lk la
    val right_elbow: Int?,   //Rs Re Rw
    val left_elbow: Int?,   //Ls Le Lw
): Parcelable