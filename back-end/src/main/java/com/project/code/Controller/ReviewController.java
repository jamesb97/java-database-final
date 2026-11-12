package com.project.code.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.code.Model.Customer;
import com.project.code.Model.Review;
import com.project.code.Repo.CustomerRepository;
import com.project.code.Repo.ReviewRepository;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    CustomerRepository customerRepository;

    @GetMapping("/{storeId}/{productId}")
    public Map<String, Object> getReviews(@PathVariable long storeId, @PathVariable long productId) {
        Map<String, Object> map = new HashMap<>();
        List<Review> reviews = reviewRepository.findByStoreIdAndProductId(storeId, productId);
        Set<Long> customerIds = reviews.stream()
                .map(Review::getCustomerId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        Map<Long, Customer> customersById = new HashMap<>();
        customerRepository.findAllById(customerIds)
                .forEach(customer -> customersById.put(customer.getId(), customer));

        List<Map<String, Object>> reviewsWithCustomerNames = new ArrayList<>();

        for (Review review : reviews) {
            Map<String, Object> reviewMap = new HashMap<>();
            reviewMap.put("review", review.getComment());
            reviewMap.put("rating", review.getRating());

            Customer customer = customersById.get(review.getCustomerId());
            if (customer != null) {
                reviewMap.put("customerName", customer.getName());
            } else {
                reviewMap.put("customerName", "Unknown");
            }

            reviewsWithCustomerNames.add(reviewMap);
        }

        map.put("reviews", reviewsWithCustomerNames);
        return map;
    }

    @GetMapping
    public Map<String, Object> getAllReviews() {
        Map<String, Object> map = new HashMap<>();
        map.put("reviews", reviewRepository.findAll());
        return map;
    }

}

// package com.project.code.Controller;

// public class ReviewController {
// 1. Set Up the Controller Class:
// - Annotate the class with `@RestController` to designate it as a REST
// controller for handling HTTP requests.
// - Map the class to the `/reviews` URL using `@RequestMapping("/reviews")`.

// 2. Autowired Dependencies:
// - Inject the following dependencies via `@Autowired`:
// - `ReviewRepository` for accessing review data.
// - `CustomerRepository` for retrieving customer details associated with
// reviews.

// 3. Define the `getReviews` Method:
// - Annotate with `@GetMapping("/{storeId}/{productId}")` to fetch reviews for
// a specific product in a store by `storeId` and `productId`.
// - Accept `storeId` and `productId` via `@PathVariable`.
// - Fetch reviews using `findByStoreIdAndProductId()` method from
// `ReviewRepository`.
// - Filter reviews to include only `comment`, `rating`, and the `customerName`
// associated with the review.
// - Use `findById(review.getCustomerId())` from `CustomerRepository` to get
// customer name.
// - Return filtered reviews in a `Map<String, Object>` with key `reviews`.

// }
