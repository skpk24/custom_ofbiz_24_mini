package org.apache.ofbiz.camel.route;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.Exchange;
import org.apache.ofbiz.base.util.Debug;

public class DemoRoute extends RouteBuilder {
    private static final String MODULE = DemoRoute.class.getName();

    @Override
    public void configure() throws Exception {
        Debug.logInfo("\n*********************************\n RouteBuilder added inside configure()  \n*********************************\n", MODULE);

        from("file-watch://plugins/camel-integration/data/")
                .convertBodyTo(String.class)
                .setHeader("CamelOfbiz.Parameters.note", body())
                .setHeader("CamelOfbiz.Parameters.noteName", header(Exchange.FILE_NAME))
                .to("ofbiz://createNote?dispatcher=#dispatcher");
                //.to("file://plugins/camel-integration/data1/");
        from("direct:yourCamelRoute")
                .to("file://plugins/camel-integration/data/");
        from("direct:yourCamelRoute1")
                .to("stream:out");
        Debug.logInfo("\n*********************************\n RouteBuilder added inside  \n*********************************\n", MODULE);
    }
}
