package com.example.recipeapp

import com.example.recipeapp.util.Greeting
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito
import java.util.Calendar

class GreetingTest {
    private val greeting = Greeting()

    @Test
    fun testMorningGreeting() {
        val calendar = Mockito.mock(Calendar::class.java)
        Mockito.`when`(calendar.get(Calendar.HOUR_OF_DAY)).thenReturn(8)

        val greeting = Greeting()
        val message = greeting.getGreetingMessage(calendar)
        assertEquals("Rise and shine, breakfast awaits!", message)
    }

    @Test
    fun testAfternoonGreeting() {
        val calendar = Mockito.mock(Calendar::class.java)
        Mockito.`when`(calendar.get(Calendar.HOUR_OF_DAY)).thenReturn(12)

        val greeting = Greeting()
        val message = greeting.getGreetingMessage(calendar)
        assertEquals("Fuel up for the afternoon!", message)
    }

    @Test
    fun testEveningGreeting() {
        val calendar = Mockito.mock(Calendar::class.java)
        Mockito.`when`(calendar.get(Calendar.HOUR_OF_DAY)).thenReturn(18)

        val greeting = Greeting()
        val message = greeting.getGreetingMessage(calendar)
        assertEquals("Perfect time for a tasty treat!", message)
    }

    @Test
    fun testMidnightGreeting() {
        val calendar = Mockito.mock(Calendar::class.java)
        Mockito.`when`(calendar.get(Calendar.HOUR_OF_DAY)).thenReturn(1)

        val greeting = Greeting()
        val message = greeting.getGreetingMessage(calendar)
        assertEquals("Late night cravings? We've all been there :)", message)
    }

    @Test
    fun testNightGreeting() {
        val calendar = Mockito.mock(Calendar::class.java)
        Mockito.`when`(calendar.get(Calendar.HOUR_OF_DAY)).thenReturn(20)

        val greeting = Greeting()
        val message = greeting.getGreetingMessage(calendar)
        assertEquals("Dinner's calling, what's cooking?", message)
    }
}