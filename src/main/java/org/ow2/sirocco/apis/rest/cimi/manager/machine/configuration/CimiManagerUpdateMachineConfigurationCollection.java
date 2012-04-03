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
package org.ow2.sirocco.apis.rest.cimi.manager.machine.configuration;

import java.util.HashMap;
import java.util.Map;

import org.ow2.sirocco.apis.rest.cimi.converter.CommonConverter;
import org.ow2.sirocco.apis.rest.cimi.domain.CimiCommonId;
import org.ow2.sirocco.apis.rest.cimi.manager.CimiManagerUpdateAbstract;
import org.ow2.sirocco.apis.rest.cimi.request.CimiRequest;
import org.ow2.sirocco.apis.rest.cimi.request.CimiResponse;
import org.ow2.sirocco.apis.rest.cimi.request.CimiSelect;
import org.ow2.sirocco.cloudmanager.core.api.IMachineManager;
import org.ow2.sirocco.cloudmanager.model.cimi.MachineConfigurationCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Manage UPDATE request of Machine Configuration Collection.
 */
@Component("CimiManagerUpdateMachineConfigurationCollection")
public class CimiManagerUpdateMachineConfigurationCollection extends CimiManagerUpdateAbstract {

    @Autowired
    @Qualifier("IMachineConfigurationManager")
    private IMachineManager manager;

    /**
     * {@inheritDoc}
     * 
     * @see org.ow2.sirocco.apis.rest.cimi.manager.CimiManagerAbstract#callService(org.ow2.sirocco.apis.rest.cimi.request.CimiRequest,
     *      org.ow2.sirocco.apis.rest.cimi.request.CimiResponse,
     *      java.lang.Object)
     */
    @Override
    protected Object callService(final CimiRequest request, final CimiResponse response, final Object dataService)
        throws Exception {
        CimiSelect select = request.getHeader().getCimiSelect();
        if (true == select.isEmpty()) {
            throw new UnsupportedOperationException();
        } else {
            Map<String, Object> attrs = new HashMap<String, Object>();
            for (String attr : select.getAttributes()) {
                attrs.put(attr, dataService);
            }
            this.manager.updateMachineConfigurationCollection(attrs);
        }
        return null;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Copy only common attributes.
     * </p>
     * 
     * @see org.ow2.sirocco.apis.rest.cimi.manager.CimiManagerAbstract#convertToDataService(org.ow2.sirocco.apis.rest.cimi.request.CimiRequest,
     *      org.ow2.sirocco.apis.rest.cimi.request.CimiResponse)
     */
    @Override
    protected Object convertToDataService(final CimiRequest request, final CimiResponse response) throws Exception {
        MachineConfigurationCollection service = new MachineConfigurationCollection();
        CommonConverter.copyToService((CimiCommonId) request.getCimiData(), service);
        return service;
    }

}
