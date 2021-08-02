package com.example.yoga.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.yoga.data.poses

class YogaAsanaViewModel(application: Application): AndroidViewModel(application){

    val poses = poses(application.applicationContext)
}