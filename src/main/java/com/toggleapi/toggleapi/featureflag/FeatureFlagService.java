package com.toggleapi.toggleapi.featureflag;

import com.project.toggleapi.openApi.generated.model.CreateFlagRequest;
import com.project.toggleapi.openApi.generated.model.FeatureFlagResponse;
import com.project.toggleapi.openApi.generated.model.PagedFeatureFlagResponse;
import jakarta.persistence.Cacheable;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeatureFlagService {

    private final FeatureFlagRepository featureFlagRepository;
    private final FeatureFlagMapper featureFlagMapper;

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

    @Transactional
    public FeatureFlagResponse createFlag(CreateFlagRequest createFlagRequest) {
        try {
            Optional<FeatureFlag> existingFlag = featureFlagRepository.findByNameAndEnvironment(createFlagRequest.getName(), createFlagRequest.getEnvironment());
            if (existingFlag.isPresent()) {
                throw new IllegalArgumentException(String.format("Un flag con il nome '%s' esiste già nell'ambiente '%s'.", createFlagRequest.getName(), createFlagRequest.getEnvironment()));
            }
            FeatureFlag featureFlag = featureFlagMapper.toEntity(createFlagRequest);
            FeatureFlag savedFlag = featureFlagRepository.saveAndFlush(featureFlag);
            return featureFlagMapper.toResponse(savedFlag);
        } catch (Exception e) {
            log.error("Errore critico durante la creazione del flag {} in {}: {}", createFlagRequest.getName(), createFlagRequest.getEnvironment(), e.getMessage());
            throw e;
        }

    }

    public PagedFeatureFlagResponse getAllFlags(Integer page, Integer size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<FeatureFlag> flagsPage = featureFlagRepository.findAll(pageable);
            return featureFlagMapper.toPagedResponse(flagsPage);
        } catch (Exception e) {
            log.error("Errore critico durante la lettura dei flag a pagina {} dimensione {}: {}", page, size, e.getMessage());
            PagedFeatureFlagResponse emptyResponse = new PagedFeatureFlagResponse();
            emptyResponse.setContent(Collections.emptyList());
            emptyResponse.setTotalElements(0);
            emptyResponse.setTotalPages(0);
            emptyResponse.setLast(true);
            return emptyResponse;
        }
    }
}
