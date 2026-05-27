package com.example.roomreservationservice.demo.controller;

import com.example.roomreservationservice.demo.model.Rooms;
import com.example.roomreservationservice.demo.service.RoomsService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
public class RoomsController {

    private final RoomsService roomsService;

    public RoomsController(RoomsService roomsService) {
        this.roomsService = roomsService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("rooms", roomsService.getAllRooms());
        return "index";
    }

    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }

    @GetMapping("/admin")
    public String admin(Model model) {
        model.addAttribute("rooms", roomsService.getAllRooms());
        model.addAttribute("room", new Rooms());
        return "admin";
    }

    @PostMapping("/admin/add")
    public String addRoom(@ModelAttribute Rooms room) {
        room.setBooked(false);
        roomsService.saveRoom(room);
        return "redirect:/admin";
    }

    @PostMapping("/admin/delete/{id}")
    public String deleteRoom(@PathVariable Long id) {
        roomsService.deleteRoom(id);
        return "redirect:/admin";
    }

    @PostMapping("/book/{id}")
    public String bookRoom(
            @PathVariable Long id,
            @RequestParam("startTime")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime startTime,
            @RequestParam("endTime")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime endTime
    ) {
        roomsService.bookRoom(id, startTime, endTime);
        return "redirect:/";
    }

    @PostMapping("/cancel/{id}")
    public String cancelBooking(@PathVariable Long id) {
        roomsService.cancelBooking(id);
        return "redirect:/";
    }
}
