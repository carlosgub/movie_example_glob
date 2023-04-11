package com.carlosgub.globant.home.data.database

import androidx.room.TypeConverter
import com.carlosgub.globant.core.commons.model.CastModel
import com.carlosgub.globant.core.commons.model.GenresModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

object Converters {
    @TypeConverter
    fun fromString(value: String): List<CastModel> {
        val listType: Type = object : TypeToken<List<CastModel>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromArrayList(list: List<CastModel>): String {
        val gson = Gson()
        return gson.toJson(list)
    }

    @TypeConverter
    fun fromStringGenresModel(value: String): List<GenresModel> {
        val listType: Type = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromArrayListGenresModel(list: List<GenresModel>): String {
        val gson = Gson()
        return gson.toJson(list)
    }
}
