package org.example.test_rest_api.repository;

import jakarta.annotation.Nonnull;
import org.example.test_rest_api.model.TestTable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TestTableRepository extends JpaRepository<TestTable, Long> {
    /**
     * Находит элемент по конкретному id
     * @param id must not be {@literal null}.
     * @return найденную запись
     */
    Optional<TestTable> findById(@Nonnull Long id);

    /**
     * Отбирает записи, у которых id принадлежит заданному множеству
     * @param ids массив id-шек
     * @return список найденных элементов
     */
    List<TestTable> getByIdIn(Iterable<Long> ids);
}
