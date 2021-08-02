package com.example.yoga.data

import android.content.Context
import com.example.yoga.model.Pose
import com.example.yoga.utils.getJsonDataFromAsset
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


fun poses(applicationContext: Context) : List<Pose>{
    val jsonFileString = getJsonDataFromAsset(applicationContext, "poses.json")
    val listPersonType = object : TypeToken<List<Pose>>() {}.type
    return Gson().fromJson(jsonFileString, listPersonType)
}