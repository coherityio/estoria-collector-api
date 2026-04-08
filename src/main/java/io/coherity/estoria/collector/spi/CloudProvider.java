package io.coherity.estoria.collector.spi;

import java.util.Optional;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public abstract class CloudProvider
{
	//private ProviderIdentifier providerIdentifier;
	
	private final ProviderInfo providerInfo;
	
//	@EqualsAndHashCode.Include
//	private final String id;
//	
//	@EqualsAndHashCode.Include
//	private final String version;
//	
//	private final String name;
//	
//	private final Map<String, Object> attributes;
	
	private final CollectorRegistry collectorRegistry;
	
	public abstract Optional<Collector> getConnectedCollector(String entityType) throws ProviderException;
	
	//public abstract ProviderSession openSession(ProviderContext context) throws ProviderException;
	
//	public abstract Optional<CollectorRegistry> getLoadedCollectorRegistry();
//	public abstract Set<Collector> getCollectors() throws ProviderException;
//	public abstract Optional<Collector> getCollector(String entityType) throws ProviderException;
}
