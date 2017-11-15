import model.TerrainType;
import model.Vehicle;
import model.WeatherType;

import java.util.Collection;

public class RewindClientWrapper implements MyStrategyPainter {

    private MyStrategy mys;
    private RewindClient rc;

    public RewindClientWrapper() {
    }

    @Override
    public void onStartTick() {
        Collection<VehicleWrapper> vehs = mys.um.vehicleById.values();
        for (VehicleWrapper veh : vehs) {
            Vehicle v = veh.v;
            rc.livingUnit(v.getX(), v.getY(), v.getRadius(), v.getDurability(), v.getMaxDurability(), veh.isEnemy ? RewindClient.Side.ENEMY : RewindClient.Side.OUR
                    , 0, convertType(v), v.getRemainingAttackCooldownTicks(), v.getAttackCooldownTicks(), v.isSelected());
        }
    }

    private RewindClient.UnitType convertType(Vehicle v) {
        switch (v.getType()) {
            case ARRV:
                return RewindClient.UnitType.ARRV;
            case FIGHTER:
                return RewindClient.UnitType.FIGHTER;
            case HELICOPTER:
                return RewindClient.UnitType.HELICOPTER;
            case IFV:
                return RewindClient.UnitType.IFV;
            case TANK:
                return RewindClient.UnitType.TANK;
        }
        return RewindClient.UnitType.UNKNOWN;
    }

    @Override
    public void setMYS(MyStrategy myStrategy) {
        mys = myStrategy;
    }

    @Override
    public void onEndTick() {
        rc.endFrame();
    }

    @Override
    public void onInitializeStrategy() {
        rc = new RewindClient();

        for (int x = 0; x < mys.terrainTypeByCellXY.length; x++) {
            for (int y = 0; y < mys.terrainTypeByCellXY[x].length; y++) {
                RewindClient.AreaType areaType = getAreaType(mys.terrainTypeByCellXY[x][y]);
                if (areaType != RewindClient.AreaType.UNKNOWN) {
                    rc.areaDescription(x, y, areaType);
                }
                RewindClient.AreaType weatherType = getAreaType(mys.weatherTypeByCellXY[x][y]);
                if (weatherType != RewindClient.AreaType.UNKNOWN) {
                    rc.areaDescription(x, y, weatherType);
                }
            }
        }
    }

    private RewindClient.AreaType getAreaType(WeatherType weatherType) {
        switch (weatherType) {
            case CLEAR:
                return RewindClient.AreaType.UNKNOWN;
            case CLOUD:
                return RewindClient.AreaType.CLOUD;
            case RAIN:
                return RewindClient.AreaType.RAIN;
        }
        return RewindClient.AreaType.UNKNOWN;
    }

    private RewindClient.AreaType getAreaType(TerrainType terrainType) {
        switch (terrainType) {
            case PLAIN:
                return RewindClient.AreaType.UNKNOWN;
            case SWAMP:
                return RewindClient.AreaType.SWAMP;
            case FOREST:
                return RewindClient.AreaType.FOREST;
        }
        return RewindClient.AreaType.UNKNOWN;
    }
}
