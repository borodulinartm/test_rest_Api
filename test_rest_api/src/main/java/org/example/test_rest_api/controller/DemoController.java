package org.example.test_rest_api.controller;

import lombok.RequiredArgsConstructor;
import org.example.test_rest_api.model.TestTable;
import org.example.test_rest_api.model.TestTableRequest;
import org.example.test_rest_api.service.impl.TestTableService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/test/demo")
public class DemoController {
    private final TestTableService testTableService;

    /**
     * Список всех записей
     * @return массив найденных записей
     */
    @GetMapping("/all")
    public ResponseEntity<List<TestTable>> getAllRecords() {
        return ResponseEntity.ok(testTableService.getAllRecords());
    }

    /**
     * Поиск по id
     * @param id идентификатор элемента
     * @return конечный объект ({@code TestTable})
     */
    @GetMapping("/{id}")
    public ResponseEntity<TestTable> getRecordById(@PathVariable Long id) {
        return ResponseEntity.of(testTableService.getRecordById(id));
    }

    /**
     * Создаёт новую запись
     * @param request содержимое нового запроса
     */
    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void createRecord(@RequestBody TestTableRequest request) {
        testTableService.createRecord(request);
    }

    /**
     * Обновление записи по id
     * @param id идентификатор записи
     * @param request запрос на создание элемента
     */
    @PutMapping(value = "/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void putRecord(@PathVariable Long id, @RequestBody TestTableRequest request) {
        testTableService.updateRecord(id, request);
    }

    /**
     * Удаление записи
     * @param id идентификатор записи
     */
    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRecord(@PathVariable Long id) {
        testTableService.deleteRecordById(id);
    }
}
