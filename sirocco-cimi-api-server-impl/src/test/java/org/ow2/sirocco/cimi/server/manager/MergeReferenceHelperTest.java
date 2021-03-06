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
package org.ow2.sirocco.cimi.server.manager;

import javax.inject.Inject;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.jglue.cdiunit.AdditionalClasses;
import org.jglue.cdiunit.CdiRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ow2.sirocco.cimi.domain.CimiCredential;
import org.ow2.sirocco.cimi.domain.CimiCredentialCreate;
import org.ow2.sirocco.cimi.domain.CimiCredentialTemplate;
import org.ow2.sirocco.cimi.domain.CimiMachineConfiguration;
import org.ow2.sirocco.cimi.domain.CimiMachineCreate;
import org.ow2.sirocco.cimi.domain.CimiMachineImage;
import org.ow2.sirocco.cimi.domain.CimiMachineTemplate;
import org.ow2.sirocco.cimi.domain.ExchangeType;
import org.ow2.sirocco.cimi.server.manager.cep.CimiManagerReadCloudEntryPoint;
import org.ow2.sirocco.cimi.server.manager.credentials.CimiManagerCreateCredential;
import org.ow2.sirocco.cimi.server.manager.credentials.CimiManagerDeleteCredential;
import org.ow2.sirocco.cimi.server.manager.credentials.CimiManagerReadCredential;
import org.ow2.sirocco.cimi.server.manager.credentials.CimiManagerReadCredentialCollection;
import org.ow2.sirocco.cimi.server.manager.credentials.CimiManagerUpdateCredential;
import org.ow2.sirocco.cimi.server.manager.credentials.template.CimiManagerCreateCredentialTemplate;
import org.ow2.sirocco.cimi.server.manager.credentials.template.CimiManagerDeleteCredentialTemplate;
import org.ow2.sirocco.cimi.server.manager.credentials.template.CimiManagerReadCredentialTemplate;
import org.ow2.sirocco.cimi.server.manager.credentials.template.CimiManagerReadCredentialTemplateCollection;
import org.ow2.sirocco.cimi.server.manager.credentials.template.CimiManagerUpdateCredentialTemplate;
import org.ow2.sirocco.cimi.server.manager.job.CimiManagerReadJobCollection;
import org.ow2.sirocco.cimi.server.manager.machine.CimiManagerActionMachine;
import org.ow2.sirocco.cimi.server.manager.machine.CimiManagerCreateMachine;
import org.ow2.sirocco.cimi.server.manager.machine.CimiManagerDeleteMachine;
import org.ow2.sirocco.cimi.server.manager.machine.CimiManagerReadMachine;
import org.ow2.sirocco.cimi.server.manager.machine.CimiManagerReadMachineCollection;
import org.ow2.sirocco.cimi.server.manager.machine.CimiManagerUpdateMachine;
import org.ow2.sirocco.cimi.server.manager.machine.configuration.CimiManagerCreateMachineConfiguration;
import org.ow2.sirocco.cimi.server.manager.machine.configuration.CimiManagerDeleteMachineConfiguration;
import org.ow2.sirocco.cimi.server.manager.machine.configuration.CimiManagerReadMachineConfiguration;
import org.ow2.sirocco.cimi.server.manager.machine.configuration.CimiManagerReadMachineConfigurationCollection;
import org.ow2.sirocco.cimi.server.manager.machine.configuration.CimiManagerUpdateMachineConfiguration;
import org.ow2.sirocco.cimi.server.manager.machine.image.CimiManagerCreateMachineImage;
import org.ow2.sirocco.cimi.server.manager.machine.image.CimiManagerDeleteMachineImage;
import org.ow2.sirocco.cimi.server.manager.machine.image.CimiManagerReadMachineImage;
import org.ow2.sirocco.cimi.server.manager.machine.image.CimiManagerReadMachineImageCollection;
import org.ow2.sirocco.cimi.server.manager.machine.image.CimiManagerUpdateMachineImage;
import org.ow2.sirocco.cimi.server.manager.machine.template.CimiManagerCreateMachineTemplate;
import org.ow2.sirocco.cimi.server.manager.machine.template.CimiManagerDeleteMachineTemplate;
import org.ow2.sirocco.cimi.server.manager.machine.template.CimiManagerReadMachineTemplate;
import org.ow2.sirocco.cimi.server.manager.machine.template.CimiManagerReadMachineTemplateCollection;
import org.ow2.sirocco.cimi.server.manager.machine.template.CimiManagerUpdateMachineTemplate;
import org.ow2.sirocco.cimi.server.request.CimiContext;
import org.ow2.sirocco.cimi.server.request.CimiContextImpl;
import org.ow2.sirocco.cimi.server.request.CimiExpand;
import org.ow2.sirocco.cimi.server.request.CimiRequest;
import org.ow2.sirocco.cimi.server.request.CimiResponse;
import org.ow2.sirocco.cimi.server.request.CimiSelect;
import org.ow2.sirocco.cimi.server.request.RequestParams;
import org.ow2.sirocco.cimi.server.test.util.ManagerProducers;
import org.ow2.sirocco.cloudmanager.core.api.ICredentialsManager;
import org.ow2.sirocco.cloudmanager.core.api.IMachineImageManager;
import org.ow2.sirocco.cloudmanager.core.api.IMachineManager;
import org.ow2.sirocco.cloudmanager.model.cimi.Credentials;
import org.ow2.sirocco.cloudmanager.model.cimi.CredentialsTemplate;
import org.ow2.sirocco.cloudmanager.model.cimi.MachineConfiguration;
import org.ow2.sirocco.cloudmanager.model.cimi.MachineImage;
import org.ow2.sirocco.cloudmanager.model.cimi.MachineTemplate;

/**
 * Interface test.
 */
@RunWith(CdiRunner.class)
@AdditionalClasses({ManagerProducers.class, CimiManagerActionMachine.class, CimiManagerReadMachine.class,
    CimiManagerUpdateMachine.class, CimiManagerDeleteMachine.class, CimiManagerCreateMachine.class,
    CimiManagerReadCloudEntryPoint.class, CimiManagerReadCredentialCollection.class,
    CimiManagerReadCredentialTemplateCollection.class, CimiManagerCreateCredentialTemplate.class,
    CimiManagerDeleteCredentialTemplate.class, CimiManagerReadCredentialTemplate.class,
    CimiManagerUpdateCredentialTemplate.class, CimiManagerCreateCredential.class, CimiManagerDeleteCredential.class,
    CimiManagerReadCredential.class, CimiManagerUpdateCredential.class, CimiManagerReadJobCollection.class,
    CimiManagerReadMachineCollection.class, CimiManagerReadMachineConfigurationCollection.class,
    CimiManagerCreateMachineConfiguration.class, CimiManagerDeleteMachineConfiguration.class,
    CimiManagerReadMachineConfiguration.class, CimiManagerUpdateMachineConfiguration.class,
    CimiManagerReadMachineImageCollection.class, CimiManagerCreateMachineImage.class, CimiManagerDeleteMachineImage.class,
    CimiManagerReadMachineImage.class, CimiManagerUpdateMachineImage.class, CimiManagerReadMachineTemplateCollection.class,
    CimiManagerCreateMachineTemplate.class, CimiManagerDeleteMachineTemplate.class, CimiManagerReadMachineTemplate.class,
    CimiManagerUpdateMachineTemplate.class, CallServiceHelperImpl.class, MergeReferenceHelperImpl.class})
public class MergeReferenceHelperTest {

    @Inject
    private IMachineImageManager serviceMachineImage;

    @Inject
    private IMachineManager serviceMachine;

    @Inject
    private ICredentialsManager serviceCredentials;

    @Inject
    private MergeReferenceHelper helper;

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
        RequestParams params = new RequestParams();
        params.setCimiSelect(new CimiSelect());
        params.setCimiExpand(new CimiExpand());
        this.request.setParams(params);

        this.response = new CimiResponse();
        this.context = new CimiContextImpl(this.request, this.response);
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
        EasyMock.reset(this.serviceCredentials);
        EasyMock.reset(this.serviceMachine);
        EasyMock.reset(this.serviceMachineImage);
    }

    @Test
    public void testCimiCredentials() throws Exception {
        CimiCredential cimi;

        Credentials reference;
        reference = new Credentials();
        reference.setUuid("456");
        reference.setName("refName");

        // Only by value
        EasyMock.replay(this.serviceCredentials);

        cimi = new CimiCredential();
        cimi.setName("name");
        this.helper.merge(this.context, cimi);

        EasyMock.verify(this.serviceCredentials);
        EasyMock.reset(this.serviceCredentials);

        // Only by reference
        EasyMock.expect(this.serviceCredentials.getCredentialsByUuid("456")).andReturn(reference);
        EasyMock.replay(this.serviceCredentials);

        cimi = new CimiCredential(this.request.getBaseUri() + ExchangeType.Credential.getPathType().getPathname() + "/456");
        this.helper.merge(this.context, cimi);

        Assert.assertEquals("refName", cimi.getName());

        EasyMock.verify(this.serviceCredentials);
        EasyMock.reset(this.serviceCredentials);

        // By reference and by value
        EasyMock.expect(this.serviceCredentials.getCredentialsByUuid("456")).andReturn(reference);
        EasyMock.replay(this.serviceCredentials);

        cimi = new CimiCredential(this.request.getBaseUri() + ExchangeType.Credential.getPathType().getPathname() + "/456");
        cimi.setName("name");
        this.helper.merge(this.context, cimi);

        Assert.assertEquals("name", cimi.getName());

        EasyMock.verify(this.serviceCredentials);
        EasyMock.reset(this.serviceCredentials);
    }

    @Test
    public void testCimiCredentialsCreate() throws Exception {
        // Mock only one method
        MergeReferenceHelperImpl mockedClass = EasyMock.createMockBuilder(MergeReferenceHelperImpl.class)
            .addMockedMethod("merge", CimiContext.class, CimiCredentialTemplate.class).createMock();

        // Prepare parameters
        CimiCredentialTemplate template;
        template = new CimiCredentialTemplate();
        template.setName("name");

        CimiCredentialCreate cimi;
        cimi = new CimiCredentialCreate();
        cimi.setCredentialTemplate(template);

        // Prepare the call to test with the final parameters
        mockedClass.merge(this.context, template);
        EasyMock.replay(mockedClass);

        // Call the method that must call the mock method
        mockedClass.merge(this.context, cimi);
        EasyMock.verify(mockedClass);
    }

    @Test
    public void testCimiCredentialsTemplate() throws Exception {
        CimiCredentialTemplate cimi;

        CredentialsTemplate reference;
        reference = new CredentialsTemplate();
        reference.setUuid("456");
        reference.setName("refName");
        reference.setDescription("refDescription");

        // Only by value
        EasyMock.replay(this.serviceCredentials);

        cimi = new CimiCredentialTemplate();
        cimi.setName("name");
        this.helper.merge(this.context, cimi);

        Assert.assertEquals("name", cimi.getName());
        Assert.assertNull("description", cimi.getDescription());

        EasyMock.verify(this.serviceCredentials);
        EasyMock.reset(this.serviceCredentials);

        // Only by reference
        EasyMock.expect(this.serviceCredentials.getCredentialsTemplateByUuid("456")).andReturn(reference);
        EasyMock.replay(this.serviceCredentials);

        cimi = new CimiCredentialTemplate(this.request.getBaseUri()
            + ExchangeType.CredentialTemplate.getPathType().getPathname() + "/456");
        this.helper.merge(this.context, cimi);

        Assert.assertEquals("refName", cimi.getName());
        Assert.assertEquals("refDescription", cimi.getDescription());

        EasyMock.verify(this.serviceCredentials);
        EasyMock.reset(this.serviceCredentials);

        // By reference and by value
        EasyMock.expect(this.serviceCredentials.getCredentialsTemplateByUuid("456")).andReturn(reference);
        EasyMock.replay(this.serviceCredentials);

        cimi = new CimiCredentialTemplate(this.request.getBaseUri()
            + ExchangeType.CredentialTemplate.getPathType().getPathname() + "/456");
        cimi.setName("name");
        this.helper.merge(this.context, cimi);

        Assert.assertEquals("name", cimi.getName());
        Assert.assertEquals("refDescription", cimi.getDescription());

        EasyMock.verify(this.serviceCredentials);
        EasyMock.reset(this.serviceCredentials);
    }

    @Test
    public void testCimiMachineImage() throws Exception {
        CimiMachineImage cimi;

        MachineImage reference;
        reference = new MachineImage();
        reference.setUuid("456");
        reference.setName("refName");

        // Only by value
        EasyMock.replay(this.serviceMachineImage);

        cimi = new CimiMachineImage();
        cimi.setName("name");
        this.helper.merge(this.context, cimi);

        EasyMock.verify(this.serviceMachineImage);
        EasyMock.reset(this.serviceMachineImage);

        // Only by reference
        EasyMock.expect(this.serviceMachineImage.getMachineImageByUuid("456")).andReturn(reference);
        EasyMock.replay(this.serviceMachineImage);

        cimi = new CimiMachineImage(this.request.getBaseUri() + ExchangeType.MachineImage.getPathType().getPathname() + "/456");
        this.helper.merge(this.context, cimi);

        Assert.assertEquals("refName", cimi.getName());

        EasyMock.verify(this.serviceMachineImage);
        EasyMock.reset(this.serviceMachineImage);

        // By reference and by value
        EasyMock.expect(this.serviceMachineImage.getMachineImageByUuid("456")).andReturn(reference);
        EasyMock.replay(this.serviceMachineImage);

        cimi = new CimiMachineImage(this.request.getBaseUri() + ExchangeType.MachineImage.getPathType().getPathname() + "/456");
        cimi.setName("name");
        this.helper.merge(this.context, cimi);

        Assert.assertEquals("name", cimi.getName());

        EasyMock.verify(this.serviceMachineImage);
        EasyMock.reset(this.serviceMachineImage);
    }

    @Test
    public void testCimiMachineConfiguration() throws Exception {
        CimiMachineConfiguration cimi;

        MachineConfiguration reference;
        reference = new MachineConfiguration();
        reference.setUuid("456");
        reference.setName("refName");

        // Only by value
        EasyMock.replay(this.serviceMachine);

        cimi = new CimiMachineConfiguration();
        cimi.setName("name");
        this.helper.merge(this.context, cimi);

        EasyMock.verify(this.serviceMachine);
        EasyMock.reset(this.serviceMachine);

        // Only by reference
        EasyMock.expect(this.serviceMachine.getMachineConfigurationByUuid("456")).andReturn(reference);
        EasyMock.replay(this.serviceMachine);

        cimi = new CimiMachineConfiguration(this.request.getBaseUri()
            + ExchangeType.MachineConfiguration.getPathType().getPathname() + "/456");
        this.helper.merge(this.context, cimi);

        Assert.assertEquals("refName", cimi.getName());

        EasyMock.verify(this.serviceMachine);
        EasyMock.reset(this.serviceMachine);

        // By reference and by value
        EasyMock.expect(this.serviceMachine.getMachineConfigurationByUuid("456")).andReturn(reference);
        EasyMock.replay(this.serviceMachine);

        cimi = new CimiMachineConfiguration(this.request.getBaseUri()
            + ExchangeType.MachineConfiguration.getPathType().getPathname() + "/456");
        cimi.setName("name");
        this.helper.merge(this.context, cimi);

        Assert.assertEquals("name", cimi.getName());

        EasyMock.verify(this.serviceMachine);
        EasyMock.reset(this.serviceMachine);
    }

    @Test
    // TODO Volumes, Network, ..
    public void testCimiMachineTemplate() throws Exception {
        CimiMachineTemplate cimi;
        CimiCredential cimiCredential;
        CimiMachineImage cimiImage;
        CimiMachineConfiguration cimiConfiguration;

        MachineTemplate reference;
        reference = new MachineTemplate();
        reference.setUuid("123");
        reference.setName("refName");
        reference.setDescription("refDescription");

        Credentials referenceCredentials;
        referenceCredentials = new Credentials();
        referenceCredentials.setUuid("234");
        referenceCredentials.setName("refNameCredentials");
        referenceCredentials.setDescription("refDescriptionCredentials");

        MachineImage referenceImage;
        referenceImage = new MachineImage();
        referenceImage.setUuid("345");
        referenceImage.setName("refNameImage");
        referenceImage.setDescription("refDescriptionImage");

        MachineConfiguration referenceConfiguration;
        referenceConfiguration = new MachineConfiguration();
        referenceConfiguration.setUuid("456");
        referenceConfiguration.setName("refNameConfiguration");
        referenceConfiguration.setDescription("refDescriptionConfiguration");

        reference.setCredential(referenceCredentials);
        reference.setMachineConfig(referenceConfiguration);
        reference.setMachineImage(referenceImage);

        // ---------------------------------------------------
        // Only by value : template
        EasyMock.replay(this.serviceCredentials);
        EasyMock.replay(this.serviceMachine);
        EasyMock.replay(this.serviceMachineImage);

        cimi = new CimiMachineTemplate();
        cimi.setName("name");
        this.helper.merge(this.context, cimi);

        EasyMock.verify(this.serviceCredentials);
        EasyMock.verify(this.serviceMachine);
        EasyMock.verify(this.serviceMachineImage);
        EasyMock.reset(this.serviceCredentials);
        EasyMock.reset(this.serviceMachine);
        EasyMock.reset(this.serviceMachineImage);

        // ---------------------------------------------------
        // Only by reference : template
        EasyMock.replay(this.serviceCredentials);
        EasyMock.expect(this.serviceMachine.getMachineTemplateByUuid("123")).andReturn(reference);
        EasyMock.replay(this.serviceMachine);
        EasyMock.replay(this.serviceMachineImage);

        cimi = new CimiMachineTemplate(this.request.getBaseUri() + ExchangeType.MachineTemplate.getPathType().getPathname()
            + "/123");
        this.helper.merge(this.context, cimi);

        Assert.assertEquals("refName", cimi.getName());
        Assert.assertEquals("refNameConfiguration", cimi.getMachineConfig().getName());

        EasyMock.verify(this.serviceCredentials);
        EasyMock.verify(this.serviceMachine);
        EasyMock.verify(this.serviceMachineImage);
        EasyMock.reset(this.serviceCredentials);
        EasyMock.reset(this.serviceMachine);
        EasyMock.reset(this.serviceMachineImage);

        // ---------------------------------------------------
        // By reference and by value : template
        EasyMock.replay(this.serviceCredentials);
        EasyMock.expect(this.serviceMachine.getMachineTemplateByUuid("123")).andReturn(reference);
        EasyMock.replay(this.serviceMachine);
        EasyMock.replay(this.serviceMachineImage);

        cimi = new CimiMachineTemplate(this.request.getBaseUri() + ExchangeType.MachineTemplate.getPathType().getPathname()
            + "/123");
        cimi.setName("name");
        this.helper.merge(this.context, cimi);

        Assert.assertEquals("name", cimi.getName());

        EasyMock.verify(this.serviceCredentials);
        EasyMock.verify(this.serviceMachine);
        EasyMock.verify(this.serviceMachineImage);
        EasyMock.reset(this.serviceCredentials);
        EasyMock.reset(this.serviceMachine);
        EasyMock.reset(this.serviceMachineImage);

        // ---------------------------------------------------
        // Only by value : template and internal entities
        EasyMock.replay(this.serviceCredentials);
        EasyMock.replay(this.serviceMachine);
        EasyMock.replay(this.serviceMachineImage);

        cimiCredential = new CimiCredential();
        cimiCredential.setName("nameCredential");
        cimiConfiguration = new CimiMachineConfiguration();
        cimiConfiguration.setName("nameConfiguration");
        cimiImage = new CimiMachineImage();
        cimiImage.setName("nameImage");

        cimi = new CimiMachineTemplate();
        cimi.setCredential(cimiCredential);
        cimi.setMachineConfig(cimiConfiguration);
        cimi.setMachineImage(cimiImage);
        this.helper.merge(this.context, cimi);

        Assert.assertEquals("nameCredential", cimi.getCredential().getName());
        Assert.assertNull(cimi.getCredential().getDescription());
        Assert.assertEquals("nameConfiguration", cimi.getMachineConfig().getName());
        Assert.assertNull(cimi.getMachineConfig().getDescription());
        Assert.assertEquals("nameImage", cimi.getMachineImage().getName());
        Assert.assertNull(cimi.getMachineImage().getDescription());

        EasyMock.verify(this.serviceCredentials);
        EasyMock.verify(this.serviceMachine);
        EasyMock.verify(this.serviceMachineImage);
        EasyMock.reset(this.serviceCredentials);
        EasyMock.reset(this.serviceMachine);
        EasyMock.reset(this.serviceMachineImage);

        // ---------------------------------------------------
        // Template by value, internal entities only by reference
        EasyMock.expect(this.serviceCredentials.getCredentialsByUuid("234")).andReturn(referenceCredentials);
        EasyMock.replay(this.serviceCredentials);
        EasyMock.expect(this.serviceMachine.getMachineConfigurationByUuid("345")).andReturn(referenceConfiguration);
        EasyMock.replay(this.serviceMachine);
        EasyMock.expect(this.serviceMachineImage.getMachineImageByUuid("456")).andReturn(referenceImage);
        EasyMock.replay(this.serviceMachineImage);

        cimi = new CimiMachineTemplate();
        cimi.setCredential(new CimiCredential(this.request.getBaseUri() + ExchangeType.Credential.getPathType().getPathname()
            + "/234"));
        cimi.setMachineConfig(new CimiMachineConfiguration(this.request.getBaseUri()
            + ExchangeType.MachineConfiguration.getPathType().getPathname() + "/345"));
        cimi.setMachineImage(new CimiMachineImage(this.request.getBaseUri()
            + ExchangeType.MachineImage.getPathType().getPathname() + "/456"));
        this.helper.merge(this.context, cimi);

        Assert.assertEquals("refNameCredentials", cimi.getCredential().getName());
        Assert.assertEquals("refDescriptionCredentials", cimi.getCredential().getDescription());
        Assert.assertEquals("refNameConfiguration", cimi.getMachineConfig().getName());
        Assert.assertEquals("refDescriptionConfiguration", cimi.getMachineConfig().getDescription());
        Assert.assertEquals("refNameImage", cimi.getMachineImage().getName());
        Assert.assertEquals("refDescriptionImage", cimi.getMachineImage().getDescription());

        EasyMock.verify(this.serviceCredentials);
        EasyMock.verify(this.serviceMachine);
        EasyMock.verify(this.serviceMachineImage);
        EasyMock.reset(this.serviceCredentials);
        EasyMock.reset(this.serviceMachine);
        EasyMock.reset(this.serviceMachineImage);

        // ---------------------------------------------------
        // Template by value, internal entities by reference and by value
        EasyMock.expect(this.serviceCredentials.getCredentialsByUuid("234")).andReturn(referenceCredentials);
        EasyMock.replay(this.serviceCredentials);
        EasyMock.expect(this.serviceMachine.getMachineConfigurationByUuid("345")).andReturn(referenceConfiguration);
        EasyMock.replay(this.serviceMachine);
        EasyMock.expect(this.serviceMachineImage.getMachineImageByUuid("456")).andReturn(referenceImage);
        EasyMock.replay(this.serviceMachineImage);

        cimi = new CimiMachineTemplate();
        cimi.setCredential(new CimiCredential(this.request.getBaseUri() + ExchangeType.Credential.getPathType().getPathname()
            + "/234"));
        cimi.getCredential().setName("nameCredentials");
        cimi.setMachineConfig(new CimiMachineConfiguration(this.request.getBaseUri()
            + ExchangeType.MachineConfiguration.getPathType().getPathname() + "/345"));
        cimi.getMachineConfig().setName("nameConfiguration");
        cimi.setMachineImage(new CimiMachineImage(this.request.getBaseUri()
            + ExchangeType.MachineImage.getPathType().getPathname() + "/456"));
        cimi.getMachineImage().setName("nameImage");
        this.helper.merge(this.context, cimi);

        Assert.assertEquals("nameCredentials", cimi.getCredential().getName());
        Assert.assertEquals("refDescriptionCredentials", cimi.getCredential().getDescription());
        Assert.assertEquals("nameConfiguration", cimi.getMachineConfig().getName());
        Assert.assertEquals("refDescriptionConfiguration", cimi.getMachineConfig().getDescription());
        Assert.assertEquals("nameImage", cimi.getMachineImage().getName());
        Assert.assertEquals("refDescriptionImage", cimi.getMachineImage().getDescription());

        EasyMock.verify(this.serviceCredentials);
        EasyMock.verify(this.serviceMachine);
        EasyMock.verify(this.serviceMachineImage);
        EasyMock.reset(this.serviceCredentials);
        EasyMock.reset(this.serviceMachine);
        EasyMock.reset(this.serviceMachineImage);
    }

    @Test
    public void testCimiMachineCreate() throws Exception {
        // Mock only one method
        MergeReferenceHelperImpl mockedClass = EasyMock.createMockBuilder(MergeReferenceHelperImpl.class)
            .addMockedMethod("merge", CimiContext.class, CimiMachineTemplate.class).createMock();

        // Prepare parameters
        CimiMachineTemplate template;
        template = new CimiMachineTemplate();
        template.setName("name");

        CimiMachineCreate cimi;
        cimi = new CimiMachineCreate();
        cimi.setMachineTemplate(template);

        // Prepare the call to test with the final parameters
        mockedClass.merge(this.context, template);
        EasyMock.replay(mockedClass);

        // Call the method that must call the mock method
        mockedClass.merge(this.context, cimi);
        EasyMock.verify(mockedClass);
    }

}
