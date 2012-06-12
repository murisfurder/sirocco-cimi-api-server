/**
 *
 * SIROCCO
 * Copyright (C) 2011 France Telecom
 * Contact: sirocco@ow2.org
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
 * USA
 *
 * $Id$
 *
 */
package org.ow2.sirocco.apis.rest.cimi.request;

import java.util.LinkedList;

import org.ow2.sirocco.apis.rest.cimi.configuration.AppConfig;
import org.ow2.sirocco.apis.rest.cimi.configuration.ConfigFactory;
import org.ow2.sirocco.apis.rest.cimi.configuration.ConfigurationException;
import org.ow2.sirocco.apis.rest.cimi.configuration.ItemConfig;
import org.ow2.sirocco.apis.rest.cimi.converter.CimiConverter;
import org.ow2.sirocco.apis.rest.cimi.domain.CimiExchange;
import org.ow2.sirocco.apis.rest.cimi.domain.CimiResource;
import org.ow2.sirocco.apis.rest.cimi.domain.ExchangeType;

/**
 * .
 */
public class CimiContextImpl implements CimiContext {

    private CimiRequest request;

    private CimiResponse response;

    private boolean convertedWriteOnly;

    private LinkedList<Class<?>> stackConverted;

    /**
     * Set constructor.
     * 
     * @param request The current request
     * @param response The current response
     */
    public CimiContextImpl(final CimiRequest request, final CimiResponse response) {
        super();
        this.request = request;
        this.response = response;
        this.stackConverted = new LinkedList<Class<?>>();
        this.convertedWriteOnly = false;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.ow2.sirocco.apis.rest.cimi.request.CimiContext#getRequest()
     */
    @Override
    public CimiRequest getRequest() {
        return this.request;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.ow2.sirocco.apis.rest.cimi.request.CimiContext#getResponse()
     */
    @Override
    public CimiResponse getResponse() {
        return this.response;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.ow2.sirocco.apis.rest.cimi.utils.CimiContext#getConverter(java.lang
     *      .Class)
     */
    @Override
    public CimiConverter getConverter(final Class<?> klass) {
        CimiConverter converter;
        try {
            ItemConfig item = AppConfig.getInstance().getConfig().find(klass);
            converter = (CimiConverter) item.getData(ConfigFactory.CONVERTER);
        } catch (Exception e) {
            throw new ConfigurationException("CimiConverter not found in configuration for " + klass.getName());
        }
        return converter;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.ow2.sirocco.apis.rest.cimi.request.CimiContext#convertToCimi(java.lang.Object,
     *      java.lang.Class)
     */
    @Override
    public Object convertToCimi(final Object service, final Class<?> cimiAssociate) {
        this.stackConverted.clear();
        return this.convertNextCimi(service, cimiAssociate);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.ow2.sirocco.apis.rest.cimi.request.CimiContext#convertNextCimi(java.lang.Object,
     *      java.lang.Class)
     */
    @Override
    public Object convertNextCimi(final Object service, final Class<?> cimiAssociate) {
        Object converted = null;
        this.stackConverted.push(cimiAssociate);
        converted = this.getConverter(cimiAssociate).toCimi(this, service);
        this.stackConverted.pop();
        return converted;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.ow2.sirocco.apis.rest.cimi.request.CimiContext#convertToService(java.lang.Object)
     */
    @Override
    public Object convertToService(final Object cimi) {
        this.stackConverted.clear();
        return this.convertNextService(cimi);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.ow2.sirocco.apis.rest.cimi.request.CimiContext#convertNextService(java.lang.Object)
     */
    @Override
    public Object convertNextService(final Object cimi) {
        return this.convertNextService(cimi, cimi.getClass());
    }

    @Override
    public Object convertNextService(final Object cimi, final Class<?> cimiToUse) {
        Object converted = null;
        this.stackConverted.push(cimiToUse);
        converted = this.getConverter(cimiToUse).toService(this, cimi);
        this.stackConverted.pop();
        return converted;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.ow2.sirocco.apis.rest.cimi.utils.CimiContext#mustBeExpanded(org
     *      .ow2.sirocco.apis.rest.cimi.domain.CimiResource)
     */
    @Override
    public boolean mustBeExpanded(final CimiResource resource) {
        boolean expand = true;
        ExchangeType typeCurrent = this.getType(resource);
        ExchangeType typeRoot = this.getType(this.getRootConverting());
        if (typeRoot != typeCurrent) {
            switch (typeRoot) {
            case CloudEntryPoint:
            case CredentialsCollection:
            case CredentialsTemplateCollection:
            case JobCollection:
            case MachineCollection:
            case MachineConfigurationCollection:
            case MachineImageCollection:
            case MachineTemplateCollection:
                expand = false;
                break;
            default:
                break;
            }
        }
        return expand;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.ow2.sirocco.apis.rest.cimi.utils.CimiContext#mustBeReferenced(org
     *      .ow2.CimiResource)
     */
    @Override
    public boolean mustBeReferenced(final CimiResource resource) {
        boolean reference = false;
        ExchangeType typeCurrent = this.getType(resource);
        ExchangeType typeRoot = this.getType(this.getRootConverting());
        if (typeRoot != typeCurrent) {
            switch (typeRoot) {
            case CloudEntryPoint:
            case CredentialsCollection:
            case CredentialsTemplateCollection:
            case JobCollection:
            case MachineCollection:
            case MachineConfigurationCollection:
            case MachineImageCollection:
            case MachineTemplateCollection:
                reference = true;
                break;
            default:
                break;
            }
        }
        return reference;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.ow2.sirocco.apis.rest.cimi.request.CimiContext#mustHaveIdInReference(org.ow2.sirocco.apis.rest.cimi.domain.CimiData)
     */
    @Override
    public boolean mustHaveIdInReference(final Class<? extends CimiResource> classResource) {
        boolean withId = true;
        switch (this.getType(classResource)) {
        case CloudEntryPoint:
        case CredentialsCollection:
        case CredentialsTemplateCollection:
        case JobCollection:
        case MachineCollection:
        case MachineConfigurationCollection:
        case MachineImageCollection:
        case MachineTemplateCollection:
            withId = false;
            break;
        default:
            break;
        }
        return withId;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.ow2.sirocco.apis.rest.cimi.utils.CimiContext#makeHref(CimiResource,
     *      java.lang.String)
     */
    @Override
    public String makeHrefBase(final CimiResource data) {
        return this.makeHref(data, null);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.ow2.sirocco.apis.rest.cimi.utils.CimiContext#makeHref(CimiResource,
     *      java.lang.String)
     */
    @Override
    public String makeHref(final CimiResource data, final String id) {
        return this.makeHref(data.getClass(), id);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.ow2.sirocco.apis.rest.cimi.request.CimiContext#makeHref(java.lang.Class,
     *      java.lang.String)
     */
    @Override
    public String makeHref(final Class<? extends CimiResource> classToUse, final String id) {
        ExchangeType type = this.getType(classToUse);
        StringBuilder sb = new StringBuilder();
        sb.append(this.request.getBaseUri()).append(type.getPathType().getPathname());
        if (true == this.mustHaveIdInReference(classToUse)) {
            sb.append('/');
            if (null != id) {
                sb.append(id);
            }
        }
        return sb.toString();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.ow2.sirocco.apis.rest.cimi.request.CimiContext#isConvertedWriteOnly()
     */
    @Override
    public boolean isConvertedWriteOnly() {
        return this.convertedWriteOnly;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.ow2.sirocco.apis.rest.cimi.request.CimiContext#setConvertedWriteOnly(boolean)
     */
    @Override
    public void setConvertedWriteOnly(final boolean convertedWriteOnly) {
        this.convertedWriteOnly = convertedWriteOnly;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.ow2.sirocco.apis.rest.cimi.request.CimiContext#findAssociate(java.lang.Class)
     */
    @SuppressWarnings("unchecked")
    @Override
    public Class<? extends CimiResource> findAssociate(final Class<?> klass) {
        Class<? extends CimiResource> cimi = null;
        try {
            ItemConfig item = AppConfig.getInstance().getConfig().find(klass);
            cimi = (Class<? extends CimiResource>) item.getData(ConfigFactory.ASSOCIATE_TO);
        } catch (Exception e) {
            throw new ConfigurationException("Associate class not found in configuration for " + klass.getName());
        }
        return cimi;
    }

    /**
     * Returns the root class being converted.
     * 
     * @return The current root converting
     */
    protected Class<?> getRootConverting() {
        return this.stackConverted.getLast();
    }

    /**
     * Get the exchange type for a CIMI instance.
     * 
     * @param exchange A CIMI Exchange instance
     * @return The exchange type or null if the instance's class is unknown
     */
    protected ExchangeType getType(final CimiExchange exchange) {
        ExchangeType type = null;
        if (null != exchange) {
            type = exchange.getExchangeType();
        }
        return type;
    }

    /**
     * Get the exchange type for a instance.
     * 
     * @param klass A class
     * @return The exchange type or null if the class is unknown
     */
    protected ExchangeType getType(final Class<?> klass) {
        ExchangeType type = null;
        ItemConfig item = AppConfig.getInstance().getConfig().find(klass);
        if (null != item) {
            type = item.getType();
        }
        return type;
    }

}