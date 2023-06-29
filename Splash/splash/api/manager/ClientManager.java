package splash.api.manager;

import splash.Splash;
import splash.api.module.Module;

import java.util.ArrayList;

/**
 * Author: Ice
 * Created: 00:23, 30-May-20
 * Project: Client
 */
public abstract class ClientManager<T> {

    private ArrayList<T> managerContent = new ArrayList<>();

    public abstract String managerName();

    public ArrayList<T> getContents() {
        return managerContent;
    }

    public void addContent(T content) {
        managerContent.add(content);
    }
}
