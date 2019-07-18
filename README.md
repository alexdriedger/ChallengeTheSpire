# Challenge The Spire

Boss Rush, Elite Rush, & More!! Available on the [Steam Workshop](https://steamcommunity.com/sharedfiles/filedetails/?id=1696570507)!

Challenge The Spire adds a variety of challenge modes to Slay The Spire. Each challenge can be played on 4 different difficulty settings (Bronze, Silver, Gold, Platinum).

## Requirements

- [ModTheSpire](https://github.com/kiooeht/ModTheSpire)
- [BaseMod](https://github.com/daviscook477/BaseMod)

## Features

### Boss Rush

- Any Class
- Start with 1000 gold + courier + eggs
- Map is a single path
  - Shop, shop, All 3 Act 1 Bosses, etc.
- Difficulty scaling
  - Silver = Ascension  9
  - Gold = Ascension  14 & less starting gold (1000 -> 750)
  - Platinum = Ascension  20

### Elite Rush

- Any Class
- Start with Black Star, Sling, Preserved Insect, 1000 gold
- Map is a single path
  - Shop, Fire, All 3 Act 1 Elites, etc.
- Difficulty scaling
  - Silver =  Ascension 9
  - Gold = Ascension 14 & one elite of each “act” a super elite & less starting gold (1000 -> 750)
  - Platinum = Ascension 20 & all super elites

### Modded Elite Rush

- Elite Rush with modded elites

### Modded Boss Rush

- Boss Rush with modded bosses
  
### Sneaky Strike

- Silent
- Inspired by Twitch chats everywhere
- Beat the run with as many sneaky strikes in the deck as possible
- There will be a sneaky strike in every card reward screen
- Sneaky Strikes are added to your deck at the middle treasure room and the campfire before the boss in every act

### Challenge API (Crossover support)

- Support for other mods to add challenges

### Custom Challenge Select Screen

- Additional screen available in addition to Standard, Daily, Custom
- Character select, Difficulty select, Challenge select

### Difficulty Scaling

- Bronze = Ascension 0
- Silver = Ascension 9
- Gold = Ascension 14 + Extra difficulty based on the challenge
- Platinum = Ascension 20 + Extra difficulty based on the challenge

## Roadmap 

### Polish Boss Rush & Elite Rush

- Support for endless and infinite spire
- Only start with 2 eggs on Platinum Boss Rush 
- Remove loot from final fight
- Fix soft lock from saving and continuing on a Boss Chest
- Remove Certain Future mod and patch Replay The Spire instead
- Remove unnecessary scores bonuses
- Victory message surrounding what type of challenge was completed
- ZHS translation for Modded Elite / Boss Rush

### Polish Sneaky Strike

- Act 4 by default
- Add neow bonus as a choice of discard relics (tough bandages, tingsha, gambling chip)
- Score bonus for number of sneaky strikes (In addition to collector bonus)

### Polish Custom Challenge Select Screen

- Record of highest difficulty achieved for each challenge for each character (including modded characters)
- Select Silent as character when selecting sneaky strike challenge
- Allow multiple challenges to be selected for Challenge API with `compatibleWith` list in json files

### Eggcellent

- Any Class
- Start with all 3 eggs
- After the act 2 boss, your eggs hatch into dragons
  - Red Dragon: Enemies start combat with +2 Strength
  - Green Dragon: Enemies start combat with +4 Plated Armor
  - Blue Dragon: Enemies start combat with +1 Artifact Charge

### RNGeeez

- Ironclad
- Start with Dead Branch and 0 cost Corruption in starting hand
- Max hp is limited to 10 (no feed, max hp events, or health relics in pool)

### General Polish

- Add icons for daily mod drop down