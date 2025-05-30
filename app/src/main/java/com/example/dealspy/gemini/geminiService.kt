package com.example.dealspy.gemini

import com.google.ai.client.generativeai.GenerativeModel


object GeminiService {
    private const val API_KEY = "AIzaSyBnbyRp-YxfxkG0op_REeCUZ9WpJxzioUM"

    private val generativeModel = GenerativeModel(
        modelName = "gemini-pro",
        apiKey = API_KEY
    )

    suspend fun generateSearchSuggestions(query: String): String {
        val prompt = "Give me product suggestions for \"$query\" from Indian e-commerce platforms with prices."

        return try {
            val response = generativeModel.generateContent(prompt)
            response.text ?: "No results found."
        } catch (e: Exception) {
            "Error: ${e.localizedMessage}"
        }
    }
}
