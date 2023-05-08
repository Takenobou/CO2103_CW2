package uk.ac.le.co2103.part2;

import java.io.Serializable;

public class ShoppingList implements Serializable {
    private String listId;
    private String name;
    private String image;

    public ShoppingList(String listId, String name, String image) {
        this.listId = listId;
        this.name = name;
        this.image = image;
    }

    public String getListId() {
        return listId;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }
}
