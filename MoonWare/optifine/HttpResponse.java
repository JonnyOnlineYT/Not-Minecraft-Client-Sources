package optifine;

import java.util.LinkedHashMap;
import java.util.Map;

public class HttpResponse
{
    private int status;
    private String statusLine;
    private Map<String, String> headers = new LinkedHashMap<String, String>();
    private byte[] body;

    public HttpResponse(int p_i61_1_, String p_i61_2_, Map p_i61_3_, byte[] p_i61_4_)
    {
        status = p_i61_1_;
        statusLine = p_i61_2_;
        headers = p_i61_3_;
        body = p_i61_4_;
    }

    public int getStatus()
    {
        return status;
    }

    public String getStatusLine()
    {
        return statusLine;
    }

    public Map getHeaders()
    {
        return headers;
    }

    public String getHeader(String p_getHeader_1_)
    {
        return headers.get(p_getHeader_1_);
    }

    public byte[] getBody()
    {
        return body;
    }
}
