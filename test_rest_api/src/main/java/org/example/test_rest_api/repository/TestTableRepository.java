package org.example.test_rest_api.repository;

import jakarta.annotation.Nonnull;
import org.example.test_rest_api.model.TestTable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TestTableRepository extends JpaRepository<TestTable, Long> {
    Optional<TestTable> findById(@Nonnull Long id);
}
