package sk.uniba.fmph.dcs.game_board;

import sk.uniba.fmph.dcs.stone_age.Effect;
import sk.uniba.fmph.dcs.stone_age.InterfaceToolUse;

public final class CurrentThrow implements InterfaceToolUse {
    private Effect throwsFor;
    private int throwResult;

    public CurrentThrow() {
    } // konstruktor by mal dostat instanciu Throw

    public void initiate(final Player player, final Effect effect, final int dices) {
    }

    @Override
    public boolean useTool(final int idx) {
        return false;
    }

    @Override
    public boolean canUseTools() {
        return false;
    }

    @Override
    public boolean finishUsingTools() {
        return false;
    }

    String state() {
        return "";
    }
}
