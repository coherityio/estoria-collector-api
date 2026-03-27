package io.coherity.estoria.collector.api;

import java.util.Map;
import java.util.Set;

public interface ProviderMetadata
{
    String version();
    Set<String> supportedEntityTypes();
    Map<String, Object> attributes();
}
