package com.example.factory

import com.example.MCPClient
import io.micronaut.context.annotation.Factory
import io.micronaut.context.event.StartupEvent
import io.micronaut.runtime.event.annotation.EventListener
import jakarta.inject.Singleton
import kotlinx.coroutines.runBlocking

@Factory
class MCPClientFactory {

    private val client = MCPClient()

    @EventListener
    fun onStartup(event: StartupEvent) {
        runBlocking {
            try {
                client.connectToServer()
            } catch (e: Exception) {
                println("Warning: Failed to connect to MCP server: ${e.message}")
            }
        }
    }

    @Singleton
    fun mcpClient(): MCPClient = client
}