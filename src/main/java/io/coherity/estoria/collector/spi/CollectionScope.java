package io.coherity.estoria.collector.spi;

import java.util.Map;
import java.util.Optional;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CollectionScope
{
    private CloudProvider provider;
    private Map<ScopeDimension, String> dimensions;
    private Map<String, String> attributes;
    public Optional<String> getValue(ScopeDimension dimension)
    {
        return Optional.ofNullable(dimensions.get(dimension));
    }
}
