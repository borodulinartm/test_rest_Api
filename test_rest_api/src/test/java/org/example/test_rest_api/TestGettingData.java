package org.example.test_rest_api;

import lombok.extern.slf4j.Slf4j;
import org.example.test_rest_api.model.TestTableRequest;
import org.example.test_rest_api.service.impl.TestTableService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DisplayName("Тест №2: Выборка 1 млн. произвольных записей через 100 подключений")
@Slf4j
public class TestGettingData {
    @Autowired
    private TestTableService testTableService;

    private static final int COUNT_ELEMS = 1_000_000;
    private static final int COUNT_CONNECTIONS = 100;
    private static final int SIZE_DATA = COUNT_ELEMS / COUNT_CONNECTIONS;
    private static final int COUNT_ROUNDS = 20;

    // Метод генерирует данные
    private void generateLargeData() {
        for (int i = 0; i < COUNT_ELEMS; i++) {
            TestTableRequest request = TestTableRequest
                    .builder()
                    .name("Test name #" + i)
                    .description("Test description #" + i)
                    .build();

            testTableService.createRecord(request);
            assertThat(request).isNotNull();
        }
    }

    @Test
    @Order(2)
    @Commit
    public void calculateTask() {
        generateLargeData();

        Callable<Boolean> callFunction = () -> {
            testTableService.getRandomRecords(SIZE_DATA);
            return true;
        };

        List<Callable<Boolean>> dataSet = new ArrayList<>(Collections.nCopies(COUNT_CONNECTIONS, callFunction));
        List<Long> timeExecutions = new ArrayList<>(COUNT_ROUNDS);

        long elapsedTime = 0L;

        // Количество раундов
        for (int i = 0; i < COUNT_ROUNDS; i++) {
            try {
                var service = Executors.newFixedThreadPool(COUNT_CONNECTIONS);
                long startTime = System.currentTimeMillis();

                List<Future<Boolean>> res = service.invokeAll(dataSet);
                for (Future<Boolean> future : res) {
                    if (!future.get()) {
                        throw new RuntimeException("Ошибка выполнения!");
                    }
                }

                long endTime = System.currentTimeMillis();
                if (elapsedTime == 0L)
                    elapsedTime = endTime - startTime;

                timeExecutions.add(endTime - startTime);
            } catch (InterruptedException | ExecutionException exception) {
                throw new RuntimeException(exception);
            }
        }

        Collections.sort(timeExecutions);

        System.out.println("Время выполнения (мс): " + elapsedTime);
        System.out.println("Медиана (мс): " + calculateMedian(timeExecutions));
        System.out.println("95 процентиль (мс): " + calculatePercentile(timeExecutions, 95));
        System.out.println("99 процентиль (мс): " + calculatePercentile(timeExecutions, 99));
    }

    // Метод рассчитывает медиану
    private double calculateMedian(List<Long> timeExecutions) {
        if (timeExecutions.size() % 2 == 0) {
            return (double)((timeExecutions.get(timeExecutions.size() / 2)
                    + timeExecutions.get(timeExecutions.size() / 2 - 1))) / 2;
        } else {
            return (double) (timeExecutions.get(timeExecutions.size() / 2));
        }
    }

    // Метод рассчитывает процентиль
    private double calculatePercentile(List<Long> timeExecutions, double percentile) {
        int indexToReturn = (int) Math.ceil(percentile / 100 * timeExecutions.size());
        return timeExecutions.get(indexToReturn - 1);
    }
}
