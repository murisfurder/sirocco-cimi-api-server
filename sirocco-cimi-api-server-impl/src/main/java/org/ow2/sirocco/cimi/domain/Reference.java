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
package org.ow2.sirocco.cimi.domain;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAttribute;

import org.ow2.sirocco.cimi.server.validator.GroupWrite;

/**
 * Class Reference.
 */
public class Reference implements Serializable {

    /** Serial number */
    private static final long serialVersionUID = 1L;

    /**
     * Field "href".
     */
    @NotNull(groups = GroupWrite.class)
    private String href;

    /**
     * Return the value of field "href".
     * 
     * @return The value
     */
    @XmlAttribute
    public String getHref() {
        return this.href;
    }

    /**
     * Set the value of field "href".
     * 
     * @param href The value
     */
    public void setHref(final String href) {
        this.href = href;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "[href=" + this.href + "]";
    }

}
