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
package org.ow2.sirocco.cimi.server.converter;

import java.util.ArrayList;
import java.util.List;

import org.ow2.sirocco.cimi.domain.CimiAddress;
import org.ow2.sirocco.cimi.domain.CimiMachineTemplateNetworkInterface;
import org.ow2.sirocco.cimi.domain.CimiNetwork;
import org.ow2.sirocco.cimi.domain.CimiNetworkPort;
import org.ow2.sirocco.cimi.server.request.CimiContext;
import org.ow2.sirocco.cloudmanager.model.cimi.Address;
import org.ow2.sirocco.cloudmanager.model.cimi.MachineTemplateNetworkInterface;
import org.ow2.sirocco.cloudmanager.model.cimi.Network;
import org.ow2.sirocco.cloudmanager.model.cimi.NetworkPort;

/**
 * Convert the data of the CIMI model and the service model in both directions.
 * <p>
 * Converted classes:
 * <ul>
 * <li>CIMI model: {@link CimiMachineTemplateNetworkInterface}</li>
 * <li>Service model: {@link MachineTemplateNetworkInterface}</li>
 * </ul>
 * </p>
 */
public class MachineTemplateNetworkInterfaceConverter extends ObjectCommonConverter {
    /**
     * {@inheritDoc}
     * 
     * @see org.ow2.sirocco.cimi.server.converter.CimiConverter#toCimi(org.ow2.sirocco.cimi.server.utils.CimiContextImpl,
     *      java.lang.Object)
     */
    @Override
    public Object toCimi(final CimiContext context, final Object dataService) {
        CimiMachineTemplateNetworkInterface cimi = new CimiMachineTemplateNetworkInterface();
        this.copyToCimi(context, dataService, cimi);
        return cimi;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.ow2.sirocco.cimi.server.converter.CimiConverter#copyToCimi(org.ow2.sirocco.cimi.server.utils.CimiContextImpl,
     *      java.lang.Object, java.lang.Object)
     */
    @Override
    public void copyToCimi(final CimiContext context, final Object dataService, final Object dataCimi) {
        this.doCopyToCimi(context, (MachineTemplateNetworkInterface) dataService,
            (CimiMachineTemplateNetworkInterface) dataCimi);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.ow2.sirocco.cimi.server.converter.CimiConverter#toService(org.ow2.sirocco.cimi.server.utils.CimiContextImpl,
     *      java.lang.Object)
     */
    @Override
    public Object toService(final CimiContext context, final Object dataCimi) {
        MachineTemplateNetworkInterface service = new MachineTemplateNetworkInterface();
        this.copyToService(context, dataCimi, service);
        return service;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.ow2.sirocco.cimi.server.converter.CimiConverter#copyToService
     *      (org.ow2.sirocco.cimi.server.utils.CimiContextImpl,
     *      java.lang.Object, java.lang.Object)
     */
    @Override
    public void copyToService(final CimiContext context, final Object dataCimi, final Object dataService) {
        this.doCopyToService(context, (CimiMachineTemplateNetworkInterface) dataCimi,
            (MachineTemplateNetworkInterface) dataService);
    }

    /**
     * Copy data from a service object to a CIMI object.
     * 
     * @param context The current context
     * @param dataService Source service object
     * @param dataCimi Destination CIMI object
     */
    protected void doCopyToCimi(final CimiContext context, final MachineTemplateNetworkInterface dataService,
        final CimiMachineTemplateNetworkInterface dataCimi) {
        // Address
        if ((null != dataService.getAddresses()) && (false == dataService.getAddresses().isEmpty())) {
            List<CimiAddress> listCimis = new ArrayList<CimiAddress>();
            for (Address itemService : dataService.getAddresses()) {
                listCimis.add((CimiAddress) context.convertNextCimi(itemService, CimiAddress.class));
            }
            dataCimi.setListAddresses(listCimis);
        }
        dataCimi.setMtu(dataService.getMtu());
        if (dataService.getSystemNetworkName() != null) {
            CimiNetwork cimiNetwork = new CimiNetwork();
            cimiNetwork.setHref("#" + dataService.getSystemNetworkName());
            dataCimi.setNetwork(cimiNetwork);
        } else {
            dataCimi.setNetwork((CimiNetwork) context.convertNextCimi(dataService.getNetwork(), CimiNetwork.class));
        }
        dataCimi.setNetworkPort((CimiNetworkPort) context.convertNextCimi(dataService.getNetworkPort(), CimiNetworkPort.class));
        dataCimi.setNetworkType(ConverterHelper.toString(dataService.getNetworkType()));
        dataCimi.setState(ConverterHelper.toString(dataService.getState()));
    }

    /**
     * Copy data from a CIMI object to a service object.
     * 
     * @param context The current context
     * @param dataCimi Source CIMI object
     * @param dataService Destination Service object
     */
    protected void doCopyToService(final CimiContext context, final CimiMachineTemplateNetworkInterface dataCimi,
        final MachineTemplateNetworkInterface dataService) {
        dataService.setMtu(dataCimi.getMtu());
        if (dataCimi.getNetwork() != null && dataCimi.getNetwork().getHref() != null
            && dataCimi.getNetwork().getHref().startsWith("#")) {
            dataService.setSystemNetworkName(dataCimi.getNetwork().getHref().substring(1));
        } else {
            dataService.setNetwork((Network) context.convertNextService(dataCimi.getNetwork(), CimiNetwork.class));
        }

        dataService.setNetworkPort((NetworkPort) context.convertNextService(dataCimi.getNetworkPort(), CimiNetworkPort.class));
        dataService.setNetworkType(ConverterHelper.toNetworkType(dataCimi.getNetworkType()));
        dataService.setState(ConverterHelper.toMachineTemplateNetworkInterfaceState(dataCimi.getState()));

        // Next read-only
        // dataService.setAddresses((List<Address>)
        // context.convertNextService(dataCimi.getAddresses()));
    }
}
