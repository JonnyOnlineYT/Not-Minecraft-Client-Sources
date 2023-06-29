package optifine;

import java.util.Map;

public class FileUploadThread extends Thread
{
    private String urlString;
    private Map headers;
    private byte[] content;
    private IFileUploadListener listener;

    public FileUploadThread(String p_i42_1_, Map p_i42_2_, byte[] p_i42_3_, IFileUploadListener p_i42_4_)
    {
        urlString = p_i42_1_;
        headers = p_i42_2_;
        content = p_i42_3_;
        listener = p_i42_4_;
    }

    public void run()
    {
        try
        {
            HttpUtils.post(urlString, headers, content);
            listener.fileUploadFinished(urlString, content, null);
        }
        catch (Exception exception)
        {
            listener.fileUploadFinished(urlString, content, exception);
        }
    }

    public String getUrlString()
    {
        return urlString;
    }

    public byte[] getContent()
    {
        return content;
    }

    public IFileUploadListener getListener()
    {
        return listener;
    }
}
