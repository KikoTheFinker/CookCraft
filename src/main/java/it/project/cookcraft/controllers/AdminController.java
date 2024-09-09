package it.project.cookcraft.controllers;

import it.project.cookcraft.models.Application;
import it.project.cookcraft.models.Review;
import it.project.cookcraft.security.JwtUtil;
import it.project.cookcraft.services.interfaces.ApplicationService;
import it.project.cookcraft.services.interfaces.ReviewService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AdminController {
    private final ApplicationService applicationService;
    private final ReviewService reviewService;
    private final JwtUtil jwtUtil;

    public AdminController(ApplicationService applicationService, ReviewService reviewService, JwtUtil jwtUtil) {
        this.applicationService = applicationService;
        this.reviewService = reviewService;
        this.jwtUtil = jwtUtil;
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
        System.out.println("Received request to delete review with ID: " + reviewId);

        boolean isDeleted = reviewService.deleteReviewByIdViaAdmin(reviewId);

        if(isDeleted) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}