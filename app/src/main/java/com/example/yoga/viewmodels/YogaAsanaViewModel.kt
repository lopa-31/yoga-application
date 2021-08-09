package com.example.yoga.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.yoga.data.poses
import com.example.yoga.model.Pose

class YogaAsanaViewModel(application: Application) : AndroidViewModel(application) {

    val poses = poses(application.applicationContext)
    val sortedPoses: List<Pose> = poses.sortedBy { it.sanskrit_name }
    val filteredPoses = mutableListOf<Pose>()

    init {
        filteredPoses.addAll(sortedPoses)
    }

    private val _selectedDifficulty = MutableLiveData("All")
    val selectedDifficulty: LiveData<String>
        get() = _selectedDifficulty

    fun setSelectedDifficulty(value: String) {
        _selectedDifficulty.value = value
    }

    fun getSelectedDifficulty() = _selectedDifficulty.value ?: "All"
}