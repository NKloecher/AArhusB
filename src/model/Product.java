package model;

public class Product {
    private String name;
    private Integer clips;
    private String category;
    private String image;

    public Product(String name, Integer clips, String category, String image) {
        this.name = name;
        this.clips = clips;
        this.category = category;
        this.image = image;
    }
}
