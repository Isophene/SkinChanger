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

package me.boomboompower.skinchanger.gui;

import me.boomboompower.skinchanger.SkinChangerModOld;
import me.boomboompower.skinchanger.gui.experimental.GuiExperimentalAllPlayers;
import me.boomboompower.skinchanger.gui.experimental.GuiExperimentalOptifine;

import me.do_you_like.mods.skinchanger.utils.game.ChatColor;
import me.do_you_like.mods.skinchanger.utils.gui.impl.ModernButton;
import me.do_you_like.mods.skinchanger.utils.gui.ModernGui;

import org.apache.commons.io.FileUtils;

import java.awt.*;
import java.io.File;
import java.util.Arrays;

@SuppressWarnings("ALL")
@Deprecated
public class ExperimentalGui extends ModernGui {
    
    private final SkinChangerModOld mod;
    
    private ModernButton skinCache;
    
    public ExperimentalGui(SkinChangerModOld mod) {
        this.mod = mod;
    }

    @Override
    public void onGuiOpen() {
        registerElement(new ModernButton(0, this.width / 2 - 75, this.height / 2 - 22, 150, 20,
            "Rending: " + (SkinChangerModOld.getInstance().isRenderingEnabled() ? ChatColor.GREEN + "On" : ChatColor.GRAY + "Off")));
        registerElement(new ModernButton(1, this.width / 2 - 75, this.height / 2 + 2, 150, 20, "All player utils"));
        registerElement(new ModernButton(2, this.width / 2 - 75, this.height / 2 + 26, 150, 20, "Optifine utils"));
        registerElement(this.skinCache = new ModernButton(3, this.width / 2 - 75, this.height / 2 + 50, 150, 20, "Delete skin cache"));

        this.skinCache.setEnabledColor(new Color(255, 0, 0, 75));
    }

    @Override
    public void onRender(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();

        super.drawScreen(mouseX, mouseY, partialTicks);

        if (this.skinCache.isHovered()) {
            drawHoveringText(Arrays.asList("This button is dangerous and", ChatColor.DARK_RED.toString() + ChatColor.BOLD + "may" + ChatColor.RESET + " be bad for your game"), mouseX, mouseY);
        }
    }

    @Override
    public void buttonPressed(ModernButton button) {
        switch (button.getId()) {
            case 0:
                SkinChangerModOld
                    .getInstance().setRenderingEnabled(!SkinChangerModOld.getInstance().isRenderingEnabled());
                button.setText("Rending: " + (SkinChangerModOld.getInstance().isRenderingEnabled() ? ChatColor.GREEN + "On" : ChatColor.GRAY + "Off"));
                break;
            case 1:
                this.mc.displayGuiScreen(new GuiExperimentalAllPlayers(this.mod));
                break;
            case 2:
                this.mc.displayGuiScreen(new GuiExperimentalOptifine());
                break;
            case 3:
                FileUtils.deleteQuietly(new File("./mods/skinchanger".replace("/", File.separator), "skins"));
                break;
            default:
                this.mc.displayGuiScreen(null);
                break;
        }
    }
}
