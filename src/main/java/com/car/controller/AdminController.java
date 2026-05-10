package com.car.controller;

import com.car.entity.Car;
import com.car.service.BookingService;
import com.car.service.CarService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AdminController {

    private final CarService carService;
    private final BookingService bookingService;

    public AdminController(CarService carService, BookingService bookingService) {
        this.carService = carService;
        this.bookingService = bookingService;
    }

    @GetMapping("/admin")
    public String adminPage(Model model) {
        model.addAttribute("car", new Car());
        model.addAttribute("cars", carService.getAllCars());
        model.addAttribute("bookings", bookingService.getAllBookings());
        return "admin";
    }

    @PostMapping("/admin/add-car")
    public String addCar(@Valid @ModelAttribute("car") Car car, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("cars", carService.getAllCars());
            model.addAttribute("bookings", bookingService.getAllBookings());
            return "admin";
        }

        carService.saveCar(car);
        return "redirect:/admin";
    }

    @GetMapping("/admin/edit-car/{id}")
    public String editCarPage(@PathVariable Long id, Model model) {
        Car car = carService.getCarById(id);

        if (car == null) {
            return "redirect:/admin";
        }

        model.addAttribute("car", car);
        model.addAttribute("cars", carService.getAllCars());
        model.addAttribute("bookings", bookingService.getAllBookings());
        return "admin";
    }

    @PostMapping("/admin/edit-car/{id}")
    public String updateCar(@PathVariable Long id,
                            @Valid @ModelAttribute("car") Car car,
                            BindingResult result,
                            Model model) {
        if (result.hasErrors()) {
            model.addAttribute("cars", carService.getAllCars());
            model.addAttribute("bookings", bookingService.getAllBookings());
            return "admin";
        }

        car.setId(id);
        carService.saveCar(car);
        return "redirect:/admin";
    }

    @PostMapping("/admin/delete-car/{id}")
    public String deleteCar(@PathVariable Long id) {
        carService.deleteCar(id);
        return "redirect:/admin";
    }
}
