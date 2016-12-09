package me.modmuss50.pong;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

import java.util.Map;

/**
 * Created by modmuss50 on 19/05/2016.
 */
// -Dfml.coreMods.load=me.modmuss50.pong.PongASM
@IFMLLoadingPlugin.Name(value = "Pong ASM")
@IFMLLoadingPlugin.MCVersion(value = "1.10.2")
public class PongASM implements IFMLLoadingPlugin {


    public static boolean deobfuscatedEnvironment = false;

    @Override
    public String[] getASMTransformerClass() {
        return new String[] { PongClassTransformer.class.getName() };
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {
        deobfuscatedEnvironment = !((Boolean)data.get("runtimeDeobfuscationEnabled")).booleanValue();
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}
