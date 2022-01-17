package com.rtomyj.skc.exception

import lombok.*

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
class YgoError(val message: String, val code: String)