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
package org.ow2.sirocco.cimi.server.manager.machine.template;

import javax.ws.rs.core.Response;

import org.ow2.sirocco.cimi.domain.CimiMachineTemplate;
import org.ow2.sirocco.cimi.server.manager.CimiManagerCreateAbstract;
import org.ow2.sirocco.cimi.server.manager.MergeReferenceHelper;
import org.ow2.sirocco.cimi.server.request.CimiContext;
import org.ow2.sirocco.cimi.server.utils.Constants;
import org.ow2.sirocco.cloudmanager.core.api.IMachineManager;
import org.ow2.sirocco.cloudmanager.model.cimi.MachineTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Manage CREATE request of MachineTemplate.
 */
@Component("CimiManagerCreateMachineTemplate")
public class CimiManagerCreateMachineTemplate extends CimiManagerCreateAbstract {

    @Autowired
    @Qualifier("IMachineManager")
    private IMachineManager manager;

    @Autowired
    @Qualifier("MergeReferenceHelper")
    private MergeReferenceHelper mergeReference;

    /**
     * {@inheritDoc}
     * 
     * @see org.ow2.sirocco.cimi.server.manager.CimiManagerAbstract#callService(org.ow2.sirocco.cimi.server.request.CimiContext,
     *      java.lang.Object)
     */
    @Override
    protected Object callService(final CimiContext context, final Object dataService) throws Exception {
        return this.manager.createMachineTemplate((MachineTemplate) dataService);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.ow2.sirocco.cimi.server.manager.CimiManagerAbstract#convertToResponse(org.ow2.sirocco.cimi.server.request.CimiContext,
     *      java.lang.Object)
     */
    @Override
    protected void convertToResponse(final CimiContext context, final Object dataService) throws Exception {
        CimiMachineTemplate cimi = (CimiMachineTemplate) context.convertToCimi(dataService, CimiMachineTemplate.class);
        context.getResponse().setCimiData(cimi);
        context.getResponse().putHeader(Constants.HEADER_LOCATION, cimi.getId());
        context.getResponse().setStatus(Response.Status.CREATED);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.ow2.sirocco.cimi.server.manager.CimiManagerAbstract#beforeConvertToDataService(org.ow2.sirocco.cimi.server.request.CimiContext)
     */
    @Override
    protected void beforeConvertToDataService(final CimiContext context) throws Exception {
        this.mergeReference.merge(context, (CimiMachineTemplate) context.getRequest().getCimiData());
    }
}
