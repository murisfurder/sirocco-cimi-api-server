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
package org.ow2.sirocco.cimi.server.manager.address;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

import org.ow2.sirocco.cimi.domain.collection.CimiAddressCollectionRoot;
import org.ow2.sirocco.cimi.server.manager.CimiManagerReadAbstract;
import org.ow2.sirocco.cimi.server.request.CimiContext;
import org.ow2.sirocco.cloudmanager.core.api.INetworkManager;
import org.ow2.sirocco.cloudmanager.core.api.QueryParams;
import org.ow2.sirocco.cloudmanager.core.api.QueryResult;

/**
 * Manage READ request of Addresss collection.
 */
@org.ow2.sirocco.cimi.server.manager.Manager("CimiManagerReadAddressCollection")
public class CimiManagerReadAddressCollection extends CimiManagerReadAbstract {

    @Inject
    private INetworkManager manager;

    /**
     * {@inheritDoc}
     * 
     * @see org.ow2.sirocco.cimi.server.manager.CimiManagerAbstract#callService(org.ow2.sirocco.cimi.server.request.CimiContext,
     *      java.lang.Object)
     */
    @Override
    protected Object callService(final CimiContext context, final Object dataService) throws Exception {
        Object out = null;
        if (false == context.hasParamsForReadingCollection()) {
            out = this.manager.getAddresses().getItems();
        } else {
            QueryResult<?> results = this.manager.getAddresses(new QueryParams.Builder().first(context.valueOfFirst())
                .last(context.valueOfLast()).filters(context.valuesOfFilter()).attributes(context.valuesOfSelect()).build());
            out = results.getItems();
        }
        return out;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.ow2.sirocco.cimi.server.manager.CimiManagerAbstract#convertToResponse(org.ow2.sirocco.cimi.server.request.CimiContext,
     *      java.lang.Object)
     */
    @Override
    protected void convertToResponse(final CimiContext context, final Object dataService) throws Exception {
        CimiAddressCollectionRoot cimi = (CimiAddressCollectionRoot) context.convertToCimi(dataService,
            CimiAddressCollectionRoot.class);
        context.getResponse().setCimiData(cimi);
        context.getResponse().setStatus(Response.Status.OK);
    }
}
