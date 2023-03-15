package com.example.tp1

object ImageRessources {
    private val imageMap = mapOf(
    "Linus Torvalds" to R.drawable.linux,
    "Richard Stallman" to R.drawable.stallman,
    "John McCarthy" to R.drawable.mccarthy,
    "Andy Gavin" to R.drawable.gavin,
    "Brian Kernighan" to R.drawable.kernighan,
    "Dennis Ritchie" to R.drawable.ritchie,
    "Ken Thompson" to R.drawable.thompson,
    "Steve Wozniak" to R.drawable.wozniak,
    "Bjarne Stroustrup" to R.drawable.stroustrup,
    "Tim Sweeney" to R.drawable.sweeney,
    "Tim Berners-Lee" to R.drawable.lee,
    "John von Neumann" to R.drawable.neumann,
    "Unknown" to R.drawable.unknown,
    "SadFace" to R.drawable.sad
    )

    fun getImageResource(name: String): Int {
        return imageMap[name] ?: throw IllegalArgumentException("Invalid pionnier name: $name")
    }
}