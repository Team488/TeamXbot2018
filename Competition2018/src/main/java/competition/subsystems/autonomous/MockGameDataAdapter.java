package competition.subsystems.autonomous;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import openrio.powerup.MatchData.GameFeature;
import openrio.powerup.MatchData.OwnedSide;

@Singleton
public class MockGameDataAdapter extends GameDataSource {

    Map<GameFeature, OwnedSide> mockData;
    
    @Inject
    public MockGameDataAdapter() {
        mockData = new HashMap<GameFeature, OwnedSide>();
        mockData.put(GameFeature.SCALE, OwnedSide.UNKNOWN);
        mockData.put(GameFeature.SWITCH_NEAR, OwnedSide.UNKNOWN);
        mockData.put(GameFeature.SWITCH_FAR, OwnedSide.UNKNOWN);
    }
    
    @Override
    public OwnedSide getOwnedSide(GameFeature feature) {
        return mockData.get(feature);
    }
    
    public void setOwnedSide(GameFeature feature, OwnedSide side) {
        mockData.replace(feature, side);
    }

}
