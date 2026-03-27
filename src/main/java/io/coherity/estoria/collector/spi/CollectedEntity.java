package io.coherity.estoria.collector.spi;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;

public interface CollectedEntity
{
    String id();
    String type();
    Optional<String> name();
    CollectionScope scope();
    Map<String, Object> attributes();
    Object rawPayload();
    Instant collectedAt();
}
