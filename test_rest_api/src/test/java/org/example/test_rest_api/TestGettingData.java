package org.example.test_rest_api;

import org.example.test_rest_api.model.TestTable;
import org.example.test_rest_api.repository.TestTableRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.LongStream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DisplayName("Тест №2: Выборка 1 млн. произвольных записей через 100 подключений")
public class TestGettingData {
    @Autowired
    private TestTableRepository testTableRepository;

    private static final int COUNT_ELEMS = 1_000_000;
    private static final int COUNT_CONNECTIONS = 100;
    private static final int SIZE_DATA = COUNT_ELEMS / COUNT_CONNECTIONS;
    private static final int COUNT_ROUNDS = 20;

    /**
     * Метод генерирует данные в таблицу (1М записей)
     */
    private void generateLargeData() {
        // Если что-либо есть в базе данных, очищаем
        testTableRepository.deleteAll();
        for (int i = 0; i < COUNT_ELEMS; i++) {
            TestTable request = TestTable
                    .builder()
                    .id((long) (i + 1))
                    .name("Test name #" + i)
                    .description("Test description #" + i)
                    .build();

            testTableRepository.save(request);
            assertThat(request).isNotNull();
        }
    }

    /**
     * Основной метод теста
     */
    @Test
    @Order(1)
    @Commit
    public void calculateTask() {
        generateLargeData();

        // В качестве подключения используется многопоточка с указанным количетсвом потоков (по условию, 100)
        Callable<Boolean> callFunction = () -> {
            Iterable<Long> randomElems = LongStream.generate(() -> new Random().nextLong(COUNT_ELEMS))
                    .limit(SIZE_DATA).boxed().toList();
            testTableRepository.getByIdIn(randomElems);
            return true;
        };

        // Массив результатов обработки
        List<Callable<Boolean>> dataSet = new ArrayList<>(Collections.nCopies(COUNT_CONNECTIONS, callFunction));
        List<Long> timeExecutions = new ArrayList<>(COUNT_ROUNDS);

        // Используется для расчёта обычного времени
        long elapsedTime = 0L;

        // Количество раундов (для расчёта медианы и процентилей)
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

    /**
     * Метод рассчитывает медиану
     * @param timeExecutions массив медиан
     * @return медианное значение
     */
    private double calculateMedian(List<Long> timeExecutions) {
        if (timeExecutions.size() % 2 == 0) {
            return (double)((timeExecutions.get(timeExecutions.size() / 2)
                    + timeExecutions.get(timeExecutions.size() / 2 - 1))) / 2;
        } else {
            return (double) (timeExecutions.get(timeExecutions.size() / 2));
        }
    }

    /**
     * Метод рассчитывает процентиль
     * @param timeExecutions массив показателей
     * @param percentile процентиль
     * @return рассчитанное значение
     */
    private double calculatePercentile(List<Long> timeExecutions, double percentile) {
        int indexToReturn = (int) Math.ceil(percentile / 100 * timeExecutions.size());
        return timeExecutions.get(indexToReturn - 1);
    }
}
