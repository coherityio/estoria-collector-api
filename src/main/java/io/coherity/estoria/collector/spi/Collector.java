package io.coherity.estoria.collector.spi;

public interface Collector
{
    CollectorDescriptor descriptor();
    CollectorCursor collect(CollectorRequest request) throws CollectorException;
}
