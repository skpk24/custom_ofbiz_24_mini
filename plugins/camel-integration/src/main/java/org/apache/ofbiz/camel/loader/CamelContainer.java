package org.apache.ofbiz.camel.loader;

import org.apache.ofbiz.base.container.Container;
import org.apache.ofbiz.base.container.ContainerException;
import org.apache.ofbiz.base.start.StartupCommand;
import java.util.List;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.engine.DefaultPackageScanClassResolver;
import org.apache.camel.spi.PackageScanClassResolver;
import org.apache.ofbiz.base.container.ContainerConfig;
import org.apache.camel.support.SimpleRegistry;
import org.apache.ofbiz.service.LocalDispatcher;
import org.apache.ofbiz.entity.DelegatorFactory;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.service.ServiceContainer;
import java.util.Set;
import org.apache.camel.builder.RouteBuilder;
import org.apache.ofbiz.base.util.Debug;

public class CamelContainer implements Container {

    private static final String MODULE = CamelContainer.class.getName();
    private static ProducerTemplate producerTemplate;
    private CamelContext context;
    private String name;
    private boolean started = false;

    @Override
    public void init(List<StartupCommand> ofbizCommands, String name, String configFile) throws ContainerException {
        this.name = name;
        context = createCamelContext();
        ContainerConfig.Configuration cfg = ContainerConfig.getConfiguration(name, configFile);
        String packageName = ContainerConfig.getPropertyValue(cfg, "package", "org.apache.ofbiz.camel.route");
        PackageScanClassResolver packageResolver = new DefaultPackageScanClassResolver();
        Set<Class<?>> routesClassesSet = packageResolver.findImplementations(RouteBuilder.class, packageName);
        routesClassesSet.forEach(key -> {
            RouteBuilder routeBuilder;
            try {
                routeBuilder = createRoutes(key.getName());
                Debug.logInfo("Adding the route to the context:  ", MODULE);
                addRoutesToContext(routeBuilder);
            } catch (ContainerException e) {

            }
        });

        producerTemplate = context.createProducerTemplate();
    }

    @Override
    public boolean start() throws ContainerException {
        Debug.logInfo("Starting camel container", MODULE);

        try {
            context.start();
        } catch (Exception e) {
            throw new ContainerException(e);
        }
        return true;
    }

    @Override
    public void stop() throws ContainerException {
        Debug.logInfo("Stopping camel container", MODULE);

        try {
            context.stop();
        } catch (Exception e) {
            throw new ContainerException(e);
        }
    }

    @Override
    public String getName() {
        return name;
    }

    private DefaultCamelContext createCamelContext() throws ContainerException {
        LocalDispatcher dispatcher = createDispatcher();
        SimpleRegistry registry = new SimpleRegistry();
        registry.bind("dispatcher", dispatcher);
        //registry.put("dispatcher", dispatcher);
        return new DefaultCamelContext(registry);
    }

    private LocalDispatcher createDispatcher() throws ContainerException {
        Delegator delegator = DelegatorFactory.getDelegator("default");
        return ServiceContainer.getLocalDispatcher("camel-dispatcher", delegator);
//        return dispatcherFactory.createLocalDispatcher("camel-dispatcher", delegator);
    }

    private RouteBuilder createRoutes(String routeBuilderClassName) throws ContainerException {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        try {
            Class<?> c = loader.loadClass(routeBuilderClassName);
            return (RouteBuilder) c.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            Debug.logError(e, "Cannot get instance of the camel route builder: " + routeBuilderClassName, MODULE);
            throw new ContainerException(e);
        }
    }

    private void addRoutesToContext(RouteBuilder routeBuilder) throws ContainerException {
        try {
            context.addRoutes(routeBuilder);
        } catch (Exception e) {
            Debug.logError(e, "Cannot add routes: " + routeBuilder, MODULE);
            throw new ContainerException(e);
        }
    }

    public static ProducerTemplate getProducerTemplate() {
        return producerTemplate;
    }

    public static void setProducerTemplate(ProducerTemplate producerTemplate) {
        CamelContainer.producerTemplate = producerTemplate;
    }
}
