package com.example.recipeapp.util

import java.util.Calendar

class Greeting {
    public fun getGreetingMessage(): String {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)

        return when (hour) {
            in 4..11 -> "Rise and shine, breakfast awaits!"
            in 12..17 -> "Fuel up for the afternoon!"
            in 18..19 -> "Perfect time for a tasty treat!"
            in 0..3 -> "Late night cravings? We've all been there :)"
            else -> "Dinner's calling, what's cooking?"
        }
    }
}