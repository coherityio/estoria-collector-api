package io.coherity.estoria.collector.spi;

import java.util.Optional;

public interface ProviderSession extends AutoCloseable
{
    CloudProvider provider();
    Optional<Collector> createCollector(String collectorId) throws ProviderException;
    EndpointResolver endpointResolver();
    <T> T getService(Class<T> serviceType) throws ProviderException;
    @Override
    void close();
}
