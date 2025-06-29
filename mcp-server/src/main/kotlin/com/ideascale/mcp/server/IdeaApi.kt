package com.ideascale.mcp.server

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.serialization.Serializable

suspend fun HttpClient.getIdeas(): List<String> {
    val uri = "a/rest/v1/ideas"
    val ideas = this.get(uri).body<List<Idea>>()
    return ideas.map { idea ->
        """
            title: ${idea.title}
            text: ${idea.text}
        """.trimIndent()
    }
}

@Serializable
data class Idea(
    val title: String,
    val text: String,
)

