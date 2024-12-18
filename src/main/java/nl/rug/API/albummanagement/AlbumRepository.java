package nl.rug.API.albummanagement;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * AlbumRepository interface provides CRUD operations for the Album entity.
 */
public interface AlbumRepository extends CrudRepository<Album, Integer> {
    List<Album> findByTitle(String title);

    List<Album> findByArtist(String artist);
}
