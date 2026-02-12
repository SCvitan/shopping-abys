package hr.abysalto.hiring.mid.model;

import jakarta.persistence.*;

@Entity
public class Favorites {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Long productId;

}
