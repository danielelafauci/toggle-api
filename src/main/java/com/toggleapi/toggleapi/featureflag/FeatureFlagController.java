package com.toggleapi.toggleapi.featureflag;



import com.project.toggleapi.openApi.generated.api.FeatureFlagApi;
import com.project.toggleapi.openApi.generated.model.FlagStatusResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class FeatureFlagController implements FeatureFlagApi {

    private final FeatureFlagService featureFlagService;

    @Override
    public ResponseEntity<FlagStatusResponse> getFlagStatus(String flagName, String environment, UUID userId)  {
        boolean isEnabled = featureFlagService.isEnabled(flagName, environment, userId);
        FlagStatusResponse flagStatusResponse = new FlagStatusResponse();
        flagStatusResponse.setEnabled(isEnabled);
        return ResponseEntity.ok(flagStatusResponse);

    }
}
