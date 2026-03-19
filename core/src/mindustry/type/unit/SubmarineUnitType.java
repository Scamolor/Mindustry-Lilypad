package mindustry.type.unit;

import mindustry.content.*;
import mindustry.entities.abilities.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.meta.*;
import mindustry.entities.comp.SubmarineComp;
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
        envDisabled = Env.space;
    }
}
