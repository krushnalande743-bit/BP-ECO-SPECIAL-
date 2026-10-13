package dev.bpeco.special;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudElementRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

public class BpEcoSpecialClient implements ClientModInitializer {
    private static KeyBinding toggleKey;
    private static double previousGamma = 1.0;

    @Override
    public void onInitializeClient() {
        // Register Right Shift Keybinding
        toggleKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.bpeco.toggle",
                GLFW.GLFW_KEY_RIGHT_SHIFT,
                "category.bpeco.general"
        ));

        // Client Tick for GUI toggle and FullBright logic
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (toggleKey.wasPressed()) {
                if (client.player != null) {
                    if (client.currentScreen instanceof BattlePieScreen) {
                        client.setScreen(null);
                    } else {
                        client.setScreen(new BattlePieScreen());
                    }
                }
            }

            // FullBright Logic
            if (client.options != null) {
                if (BattlePieScreen.isEnabled("FullBright")) {
                    if (client.options.getGamma().getValue() < 100.0) {
                        previousGamma = client.options.getGamma().getValue();
                        client.options.getGamma().setValue(100.0);
                    }
                } else {
                    if (client.options.getGamma().getValue() >= 100.0) {
                        client.options.getGamma().setValue(previousGamma);
                    }
                }
            }
        });

        // Register HUD Renderer for Coordinates, FPS, and Direction
        HudElementRegistry.attachElementBefore(
                Identifier.of("minecraft", "debug_screen"),
                Identifier.of("bp_eco_special", "hud"),
                (context, tickCounter) -> {
                    MinecraftClient client = MinecraftClient.getInstance();
                    if (client.player == null || client.options.hudHidden) return;

                    int x = 10;
                    int y = 10;

                    // Header
                    context.drawText(client.textRenderer, Text.literal("BP-ECO-SPECIAL"), x, y, 0xFF00f5d4, true);
                    y += 12;

                    // Coordinates HUD
                    if (BattlePieScreen.isEnabled("Coordinates HUD")) {
                        String coords = String.format("XYZ: %.1f %.1f %.1f",
                                client.player.getX(),
                                client.player.getY(),
                                client.player.getZ());
                        context.drawText(client.textRenderer, Text.literal(coords), x, y, 0xFFFFFFFF, true);
                        y += 10;
                    }

                    // FPS Counter
                    if (BattlePieScreen.isEnabled("FPS Counter")) {
                        String fpsText = "FPS: " + client.getCurrentFps();
                        context.drawText(client.textRenderer, Text.literal(fpsText), x, y, 0xFFFFFFFF, true);
                        y += 10;
                    }

                    // Direction HUD
                    if (BattlePieScreen.isEnabled("Direction HUD")) {
                        String facing = "Facing: " + client.player.getHorizontalFacing().getName().toUpperCase();
                        context.drawText(client.textRenderer, Text.literal(facing), x, y, 0xFFFFFFFF, true);
                    }
                }
        );
    }
}
