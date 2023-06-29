package optifine;

import net.minecraft.client.Minecraft;

import java.net.Proxy;

public class FileDownloadThread extends Thread
{
    private String urlString;
    private IFileDownloadListener listener;

    public FileDownloadThread(String p_i41_1_, IFileDownloadListener p_i41_2_)
    {
        urlString = p_i41_1_;
        listener = p_i41_2_;
    }

    public void run()
    {
        try
        {
            byte[] abyte = HttpPipeline.get(urlString, Proxy.NO_PROXY);
            listener.fileDownloadFinished(urlString, abyte, null);
        }
        catch (Exception exception)
        {
            listener.fileDownloadFinished(urlString, null, exception);
        }
    }

    public String getUrlString()
    {
        return urlString;
    }

    public IFileDownloadListener getListener()
    {
        return listener;
    }
}
