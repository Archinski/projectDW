package ru.skypro.homework.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.model.Image;
import ru.skypro.homework.repository.ImageRepository;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class ImageService {

    private final ImageRepository imageRepository;

    @Value("${upload.dir}")
    private String uploadDir;  // Путь для хранения картинок на сервере

    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @Transactional
    public String uploadImage(MultipartFile file) throws IOException {
        // Генерация уникального имени для картинки
        String fileName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();

        // Путь для хранения картинки
        Path targetLocation = Paths.get(uploadDir).resolve(fileName);

        // Сохраняем картинку на сервере
        Files.copy(file.getInputStream(), targetLocation);

        // Создание объекта изображения
        Image image = new Image();
        image.setId(UUID.randomUUID().toString());  // Генерация уникального ID
        image.setPath(targetLocation.toString());  // Сохранение пути картинки

        // Сохраняем информацию в базе данных
        imageRepository.save(image);

        return image.getId();  // Возвращаем ID изображения
    }

    public byte[] getImage(String imageId) throws IOException {
        Image image = imageRepository.findById(imageId).orElseThrow(() -> new IOException("Изображение не найдено"));
        File file = new File(image.getPath());
        return Files.readAllBytes(file.toPath());
    }
}
