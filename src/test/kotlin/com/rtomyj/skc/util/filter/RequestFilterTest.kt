package com.rtomyj.skc.util.filter

import com.google.common.net.HttpHeaders.USER_AGENT
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito
import org.slf4j.MDC
import org.springframework.http.HttpHeaders
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.net.URI
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@ExtendWith(SpringExtension::class)
class RequestFilterTest {
    @Mock
    private lateinit var request: ServerHttpRequest

    companion object {
        private const val PATH = "/path"
        private const val QUERY_STRING = "x=y"
        private const val CLIENT_ID = "JUNIT"
        private const val AGENT = "POSTMAN"
        private val HEADERS = HttpHeaders()

        @BeforeAll
        @JvmStatic
        fun beforeAll() {
            HEADERS.add("X-Forwarded-For", "1.1.1.1")
            HEADERS.add("CLIENT_ID", CLIENT_ID)
            HEADERS.add(USER_AGENT, AGENT)
        }
    }

    @Test
    fun `Test MDC Configuration - Query String Not Null`() {
        // mock
        Mockito.`when`(request.uri).thenReturn(URI("http", "", PATH, QUERY_STRING, ""))
        Mockito.`when`(request.headers).thenReturn(HEADERS)

        // call
        RequestFilter.configureMDC(request)

        // verify
        assertNotNull(MDC.get("reqPath"))
        assertNotNull(MDC.get("reqUUID"))
        assertNotNull(MDC.get("clientID"))
        assertNotNull(MDC.get("userAgent"))

        assertEquals("$PATH?$QUERY_STRING", MDC.get("reqPath"))
        assertEquals(CLIENT_ID, MDC.get("clientID"))
        assertEquals(AGENT, MDC.get("userAgent"))
    }

    @Test
    fun `Test MDC Configuration - Query String Is Null`() {
        // mock
        Mockito.`when`(request.uri).thenReturn(URI("http", "", PATH, "", ""))
        Mockito.`when`(request.headers).thenReturn(HEADERS)

        // call
        RequestFilter.configureMDC(request)

        // verify
        assertNotNull(MDC.get("reqPath"))
        assertNotNull(MDC.get("reqUUID"))
        assertNotNull(MDC.get("clientID"))
        assertNotNull(MDC.get("userAgent"))

        assertEquals(PATH, MDC.get("reqPath"))
        assertEquals(CLIENT_ID, MDC.get("clientID"))
        assertEquals(AGENT, MDC.get("userAgent"))
    }
}