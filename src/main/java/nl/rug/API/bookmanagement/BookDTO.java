package nl.rug.API.bookmanagement;

/**
 * DTO for Books.
 * @param name          asd
 * @param author        asd
 * @param publishedYear asd
 * @param isbn          asd
 * @param genre         asd
 * @param description   asd
 */
public record BookDTO(String name,
                      String author,
                      int publishedYear,
                      String isbn,
                      String genre,
                      String description) {
}
