package mindustry.world.blocks.campaign;

import arc.*;
import arc.Graphics.*;
import arc.Graphics.Cursor.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.scene.ui.layout.*;
import arc.struct.Seq;
import arc.util.*;
import mindustry.annotations.Annotations.*;
import mindustry.content.*;
import mindustry.ctype.UnlockableContent;
import mindustry.game.EventType.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.storage.CoreBlock;

import static mindustry.Vars.*;

public class Accelerator extends Block{
    public @Load("launch-arrow") TextureRegion arrowRegion;
    public Block launchBlock = Blocks.coreNucleus;
    public float powerBufferRequirement;

    /**
     * Override for planets that this block can launch to. If null, the planet's launch candidates are used.
     */
    public @Nullable Seq<Planet> launchCandidates;
    //TODO dynamic
    public float launchDuration = 120f;
    public float chargeDuration = 220f;
    public float buildDuration = 120f;
    public int[] capacities = {};

    public Accelerator(String name){
        super(name);
        update = true;
        solid = true;
        hasItems = true;
        itemCapacity = 8000;
        configurable = true;
    }

    @Override
    public void init(){
        itemCapacity = 0;
        capacities = new int[content.items().size];
        for(ItemStack stack : launchBlock.requirements){
            capacities[stack.item.id] = stack.amount;
            itemCapacity += stack.amount;
        }
        consumeItems(launchBlock.requirements);
        super.init();
    }

    @Override
    public boolean outputsItems(){
        return false;
    }

    public class AcceleratorBuild extends Building{
        public float heat, statusLerp, progress;
        public boolean launching;

        @Override
        public void updateTile(){
            super.updateTile();
            heat = Mathf.lerpDelta(heat, efficiency, 0.05f);
            statusLerp = Mathf.lerpDelta(statusLerp, power.status, 0.05f);
        }

        @Override
        public void draw(){
            super.draw();

            for(int l = 0; l < 4; l++){
                float length = 7f + l * 5f;
                Draw.color(Tmp.c1.set(Pal.darkMetal).lerp(team.color, statusLerp), Pal.darkMetal, Mathf.absin(Time.time + l*50f, 10f, 1f));

                for(int i = 0; i < 4; i++){
                    float rot = i*90f + 45f;
                    Draw.rect(arrowRegion, x + Angles.trnsx(rot, length), y + Angles.trnsy(rot, length), rot + 180f);
                }
            }

            if(heat < 0.0001f) return;

            float rad = size * tilesize / 2f * 0.74f;
            float scl = 2f;

            Draw.z(Layer.bullet - 0.0001f);
            Lines.stroke(1.75f * heat, Pal.accent);
            Lines.square(x, y, rad * 1.22f, 45f);

            Lines.stroke(3f * heat, Pal.accent);
            Lines.square(x, y, rad, Time.time / scl);
            Lines.square(x, y, rad, -Time.time / scl);

            Draw.color(team.color);
            Draw.alpha(Mathf.clamp(heat * 3f));

            for(int i = 0; i < 4; i++){
                float rot = i*90f + 45f + (-Time.time /3f)%360f;
                float length = 26f * heat;
                Draw.rect(arrowRegion, x + Angles.trnsx(rot, length), y + Angles.trnsy(rot, length), rot + 180f);
            }

            Draw.reset();
        }

        @Override
        public Cursor getCursor(){
            return !state.isCampaign() || efficiency <= 0f ? SystemCursor.arrow : super.getCursor();
        }

        public boolean canLaunch() {
            return isValid() && state.isCampaign() && efficiency > 0f && power.graph.getBatteryStored() >= powerBufferRequirement - 0.00001f && progress >= 1f && !launching;
        }

        public float launchDuration() {
            return launchDuration + chargeDuration;
        }

        @Override
        public void buildConfiguration(Table table) {
            deselect();

            if (!canLaunch()) return;

            ui.planet.showPlanetLaunch(state.rules.sector, launchCandidates == null ? state.rules.sector.planet.launchCandidates : launchCandidates, sector -> {
                if (canLaunch()) {
                    consume();
                    power.graph.useBatteries(powerBufferRequirement);
                    progress = 0f;

                    renderer.showLaunch((CoreBlock) launchBlock);

                    Time.runTask(launchDuration() - 6f, () -> {
                        //unlock right before launch
                        launching = false;
                        Planet.unlockedOnLand.each(UnlockableContent::unlock);

                        universe.clearLoadoutInfo();
                        universe.updateLoadout((CoreBlock) launchBlock);

                        control.playSector(sector);
                    });
                }
            });

            Events.fire(Trigger.acceleratorUse);
        }

        @Override
        public int getMaximumAccepted(Item item){
            return capacities[item.id];
        }

        @Override
        public boolean acceptItem(Building source, Item item){
            return items.get(item) < getMaximumAccepted(item);
        }
    }
}
