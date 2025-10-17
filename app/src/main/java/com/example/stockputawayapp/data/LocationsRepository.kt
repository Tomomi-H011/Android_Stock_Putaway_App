package com.example.stockputawayapp.data


class LocationsRepository(private val dao: LocationDao) {

    suspend fun getLocations(department: String): List<String> {
        return dao.getLocationsByDepartment(department)
    }
}