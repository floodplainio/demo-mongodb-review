package io.floodplain.demo

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Updates
import org.bson.Document
import java.util.UUID
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject
import org.jboss.logging.Logger

private val logger = Logger.getLogger(ReviewService::class.java)

@ApplicationScoped
class ReviewService {
    @Inject
    lateinit var mongoClient: MongoClient

    private fun reviewToDocument(review: Review) =
        Document()
            .append("id",UUID.randomUUID().toString())
            .append("summary",review.summary)
            .append("movie_id",review.movie_id)
            .append("score",review.score)

    fun addReviewToReviewer(person_id: Int, review: Review): Document? {
        val existing = getReviewersCollection().find(Document("personid",person_id)).first()
        if(existing==null) {
            getReviewersCollection().insertOne(Document("personid",person_id).append("reviews", mutableListOf<Document>()))
        }
        logger.info("Inserting review for person: $person_id")
        return getReviewersCollection().findOneAndUpdate(Document("personid",person_id), Updates.push("reviews",reviewToDocument(review)))
    }

    fun getReviewersCollection(): MongoCollection<Document> {
        return mongoClient.getDatabase("review_database").getCollection("reviewers")
    }
}