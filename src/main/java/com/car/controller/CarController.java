package com.car.controller;

import com.car.entity.Car;
import com.car.service.CarService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class CarController {

    private final CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping("/cars")
    public String showCars(Model model) {
        model.addAttribute("cars", carService.getAllCars());
        return "cars";
    }

    @GetMapping("/cars/{id}")
    public String showCarDetails(@PathVariable Long id, Model model) {
        Car car = carService.getCarById(id);

        if (car == null) {
            return "redirect:/cars";
        }

        model.addAttribute("car", car);
        return "car-details";
    }
}
