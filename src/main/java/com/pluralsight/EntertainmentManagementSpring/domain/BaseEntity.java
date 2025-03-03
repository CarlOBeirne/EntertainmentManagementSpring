package com.pluralsight.EntertainmentManagementSpring.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@SuperBuilder(toBuilder = true)

public abstract class BaseEntity {
    @EqualsAndHashCode.Include
    private Long id;
}