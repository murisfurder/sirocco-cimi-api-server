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
package org.ow2.sirocco.cimi.domain.collection;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.ow2.sirocco.cimi.domain.CimiArray;
import org.ow2.sirocco.cimi.domain.CimiArrayAbstract;
import org.ow2.sirocco.cimi.domain.CimiForwardingGroup;
import org.ow2.sirocco.cimi.domain.ExchangeType;

/**
 * Collection of ForwardingGroup.
 */

@XmlRootElement
@XmlType(propOrder = {"id", "count", "array", "operations"})
@JsonSerialize(include = Inclusion.NON_NULL)
public class CimiForwardingGroupCollection extends CimiCollectionAbstract<CimiForwardingGroup> {

    /** Serial number */
    private static final long serialVersionUID = 1L;

    /**
     * {@inheritDoc}
     * 
     * @see org.ow2.sirocco.cimi.domain.collection.CimiCollectionAbstract#getArray()
     */
    @Override
    @XmlElement(name = "ForwardingGroup")
    @JsonProperty(value = "forwardingGroups")
    public CimiForwardingGroup[] getArray() {
        return super.getArray();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.ow2.sirocco.cimi.domain.collection.CimiCollectionAbstract#setArray(E[])
     */
    @Override
    @JsonProperty(value = "forwardingGroups")
    public void setArray(final CimiForwardingGroup[] items) {
        super.setArray(items);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.ow2.sirocco.cimi.domain.CimiExchange#getExchangeType()
     */
    @Override
    @XmlTransient
    @JsonIgnore
    public ExchangeType getExchangeType() {
        return ExchangeType.ForwardingGroupCollection;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.ow2.sirocco.cimi.domain.collection.CimiCollection#newCollection()
     */
    @Override
    public CimiArray<CimiForwardingGroup> newCollection() {
        return new CimiForwardingGroupArray();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.ow2.sirocco.cimi.domain.collection.CimiCollection#getItemClass()
     */
    @Override
    @XmlTransient
    @JsonIgnore
    public Class<CimiForwardingGroup> getItemClass() {
        return CimiForwardingGroup.class;
    }

    /**
     * Concrete class of the collection.
     */
    public class CimiForwardingGroupArray extends CimiArrayAbstract<CimiForwardingGroup> {

        /** Serial number */
        private static final long serialVersionUID = 1L;

        @Override
        public CimiForwardingGroup[] newEmptyArraySized() {
            return new CimiForwardingGroup[this.size()];
        }
    }
}
