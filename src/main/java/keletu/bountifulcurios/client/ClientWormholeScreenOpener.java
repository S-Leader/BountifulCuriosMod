package keletu.bountifulcurios.client;

import keletu.bountifulcurios.network.ModNetworking;
import keletu.bountifulcurios.network.WormholeScreenOpener;

import java.util.List;

public final class ClientWormholeScreenOpener implements WormholeScreenOpener {
    @Override
    public void open(List<ModNetworking.WormholeTarget> targets) {
        WormholeSelectionScreen.open(targets);
    }
}
