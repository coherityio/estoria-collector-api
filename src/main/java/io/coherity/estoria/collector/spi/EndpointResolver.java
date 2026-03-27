package io.coherity.estoria.collector.spi;

import java.util.Optional;

public interface EndpointResolver
{
    Optional<EndpointReference> resolve(String serviceId, CollectionScope scope);
}
