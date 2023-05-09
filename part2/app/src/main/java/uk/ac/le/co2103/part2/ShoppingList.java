package uk.ac.le.co2103.part2;

import java.util.ArrayList;
import java.util.List;

public class ShoppingList {
    private int listId;
    private String name;
    private String imageUri;
    private List<Product> products;

    public ShoppingList(int listId, String name, String imageUri) {
        this.listId = listId;
        this.name = name;
        this.imageUri = imageUri;
        this.products = new ArrayList<>();
    }

    public int getListId() {
        return listId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void addProduct(Product product) {
        this.products.add(product);
    }
}
