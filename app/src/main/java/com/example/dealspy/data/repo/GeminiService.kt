package com.example.dealspy.data.repo

import com.example.dealspy.BuildConfig
import com.example.dealspy.data.model.GeminiSearchResponse
import com.example.dealspy.data.model.Product
import com.google.ai.client.generativeai.GenerativeModel
import com.google.gson.Gson
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GeminiService @Inject constructor() {


    private val apiKey = BuildConfig.API_KEY
    private val generativeModel = GenerativeModel(
        modelName = "gemini-2.5-flash",
        apiKey = apiKey
    )

    suspend fun generateSearchSuggestions(productName: String): List<Product> {
        val prompt = """
            ROLE: You are a strict product data extractor for Indian e‑commerce. You must return only valid JSON that parses, with real PDP links and real primary images.

TASK:

Find 5–6 real, in‑stock products for the user query: “{productName}” from these marketplaces only:
amazon.in, flipkart.com, myntra.com, ajio.com, croma.com, reliancedigital.in, nykaa.com, jiomart.com.

For each product, open the actual product detail page (PDP), not a listing/search page.

Extract the PDP’s canonical URL (rel=canonical or the site’s standard PDP URL) and the primary product image (prefer meta og:image or JSON‑LD image).

Normalize both links:

HTTPS only.

Strip tracking/referrer params (utm_, aff, ref*, tag*, pid*, psc*, etc.) unless required for the PDP to load.

De‑duplicate by URL; return unique products only.

Capture the visible price shown on the PDP, prefixed with ₹.

STRICT OUTPUT:
Return ONLY this JSON shape (no markdown, no comments, no extra text):

{
"products": [
{
"name": "Exact Product Name from PDP",
"platform": "Amazon | Flipkart | Myntra | Ajio | Croma | Reliance Digital | Nykaa | JioMart",
"price": "₹12,999",
"deepLink": "https://…(canonical PDP URL)…",
"imageUrl": "https://…(og:image or JSON-LD image)…",
"discount": "20% off"
}
]
}

RULES FOR deeplink:

Host must be one of the allowed domains above.

Must be a PDP pattern (not search/listing). Examples/regex hints:

Amazon: https://www.amazon.in/(gp/product|dp)/[A-Z0-9]{10}(/|$)

Flipkart: https://www.flipkart.com/.+/p/itm[\\w]+(/|\\?)

Myntra: https://www.myntra.com/.+/\\d+(/|$)

Ajio: https://www.ajio.com/.+/p/\\w+(/|$)

Croma: https://www.croma.com/.+/p/\\w+(/|$)

Reliance Digital: https://www.reliancedigital.in/.+/p/\\w+(/|$)

Nykaa: https://www.nykaa.com/.+/p/.+(/|$)

JioMart: https://www.jiomart.com/.+/p/\\w+(/|$)

RULES FOR imageUrl:

HTTPS only; absolute URL.

Prefer og:image or JSON‑LD image from the PDP.

Use a proper image asset (jpg/jpeg/png/webp) or an official CDN path the site uses.

Prefer ≥600px shortest side when multiple sizes exist.

RULES FOR price and discount:

price must include the ₹ symbol and match the PDP’s main selling price.

discount is optional but, if present, reflect the visible savings text (e.g., “20% off” or “₹500 off”).

VALIDATION BEFORE RETURN:

Ensure every product’s deepLink host is allowed and matches a PDP pattern.

Ensure imageUrl is HTTPS and points to an image/CDN used by that retailer.

Ensure unique deepLink values; remove duplicates.

If fewer than 5 products pass all checks, return only the valid ones (do not fabricate any field).

CONSTRAINTS:

No markdown.

No prose or explanations.

No trailing commas.

Output must be valid JSON exactly matching the schema.

QUERY: $productName

INTERNAL PROCESS (do not print):

Find candidates, visit PDPs, extract canonical URL + og:image/JSON‑LD image, normalize URLs, validate against patterns, and only then emit the JSON.

OUTPUT:

Emit the JSON object only.
        """.trimIndent()

        return try {
            val response = generativeModel.generateContent(prompt)
            val jsonString = response.text ?: return emptyList()

            // Clean the response to extract only JSON
            val cleanJson = extractJsonFromResponse(jsonString)

            // Parse JSON into GeminiSearchResponse
            val result = Gson().fromJson(cleanJson, GeminiSearchResponse::class.java)
            result.products

        } catch (e: Exception) {
            // Log the error for debugging
            println("Gemini API Error: ${e.message}")
            emptyList()
        }
    }

    suspend fun generatePriceComparison(productName: String): List<Product> {
        val prompt = """
        ROLE: You are a strict price-comparison extractor for Indian e‑commerce. Output only valid JSON that parses. Use real product detail pages (PDP), canonical URLs, and primary images.

TASK:

For the exact product: “{productName}”, find the SAME model/variant across these marketplaces only:
amazon.in, flipkart.com, myntra.com, ajio.com, croma.com, reliancedigital.in, tatacliq.com, jiomart.com.

Open each product’s PDP (not search/listing), extract:

Canonical PDP URL (rel=canonical or the site’s standard PDP URL)

Primary image (prefer meta og:image or JSON‑LD image)

Visible selling price (with ₹)

Any visible discount text (optional)

Normalize links:

HTTPS only

Strip tracking params (utm_, aff, ref*, tag*, pid*, psc*, etc.) unless needed for the PDP to load

Ensure unique products by URL

STRICT matching for the SAME variant:

Build an internal match key from brand + model + capacity/storage/size + color + edition (as visible on PDP)

Keep only entries whose match key equals the primary reference item

If any site sells a slightly different variant (different capacity/color/edition), exclude it

After collecting valid entries, sort by numeric price ascending and emit the JSON.

STRICT OUTPUT (JSON only; no markdown; no comments):

{
"products": [
{
"name": "Exact Product Name from PDP (include brand + variant)",
"platform": "Amazon | Flipkart | Myntra | Ajio | Croma | Reliance Digital | Tata CLiQ | JioMart",
"price": "₹12,999",
"deepLink": "https://…(canonical PDP URL)…",
"imageUrl": "https://…(og:image or JSON-LD image)…",
"discount": "25% off"
}
]
}

DEEPLINK RULES:

Host must be one of the allowed domains above

Must be a PDP pattern (not search/listing). Regex hints:

Amazon: ^https://(www\.)?amazon\.in/(gp/product|dp)/[A-Z0-9]{10}(/|$)

Flipkart: ^https://(www\.)?flipkart\.com/.+/p/itm[\w]+(/|\?)

Myntra: ^https://(www\.)?myntra\.com/.+/\d+(/|$)

Ajio: ^https://(www\.)?ajio\.com/.+/p/\w+(/|$)

Croma: ^https://(www\.)?croma\.com/.+/p/\w+(/|$)

Reliance Digital: ^https://(www\.)?reliancedigital\.in/.+/p/\w+(/|$)

Tata CLiQ: ^https://(www\.)?tatacliq\.com/.+/(p|product)/[\w-]+(/|\?|$)

JioMart: ^https://(www\.)?jiomart\.com/.+/p/\w+(/|$)

IMAGE RULES:

HTTPS absolute URL

Prefer og:image or JSON‑LD image from the PDP

Use a real image asset (jpg/jpeg/png/webp) or an official retailer CDN path

Choose ≥ 600px shortest side when multiple sizes are present

PRICE/DISCOUNT RULES:

price must include the ₹ symbol and match the PDP’s visible selling price

discount is optional; if present, mirror the PDP’s text (e.g., “20% off” or “₹500 off”)

VALIDATION BEFORE RETURN:

Keep 4–8 items if available; if fewer exact matches exist, return only valid ones—do not fabricate

Ensure deepLink host is allowed and looks like a PDP URL (use hints above)

Ensure imageUrl is HTTPS and a real image/CDN path

Ensure all deepLinks are unique

Sort by numeric price ascending before emitting JSON

Output must be valid JSON exactly matching the schema (no trailing commas, no extra keys)

CONSTRAINTS:

Output JSON only (no markdown, no prose, no comments)

Do not include sources array or any debug text

QUERY: $productName

INTERNAL PROCESS (do not print):

Find candidates on allowed domains, open their PDPs, extract canonical URL/og:image/price

Create a strict match key from brand + model + capacity + color + edition; filter to exact matches

Normalize/clean URLs, validate patterns, sort by price, and emit the JSON

OUTPUT:

Emit only the JSON object described above.
    """.trimIndent()

        return try {
            val response = generativeModel.generateContent(prompt)
            val jsonString = response.text ?: return emptyList()

            val cleanJson = extractJsonFromResponse(jsonString)
            val result = Gson().fromJson(cleanJson, GeminiSearchResponse::class.java)


            result.products.sortedBy { it.price }

        } catch (e: Exception) {
            println("Gemini Price Comparison Error: ${e.message}")
            emptyList()
        }
    }


    private fun extractJsonFromResponse(response: String): String {

        return response

            .replace("", "")
            .trim()
            .let { cleanText ->

                val startIndex = cleanText.indexOf("{")
                val endIndex = cleanText.lastIndexOf("}") + 1
                if (startIndex >= 0 && endIndex > startIndex) {
                    cleanText.substring(startIndex, endIndex)
                } else {
                    cleanText
                }
            }
    }
}
