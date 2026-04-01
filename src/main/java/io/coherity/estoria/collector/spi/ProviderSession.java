package io.coherity.estoria.collector.spi;

public interface ProviderSession extends AutoCloseable
{
    CloudProvider getProvider();
    EndpointResolver getEndpointResolver();
    <T> T getService(Class<T> serviceType) throws ProviderException;
    @Override
    void close();
}
