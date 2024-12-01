
package sk.uniba.fmph.dcs.stone_age;

import java.util.Collection;

public record CivilisationCard(Collection<ImmediateEffect> immediateEffect,
        Collection<EndOfGameEffect> endOfGameEffect) {

}
