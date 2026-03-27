package io.coherity.estoria.collector.spi;

import java.net.URI;
import java.util.Map;

public interface EndpointReference
{
    String id();
    URI uri();
    Map<String, Object> metadata();
}
