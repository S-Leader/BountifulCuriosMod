package keletu.bountifulcurios.client;

import keletu.bountifulcurios.network.ModNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.List;

public class WormholeSelectionScreen extends Screen {
    private static final int GUI_WIDTH = 192;
    private static final int GUI_HEIGHT = 220;
    private static final int TARGETS_PER_PAGE = 16;

    private final List<ModNetworking.WormholeTarget> targets;
    private int page;

    private WormholeSelectionScreen(List<ModNetworking.WormholeTarget> targets) {
        super(Component.translatable("gui.bountifulcurios.wormhole.title"));
        this.targets = List.copyOf(targets);
    }

    public static void open(List<ModNetworking.WormholeTarget> targets) {
        Minecraft.getInstance().setScreen(new WormholeSelectionScreen(targets));
    }

    @Override
    protected void init() {
        rebuildButtons();
    }

    private void rebuildButtons() {
        clearWidgets();
        int left = (width - GUI_WIDTH) / 2;
        int top = (height - GUI_HEIGHT) / 2;
        int start = page * TARGETS_PER_PAGE;
        int end = Math.min(start + TARGETS_PER_PAGE, targets.size());

        for (int index = start; index < end; index++) {
            ModNetworking.WormholeTarget target = targets.get(index);
            int localIndex = index - start;
            int column = localIndex % 2;
            int row = localIndex / 2;
            addRenderableWidget(Button.builder(Component.literal(target.name()), button -> {
                        ModNetworking.selectWormholeTarget(target.id());
                        onClose();
                    })
                    .bounds(left + column * 96, top + 22 + row * 24, 88, 20)
                    .build());
        }

        int pageCount = Math.max(1, (targets.size() + TARGETS_PER_PAGE - 1) / TARGETS_PER_PAGE);
        if (pageCount > 1) {
            addRenderableWidget(Button.builder(
                            Component.translatable("gui.bountifulcurios.wormhole.previous"),
                            button -> {
                                page = (page + pageCount - 1) % pageCount;
                                rebuildButtons();
                            })
                    .bounds(left, top + GUI_HEIGHT - 20, 80, 20)
                    .build());
            addRenderableWidget(Button.builder(
                            Component.translatable("gui.bountifulcurios.wormhole.next"),
                            button -> {
                                page = (page + 1) % pageCount;
                                rebuildButtons();
                            })
                    .bounds(left + GUI_WIDTH - 80, top + GUI_HEIGHT - 20, 80, 20)
                    .build());
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(graphics);
        graphics.drawCenteredString(font, title, width / 2,
                (height - GUI_HEIGHT) / 2 + 7, 0xFFFFFF);
        super.render(graphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
