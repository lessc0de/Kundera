/*******************************************************************************
 * * Copyright 2013 Impetus Infotech.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 ******************************************************************************/
package com.impetus.client.es;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.impetus.kundera.Constants;
import com.impetus.kundera.PersistenceProperties;
import com.impetus.kundera.configure.ClientFactoryConfiguraton;
import com.impetus.kundera.metadata.model.ApplicationMetadata;
import com.impetus.kundera.metadata.model.ClientMetadata;
import com.impetus.kundera.metadata.model.KunderaMetadata;
import com.impetus.kundera.metadata.model.PersistenceUnitMetadata;

/**
 * @author vivek.mishra
 *  junit for {@link ESClientFactory}
 *
 */
public class ESClientFactoryTest
{
    private final static String persistenceUnit="es";

    @Before
    public void setup()
    {
        getEntityManagerFactory();
    }
    
    @Test
    public void test() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
    {
        ESClientFactory esFactory = new ESClientFactory();
        Map<String, Object> props = new HashMap<String, Object>();
        props.put(PersistenceProperties.KUNDERA_NODES, "localhost");
        props.put(PersistenceProperties.KUNDERA_PORT, "9300");
        
        Field f = esFactory.getClass().getSuperclass().getDeclaredField("persistenceUnit");
     
        if(!f.isAccessible())
        {
            f.setAccessible(true);
        }
        f.set(esFactory, persistenceUnit);
        esFactory.initialize(props);
        esFactory.createPoolOrConnection();
        
        Assert.assertNotNull(esFactory.getClientInstance());
    }

    
    private void getEntityManagerFactory()
    {
        ClientMetadata clientMetadata = new ClientMetadata();
        Map<String, Object> props = new HashMap<String, Object>();

        props.put(Constants.PERSISTENCE_UNIT_NAME, persistenceUnit);
        props.put(PersistenceProperties.KUNDERA_CLIENT_FACTORY, ESClientFactory.class.getName());
        props.put(PersistenceProperties.KUNDERA_NODES, "localhost");
        props.put(PersistenceProperties.KUNDERA_PORT, "9160");
        props.put(PersistenceProperties.KUNDERA_KEYSPACE, "KunderaMetaDataTest");
//        props.put(PersistenceProperties.KUNDERA_DDL_AUTO_PREPARE, schemaProperty);
        clientMetadata.setLuceneIndexDir(null);

        KunderaMetadata.INSTANCE.setApplicationMetadata(null);
        ApplicationMetadata appMetadata = KunderaMetadata.INSTANCE.getApplicationMetadata();
        PersistenceUnitMetadata puMetadata = new PersistenceUnitMetadata();
        puMetadata.setPersistenceUnitName(persistenceUnit);
        Properties p = new Properties();
        p.putAll(props);
        puMetadata.setProperties(p);
        Map<String, PersistenceUnitMetadata> metadata = new HashMap<String, PersistenceUnitMetadata>();
        metadata.put(persistenceUnit, puMetadata);
        appMetadata.addPersistenceUnitMetadata(metadata);

        Map<String, List<String>> clazzToPu = new HashMap<String, List<String>>();

        List<String> pus = new ArrayList<String>();
        pus.add(persistenceUnit);

        appMetadata.setClazzToPuMap(clazzToPu);

//
//        TableProcessor processor = new TableProcessor(null);
//        processor.process(Employe.class, m);
//        processor.process(KunderaUser.class, m1);
//
//        IndexProcessor indexProcessor = new IndexProcessor();
//        indexProcessor.process(Employe.class, m);
//        indexProcessor.process(KunderaUser.class, m1);
//
//        m.setPersistenceUnit(persistenceUnit);
//
//        MetamodelImpl metaModel = new MetamodelImpl();
//        metaModel.addEntityMetadata(Employe.class, m);
//        metaModel.addEntityMetadata(KunderaUser.class, m1);

//        appMetadata.getMetamodelMap().put(persistenceUnit, metaModel);
//
//        metaModel.assignManagedTypes(appMetadata.getMetaModelBuilder(persistenceUnit).getManagedTypes());
//        metaModel.assignEmbeddables(appMetadata.getMetaModelBuilder(persistenceUnit).getEmbeddables());
//        metaModel.assignMappedSuperClass(appMetadata.getMetaModelBuilder(persistenceUnit).getMappedSuperClassTypes());

//        KunderaMetadata.INSTANCE.addClientMetadata(persistenceUnit, clientMetadata);

        String[] persistenceUnits = new String[] { persistenceUnit };
        new ClientFactoryConfiguraton(null, persistenceUnits).configure();

//        new SchemaConfiguration(null, persistenceUnits).configure();
    }

}
