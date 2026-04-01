package io.coherity.estoria.collector.spi;

import java.net.URI;
import java.util.Map;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EndpointReference
{
    private String id;
    private URI uri;
    private Map<String, Object> metadata;
}
