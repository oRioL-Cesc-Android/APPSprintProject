package com.TravelPlanner.data.repo

import com.TravelPlanner.data.remote.dto.ReservationDto
import com.TravelPlanner.models.Hotel
import com.TravelPlanner.models.ReserveRequest
import com.zeni.core.data.remote.api.HotelApiService
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class HotelRepositoryImpl @Inject constructor(
    private val api: HotelApiService,
) : HotelRepository {
    val groupId: String = "G06"


    /* ---------- Hotels ---------- */
    override suspend fun getHotels(): List<Hotel> =
        api.getHotels(groupId).map { it.toDomain() }

    /* ---------- Availability ---------- */
    override suspend fun getAvailability(
        groupId: String,
        start: String,
        end: String,
        hotelId: String?,
        city: String?
    ): List<Hotel> =
        api.getAvailability(groupId, start, end, hotelId, city)
            .available_hotels
            .map { it.toDomain() }

    /* ---------- Reserve & Cancel (within group) ---------- */
    override suspend fun reserve(groupId: String, request: ReserveRequest): ReservationDto =
        api.reserveRoom(groupId, request.toDto()).reservation.toDomain()

    override suspend fun cancel(groupId: String, request: ReserveRequest): String =
        api.cancelReservation(groupId, request.toDto()).message

    /* ---------- Reservations for a group ---------- */
    override suspend fun getGroupReservations(
        groupId: String,
        guestEmail: String?
    ): List<Reservation> =
        api.getGroupReservations(groupId, guestEmail).reservations.map { it.toDomain() }

    /* ---------- All reservations (admin) ---------- */
    override suspend fun getAllReservations(): Map<String, List<Reservation>> =
        api.getAllReservations()
            .groups
            .mapValues { entry -> entry.value.map { it.toDomain() } }

    /* ---------- Single-ID operations ---------- */
    override suspend fun getReservationById(resId: String): Reservation =
        api.getReservationById(resId).toDomain()

    override suspend fun cancelById(resId: String): Reservation =
        api.deleteReservationById(resId).toDomain()
}