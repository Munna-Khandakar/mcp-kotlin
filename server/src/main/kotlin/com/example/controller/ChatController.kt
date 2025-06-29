package com.example.controller

import com.example.MCPClient
import com.example.model.QueryRequest
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Produces
import jakarta.inject.Inject
import kotlinx.coroutines.runBlocking

@Controller("/mcp/chat")
class ChatController(@Inject private val mcpClient: MCPClient) {

    @Get()
    @Produces(MediaType.APPLICATION_JSON)
    fun getChatInfo(): Map<String, String> {
        return mapOf("info" to "This is the chat endpoint for MCP client.")
    }

    @Post()
    @Produces(MediaType.APPLICATION_JSON)
    fun handleChatRequest(@Body body: QueryRequest): Map<String, String> {
        val response = runBlocking {
            mcpClient.processQuery(body.query)
        }
        return mapOf("text" to response)
    }
}
