package competition.subsystems.autonomous;

import openrio.powerup.MatchData.GameFeature;
import openrio.powerup.MatchData.OwnedSide;

public abstract class GameDataSource {

    public abstract OwnedSide getOwnedSide(GameFeature feature);

}
