package io.coherity.estoria.collector.spi;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class CollectorInfo
{
	@EqualsAndHashCode.Include
	private String entityType;
	private String providerId;
	private Set<String> requiredEntityTypes;
	private Set<String> tags;
	private Set<String> supportedContextAttributes;
}