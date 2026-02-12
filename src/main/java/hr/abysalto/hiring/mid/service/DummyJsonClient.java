package hr.abysalto.hiring.mid.service;

import hr.abysalto.hiring.mid.dto.ProductDto;
import hr.abysalto.hiring.mid.dto.ProductListResponse;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class DummyJsonClient {

    private final RestTemplate restTemplate;

    public DummyJsonClient(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    public ProductListResponse getAllProducts(int limit, int skip) {
        String url = "https://dummyjson.com/products?limit=" + limit + "&skip=" + skip;
        return restTemplate.getForObject(url, ProductListResponse.class);
    }

    public ProductDto getProductById(Long id) {
        String url = "https://dummyjson.com/products/" + id;
        return restTemplate.getForObject(url, ProductDto.class);
    }
}
