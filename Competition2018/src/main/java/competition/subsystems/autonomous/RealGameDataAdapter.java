package competition.subsystems.autonomous;

import openrio.powerup.MatchData;
import openrio.powerup.MatchData.GameFeature;
import openrio.powerup.MatchData.OwnedSide;

public class RealGameDataAdapter extends GameDataSource {

    @Override
    public OwnedSide getOwnedSide(GameFeature feature) {
        return MatchData.getOwnedSide(feature);
    }

}
