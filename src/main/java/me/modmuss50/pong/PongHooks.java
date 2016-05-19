package me.modmuss50.pong;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;

import java.awt.*;

/**
 * Created by modmuss50 on 19/05/2016.
 */
public class PongHooks {


    public static void drawPing(int p_175245_1_, int p_175245_2_, int p_175245_3_, NetworkPlayerInfo networkPlayerInfoIn)
    {
        Color color = Color.RED;
        if (networkPlayerInfoIn.getResponseTime() < 0) {
            color = Color.WHITE;
        }
        else if (networkPlayerInfoIn.getResponseTime() < 60) {
            color = Color.GREEN;
        }
        else if (networkPlayerInfoIn.getResponseTime() < 120) {
            color = Color.YELLOW;
        }

        Minecraft.getMinecraft().fontRendererObj.drawString(networkPlayerInfoIn.getResponseTime() + "ms", p_175245_2_ + p_175245_1_ - 11, p_175245_3_, color.getRGB());
    }
}
