package optifine;

public class HttpPipelineRequest
{
    private HttpRequest httpRequest;
    private HttpListener httpListener;
    private boolean closed;

    public HttpPipelineRequest(HttpRequest p_i58_1_, HttpListener p_i58_2_)
    {
        httpRequest = p_i58_1_;
        httpListener = p_i58_2_;
    }

    public HttpRequest getHttpRequest()
    {
        return httpRequest;
    }

    public HttpListener getHttpListener()
    {
        return httpListener;
    }

    public boolean isClosed()
    {
        return closed;
    }

    public void setClosed(boolean p_setClosed_1_)
    {
        closed = p_setClosed_1_;
    }
}
