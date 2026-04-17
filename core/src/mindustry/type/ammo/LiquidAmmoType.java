package mindustry.type.ammo;

import arc.graphics.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;


//i think this class is completely useless
public class LiquidAmmoType implements AmmoType{
    public float range = 85f;
    public int ammoPerLiquid = 15;
    public Liquid liquid;

    public LiquidAmmoType(Liquid liquid){
        this.liquid = liquid;
    }

    public LiquidAmmoType(Liquid liquid, int ammoPerliquid){
        this.liquid = liquid;
        this.ammoPerLiquid = ammoPerLiquid;
    }

    public LiquidAmmoType(){
    }

    @Override
    public String icon(){
        return liquid.emoji();
    }

    @Override
    public Color color(){
        return liquid.color;
    }

    @Override
    public Color barColor(){
        return Pal.ammo;
    }

    @Override
    public void resupply(Unit unit){
        if(unit.type.ammoCapacity - unit.ammo < ammoPerLiquid) return;

        float range = unit.hitSize + this.range;

        Building build = Units.closestBuilding(unit.team, unit.x, unit.y, range, u -> u.canResupply() && u.liquids != null && u.liquids.equals(liquid));

        if(build != null){
            Fx.itemTransfer.at(build.x, build.y, ammoPerLiquid / 2f, liquid.color, unit);
            unit.ammo = Math.min(unit.ammo + ammoPerLiquid, unit.type.ammoCapacity);
            build.liquids.remove(liquid, 1);
        }
    }
}