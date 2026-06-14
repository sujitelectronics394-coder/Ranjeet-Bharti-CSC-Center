package com.example.data

import com.example.BuildConfig
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

@JsonClass(generateAdapter = true)
data class GeminiRequest(
    val contents: List<GeminiContent>,
    val systemInstruction: GeminiInstruction? = null
)

@JsonClass(generateAdapter = true)
data class GeminiContent(
    val parts: List<GeminiPart>
)

@JsonClass(generateAdapter = true)
data class GeminiPart(
    val text: String
)

@JsonClass(generateAdapter = true)
data class GeminiInstruction(
    val parts: List<GeminiPart>
)

@JsonClass(generateAdapter = true)
data class GeminiResponse(
    val candidates: List<GeminiCandidate>?
)

@JsonClass(generateAdapter = true)
data class GeminiCandidate(
    val content: GeminiContent?
)

interface GeminiApiService {
    @POST("v1beta/models/gemini-3.5-flash:generateContent")
    suspend fun generateContent(
        @Query("key") apiKey: String,
        @Body request: GeminiRequest
    ): GeminiResponse
}

object GeminiClient {
    private const val BASE_URL = "https://generativelanguage.googleapis.com/"

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    val service: GeminiApiService by lazy {
        retrofit.create(GeminiApiService::class.java)
    }

    /**
     * Helper to generate updates or responses on behalf of CSC Operator Ranjeet Bharti.
     * Includes language parameters so notifications can feel warm, local, and professional.
     */
    suspend fun generateStatusNotification(
        customerName: String,
        serviceType: String,
        status: String,
        notes: String,
        refNumber: String
    ): String {
        val apiKey = try {
            BuildConfig.GEMINI_API_KEY
        } catch (e: Exception) {
            ""
        }

        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
            return getFallbackNotification(customerName, serviceType, status, notes, refNumber)
        }

        val prompt = """
            Draft a highly professional, polite WhatsApp & SMS notification message from CSC operator 'Ranjeet Bharti' of 'Ranjeet CSC Digital Cafe', Pakariyar naugawa, Kushinagar, Uttar Pradesh.
            
            Customer: $customerName
            Service Applied: $serviceType
            New Status: $status
            Operator Remarks: ${notes.ifEmpty { "Verified and forwarded." }}
            Reference/Ack No: ${refNumber.ifEmpty { "Under Process" }}
            
            Format requirements:
            1. Use bold formatting like *Ranjeet CSC Cafe* for WhatsApp.
            2. Keep it warm. Include a greeting and contact details at the end (Ranjeet Bharti: 9169004836, Pakariyar naugawa, Kushinagar).
            3. Write in bilingual Hinglish/Hindi & English layout (first 3 lines in clear Hinglish so it's super friendly to local customers in Uttar Pradesh, followed by a professional English translation).
            4. Make it modern with professional emoji accents. Keep the length and format concise.
        """.trimIndent()

        val systemPrompt = "You are a specialized business assistant with expertise in customer notification drafting for common services centers (CSC) in India."

        val request = GeminiRequest(
            contents = listOf(GeminiContent(parts = listOf(GeminiPart(text = prompt)))),
            systemInstruction = GeminiInstruction(parts = listOf(GeminiPart(text = systemPrompt)))
        )

        return try {
            val response = service.generateContent(apiKey, request)
            response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text 
                ?: getFallbackNotification(customerName, serviceType, status, notes, refNumber)
        } catch (e: Exception) {
            e.printStackTrace()
            getFallbackNotification(customerName, serviceType, status, notes, refNumber)
        }
    }

    fun getFallbackNotification(
        customerName: String,
        serviceType: String,
        status: String,
        notes: String,
        refNumber: String
    ): String {
        val statusSymbol = when (status) {
            "Pending" -> "⏳"
            "Under Review" -> "🔍"
            "Uploaded to Portal" -> "🌐"
            "Action Required" -> "⚠️"
            "Completed" -> "✅"
            else -> "📢"
        }
        val notesStr = if (notes.isNotEmpty()) "\n📋 *Remarks:* $notes" else ""
        val refStr = if (refNumber.isNotEmpty()) "\n🎫 *Receipt/Ack No:* $refNumber" else ""

        return """
🤝 *Ranjeet CSC Cafe Update* 🤝

Namaskar *$customerName* ji, aapke *$serviceType* application ki status update hui hai:

📌 *Status:* $statusSymbol *$status*
$refStr$notesStr

Aapki application par kaam chal raha hai. Kisi bhi jaankari ke liye contact karein.

---
English Translation:
Hello $customerName, your application for $serviceType has been updated to: $status.

Regards,
*Ranjeet Bharti* (CSC Operator)
📞 *9169004836*
📧 ranjeetbharti@zohomail.com
📍 Pakariyar naugawa, post dandopur, kushinagar, UP.
        """.trimIndent()
    }
}
