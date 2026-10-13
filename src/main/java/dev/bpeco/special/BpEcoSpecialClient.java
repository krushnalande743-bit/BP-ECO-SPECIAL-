package dev.bpeco.special;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class BpEcoSpecialClient implements ClientModInitializer {
    private static KeyBinding guiKey;
    private static boolean guiOpen = false;

    private static boolean showCoords = true;
    private static boolean showFPS = true;
    private static boolean showDirection = true;

    private static final int CYAN = 0xFF35E5F2;
    private static final int WHITE = 0xFFEAFBFF;

    @Override
    public void onInitializeClient() {
        guiKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.bp_eco_special.open_gui",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_SHIFT,
                KeyBinding.Category.MISC
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (guiKey.wasPressed()) {
                guiOpen = !guiOpen;
            }
        });

        HudRenderCallback.EVENT.register((context, tickCounter) -> {
            MinecraftClient mc = MinecraftClient.getInstance();

            if (mc.player == null || mc.options.hudHidden) return;

            int x = 10;
            int y = 10;

            context.fill(x - 4, y - 4, x + 145, y + 17, 0xCC101820);
            context.fill(x - 4, y - 4, x - 1, y + 17, CYAN);
            context.drawText(mc.textRenderer,
                    Text.literal("BP-ECO-SPECIAL"),
                    x + 3, y, CYAN, true);

            y += 23;

            if (showCoords) {
                context.drawText(mc.textRenderer,
                        Text.literal(String.format("XYZ: %.1f %.1f %.1f",
                                mc.player.getX(),
                                mc.player.getY(),
                                mc.player.getZ())),
                        x, y, WHITE, true);
                y += 13;
            }

            if (showFPS) {
                context.drawText(mc.textRenderer,
                        Text.literal("FPS: " + mc.getCurrentFps()),
                        x, y, CYAN, true);
                y += 13;
            }

            if (showDirection) {
                String direction = getDirection(mc.player.getYaw());
                context.drawText(mc.textRenderer,
                        Text.literal("Facing: " + direction),
                        x, y, WHITE, true);
            }

            if (guiOpen) {
                renderGui(context, mc);
            }
        });
    }

    private static void renderGui(DrawContext context, MinecraftClient mc) {
        int width = mc.getWindow().getScaledWidth();
        int height = mc.getWindow().getScaledHeight();

        int panelWidth = 240;
        int panelHeight = 150;
        int left = (width - panelWidth) / 2;
        int top = (height - panelHeight) / 2;

        context.fill(0, 0, width, height, 0x66000000);
        context.fill(left, top, left + panelWidth, top + panelHeight,
                0xF0101820);
        context.fill(left, top, left + panelWidth, top + 3, CYAN);
        context.fill(left, top, left + 3, top + panelHeight, CYAN);

        context.drawText(mc.textRenderer,
                Text.literal("BP-ECO-SPECIAL"),
                left + 12, top + 12, CYAN, true);

        context.drawText(mc.textRenderer,
                Text.literal("Right Shift: Close GUI"),
                left + 12, top + 28, WHITE, false);

        context.drawText(mc.textRenderer,
                Text.literal((showCoords ? "[ON] " : "[OFF] ") + "Coordinates HUD"),
                left + 12, top + 52, showCoords ? CYAN : WHITE, true);

        context.drawText(mc.textRenderer,
                Text.literal((showFPS ? "[ON] " : "[OFF] ") + "FPS Counter"),
                left + 12, top + 75, showFPS ? CYAN : WHITE, true);

        context.drawText(mc.textRenderer,
                Text.literal((showDirection ? "[ON] " : "[OFF] ") + "Direction HUD"),
                left + 12, top + 98, showDirection ? CYAN : WHITE, true);

        context.drawText(mc.textRenderer,
                Text.literal("Cyan Edition"),
                left + 12, top + 128, CYAN, false);
    }

    private static String getDirection(float yaw) {
        int index = Math.floorMod(Math.round(yaw / 90.0f), 4);

        return switch (index) {
            case 0 -> "South";
            case 1 -> "West";
            case 2 -> "North";
            default -> "East";
        };
    }
}
