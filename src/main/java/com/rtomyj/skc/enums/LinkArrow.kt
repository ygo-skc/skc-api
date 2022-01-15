package com.rtomyj.skc.enums

import java.util.*
import kotlin.collections.HashMap

enum class LinkArrow(
    private val arrowEmoji: String, private val arrow: String
) {
    NONE("", "None"),
    TOP_LEFT("↖️", "Top Left"),
    TOP_CENTER("⬆️", "Top Center"),
    TOP_RIGHT("↗️", "Top Right"),
    MIDDLE_RIGHT("➡️", "Middle Right"),
    BOTTOM_RIGHT("↘️", "Bottom Right"),
    BOTTOM_CENTER("⬇️", "Bottom Center"),
    BOTTOM_LEFT("↙️", "Bottom Left"),
    MIDDLE_LEFT("⬅️", "Middle Left");


    override fun toString(): String = arrowEmoji


    companion object {
        private val dbStringToEnumMap: MutableMap<String, LinkArrow> = HashMap()

        init {
            dbStringToEnumMap["T-L"] = TOP_LEFT
            dbStringToEnumMap["T-C"] = TOP_CENTER
            dbStringToEnumMap["T-R"] = TOP_RIGHT
            dbStringToEnumMap["M-R"] = MIDDLE_RIGHT
            dbStringToEnumMap["B-R"] = BOTTOM_RIGHT
            dbStringToEnumMap["B-C"] = BOTTOM_CENTER
            dbStringToEnumMap["B-L"] = BOTTOM_LEFT
            dbStringToEnumMap["M-L"] = MIDDLE_LEFT
        }


        @JvmStatic
        fun transformDBStringToEnum(dbString: String): LinkArrow = dbStringToEnumMap[dbString.uppercase(Locale.getDefault())] ?: NONE
    }
}