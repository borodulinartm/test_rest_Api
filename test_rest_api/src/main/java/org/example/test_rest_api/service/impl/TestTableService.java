package org.example.test_rest_api.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.test_rest_api.model.TestTable;
import org.example.test_rest_api.model.TestTableRequest;
import org.example.test_rest_api.repository.TestTableRepository;
import org.example.test_rest_api.service.ITestTableService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TestTableService implements ITestTableService {
    private final TestTableRepository tableRepository;

    @Override
    public List<TestTable> getAllRecords() {
        return tableRepository.findAll();
    }

    @Override
    public Optional<TestTable> getRecordById(Long id) {
        return tableRepository.findById(id);
    }

    @Override
    public void createRecord(TestTableRequest request) {
        TestTable newData = TestTable
                .builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();
        tableRepository.save(newData);
    }

    @Override
    public void updateRecord(Long id, TestTableRequest request) {
        Optional<TestTable> curValue = getRecordById(id);
        if (curValue.isPresent()) {
            TestTable val = curValue.get();

            val.setName(request.getName());
            val.setDescription(request.getDescription());

            tableRepository.save(val);
        }
    }

    @Override
    public boolean deleteRecordById(Long id) {
        tableRepository.deleteById(id);
        return true;
    }
}
