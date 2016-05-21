package me.modmuss50.pong;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiPlayerInfo;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.opengl.GL11;

import java.awt.*;

/**
 * Created by Mark on 21/05/2016.
 */
public class PongClient {

    public void load(){
        MinecraftForge.EVENT_BUS.register(this);
    }
    @SubscribeEvent
    public void renderElemet(RenderGameOverlayEvent event){
        if(event.type == RenderGameOverlayEvent.ElementType.PLAYER_LIST){
            event.setCanceled(true);
            Minecraft mc = Minecraft.getMinecraft();
            ScaledResolution res = event.resolution;
            int width = res.getScaledWidth();
            int height = res.getScaledHeight();
            renderPlayerList(width, height, mc);
        }
    }

    public void renderPlayerList(int width, int height, Minecraft mc)
    {
        Gui gui = mc.ingameGUI;
        FontRenderer fontrenderer = mc.fontRenderer;
        ScoreObjective scoreobjective = mc.theWorld.getScoreboard().func_96539_a(0);
        NetHandlerPlayClient handler = mc.thePlayer.sendQueue;

        if (mc.gameSettings.keyBindPlayerList.getIsKeyPressed() && (!mc.isIntegratedServerRunning() || handler.playerInfoList.size() > 1 || scoreobjective != null))
        {
            mc.mcProfiler.startSection("playerList");
            java.util.List<GuiPlayerInfo> players = (java.util.List<GuiPlayerInfo>)handler.playerInfoList;
            int maxPlayers = handler.currentServerMaxPlayers;
            int rows = maxPlayers;
            int columns = 1;

            for (columns = 1; rows > 20; rows = (maxPlayers + columns - 1) / columns)
            {
                columns++;
            }

            int columnWidth = 340 / columns;

            if (columnWidth > 150)
            {
                columnWidth = 150;
            }

            int left = (width - columns * columnWidth) / 2;
            byte border = 10;
            gui.drawRect(left - 1, border - 1, left + columnWidth * columns, border + 9 * rows, Integer.MIN_VALUE);

            for (int i = 0; i < maxPlayers; i++)
            {
                int xPos = left + i % columns * columnWidth;
                int yPos = border + i / columns * 9;
                gui.drawRect(xPos, yPos, xPos + columnWidth - 1, yPos + 8, 553648127);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                GL11.glEnable(GL11.GL_ALPHA_TEST);

                if (i < players.size())
                {
                    GuiPlayerInfo player = (GuiPlayerInfo)players.get(i);
                    ScorePlayerTeam team = mc.theWorld.getScoreboard().getPlayersTeam(player.name);
                    String displayName = ScorePlayerTeam.formatPlayerName(team, player.name);
                    fontrenderer.drawStringWithShadow(displayName, xPos, yPos, 16777215);

                    if (scoreobjective != null)
                    {
                        int endX = xPos + fontrenderer.getStringWidth(displayName) + 5;
                        int maxX = xPos + columnWidth - 12 - 5;

                        if (maxX - endX > 5)
                        {
                            Score score = scoreobjective.getScoreboard().func_96529_a(player.name, scoreobjective);
                            String scoreDisplay = EnumChatFormatting.YELLOW + "" + score.getScorePoints();
                            fontrenderer.drawStringWithShadow(scoreDisplay, maxX - fontrenderer.getStringWidth(scoreDisplay), yPos, 16777215);
                        }
                    }

                    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);


                    Color color = Color.RED;
                    if (player.responseTime < 0) {
                        color = Color.WHITE;
                    }
                    else if (player.responseTime < 60) {
                        color = Color.GREEN;
                    }
                    else if (player.responseTime < 120) {
                        color = Color.YELLOW;
                    }

                    int strL = (player.responseTime + "ms").length() * 5 + 5;

                    fontrenderer.drawString(player.responseTime + "ms", xPos + columnWidth - strL, yPos, color.getRGB());
                }
            }
        }
    }

}
