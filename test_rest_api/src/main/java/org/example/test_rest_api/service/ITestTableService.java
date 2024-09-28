package org.example.test_rest_api.service;

import org.example.test_rest_api.model.TestTable;
import org.example.test_rest_api.model.TestTableRequest;

import java.util.List;
import java.util.Optional;

public interface ITestTableService {
    List<TestTable> getAllRecords();
    Optional<TestTable> getRecordById(Long id);
    TestTable createRecord(TestTableRequest request);
    TestTable updateRecord(Long id, TestTableRequest request);
    void deleteRecordById(Long id);
    List<TestTable> getRandomRecords(int countElems);
}
