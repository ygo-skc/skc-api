package com.rtomyj.skc.util.enumeration

import java.util.Locale

enum class LinkArrow(
    private val arrowEmoji: String,
    private val arrow: String,
) {
    NONE("", "None"),
    TOP_LEFT("↖️", "Top Left"),
    TOP_CENTER("⬆️", "Top Center"),
    TOP_RIGHT("↗️", "Top Right"),
    MIDDLE_RIGHT("➡️", "Middle Right"),
    BOTTOM_RIGHT("↘️", "Bottom Right"),
    BOTTOM_CENTER("⬇️", "Bottom Center"),
    BOTTOM_LEFT("↙️", "Bottom Left"),
    MIDDLE_LEFT("⬅️", "Middle Left"),
    ;

    override fun toString(): String = arrowEmoji

    companion object {
        private val dbStringToEnumMap =
            mapOf(
                "T-L" to TOP_LEFT,
                "T-C" to TOP_CENTER,
                "T-R" to TOP_RIGHT,
                "M-R" to MIDDLE_RIGHT,
                "B-R" to BOTTOM_RIGHT,
                "B-C" to BOTTOM_CENTER,
                "B-L" to BOTTOM_LEFT,
                "M-L" to MIDDLE_LEFT,
            )

        fun transformDBStringToEnum(dbString: String): LinkArrow = dbStringToEnumMap[dbString.uppercase(Locale.getDefault())] ?: NONE
    }
}
