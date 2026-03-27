package io.coherity.estoria.collector.spi;

public class ProviderException extends RuntimeException
{
    private static final long serialVersionUID = 1L;
    
	public ProviderException(String message)
    {
        super(message);
    }
    public ProviderException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
