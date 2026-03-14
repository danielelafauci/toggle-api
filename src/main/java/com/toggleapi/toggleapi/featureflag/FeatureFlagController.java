package com.toggleapi.toggleapi.featureflag;


import com.project.toggleapi.openApi.generated.api.FeatureFlagApi;
import com.project.toggleapi.openApi.generated.model.CreateFlagRequest;
import com.project.toggleapi.openApi.generated.model.FeatureFlagResponse;
import com.project.toggleapi.openApi.generated.model.FlagStatusResponse;
import com.project.toggleapi.openApi.generated.model.PagedFeatureFlagResponse;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class FeatureFlagController implements FeatureFlagApi {

    private final FeatureFlagService featureFlagService;

    @Override
    public ResponseEntity<FlagStatusResponse> getFlagStatus(String flagName, String environment, UUID userId) {
        boolean isEnabled = featureFlagService.isEnabled(flagName, environment, userId);
        FlagStatusResponse flagStatusResponse = new FlagStatusResponse();
        flagStatusResponse.setEnabled(isEnabled);
        return ResponseEntity.ok(flagStatusResponse);

    }

    @Override
    public ResponseEntity<FeatureFlagResponse> createFlag(CreateFlagRequest createFlagRequest) {
        FeatureFlagResponse response = featureFlagService.createFlag(createFlagRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @Override
    public ResponseEntity<PagedFeatureFlagResponse> getAllFlags(Integer page, Integer size) {
        PagedFeatureFlagResponse response = featureFlagService.getAllFlags(page, size);
        return ResponseEntity.ok(response);

    }

}
