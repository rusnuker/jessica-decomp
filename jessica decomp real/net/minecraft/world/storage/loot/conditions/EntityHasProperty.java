/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonDeserializationContext
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonSerializationContext
 */
package net.minecraft.world.storage.loot.conditions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import net.minecraft.entity.Entity;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.properties.EntityProperty;
import net.minecraft.world.storage.loot.properties.EntityPropertyManager;

public class EntityHasProperty
implements LootCondition {
    private final EntityProperty[] properties;
    private final LootContext.EntityTarget target;

    public EntityHasProperty(EntityProperty[] propertiesIn, LootContext.EntityTarget targetIn) {
        this.properties = propertiesIn;
        this.target = targetIn;
    }

    @Override
    public boolean testCondition(Random rand, LootContext context) {
        Entity entity = context.getEntity(this.target);
        if (entity == null) {
            return false;
        }
        EntityProperty[] entityPropertyArray = this.properties;
        int n = this.properties.length;
        int n2 = 0;
        while (n2 < n) {
            EntityProperty entityproperty = entityPropertyArray[n2];
            if (!entityproperty.testProperty(rand, entity)) {
                return false;
            }
            ++n2;
        }
        return true;
    }

    public static class Serializer
    extends LootCondition.Serializer<EntityHasProperty> {
        protected Serializer() {
            super(new ResourceLocation("entity_properties"), EntityHasProperty.class);
        }

        @Override
        public void serialize(JsonObject json, EntityHasProperty value, JsonSerializationContext context) {
            JsonObject jsonobject = new JsonObject();
            EntityProperty[] entityPropertyArray = value.properties;
            int n = entityPropertyArray.length;
            int n2 = 0;
            while (n2 < n) {
                EntityProperty entityproperty = entityPropertyArray[n2];
                EntityProperty.Serializer<EntityProperty> serializer = EntityPropertyManager.getSerializerFor(entityproperty);
                jsonobject.add(serializer.getName().toString(), serializer.serialize(entityproperty, context));
                ++n2;
            }
            json.add("properties", (JsonElement)jsonobject);
            json.add("entity", context.serialize((Object)value.target));
        }

        @Override
        public EntityHasProperty deserialize(JsonObject json, JsonDeserializationContext context) {
            Set set = JsonUtils.getJsonObject(json, "properties").entrySet();
            EntityProperty[] aentityproperty = new EntityProperty[set.size()];
            int i = 0;
            for (Map.Entry entry : set) {
                aentityproperty[i++] = EntityPropertyManager.getSerializerForName(new ResourceLocation((String)entry.getKey())).deserialize((JsonElement)entry.getValue(), context);
            }
            return new EntityHasProperty(aentityproperty, JsonUtils.deserializeClass(json, "entity", context, LootContext.EntityTarget.class));
        }
    }
}

