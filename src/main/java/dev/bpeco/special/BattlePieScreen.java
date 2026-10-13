package dev.bpeco.special;

import com.mojang.blaze3d.platform.InputConstants;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.VanillaHudElements;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import org.lwjgl.glfw.GLFW;

public class BpEcoSpecialClient implements ClientModInitializer {

    private static KeyMapping guiKey;

    private static boolean showCoords = true;
    private static boolean showFPS = true;
    private static boolean showDirection = true;

    private static final int CYAN = 0xFF35E5F2;
    private static final int WHITE = 0xFFEAFBFF;

    @Override
    public void onInitializeClient() {

        KeyMapping.Category category =
            KeyMapping.Category.register(
                Identifier.fromNamespaceAndPath(
                    "bp_eco_special",
                    "general"
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
                if (client.gui.screen() == null) {
                    client.gui.setScreen(
                        new BattlePieScreen()
                    );
                } else if (
                    client.gui.screen() instanceof BattlePieScreen
                ) {
                    client.gui.setScreen(null);
                }
            }
        });

        HudElementRegistry.attachElementBefore(
            VanillaHudElements.CHAT,
            Identifier.fromNamespaceAndPath(
                "bp_eco_special",
                "hud"
            ),
            BpEcoSpecialClient::renderHud
        );
    }

    private static void renderHud(
        GuiGraphicsExtractor context,
        DeltaTracker tickCounter
    ) {
        Minecraft mc = Minecraft.getInstance();

        if (mc.player == null) return;

        if (mc.gui.screen() instanceof BattlePieScreen) return;

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
            Component.literal("BATTLEPIE-ECO"),
            x + 3, y,
            CYAN,
            true
        );

        y += 23;

        if (showCoords) {
            context.text(
                mc.font,
                Component.literal(
                    String.format(
                        "XYZ: %.1f %.1f %.1f",
                        mc.player.getX(),
                        mc.player.getY(),
                        mc.player.getZ()
                    )
                ),
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
