package io.coherity.estoria.collector.api;

import java.util.Optional;

import io.coherity.estoria.collector.spi.Collector;

public interface ProviderSession extends AutoCloseable
{
    CloudProvider provider();
    Optional<Collector> createCollector(String collectorId) throws ProviderException;
    EndpointResolver endpointResolver();
    <T> T getService(Class<T> serviceType) throws ProviderException;
    @Override
    void close();
}
