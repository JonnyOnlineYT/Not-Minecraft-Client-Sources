package optifine;

import java.net.Proxy;
import java.util.LinkedHashMap;
import java.util.Map;

public class HttpRequest
{
    private String host;
    private int port;
    private Proxy proxy = Proxy.NO_PROXY;
    private String method;
    private String file;
    private String http;
    private Map<String, String> headers = new LinkedHashMap<String, String>();
    private byte[] body;
    private int redirects;
    public static final String METHOD_GET = "GET";
    public static final String METHOD_HEAD = "HEAD";
    public static final String METHOD_POST = "POST";
    public static final String HTTP_1_0 = "HTTP/1.0";
    public static final String HTTP_1_1 = "HTTP/1.1";

    public HttpRequest(String p_i60_1_, int p_i60_2_, Proxy p_i60_3_, String p_i60_4_, String p_i60_5_, String p_i60_6_, Map<String, String> p_i60_7_, byte[] p_i60_8_)
    {
        host = p_i60_1_;
        port = p_i60_2_;
        proxy = p_i60_3_;
        method = p_i60_4_;
        file = p_i60_5_;
        http = p_i60_6_;
        headers = p_i60_7_;
        body = p_i60_8_;
    }

    public String getHost()
    {
        return host;
    }

    public int getPort()
    {
        return port;
    }

    public String getMethod()
    {
        return method;
    }

    public String getFile()
    {
        return file;
    }

    public String getHttp()
    {
        return http;
    }

    public Map<String, String> getHeaders()
    {
        return headers;
    }

    public byte[] getBody()
    {
        return body;
    }

    public int getRedirects()
    {
        return redirects;
    }

    public void setRedirects(int p_setRedirects_1_)
    {
        redirects = p_setRedirects_1_;
    }

    public Proxy getProxy()
    {
        return proxy;
    }
}
