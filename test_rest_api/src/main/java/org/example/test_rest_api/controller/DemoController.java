package org.example.test_rest_api.controller;

import lombok.RequiredArgsConstructor;
import org.example.test_rest_api.model.TestTable;
import org.example.test_rest_api.model.TestTableRequest;
import org.example.test_rest_api.service.impl.TestTableService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/test/demo", produces = "application/json", consumes = "application/json")
public class DemoController {
    private final TestTableService testTableService;

    @GetMapping("/all")
    public ResponseEntity<List<TestTable>> getAllRecords() {
        return ResponseEntity.of(Optional.of(testTableService.getAllRecords()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TestTable> getRecordById(@PathVariable Long id) {
        return ResponseEntity.of(testTableService.getRecordById(id));
    }

    // ID передавать не нужно. Поэтому был создан отдельный класс, где всего этого нет
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public void createRecord(@RequestBody TestTableRequest request) {
        testTableService.createRecord(request);
    }

    @PutMapping("/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void putRecord(@PathVariable Long id, @RequestBody TestTableRequest request) {
        testTableService.updateRecord(id, request);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRecord(@PathVariable Long id) {
        testTableService.deleteRecordById(id);
    }
}
