package com.example.controller;

import com.example.entity.Event;
import com.example.model.CartItem;
import com.example.repository.EventRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class CartController {

    @Autowired
    private EventRepository eventRepository;

    @GetMapping("/cart")
    public String showCart(Model model, HttpSession session) {
        List<CartItem> cartItems = (List<CartItem>) session.getAttribute("cartItems");
        if (cartItems == null) {
            cartItems = new ArrayList<>();
        }

        double totalPrice = cartItems.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("totalPrice", totalPrice);

        return "cart";
    }
    @PostMapping("/add-to-cart/{id}")
    @ResponseBody
    public ResponseEntity<String> addToCart(@PathVariable Long id, HttpSession session) {
        System.out.println("Gelen etkinlik ID: " + id); 

        Event event = eventRepository.findById(id).orElse(null);

        if (event == null) {
            System.out.println("Etkinlik bulunamadı."); 
            return ResponseEntity.badRequest().body("Etkinlik bulunamadı.");
        }

        List<CartItem> cartItems = (List<CartItem>) session.getAttribute("cartItems");
        if (cartItems == null) {
            cartItems = new ArrayList<>();
        }

        boolean found = false;
        for (CartItem item : cartItems) {
            if (item.getEventId() != null && item.getEventId().equals(event.getId())) {
                item.setQuantity(item.getQuantity() + 1);
                found = true;
                break;
            }
        }

        if (!found) {
            CartItem newItem = new CartItem();
            newItem.setEventId(event.getId());
            newItem.setName(event.getName());
            newItem.setDateTime(event.getDateTime());
            newItem.setCity(event.getCity());
            newItem.setPrice(event.getTicketPrice());
            newItem.setQuantity(1);
            cartItems.add(newItem);
        }

        session.setAttribute("cartItems", cartItems);
        System.out.println("Etkinlik sepete eklendi: " + event.getName()); 
        return ResponseEntity.ok("Etkinlik sepete eklendi.");
    }
}