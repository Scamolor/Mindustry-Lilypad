package mindustry.graphics;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.graphics.gl.*;
import arc.math.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;
import mindustry.*;

import static mindustry.Vars.*;

/** Renders overlay lights. Client only. */
public class LightRenderer{
    private static final int scaling = 4;

    private float[] vertices = new float[24];
    private FrameBuffer buffer = new FrameBuffer();
    private Seq<Runnable> lights = new Seq<>();
    private Seq<CircleLight> circles = new Seq<>(CircleLight.class);
    private int circleIndex = 0;
    private TextureRegion circleRegion;

    public void add(Runnable run){
        if(!enabled()) return;

        lights.add(run);
    }

    public void add(float x, float y, float radius, Color color, float opacity){
        if(!enabled() || radius <= 0f) return;

        float res = Color.toFloatBits(color.r, color.g, color.b, opacity);

        if(circles.size <= circleIndex) circles.add(new CircleLight());

        //pool circles to prevent runaway GC usage from lambda capturing
        var light = circles.items[circleIndex];
        light.set(x, y, res, radius);

        circleIndex ++;
    }
    
    public void add(float x, float y, TextureRegion region, Color color, float opacity){
        add(x, y, region, 0f, color, opacity);
    }

    public void add(float x, float y, TextureRegion region, float rotation, Color color, float opacity){
        if(!enabled()) return;

        float res = color.toFloatBits();
        float xscl = Draw.xscl, yscl = Draw.yscl;
        add(() -> {
            Draw.color(res);
            Draw.alpha(opacity);
            Draw.scl(xscl, yscl);
            Draw.rect(region, x, y, rotation);
            Draw.scl();
        });
    }

    public void line(float x, float y, float x2, float y2, float stroke, Color tint, float alpha){
        if(!enabled()) return;

        add(() -> {
            Drawf.light(x, y, stroke * 3f, tint, alpha);
            Drawf.light(x2, y2, stroke * 3f, tint, alpha);
            float dist = Mathf.dst(x, y, x2, y2);
            int steps = (int)(dist / (stroke * 0.5f)); // Dynamic steps based on thickness
            for(int i = 0; i <= steps; i++){
                float progress = (float)i / steps;
                float lx = Mathf.lerp(x, x2, progress);
                float ly = Mathf.lerp(y, y2, progress);
                Drawf.light(lx, ly, stroke * 2.5f, tint, alpha * 0.8f);
            }
        });
    }

    public boolean enabled(){
        return state.rules.lighting && state.rules.ambientLight.a > 0.0001f && renderer.drawLight;
    }

    public void draw(){
        if(!Vars.enableLight){
            lights.clear();
            circleIndex = 0;
            return;
        }

        if(circleRegion == null) circleRegion = Core.atlas.find("circle-shadow");

        buffer.resize(Core.graphics.getWidth()/scaling, Core.graphics.getHeight()/scaling);

        Draw.color();
        buffer.begin(Color.clear);
        Draw.sort(false);
        Gl.blendEquationSeparate(Gl.funcAdd, Gl.max);
        //apparently necessary
        Blending.normal.apply();

        for(Runnable run : lights){
            run.run();
        }
        for(int i = 0; i < circleIndex; i++){
            var cir = circles.items[i];
            Draw.color(cir.color);
            Draw.rect(circleRegion, cir.x, cir.y, cir.radius * 2, cir.radius * 2);
        }
        Draw.reset();
        Draw.sort(true);
        buffer.end();
        Gl.blendEquationSeparate(Gl.funcAdd, Gl.funcAdd);

        Draw.color();
        Shaders.light.ambient.set(state.rules.ambientLight);
        buffer.blit(Shaders.light);

        lights.clear();
        circleIndex = 0;
    }

    static class CircleLight{
        float x, y, color, radius;

        public void set(float x, float y, float color, float radius){
            this.x = x;
            this.y = y;
            this.color = color;
            this.radius = radius;
        }
    }
}
