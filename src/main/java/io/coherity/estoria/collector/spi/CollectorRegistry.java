package io.coherity.estoria.collector.spi;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CollectorRegistry
{
	void register(Collector collector);

	void registerAll(Collection<Collector> collectors);

	Set<String> getRegisteredEntityTypes();

	Optional<Collector> getRegisteredCollector(String entityType);

	boolean hasRegisteredCollector(String entityType);

	Set<Collector> getRegisteredCollectors();

	int size();

	Set<String> getDependencies(String entityType);

	Set<String> getKnownEntityTypesComplement(Set<String> entityTypes);

	Set<String> getKnownEntityTypes();

	Set<String> getUnresolvedEntityTypes();

	List<String> getExecutionOrder(String entityType) throws CircularReferenceException;
}