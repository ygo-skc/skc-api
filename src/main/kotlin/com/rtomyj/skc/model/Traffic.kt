package com.rtomyj.skc.model

data class Traffic(
    val ip: String,
    val source: Source,
    val resourceUtilized: ResourceUtilized
)