package com.example.miniacquiring.service;

import com.example.miniacquiring.storage.CommissionStorage;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class CommissionService {

    private final CommissionStorage commissionStorage;

    public void deleteById(List<Long> ids) {
        log.info("Deleting commissions");
        commissionStorage.deleteById(ids);
    }

    public void deleteById(Long id) {
        log.info("Deleting commission with id = {}", id);
        commissionStorage.deleteById(id);

    }

}
