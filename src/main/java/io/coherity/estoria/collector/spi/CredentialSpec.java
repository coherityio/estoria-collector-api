package io.coherity.estoria.collector.spi;

import java.util.Map;

public interface CredentialSpec
{
    String type();
    Map<String, Object> attributes();
}
