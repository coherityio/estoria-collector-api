package io.coherity.estoria.collector.spi;

public interface Collector
{
	CollectorInfo getCollectorInfo();
	CollectorCursor collect(ProviderContext providerContext, CollectorContext collectorContext, CollectorRequestParams requestParams) throws CollectorException;
}
