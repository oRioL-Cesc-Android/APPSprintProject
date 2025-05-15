package com.TravelPlanner.data.repo

import com.TravelPlanner.data.remote.api.HotelAPiservice
import com.TravelPlanner.data.remote.dto.*
import javax.inject.Inject

class HotelRepository @Inject constructor(
    private val api: HotelAPiservice
) {

    suspend fun getHotels(groupId: String): List<HotelDto> {
        return api.listHotels(groupId)
    }

    suspend fun checkAvailability(
        groupId: String,
        startDate: String,
        endDate: String,
        hotelId: String? = null,
        city: String? = null
    ): List<AvailabilityDto> {
        return api.checkAvailability(groupId, startDate, endDate, hotelId, city)
    }

    suspend fun reserveRoom(groupId: String, reservation: ReservationAvailabilityDto): ResponseBodyDto {
        return api.reserveRoom(groupId, reservation)
    }

    suspend fun cancelReservation(groupId: String, cancelRequest: CancelRequestDto): ResponseBodyDto {
        return api.cancelReservation(groupId, cancelRequest)
    }

    suspend fun getGroupReservations(groupId: String): List<ReservationDto> {
        return api.listGroupReservations(groupId)
    }

    suspend fun getAllReservations(): List<ReservationDto> {
        return api.listAllReservations()
    }

    suspend fun getReservationById(resId: Int): ReservationDto {
        return api.getReservationById(resId)
    }

    suspend fun cancelReservationById(resId: Int): ResponseBodyDto {
        return api.cancelReservationById(resId)
    }
}
