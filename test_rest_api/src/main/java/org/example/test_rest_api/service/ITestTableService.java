package org.example.test_rest_api.service;

import org.example.test_rest_api.model.TestTable;
import org.example.test_rest_api.model.TestTableRequest;

import java.util.List;
import java.util.Optional;

public interface ITestTableService {
    List<TestTable> getAllRecords();
    Optional<TestTable> getRecordById(Long id);
    void createRecord(TestTableRequest request);
    void updateRecord(Long id, TestTableRequest request);
    boolean deleteRecordById(Long id);
}
