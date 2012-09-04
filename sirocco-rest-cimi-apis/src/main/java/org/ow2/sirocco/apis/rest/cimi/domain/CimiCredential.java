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
package org.ow2.sirocco.apis.rest.cimi.domain;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.ow2.sirocco.apis.rest.cimi.validator.GroupCreateByValue;
import org.ow2.sirocco.apis.rest.cimi.validator.GroupWrite;
import org.ow2.sirocco.apis.rest.cimi.validator.constraints.AssertCredential;
import org.ow2.sirocco.apis.rest.cimi.validator.constraints.AssertCredentialMin;
import org.ow2.sirocco.apis.rest.cimi.validator.constraints.NotEmptyIfNotNull;

/**
 * Class Credential.
 */
@XmlRootElement(name = "Credential")
@JsonSerialize(include = Inclusion.NON_NULL)
@AssertCredential(groups = GroupCreateByValue.class)
@AssertCredentialMin(groups = GroupWrite.class)
public class CimiCredential extends CimiObjectCommonAbstract {

    /** Serial number */
    private static final long serialVersionUID = 1L;

    /** The initial superuser's user name. */
    private String userName;

    /** Initial superuser's password. */
    private String password;

    /** The digit of the public key for the initial superuser. */
    @NotEmptyIfNotNull(groups = GroupWrite.class)
    private byte[] key;

    /**
     * Default constructor.
     */
    public CimiCredential() {
        super();
    }

    /**
     * Parameterized constructor.
     * 
     * @param href The reference
     */
    public CimiCredential(final String href) {
        super(href);
    }

    /**
     * Parameterized constructor.
     * 
     * @param userName The login
     * @param password The password
     * @param key The public key
     */
    public CimiCredential(final String userName, final String password, final byte[] key) {
        super();
        this.userName = userName;
        this.password = password;
        this.key = key;
    }

    /**
     * @return the userName
     */
    public String getUserName() {
        return this.userName;
    }

    /**
     * @param userName the userName to set
     */
    public void setUserName(final String userName) {
        this.userName = userName;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(final String password) {
        this.password = password;
    }

    /**
     * Return the value of field "key".
     * 
     * @return The value
     */
    @XmlElement
    public byte[] getKey() {
        return this.key;
    }

    /**
     * Set the value of field "key".
     * 
     * @param key The value
     */
    public void setKey(final byte[] key) {
        this.key = key;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.ow2.sirocco.apis.rest.cimi.domain.CimiObjectCommonAbstract#hasValues()
     */
    @Override
    public boolean hasValues() {
        boolean has = super.hasValues();
        has = has || (null != this.getKey());
        has = has || (null != this.getPassword());
        has = has || (null != this.getUserName());
        return has;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.ow2.sirocco.apis.rest.cimi.domain.CimiExchange#getExchangeType()
     */
    @Override
    @XmlTransient
    @JsonIgnore
    public ExchangeType getExchangeType() {
        return ExchangeType.Credential;
    }

}
