package io.floodplain.demo

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import io.quarkus.runtime.Startup
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.jboss.logging.Logger
import javax.annotation.PostConstruct
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random.Default.nextDouble
import kotlin.random.Random.Default.nextInt

private val logger = Logger.getLogger(ReviewService::class.java)

@Singleton
@Startup
class DemoInserter {

    @Inject
    lateinit var reviewService: ReviewService

    @OptIn(DelicateCoroutinesApi::class)
    @PostConstruct
    fun initialize() {
        val quotes = ObjectMapper().readTree(this::class.java.classLoader.getResource("quotes.json")?.readText()) as ArrayNode
        val quoteList = quotes.map {
            it.get("text").asText()
        }.toList()
        val job = GlobalScope.launch {
            while (isActive) {
                reviewService.addReviewToReviewer(nextInt(0, 100), Review(quoteList.get(nextInt(quoteList.size-1)), nextInt(1, 1000), nextDouble(0.0,10.0)))
                delay(1000)
            }
        }
    //     Runtime.getRuntime().addShutdownHook(Thread {
    //         logger.info("Shutdown called")
    //         job.cancel()
    //     });
    }
}