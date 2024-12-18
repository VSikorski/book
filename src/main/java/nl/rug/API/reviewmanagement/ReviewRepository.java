package nl.rug.API.reviewmanagement;

import org.springframework.data.repository.CrudRepository;

/**
 * Repository interface for {@link Review} entities.
 * This interface extends the {@link CrudRepository} provided by Spring Data,
 * enabling standard CRUD operations on the Review entity.
 **/
public interface ReviewRepository extends CrudRepository<Review, Integer> {
}
