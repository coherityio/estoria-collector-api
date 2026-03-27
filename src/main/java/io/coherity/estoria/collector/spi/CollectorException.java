package io.coherity.estoria.collector.spi;

public class CollectorException extends RuntimeException
{
    private static final long serialVersionUID = 1L;
    
	public CollectorException(String message)
    {
        super(message);
    }
    public CollectorException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
