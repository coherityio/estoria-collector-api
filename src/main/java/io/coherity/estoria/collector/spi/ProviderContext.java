package io.coherity.estoria.collector.spi;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
//@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class ProviderContext
{
//	@EqualsAndHashCode.Include
//	private String providerId;
    private Map<String, Object> attributes;
}
