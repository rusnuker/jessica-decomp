/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.HashMultimap
 *  com.google.common.collect.Maps
 *  com.google.common.collect.Multimap
 *  javax.annotation.Nullable
 */
package net.minecraft.entity.ai.attributes;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import java.util.Collection;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.util.LowerStringMap;

public abstract class AbstractAttributeMap {
    protected final Map<IAttribute, IAttributeInstance> attributes = Maps.newHashMap();
    protected final Map<String, IAttributeInstance> attributesByName = new LowerStringMap<IAttributeInstance>();
    protected final Multimap<IAttribute, IAttribute> descendantsByParent = HashMultimap.create();

    public IAttributeInstance getAttributeInstance(IAttribute attribute) {
        return this.attributes.get(attribute);
    }

    @Nullable
    public IAttributeInstance getAttributeInstanceByName(String attributeName) {
        return this.attributesByName.get(attributeName);
    }

    public IAttributeInstance registerAttribute(IAttribute attribute) {
        if (this.attributesByName.containsKey(attribute.getAttributeUnlocalizedName())) {
            throw new IllegalArgumentException("Attribute is already registered!");
        }
        IAttributeInstance iattributeinstance = this.createInstance(attribute);
        this.attributesByName.put(attribute.getAttributeUnlocalizedName(), iattributeinstance);
        this.attributes.put(attribute, iattributeinstance);
        IAttribute iattribute = attribute.getParent();
        while (iattribute != null) {
            this.descendantsByParent.put((Object)iattribute, (Object)attribute);
            iattribute = iattribute.getParent();
        }
        return iattributeinstance;
    }

    protected abstract IAttributeInstance createInstance(IAttribute var1);

    public Collection<IAttributeInstance> getAllAttributes() {
        return this.attributesByName.values();
    }

    public void onAttributeModified(IAttributeInstance instance) {
    }

    public void removeAttributeModifiers(Multimap<String, AttributeModifier> modifiers) {
        for (Map.Entry entry : modifiers.entries()) {
            IAttributeInstance iattributeinstance = this.getAttributeInstanceByName((String)entry.getKey());
            if (iattributeinstance == null) continue;
            iattributeinstance.removeModifier((AttributeModifier)entry.getValue());
        }
    }

    public void applyAttributeModifiers(Multimap<String, AttributeModifier> modifiers) {
        for (Map.Entry entry : modifiers.entries()) {
            IAttributeInstance iattributeinstance = this.getAttributeInstanceByName((String)entry.getKey());
            if (iattributeinstance == null) continue;
            iattributeinstance.removeModifier((AttributeModifier)entry.getValue());
            iattributeinstance.applyModifier((AttributeModifier)entry.getValue());
        }
    }
}

