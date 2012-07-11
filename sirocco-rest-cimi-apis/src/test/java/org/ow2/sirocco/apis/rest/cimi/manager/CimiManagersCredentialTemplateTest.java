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
package org.ow2.sirocco.apis.rest.cimi.manager;

import java.util.HashMap;
import java.util.Map;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ow2.sirocco.apis.rest.cimi.domain.CimiCredentialTemplate;
import org.ow2.sirocco.apis.rest.cimi.domain.ExchangeType;
import org.ow2.sirocco.apis.rest.cimi.request.CimiContext;
import org.ow2.sirocco.apis.rest.cimi.request.CimiContextImpl;
import org.ow2.sirocco.apis.rest.cimi.request.CimiRequest;
import org.ow2.sirocco.apis.rest.cimi.request.CimiResponse;
import org.ow2.sirocco.apis.rest.cimi.request.CimiSelect;
import org.ow2.sirocco.apis.rest.cimi.request.IdRequest;
import org.ow2.sirocco.apis.rest.cimi.request.RequestHeader;
import org.ow2.sirocco.apis.rest.cimi.utils.Constants;
import org.ow2.sirocco.apis.rest.cimi.utils.ConstantsPath;
import org.ow2.sirocco.cloudmanager.core.api.ICredentialsManager;
import org.ow2.sirocco.cloudmanager.model.cimi.CredentialsTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Basic tests "end to end" for managers CredentialsTemplate.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/context/managerContext.xml"})
public class CimiManagersCredentialTemplateTest {

    @Autowired
    @Qualifier("ICredentialsManager")
    private ICredentialsManager service;

    @Autowired
    @Qualifier("CimiManagerCreateCredentialTemplate")
    private CimiManager managerCreate;

    @Autowired
    @Qualifier("CimiManagerDeleteCredentialTemplate")
    private CimiManager managerDelete;

    @Autowired
    @Qualifier("CimiManagerReadCredentialTemplate")
    private CimiManager managerRead;

    @Autowired
    @Qualifier("CimiManagerUpdateCredentialTemplate")
    private CimiManager managerUpdate;

    private CimiRequest request;

    private CimiResponse response;

    private CimiContext context;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {

        this.request = new CimiRequest();
        this.request.setBaseUri("/");
        RequestHeader header = new RequestHeader();
        header.setCimiSelect(new CimiSelect());
        this.request.setHeader(header);

        this.response = new CimiResponse();
        this.context = new CimiContextImpl(this.request, this.response);
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
        EasyMock.reset(this.service);
    }

    @Test
    public void testCreate() throws Exception {
        CredentialsTemplate create = new CredentialsTemplate();
        create.setId(789);

        EasyMock.expect(this.service.createCredentialsTemplate(EasyMock.anyObject(CredentialsTemplate.class)))
            .andReturn(create);
        EasyMock.replay(this.service);

        CimiCredentialTemplate cimi = new CimiCredentialTemplate("user", "pass", new byte[1]);
        this.request.setCimiData(cimi);
        this.managerCreate.execute(this.context);

        Assert.assertEquals(201, this.response.getStatus());
        Assert.assertNotNull(this.response.getHeaders());
        Assert.assertEquals(ConstantsPath.CREDENTIAL_TEMPLATE_PATH + "/789",
            this.response.getHeaders().get(Constants.HEADER_LOCATION));
        Assert.assertEquals(ConstantsPath.CREDENTIAL_TEMPLATE_PATH + "/789",
            ((CimiCredentialTemplate) this.response.getCimiData()).getId());
        EasyMock.verify(this.service);
    }

    @Test
    public void testCreateWithRef() throws Exception {
        CredentialsTemplate reference = new CredentialsTemplate();
        reference.setId(13);
        reference.setName("nameValue");
        reference.setPassword("passwordValue");

        CredentialsTemplate create = new CredentialsTemplate();
        create.setId(789);

        EasyMock.expect(this.service.getCredentialsTemplateById("13")).andReturn(reference);
        EasyMock.expect(this.service.createCredentialsTemplate(EasyMock.anyObject(CredentialsTemplate.class)))
            .andReturn(create);
        EasyMock.replay(this.service);

        CimiCredentialTemplate cimi = new CimiCredentialTemplate(this.request.getBaseUri()
            + ExchangeType.CredentialTemplate.getPathType().getPathname() + "/13");
        this.request.setCimiData(cimi);
        this.managerCreate.execute(this.context);

        Assert.assertEquals(201, this.response.getStatus());
        Assert.assertNotNull(this.response.getHeaders());
        Assert.assertEquals(ConstantsPath.CREDENTIAL_TEMPLATE_PATH + "/789",
            this.response.getHeaders().get(Constants.HEADER_LOCATION));
        Assert.assertEquals(ConstantsPath.CREDENTIAL_TEMPLATE_PATH + "/789",
            ((CimiCredentialTemplate) this.response.getCimiData()).getId());
        EasyMock.verify(this.service);
    }

    @Test
    public void testRead() throws Exception {
        CredentialsTemplate machine = new CredentialsTemplate();
        machine.setId(1);

        EasyMock.expect(this.service.getCredentialsTemplateById("1")).andReturn(machine);
        EasyMock.replay(this.service);

        this.request.setIds(new IdRequest("1"));
        this.managerRead.execute(this.context);

        Assert.assertEquals(200, this.response.getStatus());
        Assert.assertEquals(ConstantsPath.CREDENTIAL_TEMPLATE_PATH + "/1",
            ((CimiCredentialTemplate) this.response.getCimiData()).getId());
        EasyMock.verify(this.service);
    }

    @Test
    public void testDelete() throws Exception {

        this.service.deleteCredentialsTemplate("1");
        EasyMock.replay(this.service);

        this.request.setIds(new IdRequest("1"));
        this.managerDelete.execute(this.context);

        Assert.assertEquals(200, this.response.getStatus());
        EasyMock.verify(this.service);
    }

    @Test
    public void testUpdate() throws Exception {

        this.service.updateCredentialsTemplate(EasyMock.anyObject(CredentialsTemplate.class));
        EasyMock.replay(this.service);

        CimiCredentialTemplate cimi = new CimiCredentialTemplate();
        cimi.setName("foo");
        this.request.setIds(new IdRequest("1"));
        this.request.setCimiData(cimi);

        this.managerUpdate.execute(this.context);

        Assert.assertEquals(200, this.response.getStatus());
        EasyMock.verify(this.service);
    }

    @Test
    public void testUpdateWithCimiSelect() throws Exception {

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", "fooName");
        map.put("description", "fooDescription");

        this.service.updateCredentialsTemplateAttributes(EasyMock.eq("1"), EasyMock.eq(map));
        EasyMock.replay(this.service);

        CimiCredentialTemplate cimi = new CimiCredentialTemplate();
        cimi.setName("fooName");
        cimi.setDescription("fooDescription");
        this.request.setIds(new IdRequest("1"));
        this.request.setCimiData(cimi);
        this.request.getHeader().getCimiSelect().setSelects(new String[] {"name", "description"});

        this.managerUpdate.execute(this.context);

        Assert.assertEquals(200, this.response.getStatus());
        EasyMock.verify(this.service);
    }

}