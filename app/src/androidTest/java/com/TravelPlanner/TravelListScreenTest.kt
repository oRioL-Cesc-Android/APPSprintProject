package com.TravelPlanner

import com.TravelPlanner.models.TravelItem
import com.TravelPlanner.ui.viewmodel.TravelListViewModel
import com.TravelPlanner.models.ActivityItems


import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TravelListViewModelTest {

    private lateinit var viewModel: TravelListViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
        viewModel = TravelListViewModel()
    }

    @Test
    fun `addTravelItem adds a new item`() = runTest {
        val travel = TravelItem(1, "Viaje A", "Madrid", "Bonito viaje", 4.5f, "5 días")
        viewModel.addTravelItem(travel)
        advanceUntilIdle() // <-- Asegura que se actualice el StateFlow
        assertEquals(1, viewModel.travelItems.value.size)
        assertEquals(travel, viewModel.travelItems.value.first())
    }


    @Test
    fun `deleteTravelItem removes the item`() = runTest {
        val travel = TravelItem(1, "Viaje A", "Madrid", "Bonito viaje", 4.5f, "5 días")
        viewModel.addTravelItem(travel)
        viewModel.deleteTravelItem(travel)
        assertTrue(viewModel.travelItems.value.isEmpty())
    }

    @Test
    fun `updateTravelItem modifies existing item`() = runTest {
        val travel = TravelItem(1, "Viaje A", "Madrid", "Bonito viaje", 4.5f, "5 días")
        viewModel.addTravelItem(travel)
        advanceUntilIdle() // <-- Esperar que se agregue correctamente

        val updated = travel.copy(title = "Viaje Modificado")
        viewModel.updateTravelItem(updated)
        advanceUntilIdle() // <-- Esperar a que se actualice

        val result = viewModel.travelItems.value.firstOrNull()
        assertNotNull(result)
        assertEquals("Viaje Modificado", result!!.title)
    }


    @Test
    fun `addActivityToTravel adds activity correctly`() = runTest {
        val travel = TravelItem(1, "Viaje A", "Madrid", "Bonito viaje", 4.5f, "5 días")
        viewModel.addTravelItem(travel)
        advanceUntilIdle()

        val activity = ActivityItems("Museo", "Centro", 2)
        viewModel.addActivityToTravel(travel.id, activity)
        advanceUntilIdle()

        assertEquals(1, viewModel.travelItems.value.first().activities.size)
        assertEquals(activity, viewModel.travelItems.value.first().activities.first())
    }


    @Test
    fun `removeActivityFromTravel removes activity correctly`() = runTest {
        val activity = ActivityItems("Museo", "Centro", 2)
        val travel = TravelItem(
            id = 1,
            title = "Viaje A",
            location = "Madrid",
            description = "Bonito viaje",
            rating = 4.5f,
            duration = "5 días",
            activities = listOf(activity)
        )

        viewModel.addTravelItem(travel)
        advanceUntilIdle() // <-- Esperar que se agregue correctamente antes de continuar

        viewModel.removeActivityFromTravel(travel.id, activity)
        advanceUntilIdle() // <-- Esperar a que termine la eliminación

        val result = viewModel.travelItems.value.firstOrNull()
        assertNotNull(result)
        assertTrue(result!!.activities.isEmpty())
    }


    @Test
    fun `updateActivityInTravel updates an activity correctly`() = runTest {
        val activity = ActivityItems("Museo", "Centro", 2)
        val travel = TravelItem(1, "Viaje A", "Madrid", "Bonito viaje", 4.5f, "5 días", listOf(activity))
        viewModel.addTravelItem(travel)
        advanceUntilIdle() // <-- Asegurarte de que se ha añadido el travel

        val updatedActivity = activity.copy(duration = 3)
        viewModel.updateActivityInTravel(travel.id, updatedActivity)
        advanceUntilIdle() // <-- Esperar a que se procese la actualización

        assertEquals(3, viewModel.travelItems.value.first().activities.first().duration)
    }

    @Test
    fun `startEditing and stopEditing change editingItemId correctly`() {
        viewModel.startEditing(1)
        assertEquals(1, viewModel.editingItemId.value)
        viewModel.stopEditing()
        assertNull(viewModel.editingItemId.value)
    }
}
