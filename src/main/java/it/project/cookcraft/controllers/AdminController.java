package it.project.cookcraft.controllers;

import it.project.cookcraft.models.Application;
import it.project.cookcraft.models.Review;
import it.project.cookcraft.services.interfaces.ApplicationService;
import it.project.cookcraft.services.interfaces.ReviewService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AdminController {
    private final ApplicationService applicationService;
    private final ReviewService reviewService;

    public AdminController(ApplicationService applicationService, ReviewService reviewService) {
        this.applicationService = applicationService;
        this.reviewService = reviewService;
    }

    @PreAuthorize("hasRole('Admin') || hasRole('SuperAdmin')")
    @GetMapping("/applications")
    public ResponseEntity<Page<Application>> getApplications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "4") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Application> applications = applicationService.findAllApplications(pageable);
        return new ResponseEntity<>(applications, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('Admin') || hasRole('SuperAdmin')")
    @GetMapping("/ratings")
    public ResponseEntity<Page<Review>> getReviews(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "4") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Review> reviews = reviewService.findAllReviews(pageable);
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }
}
