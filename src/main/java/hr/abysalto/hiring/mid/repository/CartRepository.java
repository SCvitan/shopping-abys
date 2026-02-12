package hr.abysalto.hiring.mid.repository;

import hr.abysalto.hiring.mid.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    @Query("""
        select c from Cart c
        left join fetch c.item
        where c.user.id = :userId
    """)
    Optional<Cart> findByUserIdWithItems(Long userId);
}
