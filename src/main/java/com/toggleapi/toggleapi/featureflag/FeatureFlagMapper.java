package com.toggleapi.toggleapi.featureflag;

import com.project.toggleapi.openApi.generated.model.CreateFlagRequest;
import com.project.toggleapi.openApi.generated.model.FeatureFlagResponse;
import com.project.toggleapi.openApi.generated.model.PagedFeatureFlagResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Mapper(componentModel = "spring")
public interface FeatureFlagMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    FeatureFlag toEntity(CreateFlagRequest createFlagRequest);

    FeatureFlagResponse toResponse(FeatureFlag featureFlag);

    @Mapping(target = "content", source = "content")
    @Mapping(target = "totalElements", source = "totalElements")
    @Mapping(target = "totalPages", source = "totalPages")
    @Mapping(target = "last", source = "last")
    PagedFeatureFlagResponse toPagedResponse(Page<FeatureFlag> page);

    default OffsetDateTime toOffsetDateTime(LocalDateTime localDateTime) {
        return localDateTime == null ? null : localDateTime.atOffset(ZoneOffset.UTC);
    }




}
