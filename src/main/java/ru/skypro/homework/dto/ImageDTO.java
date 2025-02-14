package ru.skypro.homework.dto;

public class ImageDTO {

    private String imageUrl;  // Путь к изображению для фронта

    public ImageDTO(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
