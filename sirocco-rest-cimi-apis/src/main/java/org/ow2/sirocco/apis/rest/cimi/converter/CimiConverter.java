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
package org.ow2.sirocco.apis.rest.cimi.converter;

import org.ow2.sirocco.apis.rest.cimi.request.CimiContext;

/**
 * 
 */
public interface CimiConverter {

    /**
     * Convert a service object to a CIMI object.
     * 
     * @param context The current context
     * @param dataService Service object to convert
     * @return Converted CIMI object
     */
    Object toCimi(final CimiContext context, final Object dataService);

    /**
     * Convert a CIMI object to a service object.
     * 
     * @param context The current context
     * @param dataCimi Source CIMI object to convert
     * @return Converted service object
     */
    Object toService(final CimiContext context, final Object dataCimi);
}
