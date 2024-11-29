package sk.uniba.fmph.dcs.game_board;

import java.util.Collection;
import java.util.OptionalInt;
import sk.uniba.fmph.dcs.stone_age.Effect;

public interface Building {
    OptionalInt build(Collection<Effect> resources);

    String state();
}
