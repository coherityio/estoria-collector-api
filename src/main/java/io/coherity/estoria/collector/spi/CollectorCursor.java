package io.coherity.estoria.collector.spi;

import java.util.List;
import java.util.Optional;

public interface CollectorCursor
{
    List<CloudEntity> getEntities();
    Optional<String> getNextCursorToken();
    CursorMetadata getMetadata();
}
