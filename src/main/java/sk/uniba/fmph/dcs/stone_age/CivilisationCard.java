
package sk.uniba.fmph.dcs.stone_age;

import java.util.ArrayList;

public record CivilisationCard(ArrayList<ImmediateEffect> immediateEffect, ArrayList<EndOfGameEffect> endOfGameEffect) {

}
