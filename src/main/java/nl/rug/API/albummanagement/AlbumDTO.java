package nl.rug.API.albummanagement;

import java.time.LocalDate;

/**
 * DTO for Albums.
 * @param title title of the album
 * @param artist artist of the album
 * @param releaseDate release date of the album
 */
public record AlbumDTO(String title, String artist, LocalDate releaseDate) { }
