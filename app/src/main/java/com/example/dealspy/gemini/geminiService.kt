package com.example.dealspy.gemini

import com.google.ai.client.generativeai.GenerativeModel


object GeminiService {
    private const val API_KEY = "AIzaSyBnbyRp-YxfxkG0op_REeCUZ9WpJxzioUM"

    private val generativeModel = GenerativeModel(
        modelName = "gemini-pro",
        apiKey = API_KEY
    )

    suspend fun generateSearchSuggestions(productName: String): String {
        val prompt =
            "Find specific listings for individual ${productName} products on brand websites, " +
                    "Amazon India, Flipkart, Ajio, Myntra, Meesho, Tata CLiQ, Croma, " +
                    "Blinkit, BigBasket, JioMart, and Instamart. For each product found," +
                    " extract the product name (including any variants)," +
                    " platform name, price (in INR), the direct product deep link, " +
                    "and the image URL. Present the results in the following JSON format:" +
                    "```json\n" +
                    "{\n" +
                    "  \"product\": [\n" +
                    "    {\n" +
                    "      \"name\": \"Product Name (Variant)\",\n" +
                    "      \"platform name\": \"Platform Name\",\n" +
                    "      \"price\": \"Price in INR\",\n" +
                    "      \"deep link\": \"Product URL\",\n" +
                    "      \"image URL\": \"Image URL\"\n" +
                    "    },\n" +
                    "    ...\n" +
                    "  ]\n" +
                    "}"

        return try {
            val response = generativeModel.generateContent(prompt)
            response.text ?: "No results found."
        } catch (e: Exception) {
            "Error: ${e.localizedMessage}"
        }
    }
}
