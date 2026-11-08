package com.rtomyj.skc.service

import com.rtomyj.skc.dao.StatusDao
import com.rtomyj.skc.model.DownstreamStatus
import com.rtomyj.skc.util.Blocking
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class DatabaseStatusService
    @Autowired
    constructor(
        @param:Qualifier("jdbc") private val dao: StatusDao,
    ) {
        fun getStatus(): Mono<DownstreamStatus> = Blocking.mono(dao::dbConnection)
    }
