package io.coherity.estoria.collector.spi;

import java.util.Map;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProviderContext
{
    private CollectionScope scope;
    private Map<String, Object> configuration;
}
