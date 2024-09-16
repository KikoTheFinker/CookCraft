package it.project.cookcraft.controllers;

import it.project.cookcraft.dto.ExtendedOrderDTO;
import it.project.cookcraft.models.Application;
import it.project.cookcraft.models.Order;
import it.project.cookcraft.models.Review;
import it.project.cookcraft.models.User;
import it.project.cookcraft.services.interfaces.ApplicationService;
import it.project.cookcraft.services.interfaces.OrderService;
import it.project.cookcraft.services.interfaces.ReviewService;
import it.project.cookcraft.services.interfaces.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

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
    public ResponseEntity<Page<ExtendedOrderDTO>> getOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "4") int size
    ) {
        Pageable pageable = PageRequest.of(page,size);
        List<Order> orderList = orderService.findAllOrders();
        int totalRows = orderList.size();
        Page<Order> orderPage = orderService.findAllOrders(totalRows, pageable);

        AtomicBoolean error = new AtomicBoolean(false);
        List<ExtendedOrderDTO> extendedOrderDTOS = orderPage.stream().map(order -> {
            Long userId = order.getUserId();
            Long deliveryPersonId = order.getDeliveryPersonId();

            Optional<User> user = userService.findUserById(userId);
            Optional<User> deliveryPerson = userService.findDeliveryPersonByUserId(deliveryPersonId);

            if(user.isEmpty())
            {
                error.set(true);
            }
            return new ExtendedOrderDTO(order, user.orElse(null), deliveryPerson.orElse(null));
        }).toList();

        if(error.get())
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Page<ExtendedOrderDTO> reviewedOrders = new PageImpl<>(extendedOrderDTOS, pageable, totalRows);
        return new ResponseEntity<>(reviewedOrders, HttpStatus.OK);
    }

    @GetMapping("/admin/orderreviews")
    public ResponseEntity<Page<ExtendedOrderDTO>> getOrderReviews(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "4") int size
    ) {
        Pageable pageable = PageRequest.of(page,size);
        List<Order> ordersList = orderService.findAllReviewedOrders();
        int totalRows = ordersList.size();
        Page<Order> orderPage = orderService.findAllReviewedOrders(totalRows, pageable);

        AtomicBoolean error = new AtomicBoolean(false);
        List<ExtendedOrderDTO> extendedOrderDTOS = orderPage.stream().map(order -> {
            Long userId = order.getUserId();
            Long deliveryPersonId = order.getDeliveryPersonId();

            Optional<User> user = userService.findUserById(userId);
            Optional<User> deliveryPerson = userService.findDeliveryPersonByUserId(deliveryPersonId);

            if(user.isEmpty() || deliveryPerson.isEmpty())
            {
                error.set(true);
            }
            return new ExtendedOrderDTO(order, user.orElse(null), deliveryPerson.orElse(null));
        }).toList();

        if(error.get())
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Page<ExtendedOrderDTO> reviewedOrders = new PageImpl<>(extendedOrderDTOS, pageable, totalRows);
        return new ResponseEntity<>(reviewedOrders, HttpStatus.OK);
    }

    @DeleteMapping("/admin/orderreviews/{orderId}")
    public ResponseEntity<?> removeOrderReview(@PathVariable Long orderId, @RequestHeader("Authorization") String jwtToken) {
        boolean isDeleted = orderService.deleteById(orderId);
        if(isDeleted)
        {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
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