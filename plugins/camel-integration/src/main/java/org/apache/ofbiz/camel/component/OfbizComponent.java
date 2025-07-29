package org.apache.ofbiz.camel.component;

import java.util.Map;
import org.apache.camel.Endpoint;
import org.apache.camel.support.DefaultComponent;
import org.apache.ofbiz.base.util.Debug;

/**
 * @author skpk2
 *
 */
public class OfbizComponent extends DefaultComponent {
    private static final String MODULE = OfbizComponent.class.getName();
    /**
     * This is a Javadoc
     */
    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
        Debug.logInfo("\n*********************************\n uri :  " + uri + "\n*********************************\n", MODULE);
        Debug.logInfo("\n*********************************\n remaining :  " + remaining + "\n*********************************\n", MODULE);
        Debug.logInfo("\n*********************************\n parameters :  " + parameters + "\n*********************************\n", MODULE);
        OfbizEndpoint endpoint = new OfbizEndpoint(uri, this, remaining);
        setProperties(endpoint, parameters);
        if (endpoint.getDispatcher() == null) {
            throw new IllegalArgumentException("dispatcher must be specified");
        }

        return endpoint;
    }
}
