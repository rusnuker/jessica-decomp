/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParser
 *  org.apache.commons.io.IOUtils
 */
package shadersmod.client;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.data.AnimationMetadataSection;
import net.minecraft.client.resources.data.AnimationMetadataSectionSerializer;
import net.minecraft.client.resources.data.FontMetadataSection;
import net.minecraft.client.resources.data.FontMetadataSectionSerializer;
import net.minecraft.client.resources.data.LanguageMetadataSection;
import net.minecraft.client.resources.data.LanguageMetadataSectionSerializer;
import net.minecraft.client.resources.data.MetadataSerializer;
import net.minecraft.client.resources.data.PackMetadataSection;
import net.minecraft.client.resources.data.PackMetadataSectionSerializer;
import net.minecraft.client.resources.data.TextureMetadataSection;
import net.minecraft.client.resources.data.TextureMetadataSectionSerializer;
import org.apache.commons.io.IOUtils;
import shadersmod.client.Shaders;
import shadersmod.common.SMCLog;

public class SimpleShaderTexture
extends AbstractTexture {
    private String texturePath;
    private static final MetadataSerializer METADATA_SERIALIZER = SimpleShaderTexture.makeMetadataSerializer();

    public SimpleShaderTexture(String texturePath) {
        this.texturePath = texturePath;
    }

    @Override
    public void loadTexture(IResourceManager resourceManager) throws IOException {
        this.deleteGlTexture();
        InputStream inputstream = Shaders.getShaderPackResourceStream(this.texturePath);
        if (inputstream == null) {
            throw new FileNotFoundException("Shader texture not found: " + this.texturePath);
        }
        try {
            BufferedImage bufferedimage = TextureUtil.readBufferedImage(inputstream);
            TextureMetadataSection texturemetadatasection = this.loadTextureMetadataSection();
            TextureUtil.uploadTextureImageAllocate(this.getGlTextureId(), bufferedimage, texturemetadatasection.getTextureBlur(), texturemetadatasection.getTextureClamp());
        }
        finally {
            IOUtils.closeQuietly((InputStream)inputstream);
        }
    }

    private TextureMetadataSection loadTextureMetadataSection() {
        String s = String.valueOf(this.texturePath) + ".mcmeta";
        String s1 = "texture";
        InputStream inputstream = Shaders.getShaderPackResourceStream(s);
        if (inputstream != null) {
            TextureMetadataSection texturemetadatasection1;
            MetadataSerializer metadataserializer = METADATA_SERIALIZER;
            BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(inputstream));
            try {
                JsonObject jsonobject = new JsonParser().parse((Reader)bufferedreader).getAsJsonObject();
                TextureMetadataSection texturemetadatasection = (TextureMetadataSection)metadataserializer.parseMetadataSection(s1, jsonobject);
                if (texturemetadatasection == null) {
                    TextureMetadataSection textureMetadataSection = new TextureMetadataSection(false, false);
                    return textureMetadataSection;
                }
                try {
                    texturemetadatasection1 = texturemetadatasection;
                }
                catch (RuntimeException runtimeexception) {
                    SMCLog.warning("Error reading metadata: " + s);
                    SMCLog.warning(runtimeexception.getClass().getName() + ": " + runtimeexception.getMessage());
                    TextureMetadataSection textureMetadataSection = new TextureMetadataSection(false, false);
                    return textureMetadataSection;
                }
            }
            finally {
                IOUtils.closeQuietly((Reader)bufferedreader);
                IOUtils.closeQuietly((InputStream)inputstream);
            }
            return texturemetadatasection1;
        }
        return new TextureMetadataSection(false, false);
    }

    private static MetadataSerializer makeMetadataSerializer() {
        MetadataSerializer metadataserializer = new MetadataSerializer();
        metadataserializer.registerMetadataSectionType(new TextureMetadataSectionSerializer(), TextureMetadataSection.class);
        metadataserializer.registerMetadataSectionType(new FontMetadataSectionSerializer(), FontMetadataSection.class);
        metadataserializer.registerMetadataSectionType(new AnimationMetadataSectionSerializer(), AnimationMetadataSection.class);
        metadataserializer.registerMetadataSectionType(new PackMetadataSectionSerializer(), PackMetadataSection.class);
        metadataserializer.registerMetadataSectionType(new LanguageMetadataSectionSerializer(), LanguageMetadataSection.class);
        return metadataserializer;
    }
}

