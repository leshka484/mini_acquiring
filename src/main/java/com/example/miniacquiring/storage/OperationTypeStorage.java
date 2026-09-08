package com.example.miniacquiring.storage;

import com.example.miniacquiring.storage.entity.OperationTypeEntity;
import com.example.miniacquiring.storage.repository.OperationTypeRepository;

public class OperationTypeStorage extends BaseStorage<OperationTypeEntity, Long, OperationTypeRepository> {

    public OperationTypeStorage(OperationTypeRepository operationTypeRepository) {
        super(operationTypeRepository);
    }

}
