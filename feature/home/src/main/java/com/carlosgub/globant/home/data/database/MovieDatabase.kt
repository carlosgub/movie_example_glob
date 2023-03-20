package com.carlosgub.globant.home.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.carlosgub.globant.core.commons.model.MovieModel
import com.carlosgub.globant.home.data.database.dao.MovieDao

@Database(entities = [MovieModel::class], version = 1)
@TypeConverters(Converters::class)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
}
