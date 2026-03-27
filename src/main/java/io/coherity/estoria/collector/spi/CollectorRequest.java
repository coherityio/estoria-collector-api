package io.coherity.estoria.collector.spi;

import java.util.Map;
import java.util.Optional;

public interface CollectorRequest
{
    int pageSize();
    Optional<String> cursorToken();
    CollectionScope scope();
    Map<String, Object> filters();
    Map<String, Object> options();
}
