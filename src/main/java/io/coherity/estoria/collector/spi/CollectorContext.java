package io.coherity.estoria.collector.spi;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CollectorContext
{
    //private String providerId;
    private Map<String, Object> attributes;
}
