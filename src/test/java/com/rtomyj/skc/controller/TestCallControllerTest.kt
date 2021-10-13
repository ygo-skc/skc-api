package com.rtomyj.skc.controller

import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(TestCallController::class)
class TestCallControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc


    @Nested
    inner class HappyPath {
        @Test
        fun `Calling Test Call Endpoint With Get Method - Success`() {
            mockMvc
                .perform(get("/testcall"))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.status", `is`("API is online and functional.")))
        }
    }
}