import org.junit.Test
import org.junit.Assert.*

// MODELOS

data class HotelResponse(
    val available_hotels: List<Hotel>
)

data class Hotel(
    val id: String,
    val name: String,
    val address: String,
    val rating: Int,
    val image_url: String,
    val rooms: List<Room>
)

data class Room(
    val id: String,
    val room_type: String,
    val price: Int,
    val images: List<String>
)

// INTERFAZ API

interface HotelApi {
    suspend fun checkAvailability(
        groupId: String,
        startDate: String,
        endDate: String,
        hotelId: String? = null,
        city: String? = null
    ): HotelResponse
}

// IMPLEMENTACIÓN FALSA PARA PRUEBA

class FakeHotelApi : HotelApi {
    override suspend fun checkAvailability(
        groupId: String,
        startDate: String,
        endDate: String,
        hotelId: String?,
        city: String?
    ): HotelResponse {
        // Devuelve datos fijos si los parámetros coinciden
        return if (
            groupId == "G06" &&
            startDate == "2025-05-01" &&
            endDate == "2025-05-01" &&
            hotelId == "BCN01" &&
            city == "BCN"
        ) {
            HotelResponse(
                available_hotels = listOf(
                    Hotel(
                        id = "BCN01",
                        name = "Hotel Ramblas",
                        address = "La Rambla 33, Barcelona",
                        rating = 4,
                        image_url = "/images/BCN01.png",
                        rooms = listOf(
                            Room("R1", "single", 80, listOf("/images/BCN01R1.png")),
                            Room("R2", "double", 120, listOf("/images/BCN01R2.png")),
                            Room("R3", "suite", 200, listOf("/images/BCN01R3.png"))
                        )
                    )
                )
            )
        } else {
            HotelResponse(available_hotels = emptyList())
        }
    }
}

// TEST

class HotelApiTest {

    private val api = FakeHotelApi()

    @Test
    fun testCheckAvailability_withValidParams_returnsExpectedHotel() {
        val response = runBlockingTest {
            api.checkAvailability(
                groupId = "G06",
                startDate = "2025-05-01",
                endDate = "2025-05-01",
                hotelId = "BCN01",
                city = "BCN"
            )
        }

        assertEquals(1, response.available_hotels.size)
        val hotel = response.available_hotels[0]
        assertEquals("Hotel Ramblas", hotel.name)
        assertEquals("La Rambla 33, Barcelona", hotel.address)
        assertEquals(4, hotel.rating)
        assertEquals(3, hotel.rooms.size)
        assertEquals("suite", hotel.rooms[2].room_type)
        assertEquals(200, hotel.rooms[2].price)
    }

    private fun <T> runBlockingTest(block: suspend () -> T): T {
        return kotlinx.coroutines.runBlocking { block() }
    }
}
