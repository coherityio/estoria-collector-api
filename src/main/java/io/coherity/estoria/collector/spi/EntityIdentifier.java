package io.coherity.estoria.collector.spi;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EntityIdentifier
{
    private String id;
    private String qualifiedResourceName;
}
