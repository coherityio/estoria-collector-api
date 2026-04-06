package io.coherity.estoria.collector.spi;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ProviderIdentifier
{
	@EqualsAndHashCode.Include
	private final String id;
	
	@EqualsAndHashCode.Include
	private final String version;
}