package nl.rug.API.reviewmanagement;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for handling review-related HTTP requests.
 * Supports operations to retrieve, add, update, and delete reviews.
 */
@RestController
@CrossOrigin
@RequestMapping("/reviews")
@Validated
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    /**
     * Retrieves all reviews.
     *
     * @return A ResponseEntity containing a list of reviews and the HTTP status.
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<Review>> getAllReviews() {
        List<Review> reviews = reviewService.getAllReviews();
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    /**
     * Retrieves a single review by its ID.
     *
     * @param id The ID of the review to retrieve.
     * @return The requested review.
     * @throws if no review is found with the specified ID.
     */
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Review getReviewById(@PathVariable int id) {
        return reviewService.getReviewById(id);
    }

    /**
     * Adds a new review based on the provided ReviewDTO.
     *
     * @param reviewDTO The ReviewDTO object containing review data.
     * @return The newly created review.
     * @throws if any validation constraints are violated.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Review addReview(@Valid @RequestBody ReviewDTO reviewDTO) {
        Review review = new Review(reviewDTO.reviewType(), reviewDTO.title(), reviewDTO.author(), reviewDTO.reviewFullText(), reviewDTO.reviewDescription(), reviewDTO.affiliation(), reviewDTO.reviewRating(), reviewDTO.mediaId(), reviewDTO.mediaTitle());

        return reviewService.addReview(review);
    }

    /**
     * Updates an existing review with the specified ID using data provided in the ReviewDTO.
     *
     * @param id        The ID of the review to update.
     * @param reviewDTO The ReviewDTO object containing updated review data.
     * @return The updated review.
     * @throws if no review is found with the specified ID.
     */
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Review updateReview(@PathVariable int id, @RequestBody ReviewDTO reviewDTO) {
        Review review = new Review(reviewDTO.reviewType(), reviewDTO.title(), reviewDTO.author(), reviewDTO.reviewFullText(), reviewDTO.reviewDescription(), reviewDTO.affiliation(), reviewDTO.reviewRating(), reviewDTO.mediaId(), reviewDTO.mediaTitle());

        return reviewService.updateReview(id, review);
    }

    /**
     * Deletes a review by its ID.
     *
     * @param id The ID of the review to delete.
     * @throws if no review is found with the specified ID.
     */
    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReview(@PathVariable int id) {
        reviewService.deleteReview(id);
    }
}
