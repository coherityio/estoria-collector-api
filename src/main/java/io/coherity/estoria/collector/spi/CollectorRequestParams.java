package io.coherity.estoria.collector.spi;

import java.util.Map;
import java.util.Optional;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CollectorRequestParams
{
	public static final int PAGE_SIZE_ALL = -1;
    private int pageSize;
    private Optional<String> cursorToken;
    private Map<String, Object> filters;
    private Map<String, Object> options;
//    private ProviderContext providerContext;
//    private CollectorContext collectorContext;
    
    public CollectorRequestParams pageSizeAll()
    {
    	this.pageSize = PAGE_SIZE_ALL;
    	return this;
    }
}
