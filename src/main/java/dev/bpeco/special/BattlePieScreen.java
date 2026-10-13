package dev.bpeco.special;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

import java.util.HashMap;
import java.util.Map;

public class BattlePieScreen extends Screen {
    private static final Map<String, Boolean> toggles = new HashMap<>();
    
    // Default values for HUD
    static {
        toggles.put("Coordinates HUD", true);
        toggles.put("FPS Counter", true);
        toggles.put("Direction HUD", true);
        toggles.put("FullBright", false);
    }

    private String currentTab = "Combat";

    public BattlePieScreen() {
        super(Text.literal("BattlePie-ECO"));
    }

    public static boolean isEnabled(String featureName) {
        return toggles.getOrDefault(featureName, false);
    }

    @Override
    protected void init() {
        super.init();
        rebuildWidgets();
    }

    private void rebuildWidgets() {
        this.clearChildren();

        int startX = (this.width - 380) / 2;
        int startY = (this.height - 280) / 2;

        // Tabs
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Combat"), button -> {
            currentTab = "Combat";
            rebuildWidgets();
        }).dimensions(startX + 10, startY + 30, 90, 20).build());

        this.addDrawableChild(ButtonWidget.builder(Text.literal("World"), button -> {
            currentTab = "World";
            rebuildWidgets();
        }).dimensions(startX + 105, startY + 30, 90, 20).build());

        this.addDrawableChild(ButtonWidget.builder(Text.literal("TPs"), button -> {
            currentTab = "TPs";
            rebuildWidgets();
        }).dimensions(startX + 200, startY + 30, 80, 20).build());

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Game"), button -> {
            currentTab = "Game";
            rebuildWidgets();
        }).dimensions(startX + 285, startY + 30, 85, 20).build());

        // Feature Buttons based on active tab
        int btnY = startY + 65;
        if (currentTab.equals("Combat")) {
            addFeatureButton("AutoMace", startX + 10, btnY);
            addFeatureButton("Aim Assist", startX + 10, btnY + 25);
            addFeatureButton("AutoShieldBreak", startX + 10, btnY + 50);
            addFeatureButton("AutoWindCharge", startX + 10, btnY + 75);
            addFeatureButton("TriggerBot", startX + 10, btnY + 100);
        } else if (currentTab.equals("World")) {
            addFeatureButton("Freecam", startX + 10, btnY);
            addFeatureButton("FullBright", startX + 10, btnY + 25);
            addFeatureButton("Walk Trail", startX + 10, btnY + 50);
            addFeatureButton("Chest ESP", startX + 10, btnY + 75);
            addFeatureButton("Item ESP", startX + 10, btnY + 100);
            addFeatureButton("Entity ESP", startX + 10, btnY + 125);
            addFeatureButton("Xray", startX + 10, btnY + 150);
        } else if (currentTab.equals("TPs")) {
            addFeatureButton("Auto TPahere", startX + 10, btnY);
            addFeatureButton("Suspicious Chunk Highlight", startX + 10, btnY + 25);
        } else if (currentTab.equals("Game")) {
            addFeatureButton("Coordinates HUD", startX + 10, btnY);
            addFeatureButton("FPS Counter", startX + 10, btnY + 25);
            addFeatureButton("Direction HUD", startX + 10, btnY + 50);
        }

        // Close Button
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Close"), button -> {
            if (this.client != null) {
                this.client.setScreen(null);
            }
        }).dimensions(startX + 270, startY + 245, 100, 20).build());
    }

    private void addFeatureButton(String name, int x, int y) {
        boolean state = isEnabled(name);
        this.addDrawableChild(ButtonWidget.builder(
                Text.literal(name + " [" + (state ? "ON" : "OFF") + "]"),
                button -> {
                    toggles.put(name, !state);
                    rebuildWidgets();
                }
        ).dimensions(x, y, 360, 20).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        int startX = (this.width - 380) / 2;
        int startY = (this.height - 280) / 2;

        // Background panel
        context.fill(startX, startY, startX + 380, startY + 280, 0xFF0D1b2a);
        // Cyan Header/Border accent
        context.fill(startX, startY, startX + 380, startY + 25, 0xFF00f5d4);
        context.drawText(this.textRenderer, Text.literal("BattlePie-ECO"), startX + 10, startY + 8, 0xFF000000, false);

        // Footer info
        context.drawText(this.textRenderer, Text.literal("Right Shift: Close GUI"), startX + 10, startY + 245, 0xFF888888, false);
        context.drawText(this.textRenderer, Text.literal("Cyan Edition"), startX + 10, startY + 260, 0xFF00f5d4, false);

        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
