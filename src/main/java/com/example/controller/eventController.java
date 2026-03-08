package com.example.controller;

import com.example.entity.Event;
import com.example.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/event")
@SessionAttributes("cart")
public class eventController {

    @Autowired
    private EventRepository eventRepository;

    @ModelAttribute("cart")
    public List<Event> createCart() {
        return new ArrayList<>();
    }

    @GetMapping
    public String showEvents(Model model) {
        List<Event> events = eventRepository.findAll();
        model.addAttribute("events", events);

        model.addAttribute("pageTitle", "Etkinlikler Listesi");
        model.addAttribute("content", "pages/event");
        return "layout/layout";
    }

    @PostMapping("/add-to-cart/{eventId}")
    @ResponseBody
    public String addToCart(@PathVariable Long eventId,
                            @ModelAttribute("cart") List<Event> cart) {

        Event event = eventRepository.findById(eventId).orElse(null);
        if (event == null) return "Etkinlik bulunamadı";

        boolean alreadyInCart = cart.stream().anyMatch(e -> e.getId().equals(eventId));
        if (alreadyInCart) return "Etkinlik zaten sepette";

        cart.add(event);
        return "Etkinlik sepete eklendi";
    }

    @GetMapping("/cart")
    public String showCart(@ModelAttribute("cart") List<Event> cart, Model model) {
        model.addAttribute("cart", cart);

        model.addAttribute("pageTitle", "Sepetinizdeki Etkinlikler");
        model.addAttribute("content", "pages/cart");
        return "layout/layout";
    }
}