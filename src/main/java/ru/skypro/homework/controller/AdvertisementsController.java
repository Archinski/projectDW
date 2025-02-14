package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.ads.Advertisements;
import ru.skypro.homework.dto.ads.AdvertisementsDTO;
import ru.skypro.homework.dto.ads.CreateOrUpdateAdvert;
import ru.skypro.homework.dto.ads.ExtendAdvert;

import java.io.IOException;

/**
 * Контроллер для работы с объявлениями на платформе.
 * Обрабатывает все запросы, связанные с CRUD операциями над объявлениями.
 */
@RestController
@RequestMapping("advertisements")
@RequiredArgsConstructor
@CrossOrigin
@Slf4j
@Tag(name = "Advertisements", description = "API для управления объявлениями")
@ApiResponses(value = {
        @ApiResponse(responseCode = "401", description = "Unauthorized: пользователь не авторизован"),
        @ApiResponse(responseCode = "500", description = "Internal Server Error: ошибка сервера при обработке запроса")
})
public class AdvertisementsController {

    /**
     * Получить список всех объявлений.
     *
     * @return список всех объявлений.
     */
    @Operation(summary = "Получить список всех объявлений")
    @ApiResponse(
            responseCode = "200",
            description = "OK: список объявлений успешно получен",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = Advertisements.class)))
    )
    @GetMapping
    public ResponseEntity<?> getAllAds() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Получить информацию об одном объявлении по ID.
     *
     * @param id идентификатор объявления.
     * @return информация об объявлении.
     */
    @Operation(summary = "Получить информацию об объявлении")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK: информация об объявлении получена",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ExtendAdvert.class))),
            @ApiResponse(responseCode = "404", description = "Not Found: объявление не найдено")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getAd(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Получить список объявлений пользователя.
     *
     * @return список объявлений пользователя.
     */
    @Operation(summary = "Получить список объявлений пользователя")
    @ApiResponse(
            responseCode = "200",
            description = "OK: список объявлений пользователя успешно получен",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = Advertisements.class)))
    )
    @GetMapping("/me")
    public ResponseEntity<?> getAdsMe() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Добавить новое объявление.
     *
     * @param ad свойства нового объявления.
     * @param image изображение для объявления.
     * @throws IOException ошибка при загрузке изображения.
     * @return ответ с кодом 201 (Created).
     */
    @Operation(summary = "Добавить объявление")
    @ApiResponse(
            responseCode = "201",
            description = "Created: объявление успешно создано",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = AdvertisementsDTO.class))
    )
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addAd(@RequestPart CreateOrUpdateAdvert ad,
                                   @RequestParam MultipartFile image) throws IOException {
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * Изменить объявление по ID.
     *
     * @param id идентификатор объявления.
     * @param ad обновлённые данные объявления.
     * @return ответ с кодом 200 (OK).
     */
    @Operation(summary = "Изменить объявление")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK: объявление изменено",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AdvertisementsDTO.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden: недостаточно прав для изменения"),
            @ApiResponse(responseCode = "404", description = "Not Found: объявление не найдено")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<?> updateAd(@PathVariable Integer id,
                                      @RequestBody CreateOrUpdateAdvert ad) {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Обновить изображение объявления по ID.
     *
     * @param id идентификатор объявления.
     * @param image новое изображение.
     * @return ответ с кодом 200 (OK).
     */
    @Operation(summary = "Обновление картинки объявления")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK: изображение успешно обновлено",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden: недостаточно прав для обновления"),
            @ApiResponse(responseCode = "404", description = "Not Found: объявление не найдено")
    })
    @PatchMapping("/{id}/image")
    public ResponseEntity<?> updateImage(@PathVariable("id") Integer id,
                                         @RequestParam("image") MultipartFile image) {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Удалить объявление по ID.
     *
     * @param id идентификатор объявления.
     * @return ответ с кодом 204 (No Content).
     */
    @Operation(summary = "Удалить объявление")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No Content: объявление удалено"),
            @ApiResponse(responseCode = "403", description = "Forbidden: недостаточно прав для удаления"),
            @ApiResponse(responseCode = "404", description = "Not Found: объявление не найдено")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeAd(@PathVariable Integer id) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
