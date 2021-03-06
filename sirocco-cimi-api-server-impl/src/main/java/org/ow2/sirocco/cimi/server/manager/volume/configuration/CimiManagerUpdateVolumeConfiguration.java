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
package org.ow2.sirocco.cimi.server.manager.volume.configuration;

import javax.inject.Inject;

import org.ow2.sirocco.cimi.server.manager.CimiManagerUpdateAbstract;
import org.ow2.sirocco.cimi.server.request.CimiContext;
import org.ow2.sirocco.cloudmanager.core.api.IVolumeManager;
import org.ow2.sirocco.cloudmanager.model.cimi.VolumeConfiguration;

/**
 * Manage UPDATE request of Volume Configuration.
 */
@org.ow2.sirocco.cimi.server.manager.Manager("CimiManagerUpdateVolumeConfiguration")
public class CimiManagerUpdateVolumeConfiguration extends CimiManagerUpdateAbstract {

    @Inject
    private IVolumeManager manager;

    /**
     * {@inheritDoc}
     * 
     * @see org.ow2.sirocco.cimi.server.manager.CimiManagerAbstract#callService(org.ow2.sirocco.cimi.server.request.CimiContext,
     *      java.lang.Object)
     */
    @Override
    protected Object callService(final CimiContext context, final Object dataService) throws Exception {
        if (false == context.hasParamSelect()) {
            this.manager.updateVolumeConfiguration((VolumeConfiguration) dataService);
        } else {
            this.manager.updateVolumeConfigurationAttributes(context.getRequest().getId(),
                context.copyBeanAttributesOfSelect(dataService));
        }
        return null;
    }

}
