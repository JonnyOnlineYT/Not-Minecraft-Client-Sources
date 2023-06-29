package optifine;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class HttpPipelineSender extends Thread
{
    private HttpPipelineConnection httpPipelineConnection;
    private static final String CRLF = "\r\n";
    private static Charset ASCII = StandardCharsets.US_ASCII;

    public HttpPipelineSender(HttpPipelineConnection p_i59_1_)
    {
        super("HttpPipelineSender");
        httpPipelineConnection = p_i59_1_;
    }

    public void run()
    {
        HttpPipelineRequest httppipelinerequest = null;

        try
        {
            connect();

            while (!Thread.interrupted())
            {
                httppipelinerequest = httpPipelineConnection.getNextRequestSend();
                HttpRequest httprequest = httppipelinerequest.getHttpRequest();
                OutputStream outputstream = httpPipelineConnection.getOutputStream();
                writeRequest(httprequest, outputstream);
                httpPipelineConnection.onRequestSent(httppipelinerequest);
            }
        }
        catch (InterruptedException var4)
        {
            return;
        }
        catch (Exception exception)
        {
            httpPipelineConnection.onExceptionSend(httppipelinerequest, exception);
        }
    }

    private void connect() throws IOException
    {
        String s = httpPipelineConnection.getHost();
        int i = httpPipelineConnection.getPort();
        Proxy proxy = httpPipelineConnection.getProxy();
        Socket socket = new Socket(proxy);
        socket.connect(new InetSocketAddress(s, i), 5000);
        httpPipelineConnection.setSocket(socket);
    }

    private void writeRequest(HttpRequest p_writeRequest_1_, OutputStream p_writeRequest_2_) throws IOException
    {
        write(p_writeRequest_2_, p_writeRequest_1_.getMethod() + " " + p_writeRequest_1_.getFile() + " " + p_writeRequest_1_.getHttp() + "\r\n");
        Map<String, String> map = p_writeRequest_1_.getHeaders();

        for (String s : map.keySet())
        {
            String s1 = p_writeRequest_1_.getHeaders().get(s);
            write(p_writeRequest_2_, s + ": " + s1 + "\r\n");
        }

        write(p_writeRequest_2_, "\r\n");
    }

    private void write(OutputStream p_write_1_, String p_write_2_) throws IOException
    {
        byte[] abyte = p_write_2_.getBytes(ASCII);
        p_write_1_.write(abyte);
    }
}
