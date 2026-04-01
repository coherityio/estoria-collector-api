package io.coherity.estoria.collector.spi;

import java.util.Map;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public abstract class CloudProvider
{
	@EqualsAndHashCode.Include
	private final String id;
	
	@EqualsAndHashCode.Include
	private final String version;
	
	private final String name;
	
	private final Map<String, Object> attributes;
	
	public abstract ProviderSession openSession(ProviderContext context) throws ProviderException;
}
