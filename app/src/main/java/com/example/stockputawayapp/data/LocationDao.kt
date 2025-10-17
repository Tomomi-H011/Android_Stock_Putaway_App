package com.example.stockputawayapp.data


import androidx.room.Dao
import androidx.room.Query


@Dao
interface LocationDao {
    @Query("SELECT location FROM locations WHERE department = :department")
    suspend fun getLocationsByDepartment(department: String): List<String>


}