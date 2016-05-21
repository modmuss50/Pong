package me.modmuss50.pong;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


/**
 * Created by Mark on 21/05/2016.
 */
@Mod(modid = "pong", name = "Pong", version = "2.1.0")
public class Pong {

    @SideOnly(Side.CLIENT)
    @Mod.EventHandler
    public void init(FMLInitializationEvent event){
        new PongClient().load();
    }



}
