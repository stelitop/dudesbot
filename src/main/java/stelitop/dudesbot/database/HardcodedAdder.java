package stelitop.dudesbot.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import stelitop.dudesbot.database.services.DudeService;
import stelitop.dudesbot.database.services.ItemService;
import stelitop.dudesbot.game.entities.Dude;
import stelitop.dudesbot.game.entities.Item;
import stelitop.dudesbot.game.entities.Move;
import stelitop.dudesbot.game.entities.Trait;
import stelitop.dudesbot.game.enums.ElementalType;
import stelitop.dudesbot.game.enums.Rarity;

import java.util.List;

@Component
public class HardcodedAdder implements ApplicationRunner {

    @Autowired
    private DudeService dudeService;
    @Autowired
    private ItemService itemService;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        //var x =dudeService.getDude(3);
        //System.out.println(x.get().getFlavourText());
        addDudes();
        addItems();
    }

    private void addDudes() {
        dudeService.saveDudeWithMovesAndTraits(Dude.builder()
                .name("Peeble")
                .stage(1)
                .nextEvolution("Rox")
                .types(List.of(ElementalType.Earth))
                .resistance(ElementalType.Fire)
                .weakness(ElementalType.Water)
                .health(25)
                .speed(50)
                .offense(50)
                .defense(75)
                .artistName("Scorp")
                .artLink("https://i.imgur.com/B7GGmCf.png")
                .traits(List.of(Trait.builder()
                        .name("Sandy")
                        .description("This Dude is immune to damage from Sandstorms. If it is already immune to damage from Sandstorms, this Dude is healed by Sandstorms as well.")
                        .build()))
                .moves(List.of(Move.builder()
                        .name("Pebble")
                        .description("Target another Dude. Deal 20 damage to it.")
                        .types(List.of(ElementalType.Earth))
                        .energy(1)
                        .build()))
                .rarity(Rarity.Common)
                //.appearanceChannel(568918716109291571L)
                .build());

        dudeService.saveDudeWithMovesAndTraits(Dude.builder()
                .name("Rox")
                .stage(2)
                .nextEvolution("Boldacairn")
                .previousEvolution("Peeble")
                .types(List.of(ElementalType.Earth))
                .resistance(ElementalType.Fire)
                .weakness(ElementalType.Water)
                .health(75)
                .speed(100)
                .offense(100)
                .defense(125)
                .artistName("Scorp")
                .artLink("https://i.imgur.com/hfBuj11.png")
                .traits(List.of(Trait.builder()
                        .name("Sandy")
                        .description("This Dude is immune to damage from Sandstorms. If it is already immune to damage from Sandstorms, this Dude is healed by Sandstorms as well.")
                        .build()))
                .moves(List.of(Move.builder()
                        .name("Rock")
                        .description("Target another Dude. Deal 50 damage to it.")
                        .types(List.of(ElementalType.Earth))
                        .energy(2)
                        .build(), Move.builder()
                        .name("Harden")
                        .description("Increase this Dude's Defense by 20.")
                        .types(List.of(ElementalType.Earth))
                        .energy(1)
                        .build()
                        ))
                .rarity(Rarity.Rare)
                .build());

        dudeService.saveDudeWithMovesAndTraits(Dude.builder()
                .name("Boldacairn")
                .stage(3)
                .previousEvolution("Rox")
                .types(List.of(ElementalType.Earth))
                .resistance(ElementalType.Fire)
                .weakness(ElementalType.Water)
                .health(125)
                .speed(150)
                .offense(150)
                .defense(175)
                .artistName("Scorp")
                .artLink("https://i.imgur.com/ldTaVXj.png")
                .traits(List.of(Trait.builder()
                        .name("Sandy")
                        .description("This Dude is immune to damage from Sandstorms. If it is already immune to damage from Sandstorms, this Dude is healed by Sandstorms as well.")
                        .build()))
                .moves(List.of(Move.builder()
                        .name("Boulder")
                        .description("Target another Dude. Deal 80 damage to it.")
                        .energy(3)
                        .types(List.of(ElementalType.Earth))
                        .build(), Move.builder()
                        .name("Solidify")
                        .description("Increase this Dude's Defense by 50.")
                        .energy(2)
                        .types(List.of(ElementalType.Earth))
                        .build(), Move.builder()
                        .name("Sandstorm")
                        .description("Apply the Sandstorm condition to the battlefield.")
                        .energy(1)
                        .types(List.of(ElementalType.Earth))
                        .build()
                ))
                .rarity(Rarity.Epic)
                .build());

        dudeService.saveDudeWithMovesAndTraits(Dude.builder()
                .name("Droop")
                .stage(1)
                .nextEvolution("Droople")
                .types(List.of(ElementalType.Water))
                .resistance(ElementalType.Earth)
                .weakness(ElementalType.Air)
                .health(75)
                .speed(25)
                .offense(50)
                .defense(50)
                .artistName("Scorp")
                .artLink("https://i.imgur.com/CDjzvhW.png")
                .traits(List.of(Trait.builder()
                        .name("Rainy")
                        .description("This Dude is immune to damage from Hurricanes. If it is already immune to damage from Hurricanes, this Dude is healed by Hurricanes as well.")
                        .build()))
                .moves(List.of(Move.builder()
                        .name("Squirt")
                        .description("Target another Dude. Deal 20 damage to it.")
                        .types(List.of(ElementalType.Water))
                        .energy(1)
                        .build()))
                .rarity(Rarity.Common)
                .build());

        dudeService.saveDudeWithMovesAndTraits(Dude.builder()
                .name("Droople")
                .stage(2)
                .previousEvolution("Droop")
                .nextEvolution("Drooplex")
                .types(List.of(ElementalType.Water))
                .resistance(ElementalType.Earth)
                .weakness(ElementalType.Air)
                .health(125)
                .speed(75)
                .offense(100)
                .defense(100)
                .artistName("Scorp")
                .artLink("https://i.imgur.com/2qmhkEt.png")
                .traits(List.of(Trait.builder()
                        .name("Rainy")
                        .description("This Dude is immune to damage from Hurricanes. If it is already immune to damage from Hurricanes, this Dude is healed by Hurricanes as well.")
                        .build()))
                .moves(List.of(Move.builder()
                        .name("Jet")
                        .description("Target another Dude. Deal 50 damage to it.")
                        .types(List.of(ElementalType.Water))
                        .energy(2)
                        .build(), Move.builder()
                        .name("Reinvigorate")
                        .description("Increase this Dude's current Health by 20.")
                        .types(List.of(ElementalType.Water))
                        .energy(1)
                        .build()
                ))
                .rarity(Rarity.Rare)
                .build());

        dudeService.saveDudeWithMovesAndTraits(Dude.builder()
                .name("Drooplex")
                .stage(3)
                .previousEvolution("Droople")
                .types(List.of(ElementalType.Water))
                .resistance(ElementalType.Earth)
                .weakness(ElementalType.Air)
                .health(175)
                .speed(125)
                .offense(150)
                .defense(150)
                .artistName("Scorp")
                .artLink("https://i.imgur.com/O3Y1PUb.png")
                .traits(List.of(Trait.builder()
                        .name("Rainy")
                        .description("This Dude is immune to damage from Hurricanes. If it is already immune to damage from Hurricanes, this Dude is healed by Hurricanes as well.")
                        .build()))
                .moves(List.of(Move.builder()
                        .name("Geyser")
                        .description("Target another Dude. Deal 80 damage to it.")
                        .energy(3)
                        .types(List.of(ElementalType.Water))
                        .build(), Move.builder()
                        .name("Revitalize")
                        .description("Increase this Dude's current Health by 50.")
                        .energy(2)
                        .types(List.of(ElementalType.Water))
                        .build(), Move.builder()
                        .name("Hurricane")
                        .description("Apply the Hurricane condition to the battlefield.")
                        .energy(1)
                        .types(List.of(ElementalType.Water))
                        .build()
                ))
                .rarity(Rarity.Epic)
                .build());


        dudeService.saveDudeWithMovesAndTraits(Dude.builder()
                .name("Strasolo")
                .stage(1)
                .nextEvolution("Altwo")
                .types(List.of(ElementalType.Air))
                .resistance(ElementalType.Water)
                .weakness(ElementalType.Fire)
                .health(50)
                .speed(75)
                .offense(25)
                .defense(50)
                .artistName("Scorp")
                .artLink("https://i.imgur.com/OZ8hzXw.png")
                .traits(List.of(Trait.builder()
                        .name("Windy")
                        .description("This Dude is immune to damage from Tornadoes. If it is already immune to damage from Tornadoes, this Dude is healed by Tornadoes as well.")
                        .build()))
                .moves(List.of(Move.builder()
                        .name("Breeze")
                        .description("Target another Dude. Deal 20 damage to it.")
                        .types(List.of(ElementalType.Air))
                        .energy(1)
                        .build()))
                .rarity(Rarity.Common)
                .build());

        dudeService.saveDudeWithMovesAndTraits(Dude.builder()
                .name("Altwo")
                .stage(2)
                .previousEvolution("Strasolo")
                .nextEvolution("Cirree")
                .types(List.of(ElementalType.Air))
                .resistance(ElementalType.Water)
                .weakness(ElementalType.Fire)
                .health(100)
                .speed(125)
                .offense(75)
                .defense(100)
                .artistName("Scorp")
                .artLink("https://i.imgur.com/JnIgjGV.png")
                .traits(List.of(Trait.builder()
                        .name("Windy")
                        .description("This Dude is immune to damage from Tornadoes. If it is already immune to damage from Tornadoes, this Dude is healed by Tornadoes as well.")
                        .build()))
                .moves(List.of(Move.builder()
                        .name("Wind")
                        .description("Target another Dude. Deal 50 damage to it.")
                        .types(List.of(ElementalType.Air))
                        .energy(2)
                        .build(), Move.builder()
                        .name("Quicken")
                        .description("Increase this Dude's Speed by 20.")
                        .types(List.of(ElementalType.Air))
                        .energy(1)
                        .build()
                ))
                .rarity(Rarity.Rare)
                .build());

        dudeService.saveDudeWithMovesAndTraits(Dude.builder()
                .name("Cirree")
                .stage(3)
                .previousEvolution("Altwo")
                .types(List.of(ElementalType.Air))
                .resistance(ElementalType.Water)
                .weakness(ElementalType.Fire)
                .health(150)
                .speed(175)
                .offense(125)
                .defense(150)
                .artistName("Scorp")
                .artLink("https://i.imgur.com/lQOvdA7.png")
                .traits(List.of(Trait.builder()
                        .name("Windy")
                        .description("This Dude is immune to damage from Tornadoes. If it is already immune to damage from Tornadoes, this Dude is healed by Tornadoes as well.")
                        .build()))
                .moves(List.of(Move.builder()
                        .name("Storm")
                        .description("Target another Dude. Deal 80 damage to it.")
                        .energy(3)
                        .types(List.of(ElementalType.Air))
                        .build(), Move.builder()
                        .name("Rush")
                        .description("Increase this Dude's Speed by 50.")
                        .energy(2)
                        .types(List.of(ElementalType.Air))
                        .build(), Move.builder()
                        .name("Tornado")
                        .description("Apply the Tornado condition to the battlefield.")
                        .energy(1)
                        .types(List.of(ElementalType.Air))
                        .build()
                ))
                .rarity(Rarity.Epic)
                .build());


        dudeService.saveDudeWithMovesAndTraits(Dude.builder()
                .name("Faya")
                .stage(1)
                .nextEvolution("Igni")
                .types(List.of(ElementalType.Fire))
                .resistance(ElementalType.Air)
                .weakness(ElementalType.Earth)
                .health(50)
                .speed(50)
                .offense(75)
                .defense(25)
                .artistName("Scorp")
                .artLink("https://i.imgur.com/Lf1MZ11.png")
                .traits(List.of(Trait.builder()
                        .name("Fiery")
                        .description("This Dude is immune to damage from Wildfires. If it is already immune to damage from Wildfires, this Dude is healed by Wildfires as well.")
                        .build()))
                .moves(List.of(Move.builder()
                        .name("Spark")
                        .description("Target another Dude. Deal 20 damage to it.")
                        .types(List.of(ElementalType.Fire))
                        .energy(1)
                        .build()))
                .rarity(Rarity.Common)
                .build());

        dudeService.saveDudeWithMovesAndTraits(Dude.builder()
                .name("Igni")
                .stage(2)
                .previousEvolution("Faya")
                .nextEvolution("Ferno")
                .types(List.of(ElementalType.Fire))
                .resistance(ElementalType.Air)
                .weakness(ElementalType.Earth)
                .health(100)
                .speed(100)
                .offense(125)
                .defense(75)
                .artistName("Scorp")
                .artLink("https://i.imgur.com/i2u2x4g.png")
                .traits(List.of(Trait.builder()
                        .name("Fiery")
                        .description("This Dude is immune to damage from Wildfires. If it is already immune to damage from Wildfires, this Dude is healed by Wildfires as well.")
                        .build()))
                .moves(List.of(Move.builder()
                        .name("Flame")
                        .description("Target another Dude. Deal 50 damage to it.")
                        .types(List.of(ElementalType.Fire))
                        .energy(2)
                        .build(), Move.builder()
                        .name("Glow")
                        .description("Increase this Dude's Offense by 20.")
                        .types(List.of(ElementalType.Fire))
                        .energy(1)
                        .build()
                ))
                .rarity(Rarity.Rare)
                .build());

        dudeService.saveDudeWithMovesAndTraits(Dude.builder()
                .name("Ferno")
                .stage(3)
                .previousEvolution("Igni")
                .types(List.of(ElementalType.Fire))
                .resistance(ElementalType.Air)
                .weakness(ElementalType.Earth)
                .health(150)
                .speed(150)
                .offense(175)
                .defense(125)
                .artistName("Scorp")
                .artLink("https://i.imgur.com/5fVDHZB.png")
                .traits(List.of(Trait.builder()
                        .name("Fiery")
                        .description("This Dude is immune to damage from Wildfires. If it is already immune to damage from Wildfires, this Dude is healed by Wildfires as well.")
                        .build()))
                .moves(List.of(Move.builder()
                        .name("Storm")
                        .description("Target another Dude. Deal 80 damage to it.")
                        .energy(3)
                        .types(List.of(ElementalType.Fire))
                        .build(), Move.builder()
                        .name("Rush")
                        .description("Increase this Dude's Offence by 50.")
                        .energy(2)
                        .types(List.of(ElementalType.Fire))
                        .build(), Move.builder()
                        .name("Tornado")
                        .description("Apply the Wildfire condition to the battlefield.")
                        .energy(1)
                        .types(List.of(ElementalType.Fire))
                        .build()
                ))
                .rarity(Rarity.Epic)
                .build());


        dudeService.saveDudeWithMovesAndTraits(Dude.builder()
                .name("Leafworm")
                .stage(1)
                .types(List.of(ElementalType.Nature))
                .artistName("Pink")
                .artLink("https://i.imgur.com/xAUYutJ.png")
                .rarity(Rarity.Common)
                .build());

        dudeService.saveDudeWithMovesAndTraits(Dude.builder()
                .name("Reapo")
                .stage(1)
                .types(List.of(ElementalType.Decay))
                .artistName("Samswise")
                .artLink("https://i.imgur.com/yG7WFgv.png")
                .rarity(Rarity.Common)
                .build());

        dudeService.saveDudeWithMovesAndTraits(Dude.builder()
                .name("Poomp")
                .stage(1)
                .types(List.of(ElementalType.Nature, ElementalType.Decay))
                .artistName("Pink")
                .artLink("https://i.imgur.com/TrpAgCJ.png")
                .rarity(Rarity.Common)
                .build());

        dudeService.saveDudeWithMovesAndTraits(Dude.builder()
                .name("Rattlegun")
                .stage(1)
                .types(List.of(ElementalType.Tech))
                .nextEvolution("Fangfire")
                .artistName("Kittenguin")
                .artLink("https://i.imgur.com/eDavB6g.png")
                .rarity(Rarity.Common)
                .build());

        dudeService.saveDudeWithMovesAndTraits(Dude.builder()
                .name("Mangobug")
                .stage(1)
                .types(List.of(ElementalType.Nature))
                .artistName("Empi")
                .artLink("https://i.imgur.com/r9MBEVo.png")
                .rarity(Rarity.Common)
                .build());

        dudeService.saveDudeWithMovesAndTraits(Dude.builder()
                .name("Cornacape")
                .stage(1)
                .types(List.of(ElementalType.Nature))
                .artistName("Kittenguin")
                .artLink("https://i.imgur.com/gRqgrpX.png")
                .rarity(Rarity.Common)
                .build());

        dudeService.saveDudeWithMovesAndTraits(Dude.builder()
                .name("Flipdeedoo")
                .stage(1)
                .types(List.of(ElementalType.Neutral))
                .artistName("Kittenguin")
                .artLink("https://i.imgur.com/LvFANIP.png")
                .rarity(Rarity.Common)
                .build());

        dudeService.saveDudeWithMovesAndTraits(Dude.builder()
                .name("Gubblebum")
                .stage(1)
                .types(List.of(ElementalType.Magic))
                .artistName("Empi")
                .artLink("https://i.imgur.com/YCpD9Jq.png")
                .rarity(Rarity.Common)
                .build());

        dudeService.saveDudeWithMovesAndTraits(Dude.builder()
                .name("Grizzaphite")
                .stage(1)
                .types(List.of(ElementalType.Magic, ElementalType.Decay))
                .artistName("Empi")
                .artLink("https://i.imgur.com/uubLF9a.png")
                .rarity(Rarity.Common)
                .build());

        dudeService.saveDudeWithMovesAndTraits(Dude.builder()
                .name("Eletrash")
                .stage(1)
                .types(List.of(ElementalType.Tech, ElementalType.Decay))
                .artistName("Muhframos")
                .artLink("https://i.imgur.com/3xtcsUi.png")
                .rarity(Rarity.Rare)
                .build());

        dudeService.saveDudeWithMovesAndTraits(Dude.builder()
                .name("Petelly")
                .stage(1)
                .types(List.of(ElementalType.Nature, ElementalType.Air))
                .artistName("Muhframos")
                .artLink("https://i.imgur.com/BXFTQEz.png")
                .rarity(Rarity.Rare)
                .build());

        dudeService.saveDudeWithMovesAndTraits(Dude.builder()
                .name("Monumungous")
                .stage(1)
                .types(List.of(ElementalType.Earth, ElementalType.Water))
                .artistName("Muhframos")
                .artLink("https://i.imgur.com/VmFe9RI.png")
                .rarity(Rarity.Rare)
                .build());

        dudeService.saveDudeWithMovesAndTraits(Dude.builder()
                .name("Volcrust")
                .stage(1)
                .types(List.of(ElementalType.Earth, ElementalType.Fire))
                .artistName("Muhframos")
                .artLink("https://i.imgur.com/sP3CHJI.png")
                .rarity(Rarity.Rare)
                .build());

        dudeService.saveDudeWithMovesAndTraits(Dude.builder()
                .name("Protorb")
                .stage(1)
                .types(List.of(ElementalType.Neutral))
                .artistName("Scorp")
                .artLink("https://i.imgur.com/zKeWqjS.png")
                .rarity(Rarity.Epic)
                .health(100)
                .speed(100)
                .offense(100)
                .defense(100)
                .traits(List.of(Trait.builder()
                        .name("Familiar")
                        .description("When this Dude enters play, it transforms into an exact copy of a random enemy Dude. The transformation lasts until the end of the battle.")
                        .build()))
                .moves(List.of(Move.builder()
                        .name("Emulate")
                        .description("Target another Dude. This Dude transforms into an exact copy it. The transformation lasts until the end of the battle.")
                        .types(List.of(ElementalType.Neutral))
                        .energy(1)
                        .build()))
                .build());

        dudeService.saveDudeWithMovesAndTraits(Dude.builder()
                .name("Yellephone")
                .stage(1)
                .type(ElementalType.Tech)
                .resistance(ElementalType.Air)
                .weakness(ElementalType.Decay)
                .artistName("Pink")
                .artLink("https://i.imgur.com/eFibq8d.png")
                .rarity(Rarity.Common)
                .build());

        dudeService.saveDudeWithMovesAndTraits(Dude.builder()
                .name("Eyebat")
                .stage(1)
                .nextEvolution("Amalgafaceon")
                .type(ElementalType.Air)
                .type(ElementalType.Decay)
                .resistance(ElementalType.Decay)
                .weakness(ElementalType.Magic)
                .trait(Trait.builder()
                        .name("Aspect of Amalgafaceon")
                        .description("In order to Evolve this Dude, you must have Eyebat, Lipspider, Sneezeweed, and Earm in your collection.")
                        .build())
                .rarity(Rarity.Rare)
                .artistName("Pircival")
                .artLink("https://i.imgur.com/jrp9tsP.png")
                .build());

        dudeService.saveDudeWithMovesAndTraits(Dude.builder()
                .name("Lipspider")
                .stage(1)
                .nextEvolution("Amalgafaceon")
                .type(ElementalType.Decay)
                .resistance(ElementalType.Decay)
                .weakness(ElementalType.Magic)
                .trait(Trait.builder()
                        .name("Aspect of Amalgafaceon")
                        .description("In order to Evolve this Dude, you must have Eyebat, Lipspider, Sneezeweed, and Earm in your collection.")
                        .build())
                .rarity(Rarity.Rare)
                .artistName("Pircival")
                .artLink("https://i.imgur.com/imc2Njd.png")
                .build());

        dudeService.saveDudeWithMovesAndTraits(Dude.builder()
                .name("Sneezeweed")
                .stage(1)
                .nextEvolution("Amalgafaceon")
                .type(ElementalType.Nature)
                .type(ElementalType.Decay)
                .resistance(ElementalType.Decay)
                .weakness(ElementalType.Magic)
                .trait(Trait.builder()
                        .name("Aspect of Amalgafaceon")
                        .description("In order to Evolve this Dude, you must have Eyebat, Lipspider, Sneezeweed, and Earm in your collection.")
                        .build())
                .rarity(Rarity.Rare)
                .artistName("Pircival")
                .artLink("https://i.imgur.com/I3rn7Pj.png")
                .build());

        dudeService.saveDudeWithMovesAndTraits(Dude.builder()
                .name("Earm")
                .stage(1)
                .nextEvolution("Amalgafaceon")
                .type(ElementalType.Earth)
                .type(ElementalType.Decay)
                .resistance(ElementalType.Decay)
                .weakness(ElementalType.Magic)
                .trait(Trait.builder()
                        .name("Aspect of Amalgafaceon")
                        .description("In order to Evolve this Dude, you must have Eyebat, Lipspider, Sneezeweed, and Earm in your collection.")
                        .build())
                .rarity(Rarity.Rare)
                .artistName("Pircival")
                .artLink("https://i.imgur.com/vp8wyDn.png")
                .build());

        dudeService.saveDudeWithMovesAndTraits(Dude.builder()
                .name("Amalgafaceon")
                .stage(2)
                .previousEvolution("Eyebat")
                .previousEvolution("Lipspider")
                .previousEvolution("Sneezeweed")
                .previousEvolution("Earm")
                .type(ElementalType.Earth)
                .type(ElementalType.Nature)
                .type(ElementalType.Air)
                .resistance(ElementalType.Decay)
                .weakness(ElementalType.Magic)
                .rarity(Rarity.Legendary)
                .artistName("Pircival")
                .artLink("https://i.imgur.com/dfHnskV.png")
                .build());

        dudeService.saveDudeWithMovesAndTraits(Dude.builder()
                .name("Fangfire")
                .stage(2)
                .previousEvolution("Rattlegun")
                .type(ElementalType.Tech)
                .type(ElementalType.Fire)
                .resistance(ElementalType.Earth)
                .weakness(ElementalType.Water)
                .rarity(Rarity.Rare)
                .artistName("Pircival")
                .artLink("https://i.imgur.com/JmpMR5b.png")
                .build());

        dudeService.saveDudeWithMovesAndTraits(Dude.builder()
                .name("Techai")
                .stage(1)
                .nextEvolution("Obsolessence")
                .type(ElementalType.Tech)
                .resistance(ElementalType.Nature)
                .weakness(ElementalType.Magic)
                .health(5)
                .speed(65)
                .offense(75)
                .defense(55)
                .trait(Trait.builder()
                        .name("Assistant")
                        .description("Only acts after all of your other Dudes have done so. Your Dudes that act before this one deal 1.25x damage.")
                        .build())
                .rarity(Rarity.Common)
                .artistName("Flippz")
                .artLink("https://i.imgur.com/l1sM3jQ.png")
                .build());

        dudeService.saveDudeWithMovesAndTraits(Dude.builder()
                .name("Obsolessence")
                .stage(2)
                .previousEvolution("Techai")
                .type(ElementalType.Tech)
                .resistance(ElementalType.Nature)
                .weakness(ElementalType.Magic)
                .health(145)
                .speed(100)
                .offense(85)
                .defense(70)
                .trait(Trait.builder()
                        .name("Malware")
                        .description("Your other Dudes have 1.5x Offense and take 2x damage.")
                        .build())
                .rarity(Rarity.Rare)
                .artistName("Flippz")
                .artLink("https://i.imgur.com/CHe19mZ.png")
                .build());

        dudeService.saveDudeWithMovesAndTraits(Dude.builder()
                .name("Decktie")
                .stage(1)
                .nextEvolution("Fistfool")
                .type(ElementalType.Magic)
                .resistance(ElementalType.Air)
                .weakness(ElementalType.Fire)
                .health(50)
                .speed(50)
                .offense(50)
                .defense(50)
                .trait(Trait.builder()
                        .name("Gambler")
                        .description("When this Dude enters play, roll a 6-sided die. If 6, each of its stats are increased by 80. If 5, each of its stats are increased by 50. If 4, each of its stats are increased by 20. If 3, each of its stats are decreased by 20. If 2, each of its stats are decreased by 50. If 1, each of its stats are decreased by 80.")
                        .build())
                .rarity(Rarity.Common)
                .artistName("Flippz")
                .artLink("https://i.imgur.com/QoladQx.png")
                .build());

        dudeService.saveDudeWithMovesAndTraits(Dude.builder()
                .name("Fistfool")
                .stage(2)
                .previousEvolution("Decktie")
                .type(ElementalType.Magic)
                .resistance(ElementalType.Air)
                .weakness(ElementalType.Fire)
                .health(100)
                .speed(100)
                .offense(100)
                .defense(100)
                .trait(Trait.builder()
                        .name("Gambler")
                        .description("When this Dude enters play, roll a 6-sided die. If 6, each of its stats are increased by 80. If 5, each of its stats are increased by 50. If 4, each of its stats are increased by 20. If 3, each of its stats are decreased by 20. If 2, each of its stats are decreased by 50. If 1, each of its stats are decreased by 80.")
                        .build())
                .rarity(Rarity.Rare)
                .artistName("Flippz")
                .artLink("https://i.imgur.com/ui9xyM3.png")
                .build());

        dudeService.saveDudeWithMovesAndTraits(Dude.builder()
                .name("Gatitoblade")
                .stage(1)
                .nextEvolution("Beatsabre")
                .type(ElementalType.Tech)
                .resistance(ElementalType.Magic)
                .weakness(ElementalType.Water)
                .health(40)
                .speed(80)
                .offense(20)
                .defense(20)
                .trait(Trait.builder()
                        .name("Finesse")
                        .description("After this Dude performs a move, it has +60 Offense. After this Dude is damaged, it has +60 Defense. Only one of these two effects can be active at a time.")
                        .build())
                .rarity(Rarity.Common)
                .artistName("Flippz")
                .artLink("https://i.imgur.com/Ul1hsxw.png")
                .build());

        dudeService.saveDudeWithMovesAndTraits(Dude.builder()
                .name("Beatsabre")
                .stage(2)
                .previousEvolution("Gatitoblade")
                .nextEvolution("Cenozodi")
                .type(ElementalType.Tech)
                .resistance(ElementalType.Magic)
                .weakness(ElementalType.Water)
                .health(80)
                .speed(160)
                .offense(40)
                .defense(40)
                .trait(Trait.builder()
                        .name("Finesse")
                        .description("After this Dude performs a move, it has +60 Offense. After this Dude is damaged, it has +60 Defense. Only one of these two effects can be active at a time.")
                        .build())
                .rarity(Rarity.Rare)
                .artistName("Flippz")
                .artLink("https://i.imgur.com/nsfU5kC.png")
                .build());

        dudeService.saveDudeWithMovesAndTraits(Dude.builder()
                .name("Cenozodi")
                .stage(2)
                .previousEvolution("Beatsabre")
                .type(ElementalType.Tech)
                .resistance(ElementalType.Magic)
                .weakness(ElementalType.Water)
                .health(100)
                .speed(280)
                .offense(40)
                .defense(40)
                .trait(Trait.builder()
                        .name("Finesse")
                        .description("After this Dude performs a move, it has +60 Offense. After this Dude is damaged, it has +60 Defense. Only one of these two effects can be active at a time.")
                        .build())
                .rarity(Rarity.Epic)
                .artistName("Flippz")
                .artLink("https://i.imgur.com/G1OKmug.png")
                .build());
    }

    private void addItems() {
        itemService.saveItem(Item.builder()
                .name("Invisible Hamburger")
                .text("Does nothing.")
                .rarity(Rarity.Common)
                .build());

        itemService.saveItem(Item.builder()
                .name("Chili Pepper")
                .text("When a Dude holding this is damaged and falls below half of its maximum Health, discard this item, increase the Dude's Offense by 50, and give the Dude a FIRE Resistance. If the Dude has a FIRE Weakness, remove it.")
                .rarity(Rarity.Epic)
                .build());

        itemService.saveItem(Item.builder()
                .name("Brown Pearl")
                .text("While a NEUTRAL Dude is holding this, all of its stats are increased by 10.")
                .rarity(Rarity.Rare)
                .build());

        itemService.saveItem(Item.builder()
                .name("Yellow Pearl")
                .text("While a EARTH Dude is holding this, all of its stats are increased by 10.")
                .rarity(Rarity.Rare)
                .build());

        itemService.saveItem(Item.builder()
                .name("Dark Blue Pearl")
                .text("While a WATER Dude is holding this, all of its stats are increased by 10.")
                .rarity(Rarity.Rare)
                .build());

        itemService.saveItem(Item.builder()
                .name("Light Blue Pearl")
                .text("While a AIR Dude is holding this, all of its stats are increased by 10.")
                .rarity(Rarity.Rare)
                .build());

        itemService.saveItem(Item.builder()
                .name("Orange Pearl")
                .text("While a FIRE Dude is holding this, all of its stats are increased by 10.")
                .rarity(Rarity.Rare)
                .build());

        itemService.saveItem(Item.builder()
                .name("Green Pearl")
                .text("While a NATURE Dude is holding this, all of its stats are increased by 10.")
                .rarity(Rarity.Rare)
                .build());

        itemService.saveItem(Item.builder()
                .name("Black Pearl")
                .text("While a DECAY Dude is holding this, all of its stats are increased by 10.")
                .rarity(Rarity.Rare)
                .build());

        itemService.saveItem(Item.builder()
                .name("Pink Pearl")
                .text("While a MAGIC Dude is holding this, all of its stats are increased by 10.")
                .rarity(Rarity.Rare)
                .build());

        itemService.saveItem(Item.builder()
                .name("Gray Pearl")
                .text("While a TECH Dude is holding this, all of its stats are increased by 10.")
                .rarity(Rarity.Rare)
                .build());
    }
}
