package com.example.miniacquiring.storage;

import com.example.miniacquiring.storage.entity.OperationStatusEntity;
import com.example.miniacquiring.storage.repository.OperationStatusRepository;

public class OperationStatusStorage extends BaseStorage<OperationStatusEntity, Long, OperationStatusRepository> {

    public OperationStatusStorage(OperationStatusRepository operationStatusRepository) {
        super(operationStatusRepository);
    }

}
