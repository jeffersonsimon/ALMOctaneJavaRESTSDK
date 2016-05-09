package com.hpe.adm.nga.tests.crud;

import com.hpe.adm.nga.sdk.Query;
import com.hpe.adm.nga.sdk.model.EntityModel;
import com.hpe.adm.nga.sdk.model.FieldModel;
import com.hpe.adm.nga.sdk.model.LongFieldModel;
import com.hpe.adm.nga.sdk.model.StringFieldModel;
import com.hpe.adm.nga.tests.base.TestBase;
import com.hpe.adm.nga.sdk.utils.CommonUtils;
import com.hpe.adm.nga.sdk.utils.QueryUtils;
import com.hpe.adm.nga.sdk.utils.generator.DataGenerator;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

/**
 * Created by Guy Guetta on 21/04/2016.
 */
public class UpdateEntity extends TestBase {

    public UpdateEntity() {
        entityName = "releases";
    }

    @Test
    public void testUpdateEntityById() throws Exception{

        String updatedNameValue = "updatedName" + UUID.randomUUID();

        Collection<EntityModel> generatedEntity = DataGenerator.generateEntityModel(nga, entityName);
        Collection<EntityModel> entityModels = entityList.create().entities(generatedEntity).execute();
        EntityModel entityModel = entityModels.iterator().next();
        int entityId = CommonUtils.getIdFromEntityModel(entityModel);

        Set<FieldModel> fields = new HashSet<>();
        StringFieldModel nameField = new StringFieldModel("name", updatedNameValue);
        fields.add(nameField);
        EntityModel updatedEntity = new EntityModel(fields);

        entityList.at(entityId).update().entity(updatedEntity).execute();

        EntityModel getEntity = entityList.at(entityId).get().execute();

        Assert.assertTrue(CommonUtils.isEntityAInEntityB(updatedEntity, getEntity));
    }

    @Test
    public void testUpdateEntityCollectionIdInBody() throws Exception {

        List<String> updatedNameValues =  DataGenerator.generateNamesForUpdate();

        Collection<EntityModel> generatedEntity = DataGenerator.generateEntityModelCollection(nga, entityName);
        Collection<EntityModel> entityModels = entityList.create().entities(generatedEntity).execute();
        List<Integer> entityIds = CommonUtils.getIdFromEntityModelCollection(entityModels);

        Collection<EntityModel> updatedEntityCollection = new ArrayList<>();
        for(int i = 0; i < entityIds.size(); i++) {
            Set<FieldModel> fields = new HashSet<>();
            StringFieldModel nameField = new StringFieldModel("name", updatedNameValues.get(i));
            LongFieldModel id = new LongFieldModel("id", entityIds.get(i).longValue());
            fields.add(nameField);
            fields.add(id);
            EntityModel updatedEntity = new EntityModel(fields);
            updatedEntityCollection.add(updatedEntity);
        }

        Query query = QueryUtils.getQueryForIds(entityIds);

        entityList.update().entities(updatedEntityCollection).execute();

        Collection<EntityModel> getEntity = entityList.get().query(query).execute();

        Assert.assertTrue(CommonUtils.isCollectionAInCollectionB(updatedEntityCollection, getEntity));

    }

    @Test // for release entity only
    public void testUpdateEntityCollectionWithQuery() throws Exception {
        
        String updatedEndDateValue =  "2026-03-14T12:00:00Z";

        Collection<EntityModel> generatedDefect = DataGenerator.generateEntityModelCollection(nga, entityName);
        Collection<EntityModel> entityModels = entityList.create().entities(generatedDefect).execute();
        List<Integer> entityIds = CommonUtils.getIdFromEntityModelCollection(entityModels);

        Collection<EntityModel> updatedEntityCollection = new ArrayList<>();

        Set<FieldModel> fields = new HashSet<>();
        StringFieldModel nameField = new StringFieldModel("end_date", updatedEndDateValue);
        fields.add(nameField);
        EntityModel updatedEntity = new EntityModel(fields);
        updatedEntityCollection.add(updatedEntity);

        Query query = QueryUtils.getQueryForIds(entityIds);
        entityList.update().entities(updatedEntityCollection).query(query).execute();
        Collection<EntityModel> getEntity = entityList.get().query(query).execute();
        Assert.assertTrue(CommonUtils.isCollectionAInCollectionB(updatedEntityCollection, getEntity));
    }
    
}