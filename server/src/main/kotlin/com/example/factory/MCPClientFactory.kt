package com.example.factory

import com.example.MCPClient
import io.micronaut.context.annotation.Factory
import jakarta.inject.Singleton
import kotlinx.coroutines.runBlocking

@Factory
class MCPClientFactory {

    @Singleton
    fun mcpClient(): MCPClient {
        val client = MCPClient()

        runBlocking {
            try {
                println("Connecting to MCP server...")
                client.connectToServer()
            } catch (e: Exception) {
                println("Warning: Failed to connect to MCP server: ${e.message}")
            }
        }
        return client
    }
}
