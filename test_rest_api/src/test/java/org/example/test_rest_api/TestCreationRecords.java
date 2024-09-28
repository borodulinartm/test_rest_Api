package org.example.test_rest_api;

import org.example.test_rest_api.model.TestTable;
import org.example.test_rest_api.model.TestTableRequest;
import org.example.test_rest_api.service.impl.TestTableService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class TestCreationRecords {
    private static final int COUNT_ELEMS = 100_000;

    @Autowired
    private TestTableService testTableService;

    @Test
    @DisplayName("Тест №1: Создание 100'000 записей в H2 базе данных")
    public void createData() {
        for (int i = 0; i < COUNT_ELEMS; i++) {
            TestTableRequest request = TestTableRequest
                    .builder()
                    .name("Test name #" + i)
                    .description("Test description #" + i)
                    .build();

            TestTable savedData = testTableService.createRecord(request);
            assertThat(savedData).isNotNull();
        }
    }
}
