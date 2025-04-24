package com.example.model

data class PronunciationSpeed(
    var speedType: SpeedType = SpeedType.NORMAL,
    var isSelected: Boolean = false,
)

enum class SpeedType(val typeName: String) {
    NORMAL("Normal"),
    SLOW("Slow"),
    SLOWER("Slower"),
}
