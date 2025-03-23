# Material Beacons

A Fabric Minecraft mod that changes how beacons work depending on the material used.
Instead of beacons being built out of any valid material and then selecting an effect,
the material used to build the beacon determines the effect.

Based on the concept of 
[Hardcore Beacons](https://wiki.btwce.com/index.php?title=Hardcore_Modes#Hardcore_Beacons)
from the
[Better Than Wolves](https://www.minecraftforum.net/forums/mapping-and-modding-java-edition/minecraft-mods/3117153-better-than-wolves-community-edition-v2-1-1)
mod.

## Download
[![GitHub Downloads (all assets, all releases)](https://img.shields.io/github/downloads/Joel-Paul/Material-Beacons/total?logo=github&label=GitHub)](https://github.com/Joel-Paul/Material-Beacons/releases/latest)
[![Modrinth](https://img.shields.io/modrinth/dt/QLLz3XcD?logo=modrinth&label=Modrinth)](https://modrinth.com/mod/material-beacons)
[![CurseForge](https://img.shields.io/curseforge/dt/1025875?style=flat&logo=curseforge&label=CurseForge)](https://www.curseforge.com/minecraft/mc-mods/material-beacons)

## Current Materials and Effects
| Block     | Effect          |
|-----------|-----------------|
| Diamond   | Strength        |
| Glass     | None            |
| Glowstone | Night vision    |
| Gold      | Haste           |
| Honey     | Regeneration    |
| Honeycomb | Absorption      |
| Iron      | Resistance      |
| Netherite | Fire Resistance |
| Purpur    | Slow Falling    |
| Redstone  | Speed           |
| Slime     | Jump Boost      |


## Datapacks
Beacon materials and effects can be added/modified using datapacks.
The example json below creates a beacon using wool and/or clay with two tiers of effects:
- Tier 1: Blindness and Nausea
- Tier 2: Regeneration III

(`/data/[namespace]/beacon/wool_clay.json`)
```json
{
  "bases": [
    "#minecraft:wool",
    "minecraft:clay"
  ],
  "powers": [
    [
      {
        "effect": "minecraft:blindness",
        "duration": 11,
        "amplifier": 0,
        "range": 10
      },
      {
        "effect": "minecraft:nausea",
        "duration": 11,
        "amplifier": 0,
        "range": 30
      }
    ],
    [
      {
        "effect": "minecraft:regeneration",
        "duration": 17,
        "amplifier": 3,
        "range": 30
      }
    ]
  ]
}
```

Bases are defined as a list of block tags or block ids. Powers are a list, with each index corresponding to a tier.
Each tier contains a list of effects. As shown in the first tier, multiple effects can be assigned to a single tier.
Note that each tier is independent of the others, and different effects and ranges can be applied for each tier.
