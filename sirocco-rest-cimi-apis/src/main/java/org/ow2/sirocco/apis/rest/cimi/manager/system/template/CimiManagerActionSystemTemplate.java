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
package org.ow2.sirocco.apis.rest.cimi.manager.system.template;

import javax.validation.groups.Default;

import org.ow2.sirocco.apis.rest.cimi.domain.ActionType;
import org.ow2.sirocco.apis.rest.cimi.domain.CimiAction;
import org.ow2.sirocco.apis.rest.cimi.domain.CimiActionImport;
import org.ow2.sirocco.apis.rest.cimi.manager.CimiManagerAbstract;
import org.ow2.sirocco.apis.rest.cimi.request.CimiContext;
import org.ow2.sirocco.apis.rest.cimi.validator.CimiValidatorHelper;
import org.ow2.sirocco.apis.rest.cimi.validator.GroupWrite;
import org.ow2.sirocco.cloudmanager.core.api.ISystemManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Manage ACTION request of SystemTemplate.
 */
@Component("CimiManagerActionSystemTemplate")
public class CimiManagerActionSystemTemplate extends CimiManagerAbstract {

    @Autowired
    @Qualifier("ISystemManager")
    private ISystemManager manager;

    /**
     * {@inheritDoc}
     * 
     * @see org.ow2.sirocco.apis.rest.cimi.manager.CimiManagerAbstract#validate(org.ow2.sirocco.apis.rest.cimi.request.CimiContext)
     */
    @Override
    protected boolean validate(final CimiContext context) throws Exception {
        boolean valid = CimiValidatorHelper.getInstance().validate(context, context.getRequest().getParams());
        if (valid) {
            if (null == context.getRequest().getCimiData()) {
                valid = false;
            } else {
                valid = CimiValidatorHelper.getInstance().validate(context, context.getRequest().getCimiData(), Default.class,
                    GroupWrite.class);
            }
        }
        return valid;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.ow2.sirocco.apis.rest.cimi.manager.CimiManagerAbstract#convertToDataService(org.ow2.sirocco.apis.rest.cimi.request.CimiContext)
     */
    @Override
    protected Object convertToDataService(final CimiContext context) throws Exception {
        // Nothing to do
        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.ow2.sirocco.apis.rest.cimi.manager.CimiManagerAbstract#callService(org.ow2.sirocco.apis.rest.cimi.request.CimiContext,
     *      java.lang.Object)
     */
    @Override
    protected Object callService(final CimiContext context, final Object dataService) throws Exception {
        Object out = null;
        CimiAction cimiAction = (CimiAction) context.getRequest().getCimiData();
        ActionType type = ActionType.findPath(cimiAction.getAction());
        switch (type) {
        case EXPORT:
            out = this.manager.exportSystemTemplate(context.getRequest().getId(), cimiAction.getFormat(),
                cimiAction.getDestination(), cimiAction.getProperties());
            break;
        case IMPORT:
            out = this.manager.importSystemTemplate(((CimiActionImport) cimiAction).getSource(), cimiAction.getProperties());
            break;
        default:
            throw new UnsupportedOperationException();
        }
        return out;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.ow2.sirocco.apis.rest.cimi.manager.CimiManagerAbstract#convertToResponse(org.ow2.sirocco.apis.rest.cimi.request.CimiContext,
     *      java.lang.Object)
     */
    @Override
    protected void convertToResponse(final CimiContext context, final Object dataService) throws Exception {
        // Nothing to do
    }
}