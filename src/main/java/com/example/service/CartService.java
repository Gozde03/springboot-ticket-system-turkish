package com.example.service;

import com.example.entity.Event;
import com.example.model.CartItem;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {

    private final List<CartItem> cartItems = new ArrayList<>();

    public void addToCart(Event event) {
        CartItem item = new CartItem();
        item.setName(event.getName());
        item.setDateTime(event.getDateTime());
        item.setCity(event.getCity());
        item.setPrice(event.getTicketPrice()); // DÜZELTİLDİ
        item.setQuantity(1);

        cartItems.add(item);
    }



    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public double getTotal() {
        return cartItems.stream()
                .mapToDouble(CartItem::getPrice)
                .sum();
    }

    public void clearCart() {
        cartItems.clear();
    }
}
