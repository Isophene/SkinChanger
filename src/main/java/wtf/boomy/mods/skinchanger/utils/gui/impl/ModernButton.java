/*
 *     Copyright (C) 2020 boomboompower
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package wtf.boomy.mods.skinchanger.utils.gui.impl;

import lombok.Getter;
import lombok.Setter;

import wtf.boomy.mods.skinchanger.utils.gui.InteractiveUIElement;
import wtf.boomy.mods.skinchanger.utils.gui.StartEndUIElement;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

/**
 * ModernButton, a nicer looking button
 *
 * @author boomboompower
 *
 * @version 2.0
 * @since 3.0.0
 */
public class ModernButton extends Gui implements InteractiveUIElement, StartEndUIElement {

    protected static final ResourceLocation buttonTextures = new ResourceLocation("textures/gui/widgets.png");

    @Getter
    private final int id;

    @Getter
    private int width;

    @Getter
    private int height;

    private final int xPosition;
    private final int yPosition;

    @Getter
    private boolean enabled;

    @Setter
    @Getter
    private boolean visible;

    @Getter
    @Setter
    private boolean favourite;

    @Getter
    private boolean hovered;

    private String displayString;

    private Color enabledColor = null;
    private Color disabledColor = null;

    @Getter
    private Object buttonData;

    @Getter
    private boolean partOfHeader;

    private ModernHeader parentHeader;
    private int recommendedYPosition;

    @Getter
    private boolean translatable;

    public ModernButton(int buttonId, int x, int y, String buttonText) {
        this(buttonId, x, y, 200, 20, buttonText);
    }

    public ModernButton(int buttonId, String idName, int x, int y, String buttonText) {
        this(buttonId, x, y, 200, 20, buttonText);
    }

    public ModernButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
        this.width = 200;
        this.height = 20;
        this.enabled = true;
        this.visible = true;
        this.id = buttonId;
        this.xPosition = x;
        this.yPosition = y;
        this.width = widthIn;
        this.height = heightIn;
        this.displayString = buttonText;
    }

    @Override
    public int getX() {
        return this.xPosition;
    }

    @Override
    public int getY() {
        return this.yPosition;
    }

    @Override
    public void render(int mouseX, int mouseY, float yTranslation) {
        if (this.visible) {
            FontRenderer fontrenderer = Minecraft.getMinecraft().fontRendererObj;

            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            int xPosition = this.xPosition;
            int yPosition = this.yPosition;

            this.hovered = mouseX >= xPosition && mouseY >= yPosition && mouseX < xPosition + this.width && mouseY < yPosition + this.height;
            int i = this.getHoverState(this.hovered);

            int textColor = 14737632;

            if (this.enabled) {
                drawRect(xPosition, yPosition, xPosition + this.width, yPosition + this.height, getEnabledColor().getRGB());
            } else {
                drawRect(xPosition, yPosition, xPosition + this.width, yPosition + this.height, getDisabledColor().getRGB());
            }

            renderButtonString(fontrenderer, xPosition, yPosition, textColor);
        }
    }

    @Override
    public void renderFromHeader(int xPos, int yPos, float yTranslation, int mouseX, int mouseY, int recommendedYOffset) {
        if (this.visible) {
            FontRenderer fontrenderer = Minecraft.getMinecraft().fontRendererObj;

            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            int xPosition = xPos + 5;
            int yPosition = yPos + recommendedYOffset;

            this.recommendedYPosition = yPosition;

            this.hovered = isInside(mouseX, mouseY, yTranslation);

            int i = this.getHoverState(this.hovered);

            int j = 14737632;

            if (this.enabled) {
                drawRect(xPosition, yPosition, xPosition + this.width, yPosition + height, getEnabledColor().getRGB());
            } else {
                drawRect(xPosition, yPosition, xPosition + this.width, yPosition + height, getDisabledColor().getRGB());
            }

            renderButtonString(fontrenderer, xPosition, yPosition, j);
        }
    }

    @Override
    public void onLeftClick(int mouseX, int mouseY, float yTranslation) {
        Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
    }

    @Override
    public boolean isInside(int mouseX, int mouseY, float yTranslation) {
        if (!this.visible) {
            return false;
        }

        int xPosition = this.xPosition;
        int yPosition = this.yPosition;

        if (this.partOfHeader) {
            yPosition = this.recommendedYPosition;
            xPosition += this.parentHeader.getX();
        }

        yPosition += yTranslation;

        return mouseX >= xPosition && mouseY >= yPosition && mouseX < xPosition + this.width && mouseY < yPosition + this.height;
    }

    @Override
    public void setAsPartOfHeader(ModernHeader parent) {
        this.partOfHeader = true;

        this.parentHeader = parent;
    }

    @Override
    public InteractiveUIElement disableTranslatable() {
        this.translatable = false;

        return this;
    }

    /**
     * Returns 0 if the button is disabled, 1 if the mouse is NOT hovering over this button and 2 if it IS hovering over
     * this button.
     *
     * @param mouseOver true if the mouse is over this button
     * @return an integer state based on what the color of this button should be
     */
    protected int getHoverState(boolean mouseOver) {
        int state = 1;

        if (!this.enabled) {
            state = 0;
        } else if (mouseOver) {
            state = 2;
        }
        return state;
    }

    public Color getEnabledColor() {
        return this.enabledColor == null ? new Color(255, 255, 255, 75) : this.enabledColor;
    }

    public ModernButton setEnabledColor(Color colorIn) {
        this.enabledColor = colorIn;

        return this;
    }

    public Color getDisabledColor() {
        return this.disabledColor == null ? new Color(100, 100, 100, 75) : this.disabledColor;
    }

    public ModernButton setDisabledColor(Color colorIn) {
        this.disabledColor = colorIn;

        return this;
    }

    public String getText() {
        return this.displayString;
    }

    public void setText(String text) {
        this.displayString = text != null ? text : "";
    }

    public ModernButton setEnabled(boolean isEnabled) {
        this.enabled = isEnabled;

        return this;
    }

    public boolean hasButtonData() {
        return this.buttonData != null;
    }

    public Object setButtonData(Object buttonData) {
        this.buttonData = buttonData;

        return this;
    }

    /**
     * Renders the string of the button
     *
     * @param fontrenderer the FontRenderer object
     * @param xPosition the x position of the button
     * @param yPosition the y position of the button
     * @param textColor the color of the text
     */
    private void renderButtonString(FontRenderer fontrenderer, int xPosition, int yPosition, int textColor) {
        if (!this.enabled) {
            textColor = 10526880;
        } else if (this.hovered) {
            textColor = 16777120;
        }

        if (this.enabled && this.favourite) {
            fontrenderer.drawString("\u2726", xPosition + this.width - fontrenderer.getStringWidth("\u2726") - 4, yPosition + ((fontrenderer.FONT_HEIGHT / 2) + 2), Color.ORANGE.getRGB());
        }

        fontrenderer.drawString(this.displayString, (xPosition + (float) this.width / 2 - (float) fontrenderer.getStringWidth(this.displayString) / 2), yPosition + ((float) this.height - 8) / 2, textColor, false);
    }
}