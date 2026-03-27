package io.coherity.estoria.collector.spi;

import java.util.Map;

public interface ProviderContext
{
    CollectionScope scope();
    Map<String, Object> settings();
    CredentialSpec credentialSpec();
}
