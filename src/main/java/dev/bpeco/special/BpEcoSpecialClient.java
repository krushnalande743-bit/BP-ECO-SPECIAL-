package dev.bpeco.special;

import com.mojang.blaze3d.platform.InputConstants;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import org.lwjgl.glfw.GLFW;

public class BpEcoSpecialClient implements ClientModInitializer {

    private static KeyMapping guiKey;
    private static boolean guiOpen = false;

    private static boolean showCoords = true;
    private static boolean showFPS = true;
    private static boolean showDirection = true;

    private static final int CYAN = 0xFF35E5F2;
    private static final int WHITE = 0xFFEAFBFF;

    @Override
    public void onInitializeClient() {

        KeyMapping.Category category = KeyMapping.Category.register(
                Identifier.fromNamespaceAndPath(
                        "bp_eco_special", "general"
                )
        );

        guiKey = KeyMappingHelper.registerKeyMapping(
                new KeyMapping(
                        "key.bp_eco_special.open_gui",
                        InputConstants.Type.KEYSYM,
                        GLFW.GLFW_KEY_RIGHT_SHIFT,
                        category
                )
        );

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (guiKey.consumeClick()) {
                guiOpen = !guiOpen;
            }
        });

        HudElementRegistry.attachElementBefore(
                VanillaHudElements.CHAT,
                Identifier.fromNamespaceAndPath(
                        "bp_eco_special", "hud"
                ),
                BpEcoSpecialClient::renderHud
        );
    }

    private static void renderHud(
            GuiGraphicsExtractor context,
            DeltaTracker tickCounter
    ) {
        Minecraft mc = Minecraft.getInstance();

        if (mc.player == null) {
            return;
        }

        int x = 10;
        int y = 10;

        context.fill(
                x - 4, y - 4,
                x + 145, y + 17,
                0xCC101820
        );

        context.fill(
                x - 4, y - 4,
                x - 1, y + 17,
                CYAN
        );

        context.text(
                mc.font,
                Component.literal("BP-ECO-SPECIAL"),
                x + 3, y,
                CYAN,
                true
        );

        y += 23;

        if (showCoords) {
            context.text(
                    mc.font,
                    Component.literal(String.format(
                            "XYZ: %.1f %.1f %.1f",
                            mc.player.getX(),
                            mc.player.getY(),
                            mc.player.getZ()
                    )),
                    x, y,
                    WHITE,
                    true
            );

            y += 13;
        }

        if (showFPS) {
            context.text(
                    mc.font,
                    Component.literal("FPS: " + mc.getFps()),
                    x, y,
                    CYAN,
                    true
            );

            y += 13;
        }

        if (showDirection) {
            String direction = getDirection(
                    mc.player.getYRot()
            );

            context.text(
                    mc.font,
                    Component.literal("Facing: " + direction),
                    x, y,
                    WHITE,
                    true
            );
        }

        if (guiOpen) {
            renderGui(context, mc);
        }
    }

    private static void renderGui(
            GuiGraphicsExtractor context,
            Minecraft mc
    ) {
        int width = mc.getWindow().getGuiScaledWidth();
        int height = mc.getWindow().getGuiScaledHeight();

        int panelWidth = 240;
        int panelHeight = 150;

        int left = (width - panelWidth) / 2;
        int top = (height - panelHeight) / 2;

        context.fill(
                0, 0, width, height,
                0x66000000
        );

        context.fill(
                left, top,
                left + panelWidth, top + panelHeight,
                0xF0101820
        );

        context.fill(
                left, top,
                left + panelWidth, top + 3,
                CYAN
        );

        context.fill(
                left, top,
                left + 3, top + panelHeight,
                CYAN
        );

        context.text(
                mc.font,
                Component.literal("BP-ECO-SPECIAL"),
                left + 12, top + 12,
                CYAN,
                true
        );

        context.text(
                mc.font,
                Component.literal("Right Shift: Close GUI"),
                left + 12, top + 28,
                WHITE,
                false
        );

        context.text(
                mc.font,
                Component.literal(
                        (showCoords ? "[ON] " : "[OFF] ")
                                + "Coordinates HUD"
                ),
                left + 12, top + 52,
                showCoords ? CYAN : WHITE,
                true
        );

        context.text(
                mc.font,
                Component.literal(
                        (showFPS ? "[ON] " : "[OFF] ")
                                + "FPS Counter"
                ),
                left + 12, top + 75,
                showFPS ? CYAN : WHITE,
                true
        );

        context.text(
                mc.font,
                Component.literal(
                        (showDirection ? "[ON] " : "[OFF] ")
                                + "Direction HUD"
                ),
                left + 12, top + 98,
                showDirection ? CYAN : WHITE,
                true
        );

        context.text(
                mc.font,
                Component.literal("Cyan Edition"),
                left + 12, top + 128,
                CYAN,
                false
        );
    }

    private static String getDirection(float yaw) {
        int index = Math.floorMod(
                Math.round(yaw / 90.0f),
                4
        );

        return switch (index) {
            case 0 -> "South";
            case 1 -> "West";
            case 2 -> "North";
            default -> "East";
        };
    }
}
