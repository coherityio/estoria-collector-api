package io.coherity.estoria.collector.spi;

import java.time.Instant;
import java.util.Map;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CloudEntity
{
	private EntityIdentifier entityIdentifier;
    private String entityType;
    private String name;
    private CollectorContext collectorContext;
    private Map<String, Object> attributes;
    private Object rawPayload;
    private Instant collectedAt;
}
