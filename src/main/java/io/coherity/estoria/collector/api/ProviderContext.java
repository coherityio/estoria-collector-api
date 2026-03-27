package io.coherity.estoria.collector.api;

import java.util.Map;

import io.coherity.estoria.collector.spi.CollectionScope;

public interface ProviderContext
{
    CollectionScope scope();
    Map<String, Object> settings();
    CredentialSpec credentialSpec();
}
