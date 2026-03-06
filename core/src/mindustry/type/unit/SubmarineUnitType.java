package mindustry.type.unit;

import mindustry.world.meta.*;

import mindustry.type.UnitType;

public class SubmarineUnitType extends UnitType {
    public SubmarineUnitType(String name) {
        super(name);
        flying = false;
        canDrown = false;
        omniMovement = false;
        riseSpeed = 0.2f;
        killable = true;
        playerControllable = true;
        createWreck = true;
        createScorch = true;
    }
}
