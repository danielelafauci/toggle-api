package com.toggleapi.toggleapi.featureflag;


import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.cache.annotation.Cacheable;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface FeatureFlagRepository extends JpaRepository<FeatureFlag, UUID> {

    @Cacheable(value = "flags", key = "#name + '_' + #environment")
    Optional<FeatureFlag> findByNameAndEnvironment(String name, String environment);




}
