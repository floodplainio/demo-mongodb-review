package io.floodplain.demo

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import io.quarkus.runtime.Startup
import javax.annotation.PostConstruct
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random
import kotlin.random.Random.Default.nextInt

@Singleton
@Startup
class DemoInserter {

    @Inject
    lateinit var reviewService: ReviewService

    @PostConstruct
    fun initialize() {
        val quotes = ObjectMapper().readTree(this::class.java.classLoader.getResource("quotes.json").readText()) as ArrayNode
        val quoteList = quotes.map {
            it.get("text").asText()
        }.toList()
        println("Size: ${quoteList.size}")
        repeat(Int.MAX_VALUE) {
                reviewService.addReviewToReviewer(nextInt(0, 100), Review(quoteList.get(nextInt(quoteList.size-1)), 2, 2.5))
                Thread.sleep(1000)
            }
    }
}