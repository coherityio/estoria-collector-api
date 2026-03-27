package io.coherity.estoria.collector.api;

import java.util.Map;

public interface CredentialSpec
{
    String type();
    Map<String, Object> attributes();
}
