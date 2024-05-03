package com.example.russiancompanies

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ResultsDao {
    @Query("SELECT * FROM results ORDER BY :order")
    fun getAll(order: String): LiveData<List<ResultEntity>>
    @Insert
    fun insert(vararg result: ResultEntity)
    @Delete
    fun delete(result: ResultEntity)
    @Update
    fun update(vararg result: ResultEntity)

    @Query("DELETE FROM results WHERE name LIKE '%' || :substring || '%'")
    fun deleteBySubstring(substring: String)
}