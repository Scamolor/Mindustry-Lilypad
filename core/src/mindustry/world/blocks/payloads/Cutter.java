package mindustry.world.blocks.payloads;

import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.*;
import mindustry.annotations.Annotations.*;
import mindustry.content.*;
import mindustry.ctype.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.logic.*;
import mindustry.ui.*;
import mindustry.world.*;
import mindustry.world.blocks.defense.*;
import mindustry.world.blocks.defense.Wall.*;
import mindustry.world.blocks.payloads.NuclearWarhead.*;

public class Cutter extends PayloadBlock{
    public float maxPayloadSize = 2;
    public float deconstructSpeed = 2.5f;

    public Cutter(String name){
        super(name);
        outputsPayload = acceptsPayload = update = true;
        acceptsItems = hasItems = false;
        regionRotated1 = 1;
        solid = true;
        size = 5;
        payloadSpeed = payloadRotateSpeed = 2.5f;
        clipSize = 120;
        hasPower = true;
    }

    public @Load("@-over") TextureRegion overRegion;

    @Override
    public void setBars(){
        super.setBars();

        addBar("progress", (CutterBuild e) -> new Bar("bar.progress", Pal.ammo, () -> e.progress));
    }

    public class CutterBuild extends PayloadBlockBuild<BuildPayload>{
        public int spawnedCounter;
        public float heat, time, progress;
        public boolean hasConsumed;
        public Block originalWall, smallWall;
        public String smallName;
        public TextureRegion[] icons(){
            return new TextureRegion[]{region, outRegion, topRegion};
        }

        public void consumeBlock(BuildPayload blockToCut){
            //Log.info("consume has been called");
            if(!hasConsumed && this.payload != null && this.payload.content() != null){
                originalWall = blockToCut.block();
                smallName = originalWall.name.replace("-large", "");
                smallWall = Vars.content.getByName(ContentType.block, smallName);
                //Log.info("payload consumed");
                //Log.info(smallName);
                //Log.info(smallWall);
                //Log.info(originalWall);
                this.payload = null;
                hasConsumed = true;
            } else {
                hasConsumed = false;
                //Log.info("payload not consumed");
            }
        }

        @Override
        public boolean acceptPayload(Building source, Payload payload){
            return super.acceptPayload(source, payload) && this.payload == null && payload instanceof BuildPayload b && b.build instanceof WallBuild && (b.block().size == 2);
        }

        @Override
        public void updateTile(){
            super.updateTile();
            if(hasConsumed){
                float shift = edelta() * deconstructSpeed / (originalWall.buildTime * 4);
                progress += shift;
                time += edelta();
                for(spawnedCounter = 1; spawnedCounter <= 4; spawnedCounter++){
                    if(progress / 0.25f >= (spawnedCounter + 1f)){
                        Fx.generatespark.at(x, y);
                        BuildPayload smallPayload = new BuildPayload(smallWall,this.team());
                        this.dumpPayload(smallPayload);
                        //Log.info("payload cut");
                        //Log.info(spawnedCounter);
                    }
                }
                if(progress >= 1f){
                    this.payload = null;
                    progress = 0f;
                    spawnedCounter = 0;
                    //Log.info("resetting");
                    hasConsumed = false;
                }
            } else {
                if(this.payload != null && this.payload.content() != null){
                    if(this.payload.content().name.contains("wall") && this.payload.content().name.contains("large")){
                        //Log.info("block taken");
                        consumeBlock(this.payload);
                    }else{
                        //Log.info("this block is yucky gimme something else");
                        this.movePayload(this.payload);
                    }
                }
            }
        }
        @Override
        public void draw(){
            Draw.rect(region, x, y);

            boolean fallback = true;
            for(int i = 0; i < 4; i++){
                if(blends(i) && i != rotation){
                    Draw.rect(inRegion, x, y, (i * 90) - 180);
                    fallback = false;
                }
            }
            if(fallback) Draw.rect(inRegion, x, y, rotation * 90);

            Draw.rect(outRegion, x, y, rotdeg());

            Draw.rect(topRegion, x, y);

            Draw.z(Layer.blockOver);
            drawPayload();

            if(overRegion.found()){
                Draw.z(Layer.blockOver + 0.1f);
                Draw.rect(overRegion, x, y);
            }
        }

        @Override
        public double sense(LAccess sensor){
            if(sensor == LAccess.progress) return progress;
            return super.sense(sensor);
        }

        @Override
        public void write(Writes write){
            super.write(write);
            write.f(progress);
        }

        @Override
        public void read(Reads read, byte revision){
            super.read(read, revision);
            progress = read.f();
        }
    }
}

