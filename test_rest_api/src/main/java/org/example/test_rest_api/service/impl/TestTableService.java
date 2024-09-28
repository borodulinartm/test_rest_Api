package org.example.test_rest_api.service.impl;

import jakarta.transaction.Transactional;
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
    public TestTable createRecord(TestTableRequest request) {
        TestTable newData = TestTable
                .builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();
        return tableRepository.save(newData);
    }

    @Override
    public TestTable updateRecord(Long id, TestTableRequest request) {
        Optional<TestTable> curValue = getRecordById(id);
        if (curValue.isPresent()) {
            TestTable val = curValue.get();

            val.setName(request.getName());
            val.setDescription(request.getDescription());

            return tableRepository.save(val);
        }

        return null;
    }

    @Override
    public void deleteRecordById(Long id) {
        tableRepository.deleteById(id);
    }

    @Override
    public List<TestTable> getRandomRecords(int countElems) {
        return tableRepository.getRandomElements(countElems);
    }
}
