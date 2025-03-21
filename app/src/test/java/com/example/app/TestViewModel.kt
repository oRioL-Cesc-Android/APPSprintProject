import com.example.app.ui.screens.Activitys
import com.example.app.ui.screens.TravelItem
import com.example.app.ui.viewmodel.TravelListViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TravelListViewModelTest {

    private lateinit var viewModel: TravelListViewModel

    @Before
    fun setup() {
        viewModel = TravelListViewModel()
    }

    @Test
    fun addTravelItemPasses() = runBlocking {
        val item = TravelItem(1, "Trip to Paris", "France", "A wonderful trip", 4.5f, "5 days")

        viewModel.addTravelItem(item)

        assertEquals(listOf(item), viewModel.travelItems.first())
    }

    @Test
    fun updateTravelItemPasses() = runBlocking {
        val item = TravelItem(1, "Trip to Paris", "France", "A wonderful trip", 4.5f, "5 days")
        val updatedItem = item.copy(title = "Trip to Rome")

        viewModel.addTravelItem(item)
        viewModel.updateTravelItem(updatedItem)

        assertEquals(listOf(updatedItem), viewModel.travelItems.first())
    }

    @Test
    fun deleteTravelItemPasses() = runBlocking {
        val item = TravelItem(1, "Trip to Paris", "France", "A wonderful trip", 4.5f, "5 days")

        viewModel.addTravelItem(item)
        viewModel.deleteTravelItem(item)

        assertEquals(emptyList<TravelItem>(), viewModel.travelItems.first())
    }

    @Test
    fun addActivityToTravelPasses() = runBlocking {
        val activity = Activitys("Visit Eiffel Tower", "Paris", 2)
        val item = TravelItem(1, "Trip to Paris", "France", "A wonderful trip", 4.5f, "5 days")

        viewModel.addTravelItem(item)
        viewModel.addActivityToTravel(travelId = 1, activity = activity)

        val updatedItem = viewModel.travelItems.first().first()
        assertEquals(listOf(activity), updatedItem.activities)
    }

    @Test
    fun removeActivityFromTravelPasses() = runBlocking {
        val activity = Activitys("Visit Eiffel Tower", "Paris", 2)
        val item = TravelItem(1, "Trip to Paris", "France", "A wonderful trip", 4.5f, "5 days", listOf(activity))

        viewModel.addTravelItem(item)
        viewModel.removeActivityFromTravel(travelId = 1, activity = activity)

        val updatedItem = viewModel.travelItems.first().first()
        assertEquals(emptyList<Activitys>(), updatedItem.activities)
    }

    @Test
    fun updateActivityInTravelPasses() = runBlocking {
        val activity = Activitys("Visit Eiffel Tower", "Paris", 2)
        val updatedActivity = activity.copy(duration = 3)
        val item = TravelItem(1, "Trip to Paris", "France", "A wonderful trip", 4.5f, "5 days", listOf(activity))

        viewModel.addTravelItem(item)
        viewModel.updateActivityInTravel(travelId = 1, updatedActivity = updatedActivity)

        val updatedItem = viewModel.travelItems.first().first()
        assertEquals(listOf(updatedActivity), updatedItem.activities)
    }
}

