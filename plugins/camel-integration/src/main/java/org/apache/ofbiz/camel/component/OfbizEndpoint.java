/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.ofbiz.camel.component;

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.support.DefaultEndpoint;
import org.apache.ofbiz.service.LocalDispatcher;
import org.apache.ofbiz.base.util.Debug;

/**
 * Represents a Ofbiz endpoint.
 */
public class OfbizEndpoint extends DefaultEndpoint {
    private static final String MODULE = OfbizEndpoint.class.getName();
    private String remaining;
    private LocalDispatcher dispatcher;

    public OfbizEndpoint(String uri, OfbizComponent component, String remaining) {
        super(uri, component);
        this.remaining = remaining;
    }
    /**
     * This is a Javadoc
     */
    public Producer createProducer() throws Exception {
        Debug.logInfo("\n*********************************\n remaining :  " + remaining + "\n*********************************\n", MODULE);
        return new OfbizProducer(this, remaining);
    }
    /**
     * This is a Javadoc
     */
    public Consumer createConsumer(Processor processor) throws Exception {
        Debug.logInfo("\n*********************************\n processor :  " + processor + "\n*********************************\n", MODULE);
        throw new UnsupportedOperationException("Consumer not supported for Ofbiz endpoint");
    }
    /**
     * This is a Javadoc
     */
    public boolean isSingleton() {
        return true;
    }
    /**
     * This is a Javadoc
     */
    public LocalDispatcher getDispatcher() {
        return dispatcher;
    }
    /**
     * This is a Javadoc
     */
    public void setDispatcher(LocalDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }
}
