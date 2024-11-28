package sk.uniba.fmph.dcs.game_board;

import sk.uniba.fmph.dcs.stone_age.Effect;
import sk.uniba.fmph.dcs.stone_age.InterfaceToolUse;

public class CurrentThrow implements InterfaceToolUse {
    private Effect throwsFor;
    private int throwResult;

    public final void initiate(final Player player, final Effect effect, final int dices) {

    }
    public final String state() {
        return "";
    }

    @Override
    public final boolean useTool(final int idx) {
        return false;
    }

    @Override
    public final boolean canUseTools() {
        return false;
    }

    @Override
    public final boolean finishUsingTools() {
        return false;
    }
}
