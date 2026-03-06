package mindustry.entities.comp;

import arc.math.geom.Vec2;
import arc.util.io.*;
import arc.graphics.g2d.Draw;
import arc.math.Mathf;
import mindustry.annotations.Annotations.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.*;

@Component
abstract class SubmarineComp implements Entityc, Flyingc, Healthc, Velc, Unitc, WaterMovec {
    @Import UnitType type;
    @Import Vec2 vel;
    @Import float elevation;
    boolean boost;
    @Override
    public void update() {
        type.update(self());
        if (boost) {
            this.elevation = Mathf.lerpDelta(this.elevation, -0.5f, 0.1f);
            this.vel.limit(this.type().speed * 0.6f);
        } else {
            this.elevation = Mathf.lerpDelta(this.elevation, 0f, 0.1f);
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
