package com.havens.siamese.service;

import com.havens.siamese.Service;
import com.havens.siamese.dfa.DfaTool;
import com.havens.siamese.server.Server;
import com.havens.siamese.server.WorldManager;
import org.json.JSONObject;

/**
 * Created by havens on 16-5-14.
 */
public class StartupService extends Service {

    public void create(Server server) throws Exception {
        // startup
        DfaTool.loadJsonData();
        WorldManager.getInstance(server);
    }

    public void filter(JSONObject jObject) throws Exception {

    }
}
