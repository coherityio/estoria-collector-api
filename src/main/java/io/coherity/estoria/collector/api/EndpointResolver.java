package io.coherity.estoria.collector.api;

import java.util.Optional;

import io.coherity.estoria.collector.spi.CollectionScope;

public interface EndpointResolver
{
    Optional<EndpointReference> resolve(String serviceId, CollectionScope scope);
}
