package com.jokhakhamisjuma.AquaAdventures.model;

import java.util.ArrayList;
import java.util.List;

/** 
 * Zanzibar Grocery Store App 
 * https://github.com/Ramadhani-Yassin/Grocery-App
 * Created on 04-April-2025. 
 * Created by : Ramadhani Yassin Ramadhani:-https://github.com/Ramadhani-Yassin/
 */
public class Product {
    String id;
    String category;
    String name;
    String description;
    String attribute;
    String currency;
    String price;
    String discount;
    String homepage;
    List<ProductImage> images= new ArrayList<>();

    public Product() {
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }


    public List<ProductImage> getImages() {
        return images;
    }

    public void setImages(List<ProductImage> images) {
        this.images = images;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }
}
