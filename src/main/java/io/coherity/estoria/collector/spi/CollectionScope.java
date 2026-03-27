package io.coherity.estoria.collector.spi;

import java.util.Map;
import java.util.Optional;

public interface CollectionScope
{
    CloudProvider provider();
    Map<ScopeDimension, String> dimensions();
    default Optional<String> value(ScopeDimension dimension)
    {
        return Optional.ofNullable(dimensions().get(dimension));
    }
    Map<String, String> attributes();
}
