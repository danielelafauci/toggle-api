package com.toggleapi.toggleapi.featureflag;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class FeatureFlagServiceTest {

    @Mock
    private FeatureFlagRepository featureFlagRepository;

    @InjectMocks
    private FeatureFlagService featureFlagService;

    @Test
    void shouldReturnFalse_WhenFlagDoesNotExist() {
        String flagName = "test-flag";
        String environment = "produzione";
        UUID userId = UUID.randomUUID();

        when(featureFlagRepository.findByNameAndEnvironment(flagName, environment)).thenReturn(Optional.empty());

        //ACT
        boolean result = featureFlagService.isEnabled(flagName, environment, userId);

        //ASSERT
        assertFalse(result);
    }

    @Test
    void shouldReturnFalse_WhenFlagIsNotEnabled() {
        String flagName = "test-flag";
        String environment = "produzione";
        UUID userId = UUID.randomUUID();

        FeatureFlag featureFlagFake = new FeatureFlag();
        featureFlagFake.setId(UUID.randomUUID());
        featureFlagFake.setName(flagName);
        featureFlagFake.setEnvironment(environment);
        featureFlagFake.setEnabled(false);
        featureFlagFake.setRolloutPercentage(100);

        when(featureFlagRepository.findByNameAndEnvironment(flagName, environment)).thenReturn(Optional.of(featureFlagFake));

        //ACT
        boolean result = featureFlagService.isEnabled(flagName, environment, userId);

        //ASSERT
        assertFalse(result);
    }

    @Test
    void shouldReturnTrue_WhenFlagIsEnabledAndRolloutIs100() {
        String flagName = "test-flag";
        String environment = "produzione";
        UUID userId = UUID.randomUUID();

        FeatureFlag featureFlagFake = new FeatureFlag();
        featureFlagFake.setId(UUID.randomUUID());
        featureFlagFake.setName(flagName);
        featureFlagFake.setEnvironment(environment);
        featureFlagFake.setEnabled(true);
        featureFlagFake.setRolloutPercentage(100);

        when(featureFlagRepository.findByNameAndEnvironment(flagName, environment)).thenReturn(Optional.of(featureFlagFake));

        //ACT
        boolean result = featureFlagService.isEnabled(flagName, environment, userId);

        //ASSERT
        assertTrue(result);
    }

    @Test
    void shouldReturnFalse_WhenUserIdIsNullAndRolloutIsLessThan100() {
        String flagName = "test-flag";
        String environment = "produzione";


        FeatureFlag featureFlagFake = new FeatureFlag();
        featureFlagFake.setId(UUID.randomUUID());
        featureFlagFake.setName(flagName);
        featureFlagFake.setEnvironment(environment);
        featureFlagFake.setEnabled(true);
        featureFlagFake.setRolloutPercentage(90);

        when(featureFlagRepository.findByNameAndEnvironment(flagName, environment)).thenReturn(Optional.of(featureFlagFake));

        //ACT
        boolean result = featureFlagService.isEnabled(flagName, environment, null);

        //ASSERT
        assertFalse(result);
    }


}
