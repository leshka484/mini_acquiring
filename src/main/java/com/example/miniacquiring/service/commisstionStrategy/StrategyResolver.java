package com.example.miniacquiring.service.commisstionStrategy;

import com.example.miniacquiring.core.enums.CommissionType;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StrategyResolver {

    @Bean
    public Map<CommissionType, CommissionStrategy> commissionStrategies(List<CommissionStrategy> strategies){
        return strategies.stream()
                .collect(Collectors.toUnmodifiableMap(CommissionStrategy::getCommissionType, s -> s));
    }
}
