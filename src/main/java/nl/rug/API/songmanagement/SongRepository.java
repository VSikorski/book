package nl.rug.API.songmanagement;

import org.springframework.data.repository.CrudRepository;

/**
 * Repository expanding standard Crud operations for the Song entity.
 **/
public interface SongRepository extends CrudRepository<Song, Integer> {
}
