package com.car.controller;

import com.car.entity.Booking;
import com.car.entity.Car;
import com.car.entity.User;
import com.car.service.BookingService;
import com.car.service.CarService;
import com.car.service.UserService;
import jakarta.validation.Valid;
import java.security.Principal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class BookingController {

    private final BookingService bookingService;
    private final CarService carService;
    private final UserService userService;

    public BookingController(BookingService bookingService, CarService carService, UserService userService) {
        this.bookingService = bookingService;
        this.carService = carService;
        this.userService = userService;
    }

    @GetMapping("/booking")
    public String bookingPage(@RequestParam(required = false) Long carId, Model model) {
        if (carId == null) {
            return "redirect:/cars";
        }

        Car car = carService.getCarById(carId);

        if (car == null || !car.isAvailable()) {
            return "redirect:/cars";
        }

        Booking booking = new Booking();
        booking.setCar(car);

        model.addAttribute("booking", booking);
        model.addAttribute("car", car);
        return "booking";
    }

    @PostMapping("/booking/save")
    public String saveBooking(@Valid @ModelAttribute("booking") Booking booking,
                              BindingResult result,
                              @RequestParam Long carId,
                              Principal principal,
                              Model model) {
        Car car = carService.getCarById(carId);

        if (car == null || !car.isAvailable()) {
            return "redirect:/cars";
        }

        if (result.hasErrors()) {
            model.addAttribute("car", car);
            return "booking";
        }

        User user = userService.findByEmail(principal.getName()).orElse(null);
        if (user == null) {
            return "redirect:/login";
        }

        booking.setCar(car);
        booking.setUser(user);
        bookingService.saveBooking(booking);

        // After booking, the car is no longer available.
        car.setAvailable(false);
        carService.saveCar(car);

        return "redirect:/cars";
    }
}
