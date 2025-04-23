package com.TravelPlanner.utils

import java.util.regex.Pattern

class LoginUtils {
    fun isValidEmailAddress(email: String): Boolean {
        val mailPattern = "^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,}$"
        return email.matches(mailPattern.toRegex())
    }

    fun isValidPassword(password: String): Boolean {
        val passwordPattern =
            "^(?=.*[0-9])(?=.*[A-Z])(?=.*[!@#\$%^&*+=])(?=\\S+\$).{8,}$"
        return Pattern.compile(passwordPattern).matcher(password).matches()
    }
}
