/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonArray
 *  com.google.gson.JsonDeserializationContext
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonPrimitive
 *  com.google.gson.JsonSerializationContext
 *  com.google.gson.JsonSyntaxException
 *  javax.annotation.Nullable
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package net.minecraft.world.storage.loot.functions;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSyntaxException;
import java.util.Random;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SetAttributes
extends LootFunction {
    private static final Logger LOGGER = LogManager.getLogger();
    private final Modifier[] modifiers;

    public SetAttributes(LootCondition[] conditionsIn, Modifier[] modifiersIn) {
        super(conditionsIn);
        this.modifiers = modifiersIn;
    }

    @Override
    public ItemStack apply(ItemStack stack, Random rand, LootContext context) {
        Modifier[] modifierArray = this.modifiers;
        int n = this.modifiers.length;
        int n2 = 0;
        while (n2 < n) {
            Modifier setattributes$modifier = modifierArray[n2];
            UUID uuid = setattributes$modifier.uuid;
            if (uuid == null) {
                uuid = UUID.randomUUID();
            }
            EntityEquipmentSlot entityequipmentslot = setattributes$modifier.slots[rand.nextInt(setattributes$modifier.slots.length)];
            stack.addAttributeModifier(setattributes$modifier.attributeName, new AttributeModifier(uuid, setattributes$modifier.modifierName, setattributes$modifier.amount.generateFloat(rand), setattributes$modifier.operation), entityequipmentslot);
            ++n2;
        }
        return stack;
    }

    static class Modifier {
        private final String modifierName;
        private final String attributeName;
        private final int operation;
        private final RandomValueRange amount;
        @Nullable
        private final UUID uuid;
        private final EntityEquipmentSlot[] slots;

        private Modifier(String modifName, String attrName, int operationIn, RandomValueRange randomAmount, EntityEquipmentSlot[] slotsIn, @Nullable UUID uuidIn) {
            this.modifierName = modifName;
            this.attributeName = attrName;
            this.operation = operationIn;
            this.amount = randomAmount;
            this.uuid = uuidIn;
            this.slots = slotsIn;
        }

        public JsonObject serialize(JsonSerializationContext context) {
            JsonObject jsonobject = new JsonObject();
            jsonobject.addProperty("name", this.modifierName);
            jsonobject.addProperty("attribute", this.attributeName);
            jsonobject.addProperty("operation", Modifier.getOperationFromStr(this.operation));
            jsonobject.add("amount", context.serialize((Object)this.amount));
            if (this.uuid != null) {
                jsonobject.addProperty("id", this.uuid.toString());
            }
            if (this.slots.length == 1) {
                jsonobject.addProperty("slot", this.slots[0].getName());
            } else {
                JsonArray jsonarray = new JsonArray();
                EntityEquipmentSlot[] entityEquipmentSlotArray = this.slots;
                int n = this.slots.length;
                int n2 = 0;
                while (n2 < n) {
                    EntityEquipmentSlot entityequipmentslot = entityEquipmentSlotArray[n2];
                    jsonarray.add((JsonElement)new JsonPrimitive(entityequipmentslot.getName()));
                    ++n2;
                }
                jsonobject.add("slot", (JsonElement)jsonarray);
            }
            return jsonobject;
        }

        public static Modifier deserialize(JsonObject jsonObj, JsonDeserializationContext context) {
            EntityEquipmentSlot[] aentityequipmentslot;
            String s = JsonUtils.getString(jsonObj, "name");
            String s1 = JsonUtils.getString(jsonObj, "attribute");
            int i = Modifier.getOperationFromInt(JsonUtils.getString(jsonObj, "operation"));
            RandomValueRange randomvaluerange = JsonUtils.deserializeClass(jsonObj, "amount", context, RandomValueRange.class);
            UUID uuid = null;
            if (JsonUtils.isString(jsonObj, "slot")) {
                aentityequipmentslot = new EntityEquipmentSlot[]{EntityEquipmentSlot.fromString(JsonUtils.getString(jsonObj, "slot"))};
            } else {
                if (!JsonUtils.isJsonArray(jsonObj, "slot")) {
                    throw new JsonSyntaxException("Invalid or missing attribute modifier slot; must be either string or array of strings.");
                }
                JsonArray jsonarray = JsonUtils.getJsonArray(jsonObj, "slot");
                aentityequipmentslot = new EntityEquipmentSlot[jsonarray.size()];
                int j = 0;
                for (JsonElement jsonelement : jsonarray) {
                    aentityequipmentslot[j++] = EntityEquipmentSlot.fromString(JsonUtils.getString(jsonelement, "slot"));
                }
                if (aentityequipmentslot.length == 0) {
                    throw new JsonSyntaxException("Invalid attribute modifier slot; must contain at least one entry.");
                }
            }
            if (jsonObj.has("id")) {
                String s2 = JsonUtils.getString(jsonObj, "id");
                try {
                    uuid = UUID.fromString(s2);
                }
                catch (IllegalArgumentException var12) {
                    throw new JsonSyntaxException("Invalid attribute modifier id '" + s2 + "' (must be UUID format, with dashes)");
                }
            }
            return new Modifier(s, s1, i, randomvaluerange, aentityequipmentslot, uuid);
        }

        private static String getOperationFromStr(int operationIn) {
            switch (operationIn) {
                case 0: {
                    return "addition";
                }
                case 1: {
                    return "multiply_base";
                }
                case 2: {
                    return "multiply_total";
                }
            }
            throw new IllegalArgumentException("Unknown operation " + operationIn);
        }

        private static int getOperationFromInt(String operationIn) {
            if ("addition".equals(operationIn)) {
                return 0;
            }
            if ("multiply_base".equals(operationIn)) {
                return 1;
            }
            if ("multiply_total".equals(operationIn)) {
                return 2;
            }
            throw new JsonSyntaxException("Unknown attribute modifier operation " + operationIn);
        }
    }

    public static class Serializer
    extends LootFunction.Serializer<SetAttributes> {
        public Serializer() {
            super(new ResourceLocation("set_attributes"), SetAttributes.class);
        }

        @Override
        public void serialize(JsonObject object, SetAttributes functionClazz, JsonSerializationContext serializationContext) {
            JsonArray jsonarray = new JsonArray();
            Modifier[] modifierArray = functionClazz.modifiers;
            int n = modifierArray.length;
            int n2 = 0;
            while (n2 < n) {
                Modifier setattributes$modifier = modifierArray[n2];
                jsonarray.add((JsonElement)setattributes$modifier.serialize(serializationContext));
                ++n2;
            }
            object.add("modifiers", (JsonElement)jsonarray);
        }

        @Override
        public SetAttributes deserialize(JsonObject object, JsonDeserializationContext deserializationContext, LootCondition[] conditionsIn) {
            JsonArray jsonarray = JsonUtils.getJsonArray(object, "modifiers");
            Modifier[] asetattributes$modifier = new Modifier[jsonarray.size()];
            int i = 0;
            for (JsonElement jsonelement : jsonarray) {
                asetattributes$modifier[i++] = Modifier.deserialize(JsonUtils.getJsonObject(jsonelement, "modifier"), deserializationContext);
            }
            if (asetattributes$modifier.length == 0) {
                throw new JsonSyntaxException("Invalid attribute modifiers array; cannot be empty");
            }
            return new SetAttributes(conditionsIn, asetattributes$modifier);
        }
    }
}

