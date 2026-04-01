package io.coherity.estoria.collector.spi;

import java.util.Set;

public interface Collector
{
	String getProviderId();
	String getEntityType();
	Set<String> requiresEntityTypes();
	Set<String> getTags();
	CollectorCursor collect(CollectorRequest request) throws CollectorException;
}
