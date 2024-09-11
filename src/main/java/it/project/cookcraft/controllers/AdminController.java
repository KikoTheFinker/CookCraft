package it.project.cookcraft.controllers;

import it.project.cookcraft.models.Application;
import it.project.cookcraft.models.Order;
import it.project.cookcraft.models.Review;
import it.project.cookcraft.security.JwtUtil;
import it.project.cookcraft.services.interfaces.ApplicationService;
import it.project.cookcraft.services.interfaces.OrderService;
import it.project.cookcraft.services.interfaces.ReviewService;
import it.project.cookcraft.services.interfaces.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class AdminController {
    private final ApplicationService applicationService;
    private final ReviewService reviewService;
    private final UserService userService;
    private final OrderService orderService;

    public AdminController(ApplicationService applicationService, ReviewService reviewService, UserService userService, OrderService orderService) {
        this.applicationService = applicationService;
        this.reviewService = reviewService;
        this.userService = userService;
        this.orderService = orderService;
    }

    @GetMapping("/admin/applications")
    public ResponseEntity<Page<Application>> getApplications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "4") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Application> applications = applicationService.findAllApplications(pageable);
        return new ResponseEntity<>(applications, HttpStatus.OK);
    }

    @GetMapping("/admin/orders")
    public ResponseEntity<Page<Order>> getOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "4") int size
    ) {
        Pageable pageable = PageRequest.of(page,size);
        Page<Order> orders = orderService.findAllOrders(pageable);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("/admin/orderreviews")
    public ResponseEntity<Page<Order>> getOrderReviews(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "4") int size
    ) {
        Pageable pageable = PageRequest.of(page,size);
        Page<Order> ordersWithReviews = orderService.findAllFinishedOrdersWithReviews(pageable);
        return new ResponseEntity<>(ordersWithReviews, HttpStatus.OK);
    }

    @GetMapping("/admin/reviews")
    public ResponseEntity<Page<Review>> getReviews(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "4") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Review> reviews = reviewService.findAllReviews(pageable);
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    @DeleteMapping("/admin/reviews/{reviewId}")
    public ResponseEntity<?> deleteReviewById(@PathVariable Long reviewId, @RequestHeader("Authorization") String jwtToken) {
        boolean isDeleted = reviewService.deleteReviewByIdViaAdmin(reviewId);

        if(isDeleted) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/admin/accept/{userId}")
    public ResponseEntity<?> updateUserToDelivery(@PathVariable Long userId) {
        boolean isUpdated = userService.updateUserToDeliveryById(userId);

        if(isUpdated)
        {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/admin/decline/{id}")
    public ResponseEntity<?> declineUserForDelivery(@PathVariable Long id) {
        Optional<Application> application = applicationService.findApplicationById(id);

        if(application.isPresent()) {
            applicationService.deleteApplication(application.get());
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}