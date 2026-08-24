package mindustry.entities;

import arc.math.*;
import mindustry.content.*;
import mindustry.entities.Units.*;
import mindustry.gen.*;

public class UnitSorts{
    public static Sortf

    closest = Unit::dst2,
    farthest = (u, x, y) -> -u.dst2(x, y),
    strongest = (u, x, y) -> -u.maxHealth + Mathf.dst2(u.x, u.y, x, y) / 6400f,
    weakest = (u, x, y) -> u.maxHealth + Mathf.dst2(u.x, u.y, x, y) / 6400f,
    mostArmor = (u, x, y) -> -u.armor + Mathf.dst2(u.x, u.y, x, y) / 6400f,
    leastArmor = (u, x, y) -> u.armor + Mathf.dst2(u.x, u.y, x, y) / 6400f,
    mostShield = (u, x, y) -> -u.shield + Mathf.dst2(u.x, u.y, x, y) / 6400f,
    leastShield = (u, x, y) -> u.shield + Mathf.dst2(u.x, u.y, x, y) / 6400f;
}