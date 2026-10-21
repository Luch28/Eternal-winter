// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.*;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

public class GuiMainMenu extends GuiScreen
{

    public GuiMainMenu()
    {
        updateCounter = 0.0F;
        splashText = "missingno";
        currentBg = 0;
        nextBg = 1;
        lastSwitchTime = System.currentTimeMillis();
        fadeProgress = 1.0F;
        backgroundTextures = null;
        try
        {
            ArrayList arraylist = new ArrayList();
            BufferedReader bufferedreader = new BufferedReader(new InputStreamReader((net.minecraft.src.GuiMainMenu.class).getResourceAsStream("/title/splashes.txt"), Charset.forName("UTF-8")));
            String s = "";
            do
            {
                String s1;
                if((s1 = bufferedreader.readLine()) == null)
                {
                    break;
                }
                s1 = s1.trim();
                if(s1.length() > 0)
                {
                    arraylist.add(s1);
                }
            } while(true);
            splashText = (String)arraylist.get(rand.nextInt(arraylist.size()));
        }
        catch(Exception exception) { }
    }

    public void updateScreen()
    {
        updateCounter++;

        long now = System.currentTimeMillis();
        long elapsed = now - lastSwitchTime;

        if(elapsed >= SWITCH_INTERVAL + (long)FADE_DURATION)
        {
            currentBg = nextBg;
            nextBg = (nextBg + 1) % BACKGROUND_COUNT;
            lastSwitchTime = now;
            fadeProgress = 1.0F;
        } else
        if(elapsed >= SWITCH_INTERVAL)
        {
            fadeProgress = 1.0F - ((float)(elapsed - SWITCH_INTERVAL) / FADE_DURATION);
        } else
        {
            fadeProgress = 1.0F;
        }
    }

    protected void keyTyped(char c, int i)
    {
    }

    public void initGui()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        if(calendar.get(2) + 1 == 11 && calendar.get(5) == 9)
        {
            splashText = "Happy birthday, ez!";
        } else
        if(calendar.get(2) + 1 == 6 && calendar.get(5) == 1)
        {
            splashText = "Happy birthday, Notch!";
        } else
        if(calendar.get(2) + 1 == 12 && calendar.get(5) == 24)
        {
            splashText = "Merry X-mas!";
        } else
        if(calendar.get(2) + 1 == 1 && calendar.get(5) == 1)
        {
            splashText = "Happy new year!";
        }
        StringTranslate stringtranslate = StringTranslate.getInstance();
        int i = height / 4 + 48;
        controlList.add(new GuiButton(1, width / 2 - 100, i, stringtranslate.translateKey("menu.singleplayer")));
        controlList.add(multiplayerButton = new GuiButton(2, width / 2 - 100, i + 24, stringtranslate.translateKey("menu.multiplayer")));
        controlList.add(new GuiButton(3, width / 2 - 100, i + 48, stringtranslate.translateKey("menu.mods")));
        if(mc.hideQuitButton)
        {
            controlList.add(new GuiButton(0, width / 2 - 100, i + 72, stringtranslate.translateKey("menu.options")));
        } else
        {
            controlList.add(new GuiButton(0, width / 2 - 100, i + 72 + 12, 98, 20, stringtranslate.translateKey("menu.options")));
            controlList.add(new GuiButton(4, width / 2 + 2, i + 72 + 12, 98, 20, stringtranslate.translateKey("menu.quit")));
        }
        if(mc.session == null)
        {
            multiplayerButton.enabled = false;
        }
    }

    protected void actionPerformed(GuiButton guibutton)
    {
        if(guibutton.id == 0)
        {
            mc.displayGuiScreen(new GuiOptions(this, mc.gameSettings));
        }
        if(guibutton.id == 1)
        {
            mc.displayGuiScreen(new GuiSelectWorld(this));
        }
        if(guibutton.id == 2)
        {
            mc.displayGuiScreen(new GuiMultiplayer(this));
        }
        if(guibutton.id == 3)
        {
            mc.displayGuiScreen(new GuiTexturePacks(this));
        }
        if(guibutton.id == 4)
        {
            mc.shutdown();
        }
    }

    private void loadBackgrounds()
    {
        if(backgroundTextures != null)
        {
            return;
        }
        backgroundTextures = new int[BACKGROUND_COUNT];
        for(int i = 0; i < BACKGROUND_COUNT; i++)
        {
            backgroundTextures[i] = mc.renderEngine.getTexture("/title/backgrounds/bg" + i + ".png");
        }
    }

    private void drawBackgroundQuad(int bgIndex)
    {
        float bgAspect = BG_ASPECTS[bgIndex];
        float screenAspect = (float)width / (float)height;
        float drawW;
        float drawH;
        float drawX;
        float drawY;

        if(screenAspect > bgAspect)
        {
            drawH = height;
            drawW = height * bgAspect;
            drawX = (width - drawW) / 2.0F;
            drawY = 0.0F;
        } else
        {
            drawW = width;
            drawH = width / bgAspect;
            drawX = 0.0F;
            drawY = (height - drawH) / 2.0F;
        }

        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(drawX, drawY + drawH, 0, 0.0D, 1.0D);
        tessellator.addVertexWithUV(drawX + drawW, drawY + drawH, 0, 1.0D, 1.0D);
        tessellator.addVertexWithUV(drawX + drawW, drawY, 0, 1.0D, 0.0D);
        tessellator.addVertexWithUV(drawX, drawY, 0, 0.0D, 0.0D);
        tessellator.draw();
    }

    private void drawBackground()
    {
        loadBackgrounds();
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_FOG);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        GL11.glOrtho(0.0D, width, height, 0.0D, 1000D, 3000D);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        GL11.glTranslatef(0.0F, 0.0F, -2000F);

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glColor4f(0.0F, 0.0F, 0.0F, 1.0F);
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertex(0, height, 0);
        tessellator.addVertex(width, height, 0);
        tessellator.addVertex(width, 0, 0);
        tessellator.addVertex(0, 0, 0);
        tessellator.draw();

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, backgroundTextures[currentBg]);
        drawBackgroundQuad(currentBg);
        if(fadeProgress < 1.0F)
        {
            float alpha = 1.0F - fadeProgress;
            GL11.glColor4f(1.0F, 1.0F, 1.0F, alpha);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, backgroundTextures[nextBg]);
            drawBackgroundQuad(nextBg);
        }
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glPopMatrix();
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glPopMatrix();
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
    }

    public void drawScreen(int i, int j, float f)
    {
        drawBackground();
        Tessellator tessellator = Tessellator.instance;
        char c = '\u0112';
        int k = width / 2 - c / 2;
        byte byte0 = 30;
        GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, mc.renderEngine.getTexture("/title/mclogo.png"));
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        drawTexturedModalRect(k + 0, byte0 + 0, 0, 0, 155, 44);
        drawTexturedModalRect(k + 155, byte0 + 0, 0, 45, 155, 44);
        tessellator.setColorOpaque_I(0xffffff);
        GL11.glPushMatrix();
        GL11.glTranslatef(width / 2 + 90, 70F, 0.0F);
        GL11.glRotatef(-20F, 0.0F, 0.0F, 1.0F);
        float f1 = 1.8F - MathHelper.abs(MathHelper.sin(((float)(System.currentTimeMillis() % 1000L) / 1000F) * 3.141593F * 2.0F) * 0.1F);
        f1 = (f1 * 100F) / (float)(fontRenderer.getStringWidth(splashText) + 32);
        GL11.glScalef(f1, f1, f1);
        drawCenteredString(fontRenderer, splashText, 0, -8, 0xffff00);
        GL11.glPopMatrix();
        drawString(fontRenderer, "Eternal Winter", 2, 2, 0xffffff);
        String s = "Fork Beta 1.7.3";
        drawString(fontRenderer, s, width - fontRenderer.getStringWidth(s) - 2, height - 10, 0xffffff);
        super.drawScreen(i, j, f);
    }

    private static final Random rand = new Random();
    private static final int BACKGROUND_COUNT = 3;
    private static final long SWITCH_INTERVAL = 30000L;
    private static final float FADE_DURATION = 2000F;
    private static final float BG_ASPECTS[] = {
        16.0F / 9.0F, 16.0F / 9.0F, 16.0F / 9.0F
    };
    private int[] backgroundTextures;
    private int currentBg;
    private int nextBg;
    private long lastSwitchTime;
    private float fadeProgress;
    private float updateCounter;
    private String splashText;
    private GuiButton multiplayerButton;

}