package dev.bpeco.special;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.HashMap;
import java.util.Map;

public class BattlePieScreen extends Screen {

    private static int activeTab = 0;

    private static final int CYAN = 0xFF35E5F2;
    private static final int DARK = 0xFF101820;
    private static final int WHITE = 0xFFEAFBFF;
    private static final int MUTED = 0xFF9CB5BE;

    private static final String[] TABS = {
        "Combat", "World", "TPs", "Game"
    };

    private static final String[][] FEATURES = {
        {
            "AutoMace",
            "Aim Assist",
            "AutoShieldBreak",
            "AutoWindCharge",
            "TriggerBot"
        },
        {
            "Freecam",
            "FullBright",
            "Walk Trail",
            "Chest ESP",
            "Item ESP",
            "Entity ESP",
            "Xray"
        },
        {
            "Auto TPahere",
            "Suspicious Chunk Highlight"
        },
        {
            "Coordinates HUD",
            "FPS Counter",
            "Direction HUD"
        }
    };

    private static final Map<String, Boolean> STATES =
        new HashMap<>();

    public BattlePieScreen() {
        super(Component.literal("BattlePie-ECO"));
    }

    public static boolean isEnabled(String feature) {
        boolean defaultValue =
            feature.equals("Coordinates HUD")
            || feature.equals("FPS Counter")
            || feature.equals("Direction HUD");

        return STATES.getOrDefault(feature, defaultValue);
    }

    private static String getButtonText(String feature) {
        return feature + "  [" +
            (isEnabled(feature) ? "ON" : "OFF") + "]";
    }

    @Override
    protected void init() {
        int panelWidth = Math.min(380, this.width - 20);
        int left = (this.width - panelWidth) / 2;
        int top = Math.max(8, (this.height - 280) / 2);

        int gap = 4;
        int tabWidth = (panelWidth - 24 - gap * 3) / 4;

        // Tabs
        for (int i = 0; i < TABS.length; i++) {
            final int tab = i;

            this.addRenderableWidget(
                Button.builder(
                    Component.literal(TABS[i]),
                    button -> {
                        activeTab = tab;
                        Minecraft.getInstance().gui.setScreen(
                            new BattlePieScreen()
                        );
                    }
                ).bounds(
                    left + 12 + i * (tabWidth + gap),
                    top + 36,
                    tabWidth,
                    20
                ).build()
            );
        }

        // Feature toggles
        String[] features = FEATURES[activeTab];

        for (int i = 0; i < features.length; i++) {
            String feature = features[i];

            this.addRenderableWidget(
                Button.builder(
                    Component.literal(getButtonText(feature)),
                    button -> {
                        STATES.put(feature, !isEnabled(feature));

                        button.setMessage(
                            Component.literal(getButtonText(feature))
                        );
                    }
                ).bounds(
                    left + 12,
                    top + 64 + i * 22,
                    panelWidth - 24,
                    20
                ).build()
            );
        }

        // Close button
        this.addRenderableWidget(
            Button.builder(
                Component.literal("Close"),
                button -> onClose()
            ).bounds(
                left + panelWidth - 102,
                top + 238,
                90,
                20
            ).build()
        );
    }

    @Override
    public void extractRenderState(
        GuiGraphicsExtractor graphics,
        int mouseX,
        int mouseY,
        float delta
    ) {
        super.extractRenderState(
            graphics,
            mouseX,
            mouseY,
            delta
        );

        int panelWidth = Math.min(380, this.width - 20);
        int left = (this.width - panelWidth) / 2;
        int top = Math.max(8, (this.height - 280) / 2);

        // Main panel
        graphics.fill(
            left, top,
            left + panelWidth, top + 280,
            DARK
        );

        // Cyan header
        graphics.fill(
            left, top,
            left + panelWidth, top + 28,
            CYAN
        );

        // Side accent
        graphics.fill(
            left, top,
            left + 3, top + 280,
            CYAN
        );

        graphics.text(
            this.font,
            "BattlePie-ECO",
            left + 12,
            top + 9,
            DARK,
            true
        );

        graphics.text(
            this.font,
            "Right Shift: Close GUI",
            left + 12,
            top + 250,
            WHITE,
            false
        );

        graphics.text(
            this.font,
            "Cyan Edition",
            left + 12,
            top + 266,
            MUTED,
            false
        );
    }

    @Override
    public void onClose() {
        Minecraft.getInstance().gui.setScreen(null);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
