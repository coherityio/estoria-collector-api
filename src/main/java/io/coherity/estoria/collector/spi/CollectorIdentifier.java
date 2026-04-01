package io.coherity.estoria.collector.spi;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CollectorIdentifier
{
    private String providerId;
    private String entityType;
}
