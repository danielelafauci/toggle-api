package com.toggleapi.toggleapi.featureflag;

import jakarta.persistence.Cacheable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeatureFlagService {

    private final FeatureFlagRepository featureFlagRepository;



    public boolean isEnabled(String flagName, String environment, UUID userId) {
        try {
            Optional<FeatureFlag> featureFlag = featureFlagRepository.findByNameAndEnvironment(flagName, environment);
            if (featureFlag.isEmpty()) {
                return false;
            }
            FeatureFlag flag = featureFlag.get();

            if (!flag.isEnabled()) {
                return false;
            }

            if (flag.getRolloutPercentage() == 100) {
                return true;
            }

            if (userId == null) {
                return false;
            }

            String hashKey = flagName + userId.toString();
            int userHashValue = Math.abs(hashKey.hashCode()) % 100 + 1;

            return userHashValue <= flag.getRolloutPercentage();

        } catch (Exception e) {
            log.error("Errore critico durante la lettura del flag {} in {}: {}", flagName, environment, e.getMessage());
            return false;
        }
    }
}
