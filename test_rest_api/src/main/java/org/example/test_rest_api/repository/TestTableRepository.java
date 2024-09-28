package org.example.test_rest_api.repository;

import jakarta.annotation.Nonnull;
import org.example.test_rest_api.model.TestTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TestTableRepository extends JpaRepository<TestTable, Long> {
    Optional<TestTable> findById(@Nonnull Long id);

    @Query("SELECT tt FROM TestTable tt WHERE tt.id >= " +
            "(SELECT FLOOR(RAND() * (SELECT MAX(id) FROM TestTable))) ORDER BY tt.id LIMIT :count_elems")
    List<TestTable> getRandomElements(@Param("count_elems") Integer count_elems);
}
