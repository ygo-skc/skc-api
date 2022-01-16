package com.rtomyj.skc.controller

import com.rtomyj.skc.dao.Dao
import com.rtomyj.skc.model.DownstreamStatus
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(StatusController::class)
@ContextConfiguration(classes = [StatusController::class])
@Tag("Controller")
class StatusControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean(name = "jdbc")
    private lateinit var dao: Dao


    companion object {
        private const val DB_NAME = "SKC DB"
        private const val DB_VERSION = "8"
        private const val DB_STATUS = "up"
    }


    @Nested
    inner class HappyPath {
        @Test
        fun `Calling Test Call Endpoint With Get Method - Success`() {
            val downstream = DownstreamStatus(DB_NAME, DB_VERSION, DB_STATUS)
            Mockito
                .`when`(dao.dbConnection())
                .thenReturn(downstream)

            mockMvc
                .perform(get("/status"))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.status", `is`("API is online and functional.")))
                .andExpect(jsonPath("$.downstream[0].name", `is`(DB_NAME)))
                .andExpect(jsonPath("$.downstream[0].version", `is`(DB_VERSION)))
                .andExpect(jsonPath("$.downstream[0].status", `is`(DB_STATUS)))
        }
    }
}