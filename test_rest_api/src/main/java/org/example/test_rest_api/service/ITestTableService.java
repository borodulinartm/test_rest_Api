package org.example.test_rest_api.service;

import org.example.test_rest_api.model.TestTable;
import org.example.test_rest_api.model.TestTableRequest;

import java.util.List;
import java.util.Optional;

public interface ITestTableService {
    /**
     * Возврат всех имеющихся записей в БД
     * @return список всех элементов
     */
    List<TestTable> getAllRecords();

    /**
     * Возвращает одну запись из таблицы
     * @param id идентификатор записи
     * @return - значение таблицы (может быть и null)
     */
    Optional<TestTable> getRecordById(Long id);

    /**
     * Метод создает запись в таблице БД
     * @param request структура запроса (по сути, то же самое, что и {@code TestTable} только без id
     * @return - созданную запись
     */
    TestTable createRecord(TestTableRequest request);

    /**
     * Обновление записи в БД
     * @param id идентификатор записи
     * @param request новые сведения
     * @return обновленная запись
     */
    TestTable updateRecord(Long id, TestTableRequest request);

    /**
     * Метод удаляет строку из БД по её ID
     * @param id идентификатор записи
     */
    void deleteRecordById(Long id);

    /**
     * Метод возвращает произвольные данные из таблицы.
     * Используется в тестировании
     * @param elems количество элементов
     * @return список элементов
     */
    List<TestTable> getRandomRecords(Iterable<Long> elems);
}
