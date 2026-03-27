package io.coherity.estoria.collector.spi;

import java.util.Set;

public interface CollectorDescriptor
{
    String id();
    String displayName();
    String entityType();
    Set<String> tags();
}
