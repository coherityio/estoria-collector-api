package io.coherity.estoria.collector.spi;

import java.util.Collection;

public interface CloudProvider
{
    String id();
    String displayName();
    ProviderMetadata metadata();
    Collection<CollectorDescriptor> collectors();
    ProviderSession openSession(ProviderContext context) throws ProviderException;
}
