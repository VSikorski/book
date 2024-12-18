package nl.rug.API.reviewmanagement;

/**
 * Data Transfer Object for Reviews.
 *
 * @param reviewType        The type of the review (e.g., BOOK, SONG).
 * @param title             The title of the review.
 * @param author            The author of the review.
 * @param reviewFullText    The full text of the review.
 * @param reviewDescription A brief description or summary of the review.
 * @param affiliation       The affiliation of the review author, if applicable.
 * @param mediaId           The ID of the media (book or song) being reviewed.
 * @param mediaTitle        The title of the media being reviewed.
 * @param reviewRating      The numerical rating given in the review.
 */
public record ReviewDTO(ReviewType reviewType,
                        String title,
                        String author,
                        String reviewFullText,
                        String reviewDescription,
                        String affiliation,
                        Integer mediaId,
                        String mediaTitle,
                        Integer reviewRating) {
}
