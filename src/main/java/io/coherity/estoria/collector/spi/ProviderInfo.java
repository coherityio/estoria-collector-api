package io.coherity.estoria.collector.spi;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProviderInfo
{
	private ProviderIdentifier providerIdentifier;
//	@EqualsAndHashCode.Include
//	private String id;
//	@EqualsAndHashCode.Include
//	private String version;
	
	private String name;
	private Map<String, Object> attributes;
	
	
	public String getProviderId()
	{
		if(this.providerIdentifier != null)
		{
			return providerIdentifier.getId();
		}
		return "";
	}
}