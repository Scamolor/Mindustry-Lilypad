package mindustry.entities.comp;

import arc.math.geom.Vec2;
import arc.util.io.*;
import arc.graphics.g2d.Draw;
import arc.math.Mathf;
import mindustry.ai.*;
import mindustry.annotations.Annotations.*;
import mindustry.async.*;
import mindustry.content.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.*;
import mindustry.world.*;
import mindustry.world.blocks.environment.*;

import static mindustry.Vars.*;

@Component
abstract class SubmarineComp implements Entityc, Healthc, Velc, Unitc, Posc, WaterMovec {
    @Import UnitType type;
    @Import Vec2 vel;
    @Import float elevation, health, maxHealth;
    @Import boolean dead, hovering;
    public boolean boosting, isOnDeepwater;

    public boolean onDeep(){
        Tile tile = tileOn();
        return tile != null && tile.floor().isDeep();
    }
    @Override
    public void updateBoosting(boolean boost){
        if(!type.canBoost || dead || (this.health < 0.8 * this.maxHealth)) return;
        float target = boost ? -1f : 0f;
        if(this.isOnDeepwater && (this.health >= 0.8 * this.maxHealth)) {
            elevation = Mathf.approachDelta(elevation, target, type.riseSpeed);
        } else {
            elevation = Mathf.approachDelta(elevation, 0f, (type.riseSpeed * 5)); //emergency surface
        }
    }
    @Override
    public void update() {
        type.update(self());
        isOnDeepwater = this.onDeep();
        this.boosting = 0 > this.elevation;
        if (this.boosting) {
            this.vel.limit(this.type().speed * 0.6f);
        }
        if(!this.onLiquid() && state.getPlanet() != Planets.tantros){
            kill();
        }
    }

    @MethodPriority(10f)
    @Replace
    @Override
    public void draw() {
        // 1. Store the original Z-index so we can restore it later
        float previousZ = Draw.z();
        Draw.z(Layer.groundUnit - 0.1f);
        type.draw(self());
        // drawStatusEffects();
        Draw.z(previousZ);
    }
}
