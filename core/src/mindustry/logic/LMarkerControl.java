package mindustry.logic;

public enum LMarkerControl{
    remove,
    world("true/false"),
    minimap("true/false"),
    autoscale("true/false"),
    pos("x", "y"),
    endPos("x", "y"),
    drawLayer("layer"),
    color("color"),
    radius("radius"),
    stroke("stroke"),
    outline("outline"),
    rotation("rotation"),
    shapeSides("sides"),
    shapeFill("true/false"),
    shapeOutline("true/false"),
    shape("sides", "fill", "outline"),
    arc("start", "end"),
    flushText("fetch"),
    fontSize("size"),
    textHeight("height"),
    textAlign("align"),
    lineAlign("align"),
    labelFlags("background", "outline"),
    texture("name", "-", "-"),
    textureWidth("width"),
    textureHeight("height"),
    textureSize("width", "height");

    public final String[] params;

    public static final LMarkerControl[] all = values();

    LMarkerControl(String... params){
        this.params = params;
    }
}