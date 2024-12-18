package nl.rug.API.songmanagement;

import java.sql.Timestamp;

/**
 * Data Transfer Object for Songs.
 * @param name - the name of the song
 * @param artist - the name of the artist
 * @param releasedOn - the date it was released on
 * @param duration - the duration of the songs in seconds
 * @param genre - the genre of the song
 * @param description - the description of the song
 */
public record SongDTO(String name,
                      String artist,
                      Timestamp releasedOn,
                      int duration,
                      String genre,
                      String description) {
}
