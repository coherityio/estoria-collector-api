package io.coherity.estoria.collector.spi;

import java.util.Map;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CollectionScope
{
    private CloudProvider provider;
    private Map<String, String> attributes;
//    private Map<ScopeDimension, String> dimensions;
//    public Optional<String> getValue(ScopeDimension dimension)
//    {
//        return Optional.ofNullable(dimensions.get(dimension));
//    }
}
