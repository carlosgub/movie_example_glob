package com.carlosgub.globant.home.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.carlosgub.globant.core.commons.model.MovieModel

@Dao
interface MovieDao {

    @Query("SELECT * FROM MOVIES")
    fun getMovies(): List<MovieModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMovie(movieEntity: MovieModel)

    @Query("DELETE FROM MOVIES")
    suspend fun deleteMovies()
}
