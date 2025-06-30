package com.ideascale.mcp.server

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.header
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.utils.io.streams.*
import io.modelcontextprotocol.kotlin.sdk.*
import io.modelcontextprotocol.kotlin.sdk.server.Server
import io.modelcontextprotocol.kotlin.sdk.server.ServerOptions
import io.modelcontextprotocol.kotlin.sdk.server.StdioServerTransport
import kotlinx.coroutines.Job
import kotlinx.coroutines.runBlocking
import kotlinx.io.asSink
import kotlinx.io.buffered
import kotlinx.serialization.json.*

fun runMcpServer() {
    val baseUrl = "https://w1.ideascale.me/"

    val httpClient = HttpClient(CIO) {
        defaultRequest {
            url(baseUrl)
            header("api_token", "81aec19d-d77e-4dd4-aad1-87573e9a8845")
            contentType(ContentType.Application.Json)
        }

        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
            })
        }
    }

    val server = Server(
        Implementation(
            name = "ideascale-mcp-server",
            version = "1.0.0"
        ),
        ServerOptions(
            capabilities = ServerCapabilities(
                tools = ServerCapabilities.Tools(listChanged = true),
                resources = ServerCapabilities.Resources(subscribe = true, listChanged = true)
            )
        )
    )

    server.addTool(
        name = "get_ideas",
        description = """Get all the submitted ideas from the Ideascale platform.""".trimIndent(),
    ) { request ->
        val alerts = httpClient.getIdeas()
        CallToolResult(content = alerts.map { TextContent(it) })
    }

    server.addResource(
        uri = "https://help.ideascale.com/submit-idea",
        name = "submit_new_idea",
        description = "How to submit an idea on the Ideascale platform.",
        mimeType = "text/html"
    ) { request ->
        ReadResourceResult(
            contents = listOf(
                TextResourceContents("Placeholder content for ${request.uri}", request.uri, "text/html")
            )
        )
    }

    val transport = StdioServerTransport(
        System.`in`.asInput(),
        System.out.asSink().buffered()
    )

    runBlocking {
        server.connect(transport)
        val done = Job()
        server.onClose {
            done.complete()
        }
        done.join()
    }
}
