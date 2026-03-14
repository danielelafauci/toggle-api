package com.toggleapi.toggleapi.featureflag;


import com.project.toggleapi.openApi.generated.model.CreateFlagRequest;
import com.project.toggleapi.openApi.generated.model.FeatureFlagResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FeatureFlagServiceTest {

    @Mock
    private FeatureFlagRepository featureFlagRepository;

    @Mock
    private FeatureFlagMapper featureFlagMapper;

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

    @Test
    void shouldCreateFlagSuccessfully() {
        CreateFlagRequest request = new CreateFlagRequest();
        request.setName("nuova-dashboard");
        request.setEnvironment("produzione");

        FeatureFlag entity = new FeatureFlag();
        FeatureFlag savedEntity = new FeatureFlag();
        FeatureFlagResponse expectedResponse = new FeatureFlagResponse();
        expectedResponse.setId(UUID.randomUUID());

        when(featureFlagRepository.findByNameAndEnvironment("nuova-dashboard", "produzione")).thenReturn(Optional.empty());

        when(featureFlagMapper.toEntity(request)).thenReturn(entity);
        when(featureFlagRepository.save(entity)).thenReturn(savedEntity);
        when(featureFlagMapper.toResponse(savedEntity)).thenReturn(expectedResponse);

        FeatureFlagResponse actualResponse = featureFlagService.createFlag(request);

        assertNotNull(actualResponse);
        assertEquals(expectedResponse.getId(), actualResponse.getId());

        verify(featureFlagRepository, times(1)).save(entity);
    }

    @Test
    void shouldThrowExceptionWhenFlagAlreadyExists() {

        CreateFlagRequest request = new CreateFlagRequest();
        request.setName("nuova-dashboard");
        request.setEnvironment("produzione");

        FeatureFlag existingFlag = new FeatureFlag();


        when(featureFlagRepository.findByNameAndEnvironment("nuova-dashboard", "produzione"))
                .thenReturn(Optional.of(existingFlag));


        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            featureFlagService.createFlag(request);
        });

        assertEquals("Un flag con il nome 'nuova-dashboard' esiste già nell'ambiente 'produzione'.", exception.getMessage());


        verify(featureFlagRepository, never()).save(any());
    }




}
