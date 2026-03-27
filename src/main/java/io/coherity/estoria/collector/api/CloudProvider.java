package io.coherity.estoria.collector.api;

import java.util.Collection;

import io.coherity.estoria.collector.spi.CollectorDescriptor;

public interface CloudProvider
{
    String id();
    String displayName();
    ProviderMetadata metadata();
    Collection<CollectorDescriptor> collectors();
    ProviderSession openSession(ProviderContext context) throws ProviderException;
}
