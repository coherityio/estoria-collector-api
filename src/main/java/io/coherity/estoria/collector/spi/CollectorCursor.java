package io.coherity.estoria.collector.spi;

import java.util.List;
import java.util.Optional;

public interface CollectorCursor
{
    List<CollectedEntity> entities();
    Optional<String> nextCursorToken();
    CursorMetadata metadata();
}
