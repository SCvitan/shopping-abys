package hr.abysalto.hiring.mid.repository;

import hr.abysalto.hiring.mid.model.Favorites;
import hr.abysalto.hiring.mid.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoritesRepository extends JpaRepository<Favorites, Long> {
    boolean existsByUserAndProductId(User user, Long productId);
}
