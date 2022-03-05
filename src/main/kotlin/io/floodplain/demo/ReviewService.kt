package io.floodplain.demo

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoCollection
import com.mongodb.client.model.ReplaceOptions
import com.mongodb.client.model.UpdateOptions
import org.bson.Document
import java.util.UUID
import javax.annotation.PostConstruct
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

@ApplicationScoped
class ReviewService {
    @Inject
    lateinit var mongoClient: MongoClient

    private fun reviewToDocument(review: Review) =
        Document()
            .append("id",UUID.randomUUID().toString())
            .append("subject",review.subject)
            .append("movie_id",review.movie_id)
            .append("score",review.score)

    fun addReviewToReviewer(person_id: Int, review: Review) {

        val reviewer = getReviewersCollection().find(Document("personid",person_id)).first()
            ?:Document("personid",person_id).append("reviews", mutableListOf<Document>())
        val reviewList = reviewer["reviews"] as MutableList<Document>
        reviewList.add(reviewToDocument(review))
        val res = getReviewersCollection().replaceOne(Document("personid",person_id),reviewer, ReplaceOptions().upsert(true))
    }

    fun getReviewersCollection(): MongoCollection<Document> {
        return mongoClient.getDatabase("review_database").getCollection("reviewers")
    }
}