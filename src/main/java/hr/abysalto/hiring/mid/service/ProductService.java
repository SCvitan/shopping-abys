package hr.abysalto.hiring.mid.service;

import hr.abysalto.hiring.mid.dto.ProductDto;
import hr.abysalto.hiring.mid.dto.ProductListResponse;
import hr.abysalto.hiring.mid.model.Favorites;
import hr.abysalto.hiring.mid.model.User;
import hr.abysalto.hiring.mid.repository.FavoritesRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final DummyJsonClient dummyJsonClient;
    private final FavoritesRepository repository;

    public ProductService(DummyJsonClient dummyJsonClient, FavoritesRepository repository) {
        this.dummyJsonClient = dummyJsonClient;
        this.repository = repository;
    }

    public ProductListResponse getAllProducts(int limit, int skip) {
        return dummyJsonClient.getAllProducts(limit, skip);
    }

    public ProductDto getProduct(Long productId) {
        return dummyJsonClient.getProductById(productId);
    }

    public void addToFavorites(User user, Long productId) {

        dummyJsonClient.getProductById(productId);

        if (repository.existsByUserAndProductId(user, productId)) {
            return;
        }

        Favorites favorite = new Favorites();
        favorite.setUser(user);
        favorite.setProductId(productId);

        repository.save(favorite);
    }
}
