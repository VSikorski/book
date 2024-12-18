package nl.rug.API.reviewmanagement;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import nl.rug.API.bookmanagement.Book;
import nl.rug.API.bookmanagement.BookRepository;
import nl.rug.API.songmanagement.Song;
import nl.rug.API.songmanagement.SongRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.StreamSupport;

/**
 * Service class for handling CRUD operations for reviews.
 */
@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final BookRepository bookRepository;
    private final SongRepository songRepository;

    /**
     * Retrieves all reviews from the repository.
     *
     * @return a list of all reviews
     */
    public List<Review> getAllReviews() {
        return StreamSupport.stream(reviewRepository.findAll().spliterator(), false).toList();
    }

    /**
     * Retrieves a review by its ID.
     *
     * @param id the ID of the review to retrieve
     * @return the requested review
     * @throws EntityNotFoundException if no review with the provided ID is found
     */
    public Review getReviewById(int id) {
        return reviewRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Error: Review with id " + id + " not found"));
    }

    /**
     * Adds a new review to the repository and updates the associated media (book or song) with the new review.
     *
     * @param review the review to add
     * @return the added review
     * @throws EntityNotFoundException if the associated media (book or song) is not found
     */
    @Transactional
    public Review addReview(Review review) {
        reviewRepository.save(review);

        switch (review.getReviewType()) {
            case BOOK -> {
                Book book = bookRepository.findById(review.getMediaId()).orElseThrow(
                        () -> new EntityNotFoundException("Error: Book with id " + review.getMediaId() + " not found"));
                book.getReviews().add(review);
                bookRepository.save(book);
            }
            case SONG -> {
                Song song = songRepository.findById(review.getMediaId()).orElseThrow(
                        () -> new EntityNotFoundException("Error: Song with id " + review.getMediaId() + " not found"));
                song.getReviews().add(review);
                songRepository.save(song);
            }
            default -> throw new IllegalStateException("Unexpected value: " + review.getReviewType());
        }

        return review;
    }

    /**
     * Updates an existing review with new details provided in the review object.
     *
     * @param id     the ID of the review to update
     * @param review the updated review details
     * @return the updated review
     * @throws EntityNotFoundException if no review with the provided ID is found
     */
    @Transactional
    public Review updateReview(int id, Review review) {
        Review existingReview = reviewRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Review with id " + id + " not found"));
        updateReviewInfo(existingReview, review);
        return reviewRepository.save(existingReview);
    }

    /**
     * Helper method to update the fields of an existing review with fields from another review.
     *
     * @param existingReview the existing review to be updated
     * @param review         the review containing the new data
     */
    private void updateReviewInfo(Review existingReview, Review review) {
        if (review.getTitle() != null) {
            existingReview.setTitle(review.getTitle());
        }
        if (review.getAuthor() != null) {
            existingReview.setAuthor(review.getAuthor());
        }
        if (review.getReviewFullText() != null) {
            existingReview.setReviewFullText(review.getReviewFullText());
        }
        if (review.getReviewDescription() != null) {
            existingReview.setReviewDescription(review.getReviewDescription());
        }
        if (review.getAffiliation() != null) {
            existingReview.setAffiliation(review.getAffiliation());
        }
        if (review.getReviewRating() != 0) {
            existingReview.setReviewRating(review.getReviewRating());
        }

        if (review.getReviewType() != null) {
            existingReview.setReviewType(review.getReviewType());
        }
    }

    /**
     * Deletes a review by its ID.
     *
     * @param id the ID of the review to delete
     */
    public void deleteReview(int id) {
        if (!reviewRepository.existsById(id)) {
            throw new EntityNotFoundException("Error: Review with id " + id + " not found");
        }
        reviewRepository.deleteById(id);
    }
}
