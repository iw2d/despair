drop table if exists cs_items;

create table cs_items
(
    id                     int not null auto_increment,
    itemID                 int not null,
    stock                  int      default 1,
    shopItemFlag           int      default 0,
    oldPrice               int      default 0,
    newPrice               int      default 0,
    bundleQuantity         int      default 0,
    availableDays          int      default 90,
    buyableWithMaplePoints smallint default 1,
    buyableWithCredit      smallint default 1,
    buyableWithPrepaid     smallint default 1,
    likable                smallint default 1,
    meso                   smallint default 0,
    favoritable            smallint default 1,
    gender                 int      default 2,
    likes                  int      default 0,
    requiredLevel          int      default 0,
    category               varchar(255),
    primary key (id)
);


insert into `cs_items` (`itemID`, `newPrice`, `category`, `bundleQuantity`)
values

/* Event ------------------------------------------------------------------- */
/* SSB */
    (5222060, 3400, 'SSB', 0), /* Premium Surprise Style Box */
    (5222060, 34000, 'SSB', 11), /* Premium Surprise Style Box (11) */
    (5222006, 2100, 'SSB', 0), /* Surprise Surprise Style Box */
    (5222006, 21000, 'SSB', 11), /* Surprise Surprise Style Box (11) */

/* Special */
    (5220000, 1000, 'Special', 0), /* Gachapon Ticket */
    (5220000, 5000, 'Special', 5), /* Gachapon Ticket (5) */
    (5220000, 10000, 'Special', 11), /* Gachapon Ticket (11) */
    (5451000, 1050, 'Special', 0), /* Remote Gachapon Ticket */
    (5451000, 5250, 'Special', 5), /* Remote Gachapon Ticket (5) */
    (5451000, 10500, 'Special', 11), /* Remote Gachapon Ticket (11) */
    (5680021, 2500, 'Special', 0), /* Chair Gachapon Ticket */
    (5680021, 12500, 'Special', 5), /* Chair Gachapon Ticket (5) */
    (5680021, 25000, 'Special', 11), /* Chair Gachapon Ticket (11) */

/* Chance Sale */


/* Enhance ----------------------------------------------------------------- */
/* Enhance */

/* Scroll */


/* Game -------------------------------------------------------------------- */
/* Convenience */
    (5040004, 9900, 'Convenience', 0), /* Telport Rock */
    (5450004, 1000, 'Convenience', 0), /* Traveling Merchant (30-day) */
    (5450005, 1000, 'Convenience', 0), /* Portable Storage (30-day) */

/* Social */
    (5072000, 1300, 'Social', 0), /* Super Megaphone */
    (5072000, 6000, 'Social', 5), /* Super Megaphone (5) */
    (5072000, 13000, 'Social', 11), /* Super Megaphone (11) */
    (5076000, 1300, 'Social', 0), /* Item Megaphone */
    (5076000, 6000, 'Social', 5), /* Item Megaphone (5) */
    (5076000, 13000, 'Social', 11), /* Item Megaphone (11) */
    (5077000, 3000, 'Social', 0), /* Triple Megaphone */
    (5077000, 30000, 'Social', 11), /* Triple Megaphone (11) */

/* Atmospheric Effects */
    (5120000, 300, 'Atmospheric Effects', 0), /* Snowy Snow */
    (5120001, 300, 'Atmospheric Effects', 0), /* Sprinkled Flowers */
    (5120002, 300, 'Atmospheric Effects', 0), /* Soap Bubbles */
    (5120003, 300, 'Atmospheric Effects', 0), /* Snowflakes */
    (5120004, 300, 'Atmospheric Effects', 0), /* Sprinkled Presents */
    (5120005, 300, 'Atmospheric Effects', 0), /* Sprinkled Chocolate */
    (5120006, 300, 'Atmospheric Effects', 0), /* Sprinkled Flower Petals */
    (5120007, 300, 'Atmospheric Effects', 0), /* Sprinkled Candy */
    (5120008, 300, 'Atmospheric Effects', 0), /* Sprinkled Maple Leaves */
    (5120009, 300, 'Atmospheric Effects', 0), /* Fireworks */
    (5120010, 300, 'Atmospheric Effects', 0), /* Sprinkled Coke */
    (5120011, 300, 'Atmospheric Effects', 0), /* Spirit Haunt */
    (5120012, 300, 'Atmospheric Effects', 0), /* Holiday Sock */
    (5120015, 300, 'Atmospheric Effects', 0), /* Chinese Lantern Firecrackers */

/* Add Slot */
    (5430000, 6900, 'Add Slot', 0), /* Extra Character Slot Coupon */
    (9110000, 5000, 'Add Slot', 0), /* Add Storage Slots */
    (9111000, 5000, 'Add Slot', 0), /* Add Equip Slots */
    (9112000, 5000, 'Add Slot', 0), /* Add Use Slots */
    (9114000, 5000, 'Add Slot', 0), /* Add ETC Slots */
    (9113000, 5000, 'Add Slot', 0), /* Add Set-up Slots */
    (5552000, 3000, 'Add Slot', 0), /* Add Face Slots */
    (5553000, 3000, 'Add Slot', 0);
/* Add Hair Slots */


/* Outfits ----------------------------------------------------------------- */
insert into `cs_items` (`itemID`, `newPrice`, `category`)
values
/* Weapons */
    (1701000, 4500, 'Weapons'), /*Elizabeth Fan*/
    (1702000, 4500, 'Weapons'), /*Dual Plasma Blade*/
    (1702001, 4500, 'Weapons'), /*Bouquet*/
    (1702002, 4500, 'Weapons'), /*Wooden Slingshot*/
    (1702003, 4500, 'Weapons'), /*Plastic Slingshot*/
    (1702004, 4500, 'Weapons'), /*Angel Wand*/
    (1702005, 4500, 'Weapons'), /*Yellow Candy Cane*/
    (1702006, 4500, 'Weapons'), /*Red Candy Cane*/
    (1702007, 4500, 'Weapons'), /*Green Candy Cane*/
    (1702008, 4500, 'Weapons'), /*Santa Sack*/
    (1702009, 4500, 'Weapons'), /*Tiger Paw*/
    (1702010, 4500, 'Weapons'), /*Orange Toy Hammer*/
    (1702011, 4500, 'Weapons'), /*Pink Toy Hammer*/
    (1702012, 4500, 'Weapons'), /*Yellow Spatula*/
    (1702013, 4500, 'Weapons'), /*Teddy Bear*/
    (1702014, 4500, 'Weapons'), /*Toy RIfle*/
    (1702015, 4500, 'Weapons'), /*Bug Net*/
    (1702016, 4500, 'Weapons'), /*Picnic Basket*/
    (1702017, 4500, 'Weapons'), /*Pink Rabbit Puppet*/
    (1702018, 4500, 'Weapons'), /*Vanilla Ice Cream*/
    (1702019, 4500, 'Weapons'), /*Pillow*/
    (1702020, 4500, 'Weapons'), /*Lollipop*/
    (1702021, 4500, 'Weapons'), /*Black Electric Guitar*/
    (1702022, 4500, 'Weapons'), /*Brown Electric Guitar*/
    (1702023, 4500, 'Weapons'), /*Green Electric Guitar*/
    (1702024, 4500, 'Weapons'), /*Cupid's Bow*/
    (1702025, 4500, 'Weapons'), /*Cherub's Bow*/
    (1702026, 4500, 'Weapons'), /*Cupid's Crossbow*/
    (1702027, 4500, 'Weapons'), /*Blazing Sword*/
    (1702028, 4500, 'Weapons'), /*Donut*/
    (1702029, 4500, 'Weapons'), /*White Rabbit's Foot*/
    (1702030, 4500, 'Weapons'), /*Diao Chan Sword*/
    (1702031, 4500, 'Weapons'), /*Liu Bei Sword*/
    (1702032, 4500, 'Weapons'), /*Zhu-Ge-Liang Wand*/
    (1702033, 4500, 'Weapons'), /*Sun Quan Staff*/
    (1702034, 4500, 'Weapons'), /*Guan Yu Spear*/
    (1702035, 4500, 'Weapons'), /*Cao Cao Bow*/
    (1702036, 4500, 'Weapons'), /*Witch's Broomstick*/
    (1702037, 4500, 'Weapons'), /*Coffee Pot*/
    (1702038, 4500, 'Weapons'), /*Horoscope Claw*/
    (1702039, 4500, 'Weapons'), /*Horoscope Net*/
    (1702040, 4500, 'Weapons'), /*Horoscope Bow*/
    (1702041, 4500, 'Weapons'), /*Horoscope Sword*/
    (1702042, 4500, 'Weapons'), /*Microphone*/
    (1702043, 4500, 'Weapons'), /*Poo Stick*/
    (1702044, 4500, 'Weapons'), /*Toy Machine Gun*/
    (1702045, 4500, 'Weapons'), /*Sunflower Stalk*/
    (1702046, 4500, 'Weapons'), /*Horoscope Crossbow*/
    (1702047, 4500, 'Weapons'), /*Snowflake Staff*/
    (1702048, 4500, 'Weapons'), /*Green Wash Cloth*/
    (1702049, 4500, 'Weapons'), /*Snowman Claw*/
    (1702050, 4500, 'Weapons'), /*Cellphone*/
    (1702051, 4500, 'Weapons'), /*Hong Bao*/
    (1702052, 4500, 'Weapons'), /*In-Hand FB Helmet(Home)*/
    (1702053, 4500, 'Weapons'), /*In-Hand FB Helmet(Away)*/
    (1702054, 4500, 'Weapons'), /*Football Claw*/
    (1702055, 4500, 'Weapons'), /*Ancient Korean Bow*/
    (1702056, 4500, 'Weapons'), /*Guitar*/
    (1702057, 4500, 'Weapons'), /*Blue Guitar*/
    (1702058, 4500, 'Weapons'), /*Big Hand*/
    (1702059, 4500, 'Weapons'), /*Cactus*/
    (1702060, 4500, 'Weapons'), /*Shiner*/
    (1702061, 4500, 'Weapons'), /*Red Fist of Fury*/
    (1702062, 4500, 'Weapons'), /*Blue Fist of Fury*/
    (1702063, 4500, 'Weapons'), /*Scissor Stick*/
    (1702064, 4500, 'Weapons'), /*Rock Stick*/
    (1702065, 4500, 'Weapons'), /*Paper Stick*/
    (1702066, 4500, 'Weapons'), /*Canvas Tote Bag*/
    (1702067, 4500, 'Weapons'), /*England Cheer Towel*/
    (1702068, 4500, 'Weapons'), /*France Cheer Towel*/
    (1702069, 4500, 'Weapons'), /*Brazil Cheer Towel*/
    (1702070, 4500, 'Weapons'), /*Sporty Band*/
    (1702071, 4500, 'Weapons'), /*Japan Cheer Towel*/
    (1702072, 4500, 'Weapons'), /*Laser Sword*/
    (1702073, 4500, 'Weapons'), /*Blue Shiner Crossbow*/
    (1702074, 4500, 'Weapons'), /*Pink Shiner Crossbow*/
    (1702075, 4500, 'Weapons'), /*USA Cheer Towel*/
    (1702076, 4500, 'Weapons'), /*Mexico Cheer Towel*/
    (1702077, 4500, 'Weapons'), /*Australia Cheer Towel*/
    (1702078, 4500, 'Weapons'), /*Fairy Fan*/
    (1702079, 4500, 'Weapons'), /*Blue Blazing Sword*/
    (1702080, 4500, 'Weapons'), /*Green Blazing Sword*/
    (1702081, 4500, 'Weapons'), /*Purple Blazing Sword*/
    (1702082, 4500, 'Weapons'), /*Harp*/
    (1702083, 4500, 'Weapons'), /*Foam Hand*/
    (1702084, 4500, 'Weapons'), /*Toy Pinwheel*/
    (1702085, 4500, 'Weapons'), /*Frog Claw*/
    (1702086, 4500, 'Weapons'), /*Chicken Smackaroo*/
    (1702087, 4500, 'Weapons'), /*Red Pencil*/
    (1702088, 4500, 'Weapons'), /*Super Scrubber*/
    (1702089, 4500, 'Weapons'), /*Candy Hammer*/
    (1702090, 4500, 'Weapons'), /*Feather Scimitar*/
    (1702091, 4500, 'Weapons'), /*Tennis Racquet*/
    (1702092, 4500, 'Weapons'), /*Glowing Pumpkin Basket*/
    (1702093, 4500, 'Weapons'), /*Okie Donkie*/
    (1702094, 4500, 'Weapons'), /*Mad Cow*/
    (1702095, 4500, 'Weapons'), /*Frog Glove*/
    (1702096, 4500, 'Weapons'), /*Pizza Pan*/
    (1702097, 4500, 'Weapons'), /*Fire Katana*/
    (1702098, 4500, 'Weapons'), /*Violin*/
    (1702100, 4500, 'Weapons'), /*Christmas Bell*/
    (1702101, 4500, 'Weapons'), /*Meso Gunner*/
    (1702102, 4500, 'Weapons'), /*Starblade*/
    (1702103, 4500, 'Weapons'), /*Pink Ribbon Umbrella*/
    (1702104, 4500, 'Weapons'), /*Deluxe Cone*/
    (1702105, 4500, 'Weapons'), /*Heart Key*/
    (1702106, 4500, 'Weapons'), /*Melting Chocolate*/
    (1702107, 4500, 'Weapons'), /*Chocolate*/
    (1702108, 4500, 'Weapons'), /*Giant Lollipop*/
    (1702112, 4500, 'Weapons'), /*Celestial Wand*/
    (1702113, 4500, 'Weapons'), /*Maoster Pole Arm*/
    (1702114, 4500, 'Weapons'), /*Wonky's Leaf*/
    (1702115, 4500, 'Weapons'), /*Red Rose*/
    (1702116, 4500, 'Weapons'), /*Jie 1*/
    (1702117, 4500, 'Weapons'), /*Jie 2*/
    (1702118, 4500, 'Weapons'), /*Janus Sword*/
    (1702119, 4500, 'Weapons'), /*Sachiel Sword*/
    (1702120, 4500, 'Weapons'), /*Veamoth Sword*/
    (1702121, 4500, 'Weapons'), /*Seal Pillow*/
    (1702122, 4500, 'Weapons'), /*Dragon's Fury*/
    (1702123, 4500, 'Weapons'), /*Forked Pork*/
    (1702124, 4500, 'Weapons'), /*Kitty*/
    (1702125, 4500, 'Weapons'), /*Heart Cane*/
    (1702126, 4500, 'Weapons'), /*Blue Shiner */
    (1702127, 4500, 'Weapons'), /*Water Gun*/
    (1702128, 4500, 'Weapons'), /*Green Shiner*/
    (1702129, 4500, 'Weapons'), /*Purple Shiner*/
    (1702130, 4500, 'Weapons'), /*Red Shiner*/
    (1702131, 4500, 'Weapons'), /*Pepe Beak*/
    (1702132, 4500, 'Weapons'), /*Slime Stick*/
    (1702133, 4500, 'Weapons'), /*Smackdown Fist */
    (1702134, 4500, 'Weapons'), /*Serpent Staff */
    (1702135, 4500, 'Weapons'), /*Vengence Claw*/
    (1702136, 4500, 'Weapons'), /*Ice Flower*/
    (1702138, 4500, 'Weapons'), /*Spanish Ham*/
    (1702139, 4500, 'Weapons'), /*Hook Hand*/
    (1702140, 4500, 'Weapons'), /*Giant Orchid*/
    (1702141, 4500, 'Weapons'), /*My Buddy Max*/
    (1702142, 4500, 'Weapons'), /*Pink Angel Stick*/
    (1702143, 4500, 'Weapons'), /*Combat Syringe*/
    (1702144, 4500, 'Weapons'), /*Broken Sword*/
    (1702145, 4500, 'Weapons'), /*Bionic Claw*/
    (1702146, 4500, 'Weapons'), /*Skull Staff*/
    (1702147, 4500, 'Weapons'), /*Skull Axe*/
    (1702148, 4500, 'Weapons'), /*Moon Baton*/
    (1702149, 4500, 'Weapons'), /*Tania Sword*/
    (1702150, 4500, 'Weapons'), /*Mercury Sword*/
    (1702151, 4500, 'Weapons'), /*Royal Oaken Staff*/
    (1702152, 4500, 'Weapons'), /*Flame Tongue*/
    (1702153, 4500, 'Weapons'), /*Crissagrim Blade*/
    (1702154, 4500, 'Weapons'), /*Plasma Saber*/
    (1702155, 4500, 'Weapons'), /*Shooting Star*/
    (1702156, 4500, 'Weapons'), /*Forked Turkey*/
    (1702157, 4500, 'Weapons'), /*Burning Marshmellow*/
    (1702158, 4500, 'Weapons'), /*The Jackal*/
    (1702159, 4500, 'Weapons'), /*Blackbeard's Knuckle*/
    (1702160, 4500, 'Weapons'), /*Tiger Paw Knuckle*/
    (1702161, 4500, 'Weapons'), /*Dogged Out*/
    (1702162, 4500, 'Weapons'), /*Koala Doll*/
    (1702163, 4500, 'Weapons'), /*Hot Dog Fork*/
    (1702164, 4500, 'Weapons'), /*Bunny Nunchucks*/
    (1702165, 4500, 'Weapons'), /*My Buddy DJ*/
    (1702166, 4500, 'Weapons'), /*Holiday Candy Cane*/
    (1702167, 4500, 'Weapons'), /*Glow Fingers*/
    (1702168, 4500, 'Weapons'), /*Holiday Tree Ring*/
    (1702169, 4500, 'Weapons'), /*My Buddy Tina*/
    (1702170, 4500, 'Weapons'), /*Electric Knuckle*/
    (1702171, 4500, 'Weapons'), /*Party Popper*/
    (1702172, 4500, 'Weapons'), /*Bluebird*/
    (1702173, 4500, 'Weapons'), /*Hessonite Saber*/
    (1702174, 4500, 'Weapons'), /*Butterfly Staff*/
    (1702175, 4500, 'Weapons'), /*Hot Dog Link*/
    (1702177, 4500, 'Weapons'), /*Power Pesticide*/
    (1702178, 4500, 'Weapons'), /*MDAS Weapon*/
    (1702179, 4500, 'Weapons'), /*Cloud 9 Pillow*/
    (1702180, 4500, 'Weapons'), /*Dark Seraphim*/
    (1702181, 4500, 'Weapons'), /*Picky Ducky*/
    (1702182, 4500, 'Weapons'), /*Giant Pop with a Swirl*/
    (1702183, 4500, 'Weapons'), /*Sunset Seraphim*/
    (1702184, 4500, 'Weapons'), /*Aqua Seraphim*/
    (1702185, 4500, 'Weapons'), /*White & Yellow Seraphim*/
    (1702186, 4500, 'Weapons'), /*3rd Anniversary Weapon*/
    (1702187, 4500, 'Weapons'), /*Patriot Seraphim*/
    (1702188, 4500, 'Weapons'), /*Pink Seraphim*/
    (1702189, 4500, 'Weapons'), /*Crabby*/
    (1702191, 4500, 'Weapons'), /*Rainbow Sabre*/
    (1702193, 4500, 'Weapons'), /*Towel Whip*/
    (1702195, 4500, 'Weapons'), /*MapleGirl Wand*/
    (1702196, 4500, 'Weapons'), /*Fly Blue Bird*/
    (1702197, 4500, 'Weapons'), /*Tsunami Wave*/
    (1702198, 4500, 'Weapons'), /*Bullseye Board*/
    (1702200, 4500, 'Weapons'), /*Plastic umbrella*/
    (1702201, 4500, 'Weapons'), /*Bone Weapon*/
    (1702202, 4500, 'Weapons'), /*Baby Ellie*/
    (1702203, 4500, 'Weapons'), /*Halloween Teddy*/
    (1702204, 4500, 'Weapons'), /*Japanese War Fan*/
    (1702207, 4500, 'Weapons'), /*Musical Violin*/
    (1702208, 4500, 'Weapons'), /*Alligator Tube*/
    (1702209, 4500, 'Weapons'), /*Rudolph Stick*/
    (1702210, 4500, 'Weapons'), /*Santa Buddy*/
    (1702211, 4500, 'Weapons'), /*Blizzard Stick*/
    (1702212, 4500, 'Weapons'), /*Galactic Legend*/
    (1702213, 4500, 'Weapons'), /*Heartbreak Sword*/
    (1702214, 4500, 'Weapons'), /*Whip*/
    (1702215, 4500, 'Weapons'), /*Boleadoras*/
    (1702216, 4500, 'Weapons'), /*Magic Heart Stick*/
    (1702217, 4500, 'Weapons'), /*Ducky Tube*/
    (1702218, 4500, 'Weapons'), /*Dumbell Weapon*/
    (1702219, 4500, 'Weapons'), /*Knockout Boxing Gloves*/
    (1702221, 4500, 'Weapons'), /*Mini Bean Propeller*/
    (1702222, 4500, 'Weapons'), /*Fanfare Firecracker*/
    (1702223, 4500, 'Weapons'), /*Sparkler*/
    (1702226, 4500, 'Weapons'), /*My Buddy Whale*/
    (1702228, 4500, 'Weapons'), /*Choco Banana*/
    (1702229, 4500, 'Weapons'), /*Demon Sickle*/
    (1702230, 4500, 'Weapons'), /*Popsicle Sword*/
    (1702231, 4500, 'Weapons'), /*We Care! Weapon*/
    (1702232, 4500, 'Weapons'), /*My friend Gold Bulldog*/
    (1702233, 4500, 'Weapons'), /*Rainbow Brush*/
    (1702234, 4500, 'Weapons'), /*Pluto Legend Hall*/
    (1702235, 4500, 'Weapons'), /*Metallic Arm*/
    (1702236, 4500, 'Weapons'), /*Death Note*/
    (1702237, 4500, 'Weapons'), /*Inari the White Fox*/
    (1702238, 4500, 'Weapons'), /*Soft Plush Dolphin*/
    (1702239, 4500, 'Weapons'), /*Holy Mystics*/
    (1702246, 4500, 'Weapons'), /*Ghost Weapon*/
    (1702248, 4500, 'Weapons'), /*Rudolph*/
    (1702249, 4500, 'Weapons'), /*Gosling Cushion*/
    (1702250, 4500, 'Weapons'), /*Steel Briefcase*/
    (1702251, 4500, 'Weapons'), /*Saw Machine Gun*/
    (1702252, 4500, 'Weapons'), /*Hunting Hawk*/
    (1702253, 4500, 'Weapons'), /*Bunny Umbrella*/
    (1702256, 4500, 'Weapons'), /*Mini Wind Archer*/
    (1702257, 4500, 'Weapons'), /*Mini Dawn Warrior*/
    (1702258, 4500, 'Weapons'), /*Mini Blaze Wizard*/
    (1702259, 4500, 'Weapons'), /*Mini Thunder Breaker*/
    (1702260, 4500, 'Weapons'), /*Mini Night Walker*/
    (1702261, 4500, 'Weapons'), /*Cherry Blossom Weapon*/
    (1702262, 4500, 'Weapons'), /*Green Leaf Guards*/
    (1702263, 4500, 'Weapons'), /*Kitty Spirit Weapon*/
    (1702264, 4500, 'Weapons'), /*Strawberry Basket*/
    (1702266, 4500, 'Weapons'), /*Sushine Pan*/
    (1702268, 4500, 'Weapons'), /*Evan Wand*/
    (1702274, 4500, 'Weapons'), /*Dragon Lord Gloves*/
    (1702275, 4500, 'Weapons'), /*Rainbow Umbrella*/
    (1702276, 4500, 'Weapons'), /*Rainbow Bow*/
    (1702277, 4500, 'Weapons'), /*Test Pen*/
    (1702278, 4500, 'Weapons'), /*King Crow Fan*/
    (1702279, 4500, 'Weapons'), /*Shining Feather Sword*/
    (1702280, 4500, 'Weapons'), /*Shining Feather Slayer*/
    (1702281, 4500, 'Weapons'), /*Shining Feather Lord*/
    (1702282, 4500, 'Weapons'), /*Shining Feather Bow*/
    (1702283, 4500, 'Weapons'), /*Shining Feather Knuckle*/
    (1702284, 4500, 'Weapons'), /*Handbag (Pink)*/
    (1702285, 4500, 'Weapons'), /*Handbag (Blue)*/
    (1702286, 4500, 'Weapons'), /*Marchosias*/
    (1702287, 4500, 'Weapons'), /*Battle Mage Staff*/
    (1702288, 4500, 'Weapons'), /*Wild Hunter Crossbow*/
    (1702289, 4500, 'Weapons'), /*Royal Marine Flag*/
    (1702293, 4500, 'Weapons'), /*Suitcase*/
    (1702295, 4500, 'Weapons'), /*Playing Cards*/
    (1702296, 4500, 'Weapons'), /*Yo Yo*/
    (1702299, 4500, 'Weapons'), /*Chocolate Dipped Stick*/
    (1702301, 4500, 'Weapons'), /*Rabbit Weapon*/
    (1702302, 4500, 'Weapons'), /*Alien Mug*/
    (1702303, 4500, 'Weapons'), /*Baby Bottle*/
    (1702304, 4500, 'Weapons'), /*Funny Punch Yo-yo*/
    (1702305, 4500, 'Weapons'), /*Carrot*/
    (1702306, 4500, 'Weapons'), /*Burning Breeze Fan*/
    (1702308, 4500, 'Weapons'), /*Spring Blossoms*/
    (1702309, 4500, 'Weapons'), /*Rainbow Sparkle*/
    (1702310, 4500, 'Weapons'), /*6th Anniversary Party Wand*/
    (1702311, 4500, 'Weapons'), /*MSE 4 Years & Unstoppable Star*/
    (1702313, 4500, 'Weapons'), /*Orange Seraphim*/
    (1702314, 4500, 'Weapons'), /*Heaven's Seraphim*/
    (1702315, 4500, 'Weapons'), /*Stellar Seraphim*/
    (1702316, 4500, 'Weapons'), /*Dynamic Seraphim*/
    (1702317, 4500, 'Weapons'), /*Bloody Ruby Sabre*/
    (1702318, 4500, 'Weapons'), /*Twilight Sabre*/
    (1702319, 4500, 'Weapons'), /*Evergreen Sabre*/
    (1702320, 4500, 'Weapons'), /*Slate Thunder Sabre*/
    (1702321, 4500, 'Weapons'), /*Dark Magenta Sabre*/
    (1702322, 4500, 'Weapons'), /*Soild Black Sabre*/
    (1702323, 4500, 'Weapons'), /*Ombra & Luce Sabre*/
    (1702324, 4500, 'Weapons'), /*Shock Wave*/
    (1702328, 4500, 'Weapons'), /*Pink Angel Syringe*/
    (1702329, 4500, 'Weapons'), /*Strawberry Delight*/
    (1702330, 4500, 'Weapons'), /*Milky Way*/
    (1702333, 4500, 'Weapons'), /*Strawberry Sword*/
    (1702334, 4500, 'Weapons'), /*Crystal Fantasia Wand*/
    (1702335, 4500, 'Weapons'), /*Alchemist Potion Weapon*/
    (1702336, 4500, 'Weapons'), /*Lord Tempest*/
    (1702337, 4500, 'Weapons'), /*Lightning Soul*/
    (1702340, 4500, 'Weapons'), /*Rabbit in a Hat*/
    (1702341, 4500, 'Weapons'), /*Sweet Lollipop*/
    (1702342, 4500, 'Weapons'), /*Orchid's Bunny Doll*/
    (1702344, 4500, 'Weapons'), /*Boom Box*/
    (1702345, 4500, 'Weapons'), /*Fierce Cat*/
    (1702346, 4500, 'Weapons'), /*Lucky Pouch Weapon*/
    (1702347, 4500, 'Weapons'), /*Fortune Flash*/
    (1702348, 4500, 'Weapons'), /*Snowflake Rod*/
    (1702350, 4500, 'Weapons'), /*Chocolatier Stick*/
    (1702351, 4500, 'Weapons'), /*Tedtacular Bearingtons*/
    (1702352, 4500, 'Weapons'), /*Magic Herb Teaspoon*/
    (1702355, 4500, 'Weapons'), /*Lucky Weapon*/
    (1702356, 4500, 'Weapons'), /*Legendary Weapon*/
    (1702357, 4500, 'Weapons'), /*Starfall Magic Square*/
    (1702358, 4500, 'Weapons'), /*Pink Bean Buddy*/
    (1702359, 4500, 'Weapons'), /*Blue Angel Syringe*/
    (1702360, 4500, 'Weapons'), /*Coin Sword*/
    (1702361, 4500, 'Weapons'), /*Hunter Hawk*/
    (1702363, 4500, 'Weapons'), /*Crystalline Sheen*/
    (1702364, 4500, 'Weapons'), /*Dragon Familiar*/
    (1702365, 4500, 'Weapons'), /*Tedimus Beartaculous*/
    (1702366, 4500, 'Weapons'), /*Shark-sicle*/
    (1702367, 4500, 'Weapons'), /*Rose Butterwand*/
    (1702368, 4500, 'Weapons'), /*Iris Butterwand*/
    (1702371, 4500, 'Weapons'), /*Pimp Stick*/
    (1702372, 4500, 'Weapons'), /*Pimp Chalice*/
    (1702374, 4500, 'Weapons'), /*Bladed Falcon's Katana*/
    (1702375, 4500, 'Weapons'), /*Atlantis*/
    (1702376, 4500, 'Weapons'), /*Onmyouji Fan*/
    (1702379, 4500, 'Weapons'), /*Arabian Magic Lamp*/
    (1702380, 4500, 'Weapons'), /*Azure Crystal Crusher*/
    (1702381, 4500, 'Weapons'), /*Twin Crescent Blade*/
    (1702382, 4500, 'Weapons'), /*Persimmon Branch*/
    (1702385, 4500, 'Weapons'), /*[MS Special] Hunting Hawk*/
    (1702386, 4500, 'Weapons'), /*[MS Special] Crystalline Sheen*/
    (1702387, 4500, 'Weapons'), /*[MS Special] Dragon Familiar*/
    (1702388, 4500, 'Weapons'), /*[MS Special] Tedimus Beartaculous*/
    (1702389, 4500, 'Weapons'), /*[MS Special] Fly Blue Bird*/
    (1702390, 4500, 'Weapons'), /*Halloween Leopard Umbrella*/
    (1702392, 4500, 'Weapons'), /*Dark Devil Weapon*/
    (1702393, 4500, 'Weapons'), /*Slither Style Snake Sword*/
    (1702394, 4500, 'Weapons'), /*Golden Holy Cup*/
    (1702395, 4500, 'Weapons'), /*Baller Cane*/
    (1702397, 4500, 'Weapons'), /*Twinkle Sparkle*/
    (1702398, 4500, 'Weapons'), /*Fairy Lamp*/
    (1702399, 4500, 'Weapons'), /*Neo Light Sword*/
    (1702400, 4500, 'Weapons'), /*Lotus's Bunny Doll*/
    (1702401, 4500, 'Weapons'), /*Rabbit with Carrot*/
    (1702402, 4500, 'Weapons'), /*Stylish Iron*/
    (1702403, 4500, 'Weapons'), /*Sherlock's Magnifier*/
    (1702404, 4500, 'Weapons'), /*Muffin*/
    (1702405, 4500, 'Weapons'), /*Starlight Heart Scepter*/
    (1702408, 4500, 'Weapons'), /*Francis's Puppet*/
    (1702409, 4500, 'Weapons'), /*Hilla Weapon*/
    (1702415, 4500, 'Weapons'), /*Dreamy Candy Pillow*/
    (1702416, 4500, 'Weapons'), /*Lord of the Carrots*/
    (1702417, 4500, 'Weapons'), /*Blue Rose Parasol*/
    (1702423, 4500, 'Weapons'), /*Goblin Fire*/
    (1702431, 4500, 'Weapons'), /*GM Nori's Syringe*/
    (1702433, 4500, 'Weapons'), /*Salamander*/
    (1702442, 4500, 'Weapons'), /*Baseball Bat*/
    (1702443, 4500, 'Weapons'), /*Puppeteer's Promise*/
    (1702444, 4500, 'Weapons'), /*Fermata*/
    (1702445, 4500, 'Weapons'), /*Detective Glass*/
    (1702446, 4500, 'Weapons'), /*Sea Otter Slammer*/
    (1702451, 4500, 'Weapons'), /*Superstar Microphone*/
    (1702453, 4500, 'Weapons'), /*Astral Bolt*/
    (1702454, 4500, 'Weapons'), /*Seal Wave Snuggler*/
    (1702455, 4500, 'Weapons'), /*RED Paint Bucket*/
    (1702456, 4500, 'Weapons'), /*Fairy Pico*/
    (1702457, 4500, 'Weapons'), /*Fantastic Ice Pop*/
    (1702458, 4500, 'Weapons'), /*Fireworks Fan*/
    (1702459, 4500, 'Weapons'), /*Cotton Candy*/
    (1702460, 4500, 'Weapons'), /*Star Weapon*/
    (1702461, 4500, 'Weapons'), /*Chicky-Chicky Boom*/
    (1702462, 4500, 'Weapons'), /*Fantasy Butterfly Flower*/
    (1702464, 4500, 'Weapons'), /*Sparkling Buddy*/
    (1702466, 4500, 'Weapons'), /*Mint Chocolatier Stick*/
    (1702467, 4500, 'Weapons'), /*Cotton Candy Cloud*/
    (1702468, 4500, 'Weapons'), /*Soft Chocolate Fondue Scepter*/
    (1702469, 4500, 'Weapons'), /*Arachne*/
    (1702470, 4500, 'Weapons'), /*Free Spirit*/
    (1702472, 4500, 'Weapons'), /*Vampire Phantom's Fate*/
    (1702473, 4500, 'Weapons'), /*Shadow Executor*/
    (1702475, 4500, 'Weapons'), /*Maha*/
    (1702476, 4500, 'Weapons'), /*Sweet Snake*/
    (1702477, 4500, 'Weapons'), /*Evil Skull*/
    (1702478, 4500, 'Weapons'), /*Cat Soul*/
    (1702479, 4500, 'Weapons'), /*バットソード*/
    (1702480, 4500, 'Weapons'), /*Celena*/
    (1702485, 4500, 'Weapons'), /*Goodie Bundle*/
    (1702486, 4500, 'Weapons'), /*Fluttering Camellia Flower*/
    (1702487, 4500, 'Weapons'), /*Red Flower*/
    (1702488, 4500, 'Weapons'), /*Pony's Carrot*/
    (1702489, 4500, 'Weapons'), /*Sweet Chocolate Fondue Stick*/
    (1702491, 4500, 'Weapons'), /*Bubble Cleaner*/
    (1702492, 4500, 'Weapons'), /*Red Lantern*/
    (1702497, 4500, 'Weapons'), /*Sparkling Luck Sack*/
    (1702499, 4500, 'Weapons'), /*Guardian Scepter*/
    (1702501, 4500, 'Weapons'), /*Flower Dance*/
    (1702502, 4500, 'Weapons'), /*Cane From the Stars*/
    (1702503, 4500, 'Weapons'), /*Bubbling Shot*/
    (1702504, 4500, 'Weapons'), /*Frozen Heart*/
    (1702505, 4500, 'Weapons'), /*Breezy Bamboo*/
    (1702506, 4500, 'Weapons'), /*Perfect Cooking */
    (1702507, 4500, 'Weapons'), /*Contemporary Chic Fan*/
    (1702509, 4500, 'Weapons'), /*Sunny Rainbow */
    (1702510, 4500, 'Weapons'), /*Rabbit and Bear Flashlight*/
    (1702512, 4500, 'Weapons'), /*Crown Rod*/
    (1702519, 4500, 'Weapons'), /*Pink Antique Parasol*/
    (1702521, 4500, 'Weapons'), /*Blue Swallow*/
    (1702522, 4500, 'Weapons'), /*Viking Sword for Transformation*/
    (1702523, 4500, 'Weapons'), /*Sunny Day Rainbow*/
    (1702524, 4500, 'Weapons'), /*Plump Tomato*/
    (1702525, 4500, 'Weapons'), /*Final Ingredient*/
    (1702526, 4500, 'Weapons'), /*Rifle Blade*/
    (1702528, 4500, 'Weapons'), /*Xylophone Melody*/
    (1702529, 4500, 'Weapons'), /*Shadow Lamp*/
    (1702530, 4500, 'Weapons'), /*Sweet Summer Hammer*/
    (1702533, 4500, 'Weapons'), /*Photo-op*/
    (1702534, 4500, 'Weapons'), /*Baby Paci*/
    (1702535, 4500, 'Weapons'), /*Hula Hula Penglyn*/
    (1702538, 4500, 'Weapons'), /*Dewdrop Lantern*/
    (1702540, 4500, 'Weapons'), /*Here's the Flashlight!*/
    (1702541, 4500, 'Weapons'), /*Perfect Baby*/
    (1702547, 4500, 'Weapons'), /*Sweet Persimmon*/
    (1702549, 4500, 'Weapons'), /*Pom-pom Power*/
    (1702550, 4500, 'Weapons'), /*Peach Trio*/
    (1702551, 4500, 'Weapons'), /*Korean Thanksgiving Persimmon Branch*/
    (1702553, 4500, 'Weapons'), /*Dangerous Medicine Bottle*/
    (1702554, 4500, 'Weapons'), /*Scary Huge Hand*/
    (1702555, 4500, 'Weapons'), /*Noble Lady's Black Fan*/
    (1702556, 4500, 'Weapons'), /*Blade*/
    (1702557, 4500, 'Weapons'), /*Duster*/
    (1702559, 4500, 'Weapons'), /*Puppy Pal Weapon (White)*/
    (1702560, 4500, 'Weapons'), /*Puppy Pal Weapon (Brown)*/
    (1702561, 4500, 'Weapons'), /*Sweet Fork Cake*/
    (1702562, 4500, 'Weapons'), /*Winter Snowman*/
    (1702565, 4500, 'Weapons'), /*Death's Scythe*/
    (1702566, 4500, 'Weapons'), /*Rammy Scepter*/
    (1702567, 4500, 'Weapons'), /*Rawrin' Tiger Weapon*/
    (1702570, 4500, 'Weapons'), /*Fluffy Snow Bunny*/
    (1702571, 4500, 'Weapons'), /*Top Snow Shovel*/
    (1702572, 4500, 'Weapons'), /*Red Rose Umbrella*/
    (1702574, 4500, 'Weapons'), /*Beast Trainer*/
    (1702575, 4500, 'Weapons'), /*Lovely Chocolate Basket*/
    (1702576, 4500, 'Weapons'), /*Ground Pounder*/
    (1702577, 4500, 'Weapons'), /*Lalala Goldfish Fishing Net*/
    (1702579, 4500, 'Weapons'), /*Crystal Cat Weapon*/
    (1702581, 4500, 'Weapons'), /*Sweetie Bros*/
    (1702583, 4500, 'Weapons'), /*Kitty Pringles*/
    (1702584, 4500, 'Weapons'), /*Cutie Puppy*/
    (1702586, 4500, 'Weapons'), /*Dreaming Dandelion*/
    (1702587, 4500, 'Weapons'), /*Rockin' Guitar*/
    (1702588, 4500, 'Weapons'), /*Black Cat Plush*/
    (1702589, 4500, 'Weapons'), /*Fairy Blossom*/
    (1702590, 4500, 'Weapons'), /*자이언트 돼지바*/
    (1702591, 4500, 'Weapons'), /*Grand Romance*/
    (1702593, 4500, 'Weapons'), /*Winding Sky Bamboo*/
    (1702594, 4500, 'Weapons'), /*Sweepy Orchid*/
    (1702595, 4500, 'Weapons'), /*Mint Kitty Tea Time*/
    (1702597, 4500, 'Weapons'), /*Rainbow Seashell*/
    (1702599, 4500, 'Weapons'), /*Hoya Roar*/
    (1702600, 4500, 'Weapons'), /*Pasta*/
    (1702601, 4500, 'Weapons'), /*Bacon*/
    (1702602, 4500, 'Weapons'), /*Hamburger*/
    (1702603, 4500, 'Weapons'), /*Rib Steak*/
    (1702604, 4500, 'Weapons'), /*Parfait*/
    (1702606, 4500, 'Weapons'), /*Squid*/
    (1702607, 4500, 'Weapons'), /*Cheese Carrot Stick*/
    (1702608, 4500, 'Weapons'), /*Marine Stripe Umbrella*/
    (1702611, 4500, 'Weapons'), /*Duckling Cross Bag Weapon*/
    (1702616, 4500, 'Weapons'), /*Ducky Candy Bar*/
    (1702617, 4500, 'Weapons'), /*Lotus Fantasy*/
    (1702619, 4500, 'Weapons'), /*Musical Green Onion*/
    (1702620, 4500, 'Weapons'), /*Mystery Dice*/
    (1702623, 4500, 'Weapons'), /*Today Jay*/
    (1702624, 4500, 'Weapons'), /*Master Time*/
    (1702625, 4500, 'Weapons'), /*Sparking Bluebird*/
    (1702626, 4500, 'Weapons'), /*British Handbag Weapon*/
    (1702627, 4500, 'Weapons'), /*Sakura Sword*/
    (1702628, 4500, 'Weapons'), /*Farmer's Glorious Egg Stick*/
    (1702629, 4500, 'Weapons'), /*Vintage Cellphone*/
    (1702630, 4500, 'Weapons'), /*Striking Lantern*/
    (1702631, 4500, 'Weapons'), /*Bloody Fairytale*/
    (1702632, 4500, 'Weapons'), /*Zakum Arms*/
    (1702633, 4500, 'Weapons'), /*Banana Monkey Attacker*/
    (1702634, 4500, 'Weapons'), /*Maple Zombies*/
    (1702635, 4500, 'Weapons'), /*Mr. Orlov Coin Sword*/
    (1702637, 4500, 'Weapons'), /*Hard Carrier Suitcase*/
    (1702638, 4500, 'Weapons'), /*Blue Marine Thirst For Knowledge*/
    (1702639, 4500, 'Weapons'), /*Kitty Bangle*/
    (1702640, 4500, 'Weapons'), /*Bunny Snowman Attacker*/
    (1702641, 4500, 'Weapons'), /*Dragon Master's Wand*/
    (1702643, 4500, 'Weapons'), /*Elven Monarch's Dual Bowguns*/
    (1702645, 4500, 'Weapons'), /*Phantom's Cane*/
    (1702647, 4500, 'Weapons'), /*Maha the Polearm*/
    (1702649, 4500, 'Weapons'), /*Shining Rod of Equilibrium*/
    (1702651, 4500, 'Weapons'), /*Forgotten Hero's Knuckle*/
    (1702654, 4500, 'Weapons'), /*Mr. Hot Spring Kitty*/
    (1702655, 4500, 'Weapons'), /*Lil Mercedes*/
    (1702656, 4500, 'Weapons'), /*Lil Luminous*/
    (1702657, 4500, 'Weapons'), /*Lil Shade*/
    (1702659, 4500, 'Weapons'), /*Timemaster*/
    (1702665, 4500, 'Weapons'), /*Lil Evan*/
    (1702666, 4500, 'Weapons'), /*Lil Aran*/
    (1702667, 4500, 'Weapons'), /*Lil Phantom*/
    (1702668, 4500, 'Weapons'), /*Winter Deer Tambourine*/
    (1702671, 4500, 'Weapons'), /*Magic Tome Weapon*/
    (1702672, 4500, 'Weapons'), /*Duckling Cross Bag*/
    (1702673, 4500, 'Weapons'), /*Monkey Banana*/
    (1702675, 4500, 'Weapons'), /*Smile Seed Weapon*/
    (1702676, 4500, 'Weapons'), /*Muse Crystal*/
    (1702677, 4500, 'Weapons'), /*Lil Damien*/
    (1702678, 4500, 'Weapons'), /*Lil Alicia*/
    (1702679, 4500, 'Weapons'), /*Playful Black Nyanya*/
    (1702682, 4500, 'Weapons'), /*Triple Fish Skewer*/
    (1702684, 4500, 'Weapons'), /*Blue Phoenix Weapon*/
    (1702685, 4500, 'Weapons'), /*Red Phoenix Weapon*/
    (1702686, 4500, 'Weapons'), /*Sweet Pig Weapon*/
    (1702692, 4500, 'Weapons'), /*Chicken Cutie Weapon*/
    (1702693, 4500, 'Weapons'), /*Bubble Leaf Weapon*/
    (1702697, 4500, 'Weapons'), /*Cup Cat Weapon*/
    (1702698, 4500, 'Weapons'), /*Blaster Weapon*/
    (1702699, 4500, 'Weapons'), /*Colorful Beach Ball*/
    (1702710, 4500, 'Weapons'), /*Kamaitachi's Sickle*/
    (1702711, 4500, 'Weapons'), /*Owl Spellbook*/
    (1702712, 4500, 'Weapons'), /*Moon Bunny Bell Weapon*/
    (1702713, 4500, 'Weapons'), /*Bichon Paw Weapon*/
    (1702714, 4500, 'Weapons'), /*Witch's Staff*/

/* Hats */
    (1000000, 3000, 'Hats'), /*Blue Beanie*/
    (1000001, 3000, 'Hats'), /*Fine Black Hanbok Hat*/
    (1000002, 3000, 'Hats'), /*Fine Blue Hanbok Hat*/
    (1000003, 3000, 'Hats'), /*Scream Mask*/
    (1000004, 3000, 'Hats'), /*Old School Uniform Hat*/
    (1000005, 3000, 'Hats'), /*Men's Ninja Hat*/
    (1000006, 3000, 'Hats'), /*Samurai Hair-do*/
    (1000007, 3000, 'Hats'), /*Hat of Death*/
    (1000008, 3000, 'Hats'), /*Detective Hat*/
    (1000009, 3000, 'Hats'), /*Mesoranger Red Helmet*/
    (1000010, 3000, 'Hats'), /*Mesoranger Blue Helmet*/
    (1000011, 3000, 'Hats'), /*Mesoranger Green Helmet*/
    (1000012, 3000, 'Hats'), /*Mesoranger Black Helmet*/
    (1000013, 3000, 'Hats'), /*Yellow Crown*/
    (1000014, 3000, 'Hats'), /*Green Crown*/
    (1000015, 3000, 'Hats'), /*Blue Crown*/
    (1000016, 3000, 'Hats'), /*Red Crown*/
    (1000017, 3000, 'Hats'), /*Van Hat*/
    (1000018, 3000, 'Hats'), /*Kuniragi Hat*/
    (1000019, 3000, 'Hats'), /*Green Goya Hat*/
    (1000020, 3000, 'Hats'), /*Chief Hat*/
    (1000021, 3000, 'Hats'), /*General's Wig*/
    (1000023, 3000, 'Hats'), /*Race Ace Cap*/
    (1000024, 3000, 'Hats'), /*Oriental Bridegroom Hat*/
    (1000026, 3000, 'Hats'), /*Santa Boy Hat*/
    (1000027, 3000, 'Hats'), /*Lunar Celebration Cap*/
    (1000028, 3000, 'Hats'), /*Korean Official Hat*/
    (1000029, 3000, 'Hats'), /*Wedding veil*/
    (1000030, 3000, 'Hats'), /*Sachiel Wig (M)*/
    (1000031, 3000, 'Hats'), /*Veamoth Wig (M)*/
    (1000032, 3000, 'Hats'), /*Janus Wig (M)*/
    (1000035, 3000, 'Hats'), /*White Floral Hat*/
    (1000041, 3000, 'Hats'), /*Napoleon Hat*/
    (1000043, 3000, 'Hats'), /*Santa Hat*/
    (1000044, 3000, 'Hats'), /*Twinkling Boy Hat*/
    (1000045, 3000, 'Hats'), /*Dark Force Horns (M) */
    (1000046, 3000, 'Hats'), /*Elven Spirit Band (M)*/
    (1000050, 3000, 'Hats'), /*Mint Snow Cap*/
    (1000051, 3000, 'Hats'), /*Aerial Elven Spirit Band*/
    (1000058, 3000, 'Hats'), /*Evergreen Magistrate Hat*/
    (1000060, 3000, 'Hats'), /*Dark Force Horns*/
    (1000061, 3000, 'Hats'), /*Alps Boy Hat*/
    (1000062, 3000, 'Hats'), /*Cool Carrot Hat*/
    (1000069, 3000, 'Hats'), /*Moonlight Floral Hat*/
    (1000070, 3000, 'Hats'), /*Bon-Bon Pony Hat*/
    (1000071, 3000, 'Hats'), /*Blue Pedora*/
    (1000072, 3000, 'Hats'), /*Jumpy Blue*/
    (1000074, 3000, 'Hats'), /*Yellow Picnic Beret*/
    (1000076, 3000, 'Hats'), /*Red Dusk*/
    (1000077, 3000, 'Hats'), /*Dylan's Silk Hat*/
    (1000079, 3000, 'Hats'), /*Mad Doctor Bolt*/
    (1002314, 3000, 'Hats'), /*Zombie Mushroom Hat*/
    (1000082, 3000, 'Hats'), /*Fashionista Wig (M)*/
    (1000083, 3000, 'Hats'), /*Maple Festival Wig*/
    (1000084, 3000, 'Hats'), /*Little Wing Cap*/
    (1000085, 3000, 'Hats'), /*Aquamarine Gem*/
    (1000086, 3000, 'Hats'), /*Team Wig*/
    (1000087, 3000, 'Hats'), /*Ribbon Headband*/
    (1000088, 3000, 'Hats'), /*Kinesis Wig*/
    (1000090, 3000, 'Hats'), /*Penguin Hood*/
    (1000091, 3000, 'Hats'), /*Bloody Guardian Hood*/
    (1001000, 3000, 'Hats'), /*Orange Beanie*/
    (1001001, 3000, 'Hats'), /*Hanbok Jobawi*/
    (1001002, 3000, 'Hats'), /*Witch Hat*/
    (1001003, 3000, 'Hats'), /*Pink Nurse Hat*/
    (1001004, 3000, 'Hats'), /*White Nurse Hat*/
    (1001005, 3000, 'Hats'), /*Women's Ninja Hat*/
    (1001006, 3000, 'Hats'), /*SF Ninja Hat*/
    (1001007, 3000, 'Hats'), /*Miko Wig*/
    (1001008, 3000, 'Hats'), /*A Ladylike Hat*/
    (1001009, 3000, 'Hats'), /*Ribbon*/
    (1001010, 3000, 'Hats'), /*Teddy Bear Hat*/
    (1001011, 3000, 'Hats'), /*Strawberry Headgear*/
    (1001012, 3000, 'Hats'), /*Tiara*/
    (1001013, 3000, 'Hats'), /*Beret*/
    (1001014, 3000, 'Hats'), /*Mesoranger Pink Helmet*/
    (1001015, 3000, 'Hats'), /*Mesoranger Yellow Helmet*/
    (1001017, 3000, 'Hats'), /*Princess Tiara*/
    (1001018, 3000, 'Hats'), /*Lady Blue*/
    (1001019, 3000, 'Hats'), /*Lady Pink*/
    (1001020, 3000, 'Hats'), /*Lady Yellow*/
    (1001021, 3000, 'Hats'), /*The Gabera Hat*/
    (1001022, 3000, 'Hats'), /*Van Hat with Heart*/
    (1001023, 3000, 'Hats'), /*Picnic Hat*/
    (1001024, 3000, 'Hats'), /*Diamond Tiara*/
    (1001025, 3000, 'Hats'), /*Ruby Tiara*/
    (1001026, 3000, 'Hats'), /*Red-Feathered Bandana*/
    (1001027, 3000, 'Hats'), /*Blue-Feathered Bandana*/
    (1001028, 3000, 'Hats'), /*Jami Wig*/
    (1001029, 3000, 'Hats'), /*Yellow Bride's Veil*/
    (1001030, 3000, 'Hats'), /*Diao Chan Headpiece*/
    (1001031, 3000, 'Hats'), /*White Cat Ears*/
    (1001032, 3000, 'Hats'), /*Black Cat Ears*/
    (1001033, 3000, 'Hats'), /*Maid Hat*/
    (1001034, 3000, 'Hats'), /*Oriental Princess Hat*/
    (1001036, 3000, 'Hats'), /*Santa Girl Hat*/
    (1001037, 3000, 'Hats'), /*Leopard Print Hat*/
    (1001038, 3000, 'Hats'), /*Korean Dress Wig*/
    (1001039, 3000, 'Hats'), /*Lunar Celebration Ornament*/
    (1001040, 3000, 'Hats'), /*Royal Maid Hat*/
    (1001041, 3000, 'Hats'), /*Royal Nurse Hat*/
    (1001042, 3000, 'Hats'), /*Purple Bride's Veil*/
    (1001043, 3000, 'Hats'), /*Royal Tiara*/
    (1001044, 3000, 'Hats'), /*Green Bride's Veil*/
    (1001045, 3000, 'Hats'), /*Sachiel Wig (F)*/
    (1001046, 3000, 'Hats'), /*Veamoth Wig (F)*/
    (1001047, 3000, 'Hats'), /*Janus Wig (F)*/
    (1001048, 3000, 'Hats'), /*Gothic Mini Hat*/
    (1001049, 3000, 'Hats'), /*Gothic Headband*/
    (1001055, 3000, 'Hats'), /*Strawberry Milk Frill Bonnet*/
    (1001058, 3000, 'Hats'), /*Native American Chief Hat*/
    (1001061, 3000, 'Hats'), /*Elizabeth Hat*/
    (1001063, 3000, 'Hats'), /*Dear Christmas*/
    (1001064, 3000, 'Hats'), /*Twinkling Girl Hat*/
    (1001065, 3000, 'Hats'), /*Pink Angel Wing Cap*/
    (1001066, 3000, 'Hats'), /*Red Hood Bandana*/
    (1001068, 3000, 'Hats'), /*Dark Force Horns (F) */
    (1001069, 3000, 'Hats'), /*Elven Spirit Band (F) */
    (1001070, 3000, 'Hats'), /*Gold Angora Gatsby*/
    (1001071, 3000, 'Hats'), /*Silver Angora Gatsby*/
    (1001075, 3000, 'Hats'), /*Star of Ereve*/
    (1001076, 3000, 'Hats'), /*Cherry Snow Cap*/
    (1001083, 3000, 'Hats'), /*Angelic Ribbon*/
    (1001084, 3000, 'Hats'), /*Angelic Navy Cap*/
    (1001085, 3000, 'Hats'), /*Pinky Butterfly Hair Pin*/
    (1001088, 3000, 'Hats'), /*Alps Girl Hat*/
    (1001089, 3000, 'Hats'), /*Warm Carrot Hat*/
    (1001090, 3000, 'Hats'), /*Fluffy Cat Hood*/
    (1001091, 3000, 'Hats'), /*Dumpling Head Wig*/
    (1001092, 3000, 'Hats'), /*Moonlight Floral Hairpin*/
    (1001093, 3000, 'Hats'), /*Bon-Bon Pony Cap*/
    (1001094, 3000, 'Hats'), /*Lace Cap*/
    (1001095, 3000, 'Hats'), /*Jumpy Pink*/
    (1001097, 3000, 'Hats'), /*White Picnic Beret*/
    (1001098, 3000, 'Hats'), /*Blue Twilight*/
    (1001099, 3000, 'Hats'), /*Rosalia's Rose*/
    (1001100, 3000, 'Hats'), /*Ribbon Angel Cap*/
    (1001103, 3000, 'Hats'), /*Fashionista Wig (F)*/
    (1001105, 3000, 'Hats'), /*Little Wing Fedora*/
    (1001106, 3000, 'Hats'), /*Pink Diamond Gem*/
    (1001107, 3000, 'Hats'), /*Momo Wig*/
    (1001109, 3000, 'Hats'), /*Odette Tiara*/
    (1001113, 3000, 'Hats'), /*Bloody Veil*/
    (1002000, 3000, 'Hats'), /*Brown Flight Headgear*/
    (1002015, 3000, 'Hats'), /*Red Swimming Goggle*/
    (1002018, 3000, 'Hats'), /*Green Camping Hat*/
    (1002031, 3000, 'Hats'), /*Cat Hat*/
    (1002032, 3000, 'Hats'), /*Puffy Brown Hat*/
    (1002070, 3000, 'Hats'), /*Green Swimming Goggle*/
    (1002071, 3000, 'Hats'), /*Blue Swimming Goggle*/
    (1002076, 3000, 'Hats'), /*Red Flight Headgear*/
    (1002077, 3000, 'Hats'), /*Blue Flight Headgear*/
    (1002078, 3000, 'Hats'), /*Sky Blue Camping Hat*/
    (1002079, 3000, 'Hats'), /*Pink Camping Hat*/
    (1002186, 3000, 'Hats'), /*Transparent Hat*/
    (1002187, 3000, 'Hats'), /*Blue Cowboy Hat*/
    (1002188, 3000, 'Hats'), /*Red Cowboy Hat*/
    (1002189, 3000, 'Hats'), /*Dark Cowboy Hat*/
    (1002190, 3000, 'Hats'), /*Blue Pre-School Hat*/
    (1002191, 3000, 'Hats'), /*Red Pre-School Hat*/
    (1002192, 3000, 'Hats'), /*Blue Chinese Undead Hat*/
    (1002193, 3000, 'Hats'), /*Maroon Chinese Undead Hat*/
    (1002194, 3000, 'Hats'), /*Rosy Swimming Cap*/
    (1002195, 3000, 'Hats'), /*Flowery Swimming Cap*/
    (1002196, 3000, 'Hats'), /*Blue Baseball Helmet*/
    (1002197, 3000, 'Hats'), /*Red Baseball Helmet*/
    (1002198, 3000, 'Hats'), /*Indigo Baseball Helmet*/
    (1002199, 3000, 'Hats'), /*Black Baseball Helmet*/
    (1002200, 3000, 'Hats'), /*Green Visor*/
    (1002201, 3000, 'Hats'), /*Sky Blue Visor*/
    (1002202, 3000, 'Hats'), /*Orange Visor*/
    (1002203, 3000, 'Hats'), /*Yellow Rain Cap*/
    (1002204, 3000, 'Hats'), /*Red Rain Cap*/
    (1002205, 3000, 'Hats'), /*Sky Blue Rain Cap*/
    (1002206, 3000, 'Hats'), /*Green Rain Cap*/
    (1002219, 3000, 'Hats'), /*Destreza Hat*/
    (1002220, 3000, 'Hats'), /*Black Slanted Visor*/
    (1002221, 3000, 'Hats'), /*Purple Slanted Visor*/
    (1002222, 3000, 'Hats'), /*Red Upside-Down Visor*/
    (1002223, 3000, 'Hats'), /*Blue Upside-Down Visor*/
    (1002224, 3000, 'Hats'), /*Tiger Mask*/
    (1002226, 3000, 'Hats'), /*Fashionable Hat*/
    (1002227, 3000, 'Hats'), /*Blue Fisherman Hat*/
    (1002228, 3000, 'Hats'), /*Cabbie*/
    (1002229, 3000, 'Hats'), /*Goggled Red Cap*/
    (1002230, 3000, 'Hats'), /*Goggled Black Cap*/
    (1002231, 3000, 'Hats'), /*Goggled Blue Cap*/
    (1002232, 3000, 'Hats'), /*Starry Red Beanie*/
    (1002233, 3000, 'Hats'), /*Starry Pink Beanie*/
    (1002234, 3000, 'Hats'), /*Starry Sky Blue Beanie*/
    (1002235, 3000, 'Hats'), /*Sky Blue Goggled Beanie*/
    (1002236, 3000, 'Hats'), /*Khaki Goggled Beanie*/
    (1002237, 3000, 'Hats'), /*Blue Cap*/
    (1002238, 3000, 'Hats'), /*Construction Hardhat*/
    (1002239, 3000, 'Hats'), /*The Legendary Gold Poop Hat*/
    (1002240, 3000, 'Hats'), /*Hajimaki*/
    (1002241, 3000, 'Hats'), /*Techwin Wig*/
    (1002250, 3000, 'Hats'), /*Headphone Bandana*/
    (1002251, 3000, 'Hats'), /*The Graduation Hat*/
    (1002255, 3000, 'Hats'), /*Circus Cowboy Hat*/
    (1002256, 3000, 'Hats'), /*Orange Mushroom Hat*/
    (1002257, 3000, 'Hats'), /*Blue Mushroom Hat*/
    (1002258, 3000, 'Hats'), /*Blue Diamondy Bandana*/
    (1002259, 3000, 'Hats'), /*Black Top Hat*/
    (1002260, 3000, 'Hats'), /*Yellow Trucker Hat*/
    (1002261, 3000, 'Hats'), /*Blue Trucker Hat*/
    (1002262, 3000, 'Hats'), /*Red Trucker Hat*/
    (1002263, 3000, 'Hats'), /*Green Trucker Hat*/
    (1002264, 3000, 'Hats'), /*Hardhat*/
    (1002265, 3000, 'Hats'), /*Elf's Ear*/
    (1002266, 3000, 'Hats'), /*Basic Earmuff*/
    (1002279, 3000, 'Hats'), /*Bunny Hat*/
    (1002280, 3000, 'Hats'), /*Ducky Hat*/
    (1002290, 3000, 'Hats'), /*Camouflaged Helmet*/
    (1002291, 3000, 'Hats'), /*Starred Hunting Hat*/
    (1002292, 3000, 'Hats'), /*Pink Frill Pajama Hat*/
    (1002293, 3000, 'Hats'), /*Blue Pajama Hat*/
    (1002294, 3000, 'Hats'), /*Red Frill Pajama Hat*/
    (1002295, 3000, 'Hats'), /*Chef's Hat*/
    (1002296, 3000, 'Hats'), /*Slime Hat*/
    (1002297, 3000, 'Hats'), /*Brown Bucket Hat*/
    (1002298, 3000, 'Hats'), /*Blue Bucket Hat*/
    (1002299, 3000, 'Hats'), /*Cubic Newsie Hat*/
    (1002300, 3000, 'Hats'), /*Green Picnic Hat*/
    (1002301, 3000, 'Hats'), /*Yellow Picnic Hat*/
    (1002302, 3000, 'Hats'), /*Pink Picnic Hat*/
    (1002303, 3000, 'Hats'), /*Blue Picnic Hat*/
    (1002304, 3000, 'Hats'), /*Silver Chain Hat*/
    (1002305, 3000, 'Hats'), /*Blue Headband*/
    (1002306, 3000, 'Hats'), /*Brown Headband*/
    (1002307, 3000, 'Hats'), /*Blue B-Ball Headband*/
    (1002308, 3000, 'Hats'), /*Orange B-Ball Headband*/
    (1002309, 3000, 'Hats'), /*Watermelon Hat*/
    (1002310, 3000, 'Hats'), /*Flower Crown*/
    (1002311, 3000, 'Hats'), /*Traveler's Hat*/
    (1002312, 3000, 'Hats'), /*Evil Watermelon Hat*/
    (1002313, 3000, 'Hats'), /*Palm Tree Hat*/
    (1002315, 3000, 'Hats'), /*Red Straw Hat*/
    (1002316, 3000, 'Hats'), /*Blue Straw Hat*/
    (1002317, 3000, 'Hats'), /*Grey Headband*/
    (1002318, 3000, 'Hats'), /*Red Headband*/
    (1002319, 3000, 'Hats'), /*Whale Hat*/
    (1002320, 3000, 'Hats'), /*Fuji Hat*/
    (1002321, 3000, 'Hats'), /*Crow Hat*/
    (1002322, 3000, 'Hats'), /*Lobster Hat*/
    (1002331, 3000, 'Hats'), /*Wind Goblin*/
    (1002332, 3000, 'Hats'), /*Cloud Goblin*/
    (1002333, 3000, 'Hats'), /*Big Halo*/
    (1002334, 3000, 'Hats'), /*Raccoon Hat*/
    (1002335, 3000, 'Hats'), /*Triangular Hat*/
    (1002336, 3000, 'Hats'), /*Noble Moca*/
    (1002337, 3000, 'Hats'), /*Laurel Crown*/
    (1002341, 3000, 'Hats'), /*Starry Olive Beanie*/
    (1002342, 3000, 'Hats'), /*Olive Beanie*/
    (1002343, 3000, 'Hats'), /*White Beanie*/
    (1002344, 3000, 'Hats'), /*Woodsman Hat*/
    (1002345, 3000, 'Hats'), /*Party Hat*/
    (1002346, 3000, 'Hats'), /*Blue Corporal Hat*/
    (1002347, 3000, 'Hats'), /*Brown Corporal Hat*/
    (1002348, 3000, 'Hats'), /*Bamboo Hat*/
    (1002349, 3000, 'Hats'), /*Black Cowboy Hat*/
    (1002351, 3000, 'Hats'), /*Yellow Cowboy Hat*/
    (1002352, 3000, 'Hats'), /*Red Knitted Hat*/
    (1002353, 3000, 'Hats'), /*Purple Knitted Hat*/
    (1002354, 3000, 'Hats'), /*Yellow Knitted Hat*/
    (1002355, 3000, 'Hats'), /*Blue Kitty Beanie*/
    (1002356, 3000, 'Hats'), /*Yellow Kitty Beanie*/
    (1002358, 3000, 'Hats'), /*Green Knitted Gumball*/
    (1002359, 3000, 'Hats'), /*Blue Knitted Gumball*/
    (1002360, 3000, 'Hats'), /*Pink Knitted Gumball*/
    (1002361, 3000, 'Hats'), /*Red Festive Gumball*/
    (1002362, 3000, 'Hats'), /*White Festive Gumball*/
    (1002367, 3000, 'Hats'), /*Angel Halo*/
    (1002368, 3000, 'Hats'), /*Reindeer Hat*/
    (1002369, 3000, 'Hats'), /*Antenna Hairband*/
    (1002370, 3000, 'Hats'), /*Black-Striped Feathered Bandana*/
    (1002371, 3000, 'Hats'), /*Red-Dotted Feathered Bandana*/
    (1002372, 3000, 'Hats'), /*Feathered Bandana with Hearts*/
    (1002373, 3000, 'Hats'), /*Cloth Wrapper*/
    (1002374, 3000, 'Hats'), /*Red Beret*/
    (1002375, 3000, 'Hats'), /*Yellow Beret*/
    (1002376, 3000, 'Hats'), /*Pink Beret*/
    (1002384, 3000, 'Hats'), /*Casual Cowboy Hat*/
    (1002385, 3000, 'Hats'), /*Red Eskimo Hat*/
    (1002386, 3000, 'Hats'), /*Brown Eskimo Hat*/
    (1002387, 3000, 'Hats'), /*Green Eskimo Hat*/
    (1002388, 3000, 'Hats'), /*Peter Pan Hat*/
    (1002389, 3000, 'Hats'), /*Devilish Horns*/
    (1002396, 3000, 'Hats'), /*Hawaiian Flower*/
    (1002397, 3000, 'Hats'), /*Sunflower Petal*/
    (1002409, 3000, 'Hats'), /*Tin Bucket*/
    (1002410, 3000, 'Hats'), /*Pink Turban*/
    (1002411, 3000, 'Hats'), /*Yellow Turban*/
    (1002412, 3000, 'Hats'), /*Skyblue Turban*/
    (1002413, 3000, 'Hats'), /*Octopus Hat*/
    (1002417, 3000, 'Hats'), /*Drake Hat*/
    (1002420, 3000, 'Hats'), /*Biker Bandana*/
    (1002421, 3000, 'Hats'), /*Pink Knitted Beanie*/
    (1002422, 3000, 'Hats'), /*Blue Knitted Beanie*/
    (1002423, 3000, 'Hats'), /*Yellow Knitted Beanie*/
    (1002426, 3000, 'Hats'), /*Beige Goya Beret*/
    (1002427, 3000, 'Hats'), /*Green Goya Beret*/
    (1002428, 3000, 'Hats'), /*Beige Checkered Hat*/
    (1002429, 3000, 'Hats'), /*Meshcap*/
    (1002431, 3000, 'Hats'), /*Bull's Horn*/
    (1002432, 3000, 'Hats'), /*Spring Hat*/
    (1002433, 3000, 'Hats'), /*Summer Hat*/
    (1002434, 3000, 'Hats'), /*Autumn Hat*/
    (1002435, 3000, 'Hats'), /*Korean Flower Petal*/
    (1002437, 3000, 'Hats'), /*Guan Yu Headpiece*/
    (1002438, 3000, 'Hats'), /*Zhu-Ge-Liang Hat*/
    (1002439, 3000, 'Hats'), /*Blue Jelly Cap*/
    (1002440, 3000, 'Hats'), /*Pink Jelly Cap*/
    (1002442, 3000, 'Hats'), /*Rainbow Afro Wig*/
    (1002443, 3000, 'Hats'), /*Patissier Hat*/
    (1002444, 3000, 'Hats'), /*Liu Bei Headpiece*/
    (1002445, 3000, 'Hats'), /*Cao Cao Headpiece*/
    (1002446, 3000, 'Hats'), /*Sun Quan Headpiece*/
    (1002447, 3000, 'Hats'), /*Rolled Towel*/
    (1002449, 3000, 'Hats'), /*Winged Cap*/
    (1002450, 3000, 'Hats'), /*Conch Cap*/
    (1002451, 3000, 'Hats'), /*Starfish*/
    (1002456, 3000, 'Hats'), /*Horoscope Hat (Aquarius)*/
    (1002457, 3000, 'Hats'), /*Horoscope Hat (Pisces)*/
    (1002458, 3000, 'Hats'), /*Horoscope Hat (Aries)*/
    (1002459, 3000, 'Hats'), /*Horoscope Hat (Taurus)*/
    (1002460, 3000, 'Hats'), /*Horoscope Hat (Gemini)*/
    (1002461, 3000, 'Hats'), /*Horoscope Hat (Cancer)*/
    (1002462, 3000, 'Hats'), /*Horoscope Hat (Leo)*/
    (1002463, 3000, 'Hats'), /*Horoscope Hat (Virgo)*/
    (1002464, 3000, 'Hats'), /*Horoscope Hat (Libra)*/
    (1002465, 3000, 'Hats'), /*Horoscope Hat (Scorpius)*/
    (1002466, 3000, 'Hats'), /*Horoscope Hat (Sagittarius)*/
    (1002467, 3000, 'Hats'), /*Horoscope Hat (Capricorn)*/
    (1002468, 3000, 'Hats'), /*Golden Bulldog Hat*/
    (1002469, 3000, 'Hats'), /*Jester Hat*/
    (1002470, 3000, 'Hats'), /*Welding Mask*/
    (1002472, 3000, 'Hats'), /*Cabbage Patch Hat*/
    (1002476, 3000, 'Hats'), /*Rough Hat*/
    (1002477, 3000, 'Hats'), /*Slime Hair Pin*/
    (1002478, 3000, 'Hats'), /*Mushroom Hair Pin*/
    (1002479, 3000, 'Hats'), /*Snowman Mask*/
    (1002480, 3000, 'Hats'), /*White Wig Hat*/
    (1002481, 3000, 'Hats'), /*Black Snowboard Helmet*/
    (1002482, 3000, 'Hats'), /*Red Snowboard Helmet*/
    (1002484, 3000, 'Hats'), /*Polar Bear Hat*/
    (1002485, 3000, 'Hats'), /*Grey Visor Beanie*/
    (1002486, 3000, 'Hats'), /*Green Visor Beanie*/
    (1002487, 3000, 'Hats'), /*Rainbow Visor Beanie*/
    (1002488, 3000, 'Hats'), /*Military Fur Hat*/
    (1002489, 3000, 'Hats'), /*Football Helmet (Home)*/
    (1002490, 3000, 'Hats'), /*Football Helmet (Away)*/
    (1002491, 3000, 'Hats'), /*Musashi Hat*/
    (1002493, 3000, 'Hats'), /*Teddy Bear Headgear*/
    (1002495, 3000, 'Hats'), /*Angora Hat*/
    (1002496, 3000, 'Hats'), /*Black Skull Bandana*/
    (1002497, 3000, 'Hats'), /*Hunting Cap*/
    (1002498, 3000, 'Hats'), /*Bald Wig*/
    (1002499, 3000, 'Hats'), /*White Tiger Hat*/
    (1002500, 3000, 'Hats'), /*Korean Flag Bandana*/
    (1002501, 3000, 'Hats'), /*Reggae Hat*/
    (1002502, 3000, 'Hats'), /*Vintage Denim Hat*/
    (1002503, 3000, 'Hats'), /*Vintage Pink Hat*/
    (1002504, 3000, 'Hats'), /*Old Fisherman Hat*/
    (1002505, 3000, 'Hats'), /*Sergeant Hat*/
    (1002507, 3000, 'Hats'), /*Soccer Ball Hat*/
    (1002512, 3000, 'Hats'), /*Red Spirit Bandana*/
    (1002513, 3000, 'Hats'), /*Maple Party Hat*/
    (1002519, 3000, 'Hats'), /*White Felt Hat*/
    (1002521, 3000, 'Hats'), /*White Hairband*/
    (1002522, 3000, 'Hats'), /*Pink-Dotted Hairband*/
    (1002523, 3000, 'Hats'), /*Paper Boat Hat*/
    (1002524, 3000, 'Hats'), /*Coke Hat*/
    (1002525, 3000, 'Hats'), /*Mummy Hat*/
    (1002526, 3000, 'Hats'), /*Skull Hat*/
    (1002534, 3000, 'Hats'), /*White Puppy Hat*/
    (1002536, 3000, 'Hats'), /*Brown Paperbag Mask*/
    (1002542, 3000, 'Hats'), /*Acorn Headgear*/
    (1002543, 3000, 'Hats'), /*Acorn Helmet*/
    (1002544, 3000, 'Hats'), /*Pumpkin Headgear*/
    (1002545, 3000, 'Hats'), /*Yellow Slime Hat*/
    (1002548, 3000, 'Hats'), /*White Rabbit Hat*/
    (1002549, 3000, 'Hats'), /*Black Cat Hat*/
    (1002552, 3000, 'Hats'), /*Moon Bunny Headgear*/
    (1002555, 3000, 'Hats'), /*Demon Goblin*/
    (1002556, 3000, 'Hats'), /*Maple-Stein Head*/
    (1002557, 3000, 'Hats'), /*Jr. Lioner Hat*/
    (1002558, 3000, 'Hats'), /*Werebeast*/
    (1002559, 3000, 'Hats'), /*Nordic Knitted Beanie*/
    (1002560, 3000, 'Hats'), /*Striped Knitted Beanie*/
    (1002565, 3000, 'Hats'), /*Fur Hat*/
    (1002566, 3000, 'Hats'), /*Skull Beanie*/
    (1002567, 3000, 'Hats'), /*Elf Hat*/
    (1002568, 3000, 'Hats'), /*Tweed Headband*/
    (1002569, 3000, 'Hats'), /*Candlelight hat*/
    (1002570, 3000, 'Hats'), /*Pastel Cap*/
    (1002575, 3000, 'Hats'), /*Angel Headband*/
    (1002576, 3000, 'Hats'), /*Fallen Angel Headband*/
    (1002582, 3000, 'Hats'), /*Maximus Galea*/
    (1002583, 3000, 'Hats'), /*Wrestling Mask*/
    (1002590, 3000, 'Hats'), /*Star Baseball Cap*/
    (1002591, 3000, 'Hats'), /*Leatty Hat*/
    (1002592, 3000, 'Hats'), /*Sun Wu Kong Hat*/
    (1002593, 3000, 'Hats'), /*Smiley Headgear*/
    (1002594, 3000, 'Hats'), /*Goggled Smiley Headgear*/
    (1002596, 3000, 'Hats'), /*Bulldog Cap*/
    (1002597, 3000, 'Hats'), /*Husky Hat*/
    (1002598, 3000, 'Hats'), /*Rabbit Ear*/
    (1002599, 3000, 'Hats'), /*Golden Trench Helmet*/
    (1002605, 3000, 'Hats'), /*Jet Black Head Scarf*/
    (1002607, 3000, 'Hats'), /*Zhu Ba Jie Hat*/
    (1002608, 3000, 'Hats'), /*Superstar Cap*/
    (1002609, 3000, 'Hats'), /*Crazy Bunny Hat*/
    (1002650, 3000, 'Hats'), /*Vintage Grey Cap*/
    (1002653, 3000, 'Hats'), /*Stack of Books*/
    (1002660, 3000, 'Hats'), /*Orange Cap with Shades*/
    (1002661, 3000, 'Hats'), /*Bird Nest*/
    (1002665, 3000, 'Hats'), /*Tomato Hat*/
    (1002666, 3000, 'Hats'), /*White Basic Cap*/
    (1002667, 3000, 'Hats'), /*Star Hair Pin*/
    (1002672, 3000, 'Hats'), /*Helm of the Golden Monk*/
    (1002673, 3000, 'Hats'), /*Helm of the Silver Monk*/
    (1002674, 3000, 'Hats'), /*Helm of the Bronze Monk*/
    (1002678, 3000, 'Hats'), /*Old Hockey Mask*/
    (1002679, 3000, 'Hats'), /*Eye Poppers*/
    (1002691, 3000, 'Hats'), /*Centaurus Horns*/
    (1002692, 3000, 'Hats'), /*Centaurus Horns (Ghost)*/
    (1002693, 3000, 'Hats'), /*Centaurus Horns (Green)*/
    (1002694, 3000, 'Hats'), /*Centaurus Horns (Light)*/
    (1002695, 3000, 'Hats'), /*Soul Teddy Hat*/
    (1002696, 3000, 'Hats'), /*Stoplight Hat*/
    (1002697, 3000, 'Hats'), /*Devilfish Headgear*/
    (1002698, 3000, 'Hats'), /*Vintage Khaki Cap*/
    (1002700, 3000, 'Hats'), /*Big Green Eye*/
    (1002701, 3000, 'Hats'), /*Huge Green Lips*/
    (1002703, 3000, 'Hats'), /*Big Blue Eye - Blue Skin*/
    (1002704, 3000, 'Hats'), /*Big Blue Eye - Normal Skin*/
    (1002705, 3000, 'Hats'), /*Huge Blue Lips*/
    (1002706, 3000, 'Hats'), /*Huge Red Lips*/
    (1002708, 3000, 'Hats'), /*Red Vintage Bandana*/
    (1002709, 3000, 'Hats'), /*Snowy Knitted Hat*/
    (1002710, 3000, 'Hats'), /*Pink Kitty Hat*/
    (1002711, 3000, 'Hats'), /*White Kitty Ears*/
    (1002712, 3000, 'Hats'), /*Black Kitty Ears*/
    (1002713, 3000, 'Hats'), /*Black Bubble Beanie*/
    (1002714, 3000, 'Hats'), /*Christmas Tree Hat*/
    (1002715, 3000, 'Hats'), /*Military Beanie*/
    (1002720, 3000, 'Hats'), /*Lovely Christmas*/
    (1002721, 3000, 'Hats'), /*Raccoon Earmuffs*/
    (1002722, 3000, 'Hats'), /*Teddy Earmuffs*/
    (1002725, 3000, 'Hats'), /*Pierced Apple*/
    (1002726, 3000, 'Hats'), /*Umbrella Hat*/
    (1002727, 3000, 'Hats'), /*Huge Pink Ribbon*/
    (1002734, 3000, 'Hats'), /*Chinese Lion Headgear*/
    (1002735, 3000, 'Hats'), /*Glowy Smile Cap*/
    (1002736, 3000, 'Hats'), /*Glowy Patterned Cap*/
    (1002738, 3000, 'Hats'), /*Bunny Earmuffs*/
    (1002741, 3000, 'Hats'), /*Yellow Baby Dragon Hat*/
    (1002742, 3000, 'Hats'), /*Baby Turkey Hat*/
    (1002745, 3000, 'Hats'), /*Baby Gold Dragon*/
    (1002746, 3000, 'Hats'), /*Sleepy Turkey*/
    (1002747, 3000, 'Hats'), /*Superstar Headphones*/
    (1002748, 3000, 'Hats'), /*Apple-Green Hood*/
    (1002752, 3000, 'Hats'), /*Celestial Crown*/
    (1002753, 3000, 'Hats'), /*Stylish Pink Cotton Cap*/
    (1002754, 3000, 'Hats'), /*Orange Mushroom Scholar*/
    (1002755, 3000, 'Hats'), /*Hero's Beret*/
    (1002756, 3000, 'Hats'), /*Hero's Casket*/
    (1002759, 3000, 'Hats'), /*Maple Hood Hat*/
    (1002760, 3000, 'Hats'), /*Globe Cap*/
    (1002761, 3000, 'Hats'), /*Maple Leaf eye mask*/
    (1002770, 3000, 'Hats'), /*Cone Ears*/
    (1002771, 3000, 'Hats'), /*Tiger Cub Hat*/
    (1002774, 3000, 'Hats'), /*Victory Hairpin*/
    (1002775, 3000, 'Hats'), /*3rd Anniversary Hat*/
    (1002784, 3000, 'Hats'), /*"A" Cap*/
    (1002785, 3000, 'Hats'), /*Prismatic Sun Cap*/
    (1002796, 3000, 'Hats'), /*Cutie Birk Hat*/
    (1002803, 3000, 'Hats'), /*Mrs. Octopus*/
    (1002804, 3000, 'Hats'), /*Brown Felt Hat*/
    (1002811, 3000, 'Hats'), /*Striped Bandana*/
    (1002820, 3000, 'Hats'), /*Inferno Horns*/
    (1002821, 3000, 'Hats'), /*Violet Heart Beanie*/
    (1002823, 3000, 'Hats'), /*Scarface Mask*/
    (1002824, 3000, 'Hats'), /*Noob Hat*/
    (1002831, 3000, 'Hats'), /*Leo Hairpin*/
    (1002834, 3000, 'Hats'), /*Scorpius Hairpin*/
    (1002835, 3000, 'Hats'), /*Sagittarius Hair Clip*/
    (1002836, 3000, 'Hats'), /*Capricorn Hair Clip*/
    (1002837, 3000, 'Hats'), /*Tengu Mask*/
    (1002839, 3000, 'Hats'), /*Pumpkin Hat*/
    (1002840, 3000, 'Hats'), /*Hatched Bird Cap*/
    (1002842, 3000, 'Hats'), /*Golden Fox Ears*/
    (1002843, 3000, 'Hats'), /*Silver Fox Ears*/
    (1002844, 3000, 'Hats'), /*Chipmunk Ears*/
    (1002845, 3000, 'Hats'), /*Pink Bunny Cap*/
    (1002846, 3000, 'Hats'), /*Blue Bow Beret*/
    (1002847, 3000, 'Hats'), /*Frog Hat*/
    (1002849, 3000, 'Hats'), /*Panda Hat*/
    (1002863, 3000, 'Hats'), /*Bear Tassel Hat*/
    (1002870, 3000, 'Hats'), /*Moon Bunny Hat*/
    (1002876, 3000, 'Hats'), /*Holly Hair Clip*/
    (1002877, 3000, 'Hats'), /*Cow Mask*/
    (1002878, 3000, 'Hats'), /*Snow Flake Hat*/
    (1002882, 3000, 'Hats'), /*Owl Hat*/
    (1002884, 3000, 'Hats'), /*Red Panda Cap*/
    (1002885, 3000, 'Hats'), /*Pink Bow*/
    (1002886, 3000, 'Hats'), /*Strawberry Hairband*/
    (1002887, 3000, 'Hats'), /*Pink Ribbon Hairband*/
    (1002888, 3000, 'Hats'), /*Red Ribbon Hairband*/
    (1002889, 3000, 'Hats'), /*Purple Ribbon Hairband*/
    (1002890, 3000, 'Hats'), /*Blue Ribbon Hairband*/
    (1002891, 3000, 'Hats'), /*Green Ribbon Hairband*/
    (1002903, 3000, 'Hats'), /*Pink Bandana*/
    (1002907, 3000, 'Hats'), /*Checkered Fedora*/
    (1002912, 3000, 'Hats'), /*Iljimae Mask*/
    (1002913, 3000, 'Hats'), /*Miranda Ribbon*/
    (1002919, 3000, 'Hats'), /*Courageous Little Lamb Hat*/
    (1002920, 3000, 'Hats'), /*Pink Mini Hat*/
    (1002921, 3000, 'Hats'), /*Blue Mini Hat*/
    (1002922, 3000, 'Hats'), /*Navy Hoodie Cap*/
    (1002923, 3000, 'Hats'), /*Treacherous Wolf Hat*/
    (1002928, 3000, 'Hats'), /*Pink Star Beanie*/
    (1002929, 3000, 'Hats'), /*Colorful Striped Beanie*/
    (1002930, 3000, 'Hats'), /*6th Anniversary Cone Hat*/
    (1002937, 3000, 'Hats'), /*Felt Hat*/
    (1002941, 3000, 'Hats'), /*Moon Bloom Hair Pin*/
    (1002942, 3000, 'Hats'), /*Green Mushroom Hat*/
    (1002943, 3000, 'Hats'), /*Sailor Hat*/
    (1002944, 3000, 'Hats'), /*Honey Bee Hat*/
    (1002945, 3000, 'Hats'), /*Heart Hairband*/
    (1002950, 3000, 'Hats'), /*Pink Flower Headwrap*/
    (1002951, 3000, 'Hats'), /*Yellow Flower Headwrap*/
    (1002952, 3000, 'Hats'), /*Purple Flower Headwrap*/
    (1002953, 3000, 'Hats'), /*Fluttering Sunhat*/
    (1002954, 3000, 'Hats'), /*Aran Helmet*/
    (1002955, 3000, 'Hats'), /*Brave Musashi Helmet*/
    (1002957, 3000, 'Hats'), /*Pink Bean Hat*/
    (1002960, 3000, 'Hats'), /*Black Crown*/
    (1002961, 3000, 'Hats'), /*Gray Mask*/
    (1002962, 3000, 'Hats'), /*Peony Flower Accessory*/
    (1002967, 3000, 'Hats'), /*Teru Teru Hairband*/
    (1002968, 3000, 'Hats'), /*Pancake Hat*/
    (1002969, 3000, 'Hats'), /*Brown Puppy Ears*/
    (1002973, 3000, 'Hats'), /*Mysterious Mask*/
    (1002974, 3000, 'Hats'), /*Jr. Lucida Hat*/
    (1002975, 3000, 'Hats'), /*Aviator Pilot Goggles*/
    (1002976, 3000, 'Hats'), /*Maid Headband*/
    (1002978, 3000, 'Hats'), /*Cute Mouse Ears*/
    (1002979, 3000, 'Hats'), /*Marbum Headgear*/
    (1002983, 3000, 'Hats'), /*We Care! Hat*/
    (1002984, 3000, 'Hats'), /*Spiegelmann's Hat*/
    (1002985, 3000, 'Hats'), /*Pachinko Marble-box Hat*/
    (1002987, 3000, 'Hats'), /*Cursed Golden trench helmet*/
    (1002995, 3000, 'Hats'), /*Royal Navy Hat*/
    (1002998, 3000, 'Hats'), /*Edwin Wig*/
    (1002999, 3000, 'Hats'), /*Fire Shadow Hair*/
    (1003000, 3000, 'Hats'), /*Cherry Blossom Hair*/
    (1003001, 3000, 'Hats'), /*Chaos Metallic Helmet*/
    (1003005, 3000, 'Hats'), /*Maple Racing Helmet*/
    (1003006, 3000, 'Hats'), /*Kitty Camping Hat*/
    (1003008, 3000, 'Hats'), /*Pharaoh Crown*/
    (1003009, 3000, 'Hats'), /*Christmas Light Hairband*/
    (1003010, 3000, 'Hats'), /*Dancing Blue Butterfly*/
    (1003013, 3000, 'Hats'), /*Red Loose-Fit Beanie*/
    (1003014, 3000, 'Hats'), /*Pink Scooter Helmet*/
    (1003015, 3000, 'Hats'), /*Blue Scooter Helmet*/
    (1003022, 3000, 'Hats'), /*Devil Horns*/
    (1003029, 3000, 'Hats'), /*Former Hero Female Face*/
    (1003030, 3000, 'Hats'), /*Former Hero Male Face*/
    (1003038, 3000, 'Hats'), /*Doll Face Hat*/
    (1003044, 3000, 'Hats'), /*Clown Hat*/
    (1003047, 3000, 'Hats'), /*Bear Hat*/
    (1003048, 3000, 'Hats'), /*Christmas Wreath*/
    (1003049, 3000, 'Hats'), /*Giant Bear Cap*/
    (1003050, 3000, 'Hats'), /*Bunny Ears*/
    (1003051, 3000, 'Hats'), /*Desert Fox*/
    (1003052, 3000, 'Hats'), /*Tilted Fedora*/
    (1003053, 3000, 'Hats'), /*Pink Fur Hat*/
    (1003054, 3000, 'Hats'), /*White Fur Hat*/
    (1003057, 3000, 'Hats'), /*Mini Crown*/
    (1003058, 3000, 'Hats'), /*Christmas Hairpin*/
    (1003059, 3000, 'Hats'), /*Qi-pao Hair*/
    (1003060, 3000, 'Hats'), /*Silver Coronet*/
    (1003070, 3000, 'Hats'), /*Tiger-Print Cap*/
    (1003071, 3000, 'Hats'), /*Pinky Bow*/
    (1003072, 3000, 'Hats'), /*Black-Lace Ribbon*/
    (1003074, 3000, 'Hats'), /*Strawberry Hat*/
    (1003077, 3000, 'Hats'), /*Knitted Corsage Hat*/
    (1003078, 3000, 'Hats'), /*Sparkling Butterfly*/
    (1003079, 3000, 'Hats'), /*Green Leaf Hat*/
    (1003080, 3000, 'Hats'), /*Cat Set Hat*/
    (1003082, 3000, 'Hats'), /*Wolf Hat*/
    (1003083, 3000, 'Hats'), /*Sprout Hat*/
    (1003084, 3000, 'Hats'), /*Royal Crown*/
    (1003089, 3000, 'Hats'), /*Evan Wing Headband*/
    (1003092, 3000, 'Hats'), /*Hawkeye Captain Hat*/
    (1003101, 3000, 'Hats'), /*Dunas Hat*/
    (1003103, 3000, 'Hats'), /*6th Anniversary Top Hat*/
    (1003109, 3000, 'Hats'), /*Royal Rainbow Hood*/
    (1003120, 3000, 'Hats'), /*Oz Magic Hat*/
    (1003121, 3000, 'Hats'), /*Evan Headband*/
    (1003122, 3000, 'Hats'), /*Yellow Petite Scarf*/
    (1003123, 3000, 'Hats'), /*Black Petite Scarf*/
    (1003130, 3000, 'Hats'), /*Shining Feather*/
    (1003131, 3000, 'Hats'), /*Black Dressy Ribbon*/
    (1003132, 3000, 'Hats'), /*Red Bow*/
    (1003133, 3000, 'Hats'), /*White Bow*/
    (1003135, 3000, 'Hats'), /*Wild Hunter's Hat*/
    (1003136, 3000, 'Hats'), /*Battle Mage Goggles*/
    (1003141, 3000, 'Hats'), /*Straw Sun Hat*/
    (1003144, 3000, 'Hats'), /*King Crow Hat*/
    (1003145, 3000, 'Hats'), /*Dragon Hat*/
    (1003146, 3000, 'Hats'), /*Lace Ribbon (Pink)*/
    (1003147, 3000, 'Hats'), /*Maid Headband (Blue)*/
    (1003148, 3000, 'Hats'), /*Pilot Cap*/
    (1003149, 3000, 'Hats'), /*Rabbit Ear Hood*/
    (1003161, 3000, 'Hats'), /*Sanctus Combat Veil*/
    (1003163, 3000, 'Hats'), /*Brown Hunting Cap*/
    (1003170, 3000, 'Hats'), /*Star Head Wrap*/
    (1003171, 3000, 'Hats'), /*Leather Cap*/
    (1003182, 3000, 'Hats'), /*Paypal Cap*/
    (1003185, 3000, 'Hats'), /*Rabbit hat*/
    (1003186, 3000, 'Hats'), /*Cat Hood (Pink)*/
    (1003187, 3000, 'Hats'), /*Gray Cat Hood*/
    (1003192, 3000, 'Hats'), /*Blue Pre-School Uniform Hat*/
    (1003193, 3000, 'Hats'), /*Red Pre-School Uniform Hat*/
    (1003194, 3000, 'Hats'), /*Rookie Bobble Heart Band*/
    (1003196, 3000, 'Hats'), /*Rudolph Santa Hat*/
    (1003202, 3000, 'Hats'), /*Golden Beetle*/
    (1003203, 3000, 'Hats'), /*Red Riding Hood*/
    (1003204, 3000, 'Hats'), /*Courageous Bunny Hat*/
    (1003207, 3000, 'Hats'), /*Curly Rabbit Poof*/
    (1003208, 3000, 'Hats'), /*Magic Hat*/
    (1003210, 3000, 'Hats'), /*Earmuffs and Pom Pom Beanie*/
    (1003211, 3000, 'Hats'), /*Winter 2010 Moon Bunny Hat*/
    (1003214, 3000, 'Hats'), /*Blue Snowdrop Cunning Hat*/
    (1003215, 3000, 'Hats'), /*Pink Snowdrop Cunning Hat*/
    (1003216, 3000, 'Hats'), /*Pirate Captain's Hat*/
    (1003217, 3000, 'Hats'), /*Flower Heir Cap*/
    (1003218, 3000, 'Hats'), /*Flower Heiress Band*/
    (1003220, 3000, 'Hats'), /*Knit Flower Hairband*/
    (1003221, 3000, 'Hats'), /*Pink Polka Dot Bow*/
    (1003222, 3000, 'Hats'), /*Blue Polka Dot Bow*/
    (1003223, 3000, 'Hats'), /*Lost Baby Chick*/
    (1003226, 3000, 'Hats'), /*Rookie Hatchling Hat*/
    (1003232, 3000, 'Hats'), /*Pretty Teddy*/
    (1003233, 3000, 'Hats'), /*Honey Rabbit */
    (1003234, 3000, 'Hats'), /*Pink Jeweled Chaplain Hat*/
    (1003235, 3000, 'Hats'), /*Blue Jeweled Chaplain Hat*/
    (1003237, 3000, 'Hats'), /*Lion Head*/
    (1003238, 3000, 'Hats'), /*Gray Puppy Ears*/
    (1003239, 3000, 'Hats'), /*Raspberry Candy Hoodie*/
    (1003240, 3000, 'Hats'), /*Blueberry Candy Hoodie*/
    (1003241, 3000, 'Hats'), /*6th Anniversary Party Hat*/
    (1003247, 3000, 'Hats'), /*Mad Hatter's Hat*/
    (1003249, 3000, 'Hats'), /*Topaz Musical Note*/
    (1003250, 3000, 'Hats'), /*Ruby Musical Note*/
    (1003251, 3000, 'Hats'), /*Citrine Musical Note*/
    (1003252, 3000, 'Hats'), /*Amethyst Musical Note*/
    (1003253, 3000, 'Hats'), /*Amber Musical Note*/
    (1003254, 3000, 'Hats'), /*Sapphire Musical Note*/
    (1003255, 3000, 'Hats'), /*Quartz Musical Note*/
    (1003256, 3000, 'Hats'), /*Emerald Musical Note*/
    (1003263, 3000, 'Hats'), /*MSE 4 Years & Unstoppable Hat*/
    (1003264, 3000, 'Hats'), /*Rose Tinia Shades*/
    (1003265, 3000, 'Hats'), /*Marine Tinia Shades*/
    (1003268, 3000, 'Hats'), /*Button-a-holic Sugar Cap*/
    (1003269, 3000, 'Hats'), /*Button-a-holic Toy Cap*/
    (1003271, 3000, 'Hats'), /*Pink Heart Transparent Hat*/
    (1003272, 3000, 'Hats'), /*Bastille Hat*/
    (1003276, 3000, 'Hats'), /*Blue Heart Transparent Hat*/
    (1003277, 3000, 'Hats'), /*Grass Spirit*/
    (1003278, 3000, 'Hats'), /*Mermaid Shell*/
    (1003279, 3000, 'Hats'), /*Chain Crusher Cap*/
    (1003295, 3000, 'Hats'), /*Lazy Chicken Headband*/
    (1003352, 3000, 'Hats'), /*Tic Toc Red Sun Cap*/
    (1003353, 3000, 'Hats'), /*Dear Orange Sun Cap*/
    (1003354, 3000, 'Hats'), /*Fresh Lemon Sun Cap*/
    (1003355, 3000, 'Hats'), /*Lime Green Sun Cap*/
    (1003356, 3000, 'Hats'), /*Crystal Blue Sun Cap*/
    (1003357, 3000, 'Hats'), /*Night Navy Sun Cap*/
    (1003358, 3000, 'Hats'), /*Sweet Purple Sun Cap*/
    (1003362, 3000, 'Hats'), /*Rosy Pink Twin Ribbons*/
    (1003367, 3000, 'Hats'), /*Crown of Flowers*/
    (1003368, 3000, 'Hats'), /*Western Cowboy Hat*/
    (1003376, 3000, 'Hats'), /*Memorial Angel*/
    (1003377, 3000, 'Hats'), /*Alchemist Hat*/
    (1003386, 3000, 'Hats'), /*Bat Costume Hood*/
    (1003387, 3000, 'Hats'), /*Beanie Headphone*/
    (1003390, 3000, 'Hats'), /*Orchid's Black Wing Hat*/
    (1003392, 3000, 'Hats'), /*Honeybee Antenna Hairband*/
    (1003393, 3000, 'Hats'), /*Imperial Duke Crown*/
    (1003398, 3000, 'Hats'), /*Dark Mihile*/
    (1003399, 3000, 'Hats'), /*Dark Oz*/
    (1003400, 3000, 'Hats'), /*Dark Irena*/
    (1003401, 3000, 'Hats'), /*Dark Eckhart*/
    (1003402, 3000, 'Hats'), /*Dark Hawkeye*/
    (1003403, 3000, 'Hats'), /*Dark Cygnus's Hairband*/
    (1003404, 3000, 'Hats'), /*Imp Hat*/
    (1003417, 3000, 'Hats'), /*Dino Cap*/
    (1003421, 3000, 'Hats'), /*Noblesse Gold Hood*/
    (1003422, 3000, 'Hats'), /*Garnet Raven Persona*/
    (1003459, 3000, 'Hats'), /*Lucia Hat*/
    (1003460, 3000, 'Hats'), /*Milk Chocolate Cone*/
    (1003461, 3000, 'Hats'), /*Lania's Flower Crown*/
    (1003462, 3000, 'Hats'), /*Kitty Cap*/
    (1003463, 3000, 'Hats'), /*Pixiemom Hat*/
    (1003482, 3000, 'Hats'), /*Green Zodiac Dragon Hat*/
    (1003483, 3000, 'Hats'), /*Pink Zodiac Dragon Hat*/
    (1003484, 3000, 'Hats'), /*White Zodiac Dragon Hat*/
    (1003485, 3000, 'Hats'), /*Green Zodiac Dragon Cake*/
    (1003486, 3000, 'Hats'), /*Yellow Zodiac Dragon Cake*/
    (1003487, 3000, 'Hats'), /*White Zodiac Dragon Cake*/
    (1003489, 3000, 'Hats'), /*Gas Mask and Helmet*/
    (1003490, 3000, 'Hats'), /*Maid Band*/
    (1003492, 3000, 'Hats'), /*Crisp Egg Nigiri*/
    (1003493, 3000, 'Hats'), /*Fresh Salmon Nigiri*/
    (1003494, 3000, 'Hats'), /*Chewy Octopus Nigiri*/
    (1003495, 3000, 'Hats'), /*Tangy Fish Egg Nigiri*/
    (1003496, 3000, 'Hats'), /*Cute Shrimp Nigiri*/
    (1003503, 3000, 'Hats'), /*Alice's Teacup*/
    (1003504, 3000, 'Hats'), /*Blue Dragon Horn*/
    (1003505, 3000, 'Hats'), /*Red Dragon Horn*/
    (1003506, 3000, 'Hats'), /*Intergalactic Hat*/
    (1003509, 3000, 'Hats'), /*Sausage Hat*/
    (1003510, 3000, 'Hats'), /*Alice Rabbit Hat*/
    (1003517, 3000, 'Hats'), /*Ebony Pimpernel Hat*/
    (1003518, 3000, 'Hats'), /*Small Black Devil Hat*/
    (1003519, 3000, 'Hats'), /*Sunset-colored Straw Hat*/
    (1003520, 3000, 'Hats'), /*Wire Headband*/
    (1003531, 3000, 'Hats'), /*Dainty Hat*/
    (1003532, 3000, 'Hats'), /*Lucky Hat*/
    (1003533, 3000, 'Hats'), /*Legendary Hat*/
    (1003538, 3000, 'Hats'), /*Button-A-Holic Toy Cap*/
    (1003539, 3000, 'Hats'), /*GM Nori's Wing Cap*/
    (1003541, 3000, 'Hats'), /*Country Rabbit Hat*/
    (1003542, 3000, 'Hats'), /*Strawberry Macaroon Hairpin*/
    (1003543, 3000, 'Hats'), /*Macaroon Hairpin*/
    (1003544, 3000, 'Hats'), /*Strawberry Cupcake Hairpin*/
    (1003545, 3000, 'Hats'), /*Melon Cupcake Hairpin*/
    (1003546, 3000, 'Hats'), /*Chocolate Cupcake Hairpin*/
    (1003547, 3000, 'Hats'), /*Parfait Cupcake Hairpin*/
    (1003548, 3000, 'Hats'), /*Aerial Mystic Black Silk Hat*/
    (1003549, 3000, 'Hats'), /*Aerial Mystic Black Silk Ribbon*/
    (1003559, 3000, 'Hats'), /*Blue Cat Hood*/
    (1003560, 3000, 'Hats'), /*Yellow Cat Hood*/
    (1003586, 3000, 'Hats'), /*Mint Star Marine Cap*/
    (1003587, 3000, 'Hats'), /*Pink Ribbon Marine Cap*/
    (1003588, 3000, 'Hats'), /*Pink Teddy Hat*/
    (1003594, 3000, 'Hats'), /*Cool Summer Snorkeling*/
    (1003596, 3000, 'Hats'), /*Metal Pink Baseball Cap*/
    (1003597, 3000, 'Hats'), /*Metal Crown Nuera*/
    (1003626, 3000, 'Hats'), /*Jett's Hat*/
    (1003636, 3000, 'Hats'), /*Aqua Shell*/
    (1003639, 3000, 'Hats'), /*Cheering Pink*/
    (1003640, 3000, 'Hats'), /*Cheering Blue*/
    (1003641, 3000, 'Hats'), /*Cheering Green*/
    (1003642, 3000, 'Hats'), /*Cheering Gold*/
    (1003643, 3000, 'Hats'), /*Yin-Yang Hairpin*/
    (1003654, 3000, 'Hats'), /*Yukimura's Helm*/
    (1003655, 3000, 'Hats'), /*Kanetsuku's Helm*/
    (1003656, 3000, 'Hats'), /*Hideyoshi's Helm*/
    (1003657, 3000, 'Hats'), /*Shingen's Helm*/
    (1003658, 3000, 'Hats'), /*Muneshige's Helm*/
    (1003666, 3000, 'Hats'), /*Blue Arabian Hat*/
    (1003667, 3000, 'Hats'), /*Red Arabian Hat*/
    (1003668, 3000, 'Hats'), /*Hyper Lost Baby Chick*/
    (1003669, 3000, 'Hats'), /*Hyper Honeybee Antenna Hairband*/
    (1003670, 3000, 'Hats'), /*Maple Red Beret*/
    (1003671, 3000, 'Hats'), /*Maple Blue Beret*/
    (1003672, 3000, 'Hats'), /*Maple Black Beret*/
    (1003673, 3000, 'Hats'), /*Maple Green Beret*/
    (1003682, 3000, 'Hats'), /*Zombuddy Hat*/
    (1003687, 3000, 'Hats'), /*Hyper Teddy Earmuffs*/
    (1003688, 3000, 'Hats'), /*Hyper Cat Hat*/
    (1003699, 3000, 'Hats'), /*Hidden Street Red Husky Hat*/
    (1003713, 3000, 'Hats'), /*Seal Hat*/
    (1003714, 3000, 'Hats'), /*Halloween Leopard Ears*/
    (1003727, 3000, 'Hats'), /*Red Pierre Hat*/
    (1003728, 3000, 'Hats'), /*Blue Pierre Hat*/
    (1003729, 3000, 'Hats'), /*Hyper Bunny Earmuffs*/
    (1003730, 3000, 'Hats'), /*Cat Lolita Hat*/
    (1003735, 3000, 'Hats'), /*Scarlion Boss Hat*/
    (1003737, 3000, 'Hats'), /*Snowman*/
    (1003739, 3000, 'Hats'), /*Decked Out Santa Hat*/
    (1003742, 3000, 'Hats'), /*Dark Devil Hat*/
    (1003743, 3000, 'Hats'), /*Slither Style Cap*/
    (1003749, 3000, 'Hats'), /*Zodiac Snake Cake*/
    (1003750, 3000, 'Hats'), /*Ribbon Kitty Ears*/
    (1003759, 3000, 'Hats'), /*Blue Point Kitty Hat*/
    (1003760, 3000, 'Hats'), /*Kitty Headphones*/
    (1003761, 3000, 'Hats'), /*Featherly Angel Hat*/
    (1003763, 3000, 'Hats'), /*Black Wing Master's Hat*/
    (1003775, 3000, 'Hats'), /*GM Hat*/
    (1003776, 3000, 'Hats'), /*Harp Seal Mask*/
    (1003777, 3000, 'Hats'), /*Goth Cat Hood*/
    (1003789, 3000, 'Hats'), /*Zombie Hunter Hat*/
    (1003790, 3000, 'Hats'), /*Visor*/
    (1003792, 3000, 'Hats'), /*Inkwell Hat*/
    (1003802, 3000, 'Hats'), /*Green Dinosaur Hat*/
    (1003803, 3000, 'Hats'), /*Purple Dinosaur Hat*/
    (1003807, 3000, 'Hats'), /*Heart Sunglasses*/
    (1003808, 3000, 'Hats'), /*Mystic Black Silk Hat*/
    (1003809, 3000, 'Hats'), /*Mystic Black Silk Ribbon*/
    (1003825, 3000, 'Hats'), /*The Bladed Falcon's Helm*/
    (1003829, 3000, 'Hats'), /*Bunny Top Hat*/
    (1003830, 3000, 'Hats'), /*Blue Love Bonnet*/
    (1003831, 3000, 'Hats'), /*Ramling Hair Pin*/
    (1003836, 3000, 'Hats'), /*Wild Spike Wig*/
    (1003837, 3000, 'Hats'), /*Colorstream Wig*/
    (1003838, 3000, 'Hats'), /*Wacky Olympus Wig*/
    (1003839, 3000, 'Hats'), /*Goin' Nuclear Wig*/
    (1003842, 3000, 'Hats'), /*Succubus Hat*/
    (1003843, 3000, 'Hats'), /*Bizarre Fox Mask*/
    (1003844, 3000, 'Hats'), /*Nao Hat*/
    (1003845, 3000, 'Hats'), /*Lorna and Pan Hat*/
    (1003846, 3000, 'Hats'), /*Danjin Hat*/
    (1003847, 3000, 'Hats'), /*Slayer Wig*/
    (1003853, 3000, 'Hats'), /*Blavy Angel Wing*/
    (1003855, 3000, 'Hats'), /*Leaf Hat*/
    (1003859, 3000, 'Hats'), /*Iris Psyche*/
    (1003860, 3000, 'Hats'), /*Seria Wig*/
    (1003861, 3000, 'Hats'), /*Funky Mini Crown*/
    (1003862, 3000, 'Hats'), /*Teddy Ribbon*/
    (1003865, 3000, 'Hats'), /*Starlight Wings*/
    (1003867, 3000, 'Hats'), /*Nice Shot Visor*/
    (1003873, 3000, 'Hats'), /*Water Thief Hat*/
    (1003874, 3000, 'Hats'), /*Blue Mossy Mom Hat*/
    (1003875, 3000, 'Hats'), /*Jr. Cellion Hat*/
    (1003876, 3000, 'Hats'), /*Lupin Hat*/
    (1003877, 3000, 'Hats'), /*Yeti Hat*/
    (1003878, 3000, 'Hats'), /*Pepe Hat*/
    (1003884, 3000, 'Hats'), /*Cute Wire Hair Band*/
    (1003889, 3000, 'Hats'), /*I'm Controlled!*/
    (1003890, 3000, 'Hats'), /*GM Sori's Fedora*/
    (1003892, 3000, 'Hats'), /*Leaf Diamond*/
    (1003897, 3000, 'Hats'), /*Indian Chief Hat*/
    (1003904, 3000, 'Hats'), /*Triumphant Ribbon Pig Hat*/
    (1003905, 3000, 'Hats'), /*Tenacious Ribbon Pig Hat*/
    (1003906, 3000, 'Hats'), /*Triumphant Zakum Hat*/
    (1003907, 3000, 'Hats'), /*Tenacious Zakum Helmet*/
    (1003909, 3000, 'Hats'), /*Pink Soda Cap*/
    (1003910, 3000, 'Hats'), /*Petite Diablo*/
    (1003912, 3000, 'Hats'), /*Puppy Ears*/
    (1003917, 3000, 'Hats'), /*Pink Sunglasses Hat*/
    (1003919, 3000, 'Hats'), /*Plait-Knitted Hat*/
    (1003920, 3000, 'Hats'), /*Hawaiian Sunhat*/
    (1003934, 3000, 'Hats'), /*Shadow Hood*/
    (1003935, 3000, 'Hats'), /*Anima Ears*/
    (1003936, 3000, 'Hats'), /*Azalea Hair Pin*/
    (1003937, 3000, 'Hats'), /*Romantic Bamboo Hat*/
    (1003941, 3000, 'Hats'), /*Pink Cheer*/
    (1003942, 3000, 'Hats'), /*Blue Cheer*/
    (1003945, 3000, 'Hats'), /*Superstar Crown*/
    (1003948, 3000, 'Hats'), /*フューチャーロイドヘッドセンサー*/
    (1003950, 3000, 'Hats'), /*Plump Bear Hood*/
    (1003952, 3000, 'Hats'), /*Odile Tiara*/
    (1003953, 3000, 'Hats'), /*Rhinne Luster*/
    (1003954, 3000, 'Hats'), /*Head Cooler*/
    (1003955, 3000, 'Hats'), /*Romance Rose*/
    (1003957, 3000, 'Hats'), /*Mint Mochi Ice*/
    (1003958, 3000, 'Hats'), /*Pink Mochi Ice*/
    (1003962, 3000, 'Hats'), /*Checkered Bonnet*/
    (1003963, 3000, 'Hats'), /*PSY Hat*/
    (1003964, 3000, 'Hats'), /*Star Checkered Cap*/
    (1003965, 3000, 'Hats'), /*Chicken Hataroo*/
    (1003966, 3000, 'Hats'), /*Camellia Hairpin*/
    (1003967, 3000, 'Hats'), /*Chocoram Doll Hat*/
    (1003968, 3000, 'Hats'), /*Puffram Hat*/
    (1003971, 3000, 'Hats'), /*Powder Fedora*/
    (1003972, 3000, 'Hats'), /*Powder Lace Band*/
    (1003975, 3000, 'Hats'), /*Princess of Time Veil*/
    (1003998, 3000, 'Hats'), /*White Choco Bunny*/
    (1004001, 3000, 'Hats'), /*Vampire Phantom Hat*/
    (1004003, 3000, 'Hats'), /*Pink Nero Hoodie*/
    (1004004, 3000, 'Hats'), /*Grey Nero Hoodie*/
    (1004014, 3000, 'Hats'), /*Grab N' Pull*/
    (1004015, 3000, 'Hats'), /*Freud's Face(M)*/
    (1004016, 3000, 'Hats'), /*Freud's Face(F)*/
    (1004017, 3000, 'Hats'), /*Aran's Helmet*/
    (1004018, 3000, 'Hats'), /*Brave Aran's Helmet*/
    (1004024, 3000, 'Hats'), /*Cheese Hat*/
    (1004026, 3000, 'Hats'), /*Black Cat Beanie*/
    (1004027, 3000, 'Hats'), /*Sky Blue Cat Beanie*/
    (1004028, 3000, 'Hats'), /*Orange Cat Beanie*/
    (1004029, 3000, 'Hats'), /*Snow Bear Beanie*/
    (1004034, 3000, 'Hats'), /*Study Break*/
    (1004035, 3000, 'Hats'), /*Snake Snapback Hat*/
    (1004036, 3000, 'Hats'), /*Mr. K's Cat Hat*/
    (1004038, 3000, 'Hats'), /*Ice Hat*/
    (1004039, 3000, 'Hats'), /*Eunwol Fox Ears*/
    (1004040, 3000, 'Hats'), /*Red Panda Ears*/
    (1004042, 3000, 'Hats'), /*Deluxe Rabbit Ears*/
    (1004044, 3000, 'Hats'), /*Bear Ears*/
    (1004045, 3000, 'Hats'), /*Beast Tamer Animal Ears 6*/
    (1004046, 3000, 'Hats'), /*Beast Tamer Animal Ears 7*/
    (1004047, 3000, 'Hats'), /*Beast Tamer Animal Ears 8*/
    (1004048, 3000, 'Hats'), /*Rudi's Hat*/
    (1004073, 3000, 'Hats'), /*Year of Horse Hat (Peach)*/
    (1004074, 3000, 'Hats'), /*Year of Horse Hat (Blue)*/
    (1004081, 3000, 'Hats'), /*Dawn Bear Hoodie*/
    (1004113, 3000, 'Hats'), /*Ghost Bride's Antique Wedding Veil*/
    (1004090, 3000, 'Hats'), /*Explorer Cap*/
    (1004091, 3000, 'Hats'), /*Deer Headband*/
    (1004092, 3000, 'Hats'), /*Cutie Horse Hat*/
    (1004099, 3000, 'Hats'), /*Christmas Antlers*/
    (1004106, 3000, 'Hats'), /*Guardian Head Band*/
    (1004108, 3000, 'Hats'), /*Fancy Magician Hat*/
    (1004117, 3000, 'Hats'), /*Candy Candy*/
    (1004120, 3000, 'Hats'), /*Strawberry Fairy*/
    (1004122, 3000, 'Hats'), /*Chef Hat*/
    (1004123, 3000, 'Hats'), /*Contemporary Chic Hat*/
    (1004125, 3000, 'Hats'), /*Pineapple Hat*/
    (1004126, 3000, 'Hats'), /*Rainbow Hat*/
    (1004136, 3000, 'Hats'), /*Nurse Cap*/
    (1004137, 3000, 'Hats'), /*Rabbit and Bear Hat*/
    (1004139, 3000, 'Hats'), /*Pink Panda Hat*/
    (1004140, 3000, 'Hats'), /*Commander Lotus Mask*/
    (1004141, 3000, 'Hats'), /*Commander Damien Mask*/
    (1004142, 3000, 'Hats'), /*Commander Lucid Mask*/
    (1004143, 3000, 'Hats'), /*Commander Magnus Mask*/
    (1004144, 3000, 'Hats'), /*Commander Von Leon Mask*/
    (1004145, 3000, 'Hats'), /*Commander Arkarium Mask*/
    (1004146, 3000, 'Hats'), /*Commander Orchid Mask*/
    (1004147, 3000, 'Hats'), /*Commander Will Mask*/
    (1004148, 3000, 'Hats'), /*Commander Hilla Mask*/
    (1004156, 3000, 'Hats'), /*Starry Earmuff*/
    (1004157, 3000, 'Hats'), /*Heart Headset*/
    (1004158, 3000, 'Hats'), /*LED Mouse Band*/
    (1004164, 3000, 'Hats'), /*Targa Silk Hat*/
    (1004165, 3000, 'Hats'), /*Spring Rose*/
    (1004166, 3000, 'Hats'), /*Black Butterfly Ribbon Headband*/
    (1004167, 3000, 'Hats'), /*Dinosaur Snapback*/
    (1004169, 3000, 'Hats'), /*Fried Egg Head*/
    (1004170, 3000, 'Hats'), /*Colorful Marble Parfait*/
    (1004171, 3000, 'Hats'), /*Dancing Carousel*/
    (1004175, 3000, 'Hats'), /*Angelic Melody*/
    (1004176, 3000, 'Hats'), /*Rabbit Mask*/
    (1004177, 3000, 'Hats'), /*Dark Cygnus*/
    (1004178, 3000, 'Hats'), /*Slab*/
    (1004179, 3000, 'Hats'), /*Red Elf Hat*/
    (1004180, 3000, 'Hats'), /*Disease Control STAR*/

/* Capes */
    (1100000, 3000, 'Capes'), /*Napoleon Cape*/
    (1100004, 3000, 'Capes'), /*Mad Doctor Stethoscope*/
    (1101000, 3000, 'Capes'), /*Ribbon Angel Syringe*/
    (1102005, 3000, 'Capes'), /*Baby Angel Wings*/
    (1102006, 3000, 'Capes'), /*Devil Wings*/
    (1102007, 3000, 'Capes'), /*Yellow Star Cape*/
    (1102008, 3000, 'Capes'), /*Blue Star Cape*/
    (1102009, 3000, 'Capes'), /*Red Star Cape*/
    (1102010, 3000, 'Capes'), /*Black Star Cape*/
    (1102019, 3000, 'Capes'), /*Korean-Flagged Cape*/
    (1102020, 3000, 'Capes'), /*Turtle Shell*/
    (1102025, 3000, 'Capes'), /*Red Hood*/
    (1102036, 3000, 'Capes'), /*Red Landcell Pack*/
    (1102037, 3000, 'Capes'), /*Black Landcell Pack*/
    (1102038, 3000, 'Capes'), /*Blue Landcell Pack*/
    (1102039, 3000, 'Capes'), /*Transparent Cape*/
    (1102044, 3000, 'Capes'), /*Red G-Wing Jetpack*/
    (1102045, 3000, 'Capes'), /*Blue G-Wing Jetpack*/
    (1102049, 3000, 'Capes'), /*Blue Nymph Wing*/
    (1102050, 3000, 'Capes'), /*Green Nymph Wing*/
    (1102051, 3000, 'Capes'), /*Yellow Nymph Wing*/
    (1102052, 3000, 'Capes'), /*Pink Nymph Wing*/
    (1102058, 3000, 'Capes'), /*Gargoyle Wings*/
    (1102059, 3000, 'Capes'), /*Michael Wings*/
    (1102060, 3000, 'Capes'), /*Pink Ribbon*/
    (1102062, 3000, 'Capes'), /*Martial Cape*/
    (1102063, 3000, 'Capes'), /*Fallen Angel Wings*/
    (1102065, 3000, 'Capes'), /*Christmas Cape*/
    (1102066, 3000, 'Capes'), /*Dracula Cloak*/
    (1102067, 3000, 'Capes'), /*Tiger Tail*/
    (1102068, 3000, 'Capes'), /*Harpie Cape*/
    (1102069, 3000, 'Capes'), /*Pink Wings*/
    (1102070, 3000, 'Capes'), /*Blue Book Bag*/
    (1102072, 3000, 'Capes'), /*Yellow-Green Backpack*/
    (1102073, 3000, 'Capes'), /*Hot Pink Backpack*/
    (1102074, 3000, 'Capes'), /*Dragonfly Wings*/
    (1102075, 3000, 'Capes'), /*Bat's Bane*/
    (1102076, 3000, 'Capes'), /*Newspaper Cape*/
    (1102077, 3000, 'Capes'), /*Cotton Blanket*/
    (1102091, 3000, 'Capes'), /*Summer Kite*/
    (1102092, 3000, 'Capes'), /*Cuddle Bear*/
    (1102093, 3000, 'Capes'), /*Heart Balloon*/
    (1102094, 3000, 'Capes'), /*Sun Wu Kong Tail*/
    (1102095, 3000, 'Capes'), /*Veamoth Wings*/
    (1102096, 3000, 'Capes'), /*Sachiel Wings*/
    (1102097, 3000, 'Capes'), /*Janus Wings*/
    (1102098, 3000, 'Capes'), /*Coffin of Gloom*/
    (1102107, 3000, 'Capes'), /*Rocket Booster*/
    (1102108, 3000, 'Capes'), /*Fallen Angel Tail*/
    (1102110, 3000, 'Capes'), /*Chipmunk Tail*/
    (1102111, 3000, 'Capes'), /*Elephant Balloon*/
    (1102112, 3000, 'Capes'), /*Bunny Doll*/
    (1102137, 3000, 'Capes'), /*Orange Mushroom Balloon*/
    (1102138, 3000, 'Capes'), /*Pink Wing Bag*/
    (1102141, 3000, 'Capes'), /*Pepe Balloon*/
    (1102142, 3000, 'Capes'), /*The Flaming Cape*/
    (1102144, 3000, 'Capes'), /*Sage Cape*/
    (1102148, 3000, 'Capes'), /*Tania Cloak*/
    (1102149, 3000, 'Capes'), /*Mercury Cloak*/
    (1102150, 3000, 'Capes'), /*Count Dracula Cape*/
    (1102151, 3000, 'Capes'), /*Lost Kitty*/
    (1102152, 3000, 'Capes'), /*Pirate Emblem Flag*/
    (1102153, 3000, 'Capes'), /*Sunfire Wings*/
    (1102155, 3000, 'Capes'), /*My Buddy Rex*/
    (1102156, 3000, 'Capes'), /*Aerial Wave Cape*/
    (1102157, 3000, 'Capes'), /*Puppet Strings*/
    (1102158, 3000, 'Capes'), /*Peacock Feather Cape*/
    (1102159, 3000, 'Capes'), /*White Monkey Balloon*/
    (1102160, 3000, 'Capes'), /*Baby Lupin Cape*/
    (1102162, 3000, 'Capes'), /*Baby White Monkey Balloon*/
    (1102164, 3000, 'Capes'), /*Maple MSX Guitar*/
    (1102169, 3000, 'Capes'), /*Blue Wing Bag*/
    (1102171, 3000, 'Capes'), /*3rd Anniversary Balloon*/
    (1102175, 3000, 'Capes'), /*Cutie Birk Wings*/
    (1102184, 3000, 'Capes'), /*Aurora Happy Wing*/
    (1102185, 3000, 'Capes'), /*Rainbow Scarf*/
    (1102186, 3000, 'Capes'), /*Kitty Parachute*/
    (1102187, 3000, 'Capes'), /*Golden Fox Tail*/
    (1102188, 3000, 'Capes'), /*Silver Fox Tail*/
    (1102196, 3000, 'Capes'), /*Snowflake Scarf*/
    (1102197, 3000, 'Capes'), /*Yellow Canary*/
    (1102202, 3000, 'Capes'), /*Galactic Flame Cape*/
    (1102203, 3000, 'Capes'), /*Super Rocket Booster*/
    (1102204, 3000, 'Capes'), /*Romantic Rose*/
    (1102208, 3000, 'Capes'), /*Slime Effect Cape*/
    (1102210, 3000, 'Capes'), /*Honeybee's Sting*/
    (1102211, 3000, 'Capes'), /*Bound Wings*/
    (1102212, 3000, 'Capes'), /*Lost Child*/
    (1102213, 3000, 'Capes'), /*Pink Bean Tail*/
    (1102214, 3000, 'Capes'), /*Pink Bean Balloon*/
    (1102215, 3000, 'Capes'), /*Balloon Bouquet*/
    (1102216, 3000, 'Capes'), /*Brown Dog Tail*/
    (1102217, 3000, 'Capes'), /*Goblin Cat*/
    (1102218, 3000, 'Capes'), /*Pink Floating Ribbon*/
    (1102220, 3000, 'Capes'), /*Pachinko Marble-box Cape*/
    (1102221, 3000, 'Capes'), /*Pluto Flame Cape*/
    (1102222, 3000, 'Capes'), /*Seraphim Cape*/
    (1102223, 3000, 'Capes'), /*Star Tail*/
    (1102224, 3000, 'Capes'), /*Lamby Cape*/
    (1102229, 3000, 'Capes'), /*Bear Cape*/
    (1102230, 3000, 'Capes'), /*Penguin Sled*/
    (1102232, 3000, 'Capes'), /*Celestial Star*/
    (1102233, 3000, 'Capes'), /*Snowman Cape*/
    (1102238, 3000, 'Capes'), /*Cat Set Tail*/
    (1102239, 3000, 'Capes'), /*Dual Blade Cape*/
    (1102240, 3000, 'Capes'), /*Royal Cape*/
    (1102242, 3000, 'Capes'), /*Hawkeye Ocean Cape*/
    (1102243, 3000, 'Capes'), /*Dunas Cape*/
    (1102245, 3000, 'Capes'), /*Sun Cape*/
    (1102249, 3000, 'Capes'), /*Oz Magic Cape*/
    (1102250, 3000, 'Capes'), /*Murgoth's Feather*/
    (1102251, 3000, 'Capes'), /*World Cup Towel*/
    (1102252, 3000, 'Capes'), /*Phoenix Wing*/
    (1102253, 3000, 'Capes'), /*Purple Wings*/
    (1102254, 3000, 'Capes'), /*Wild Hunter Cape*/
    (1102255, 3000, 'Capes'), /*Battle Mage Cape*/
    (1102257, 3000, 'Capes'), /*Marines Maple Balloon*/
    (1102258, 3000, 'Capes'), /*Teddy Bear Balloon*/
    (1102259, 3000, 'Capes'), /*Flying Dragon Cape*/
    (1102261, 3000, 'Capes'), /*Equalizer Scarf*/
    (1102267, 3000, 'Capes'), /*Friendly Nine-Tailed Fox Tails*/
    (1102270, 3000, 'Capes'), /*Moon and Sun Cape*/
    (1102271, 3000, 'Capes'), /*Lovely Chocolate Balloons*/
    (1102273, 3000, 'Capes'), /*Lucifer Half Wing*/
    (1102285, 3000, 'Capes'), /*Pink Teru Cape*/
    (1102286, 3000, 'Capes'), /*Blue Teru Cape*/
    (1102287, 3000, 'Capes'), /*Yellow Teru Cape*/
    (1102288, 3000, 'Capes'), /*Piggyback Snowman*/
    (1102290, 3000, 'Capes'), /*Silken Flower Cape*/
    (1102291, 3000, 'Capes'), /*Nekomata*/
    (1102292, 3000, 'Capes'), /*Twinkling Rainbow*/
    (1102296, 3000, 'Capes'), /*Gray Puppy Tail*/
    (1102300, 3000, 'Capes'), /*6th B-Day Party Ball*/
    (1102301, 3000, 'Capes'), /*Traveler's Cape*/
    (1102307, 3000, 'Capes'), /*New Sachiel Wings*/
    (1102308, 3000, 'Capes'), /*New Veamoth Wings*/
    (1102309, 3000, 'Capes'), /*New Janus Wings*/
    (1102310, 3000, 'Capes'), /*Fairytale Mantle*/
    (1102318, 3000, 'Capes'), /*Demon Wings*/
    (1102319, 3000, 'Capes'), /*Legends Balloon*/
    (1102323, 3000, 'Capes'), /*Legends Pink Balloon*/
    (1102324, 3000, 'Capes'), /*Legends Twin Balloons*/
    (1102325, 3000, 'Capes'), /*Harmony Wings*/
    (1102326, 3000, 'Capes'), /*Angelic Feathers*/
    (1102336, 3000, 'Capes'), /*Alchemist Cape*/
    (1102338, 3000, 'Capes'), /*Honeybee Wings */
    (1102343, 3000, 'Capes'), /*Dark Force Cape */
    (1102344, 3000, 'Capes'), /*Elven Spirit Cape*/
    (1102349, 3000, 'Capes'), /*Fairy Wing Cape*/
    (1102355, 3000, 'Capes'), /*Jewel Blizzard*/
    (1102356, 3000, 'Capes'), /*Angelic Emerald*/
    (1102357, 3000, 'Capes'), /*Pretty Pink Bean Balloon*/
    (1102358, 3000, 'Capes'), /*Round-We-Go Mirror Ball*/
    (1102359, 3000, 'Capes'), /*Floaty Snowman Balloon*/
    (1102368, 3000, 'Capes'), /*Floating Silken Flower Cape*/
    (1102373, 3000, 'Capes'), /*Lucia Cape*/
    (1102374, 3000, 'Capes'), /*Monkey Cape*/
    (1102376, 3000, 'Capes'), /*Psyche Flora*/
    (1102377, 3000, 'Capes'), /*Psyche Mystic*/
    (1102378, 3000, 'Capes'), /*Psyche Melody*/
    (1102380, 3000, 'Capes'), /*Frog Cronies*/
    (1102381, 3000, 'Capes'), /*Imperial Duke Wing*/
    (1102385, 3000, 'Capes'), /*Lux Cherubim*/
    (1102386, 3000, 'Capes'), /*Nox Cherubim*/
    (1102387, 3000, 'Capes'), /*Blue Dragon Tail*/
    (1102388, 3000, 'Capes'), /*Red Dragon Tail*/
    (1102389, 3000, 'Capes'), /*Aurora Pharady*/
    (1102390, 3000, 'Capes'), /*Aurora Angel*/
    (1102392, 3000, 'Capes'), /*Dainty Cape*/
    (1102396, 3000, 'Capes'), /*Ebony Pimpernel Cape*/
    (1102420, 3000, 'Capes'), /*Magic Star Cape*/
    (1102421, 3000, 'Capes'), /*Lemon Floating Smile*/
    (1102423, 3000, 'Capes'), /*Euro Balloon (PL)*/
    (1102424, 3000, 'Capes'), /*Euro Balloon (GR)*/
    (1102425, 3000, 'Capes'), /*Euro Balloon (RU)*/
    (1102426, 3000, 'Capes'), /*Euro Balloon (CZ)*/
    (1102427, 3000, 'Capes'), /*Euro Balloon (NL)*/
    (1102428, 3000, 'Capes'), /*Euro Balloon (DK)*/
    (1102429, 3000, 'Capes'), /*Euro Balloon (DE)*/
    (1102430, 3000, 'Capes'), /*Euro Balloon (PT)*/
    (1102431, 3000, 'Capes'), /*Euro Balloon (ES)*/
    (1102432, 3000, 'Capes'), /*Euro Balloon (IT)*/
    (1102433, 3000, 'Capes'), /*Euro Balloon (IE)*/
    (1102434, 3000, 'Capes'), /*Euro Balloon (HR)*/
    (1102435, 3000, 'Capes'), /*Euro Balloon (UA)*/
    (1102436, 3000, 'Capes'), /*Euro Balloon (SE)*/
    (1102437, 3000, 'Capes'), /*Euro Balloon (FR)*/
    (1102438, 3000, 'Capes'), /*Euro Balloon (GB)*/
    (1102450, 3000, 'Capes'), /*Heavenly Aura*/
    (1102451, 3000, 'Capes'), /*Void Aura*/
    (1102452, 3000, 'Capes'), /*Fairy Aura*/
    (1102453, 3000, 'Capes'), /*Dryad*/
    (1102461, 3000, 'Capes'), /*Valentine's Cape*/
    (1102465, 3000, 'Capes'), /*Jett's Cape*/
    (1102466, 3000, 'Capes'), /*Flying Nobilitas*/
    (1102486, 3000, 'Capes'), /*BasilMarket Billionaire Balloon*/
    (1102487, 3000, 'Capes'), /*Luminous Cherubim*/
    (1102488, 3000, 'Capes'), /*Cupcake Balloon*/
    (1102491, 3000, 'Capes'), /*Sunny Day*/
    (1102496, 3000, 'Capes'), /*Hyper Honeybee Wings*/
    (1102503, 3000, 'Capes'), /*Frisky Cat Tail*/
    (1102508, 3000, 'Capes'), /*Koala Cape*/
    (1102510, 3000, 'Capes'), /*Ribbon Kitty Tail*/
    (1102511, 3000, 'Capes'), /*Angel Cherub*/
    (1102512, 3000, 'Capes'), /*Dark Force Cape*/
    (1102532, 3000, 'Capes'), /*Light Wing Cherubim*/
    (1102544, 3000, 'Capes'), /*Albatross Cape*/
    (1102546, 3000, 'Capes'), /*Blue Bird Dream Wings*/
    (1102547, 3000, 'Capes'), /*Amethyst Dream Wings*/
    (1102548, 3000, 'Capes'), /*Leafy Dream Wings*/
    (1102549, 3000, 'Capes'), /*Steward Cat*/
    (1102550, 3000, 'Capes'), /*Lime Green Wings*/
    (1102551, 3000, 'Capes'), /*Sapphire Wings*/
    (1102554, 3000, 'Capes'), /*Succubus Wings*/
    (1102555, 3000, 'Capes'), /*Angelic White Wings*/
    (1102564, 3000, 'Capes'), /*Angel's Ribbon*/
    (1102572, 3000, 'Capes'), /*Gratias Aura*/
    (1102574, 3000, 'Capes'), /*Chicky Pile*/
    (1102582, 3000, 'Capes'), /*GM Daejang's Lucia Cape*/
    (1102583, 3000, 'Capes'), /*Baby Dragon Pobi*/
    (1102591, 3000, 'Capes'), /*Battle Monster Victory Cape*/
    (1102592, 3000, 'Capes'), /*Battle Monster Consolation Cape*/
    (1102593, 3000, 'Capes'), /*Floaty Baseball*/
    (1102604, 3000, 'Capes'), /*Gear Wing*/
    (1102605, 3000, 'Capes'), /*Shadow Peacemaker*/
    (1102608, 3000, 'Capes'), /*Superstar Mirror Ball*/
    (1102613, 3000, 'Capes'), /*Futuroid Tail Sensor*/
    (1102615, 3000, 'Capes'), /*Clocktower Wind-up Doll*/
    (1102616, 3000, 'Capes'), /*Lapis's Spirit*/
    (1102617, 3000, 'Capes'), /*Lazuli's Spirit*/
    (1102619, 3000, 'Capes'), /*Icy Sweet Penguin*/
    (1102620, 3000, 'Capes'), /*My Own Fireworks*/
    (1102621, 3000, 'Capes'), /*Nagging Megaphone*/
    (1102622, 3000, 'Capes'), /*Princess of Time Pocket Watch*/
    (1102624, 3000, 'Capes'), /*Aeolus Aura*/
    (1102625, 3000, 'Capes'), /*Snail Shell*/
    (1102629, 3000, 'Capes'), /*Pink Cherubim*/
    (1102630, 3000, 'Capes'), /*Romantic Wing Cherubim*/
    (1102631, 3000, 'Capes'), /*Vampire Phantom Cape*/
    (1102640, 3000, 'Capes'), /*Aran's Cape*/
    (1102641, 3000, 'Capes'), /*Yui's Spirit*/
    (1102642, 3000, 'Capes'), /*Yui's Wings*/
    (1102643, 3000, 'Capes'), /*Golden Age*/
    (1102644, 3000, 'Capes'), /*Pretty Pixie*/
    (1102648, 3000, 'Capes'), /*Mr. K's Cat Tail*/
    (1102650, 3000, 'Capes'), /*Eunwol Foxtail*/
    (1102651, 3000, 'Capes'), /*Red Panda Tail*/
    (1102653, 3000, 'Capes'), /*Deluxe Rabbit Tail*/
    (1102654, 3000, 'Capes'), /*Puppy Tail*/
    (1102655, 3000, 'Capes'), /*Bear Tail*/
    (1102656, 3000, 'Capes'), /*Bunny Tail*/
    (1102657, 3000, 'Capes'), /*Cat o' Nine Tails*/
    (1102658, 3000, 'Capes'), /*Cute Kitty Tail*/
    (1102667, 3000, 'Capes'), /*Magical Misty Moon*/
    (1102668, 3000, 'Capes'), /*Night Angel Wings*/
    (1102669, 3000, 'Capes'), /*Royal Spoiled Fairy*/
    (1102673, 3000, 'Capes'), /*Ghost Balloon*/
    (1102674, 3000, 'Capes'), /*Food Escape*/
    (1102675, 3000, 'Capes'), /*Candy Party Ribbon*/
    (1102682, 3000, 'Capes'), /*Nurse Syringe*/
    (1102683, 3000, 'Capes'), /*Rabbit and Bear Book Bag*/
    (1102684, 3000, 'Capes'), /*Doctor Stethoscope*/
    (1102685, 3000, 'Capes'), /*Baby Pink Panda Cape*/
    (1102688, 3000, 'Capes'), /*Boom Boom Fireworks*/
    (1102694, 3000, 'Capes'), /*Mini-Mini Slime*/
    (1102695, 3000, 'Capes'), /*Spirited Nine Tails*/
    (1102697, 3000, 'Capes'), /*Ruby Dragonfly Wings*/
    (1102698, 3000, 'Capes'), /*Emerald Dragonfly Wings*/
    (1102699, 3000, 'Capes'), /*Magma Wings*/
    (1102700, 3000, 'Capes'), /*Petit Ciel*/
    (1102702, 3000, 'Capes'), /*Ruby Monarch*/
    (1102703, 3000, 'Capes'), /*Jade Monarch*/
    (1102705, 3000, 'Capes'), /*Island Travel Bags*/
    (1102706, 3000, 'Capes'), /*Melodic Aurora*/
    (1102707, 3000, 'Capes'), /*Dreaming Conch*/
    (1102708, 3000, 'Capes'), /*Blushy Conch*/
    (1102709, 3000, 'Capes'), /*Lumina Flutter*/
    (1102712, 3000, 'Capes'), /*Long-awaited Resort*/
    (1102723, 3000, 'Capes'), /*Giant Bright Angel Wings*/
    (1102724, 3000, 'Capes'), /*Giant Dark Devil Wings*/
    (1102725, 3000, 'Capes'), /*Flopping Baby Sea Otter*/
    (1102726, 3000, 'Capes'), /*Carrot Cape*/
    (1102729, 3000, 'Capes'), /*Glowing Lights*/
    (1102730, 3000, 'Capes'), /*Glorious Aura*/
    (1102747, 3000, 'Capes'), /*Cutie Pandas*/
    (1102748, 3000, 'Capes'), /*Rabbit-Bear Camping Bag*/
    (1102749, 3000, 'Capes'), /*Starland Balloon*/
    (1102754, 3000, 'Capes'), /*Idol of the Birds*/
    (1102755, 3000, 'Capes'), /*Boom Star Balloon*/
    (1102756, 3000, 'Capes'), /*Corn Cape*/
    (1102758, 3000, 'Capes'), /*Victory Wings*/
    (1102759, 3000, 'Capes'), /*Ball Buddies*/
    (1102766, 3000, 'Capes'), /*Raging Lotus Aura*/
    (1102767, 3000, 'Capes'), /*Ill Orchid IV*/
    (1102768, 3000, 'Capes'), /*Worn Witch Cape*/
    (1102769, 3000, 'Capes'), /*Witch Cape*/
    (1102772, 3000, 'Capes'), /*Worn Ghost Cape*/
    (1102773, 3000, 'Capes'), /*Ghost Cape*/
    (1102774, 3000, 'Capes'), /*Total Lunar Eclipse Cape*/
    (1102778, 3000, 'Capes'), /*Lolli Lolli Lollipop*/
    (1102779, 3000, 'Capes'), /*Gold Wing*/
    (1102780, 3000, 'Capes'), /*With Eren*/
    (1102781, 3000, 'Capes'), /*With Mikasa*/
    (1102782, 3000, 'Capes'), /*With Annie*/
    (1102783, 3000, 'Capes'), /*With Sasha*/
    (1102784, 3000, 'Capes'), /*With Christa*/
    (1102785, 3000, 'Capes'), /*With Levi*/
    (1102786, 3000, 'Capes'), /*Titan Escape*/
    (1102787, 3000, 'Capes'), /*Scout Regiment Cape*/
    (1102788, 3000, 'Capes'), /*With Armin*/
    (1102789, 3000, 'Capes'), /*Snow Bear Scarf*/
    (1102798, 3000, 'Capes'), /*Blue Bird Wings*/
    (1102800, 3000, 'Capes'), /*Fluffy Bell Cape*/
    (1102801, 3000, 'Capes'), /*Silver Wolf Coat*/
    (1102808, 3000, 'Capes'), /*Loved Mouse Couple*/
    (1102809, 3000, 'Capes'), /*Death Waltz Cloak*/
    (1102811, 3000, 'Capes'), /*Snow Bloom*/
    (1102812, 3000, 'Capes'), /*Blizzard Drive*/
    (1102813, 3000, 'Capes'), /*Shoulder Blanche*/
    (1102815, 3000, 'Capes'), /*Lucky Charm Bag*/
    (1102816, 3000, 'Capes'), /*Fairy Bell*/
    (1102818, 3000, 'Capes'), /*Crystal Cat Star Cape*/
    (1102819, 3000, 'Capes'), /*Naughty Boy Backpack*/
    (1102820, 3000, 'Capes'), /*Hazy Night Tassel*/
    (1102822, 3000, 'Capes'), /*Flowery Breeze*/
    (1102823, 3000, 'Capes'), /*Petite Devil Wings*/
    (1102824, 3000, 'Capes'), /*Halfblood Wings*/
    (1102827, 3000, 'Capes'), /*The Kingdom Cape of King*/
    (1102830, 3000, 'Capes'), /*돼지바의 요정*/
    (1102831, 3000, 'Capes'), /*Soaring High*/
    (1102832, 3000, 'Capes'), /*Machine Cape*/
    (1102835, 3000, 'Capes'), /*Schwarzer Coat*/
    (1102836, 3000, 'Capes'), /*Wonder Kitty*/
    (1102837, 3000, 'Capes'), /*Dreams Within Dreams*/
    (1102839, 3000, 'Capes'), /*Pink Zakum Arms*/
    (1102841, 3000, 'Capes'), /*Iris Pearl*/
    (1102842, 3000, 'Capes'), /*Pineapple Bag*/
    (1102844, 3000, 'Capes'), /*Run Run Bounce Bounce*/
    (1102845, 3000, 'Capes'), /*Blue Panda*/
    (1102847, 3000, 'Capes'), /*Yeonhwa School Guardian Soul Fire*/
    (1102848, 3000, 'Capes'), /*Gravity*/
    (1102857, 3000, 'Capes'), /*Legendary Fish Man*/
    (1102858, 3000, 'Capes'), /*Eternal Clockwork*/
    (1102859, 3000, 'Capes'), /*Sapphire Snow*/
    (1102860, 3000, 'Capes'), /*British Weather Cape*/
    (1102861, 3000, 'Capes'), /*Ursus Light*/
    (1102863, 3000, 'Capes'), /*Sparkly Rainbow Cape*/
    (1102864, 3000, 'Capes'), /*Farmer's Grace*/
    (1102865, 3000, 'Capes'), /*Thundercrash Cape*/
    (1102868, 3000, 'Capes'), /*Triple Bat Cape*/
    (1102869, 3000, 'Capes'), /*Bloody Rose*/
    (1102870, 3000, 'Capes'), /*Midnight Black Coffin*/
    (1102872, 3000, 'Capes'), /*Shining Noblesse*/
    (1102873, 3000, 'Capes'), /*Eternal Noblesse*/
    (1102874, 3000, 'Capes'), /*Lumin Wings*/
    (1102875, 3000, 'Capes'), /*Amnesiac Alien*/
    (1102876, 3000, 'Capes'), /*Selfie Time*/
    (1102877, 3000, 'Capes'), /*Blue Marine Sunshine*/
    (1102884, 3000, 'Capes'), /*Angelic Polar Cape*/
    (1102885, 3000, 'Capes'), /*Fluffy Fox Tail (Gold)*/
    (1102886, 3000, 'Capes'), /*Fluffy Fox Tail (Silver)*/
    (1102888, 3000, 'Capes'), /*Evan Dragon Cape*/
    (1102890, 3000, 'Capes'), /*Royal Mercedes Cape*/
    (1102892, 3000, 'Capes'), /*Mystic Phantom Cape*/
    (1102894, 3000, 'Capes'), /*Winter Aran Cape*/
    (1102896, 3000, 'Capes'), /*Chiaroscuro Luminous Cape*/
    (1102898, 3000, 'Capes'), /*Secret Shade Cape*/
    (1102900, 3000, 'Capes'), /*Lumpy Snowflakes*/
    (1102902, 3000, 'Capes'), /*Dokidoki*/
    (1102903, 3000, 'Capes'), /*Floating Monkey*/
    (1102905, 3000, 'Capes'), /*Today's Sunshine Cape*/
    (1102906, 3000, 'Capes'), /*Snug Black Nero*/
    (1102907, 3000, 'Capes'), /*Ice Flower Wing*/
    (1102910, 3000, 'Capes'), /*Smile Seed Cape*/
    (1102912, 3000, 'Capes'), /*Umbral Cloak*/
    (1102913, 3000, 'Capes'), /*Flower Dancer's Red Cape*/
    (1102914, 3000, 'Capes'), /*Moon Dancer's Blue Cape*/
    (1102915, 3000, 'Capes'), /*Concert Muse*/
    (1102916, 3000, 'Capes'), /*Baby Binkie Toys*/
    (1102917, 3000, 'Capes'), /*Angel's Cookie Backpack*/
    (1102926, 3000, 'Capes'), /*Shark Cape*/
    (1102927, 3000, 'Capes'), /*Windy Paw Scarf*/
    (1102937, 3000, 'Capes'), /*Fantastic Beach Cape*/
    (1102939, 3000, 'Capes'), /*Red Cloud Cape*/
    (1102953, 3000, 'Capes'), /*Kamaitachi Cape*/
    (1102954, 3000, 'Capes'), /*Owl Balloon*/
    (1102955, 3000, 'Capes'), /*Moon Bunny Cape*/
    (1102956, 3000, 'Capes'), /*Dark Musician Scarf*/
    (1102957, 3000, 'Capes'), /*Chained Princess Chain*/
    (1102958, 3000, 'Capes'), /*Light Bulb Wings*/

/* Outfits */
    (1050004, 4500, 'Outfits'), /*Blue Officer Uniform*/
    (1050012, 4500, 'Outfits'), /*Grey Skull Overall*/
    (1050013, 4500, 'Outfits'), /*Red Skull Overall*/
    (1050014, 4500, 'Outfits'), /*Green Skull Overall*/
    (1050015, 4500, 'Outfits'), /*Blue Skull Overall*/
    (1050016, 4500, 'Outfits'), /*Orange Skull Overall*/
    (1050017, 4500, 'Outfits'), /*Yellow Tights*/
    (1050019, 4500, 'Outfits'), /*Santa Costume*/
    (1050020, 4500, 'Outfits'), /*Paper Box*/
    (1050032, 4500, 'Outfits'), /*Silver Officer Uniform*/
    (1050033, 4500, 'Outfits'), /*Black Officer Uniform*/
    (1050034, 4500, 'Outfits'), /*Red Officer Uniform*/
    (1050040, 4500, 'Outfits'), /*Red Swimming Trunk*/
    (1050041, 4500, 'Outfits'), /*Blue Swimming Trunk*/
    (1050042, 4500, 'Outfits'), /*Fine Brown Hanbok*/
    (1050043, 4500, 'Outfits'), /*Fine Black Hanbok*/
    (1050044, 4500, 'Outfits'), /*Fine Blue Hanbok*/
    (1050050, 4500, 'Outfits'), /*Dark Suit*/
    (1050057, 4500, 'Outfits'), /*Ghost Uniform*/
    (1050065, 4500, 'Outfits'), /*Blue Celebration Hanbok*/
    (1050066, 4500, 'Outfits'), /*Green Celebration Hanbok*/
    (1050071, 4500, 'Outfits'), /*Men's Ninja Overall*/
    (1050079, 4500, 'Outfits'), /*Black Coat of Death*/
    (1050084, 4500, 'Outfits'), /*Red Mesoranger*/
    (1050085, 4500, 'Outfits'), /*Blue Mesoranger*/
    (1050086, 4500, 'Outfits'), /*Green Mesoranger*/
    (1050087, 4500, 'Outfits'), /*Black Mesoranger*/
    (1050101, 4500, 'Outfits'), /*Western Cowboy*/
    (1050109, 4500, 'Outfits'), /*Green Picnicwear*/
    (1050110, 4500, 'Outfits'), /*Sky Blue Picnicwear*/
    (1050111, 4500, 'Outfits'), /*Boxing Trunks*/
    (1050112, 4500, 'Outfits'), /*Wedding Dress*/
    (1050113, 4500, 'Outfits'), /*Wedding Tuxedo*/
    (1050114, 4500, 'Outfits'), /*Poseidon Armor*/
    (1050115, 4500, 'Outfits'), /*Sea Hermit Robe*/
    (1050116, 4500, 'Outfits'), /*Race Ace Suit*/
    (1050117, 4500, 'Outfits'), /*Tiny Blue Swimshorts*/
    (1050118, 4500, 'Outfits'), /*Tiny Black Swimshorts*/
    (1050119, 4500, 'Outfits'), /*Santa Boy Overall*/
    (1050120, 4500, 'Outfits'), /*Horoscope Overall*/
    (1050121, 4500, 'Outfits'), /*Oriental Bridegroom Suit*/
    (1050122, 4500, 'Outfits'), /*Unseemly Wedding Suit*/
    (1050123, 4500, 'Outfits'), /*Royal Hanbok*/
    (1050124, 4500, 'Outfits'), /*Lunar Celebration Suit*/
    (1050125, 4500, 'Outfits'), /*Brown Casual Look*/
    (1050126, 4500, 'Outfits'), /*Imperial Uniform*/
    (1050128, 4500, 'Outfits'), /*Go! Korea!*/
    (1050129, 4500, 'Outfits'), /*Korean Martial Art Uniform*/
    (1050135, 4500, 'Outfits'), /*Beau Tuxedo*/
    (1050136, 4500, 'Outfits'), /*Black Male Fur Coat*/
    (1050137, 4500, 'Outfits'), /*White Male Fur Coat*/
    (1050138, 4500, 'Outfits'), /*School Uniform with Hoody Jumper*/
    (1050139, 4500, 'Outfits'), /*Boys Uniform*/
    (1050140, 4500, 'Outfits'), /*Thai Formal Dress*/
    (1050141, 4500, 'Outfits'), /*Blue Kitty Hood*/
    (1050142, 4500, 'Outfits'), /*Hooded Korean Traditional Costume*/
    (1050143, 4500, 'Outfits'), /*Retro School Uniform*/
    (1050145, 4500, 'Outfits'), /*Violet Tunic*/
    (1050146, 4500, 'Outfits'), /*Buddy Overall Jeans*/
    (1050147, 4500, 'Outfits'), /*Princess Korean Traditional Costume*/
    (1050148, 4500, 'Outfits'), /*Shin-Hwa High Uniform*/
    (1050152, 4500, 'Outfits'), /*Sailor Outfit*/
    (1050153, 4500, 'Outfits'), /*Exotic Festival Outfit*/
    (1050154, 4500, 'Outfits'), /*Seraphim Suit*/
    (1050156, 4500, 'Outfits'), /*Blue Towel*/
    (1050157, 4500, 'Outfits'), /*Cutie Boy Overall*/
    (1050160, 4500, 'Outfits'), /*Nya-ong's Long Hood T-shirt*/
    (1050161, 4500, 'Outfits'), /*Bunny Boy*/
    (1050168, 4500, 'Outfits'), /*Evan Elegant Suit*/
    (1050170, 4500, 'Outfits'), /*Napoleon Uniform*/
    (1050171, 4500, 'Outfits'), /*Evan Outfit*/
    (1050177, 4500, 'Outfits'), /*Maple Boy School Uniform*/
    (1050179, 4500, 'Outfits'), /*Holiday Party Gear*/
    (1050186, 4500, 'Outfits'), /*Rookie Maple Boy School Uniform*/
    (1050187, 4500, 'Outfits'), /*Blue Snow Flower Wear*/
    (1050188, 4500, 'Outfits'), /*Flower Heir Hanbok*/
    (1050190, 4500, 'Outfits'), /*Military Pop Star*/
    (1050193, 4500, 'Outfits'), /*Red Overall Pants*/
    (1050208, 4500, 'Outfits'), /*Schoolboy Formals*/
    (1050209, 4500, 'Outfits'), /*Moonlight Serenade Get-Up*/
    (1050210, 4500, 'Outfits'), /*Light Cotton Candy Overalls*/
    (1050215, 4500, 'Outfits'), /*Maple Doctor's Scrubs (M)*/
    (1050220, 4500, 'Outfits'), /*Dark Force Mail (M) */
    (1050221, 4500, 'Outfits'), /*Elven Spirit Coat (M) */
    (1050226, 4500, 'Outfits'), /*Imperial Garnet Suit*/
    (1050227, 4500, 'Outfits'), /*Mint Snow Outfit*/
    (1050228, 4500, 'Outfits'), /*Elven Spirit Coat*/
    (1050229, 4500, 'Outfits'), /*Gentle Hanbok*/
    (1050232, 4500, 'Outfits'), /*Chamomile Tea Time*/
    (1050234, 4500, 'Outfits'), /*Magic Star Suit*/
    (1050235, 4500, 'Outfits'), /*Prince Charming*/
    (1050241, 4500, 'Outfits'), /*Jett's Outfit(M)*/
    (1050242, 4500, 'Outfits'), /*Opening Star*/
    (1050246, 4500, 'Outfits'), /*Saint Luminous*/
    (1050247, 4500, 'Outfits'), /*Evergreen Magistrate Outfit*/
    (1050248, 4500, 'Outfits'), /*Halloween Leopard Costume*/
    (1050255, 4500, 'Outfits'), /*Dark Force Suit*/
    (1050256, 4500, 'Outfits'), /*Alps Boy Overall*/
    (1050284, 4500, 'Outfits'), /*Golden Bell Outfit*/
    (1050285, 4500, 'Outfits'), /*Thermidor*/
    (1050291, 4500, 'Outfits'), /*Tinky Baseball*/
    (1050292, 4500, 'Outfits'), /*Blue Swimming Trunks*/
    (1050293, 4500, 'Outfits'), /*Beach Bum Outfit*/
    (1050296, 4500, 'Outfits'), /*Superstar Suit*/
    (1050297, 4500, 'Outfits'), /*Rising Star*/
    (1050298, 4500, 'Outfits'), /*Alpha Seraphim*/
    (1050299, 4500, 'Outfits'), /*Baby Doll Puka*/
    (1050300, 4500, 'Outfits'), /*Fresh Ice*/
    (1050301, 4500, 'Outfits'), /*Blue Checkered Vacation*/
    (1050302, 4500, 'Outfits'), /*Powder Butler's Digs (M)*/
    (1050303, 4500, 'Outfits'), /*Ribbon Boy School Look*/
    (1050304, 4500, 'Outfits'), /*Splash Choco Boy*/
    (1050305, 4500, 'Outfits'), /*Bloody Leo*/
    (1050310, 4500, 'Outfits'), /*Shiny Shopper*/
    (1050311, 4500, 'Outfits'), /*Moonlight Costume*/
    (1050312, 4500, 'Outfits'), /*Grand Pony*/
    (1050314, 4500, 'Outfits'), /*Plop! Chocolate Boy*/
    (1050315, 4500, 'Outfits'), /*Blue Shiny Suit*/
    (1050316, 4500, 'Outfits'), /*Balloon Overalls*/
    (1050318, 4500, 'Outfits'), /*White Proposal*/
    (1050319, 4500, 'Outfits'), /*Sky Blue Picnic*/
    (1050321, 4500, 'Outfits'), /*[[FROZEN CONTENT]] Kristoff Coat*/
    (1050322, 4500, 'Outfits'), /*Party Prince*/
    (1050335, 4500, 'Outfits'), /*Melody Boy*/
    (1050336, 4500, 'Outfits'), /*축구선수 유니폼*/
    (1050337, 4500, 'Outfits'), /*Hawaiian Couple*/
    (1050338, 4500, 'Outfits'), /*Maple Leaf High Swimsuit (M)*/
    (1050339, 4500, 'Outfits'), /*Glowy Light*/
    (1050340, 4500, 'Outfits'), /*Gentle Ice Boy*/
    (1050341, 4500, 'Outfits'), /*In-forest Camping Look*/
    (1050343, 4500, 'Outfits'), /*Gentle Dylan*/
    (1050346, 4500, 'Outfits'), /*Cheer Uniform*/
    (1050351, 4500, 'Outfits'), /*Mad Doctor Gown*/
    (1050353, 4500, 'Outfits'), /*Sweet Patissier*/
    (1050356, 4500, 'Outfits'), /*Gothic Boys Uniform*/
    (1050359, 4500, 'Outfits'), /*Cool Snow Flower*/
    (1050360, 4500, 'Outfits'), /*Ryan D Suit*/
    (1050361, 4500, 'Outfits'), /*Mr. Love Messenger Outfit*/
    (1050362, 4500, 'Outfits'), /*Little Trainer Outfit (M)*/
    (1050364, 4500, 'Outfits'), /*Leafy Love Outfit*/
    (1050365, 4500, 'Outfits'), /*Starlight Boy outfit*/
    (1050366, 4500, 'Outfits'), /*The Kingdom Suit of King*/
    (1051132, 4500, 'Outfits'), /*White Coat*/
    (1050368, 4500, 'Outfits'), /*Romantic Sky*/
    (1050370, 4500, 'Outfits'), /*Mint Kitty Tea Party*/
    (1050371, 4500, 'Outfits'), /*Blue Crystal*/
    (1050372, 4500, 'Outfits'), /*Gentle Mickey*/
    (1050373, 4500, 'Outfits'), /*Tim Gentleman Suit*/
    (1050378, 4500, 'Outfits'), /*Yeonhwa School Uniform*/
    (1050380, 4500, 'Outfits'), /*Kinesis Uniform*/
    (1050382, 4500, 'Outfits'), /*Millionaire Suit*/
    (1050383, 4500, 'Outfits'), /*Teddy Suspenders*/
    (1050384, 4500, 'Outfits'), /*Penguin Doll Outfit*/
    (1050385, 4500, 'Outfits'), /*Time Getup*/
    (1050386, 4500, 'Outfits'), /*Sparkling Bluebird (M)*/
    (1050387, 4500, 'Outfits'), /*British Marine Outfit (M)*/
    (1050388, 4500, 'Outfits'), /*Ursus Gentleman's Suit*/
    (1050389, 4500, 'Outfits'), /*Cutie Farmer Apron*/
    (1050390, 4500, 'Outfits'), /*Polka-Dot Bell Bottoms*/
    (1051131, 4500, 'Outfits'), /*Santa Girl Overall*/
    (1050392, 4500, 'Outfits'), /*Bloody Guardian*/
    (1050393, 4500, 'Outfits'), /*Banana Outing Clothes*/
    (1050394, 4500, 'Outfits'), /*Bubbly Traveler*/
    (1050395, 4500, 'Outfits'), /*Blue Marine Uniform (M)*/
    (1051125, 4500, 'Outfits'), /*Black Cat Costume*/
    (1051126, 4500, 'Outfits'), /*Red Chinese Dress*/
    (1051127, 4500, 'Outfits'), /*Maid Uniform*/
    (1050401, 4500, 'Outfits'), /*Time Master*/
    (1050402, 4500, 'Outfits'), /*Evan Dragon Suit*/
    (1050404, 4500, 'Outfits'), /*Royal Mercedes Suit*/
    (1050406, 4500, 'Outfits'), /*Mystic Phantom Suit*/
    (1050408, 4500, 'Outfits'), /*Winter Aran Suit*/
    (1050410, 4500, 'Outfits'), /*Chiaroscuro Luminous Suit*/
    (1050412, 4500, 'Outfits'), /*Secret Shade Suit*/
    (1050414, 4500, 'Outfits'), /*Cozy Bathrobe*/
    (1050416, 4500, 'Outfits'), /*Time Tailcoat*/
    (1050417, 4500, 'Outfits'), /*Ice Deer Parka*/
    (1051130, 4500, 'Outfits'), /*Unseemly Wedding Dress*/
    (1051129, 4500, 'Outfits'), /*Oriental Princess Gown*/
    (1050422, 4500, 'Outfits'), /*Concert Muse (Tenor)*/
    (1051002, 4500, 'Outfits'), /*Cat Suit*/
    (1051018, 4500, 'Outfits'), /*Purple Skull Overall*/
    (1051028, 4500, 'Outfits'), /*White Swimming Suit*/
    (1051029, 4500, 'Outfits'), /*Red Swimming Suit*/
    (1051035, 4500, 'Outfits'), /*Fine Red Hanbok*/
    (1051040, 4500, 'Outfits'), /*Dark Enamel Suit*/
    (1051048, 4500, 'Outfits'), /*Witch Clothes*/
    (1051049, 4500, 'Outfits'), /*Mrs. Claus Costume*/
    (1051050, 4500, 'Outfits'), /*Blue Celeberation Hanbok*/
    (1051051, 4500, 'Outfits'), /*Pink Celebration Hanbok*/
    (1051059, 4500, 'Outfits'), /*Pink Nurse Uniform*/
    (1051060, 4500, 'Outfits'), /*White Nurse Uniform*/
    (1051061, 4500, 'Outfits'), /*Women's Ninja Uniform*/
    (1051070, 4500, 'Outfits'), /*Bunny Costume*/
    (1051071, 4500, 'Outfits'), /*Pink Kimono*/
    (1051072, 4500, 'Outfits'), /*White Kimono*/
    (1051073, 4500, 'Outfits'), /*Red Kimono*/
    (1051074, 4500, 'Outfits'), /*Yellow Kimono*/
    (1051075, 4500, 'Outfits'), /*Blue Swimming Suit*/
    (1051076, 4500, 'Outfits'), /*Ghost Suit*/
    (1051086, 4500, 'Outfits'), /*Ragged Hanbok*/
    (1051087, 4500, 'Outfits'), /*Pink Mesoranger*/
    (1051088, 4500, 'Outfits'), /*Yellow Mesoranger*/
    (1051099, 4500, 'Outfits'), /*Prep Uniform*/
    (1051100, 4500, 'Outfits'), /*Western Cowgirl*/
    (1051108, 4500, 'Outfits'), /*Pink Picnic Dress*/
    (1051109, 4500, 'Outfits'), /*Yellow Picnic Dress*/
    (1051110, 4500, 'Outfits'), /*Purple Frill One Piece*/
    (1051111, 4500, 'Outfits'), /*Blue Frill One Piece*/
    (1051112, 4500, 'Outfits'), /*Boxing Gear*/
    (1051113, 4500, 'Outfits'), /*Wedding Tuxedo (F)*/
    (1051115, 4500, 'Outfits'), /*Sea Queen Dress*/
    (1051116, 4500, 'Outfits'), /*Race Queen Uniform*/
    (1051117, 4500, 'Outfits'), /*Diao Chan Dress*/
    (1051118, 4500, 'Outfits'), /*Pink Strapless Bikini*/
    (1051119, 4500, 'Outfits'), /*Blue Strapless Bikini*/
    (1051120, 4500, 'Outfits'), /*Flight Attendant Uniform*/
    (1051121, 4500, 'Outfits'), /*Tropical Dress*/
    (1051122, 4500, 'Outfits'), /*White Cat Costume*/
    (1051123, 4500, 'Outfits'), /*Violet Strapless Dress*/
    (1051124, 4500, 'Outfits'), /*Purple Ring One Piece*/
    (1051133, 4500, 'Outfits'), /*Rough Coat*/
    (1051134, 4500, 'Outfits'), /*Leopard Print Coat*/
    (1051135, 4500, 'Outfits'), /*Ruffled Coat*/
    (1051136, 4500, 'Outfits'), /*Royal Palace Hanbok*/
    (1051137, 4500, 'Outfits'), /*Rabbit Fur Dress*/
    (1051138, 4500, 'Outfits'), /*Lunar Celebration Dress*/
    (1051139, 4500, 'Outfits'), /*White Ribboned Sailor Dress*/
    (1051141, 4500, 'Outfits'), /*Female Shaman Costume*/
    (1051142, 4500, 'Outfits'), /*Vibrant Yellow Dress*/
    (1051143, 4500, 'Outfits'), /*Race Queen Tank Top & Skirt*/
    (1051144, 4500, 'Outfits'), /*Elegant Blue One Piece*/
    (1051145, 4500, 'Outfits'), /*Royal Maid Uniform*/
    (1051146, 4500, 'Outfits'), /*Royal Nurse Uniform*/
    (1051147, 4500, 'Outfits'), /*Street Cred Ensemble*/
    (1051148, 4500, 'Outfits'), /*Navy Blue Au Luxe*/
    (1051149, 4500, 'Outfits'), /*Princess Dress*/
    (1051154, 4500, 'Outfits'), /*Princess Isis*/
    (1051155, 4500, 'Outfits'), /*Queen Mary*/
    (1051156, 4500, 'Outfits'), /*Black Female Fur Coat*/
    (1051157, 4500, 'Outfits'), /*White Female Fur Coat*/
    (1051159, 4500, 'Outfits'), /*Girls Uniform*/
    (1051160, 4500, 'Outfits'), /*Pink-Striped Dress*/
    (1051162, 4500, 'Outfits'), /*Cute Sailor Dress*/
    (1051163, 4500, 'Outfits'), /*Gothic Overall*/
    (1051164, 4500, 'Outfits'), /*Kitty Hoodie*/
    (1051166, 4500, 'Outfits'), /*Dressu Korean Traditional Costume*/
    (1051167, 4500, 'Outfits'), /*Black Rockabilly Dress*/
    (1051169, 4500, 'Outfits'), /*Sky Blue Picnicwear [F]*/
    (1051171, 4500, 'Outfits'), /*Royal Costume*/
    (1051173, 4500, 'Outfits'), /*Purple Dorothy Dress*/
    (1051174, 4500, 'Outfits'), /*Bikini*/
    (1051175, 4500, 'Outfits'), /*Strawberry Milk Dress*/
    (1051179, 4500, 'Outfits'), /*Pretty Girl*/
    (1051183, 4500, 'Outfits'), /*Night Fever Ensemble*/
    (1051185, 4500, 'Outfits'), /*Maid Dress*/
    (1051188, 4500, 'Outfits'), /*Blue Daisy Dress*/
    (1051189, 4500, 'Outfits'), /*Yellow Anticipation*/
    (1051192, 4500, 'Outfits'), /*Blue Marine Girl*/
    (1051193, 4500, 'Outfits'), /*Orange Towel*/
    (1051195, 4500, 'Outfits'), /*Cutie Girl Overall*/
    (1051196, 4500, 'Outfits'), /*Black Top Dress*/
    (1051198, 4500, 'Outfits'), /*Pink mini dress*/
    (1051200, 4500, 'Outfits'), /*Bunny Girl*/
    (1051208, 4500, 'Outfits'), /*Strawberry Girl*/
    (1051209, 4500, 'Outfits'), /*Evan Great Suit*/
    (1051211, 4500, 'Outfits'), /*Elizabeth Dress*/
    (1051218, 4500, 'Outfits'), /*Maple Girl School Uniform*/
    (1051219, 4500, 'Outfits'), /*Rainbow Mini Dress*/
    (1051221, 4500, 'Outfits'), /*Holiday Party Dress*/
    (1051227, 4500, 'Outfits'), /*Rookie Maple Girl School Uniform*/
    (1051228, 4500, 'Outfits'), /*Pink Snow Flower Wear*/
    (1051229, 4500, 'Outfits'), /*Flower Heiress Hanbok*/
    (1051231, 4500, 'Outfits'), /*Alps Girl*/
    (1051232, 4500, 'Outfits'), /*Pink Shock Pop Star*/
    (1051233, 4500, 'Outfits'), /*Taxi Costume*/
    (1051235, 4500, 'Outfits'), /*Orange Checked Squares*/
    (1052049, 4500, 'Outfits'), /*Yang In*/
    (1051252, 4500, 'Outfits'), /*Pink Angel Uniform*/
    (1051253, 4500, 'Outfits'), /*Little Red Riding Dress*/
    (1051254, 4500, 'Outfits'), /*Schoolgirl Formals*/
    (1051255, 4500, 'Outfits'), /*Golden Moonlight Dress*/
    (1051256, 4500, 'Outfits'), /*Light Chiffon Dress*/
    (1051261, 4500, 'Outfits'), /*Marchen Fantasy*/
    (1051262, 4500, 'Outfits'), /*Maple Doctor's Scrubs (F)*/
    (1051264, 4500, 'Outfits'), /*Silver Angora Fur Dress*/
    (1051265, 4500, 'Outfits'), /*Gold Angora Fur Dress*/
    (1051270, 4500, 'Outfits'), /*Dark Force Mail (F) */
    (1051271, 4500, 'Outfits'), /*Elven Spirit Coat (F) */
    (1051277, 4500, 'Outfits'), /*Cygnus Dress*/
    (1051278, 4500, 'Outfits'), /*Cherry Snow Outfit*/
    (1051282, 4500, 'Outfits'), /*Rosemary Tea*/
    (1051284, 4500, 'Outfits'), /*Magic Star Dress*/
    (1051285, 4500, 'Outfits'), /*Princess Charming*/
    (1051290, 4500, 'Outfits'), /*Jett's Outfit(F)*/
    (1051292, 4500, 'Outfits'), /*Opening Angel*/
    (1051294, 4500, 'Outfits'), /*Lyrical Dress*/
    (1051295, 4500, 'Outfits'), /*Magical Dress*/
    (1051296, 4500, 'Outfits'), /*Cynical Dress*/
    (1051302, 4500, 'Outfits'), /*Pink Fluffy Hanbok*/
    (1051304, 4500, 'Outfits'), /*Halloween Leopard Dress*/
    (1051312, 4500, 'Outfits'), /*Alps Girl Dress*/
    (1051332, 4500, 'Outfits'), /*Logical Dress*/
    (1051333, 4500, 'Outfits'), /*Miracle Dress*/
    (1051345, 4500, 'Outfits'), /*Fluffy Cat Outfit*/
    (1051348, 4500, 'Outfits'), /*Ellinia Magic Academy Uniform*/
    (1051349, 4500, 'Outfits'), /*Succubus Dress*/
    (1051350, 4500, 'Outfits'), /*Golden Bell Dress*/
    (1051351, 4500, 'Outfits'), /*GM Nori's Uniform*/
    (1051357, 4500, 'Outfits'), /*Pinky Baseball*/
    (1051358, 4500, 'Outfits'), /*Pink Cutie Bikini*/
    (1051359, 4500, 'Outfits'), /*Beach Babe Outfit*/
    (1051362, 4500, 'Outfits'), /*Superstar Dress*/
    (1051363, 4500, 'Outfits'), /*Rising Angel*/
    (1051365, 4500, 'Outfits'), /*Beta Seraphim*/
    (1051366, 4500, 'Outfits'), /*Baby Doll Linka*/
    (1051368, 4500, 'Outfits'), /*Fresh Checkered Vacation*/
    (1051369, 4500, 'Outfits'), /*Powder Maid's Getup (F)*/
    (1051370, 4500, 'Outfits'), /*Passionate Qi Pao*/
    (1051371, 4500, 'Outfits'), /*Ribbon Girl School Look*/
    (1051372, 4500, 'Outfits'), /*Splash Choco Girl*/
    (1051373, 4500, 'Outfits'), /*Bloody Jeanne*/
    (1051374, 4500, 'Outfits'), /*Odette Tutu*/
    (1051375, 4500, 'Outfits'), /*Odile Tutu*/
    (1051376, 4500, 'Outfits'), /*Halloweenroid Dress*/
    (1051382, 4500, 'Outfits'), /*Lovely Shopper*/
    (1051383, 4500, 'Outfits'), /*Moonlight Outfit*/
    (1051384, 4500, 'Outfits'), /*Glory Pony*/
    (1051385, 4500, 'Outfits'), /*Plop! Chocolate Girl*/
    (1051386, 4500, 'Outfits'), /*Blue Shiny Dress*/
    (1051389, 4500, 'Outfits'), /*White Fiancee*/
    (1051390, 4500, 'Outfits'), /*Forsythia Picnic*/
    (1051391, 4500, 'Outfits'), /*Icy Dress*/
    (1051392, 4500, 'Outfits'), /*Party Princess*/
    (1051405, 4500, 'Outfits'), /*Melody Girl*/
    (1051407, 4500, 'Outfits'), /*Maple Leaf High Swimsuit (F)*/
    (1051408, 4500, 'Outfits'), /*Shiny Light*/
    (1051409, 4500, 'Outfits'), /*Gentle Ice Girl*/
    (1051411, 4500, 'Outfits'), /*Lady Rosalia*/
    (1051420, 4500, 'Outfits'), /*Ribbon Angel Uniform*/
    (1051422, 4500, 'Outfits'), /*Sweet Patissiere*/
    (1051425, 4500, 'Outfits'), /*Big Ribbon Yellow Dress*/
    (1051426, 4500, 'Outfits'), /*Gothic Girls Uniform*/
    (1051429, 4500, 'Outfits'), /*Sweet Snow Flower*/
    (1051430, 4500, 'Outfits'), /*Sierra Grace Dress*/
    (1051431, 4500, 'Outfits'), /*Ms. Love Messenger Outfit*/
    (1051432, 4500, 'Outfits'), /*Little Trainer Outfit (F)*/
    (1051434, 4500, 'Outfits'), /*Blooming Leafy Love Outfit*/
    (1051435, 4500, 'Outfits'), /*Starlight Girl Outfit*/
    (1051436, 4500, 'Outfits'), /*The Kingdom Dress of Queen*/
    (1051437, 4500, 'Outfits'), /*Pink Romance*/
    (1051440, 4500, 'Outfits'), /*Pink Sapphire*/
    (1051441, 4500, 'Outfits'), /*Bunny Romper*/
    (1051442, 4500, 'Outfits'), /*Momo Maid Dress*/
    (1051452, 4500, 'Outfits'), /*Orange Day*/
    (1051455, 4500, 'Outfits'), /*Time Cantabile*/
    (1051456, 4500, 'Outfits'), /*Sparkling Bluebird (F)*/
    (1051457, 4500, 'Outfits'), /*British Marine Outfit (F)*/
    (1051458, 4500, 'Outfits'), /*Ursus Lady's Suit*/
    (1051459, 4500, 'Outfits'), /*Pure Farmer One-piece*/
    (1051460, 4500, 'Outfits'), /*Polka Dot Dress*/
    (1051461, 4500, 'Outfits'), /*Giant Ribbon Outfit (F)*/
    (1052042, 4500, 'Outfits'), /*Pink Robot Pilotgear*/
    (1051463, 4500, 'Outfits'), /*Bloody Bride*/
    (1051465, 4500, 'Outfits'), /*Shy Traveler*/
    (1051466, 4500, 'Outfits'), /*Blue Marine Uniform (F)*/
    (1052050, 4500, 'Outfits'), /*Red Hip Hop*/
    (1052051, 4500, 'Outfits'), /*Blue Hip Hop*/
    (1051470, 4500, 'Outfits'), /*Time Mistress*/
    (1052045, 4500, 'Outfits'), /*Mink Coat*/
    (1052048, 4500, 'Outfits'), /*Brown Snowboard Overall*/
    (1052046, 4500, 'Outfits'), /*Snowman Costume*/
    (1052047, 4500, 'Outfits'), /*Black Snowboard Overall*/
    (1052040, 4500, 'Outfits'), /*Cao Cao Robe*/
    (1052041, 4500, 'Outfits'), /*Sun Quan Robe*/
    (1051483, 4500, 'Outfits'), /*Frilly Bathrobe*/
    (1051486, 4500, 'Outfits'), /*Snow Deer Parka*/
    (1052044, 4500, 'Outfits'), /*Scuba Diving Suit*/
    (1051490, 4500, 'Outfits'), /*Concert Muse (Soprano)*/
    (1052000, 4500, 'Outfits'), /*Recycled Box*/
    (1052002, 4500, 'Outfits'), /*Cardboard Box*/
    (1052003, 4500, 'Outfits'), /*Blue Chinese Undead Costume*/
    (1052004, 4500, 'Outfits'), /*Maroon Chinese Undead Costume*/
    (1052005, 4500, 'Outfits'), /*Yellow Raincoat*/
    (1052006, 4500, 'Outfits'), /*Sky Blue Raincoat*/
    (1052007, 4500, 'Outfits'), /*Red Raincoat*/
    (1052008, 4500, 'Outfits'), /*Green Raincoat*/
    (1052009, 4500, 'Outfits'), /*Orange Overall*/
    (1052010, 4500, 'Outfits'), /*Pink Overall*/
    (1052011, 4500, 'Outfits'), /*Blue Overall*/
    (1052012, 4500, 'Outfits'), /*Green Overall*/
    (1052013, 4500, 'Outfits'), /*Graduation Gown*/
    (1052014, 4500, 'Outfits'), /*Ducky Costume*/
    (1052015, 4500, 'Outfits'), /*Blue Shinsengumi Uniform*/
    (1052016, 4500, 'Outfits'), /*Brown Shinsengumi Uniform*/
    (1052017, 4500, 'Outfits'), /*Orange Life-Jacket*/
    (1052018, 4500, 'Outfits'), /*Green Life-Jacket*/
    (1052019, 4500, 'Outfits'), /*Blue Life-Jacket*/
    (1052020, 4500, 'Outfits'), /*White Body Tights*/
    (1052021, 4500, 'Outfits'), /*Black Body Tights*/
    (1052022, 4500, 'Outfits'), /*White Holed Tights*/
    (1052023, 4500, 'Outfits'), /*Black Holed Tights*/
    (1052024, 4500, 'Outfits'), /*Big Kimono*/
    (1052025, 4500, 'Outfits'), /*Denim Overall*/
    (1052026, 4500, 'Outfits'), /*Grey Full Coat*/
    (1052027, 4500, 'Outfits'), /*Red Full Coat*/
    (1052028, 4500, 'Outfits'), /*Forest Samurai Outfit*/
    (1052029, 4500, 'Outfits'), /*Premium Trenchcoat*/
    (1052030, 4500, 'Outfits'), /*Toga*/
    (1052031, 4500, 'Outfits'), /*Reindeer Suit*/
    (1052032, 4500, 'Outfits'), /*Red Bruma*/
    (1052033, 4500, 'Outfits'), /*Green Bruma*/
    (1052034, 4500, 'Outfits'), /*Blue Bruma*/
    (1052035, 4500, 'Outfits'), /*Guan Yu Armor*/
    (1052036, 4500, 'Outfits'), /*Zhu-Ge-Liang Gown*/
    (1052037, 4500, 'Outfits'), /*Patissier Uniform*/
    (1052038, 4500, 'Outfits'), /*Blue Robot Pilotgear*/
    (1052039, 4500, 'Outfits'), /*Liu Bei Robe*/
    (1052043, 4500, 'Outfits'), /*Hip Hop Sweats*/
    (1052052, 4500, 'Outfits'), /*Musashi Costume*/
    (1052053, 4500, 'Outfits'), /*Teddy Bear Costume*/
    (1052054, 4500, 'Outfits'), /*Welder Look*/
    (1052055, 4500, 'Outfits'), /*Enamer*/
    (1052056, 4500, 'Outfits'), /*Soccer Uniform*/
    (1052057, 4500, 'Outfits'), /*Soccer Uniform (No.7)*/
    (1052058, 4500, 'Outfits'), /*Soccer Uniform (No.10)*/
    (1052059, 4500, 'Outfits'), /*Soccer Uniform (No.14)*/
    (1052060, 4500, 'Outfits'), /*England Soccer Uniform(No.8)*/
    (1052061, 4500, 'Outfits'), /*Brazil Soccer Uniform(No.9)*/
    (1052062, 4500, 'Outfits'), /*France Soccer Uniform(No.10)*/
    (1052063, 4500, 'Outfits'), /*USA Soccer Uniform(No.17)*/
    (1052064, 4500, 'Outfits'), /*Soccer Uniform(No.4)*/
    (1052065, 4500, 'Outfits'), /*Soccer Uniform(No.21)*/
    (1052066, 4500, 'Outfits'), /*Mexico Soccer Uniform(No.9)*/
    (1052067, 4500, 'Outfits'), /*Mummy Suit*/
    (1052068, 4500, 'Outfits'), /*Skull Suit*/
    (1052069, 4500, 'Outfits'), /*Flamboyant Autumn Gear*/
    (1052070, 4500, 'Outfits'), /*Golden Armor*/
    (1052073, 4500, 'Outfits'), /*White Rabbit Suit*/
    (1052074, 4500, 'Outfits'), /*Nero Bell Outfit*/
    (1052077, 4500, 'Outfits'), /*Moon Bunny Costume*/
    (1052078, 4500, 'Outfits'), /*Soap Bubble Bonanza*/
    (1052079, 4500, 'Outfits'), /*Prince of Darkness*/
    (1052082, 4500, 'Outfits'), /*Elf Overall*/
    (1052083, 4500, 'Outfits'), /*Sun Wukong Robe*/
    (1052085, 4500, 'Outfits'), /*Red Amorian Apron*/
    (1052086, 4500, 'Outfits'), /*Blue Amorian Apron*/
    (1052087, 4500, 'Outfits'), /*Dark Blue Kimono*/
    (1052089, 4500, 'Outfits'), /*Black Overcoat of Doom*/
    (1052090, 4500, 'Outfits'), /*Rompers*/
    (1052091, 4500, 'Outfits'), /*Sachiel Armor*/
    (1052092, 4500, 'Outfits'), /*Veamoth Armor*/
    (1052093, 4500, 'Outfits'), /*Janus Armor*/
    (1052094, 4500, 'Outfits'), /*Zhu Ba Jie Overall*/
    (1052135, 4500, 'Outfits'), /*Centaurus Legs*/
    (1052136, 4500, 'Outfits'), /*2nd Anniversary Mushroom Suit*/
    (1052137, 4500, 'Outfits'), /*Tomato Suit*/
    (1052142, 4500, 'Outfits'), /*Shorts with Suspenders*/
    (1052143, 4500, 'Outfits'), /*Sky Blue Padded Coat*/
    (1052144, 4500, 'Outfits'), /*Luxurious Padded Coat*/
    (1052145, 4500, 'Outfits'), /*Christmas Party Suit*/
    (1052147, 4500, 'Outfits'), /*Chinese Lion Costume*/
    (1052151, 4500, 'Outfits'), /*Bosshunter Armor*/
    (1052152, 4500, 'Outfits'), /*Bosshunter Gi*/
    (1052153, 4500, 'Outfits'), /*Red Viska for Transformation*/
    (1052154, 4500, 'Outfits'), /*Tiger Cub Outfit*/
    (1052168, 4500, 'Outfits'), /*Cutie Birk Outfit*/
    (1052169, 4500, 'Outfits'), /*Gaga Suit*/
    (1052170, 4500, 'Outfits'), /*Noob Overall*/
    (1052171, 4500, 'Outfits'), /*Baby Chick Apron*/
    (1052172, 4500, 'Outfits'), /*Pumpkin Suit*/
    (1052174, 4500, 'Outfits'), /*Fox Outfit*/
    (1052175, 4500, 'Outfits'), /*Coastal Winter Wear*/
    (1052176, 4500, 'Outfits'), /*Fashionable Checkerwear*/
    (1052178, 4500, 'Outfits'), /*Snowflake Knit*/
    (1052179, 4500, 'Outfits'), /*Cow Costume*/
    (1052180, 4500, 'Outfits'), /*Denim Overalls*/
    (1052182, 4500, 'Outfits'), /*Galactic Hero Suit*/
    (1052183, 4500, 'Outfits'), /*Stealth Suit*/
    (1052724, 4500, 'Outfits'), /*Cutie Horse Suit*/
    (1052192, 4500, 'Outfits'), /*Bombacha*/
    (1052193, 4500, 'Outfits'), /*Honeybee Costume*/
    (1052194, 4500, 'Outfits'), /*Ugabuga*/
    (1052195, 4500, 'Outfits'), /*Aran Armor*/
    (1052754, 4500, 'Outfits'), /*Pink Panda Outfit*/
    (1052197, 4500, 'Outfits'), /*Brave Soldier Armor*/
    (1052198, 4500, 'Outfits'), /*Pink Bean Suit*/
    (1052199, 4500, 'Outfits'), /*Blade Overall*/
    (1052200, 4500, 'Outfits'), /*Lolli Pink Suit*/
    (1052201, 4500, 'Outfits'), /*Shiny Sailor Uniform*/
    (1052203, 4500, 'Outfits'), /*One Summer Night */
    (1052204, 4500, 'Outfits'), /*Marine Girl Dress*/
    (1052205, 4500, 'Outfits'), /*Pluto Hero Suit*/
    (1052207, 4500, 'Outfits'), /*Cursed Golden Armor*/
    (1052209, 4500, 'Outfits'), /*Royal Navy Uniform*/
    (1052210, 4500, 'Outfits'), /*Alchemist Overall*/
    (1052211, 4500, 'Outfits'), /*Fire Shadow Suit*/
    (1052212, 4500, 'Outfits'), /*Cherry Blossom Suit*/
    (1052213, 4500, 'Outfits'), /*Chaos Armor*/
    (1052214, 4500, 'Outfits'), /*Maple Racing Suit*/
    (1052218, 4500, 'Outfits'), /*Clown Suit*/
    (1052224, 4500, 'Outfits'), /*Strawberry Baby*/
    (1052225, 4500, 'Outfits'), /*Lolita Butterfly Dress*/
    (1052228, 4500, 'Outfits'), /*Layered Long Skull Tee*/
    (1052229, 4500, 'Outfits'), /*Qi-pao Dress*/
    (1052231, 4500, 'Outfits'), /*Little Prince*/
    (1052232, 4500, 'Outfits'), /*Pink Fur Ribbon Dress*/
    (1052233, 4500, 'Outfits'), /*White Fur Ribbon Dress*/
    (1052234, 4500, 'Outfits'), /*Stylish Layered Plaid*/
    (1052236, 4500, 'Outfits'), /*Petite School Shawl*/
    (1052245, 4500, 'Outfits'), /*Green Leaf Overall*/
    (1052246, 4500, 'Outfits'), /*Cat Set Suit*/
    (1052248, 4500, 'Outfits'), /*Dual Blade Suit*/
    (1052253, 4500, 'Outfits'), /*Green Overall Shorts*/
    (1052255, 4500, 'Outfits'), /*Hawkeye Captain Suit*/
    (1052275, 4500, 'Outfits'), /*Royal Rainbow Zip-Up Jacket*/
    (1052282, 4500, 'Outfits'), /*Oz Magic Robe*/
    (1052283, 4500, 'Outfits'), /*Henesys Academy Uniform (with skirt)*/
    (1052284, 4500, 'Outfits'), /*Henesys Academy Uniform (with pants)*/
    (1052286, 4500, 'Outfits'), /*Pilot Suit*/
    (1052289, 4500, 'Outfits'), /*Wild Hunter Suit*/
    (1052290, 4500, 'Outfits'), /*Battle Mage Suit*/
    (1052291, 4500, 'Outfits'), /*Hooded Track Suit*/
    (1052292, 4500, 'Outfits'), /*King Crow Suit*/
    (1052293, 4500, 'Outfits'), /*Sanctus Combat Dress*/
    (1052294, 4500, 'Outfits'), /*Sanctus Combat Suit*/
    (1052295, 4500, 'Outfits'), /*Maid Dress (Pink)*/
    (1052296, 4500, 'Outfits'), /*Maid Dress (Blue)*/
    (1052298, 4500, 'Outfits'), /*Rabbit Ear Dress*/
    (1052306, 4500, 'Outfits'), /*Japanesque Dress*/
    (1052309, 4500, 'Outfits'), /*Trench Coat*/
    (1052324, 4500, 'Outfits'), /*Paypal Suit*/
    (1052329, 4500, 'Outfits'), /*Pitch Dark Outfit*/
    (1052330, 4500, 'Outfits'), /*Blue Mage Gear*/
    (1052331, 4500, 'Outfits'), /*Red Mage Gear*/
    (1052332, 4500, 'Outfits'), /*Christmas Casual Outfit*/
    (1052338, 4500, 'Outfits'), /*Red's Dress*/
    (1052339, 4500, 'Outfits'), /*Lab Gear (F)*/
    (1052340, 4500, 'Outfits'), /*Lab Gear (M)*/
    (1052746, 4500, 'Outfits'), /*Chef Overall*/
    (1052345, 4500, 'Outfits'), /*Winter 2010 Moon Bunny Outfit*/
    (1052348, 4500, 'Outfits'), /*Commander Captain*/
    (1052349, 4500, 'Outfits'), /*Belt Coat*/
    (1052354, 4500, 'Outfits'), /*Rising Star Baggy Digs*/
    (1052355, 4500, 'Outfits'), /*Rookie Yellow Raincoat*/
    (1052367, 4500, 'Outfits'), /*Crow Suit*/
    (1052368, 4500, 'Outfits'), /*Starling Suit*/
    (1052369, 4500, 'Outfits'), /* MSE 4 Years & Unstoppable Overall*/
    (1052370, 4500, 'Outfits'), /*Victorian Vampire Suit*/
    (1052372, 4500, 'Outfits'), /*Blue Arabian Outfit*/
    (1052373, 4500, 'Outfits'), /*Red Arabian Outfit*/
    (1052408, 4500, 'Outfits'), /*Kerning Engineering School Uniform*/
    (1052410, 4500, 'Outfits'), /*Ribboned Justice Dress*/
    (1052412, 4500, 'Outfits'), /*Toy Prince*/
    (1052416, 4500, 'Outfits'), /*Orchid's Black Wing Uniform*/
    (1052417, 4500, 'Outfits'), /*Honeybee Suit*/
    (1052418, 4500, 'Outfits'), /*Princely Daywear*/
    (1052419, 4500, 'Outfits'), /*Pink Lolita Outfit*/
    (1052421, 4500, 'Outfits'), /*Urban Pirate Outfit*/
    (1052423, 4500, 'Outfits'), /*Hades Overall*/
    (1052424, 4500, 'Outfits'), /*Fancy Noblesse Robe*/
    (1052425, 4500, 'Outfits'), /*White Combat Tunic*/
    (1052426, 4500, 'Outfits'), /*White Combat Habit*/
    (1052435, 4500, 'Outfits'), /*Princess Hakama*/
    (1052438, 4500, 'Outfits'), /*Ganache Chocolate Suit*/
    (1052439, 4500, 'Outfits'), /*Ellinia Magic School Uniform*/
    (1052440, 4500, 'Outfits'), /*Mu Lung Dojo Uniform*/
    (1052442, 4500, 'Outfits'), /*Combat Fatigues*/
    (1052443, 4500, 'Outfits'), /*Taisho Romance*/
    (1052445, 4500, 'Outfits'), /*Intergalactic Armor*/
    (1052448, 4500, 'Outfits'), /*Tomato Outfit*/
    (1052449, 4500, 'Outfits'), /*Sausage Outfit*/
    (1052458, 4500, 'Outfits'), /*Lucia Overall*/
    (1052459, 4500, 'Outfits'), /*Blue Angel Uniform*/
    (1052474, 4500, 'Outfits'), /*The Onmyouji Ceremonial Robes*/
    (1052503, 4500, 'Outfits'), /*Cool Summer Look*/
    (1052531, 4500, 'Outfits'), /*The Bladed Falcon's Armor*/
    (1052536, 4500, 'Outfits'), /*Marine Stripe*/
    (1052537, 4500, 'Outfits'), /*Hyper Honeybee Suit*/
    (1052542, 4500, 'Outfits'), /*Angel Costume*/
    (1052544, 4500, 'Outfits'), /*Hyper Rising Star Baggy Digs*/
    (1052550, 4500, 'Outfits'), /*Seal Costume*/
    (1052551, 4500, 'Outfits'), /*Yellow Bell Robe*/
    (1052552, 4500, 'Outfits'), /*Gray Bell Robe*/
    (1052554, 4500, 'Outfits'), /*Cat Lolita Outfit*/
    (1052568, 4500, 'Outfits'), /*Decked-Out Santa Outfit*/
    (1052571, 4500, 'Outfits'), /*Dark Devil Outfit*/
    (1052574, 4500, 'Outfits'), /*Flowing Flame Robes*/
    (1052575, 4500, 'Outfits'), /*Pious Shaman Robes*/
    (1052576, 4500, 'Outfits'), /*Lotus's Black Wings Uniform*/
    (1052579, 4500, 'Outfits'), /*Xenon Neo-Tech Suit*/
    (1052586, 4500, 'Outfits'), /*Lucia Outfit*/
    (1052587, 4500, 'Outfits'), /*Harp Seal Doll Outfit*/
    (1052594, 4500, 'Outfits'), /*Green Dinosaur Overall*/
    (1052595, 4500, 'Outfits'), /*Purple Dinosaur Overall*/
    (1052601, 4500, 'Outfits'), /*Kerning Technical High Uniform*/
    (1052602, 4500, 'Outfits'), /*Black Duster*/
    (1052603, 4500, 'Outfits'), /*Mu Lung Academy Uniform*/
    (1052604, 4500, 'Outfits'), /*Blue Love Dress*/
    (1052605, 4500, 'Outfits'), /*Ramling PJs*/
    (1052618, 4500, 'Outfits'), /*Blue Kitty Hoodie*/
    (1052619, 4500, 'Outfits'), /*Pink Kitty Hoodie*/
    (1052624, 4500, 'Outfits'), /*GM Haku's Pirate Gear*/
    (1052626, 4500, 'Outfits'), /*Splash Wave*/
    (1052627, 4500, 'Outfits'), /*Pirate Captain's Coat*/
    (1052634, 4500, 'Outfits'), /*Man's Shirts*/
    (1052644, 4500, 'Outfits'), /*Shadow Executer*/
    (1052654, 4500, 'Outfits'), /*フューチャーロイドネオンアーマー*/
    (1052655, 4500, 'Outfits'), /*Dawn Bear Outfit*/
    (1052656, 4500, 'Outfits'), /*White Swan Ballet Outfit*/
    (1052657, 4500, 'Outfits'), /*Black Swan Ballet Outfit*/
    (1052661, 4500, 'Outfits'), /*Chicken Coataroo*/
    (1052662, 4500, 'Outfits'), /*Camellia Flower Lovely Night Clothes*/
    (1052663, 4500, 'Outfits'), /*Flowing Wind Robe*/
    (1052664, 4500, 'Outfits'), /*Gentleman Bow Tie Suit*/
    (1052665, 4500, 'Outfits'), /*Gentleman Suit*/
    (1052666, 4500, 'Outfits'), /*Chocoram Doll Outfit*/
    (1052667, 4500, 'Outfits'), /*Puffram Onesie*/
    (1052668, 4500, 'Outfits'), /*Princess of Time Dress*/
    (1052671, 4500, 'Outfits'), /*Oversized Oxford*/
    (1052675, 4500, 'Outfits'), /*Vampire Phantom Suit*/
    (1052676, 4500, 'Outfits'), /*Kirito's Outfit*/
    (1052677, 4500, 'Outfits'), /*Asuna's Dress*/
    (1052678, 4500, 'Outfits'), /*Leafa's Dress*/
    (1052679, 4500, 'Outfits'), /*Freud's Robe*/
    (1052680, 4500, 'Outfits'), /*Aran's Armor(M)*/
    (1052681, 4500, 'Outfits'), /*Aran's Armor(F)*/
    (1052682, 4500, 'Outfits'), /*Brave Aran's Armor*/
    (1052684, 4500, 'Outfits'), /*Pumpkin Bat Outfit*/
    (1052685, 4500, 'Outfits'), /*Yui's Dress*/
    (1052692, 4500, 'Outfits'), /*Mr. K's Cat Outfit*/
    (1052693, 4500, 'Outfits'), /*Rudi's Outfit*/
    (1052747, 4500, 'Outfits'), /*Contemporary Chic Outfit*/
    (1052749, 4500, 'Outfits'), /*Nurse Dress*/
    (1052750, 4500, 'Outfits'), /*Doctor Suit*/
    (1052725, 4500, 'Outfits'), /*Fancy Magician Overall*/
    (1052726, 4500, 'Outfits'), /*Ghost Bride Wedding Dress*/
    (1052727, 4500, 'Outfits'), /*Refreshing Male Outfit*/
    (1052728, 4500, 'Outfits'), /*Refreshing Female Cardigan Outfit*/
    (1052762, 4500, 'Outfits'), /*Banana Overalls*/
    (1052771, 4500, 'Outfits'), /*Ayame Overall Armor*/
    (1052772, 4500, 'Outfits'), /*2014 유니폼*/
    (1052773, 4500, 'Outfits'), /*暗夜精灵铠甲*/
    (1052774, 4500, 'Outfits'), /*隐武士铠甲*/
    (1052779, 4500, 'Outfits'), /*Peach Camellia Kimono*/
    (1052780, 4500, 'Outfits'), /*Red Wind Robes*/
    (1052781, 4500, 'Outfits'), /*Red Pony Overalls*/
    (1052782, 4500, 'Outfits'), /*Blue Pony Overalls*/
    (1052811, 4500, 'Outfits'), /*Bright Angel Coat*/
    (1052812, 4500, 'Outfits'), /*Dark Devil Coat*/
    (1052837, 4500, 'Outfits'), /*Gym Teacher's Suit*/
    (1052838, 4500, 'Outfits'), /*Student Swimsuit*/
    (1052841, 4500, 'Outfits'), /*Sweet Persimmon Suit*/
    (1052842, 4500, 'Outfits'), /*White Puppy Outfit*/
    (1052843, 4500, 'Outfits'), /*Brown Puppy Outfit*/
    (1052844, 4500, 'Outfits'), /*Corn Overalls*/
    (1052845, 4500, 'Outfits'), /*Loose-fit Homecoming Duds*/
    (1052846, 4500, 'Outfits'), /*Peach Overalls*/
    (1052852, 4500, 'Outfits'), /*Raging Lotus Gown*/
    (1052853, 4500, 'Outfits'), /*Ill Orchid Gown*/
    (1052854, 4500, 'Outfits'), /*Worn Ghost Suit*/
    (1052855, 4500, 'Outfits'), /*Worn Witch Outfit*/
    (1052856, 4500, 'Outfits'), /*Worn Skull Outfit*/
    (1052858, 4500, 'Outfits'), /*Witch Outfit*/
    (1052859, 4500, 'Outfits'), /*Skull Outfit*/
    (1052865, 4500, 'Outfits'), /*Dinofrog Outfit*/
    (1052870, 4500, 'Outfits'), /*Cadet Corps Uniform*/
    (1052871, 4500, 'Outfits'), /*Scout Regiment Uniform*/
    (1052872, 4500, 'Outfits'), /*Red Ribbon Dress*/
    (1052873, 4500, 'Outfits'), /*Mikasa's Scout Regiment Uniform*/
    (1052874, 4500, 'Outfits'), /*Levi's Scout Regiment Uniform*/
    (1052876, 4500, 'Outfits'), /*Eren's Scout Regiment Uniform*/
    (1052891, 4500, 'Outfits'), /*Blue Bird Overall*/
    (1052892, 4500, 'Outfits'), /*Blushing Bunny Suspenders*/
    (1052894, 4500, 'Outfits'), /*Romantic Dress*/
    (1052895, 4500, 'Outfits'), /*Silver Wolf Outfit*/
    (1052899, 4500, 'Outfits'), /*Black Mouse Hooded Onesie*/
    (1052901, 4500, 'Outfits'), /*Hipster*/
    (1052902, 4500, 'Outfits'), /*Jumpsuit*/
    (1052903, 4500, 'Outfits'), /*White Servant Tux*/
    (1052904, 4500, 'Outfits'), /*Lovely Princess Dress*/
    (1052909, 4500, 'Outfits'), /*Honeybee Coat*/
    (1052910, 4500, 'Outfits'), /*Crystal Cat Outfit (M)*/
    (1052911, 4500, 'Outfits'), /*Crystal Cat Outfit (F)*/
    (1052912, 4500, 'Outfits'), /*Quilting Fashion King*/
    (1052916, 4500, 'Outfits'), /*Akarin's Flowery Dress*/
    (1052917, 4500, 'Outfits'), /*Akatsuki's Dark Suit*/
    (1052920, 4500, 'Outfits'), /*Red Mouse Hooded Onesie*/
    (1052921, 4500, 'Outfits'), /*Bubbly Blue Carp Outfit*/
    (1052922, 4500, 'Outfits'), /*Bubbly Red Carp Outfit*/
    (1052923, 4500, 'Outfits'), /*Feline Blue Sleeves*/
    (1052924, 4500, 'Outfits'), /*Noble Blossom Coat*/
    (1052925, 4500, 'Outfits'), /*Pink Blossom Dress*/
    (1052926, 4500, 'Outfits'), /*Cottontail Rabbit Dress*/
    (1052939, 4500, 'Outfits'), /*돼지바 탱글딸기*/
    (1052940, 4500, 'Outfits'), /*Spring Sunlight Pullover*/
    (1052941, 4500, 'Outfits'), /*Dark Lotus Uniform*/
    (1052942, 4500, 'Outfits'), /*Blue Panda Doll Outfit*/
    (1052946, 4500, 'Outfits'), /*Schwarzer Cross*/
    (1052948, 4500, 'Outfits'), /*Evening Orchid*/
    (1052949, 4500, 'Outfits'), /*Haku Cloth*/
    (1052954, 4500, 'Outfits'), /*Deep Sky*/
    (1052955, 4500, 'Outfits'), /*Assistant Chef Outfit*/
    (1052956, 4500, 'Outfits'), /*Beginner Chef Outfit*/
    (1052957, 4500, 'Outfits'), /*Intermediate Chef Outfit*/
    (1052958, 4500, 'Outfits'), /*Advanced Chef Outfit*/
    (1052959, 4500, 'Outfits'), /*Sous-Chef Outfit*/
    (1052960, 4500, 'Outfits'), /*Chef Outfit*/
    (1052965, 4500, 'Outfits'), /*Black Sailor Dress*/
    (1052966, 4500, 'Outfits'), /*Hilla Android Uniform*/
    (1052967, 4500, 'Outfits'), /*Magnus Android Uniform*/
    (1052975, 4500, 'Outfits'), /*Preppy Suspenders*/
    (1052976, 4500, 'Outfits'), /*Clear Blue*/
    (1052977, 4500, 'Outfits'), /*Pink Cardigan*/
    (1052994, 4500, 'Outfits'), /*Abyss Burgunt*/
    (1052995, 4500, 'Outfits'), /*Nyanya Steward Tuxedo*/
    (1052996, 4500, 'Outfits'), /*Undertaker*/
    (1052998, 4500, 'Outfits'), /*Show me the Meso*/
    (1052999, 4500, 'Outfits'), /*Polar Fur-Trimmed Dress*/
    (1053000, 4500, 'Outfits'), /*Enari's Cow Outfit*/
    (1053001, 4500, 'Outfits'), /*Flutter-sleeve Bell Suit*/
    (1053006, 4500, 'Outfits'), /*Yarn Bunny Outfit*/
    (1053018, 4500, 'Outfits'), /*Beaky Owl Outfit*/
    (1053022, 4500, 'Outfits'), /*Umbral Attire*/
    (1053023, 4500, 'Outfits'), /*Umbral Coat*/
    (1053024, 4500, 'Outfits'), /*Flower Dancer's Dress*/
    (1053025, 4500, 'Outfits'), /*Moon Dancer's Attire*/
    (1053028, 4500, 'Outfits'), /*Baby Binkie Spacesuit*/
    (1053033, 4500, 'Outfits'), /*Damien Coat*/
    (1053034, 4500, 'Outfits'), /*Alicia Dress*/
    (1053040, 4500, 'Outfits'), /*Shark Bodysuit*/
    (1053041, 4500, 'Outfits'), /*Blue Phoenix Toga*/
    (1053042, 4500, 'Outfits'), /*Red Phoenix Toga*/
    (1053045, 4500, 'Outfits'), /*Kitty Overall (Male)*/
    (1053046, 4500, 'Outfits'), /*Winged Kitty Dress (Female)*/
    (1053047, 4500, 'Outfits'), /*Mischievous Sweet Pig Outfit*/
    (1053048, 4500, 'Outfits'), /*Cunning Sweet Pig Outfit*/
    (1053051, 4500, 'Outfits'), /*Chicken Cutie Outfit*/
    (1053052, 4500, 'Outfits'), /*Bubble Leaf Pants*/
    (1053053, 4500, 'Outfits'), /*Bubble Leaf Skirt*/
    (1053056, 4500, 'Outfits'), /*Blaster Outfit (M)*/
    (1053057, 4500, 'Outfits'), /*Blaster Outfit (F)*/
    (1053058, 4500, 'Outfits'), /*Sky-blue Overalls*/
    (1053059, 4500, 'Outfits'), /*Villain's Cool Tights (Outfit)*/
    (1053060, 4500, 'Outfits'), /*Colorful Bikini*/
    (1053061, 4500, 'Outfits'), /*Colorful Beach Pants*/
    (1053083, 4500, 'Outfits'), /*Super Miracle Cube Outfit*/
    (1053084, 4500, 'Outfits'), /*Violet Cube Outfit*/
    (1053085, 4500, 'Outfits'), /*Black Cube Outfit*/
    (1053086, 4500, 'Outfits'), /*Kamaitachi Outfit*/
    (1053087, 4500, 'Outfits'), /*Formal Brown Shorts*/
    (1053088, 4500, 'Outfits'), /*Formal Brown Skirt*/
    (1053089, 4500, 'Outfits'), /*Moon Bunny Outfit (M)*/
    (1053090, 4500, 'Outfits'), /*Moon Bunny Outfit (F)*/
    (1053091, 4500, 'Outfits'), /*Dark Musician Coat*/
    (1053092, 4500, 'Outfits'), /*Chained Princess Coat*/
    (1053093, 4500, 'Outfits'), /*Halloween Festival Costume (M)*/
    (1053094, 4500, 'Outfits'), /*Halloween Festival Costume (F)*/
    (1053095, 4500, 'Outfits'), /*Bichon Outfit*/

/* Tops */
    (1040001, 3000, 'Tops'), /*Black Blazer*/
    (1040005, 3000, 'Tops'), /*Orange Baseball Jacket*/
    (1040027, 3000, 'Tops'), /*Old School Blazer*/
    (1040045, 3000, 'Tops'), /*Red Rider*/
    (1040046, 3000, 'Tops'), /*Shine Rider*/
    (1040047, 3000, 'Tops'), /*Dark Rider*/
    (1040051, 3000, 'Tops'), /*Blue Striped Trainer*/
    (1040052, 3000, 'Tops'), /*Green Striped Trainer*/
    (1040053, 3000, 'Tops'), /*Orange Striped Trainer*/
    (1040054, 3000, 'Tops'), /*Green Disco Shirt*/
    (1040055, 3000, 'Tops'), /*Orange Disco Shirt*/
    (1040056, 3000, 'Tops'), /*Original Disco Shirt*/
    (1040064, 3000, 'Tops'), /*Wild Top*/
    (1040065, 3000, 'Tops'), /*Brown Wild Top*/
    (1040066, 3000, 'Tops'), /*Red Wild Top*/
    (1040077, 3000, 'Tops'), /*Cowboy Top*/
    (1040078, 3000, 'Tops'), /*Pre-School Uniform Top*/
    (1040101, 3000, 'Tops'), /*Skull T-Shirt*/
    (1040114, 3000, 'Tops'), /*Hawaiian Shirt*/
    (1040119, 3000, 'Tops'), /*Ragged Top*/
    (1040123, 3000, 'Tops'), /*Prep School Uniform*/
    (1040124, 3000, 'Tops'), /*Crusader T-Shirt*/
    (1040125, 3000, 'Tops'), /*Military Cargo Jacket*/
    (1040126, 3000, 'Tops'), /*Yellow Frill Sleeveless*/
    (1040127, 3000, 'Tops'), /*Blue Heart Tanktop*/
    (1040128, 3000, 'Tops'), /*Blue Line Tanktop*/
    (1040129, 3000, 'Tops'), /*Red Casual Suit*/
    (1040130, 3000, 'Tops'), /*Green Tie Casual Suit*/
    (1040131, 3000, 'Tops'), /*Pink Tie Casual Suit*/
    (1040132, 3000, 'Tops'), /*Palm Tree Tanktop*/
    (1040133, 3000, 'Tops'), /*Long Blue Shirt*/
    (1040134, 3000, 'Tops'), /*Orange Puffy Jacket*/
    (1040135, 3000, 'Tops'), /*Muscle Man T*/
    (1040137, 3000, 'Tops'), /*Tania Tailored Jacket*/
    (1040138, 3000, 'Tops'), /*Mercury Leather Jacket (M)*/
    (1040139, 3000, 'Tops'), /*Island Beads (M)*/
    (1040140, 3000, 'Tops'), /*Pink Mimi Blouse*/
    (1040141, 3000, 'Tops'), /*Blue Sailor Shirt*/
    (1040143, 3000, 'Tops'), /*Pink Top*/
    (1040144, 3000, 'Tops'), /*Bulletproof Vest*/
    (1040148, 3000, 'Tops'), /*Retro School Uniform Jacket*/
    (1040154, 3000, 'Tops'), /*Pre-School Top*/
    (1040186, 3000, 'Tops'), /*Cowboy Shirt*/
    (1040192, 3000, 'Tops'), /*Green Bunny T-Shirt*/
    (1040193, 3000, 'Tops'), /*RED T-shirt*/
    (1040194, 3000, 'Tops'), /*Guys Pineapple Tank top*/
    (1040195, 3000, 'Tops'), /*Sleeveless Purple Mustache Shirt (M)*/
    (1040196, 3000, 'Tops'), /*Smile Seed Top*/
    (1041000, 3000, 'Tops'), /*Blue Frill Blouse*/
    (1041009, 3000, 'Tops'), /*Red Sailor Shirt*/
    (1041070, 3000, 'Tops'), /*Sky Blue Mimi Blouse*/
    (1041071, 3000, 'Tops'), /*Yellow Mimi Blouse*/
    (1041104, 3000, 'Tops'), /*Old School Uniform Top*/
    (1041108, 3000, 'Tops'), /*SF Ninja Top*/
    (1041109, 3000, 'Tops'), /*Red Trainer Jacket*/
    (1041110, 3000, 'Tops'), /*Sky Blue Trainer Jacket*/
    (1041111, 3000, 'Tops'), /*Pink Trainer Jacket*/
    (1041112, 3000, 'Tops'), /*Black Trainer Jacket*/
    (1041113, 3000, 'Tops'), /*Pink Frill Pajama Top*/
    (1041125, 3000, 'Tops'), /*Rainbow Knit*/
    (1041127, 3000, 'Tops'), /*Heart Sleeveless*/
    (1041128, 3000, 'Tops'), /*Cross Sleeveless*/
    (1041129, 3000, 'Tops'), /*Yellow Frill Camisole*/
    (1041130, 3000, 'Tops'), /*Blue Frill Camisole*/
    (1041131, 3000, 'Tops'), /*Pink Ribboned Janie*/
    (1041132, 3000, 'Tops'), /*Pink Frill Camisole*/
    (1041133, 3000, 'Tops'), /*Grey Cardigan*/
    (1041134, 3000, 'Tops'), /*Angora Mustang*/
    (1041135, 3000, 'Tops'), /*Tube-Top Jacket*/
    (1041136, 3000, 'Tops'), /*Pink Vest Blouse*/
    (1041137, 3000, 'Tops'), /*Pink-Dotted Top*/
    (1041138, 3000, 'Tops'), /*Tania Bolero*/
    (1041139, 3000, 'Tops'), /*Mercury Leather Jacket (F)*/
    (1041140, 3000, 'Tops'), /*Island Beads (F)*/
    (1041142, 3000, 'Tops'), /*Ribbon Frilled top*/
    (1041146, 3000, 'Tops'), /*Old School Blazer [F]*/
    (1041147, 3000, 'Tops'), /*Muscle Man*/
    (1041189, 3000, 'Tops'), /*Cowgirl Shirt*/
    (1041194, 3000, 'Tops'), /*Pink Bunny T-Shirt*/
    (1041196, 3000, 'Tops'), /*Girls Pineapple Tank top*/
    (1041197, 3000, 'Tops'), /*Pink Mustache T-Shirt (F)*/
    (1042000, 3000, 'Tops'), /*Orange Hooded Vest*/
    (1042001, 3000, 'Tops'), /*Black Hooded Vest*/
    (1042002, 3000, 'Tops'), /*Red Hooded Vest*/
    (1042004, 3000, 'Tops'), /*Pink Hooded Vest*/
    (1042005, 3000, 'Tops'), /*Pink Camping Shirt*/
    (1042006, 3000, 'Tops'), /*Green Camping Shirt*/
    (1042007, 3000, 'Tops'), /*Blue Camping Shirt*/
    (1042008, 3000, 'Tops'), /*Wildcats Baseball Shirt (Basic)*/
    (1042009, 3000, 'Tops'), /*Baseball Shirt (Home)*/
    (1042010, 3000, 'Tops'), /*Baseball Shirt (Away)*/
    (1042011, 3000, 'Tops'), /*Wildcats Baseball Shirt (Alternate)*/
    (1042012, 3000, 'Tops'), /*Yellow Snowboard Top*/
    (1042013, 3000, 'Tops'), /*Green Snowboard Top*/
    (1042014, 3000, 'Tops'), /*Yellow Layered Combo*/
    (1042015, 3000, 'Tops'), /*Blue Layered Combo*/
    (1042016, 3000, 'Tops'), /*Pink Snowboard Top*/
    (1042017, 3000, 'Tops'), /*Sky Blue Snowboard Top*/
    (1042018, 3000, 'Tops'), /*Red T-Shirt w/ Heart*/
    (1042019, 3000, 'Tops'), /*M Layered T-Shirt*/
    (1042020, 3000, 'Tops'), /*Old Military Uniform*/
    (1042021, 3000, 'Tops'), /*Starry Layered Combo*/
    (1042022, 3000, 'Tops'), /*Camouflaged Uniform*/
    (1042023, 3000, 'Tops'), /*Blue Polka-Dot Pajama Top*/
    (1042024, 3000, 'Tops'), /*Red Polka-Dot Pajama Top*/
    (1042025, 3000, 'Tops'), /*Prisoner Top*/
    (1042026, 3000, 'Tops'), /*Flowery Dress Shirt*/
    (1042027, 3000, 'Tops'), /*Blue B-Ball Jersey*/
    (1042028, 3000, 'Tops'), /*Orange B-Ball Jersey*/
    (1042029, 3000, 'Tops'), /*Octopus T-Shirt*/
    (1042030, 3000, 'Tops'), /*Slime T-Shirt*/
    (1042031, 3000, 'Tops'), /*Orange Mushroom T-Shirt*/
    (1042032, 3000, 'Tops'), /*Beetle Longsleeve*/
    (1042033, 3000, 'Tops'), /*Beige Double-Coat*/
    (1042034, 3000, 'Tops'), /*Green Double-Coat*/
    (1042035, 3000, 'Tops'), /*Red Double-Coat*/
    (1042036, 3000, 'Tops'), /*Christmas Padded Jacket*/
    (1042037, 3000, 'Tops'), /*Snowman Padded Jacket*/
    (1042038, 3000, 'Tops'), /*Red Sweater*/
    (1042039, 3000, 'Tops'), /*Sky Blue Allstar*/
    (1042040, 3000, 'Tops'), /*Pink Allstar*/
    (1042041, 3000, 'Tops'), /*Black Allstar*/
    (1042042, 3000, 'Tops'), /*White Hooded Vest*/
    (1042043, 3000, 'Tops'), /*Green Striped Rugby Tee*/
    (1042044, 3000, 'Tops'), /*Pink Striped Rugby Tee*/
    (1042045, 3000, 'Tops'), /*Bowling Shirt*/
    (1042046, 3000, 'Tops'), /*White Casual Suit*/
    (1042047, 3000, 'Tops'), /*Star-Patterned Yellow Shirt*/
    (1042048, 3000, 'Tops'), /*Purple Star Shirt*/
    (1042049, 3000, 'Tops'), /*Short Denim Jacket*/
    (1042050, 3000, 'Tops'), /*Baseball Jumper*/
    (1042051, 3000, 'Tops'), /*Bomber Jacket*/
    (1042052, 3000, 'Tops'), /*Blue Down Parka*/
    (1042053, 3000, 'Tops'), /*Blue Wool Jacket*/
    (1042054, 3000, 'Tops'), /*Pink Wool Jacket*/
    (1042055, 3000, 'Tops'), /*Pink Down Parka*/
    (1042056, 3000, 'Tops'), /*Beat Shirt*/
    (1042058, 3000, 'Tops'), /*Red Half*/
    (1042059, 3000, 'Tops'), /*Preppy Red and White*/
    (1042060, 3000, 'Tops'), /*Pola Sweater*/
    (1042061, 3000, 'Tops'), /*Ball Zone Jumper*/
    (1042062, 3000, 'Tops'), /*Stitched Leather Jacket*/
    (1042063, 3000, 'Tops'), /*Red Turtleneck Sweater*/
    (1042064, 3000, 'Tops'), /*Football Jersey (Home)*/
    (1042065, 3000, 'Tops'), /*Football Top (Away)*/
    (1042066, 3000, 'Tops'), /*Orange Hooded Shirt*/
    (1042067, 3000, 'Tops'), /*Orange Hooded Zip-Up*/
    (1042068, 3000, 'Tops'), /*Drill Muffler*/
    (1042069, 3000, 'Tops'), /*Pink Big-Belt Shirt*/
    (1042070, 3000, 'Tops'), /*Sky Blue Big-Belt Shirt*/
    (1042071, 3000, 'Tops'), /*Pastel Layered Hooded Shirt*/
    (1042072, 3000, 'Tops'), /*Red Layered Hooded Shirt*/
    (1042073, 3000, 'Tops'), /*Navy Blue Dress Shirt*/
    (1042074, 3000, 'Tops'), /*White Longsleeve With Star*/
    (1042075, 3000, 'Tops'), /*Pink Pluto T*/
    (1042076, 3000, 'Tops'), /*Dotted Disco Shirt*/
    (1042077, 3000, 'Tops'), /*Rainbow T*/
    (1042078, 3000, 'Tops'), /*White & Blue Sailor Top*/
    (1042080, 3000, 'Tops'), /*Red Hot Racer T*/
    (1042081, 3000, 'Tops'), /*Cherry Layered T*/
    (1042082, 3000, 'Tops'), /*Black Cardigan Set*/
    (1042083, 3000, 'Tops'), /*Rainbow Hooded Pancho*/
    (1042084, 3000, 'Tops'), /*Army General Hoodie*/
    (1042085, 3000, 'Tops'), /*Canary Heart T*/
    (1042086, 3000, 'Tops'), /*Tourist T*/
    (1042087, 3000, 'Tops'), /*Skull Shirt*/
    (1042088, 3000, 'Tops'), /*Black Skull Hooded Vest*/
    (1042089, 3000, 'Tops'), /*Blue Skull Hooded Vest*/
    (1042090, 3000, 'Tops'), /*Red Skull Hooded Vest*/
    (1042091, 3000, 'Tops'), /*Pink Skull Hooded Vest*/
    (1042092, 3000, 'Tops'), /*Pelvis Hoodie*/
    (1042093, 3000, 'Tops'), /*Pointed Double Coat*/
    (1042094, 3000, 'Tops'), /*Orange Snowflake Sweater*/
    (1042095, 3000, 'Tops'), /*Vintage Hooded Shirt*/
    (1042096, 3000, 'Tops'), /*M Shirt*/
    (1042097, 3000, 'Tops'), /*Print Layered Hoody*/
    (1042098, 3000, 'Tops'), /*Camo Hooded Jacket*/
    (1042099, 3000, 'Tops'), /*Striped Hooded Shirt*/
    (1042100, 3000, 'Tops'), /*Checkered Casual Suit*/
    (1042101, 3000, 'Tops'), /*Blanc Rose Top*/
    (1042102, 3000, 'Tops'), /*Aqua Road T*/
    (1042103, 3000, 'Tops'), /*White Outlaw Shirt*/
    (1042104, 3000, 'Tops'), /*Lime Green Sleeveless*/
    (1042105, 3000, 'Tops'), /*Crown Hooded T*/
    (1042106, 3000, 'Tops'), /*Rainbow-Striped Hoodie*/
    (1042107, 3000, 'Tops'), /*Pink Flower T-shirt*/
    (1042108, 3000, 'Tops'), /*Purple Tank*/
    (1042109, 3000, 'Tops'), /*Yellow & Red-Striped Jacket*/
    (1042110, 3000, 'Tops'), /*Red Hooded Coat*/
    (1042116, 3000, 'Tops'), /*Orange Pea Coat*/
    (1042117, 3000, 'Tops'), /*Green Baseball Jacket*/
    (1042118, 3000, 'Tops'), /*Red Checkered Shirt*/
    (1042119, 3000, 'Tops'), /*Vintage Muffler Jacket*/
    (1042120, 3000, 'Tops'), /*Celeste Blue Double Coat*/
    (1042121, 3000, 'Tops'), /*Opera Pink Double Coat*/
    (1042122, 3000, 'Tops'), /*Bowtie Jacket*/
    (1042125, 3000, 'Tops'), /*Yellow Longsleeve with Bunny Bag*/
    (1042126, 3000, 'Tops'), /*Red and Black Blazer*/
    (1042127, 3000, 'Tops'), /*Green Suspenders*/
    (1042128, 3000, 'Tops'), /*Apple-Green Sweater*/
    (1042129, 3000, 'Tops'), /*"Black Tie Affair" Dress Shirt*/
    (1042130, 3000, 'Tops'), /*Gold Chainz*/
    (1042131, 3000, 'Tops'), /*Preppy Black Vest*/
    (1042132, 3000, 'Tops'), /*Aqua Green Star*/
    (1042133, 3000, 'Tops'), /*Striped Hoodie Shirt*/
    (1042134, 3000, 'Tops'), /*Yellow Shirt with Pads*/
    (1042135, 3000, 'Tops'), /*Dark Master Sergeant for Transformation*/
    (1042136, 3000, 'Tops'), /*Red Legolesse for Transformation*/
    (1042137, 3000, 'Tops'), /*Dark Tech Top*/
    (1042138, 3000, 'Tops'), /*The White Tee*/
    (1042140, 3000, 'Tops'), /*Slick Agent Top*/
    (1042141, 3000, 'Tops'), /*Pink Star Glow*/
    (1042142, 3000, 'Tops'), /*Rainbow Top*/
    (1042143, 3000, 'Tops'), /*Disco Tank Top*/
    (1042144, 3000, 'Tops'), /*Checkered Resort Shirt*/
    (1042145, 3000, 'Tops'), /*Layered Duckie T*/
    (1042146, 3000, 'Tops'), /*Superstar Hoodie*/
    (1042147, 3000, 'Tops'), /*Preppy Knit Vest*/
    (1042149, 3000, 'Tops'), /*80's Knit Pullover*/
    (1042150, 3000, 'Tops'), /*Black "Hit Me" Shirt*/
    (1042151, 3000, 'Tops'), /*Brown Argyle Sweater*/
    (1042152, 3000, 'Tops'), /*Rainbow Knitted Top*/
    (1042153, 3000, 'Tops'), /*Red Plaid Duffle Coat*/
    (1042154, 3000, 'Tops'), /*Bohemian Hooded Jacket*/
    (1042155, 3000, 'Tops'), /*Sky Rider Jacket*/
    (1042156, 3000, 'Tops'), /*Galaxy T-Shirt*/
    (1042157, 3000, 'Tops'), /*Lovely Pink Heart T-Shirt*/
    (1042158, 3000, 'Tops'), /*Baseball Classic*/
    (1042159, 3000, 'Tops'), /*Animal One Piece*/
    (1042160, 3000, 'Tops'), /*Navy Hoodie*/
    (1042161, 3000, 'Tops'), /*Yellow Spring Jacket*/
    (1042162, 3000, 'Tops'), /*Blue-Striped Undershirt*/
    (1042163, 3000, 'Tops'), /*Pink Heart T-Shirt & Muffler*/
    (1042164, 3000, 'Tops'), /*Green Tie & Shirt*/
    (1042165, 3000, 'Tops'), /*Pink Bowtie & White Vest*/
    (1042166, 3000, 'Tops'), /*Leather Biker Jacket*/
    (1042168, 3000, 'Tops'), /*Lightning T-Shirt*/
    (1042169, 3000, 'Tops'), /*Rainbow Tie-Dye Shirt*/
    (1042170, 3000, 'Tops'), /*Cool Summer Shirt*/
    (1042171, 3000, 'Tops'), /*Idol Star Vest*/
    (1042172, 3000, 'Tops'), /*Preppy Blue Shirt*/
    (1042173, 3000, 'Tops'), /*Green Polo*/
    (1042174, 3000, 'Tops'), /*Camping Shirt*/
    (1042176, 3000, 'Tops'), /*I Love CN Top*/
    (1042177, 3000, 'Tops'), /*Vintage Hoodie Jacket*/
    (1042178, 3000, 'Tops'), /*Puppy Tee*/
    (1042181, 3000, 'Tops'), /*Napoleon Jacket*/
    (1042182, 3000, 'Tops'), /*Denim Hoodie*/
    (1042183, 3000, 'Tops'), /*Pink Argyle Plaid*/
    (1042184, 3000, 'Tops'), /*Tiger-Print Scarf & Top*/
    (1042185, 3000, 'Tops'), /*JM's Street Gear*/
    (1042186, 3000, 'Tops'), /*Fur Vest*/
    (1042187, 3000, 'Tops'), /*Pink Sweater*/
    (1042188, 3000, 'Tops'), /*Puffy Raglan Tee*/
    (1042189, 3000, 'Tops'), /*Lamb Wool Top*/
    (1042190, 3000, 'Tops'), /*Dual-Color Heart Tee*/
    (1042193, 3000, 'Tops'), /*Padded Vest*/
    (1042194, 3000, 'Tops'), /*White Collared Shirt*/
    (1042198, 3000, 'Tops'), /*Rainbow Tee*/
    (1042199, 3000, 'Tops'), /*Pink Smiley Tee*/
    (1042200, 3000, 'Tops'), /*Blue Smiley Tee*/
    (1042202, 3000, 'Tops'), /*Penguin Tee*/
    (1042203, 3000, 'Tops'), /*Orange Scarf Tee*/
    (1042204, 3000, 'Tops'), /*Hamburger Tee*/
    (1042206, 3000, 'Tops'), /*Black Rider Jacket*/
    (1042207, 3000, 'Tops'), /*Star Trainer Jacket*/
    (1042208, 3000, 'Tops'), /*Elephant Hoody*/
    (1042209, 3000, 'Tops'), /*Mustang Vest Green Tee*/
    (1042210, 3000, 'Tops'), /*Mustang Vest Pink Tee*/
    (1042212, 3000, 'Tops'), /*Blue Spring Jacket*/
    (1042213, 3000, 'Tops'), /*Pink Spring Jacket*/
    (1042214, 3000, 'Tops'), /*Spring Sweater Set*/
    (1042215, 3000, 'Tops'), /*Jester Sweater*/
    (1042216, 3000, 'Tops'), /*Red Viva Baseball*/
    (1042217, 3000, 'Tops'), /*Black Viva Baseball*/
    (1042218, 3000, 'Tops'), /*Raspberry Candy T-Shirt*/
    (1042219, 3000, 'Tops'), /*Blue Stars T-Shirt*/
    (1042220, 3000, 'Tops'), /*Shiny Training Top*/
    (1042221, 3000, 'Tops'), /*Joyous 8th T-Shirt*/
    (1042222, 3000, 'Tops'), /*Lemon Freshness*/
    (1042228, 3000, 'Tops'), /*I Love SG Top*/
    (1042229, 3000, 'Tops'), /*I Love MY Top*/
    (1042230, 3000, 'Tops'), /*Cutie Raincoat*/
    (1042232, 3000, 'Tops'), /*Bat Costume Sweater*/
    (1042235, 3000, 'Tops'), /*Rabbit Top*/
    (1042236, 3000, 'Tops'), /*Green Apple Sweater*/
    (1042237, 3000, 'Tops'), /*Gold Tailor Vest*/
    (1042238, 3000, 'Tops'), /*Pink Bunny Sweater*/
    (1042240, 3000, 'Tops'), /*Colorful T-Shirt*/
    (1042241, 3000, 'Tops'), /*Flying Violet*/
    (1042242, 3000, 'Tops'), /*Summer Picnic*/
    (1042245, 3000, 'Tops'), /*Hyper Spring Jealousy*/
    (1042246, 3000, 'Tops'), /*Hyper Green Suspenders*/
    (1042250, 3000, 'Tops'), /*Hyper Spring Sweater Set*/
    (1042251, 3000, 'Tops'), /*Slither Style Hoodie*/
    (1042252, 3000, 'Tops'), /*Cute Sleeveless Shirt*/
    (1042260, 3000, 'Tops'), /*Loose Fit Sweater*/
    (1042263, 3000, 'Tops'), /*Funky Jumper*/
    (1042264, 3000, 'Tops'), /*Colored Golf Shirt*/
    (1042265, 3000, 'Tops'), /*Strawberry Shirt*/
    (1042267, 3000, 'Tops'), /*Exciting Hoodie*/
    (1042269, 3000, 'Tops'), /*Ribbon Days*/
    (1042271, 3000, 'Tops'), /*Meow T-shirt*/
    (1042275, 3000, 'Tops'), /*Frog Raindrop*/
    (1042277, 3000, 'Tops'), /*Star T-Shirt*/
    (1042279, 3000, 'Tops'), /*Hun T-Shirt*/
    (1042280, 3000, 'Tops'), /*Min T-Shirt*/
    (1042281, 3000, 'Tops'), /*Jeong T-Shirt*/
    (1042282, 3000, 'Tops'), /*Eum T-Shirt*/
    (1042285, 3000, 'Tops'), /*Pastel Dot Tee*/
    (1042286, 3000, 'Tops'), /*Athletic Hood*/
    (1042287, 3000, 'Tops'), /*Red Check Rider*/
    (1042290, 3000, 'Tops'), /*White Cherry Knit*/
    (1042291, 3000, 'Tops'), /*Vibrant Yellow Knit*/
    (1042292, 3000, 'Tops'), /*Banana Cardigan*/
    (1042293, 3000, 'Tops'), /*Guardian Clothing*/
    (1042294, 3000, 'Tops'), /*Thumping Heart Vest*/
    (1042311, 3000, 'Tops'), /*Rainbow T-shirt*/
    (1042312, 3000, 'Tops'), /*Blue Mushroom T-Shirt*/
    (1042313, 3000, 'Tops'), /*Full of Hearts T-Shirt*/
    (1042314, 3000, 'Tops'), /*Rabbit and Bear Shirt*/
    (1042315, 3000, 'Tops'), /*Bubbly Elephant Shirt*/
    (1048002, 3000, 'Tops'), /*Carrot T-shirt*/
    (1042319, 3000, 'Tops'), /*Hoi Poi T-shirt*/
    (1042320, 3000, 'Tops'), /*Island Travel T-Shirt*/
    (1042329, 3000, 'Tops'), /*Sweet Summer Shirt*/
    (1042330, 3000, 'Tops'), /*Charming Baby*/
    (1042332, 3000, 'Tops'), /*Red Ribbon Kitty Top*/
    (1042333, 3000, 'Tops'), /*Pink Kitty Sweatshirt*/
    (1042334, 3000, 'Tops'), /*Green Kitty Shirt*/
    (1042335, 3000, 'Tops'), /*Pink Marine T-shirt*/
    (1042336, 3000, 'Tops'), /*Corny Top*/
    (1042337, 3000, 'Tops'), /*Teddy Picnic Shirt*/
    (1042338, 3000, 'Tops'), /*Brown Teddy Top*/
    (1042339, 3000, 'Tops'), /*White Kitty Pink Top*/
    (1042341, 3000, 'Tops'), /*Hatchling T-shirt*/
    (1042342, 3000, 'Tops'), /*Rawrin' Tiger Top*/
    (1042343, 3000, 'Tops'), /*Black Hip Hop*/
    (1042344, 3000, 'Tops'), /*Gold Fur-Lined Jacket*/
    (1042345, 3000, 'Tops'), /*Baby Ram Pullover (Blue)*/
    (1042346, 3000, 'Tops'), /*Baby Ram Pullover (Pink)*/
    (1042347, 3000, 'Tops'), /*Naughty Boy T-Shirt*/
    (1042348, 3000, 'Tops'), /*Boldly Colored Polo*/
    (1042349, 3000, 'Tops'), /*All About Black*/
    (1049000, 3000, 'Tops'), /*Friendship Shirt*/
    (1042351, 3000, 'Tops'), /*Hoya T-shirt*/
    (1042354, 3000, 'Tops'), /*Duang Effect T-Shirt*/
    (1042355, 3000, 'Tops'), /*Ranbingluan Effect T-Shirt*/
    (1042356, 3000, 'Tops'), /*Chenghuiwan Effect T-Shirt*/
    (1042357, 3000, 'Tops'), /*Cloud Prison*/
    (1042358, 3000, 'Tops'), /*Soft Olive Knitwear*/
    (1042361, 3000, 'Tops'), /*Red Cloud Top*/
    (1048000, 3000, 'Tops'), /*Couple Shirt*/
    (1048001, 3000, 'Tops'), /*Bunny Love T-Shirt*/

/* Bottoms */
    (1060001, 3000, 'Bottoms'), /*Black Suit Pants*/
    (1060003, 3000, 'Bottoms'), /*Military Shorts*/
    (1060034, 3000, 'Bottoms'), /*Blue Rider Pants*/
    (1060035, 3000, 'Bottoms'), /*Shine Rider Pants*/
    (1060036, 3000, 'Bottoms'), /*Dark Rider Pants*/
    (1060040, 3000, 'Bottoms'), /*Blue Trainer Pants*/
    (1060041, 3000, 'Bottoms'), /*Green Trainer Pants*/
    (1060042, 3000, 'Bottoms'), /*Orange Trainer Pants*/
    (1060047, 3000, 'Bottoms'), /*Original Disco Pants*/
    (1060048, 3000, 'Bottoms'), /*Green Disco Pants*/
    (1060049, 3000, 'Bottoms'), /*Blue Disco Pants*/
    (1060053, 3000, 'Bottoms'), /*Wild Pants*/
    (1060054, 3000, 'Bottoms'), /*Brown Wild Pants*/
    (1060055, 3000, 'Bottoms'), /*Red Wild Pants*/
    (1060066, 3000, 'Bottoms'), /*Cowboy Pants*/
    (1060067, 3000, 'Bottoms'), /*Pre-School Pants*/
    (1060096, 3000, 'Bottoms'), /*Old School Uniform Pants*/
    (1060103, 3000, 'Bottoms'), /*Hawaiian Skirt*/
    (1060108, 3000, 'Bottoms'), /*Torn-Up Jeans*/
    (1060112, 3000, 'Bottoms'), /*Prep School Uniform Pants*/
    (1060113, 3000, 'Bottoms'), /*Blue Leggings*/
    (1060114, 3000, 'Bottoms'), /*Washed Jeans*/
    (1060116, 3000, 'Bottoms'), /*Military Cargo Shorts*/
    (1060117, 3000, 'Bottoms'), /*Tropical Shorts*/
    (1060118, 3000, 'Bottoms'), /*Orange Puffy Pants*/
    (1060119, 3000, 'Bottoms'), /*Denim Wrinkled Skirt*/
    (1060120, 3000, 'Bottoms'), /*Tania Tartan Pants*/
    (1060121, 3000, 'Bottoms'), /*Mercury Washed Jeans*/
    (1060122, 3000, 'Bottoms'), /*Pink Miniskirt*/
    (1060123, 3000, 'Bottoms'), /*Blue Sailor Skirt*/
    (1060125, 3000, 'Bottoms'), /*Blue Skirt (m)*/
    (1060126, 3000, 'Bottoms'), /*Black Wakeboard Pants*/
    (1060139, 3000, 'Bottoms'), /*Retro School Uniform Pants*/
    (1060179, 3000, 'Bottoms'), /*Golf Shorts*/
    (1060180, 3000, 'Bottoms'), /*Puffy Puff Pants*/
    (1060181, 3000, 'Bottoms'), /*Star Shorts*/
    (1060187, 3000, 'Bottoms'), /*Green Rolled-Up Shorts*/
    (1060188, 3000, 'Bottoms'), /*White Hot Pants*/
    (1060189, 3000, 'Bottoms'), /*Smile Seed Pants*/
    (1061000, 3000, 'Bottoms'), /*Blue Bell Dress*/
    (1061005, 3000, 'Bottoms'), /*Roll-Up Jean*/
    (1061007, 3000, 'Bottoms'), /*Red Sailor Skirt*/
    (1061065, 3000, 'Bottoms'), /*Sky Blue Miniskirt*/
    (1061066, 3000, 'Bottoms'), /*Yellow Mimi Skirt*/
    (1061068, 3000, 'Bottoms'), /*Pre-School Uniform Skirt*/
    (1061072, 3000, 'Bottoms'), /*Red Trainer Pants*/
    (1061073, 3000, 'Bottoms'), /*Sky Blue Trainer Pants*/
    (1061074, 3000, 'Bottoms'), /*Pink Trainer Pants*/
    (1061075, 3000, 'Bottoms'), /*Black Trainer Pants*/
    (1061089, 3000, 'Bottoms'), /*Blue Skirt*/
    (1061103, 3000, 'Bottoms'), /*Old School Uniform (Skirt)*/
    (1061107, 3000, 'Bottoms'), /*SF Ninja Pants*/
    (1061108, 3000, 'Bottoms'), /*Red Training Shorts*/
    (1061109, 3000, 'Bottoms'), /*Sky Blue Training Shorts*/
    (1061110, 3000, 'Bottoms'), /*Pink Training Shorts*/
    (1061111, 3000, 'Bottoms'), /*Black Training Shorts*/
    (1061112, 3000, 'Bottoms'), /*Pink Frill Pajama Bottom*/
    (1061124, 3000, 'Bottoms'), /*Red Leggings*/
    (1061126, 3000, 'Bottoms'), /*Plitz Skirt*/
    (1061127, 3000, 'Bottoms'), /*Blue Diamond Bootcuts*/
    (1061128, 3000, 'Bottoms'), /*Pink Diamond Bootcuts*/
    (1061129, 3000, 'Bottoms'), /*Butterfly Skirt*/
    (1061130, 3000, 'Bottoms'), /*Green Long Skirt*/
    (1061131, 3000, 'Bottoms'), /*Blue Slit Skirt*/
    (1061132, 3000, 'Bottoms'), /*Skirt with Tights*/
    (1061133, 3000, 'Bottoms'), /*Orange Long Skirt*/
    (1061134, 3000, 'Bottoms'), /*Denim Miniskirt*/
    (1061135, 3000, 'Bottoms'), /*Pink Layered Skirt*/
    (1061136, 3000, 'Bottoms'), /*Long Khaki Skirt*/
    (1061137, 3000, 'Bottoms'), /*Dark Denim Skirt*/
    (1061138, 3000, 'Bottoms'), /*Pink Heart Hot Pants*/
    (1061140, 3000, 'Bottoms'), /*Denim Skirt & Striped Sox*/
    (1061141, 3000, 'Bottoms'), /*Tania Tartan Skirt*/
    (1061142, 3000, 'Bottoms'), /*Mercury Jean Skirt*/
    (1061143, 3000, 'Bottoms'), /*Amorian Pink Skirt*/
    (1061144, 3000, 'Bottoms'), /*Blue Jeans*/
    (1061147, 3000, 'Bottoms'), /*Old School Uniform Pants (F)*/
    (1061148, 3000, 'Bottoms'), /*Pink Frill Swim Skirt*/
    (1061166, 3000, 'Bottoms'), /*Pre-School Skirt*/
    (1061170, 3000, 'Bottoms'), /*Bright Frilly Shorts*/
    (1061198, 3000, 'Bottoms'), /*Cowgirl Pants*/
    (1061203, 3000, 'Bottoms'), /*Puffy Puff Dress*/
    (1061204, 3000, 'Bottoms'), /*Golf Skirt*/
    (1061207, 3000, 'Bottoms'), /*Star Skirt*/
    (1061210, 3000, 'Bottoms'), /*Check Skirt*/
    (1061211, 3000, 'Bottoms'), /*Green Skirt*/
    (1061213, 3000, 'Bottoms'), /*Smile Seed Skirt*/
    (1062003, 3000, 'Bottoms'), /*Red Hip-Hop Pants*/
    (1062005, 3000, 'Bottoms'), /*Lined Hip-Hop Pants*/
    (1062008, 3000, 'Bottoms'), /*Pink Camping Shorts*/
    (1062009, 3000, 'Bottoms'), /*Green Camping Shorts*/
    (1062010, 3000, 'Bottoms'), /*Blue Camping Shorts*/
    (1062011, 3000, 'Bottoms'), /*Wildcats Baseball Pants (Basic)*/
    (1062012, 3000, 'Bottoms'), /*Baseball Pants (Home)*/
    (1062013, 3000, 'Bottoms'), /*Baseball Pants (Away)*/
    (1062014, 3000, 'Bottoms'), /*Wildcats Baseball Pants (Alternate)*/
    (1062015, 3000, 'Bottoms'), /*Ripped Jeans*/
    (1062016, 3000, 'Bottoms'), /*Yellow Snowboard Pants*/
    (1062017, 3000, 'Bottoms'), /*Green Snowboard Pants*/
    (1062018, 3000, 'Bottoms'), /*Bell-Bottomed Faded Jeans*/
    (1062019, 3000, 'Bottoms'), /*Pink Snowboard Pants*/
    (1062020, 3000, 'Bottoms'), /*Sky Blue Snowboard Pants*/
    (1062021, 3000, 'Bottoms'), /*Jean Shorts*/
    (1062022, 3000, 'Bottoms'), /*Old Army Pants*/
    (1062023, 3000, 'Bottoms'), /*Baggy Jeans*/
    (1062024, 3000, 'Bottoms'), /*Camouflaged Army Pants*/
    (1062025, 3000, 'Bottoms'), /*Blue Polka-Dot Pajama Pants*/
    (1062026, 3000, 'Bottoms'), /*Red Polka-Dot Pajama Pants*/
    (1062027, 3000, 'Bottoms'), /*Prisoner Pants*/
    (1062028, 3000, 'Bottoms'), /*Picnic Jean Shorts*/
    (1062029, 3000, 'Bottoms'), /*Blue B-Ball Shorts*/
    (1062030, 3000, 'Bottoms'), /*Orange B-Ball Shorts*/
    (1062031, 3000, 'Bottoms'), /*Checkered Shorts*/
    (1062032, 3000, 'Bottoms'), /*Cargo Pants*/
    (1062033, 3000, 'Bottoms'), /*Red Checkered Pants*/
    (1062034, 3000, 'Bottoms'), /*White Checkered Pants*/
    (1062035, 3000, 'Bottoms'), /*Bone Buckled Slacks*/
    (1062038, 3000, 'Bottoms'), /*Hip Hop Jeans*/
    (1062039, 3000, 'Bottoms'), /*White Jeans*/
    (1062040, 3000, 'Bottoms'), /*Washed Denim Cargos*/
    (1062041, 3000, 'Bottoms'), /*Denim Cargos*/
    (1062042, 3000, 'Bottoms'), /*Jeans with Chain*/
    (1062043, 3000, 'Bottoms'), /*Black Leather Pants*/
    (1062044, 3000, 'Bottoms'), /*Red Starrium*/
    (1062045, 3000, 'Bottoms'), /*Patched Denim Jeans*/
    (1062046, 3000, 'Bottoms'), /*Vintage Pocket Pants*/
    (1062047, 3000, 'Bottoms'), /*Brisk*/
    (1062048, 3000, 'Bottoms'), /*Brown Checkered Pants*/
    (1062049, 3000, 'Bottoms'), /*Football Pants (Home)*/
    (1062050, 3000, 'Bottoms'), /*Football Bottom (Away)*/
    (1062051, 3000, 'Bottoms'), /*All-Star Blue Jeans*/
    (1062052, 3000, 'Bottoms'), /*White Faded Jeans*/
    (1062053, 3000, 'Bottoms'), /*Pink-Lined Shorts*/
    (1062054, 3000, 'Bottoms'), /*Busy Bee Shorts*/
    (1062055, 3000, 'Bottoms'), /*Jailbird Shorts*/
    (1062056, 3000, 'Bottoms'), /*Military Cargo Pants*/
    (1062057, 3000, 'Bottoms'), /*Scottish Pants*/
    (1062058, 3000, 'Bottoms'), /*Inferno Jeans*/
    (1062059, 3000, 'Bottoms'), /*Vintage Black Jeans*/
    (1062060, 3000, 'Bottoms'), /*Blue Skinny Jeans*/
    (1062061, 3000, 'Bottoms'), /*Olive Skinny Jeans*/
    (1062062, 3000, 'Bottoms'), /*Red Wine Skinny Jeans*/
    (1062063, 3000, 'Bottoms'), /*Dark Rocker Jeans*/
    (1062064, 3000, 'Bottoms'), /*Checks Point Pants*/
    (1062065, 3000, 'Bottoms'), /*White-Striped Trainer Shorts*/
    (1062066, 3000, 'Bottoms'), /*Vintage Sky Blue Jeans*/
    (1062067, 3000, 'Bottoms'), /*Summer Capris*/
    (1062068, 3000, 'Bottoms'), /*Rainbow Shorts*/
    (1062069, 3000, 'Bottoms'), /*Brown Chained Pants*/
    (1062070, 3000, 'Bottoms'), /*Painted Blue Jeans*/
    (1062071, 3000, 'Bottoms'), /*Low-Rise Ripped Jeans*/
    (1062072, 3000, 'Bottoms'), /*Relaxed Fit Jeans*/
    (1062073, 3000, 'Bottoms'), /*Olive Pumpkin Pants*/
    (1062074, 3000, 'Bottoms'), /*Brown Pumpkin Pants*/
    (1062075, 3000, 'Bottoms'), /*Vintage Black Pants*/
    (1062076, 3000, 'Bottoms'), /*Light Blue Ripped Jeans*/
    (1062077, 3000, 'Bottoms'), /*Brown Bubble Jeans*/
    (1062081, 3000, 'Bottoms'), /*Bunny-Padded Snowboard Pants*/
    (1062082, 3000, 'Bottoms'), /*Red and Black Warm-ups*/
    (1062083, 3000, 'Bottoms'), /*Brown Pocket Shorts*/
    (1062084, 3000, 'Bottoms'), /*Jewel Chain Jeans*/
    (1062085, 3000, 'Bottoms'), /*"Black Tie Affair" Dress Pants*/
    (1062086, 3000, 'Bottoms'), /*Dark Master Sergeant Skirt for Transformation*/
    (1062087, 3000, 'Bottoms'), /*Red Legolia Pants for Transformation*/
    (1062088, 3000, 'Bottoms'), /*Dark Night Pants for Transformation*/
    (1062089, 3000, 'Bottoms'), /*Pink Heart Boxers*/
    (1062091, 3000, 'Bottoms'), /*Black Checkered Shorts*/
    (1062092, 3000, 'Bottoms'), /*Pink 80s Slacks*/
    (1062093, 3000, 'Bottoms'), /*Moss Green Pants*/
    (1062094, 3000, 'Bottoms'), /*Ruby-Buckled Shorts*/
    (1062095, 3000, 'Bottoms'), /*Milan Jeans*/
    (1062096, 3000, 'Bottoms'), /*Practical Linen Trousers*/
    (1062097, 3000, 'Bottoms'), /*Ella Blue Denim*/
    (1062098, 3000, 'Bottoms'), /*Aqua Jeans*/
    (1062100, 3000, 'Bottoms'), /*Rolled-Up Baggy Jeans*/
    (1062101, 3000, 'Bottoms'), /*Rolled-Up Skinny Jeans*/
    (1062102, 3000, 'Bottoms'), /*Twinkle Star Blue Jeans*/
    (1062103, 3000, 'Bottoms'), /*Baggy Glow-in-the-Dark Pants*/
    (1062104, 3000, 'Bottoms'), /*Dark Purple Jeans*/
    (1062105, 3000, 'Bottoms'), /*Plaid Roll-Up Jeans*/
    (1062106, 3000, 'Bottoms'), /*Bunny Frill Pants*/
    (1062107, 3000, 'Bottoms'), /*Shooting Star Jeans*/
    (1062108, 3000, 'Bottoms'), /*Vintage Jeans*/
    (1062109, 3000, 'Bottoms'), /*Neon Skinny Jeans*/
    (1062110, 3000, 'Bottoms'), /*Baby Pink Pants*/
    (1062111, 3000, 'Bottoms'), /*Blue Ribbon Shorts*/
    (1062112, 3000, 'Bottoms'), /*Underpants*/
    (1062113, 3000, 'Bottoms'), /*Crayon Shorts*/
    (1062114, 3000, 'Bottoms'), /*Pink Heart Shorts*/
    (1062116, 3000, 'Bottoms'), /*Star Beach Shorts*/
    (1062117, 3000, 'Bottoms'), /*Idol Star Chain Pants*/
    (1062118, 3000, 'Bottoms'), /*Stone Washed Jeans*/
    (1062119, 3000, 'Bottoms'), /*Technicolour Funky Pants*/
    (1062121, 3000, 'Bottoms'), /*Tiger-Print Leggings*/
    (1062122, 3000, 'Bottoms'), /*Plaid-Cuffed Jeans*/
    (1062123, 3000, 'Bottoms'), /*High-Rider*/
    (1062124, 3000, 'Bottoms'), /*Saruel Pants*/
    (1062126, 3000, 'Bottoms'), /*Pink Sprite Pants*/
    (1062129, 3000, 'Bottoms'), /*Red Spotted Shorts*/
    (1062130, 3000, 'Bottoms'), /*Blue Spotted Shorts*/
    (1062133, 3000, 'Bottoms'), /*Star Trainer Pants*/
    (1062134, 3000, 'Bottoms'), /*Super Pop Shorts*/
    (1062135, 3000, 'Bottoms'), /*Shiny Gold Pants*/
    (1062136, 3000, 'Bottoms'), /*Layered Denim Pants*/
    (1062137, 3000, 'Bottoms'), /*Plum Sherbet Pants*/
    (1062138, 3000, 'Bottoms'), /*Mint Sherbet Pants*/
    (1062139, 3000, 'Bottoms'), /*Deep Blue Sea Knee Socks*/
    (1062145, 3000, 'Bottoms'), /*Funky Xylophone Leggings*/
    (1062147, 3000, 'Bottoms'), /*Sky Rainbow Shorts [temp]*/
    (1062151, 3000, 'Bottoms'), /*Rabbit Bottom*/
    (1062152, 3000, 'Bottoms'), /*Neon Pink Pants*/
    (1062153, 3000, 'Bottoms'), /*Vacation Denim Pants*/
    (1062155, 3000, 'Bottoms'), /*Oceanic Sandblasted Jeans*/
    (1062156, 3000, 'Bottoms'), /*Mosaic Purple*/
    (1062157, 3000, 'Bottoms'), /*Chocolate Strawberry Pants*/
    (1062159, 3000, 'Bottoms'), /*Hyper Chocolate Strawberry Pants*/
    (1062160, 3000, 'Bottoms'), /*Hyper Funky Xylophone Leggings*/
    (1062162, 3000, 'Bottoms'), /*Hyper Deep Blue Sea Knee Socks*/
    (1062163, 3000, 'Bottoms'), /*Slither Style Pants*/
    (1062171, 3000, 'Bottoms'), /*Stocking Shorts*/
    (1062172, 3000, 'Bottoms'), /*Checkered Tights*/
    (1062173, 3000, 'Bottoms'), /*Funky Shorts*/
    (1062174, 3000, 'Bottoms'), /*Hearts Tights*/
    (1062175, 3000, 'Bottoms'), /*Pink Skinny Jeans*/
    (1062179, 3000, 'Bottoms'), /*Little Bunny Pants*/
    (1062182, 3000, 'Bottoms'), /*Sapphire Jeans*/
    (1062183, 3000, 'Bottoms'), /*Hot Pink Overalls*/
    (1062184, 3000, 'Bottoms'), /*Cargo Hiphop Pants*/
    (1062185, 3000, 'Bottoms'), /*Violet Dot Jeans*/
    (1062189, 3000, 'Bottoms'), /*Guardian Pants*/
    (1062203, 3000, 'Bottoms'), /*Otherworldly Slacks*/
    (1062204, 3000, 'Bottoms'), /*Rainbow Pants*/
    (1062207, 3000, 'Bottoms'), /*Hoi Poi Shorts*/
    (1062208, 3000, 'Bottoms'), /*Bunny Patch Pants*/
    (1062209, 3000, 'Bottoms'), /*Mini Bunny Pants*/
    (1062210, 3000, 'Bottoms'), /*Isand Travel Shorts*/
    (1062211, 3000, 'Bottoms'), /*Sweet Summer Shorts*/
    (1062212, 3000, 'Bottoms'), /*Heart Hot Pants*/
    (1062213, 3000, 'Bottoms'), /*Baby Purple Shorts*/
    (1062214, 3000, 'Bottoms'), /*Teddy Hip Pants*/
    (1062216, 3000, 'Bottoms'), /*Mismatched Shorts*/
    (1062217, 3000, 'Bottoms'), /*Polka-Dot A Line Skirt*/
    (1062218, 3000, 'Bottoms'), /*Green Speckled Sweatpants*/
    (1062219, 3000, 'Bottoms'), /*Colorful Blue Pants*/
    (1062220, 3000, 'Bottoms'), /*White Shorts*/
    (1062221, 3000, 'Bottoms'), /*Teddy Picnic Pants*/
    (1062222, 3000, 'Bottoms'), /*Brown Teddy Capris Pants*/
    (1062223, 3000, 'Bottoms'), /*Pink Kitty Denim Skirt*/
    (1062225, 3000, 'Bottoms'), /*Heart Patch Knit Pants*/
    (1062226, 3000, 'Bottoms'), /*Rawrin' Tiger Pants*/
    (1062228, 3000, 'Bottoms'), /*White Rainbow Leggings*/
    (1062229, 3000, 'Bottoms'), /*Naughty Boy Pants*/
    (1062231, 3000, 'Bottoms'), /*All About Jeans*/
    (1062232, 3000, 'Bottoms'), /*Hoya Shorts*/
    (1062233, 3000, 'Bottoms'), /*Dark Slate Jeans*/
    (1062234, 3000, 'Bottoms'), /*Saggy Pants*/
    (1062235, 3000, 'Bottoms'), /*Red Cloud Bottom*/

/* Shoes */
    (1070000, 3000, 'Shoes'), /*Blue Gomushin*/
    (1070001, 3000, 'Shoes'), /*Black Santa Boots*/
    (1070002, 3000, 'Shoes'), /*Kimono Shoes (M)*/
    (1070003, 3000, 'Shoes'), /*Black Shoes of Death*/
    (1070004, 3000, 'Shoes'), /*Blue Western Walkers*/
    (1070005, 3000, 'Shoes'), /*Santa Boy Boots*/
    (1070006, 3000, 'Shoes'), /*Royal Costume Shoes*/
    (1070007, 3000, 'Shoes'), /*Lunar Celebration Shoes*/
    (1070008, 3000, 'Shoes'), /*Korean Martial Arts Shoes*/
    (1070009, 3000, 'Shoes'), /*Paris Wingtips*/
    (1070014, 3000, 'Shoes'), /*Veras Heels [m]*/
    (1070015, 3000, 'Shoes'), /*Bunny Boots [m]*/
    (1070016, 3000, 'Shoes'), /*Dandy Silver Sneaks*/
    (1070018, 3000, 'Shoes'), /*Napoleon Shoes */
    (1070019, 3000, 'Shoes'), /*Napoleon Boots*/
    (1070020, 3000, 'Shoes'), /*Twinkling Boy Glow Shoes*/
    (1070024, 3000, 'Shoes'), /*Garnet-Studded Boots*/
    (1070028, 3000, 'Shoes'), /*Evergreen Magistrate Pretty Shoes*/
    (1070031, 3000, 'Shoes'), /*Alps Boy Shoes*/
    (1070057, 3000, 'Shoes'), /*Shadow Sandals*/
    (1070059, 3000, 'Shoes'), /*Rainbow Picnic Shoes*/
    (1070060, 3000, 'Shoes'), /*[[FROZEN CONTENT]] Kristoff Shoes*/
    (1070061, 3000, 'Shoes'), /*Glass Sneakers*/
    (1070064, 3000, 'Shoes'), /*Mad Doctor Boots*/
    (1070065, 3000, 'Shoes'), /*Blue Macaron Shoes*/
    (1070067, 3000, 'Shoes'), /*Cozy Snow Flower*/
    (1070068, 3000, 'Shoes'), /*The Kingdom Dress Shoes of King*/
    (1070069, 3000, 'Shoes'), /*Soaring Sky*/
    (1070070, 3000, 'Shoes'), /*Yeonhwa School Shoes*/
    (1070071, 3000, 'Shoes'), /*Mr. Time Shoes*/
    (1070072, 3000, 'Shoes'), /*Cutie Farmer Sneakers*/
    (1070073, 3000, 'Shoes'), /*Bloody Sneakers*/
    (1070075, 3000, 'Shoes'), /*Time Master Shoes*/
    (1070076, 3000, 'Shoes'), /*Red Santa Boots*/
    (1070078, 3000, 'Shoes'), /*Concert Muse Shoes*/
    (1071000, 3000, 'Shoes'), /*Blue Loose Sox*/
    (1071001, 3000, 'Shoes'), /*Red Loose Sox*/
    (1071004, 3000, 'Shoes'), /*Pink Nurse Shoes*/
    (1071005, 3000, 'Shoes'), /*White Nurse Shoes*/
    (1071006, 3000, 'Shoes'), /*SF Ninja Shoes*/
    (1071007, 3000, 'Shoes'), /*Bunny Boots*/
    (1071008, 3000, 'Shoes'), /*Kimono Shoes (F)*/
    (1071010, 3000, 'Shoes'), /*Sea Queen Sandals*/
    (1071011, 3000, 'Shoes'), /*Race Queen Boots*/
    (1071012, 3000, 'Shoes'), /*Diao Chan Shoes*/
    (1071013, 3000, 'Shoes'), /*White Cat Shoes*/
    (1071014, 3000, 'Shoes'), /*Black Cat Shoes*/
    (1071015, 3000, 'Shoes'), /*Maid Shoes*/
    (1071016, 3000, 'Shoes'), /*Santa Girl Boots*/
    (1071017, 3000, 'Shoes'), /*Leopard Print Shoes*/
    (1071018, 3000, 'Shoes'), /*Brown Leather Boots*/
    (1071019, 3000, 'Shoes'), /*Lunar Celebration Pumps*/
    (1071020, 3000, 'Shoes'), /*Veras Heels*/
    (1071021, 3000, 'Shoes'), /*Gothic Boots*/
    (1071024, 3000, 'Shoes'), /*Black Dress Shoes [f]*/
    (1071025, 3000, 'Shoes'), /*Paris Wingtips [F]*/
    (1071026, 3000, 'Shoes'), /*White High Top*/
    (1071030, 3000, 'Shoes'), /*Twinkling Girl Glow Shoes*/
    (1071031, 3000, 'Shoes'), /*Pink Angel Wing Shoes*/
    (1071032, 3000, 'Shoes'), /*Red Ribbon Shoes*/
    (1071037, 3000, 'Shoes'), /*Cygnus Sandals*/
    (1071044, 3000, 'Shoes'), /*Pinky Pretty Gomushin*/
    (1071048, 3000, 'Shoes'), /*Alps Girl Shoes*/
    (1071074, 3000, 'Shoes'), /*Shadow Garter*/
    (1071076, 3000, 'Shoes'), /*Colorful Picnic Shoes*/
    (1071077, 3000, 'Shoes'), /*[[FROZEN CONTENT]] Elsa Heels*/
    (1071078, 3000, 'Shoes'), /*Glass Slippers*/
    (1071080, 3000, 'Shoes'), /*Ribbon Angel Shoes*/
    (1071081, 3000, 'Shoes'), /*Pink Macaron Shoes*/
    (1071084, 3000, 'Shoes'), /*The Kingdom Blue Heels of Queen*/
    (1071085, 3000, 'Shoes'), /*Soaring Cloud*/
    (1071088, 3000, 'Shoes'), /*Ms. Time Shoes*/
    (1071089, 3000, 'Shoes'), /*Pure Farmer Sandals*/
    (1071090, 3000, 'Shoes'), /*Bloody Heels*/
    (1071092, 3000, 'Shoes'), /*Time Mistress Shoes*/
    (1071095, 3000, 'Shoes'), /*Concert Muse Heels*/
    (1072010, 3000, 'Shoes'), /*Black Dress Shoes*/
    (1072013, 3000, 'Shoes'), /*Red Air H's*/
    (1072014, 3000, 'Shoes'), /*Camping Boots*/
    (1072057, 3000, 'Shoes'), /*Blue Air H's*/
    (1072058, 3000, 'Shoes'), /*Black Air H's*/
    (1072088, 3000, 'Shoes'), /*Cowboy Boots*/
    (1072092, 3000, 'Shoes'), /*Yellow Flippers*/
    (1072093, 3000, 'Shoes'), /*Blue Flippers*/
    (1072094, 3000, 'Shoes'), /*Yellow Rain Boots*/
    (1072095, 3000, 'Shoes'), /*Sky Blue Rain Boots*/
    (1072096, 3000, 'Shoes'), /*Red Rain Boots*/
    (1072097, 3000, 'Shoes'), /*Green Rain Boots*/
    (1072098, 3000, 'Shoes'), /*Blue Baseball Cleats*/
    (1072099, 3000, 'Shoes'), /*Red Baseball Cleats*/
    (1072100, 3000, 'Shoes'), /*Black Baseball Cleats*/
    (1072111, 3000, 'Shoes'), /*Black Leather Boots*/
    (1072153, 3000, 'Shoes'), /*Transparent Shoes*/
    (1072175, 3000, 'Shoes'), /*Ninja Shoes*/
    (1072176, 3000, 'Shoes'), /*Military Boots*/
    (1072180, 3000, 'Shoes'), /*Flipper Boots*/
    (1072181, 3000, 'Shoes'), /*Green Ting Slippers*/
    (1072186, 3000, 'Shoes'), /*Gold Kitty Slippers*/
    (1072187, 3000, 'Shoes'), /*Blue Marble Slippers*/
    (1072188, 3000, 'Shoes'), /*Red Marble Slippers*/
    (1072189, 3000, 'Shoes'), /*Bunny Slippers*/
    (1072190, 3000, 'Shoes'), /*Blue B-ball Sneakers*/
    (1072191, 3000, 'Shoes'), /*Orange B-ball Sneakers*/
    (1072199, 3000, 'Shoes'), /*Ragged Gomushin*/
    (1072200, 3000, 'Shoes'), /*Brown Dress Shoes*/
    (1072201, 3000, 'Shoes'), /*Red Leather Boots*/
    (1072202, 3000, 'Shoes'), /*Mesoranger Boots*/
    (1072217, 3000, 'Shoes'), /*Beige Golashes*/
    (1072218, 3000, 'Shoes'), /*Sky Blue Golashes*/
    (1072219, 3000, 'Shoes'), /*Pink Golashes*/
    (1072230, 3000, 'Shoes'), /*Black Boxing Shoes*/
    (1072231, 3000, 'Shoes'), /*Blue Boxing Shoes*/
    (1072232, 3000, 'Shoes'), /*Red Boxing Shoes*/
    (1072233, 3000, 'Shoes'), /*Bear Shoes*/
    (1072234, 3000, 'Shoes'), /*Bubbling Slippers*/
    (1072235, 3000, 'Shoes'), /*Slime Slippers*/
    (1072236, 3000, 'Shoes'), /*Guan Yu Shoes*/
    (1072237, 3000, 'Shoes'), /*Zhu-Ge-Liang Shoes*/
    (1072240, 3000, 'Shoes'), /*Big Rabbit Feet*/
    (1072241, 3000, 'Shoes'), /*Liu Bei Shoes*/
    (1072242, 3000, 'Shoes'), /*Cao Cao Shoes*/
    (1072243, 3000, 'Shoes'), /*Sun Quan Shoes*/
    (1072244, 3000, 'Shoes'), /*Red Enamel Shoes*/
    (1072245, 3000, 'Shoes'), /*Blue Enamel Shoes*/
    (1072246, 3000, 'Shoes'), /*Pink Sneakers*/
    (1072247, 3000, 'Shoes'), /*Hunting Boots*/
    (1072250, 3000, 'Shoes'), /*Horoscope Shoes*/
    (1072251, 3000, 'Shoes'), /*Pro-Cat Sticker*/
    (1072252, 3000, 'Shoes'), /*Snowboard Boots*/
    (1072253, 3000, 'Shoes'), /*Red Santa Shoes*/
    (1072254, 3000, 'Shoes'), /*Football Cleats (Home)*/
    (1072255, 3000, 'Shoes'), /*Football Cleats (Away)*/
    (1072256, 3000, 'Shoes'), /*Teddy Bear Shoes*/
    (1072257, 3000, 'Shoes'), /*Puppy Slippers*/
    (1072258, 3000, 'Shoes'), /*Gray Kitty Slippers*/
    (1072259, 3000, 'Shoes'), /*Chick Slippers*/
    (1072260, 3000, 'Shoes'), /*Penguin Slippers*/
    (1072265, 3000, 'Shoes'), /*Blue Soccer Cleats*/
    (1072266, 3000, 'Shoes'), /*Black Soccer Cleats*/
    (1072267, 3000, 'Shoes'), /*Red Soccer Cleats*/
    (1072270, 3000, 'Shoes'), /*White Rabbit Shoes*/
    (1072274, 3000, 'Shoes'), /*Moon Bunny Paws*/
    (1072276, 3000, 'Shoes'), /*Booster Shoes*/
    (1072277, 3000, 'Shoes'), /*Red Elf shoes*/
    (1072278, 3000, 'Shoes'), /*Rudolph Slippers*/
    (1072279, 3000, 'Shoes'), /*Super Booster Shoes*/
    (1072280, 3000, 'Shoes'), /*Golden Shoes*/
    (1072281, 3000, 'Shoes'), /*Sachiel Shoes*/
    (1072282, 3000, 'Shoes'), /*Veamoth Shoes*/
    (1072283, 3000, 'Shoes'), /*Janus Shoes*/
    (1072284, 3000, 'Shoes'), /*Zhu Ba Jie Shoes*/
    (1072322, 3000, 'Shoes'), /*Rollerskates*/
    (1072323, 3000, 'Shoes'), /*Starry Slippers*/
    (1072324, 3000, 'Shoes'), /*Piggy Slippers*/
    (1072325, 3000, 'Shoes'), /*Red Slime Slippers*/
    (1072326, 3000, 'Shoes'), /*Yellow Slime Slippers*/
    (1072327, 3000, 'Shoes'), /*Tania En Fuego*/
    (1072328, 3000, 'Shoes'), /*Mercury Lightning*/
    (1072329, 3000, 'Shoes'), /*Flipped Blue High Top*/
    (1072330, 3000, 'Shoes'), /*Black Classic Sneakers*/
    (1072331, 3000, 'Shoes'), /*Velcro High Tops*/
    (1072332, 3000, 'Shoes'), /*Black Enamel Shoes*/
    (1072333, 3000, 'Shoes'), /*Green Classic Sneakers*/
    (1072334, 3000, 'Shoes'), /*Red Checkered Sneakers*/
    (1072335, 3000, 'Shoes'), /*Natural Golashes*/
    (1072336, 3000, 'Shoes'), /*Soccer Cleats*/
    (1072337, 3000, 'Shoes'), /*Fluffy Slippers*/
    (1072341, 3000, 'Shoes'), /*Orange Sneakz*/
    (1072347, 3000, 'Shoes'), /*Olive Green Kicks*/
    (1072348, 3000, 'Shoes'), /*Elephant Slippers*/
    (1072349, 3000, 'Shoes'), /*Green Sneakz*/
    (1072350, 3000, 'Shoes'), /*Black High Tops*/
    (1072351, 3000, 'Shoes'), /*Green Ankle Boots for Transformation*/
    (1072352, 3000, 'Shoes'), /*Red Silky Boots for Transformation*/
    (1072353, 3000, 'Shoes'), /*White Ninja Sandals for Transformation*/
    (1072354, 3000, 'Shoes'), /*Black Voyson Shoes for Transformation*/
    (1072367, 3000, 'Shoes'), /*Cutie Birk Shoes*/
    (1072370, 3000, 'Shoes'), /*Gaga Shoes*/
    (1072371, 3000, 'Shoes'), /*Custom Blue High Tops*/
    (1072373, 3000, 'Shoes'), /*Purple Rainbow Sneaks*/
    (1072374, 3000, 'Shoes'), /*Lace Long Boots*/
    (1072377, 3000, 'Shoes'), /*Treacherous Wolf Shoes*/
    (1072379, 3000, 'Shoes'), /*Yellow Rainbow Sneaks*/
    (1072380, 3000, 'Shoes'), /*White & Blue Sandals*/
    (1072381, 3000, 'Shoes'), /*Aran Combat Shoes*/
    (1072382, 3000, 'Shoes'), /*Brave Soldier Shoes */
    (1072384, 3000, 'Shoes'), /*Bling Bling Shoes*/
    (1072385, 3000, 'Shoes'), /*White Slipshoes*/
    (1072386, 3000, 'Shoes'), /*Black Geda*/
    (1072387, 3000, 'Shoes'), /*Pink Geda*/
    (1072388, 3000, 'Shoes'), /*Stripe Knee Socks*/
    (1072389, 3000, 'Shoes'), /*Black Platform Boots*/
    (1072392, 3000, 'Shoes'), /*Red Ankle-Strap Shoes*/
    (1072393, 3000, 'Shoes'), /*We Care! Shoes*/
    (1072394, 3000, 'Shoes'), /*Pink Polka-Dotted Boots*/
    (1072395, 3000, 'Shoes'), /*Mix-n-Match Sneakers*/
    (1072397, 3000, 'Shoes'), /*Idol Star Snickers*/
    (1072398, 3000, 'Shoes'), /*Cursed Golden shoes*/
    (1072404, 3000, 'Shoes'), /*Alchemist Shoes*/
    (1072406, 3000, 'Shoes'), /*Chaos Metallic Shoes*/
    (1072407, 3000, 'Shoes'), /*Kawaii Kitty Shoes*/
    (1072408, 3000, 'Shoes'), /*Maple Racing Shoes*/
    (1072417, 3000, 'Shoes'), /*Clown Shoes*/
    (1072425, 3000, 'Shoes'), /*Freud's Shoes*/
    (1072426, 3000, 'Shoes'), /*Shiny Anklet*/
    (1072433, 3000, 'Shoes'), /*Passionate Flats*/
    (1072437, 3000, 'Shoes'), /*Pink Bean Shoes*/
    (1072438, 3000, 'Shoes'), /*Green Leaf Shoes*/
    (1072439, 3000, 'Shoes'), /*Strawberry Shoes*/
    (1072440, 3000, 'Shoes'), /*Cat Set Boots*/
    (1072441, 3000, 'Shoes'), /*Dual Blade Boots*/
    (1072443, 3000, 'Shoes'), /*Evan Golden Boots*/
    (1072444, 3000, 'Shoes'), /*Hawkeye Ocean Boots*/
    (1072448, 3000, 'Shoes'), /*Rainbow Boots*/
    (1072454, 3000, 'Shoes'), /*Oz Magic Boots*/
    (1072456, 3000, 'Shoes'), /*Evan Boots*/
    (1072457, 3000, 'Shoes'), /*Blue Slip-Ons*/
    (1072461, 3000, 'Shoes'), /*Battle Mage Boots*/
    (1072462, 3000, 'Shoes'), /*Wild Hunter Boots*/
    (1072464, 3000, 'Shoes'), /*Combat Boots*/
    (1072465, 3000, 'Shoes'), /*King Crow Kimono Shoes*/
    (1072466, 3000, 'Shoes'), /*Henesys Academy Shoes*/
    (1072467, 3000, 'Shoes'), /*Pilot Boots*/
    (1072468, 3000, 'Shoes'), /*Lolita Knee Socks Shoes*/
    (1072469, 3000, 'Shoes'), /*Striped Leggings (Pink)*/
    (1072470, 3000, 'Shoes'), /*Striped Leggings (Blue)*/
    (1072478, 3000, 'Shoes'), /*Brown Ankle Boots*/
    (1072482, 3000, 'Shoes'), /*Panda Slippers*/
    (1072483, 3000, 'Shoes'), /*White Boots*/
    (1072484, 3000, 'Shoes'), /*Black Kitty Slippers*/
    (1072495, 3000, 'Shoes'), /*Blue Sneakers*/
    (1072507, 3000, 'Shoes'), /*Pearl Anklet*/
    (1072508, 3000, 'Shoes'), /*Winter 2010 Moon Bunny Shoes*/
    (1072509, 3000, 'Shoes'), /*Red's Shoes*/
    (1072514, 3000, 'Shoes'), /*Pink Winged Shoes*/
    (1072515, 3000, 'Shoes'), /*Furry Lion Slippers*/
    (1072516, 3000, 'Shoes'), /*Rookie Chick Slippers*/
    (1072517, 3000, 'Shoes'), /*Winged Shoes*/
    (1072520, 3000, 'Shoes'), /*6th Anniversary Item*/
    (1072529, 3000, 'Shoes'), /*Pink Elephant Slippers*/
    (1072531, 3000, 'Shoes'), /*Koala Slippers*/
    (1072532, 3000, 'Shoes'), /*MSE 4 Years & Unstoppable Shoes*/
    (1072536, 3000, 'Shoes'), /*Starling Shoes*/
    (1072537, 3000, 'Shoes'), /*Crow Shoes*/
    (1072609, 3000, 'Shoes'), /*Ribboned Justice Boots*/
    (1072613, 3000, 'Shoes'), /*Western Cowboy Boots*/
    (1072622, 3000, 'Shoes'), /*Orchid's Black Wing Shoes*/
    (1072627, 3000, 'Shoes'), /*Dark Force Boots */
    (1072628, 3000, 'Shoes'), /*Elven Spirit Boots */
    (1072631, 3000, 'Shoes'), /*Urban Pirate Shoes*/
    (1072632, 3000, 'Shoes'), /*Nyanya Steward Shoes*/
    (1072633, 3000, 'Shoes'), /*GM Haku's Pirate Shoes*/
    (1072637, 3000, 'Shoes'), /*Hades Shoes*/
    (1072646, 3000, 'Shoes'), /*Elven Spirit Boots*/
    (1072647, 3000, 'Shoes'), /*Kerning Engineering School Shoes*/
    (1072648, 3000, 'Shoes'), /*Ellinia Magic School Shoes*/
    (1072649, 3000, 'Shoes'), /*Mu Lung Dojo Training Shoes*/
    (1072650, 3000, 'Shoes'), /*Blue Dragon Shoes*/
    (1072651, 3000, 'Shoes'), /*Red Dragon Shoes*/
    (1072652, 3000, 'Shoes'), /*Intergalactic Shoes*/
    (1072658, 3000, 'Shoes'), /*Glowing Foot Ring*/
    (1072662, 3000, 'Shoes'), /*Lucia Shoes*/
    (1072663, 3000, 'Shoes'), /*GM Nori's Wing Shoes*/
    (1072676, 3000, 'Shoes'), /*The Onmyouji's Shoes*/
    (1072680, 3000, 'Shoes'), /*Blue Arabian Shoes*/
    (1072681, 3000, 'Shoes'), /*Red Arabian Shoes*/
    (1072708, 3000, 'Shoes'), /*Cool Summer Flippers*/
    (1072729, 3000, 'Shoes'), /*Jett's Boots*/
    (1072742, 3000, 'Shoes'), /*Nero Paws*/
    (1072748, 3000, 'Shoes'), /*Exotic Festival Shoes*/
    (1072749, 3000, 'Shoes'), /*Bubble Bubble Chocolate Shoes*/
    (1072750, 3000, 'Shoes'), /*The Bladed Falcon's Shoes*/
    (1072756, 3000, 'Shoes'), /*Hyper Kitten Mittens*/
    (1072758, 3000, 'Shoes'), /*Kitty Slippers*/
    (1072760, 3000, 'Shoes'), /*Halloween Leopard Shoes*/
    (1072770, 3000, 'Shoes'), /*Dark Devil Shoes*/
    (1072771, 3000, 'Shoes'), /*Slither Style High-Tops*/
    (1072772, 3000, 'Shoes'), /*Pious Shaman Stockings*/
    (1072773, 3000, 'Shoes'), /*Red Strap Clogs*/
    (1072778, 3000, 'Shoes'), /*Dark Force Boots*/
    (1072779, 3000, 'Shoes'), /*Featherly Angel Shoes*/
    (1072780, 3000, 'Shoes'), /*Blue Point Kitty Shoes*/
    (1072781, 3000, 'Shoes'), /*Kitty Shoes*/
    (1072782, 3000, 'Shoes'), /*Xenon Neo-Tech Shoes*/
    (1072783, 3000, 'Shoes'), /*Lotus's Black Wing Shoes*/
    (1072791, 3000, 'Shoes'), /*Green Dinosaur Shoes*/
    (1072800, 3000, 'Shoes'), /*Mid High Golf Shoes*/
    (1072803, 3000, 'Shoes'), /*Purple Dinosaur Shoes*/
    (1072808, 3000, 'Shoes'), /*Ramling Slippers*/
    (1072809, 3000, 'Shoes'), /*Kerning Technical High Shoes*/
    (1072810, 3000, 'Shoes'), /*Ellinia Magic Academy Shoes*/
    (1072811, 3000, 'Shoes'), /*Mu Lung Academy Training Shoes*/
    (1072812, 3000, 'Shoes'), /*Kimono Sandals*/
    (1072813, 3000, 'Shoes'), /*Kimono Shoes*/
    (1072816, 3000, 'Shoes'), /*Succubus Shoes*/
    (1072817, 3000, 'Shoes'), /*Blavy Angel Shoes*/
    (1072820, 3000, 'Shoes'), /*Funky Shoes*/
    (1072821, 3000, 'Shoes'), /*Golden Bell Shoes*/
    (1072823, 3000, 'Shoes'), /*Golf Shoes*/
    (1072824, 3000, 'Shoes'), /*Angel Wing Shoes*/
    (1072830, 3000, 'Shoes'), /*GM Daejang's Lucia Shoes*/
    (1072831, 3000, 'Shoes'), /*Flame Boots*/
    (1072832, 3000, 'Shoes'), /*Pink Bean Slippers*/
    (1072836, 3000, 'Shoes'), /*Baseball Shoes*/
    (1072839, 3000, 'Shoes'), /*Shoes of Life*/
    (1072840, 3000, 'Shoes'), /*Shoes of Destruction*/
    (1072843, 3000, 'Shoes'), /*Bubble Flip Flops*/
    (1072848, 3000, 'Shoes'), /*Bloody Garter*/
    (1072851, 3000, 'Shoes'), /*Bubble Bubble Shoes*/
    (1072852, 3000, 'Shoes'), /*Superstar Shoes*/
    (1072854, 3000, 'Shoes'), /*フューチャーロイドスキンシューズ*/
    (1072855, 3000, 'Shoes'), /*フューチャーロイドネオンブーツ*/
    (1072856, 3000, 'Shoes'), /*Dawn Bear Comfy Boots*/
    (1072857, 3000, 'Shoes'), /*Odette Ballet Slippers*/
    (1072858, 3000, 'Shoes'), /*Odile Ballet Slippers*/
    (1072859, 3000, 'Shoes'), /*Cobalt Zero Shoes*/
    (1072860, 3000, 'Shoes'), /*Star Winkle*/
    (1072862, 3000, 'Shoes'), /*Heart Pudding Slippers*/
    (1072863, 3000, 'Shoes'), /*Stirkandbock Sandals*/
    (1072864, 3000, 'Shoes'), /*PSY Shoes*/
    (1072865, 3000, 'Shoes'), /*Camellia Flower Geta*/
    (1072866, 3000, 'Shoes'), /*Chocoram Doll Shoes*/
    (1072867, 3000, 'Shoes'), /*Puffram Shoes*/
    (1072868, 3000, 'Shoes'), /*Powder Flats*/
    (1072869, 3000, 'Shoes'), /*Princess of Time Heels*/
    (1072871, 3000, 'Shoes'), /*Halloweenroid Boots*/
    (1072873, 3000, 'Shoes'), /*Asuna's Shoes*/
    (1072875, 3000, 'Shoes'), /*Leafa's Shoes*/
    (1072876, 3000, 'Shoes'), /*Cacao Bear Shoes*/
    (1072878, 3000, 'Shoes'), /*Vampire Phantom Boots*/
    (1072880, 3000, 'Shoes'), /*Aran's Boots*/
    (1072881, 3000, 'Shoes'), /*Brave Aran's Boots*/
    (1072883, 3000, 'Shoes'), /*Heathcliff's Boots*/
    (1072884, 3000, 'Shoes'), /*Yui's Anklet*/
    (1072889, 3000, 'Shoes'), /*Snake High-tops*/
    (1072890, 3000, 'Shoes'), /*Mr. K's Cat Shoes*/
    (1072897, 3000, 'Shoes'), /*Blue Moccasin*/
    (1072901, 3000, 'Shoes'), /*Moonlight Marble Shoes*/
    (1072908, 3000, 'Shoes'), /*Pony Wing Shoes*/
    (1072913, 3000, 'Shoes'), /*Blue Slippers*/
    (1072916, 3000, 'Shoes'), /*Guardian Shoes*/
    (1072917, 3000, 'Shoes'), /*Cutie Horse Shoes*/
    (1072918, 3000, 'Shoes'), /*Pink Flowery Shoes*/
    (1072919, 3000, 'Shoes'), /*Blue Butterfly Shoes*/
    (1072920, 3000, 'Shoes'), /*Ghost Bride's Shoes*/
    (1072921, 3000, 'Shoes'), /*Fancy Magician Shoes*/
    (1072922, 3000, 'Shoes'), /*Chef Shoes*/
    (1072923, 3000, 'Shoes'), /*Contemporary Chic Shoes*/
    (1072924, 3000, 'Shoes'), /*Nurse Boots*/
    (1072925, 3000, 'Shoes'), /*Doctor Boots*/
    (1072926, 3000, 'Shoes'), /*Colorful Sneakers*/
    (1072934, 3000, 'Shoes'), /*Rainbow Sneakers*/
    (1072942, 3000, 'Shoes'), /*Island Travel Shoes*/
    (1072943, 3000, 'Shoes'), /*Humming Shoes*/
    (1072944, 3000, 'Shoes'), /*暗夜精灵战靴*/
    (1072945, 3000, 'Shoes'), /*隐武士战靴*/
    (1072949, 3000, 'Shoes'), /*Red Pony Sneakers*/
    (1072950, 3000, 'Shoes'), /*Blue Pony Sneakers*/
    (1072951, 3000, 'Shoes'), /*Hula Hula Beaded Anklet*/
    (1072978, 3000, 'Shoes'), /*Glowy Leather Shoes*/
    (1072979, 3000, 'Shoes'), /*Bright Angel Boots*/
    (1072980, 3000, 'Shoes'), /*Dark Devil Boots*/
    (1072998, 3000, 'Shoes'), /*Rabbit-Bear Slippers*/
    (1072999, 3000, 'Shoes'), /*Ribbon Red Shoes*/
    (1073008, 3000, 'Shoes'), /*Scarlet Sneakers*/
    (1073009, 3000, 'Shoes'), /*Corn Shoes*/
    (1073011, 3000, 'Shoes'), /*Cheerleader Shoes*/
    (1073013, 3000, 'Shoes'), /*Wiggly Puppy Shoes*/
    (1073014, 3000, 'Shoes'), /*Pink Puppy Shoes*/
    (1073019, 3000, 'Shoes'), /*Dinofrog Shoes*/
    (1073022, 3000, 'Shoes'), /*Pink Kitty Blue Sneakers*/
    (1073024, 3000, 'Shoes'), /*Red Shoes*/
    (1073025, 3000, 'Shoes'), /*Hatchling Shoes*/
    (1073027, 3000, 'Shoes'), /*ODM Gear*/
    (1073036, 3000, 'Shoes'), /*Blue Bird Shoes*/
    (1073037, 3000, 'Shoes'), /*Cutie Bunny Shoes*/
    (1073038, 3000, 'Shoes'), /*Soft Pink Boots*/
    (1073040, 3000, 'Shoes'), /*Maple Mouse Shoes*/
    (1073041, 3000, 'Shoes'), /*Black Forte Boots*/
    (1073044, 3000, 'Shoes'), /*-*/
    (1073046, 3000, 'Shoes'), /*Baby Ram Slippers (Blue)*/
    (1073047, 3000, 'Shoes'), /*Baby Ram Slippers (Pink)*/
    (1073050, 3000, 'Shoes'), /*Ring Sneakers*/
    (1073051, 3000, 'Shoes'), /*Ryan D Shoes*/
    (1073052, 3000, 'Shoes'), /*Sierra Grace Boots */
    (1073055, 3000, 'Shoes'), /*Akarin's Flowery Shoes*/
    (1073056, 3000, 'Shoes'), /*Blooming Spring*/
    (1073058, 3000, 'Shoes'), /*Naughty Boy Shoes*/
    (1073059, 3000, 'Shoes'), /*Cat Knee Socks*/
    (1073060, 3000, 'Shoes'), /*Noble Blossom Shoes*/
    (1073061, 3000, 'Shoes'), /*Pink Blossom Shoes*/
    (1073062, 3000, 'Shoes'), /*Cottontail Rabbit Shoes*/
    (1073074, 3000, 'Shoes'), /*Schwarzer Boots*/
    (1073075, 3000, 'Shoes'), /*Mint Kitty Slippers*/
    (1073079, 3000, 'Shoes'), /*Jelly Shoes*/
    (1073080, 3000, 'Shoes'), /*Black Sailor Shoes*/
    (1073084, 3000, 'Shoes'), /*Kinesis Shoes*/
    (1073088, 3000, 'Shoes'), /*Bluebird Shoes*/
    (1073090, 3000, 'Shoes'), /*White Ursus Slippers*/
    (1073091, 3000, 'Shoes'), /*Brown Ursus Slippers*/
    (1073092, 3000, 'Shoes'), /*Black Ursus Slippers*/
    (1073096, 3000, 'Shoes'), /*Little Vampire Shoes*/
    (1073169, 3000, 'Shoes'), /*Bichon Shoes*/
    (1073105, 3000, 'Shoes'), /*Exciting Kicks*/
    (1073106, 3000, 'Shoes'), /*Polar Booties*/
    (1073107, 3000, 'Shoes'), /*Wooden Bell Shoes*/
    (1073108, 3000, 'Shoes'), /*Flutter Bell Sandals*/
    (1073115, 3000, 'Shoes'), /*Evan Dragon Boots*/
    (1073117, 3000, 'Shoes'), /*Royal Mercedes Shoes*/
    (1073119, 3000, 'Shoes'), /*Mystic Phantom Shoes*/
    (1073121, 3000, 'Shoes'), /*Winter Aran Boots*/
    (1073123, 3000, 'Shoes'), /*Chiaroscuro Luminous Shoes*/
    (1073125, 3000, 'Shoes'), /*Secret Shade Boots*/
    (1073127, 3000, 'Shoes'), /*Cozy Fluffy Slippers*/
    (1073128, 3000, 'Shoes'), /*Snow Boots*/
    (1073132, 3000, 'Shoes'), /*Umbral Shoes*/
    (1073133, 3000, 'Shoes'), /*Umbral Boots*/
    (1073134, 3000, 'Shoes'), /*Flower Dancer's Sandals*/
    (1073135, 3000, 'Shoes'), /*Moon Dancer's Boots*/
    (1073144, 3000, 'Shoes'), /*Shark Bite Shoes*/
    (1073145, 3000, 'Shoes'), /*Kitty Follower*/
    (1073150, 3000, 'Shoes'), /*Chicken Cutie Shoes*/
    (1073153, 3000, 'Shoes'), /*Blaster Shoes*/
    (1073155, 3000, 'Shoes'), /*Villain Shoes*/
    (1073156, 3000, 'Shoes'), /*Colorful Beach Sandals*/
    (1073157, 3000, 'Shoes'), /*Red Cloud Shoes*/
    (1073167, 3000, 'Shoes'), /*Dark Musician Shoes*/
    (1073168, 3000, 'Shoes'), /*Chained Princess Shoes*/

/* Gloves */
    (1080000, 3000, 'Gloves'), /*White Ninja Gloves*/
    (1080001, 3000, 'Gloves'), /*Wedding Gloves*/
    (1080007, 3000, 'Gloves'), /*크리스토프 장갑*/
    (1080008, 3000, 'Gloves'), /*Whip Cream Pon Pon*/
    (1080009, 3000, 'Gloves'), /*Penguin Gloves*/
    (1081000, 3000, 'Gloves'), /*Red Ninja Gloves*/
    (1081001, 3000, 'Gloves'), /*Blue Ninja Gloves*/
    (1081003, 3000, 'Gloves'), /*White Cat Gloves*/
    (1081004, 3000, 'Gloves'), /*Black Cat Gloves*/
    (1081006, 3000, 'Gloves'), /*Elizabeth Gloves*/
    (1081013, 3000, 'Gloves'), /*엘사 장갑*/
    (1081014, 3000, 'Gloves'), /*Whip Cream Bon Bon*/
    (1082040, 3000, 'Gloves'), /*Red Boxing Gloves*/
    (1082041, 3000, 'Gloves'), /*Blue Boxing Gloves*/
    (1082057, 3000, 'Gloves'), /*Brown Baseball Glove*/
    (1082058, 3000, 'Gloves'), /*Blue Baseball Glove*/
    (1082077, 3000, 'Gloves'), /*White Bandage*/
    (1082078, 3000, 'Gloves'), /*Brown Bandage*/
    (1082079, 3000, 'Gloves'), /*Black Bandage*/
    (1082101, 3000, 'Gloves'), /*Santa Gloves*/
    (1082102, 3000, 'Gloves'), /*Transparent Gloves*/
    (1082113, 3000, 'Gloves'), /*Hair-Cutter Gloves*/
    (1082124, 3000, 'Gloves'), /*Mesoranger Gloves*/
    (1082155, 3000, 'Gloves'), /*Snowman Gloves*/
    (1082156, 3000, 'Gloves'), /*Teddy Bear Gloves*/
    (1082157, 3000, 'Gloves'), /*Skull Gloves*/
    (1082161, 3000, 'Gloves'), /*Star Gloves*/
    (1082162, 3000, 'Gloves'), /*Love Gloves*/
    (1082165, 3000, 'Gloves'), /*White Rabbit Gloves*/
    (1082166, 3000, 'Gloves'), /*Nero Gloves*/
    (1082169, 3000, 'Gloves'), /*Moon Bunny Gloves*/
    (1082170, 3000, 'Gloves'), /*Rose Crystal Watch*/
    (1082171, 3000, 'Gloves'), /*Blue Watch*/
    (1082172, 3000, 'Gloves'), /*Snowflake Gloves*/
    (1082173, 3000, 'Gloves'), /*Lightning Gloves*/
    (1082224, 3000, 'Gloves'), /*Tania Gloves*/
    (1082225, 3000, 'Gloves'), /*Mercury Gloves*/
    (1082227, 3000, 'Gloves'), /*Skull Tattoo*/
    (1082229, 3000, 'Gloves'), /*Heart Ribbon Glove*/
    (1082231, 3000, 'Gloves'), /*Luxury Wristwatch*/
    (1082233, 3000, 'Gloves'), /*Moomoo Gloves*/
    (1082247, 3000, 'Gloves'), /*Cutie Birk Gloves*/
    (1082249, 3000, 'Gloves'), /*Neon Amulet*/
    (1082250, 3000, 'Gloves'), /*Treacherous Wolf Gloves*/
    (1082251, 3000, 'Gloves'), /*Rock Chain Armlet*/
    (1082253, 3000, 'Gloves'), /*Neon Sign Amulet*/
    (1082255, 3000, 'Gloves'), /*Maple Racing Glove*/
    (1082261, 3000, 'Gloves'), /*Freud's Gloves*/
    (1082263, 3000, 'Gloves'), /*Bunny Gloves*/
    (1082267, 3000, 'Gloves'), /*Cat Set Mittens*/
    (1082268, 3000, 'Gloves'), /*Dual Blade Gloves*/
    (1082272, 3000, 'Gloves'), /*Evan Golden Gloves*/
    (1082273, 3000, 'Gloves'), /*Hawkeye Ocean Gloves*/
    (1082274, 3000, 'Gloves'), /*Evan Gloves*/
    (1082282, 3000, 'Gloves'), /*Battle Mage Gloves*/
    (1082310, 3000, 'Gloves'), /*Winter 2011 Moon Bunny Gloves*/
    (1082312, 3000, 'Gloves'), /*Rainbow Bracelet*/
    (1082407, 3000, 'Gloves'), /*Dark Force Gloves */
    (1082408, 3000, 'Gloves'), /*Elven Spirit Gloves*/
    (1082421, 3000, 'Gloves'), /*Blue Dragon Gloves*/
    (1082422, 3000, 'Gloves'), /*Red Dragon Gloves*/
    (1082423, 3000, 'Gloves'), /*Intergalactic Gloves*/
    (1082448, 3000, 'Gloves'), /*Arabian Gold Bracelet*/
    (1082493, 3000, 'Gloves'), /*Harp Seal Doll Gloves*/
    (1082495, 3000, 'Gloves'), /*Cat Lolita Gloves*/
    (1082500, 3000, 'Gloves'), /*Dark Devil Gloves*/
    (1082501, 3000, 'Gloves'), /*Dark Force Gloves*/
    (1082502, 3000, 'Gloves'), /*Blue Point Kitty Gloves*/
    (1082503, 3000, 'Gloves'), /*Featherly Angel Gloves*/
    (1082504, 3000, 'Gloves'), /*Kitty Gloves*/
    (1082505, 3000, 'Gloves'), /*Xenon Neo-Tech Gloves*/
    (1082511, 3000, 'Gloves'), /*Green Dinosaur Gloves*/
    (1082517, 3000, 'Gloves'), /*Golf Gloves*/
    (1082519, 3000, 'Gloves'), /*Purple Dinosaur Gloves*/
    (1082520, 3000, 'Gloves'), /*Ramling Fur Glove*/
    (1082694, 3000, 'Gloves'), /*Villain Gloves*/
    (1082524, 3000, 'Gloves'), /*Blavy Angel Bangle*/
    (1082525, 3000, 'Gloves'), /*Succubus Gloves*/
    (1082702, 3000, 'Gloves'), /*Kamaitachi Gloves*/
    (1082703, 3000, 'Gloves'), /*Bichon Gloves*/
    (1082548, 3000, 'Gloves'), /*Star Bracelet*/
    (1082549, 3000, 'Gloves'), /*Chicken Glovaroo*/
    (1082550, 3000, 'Gloves'), /*White Ghostly Cloth*/
    (1082551, 3000, 'Gloves'), /*Chocoram Doll Gloves*/
    (1082552, 3000, 'Gloves'), /*Puffram Gloves*/
    (1082554, 3000, 'Gloves'), /*Princess of Time Gloves*/
    (1082555, 3000, 'Gloves'), /*Fairy Spark*/
    (1082558, 3000, 'Gloves'), /*Kirito's Gloves*/
    (1082563, 3000, 'Gloves'), /*Heathcliff's Gloves*/
    (1082564, 3000, 'Gloves'), /*Yui's Gloves*/
    (1082565, 3000, 'Gloves'), /*Chocolate Ribbon*/
    (1082571, 3000, 'Gloves'), /*Mr. K's Cat Gloves*/
    (1082580, 3000, 'Gloves'), /*Pony Gloves*/
    (1082585, 3000, 'Gloves'), /*Guardian Gloves*/
    (1082587, 3000, 'Gloves'), /*Pink Panda Gloves*/
    (1082588, 3000, 'Gloves'), /*Rainbow Marbles*/
    (1082592, 3000, 'Gloves'), /*Burning Ghost Wristband*/
    (1082620, 3000, 'Gloves'), /*Aloha Flower Accessory*/
    (1082623, 3000, 'Gloves'), /*Bright Angel Gloves*/
    (1082631, 3000, 'Gloves'), /*Glowing Bracelet*/
    (1082632, 3000, 'Gloves'), /*Worn Skull Gloves*/
    (1082634, 3000, 'Gloves'), /*Dinofrog Gloves*/
    (1082641, 3000, 'Gloves'), /*Blue Bird Gloves*/
    (1082657, 3000, 'Gloves'), /*Blue Panda Doll Gloves*/
    (1082666, 3000, 'Gloves'), /*White Ursus Gloves*/
    (1082667, 3000, 'Gloves'), /*Brown Ursus Gloves*/
    (1082668, 3000, 'Gloves'), /*Black Ursus Gloves*/
    (1082684, 3000, 'Gloves'), /*Beaky Owl Gloves*/
    (1082689, 3000, 'Gloves'), /*Paw Gloves*/
    (1082692, 3000, 'Gloves'), /*Candybear Watch*/

/* Accessories */

    (1010000, 3000, 'Accessories'), /*Long Brown Beard*/
    (1010001, 3000, 'Accessories'), /*Goatee*/
    (1010002, 3000, 'Accessories'), /*Ninja Mask for Men*/
    (1010003, 3000, 'Accessories'), /*5 O'Clock Shadow*/
    (1010004, 3000, 'Accessories'), /*General's Mustache (1)*/
    (1010005, 3000, 'Accessories'), /*General's Mustache (2)*/
    (1010006, 3000, 'Accessories'), /*Yakuza Scar*/
    (1010007, 3000, 'Accessories'), /*Cold Make-up*/
    (1011000, 3000, 'Accessories'), /*Ninja Mask for Women*/
    (1011001, 3000, 'Accessories'), /*SF Ninja Mask*/
    (1011002, 3000, 'Accessories'), /*Heart*/
    (1011003, 3000, 'Accessories'), /*Freckles*/
    (1011006, 3000, 'Accessories'), /*Soulful Make-up*/
    (1012000, 3000, 'Accessories'), /*Battle Scar*/
    (1012001, 3000, 'Accessories'), /*Bindi*/
    (1012002, 3000, 'Accessories'), /*Leather Mask*/
    (1012003, 3000, 'Accessories'), /*Rouge*/
    (1012004, 3000, 'Accessories'), /*Camo Face Paint*/
    (1012005, 3000, 'Accessories'), /*Bruise*/
    (1012006, 3000, 'Accessories'), /*Rose*/
    (1012007, 3000, 'Accessories'), /*Santa Beard*/
    (1012008, 3000, 'Accessories'), /*Censor*/
    (1012009, 3000, 'Accessories'), /*Kiss Mark*/
    (1012010, 3000, 'Accessories'), /*Hinomaru*/
    (1012021, 3000, 'Accessories'), /*White Kabuki Mask*/
    (1012022, 3000, 'Accessories'), /*Red Kabuki Mask*/
    (1012023, 3000, 'Accessories'), /*Yellow Kabuki Mask*/
    (1012024, 3000, 'Accessories'), /*Gentleman's Mustache*/
    (1012025, 3000, 'Accessories'), /*War Paint*/
    (1012026, 3000, 'Accessories'), /*Guan Yu Beard*/
    (1012027, 3000, 'Accessories'), /*Bandage Strip*/
    (1012028, 3000, 'Accessories'), /*Blush*/
    (1012029, 3000, 'Accessories'), /*Jester Mask*/
    (1012030, 3000, 'Accessories'), /*Eye Scar*/
    (1012031, 3000, 'Accessories'), /*Leaf*/
    (1012032, 3000, 'Accessories'), /*White Bread*/
    (1012033, 3000, 'Accessories'), /*England Face Painting*/
    (1012034, 3000, 'Accessories'), /*Tri-color Paint (France)*/
    (1012035, 3000, 'Accessories'), /*Brazillian Paint (Brazil)*/
    (1012036, 3000, 'Accessories'), /*Bundes Paint (Germany)*/
    (1012037, 3000, 'Accessories'), /*Armillary Shield Paint (Portugal)*/
    (1012038, 3000, 'Accessories'), /*Rising Sun Paint (Japan)*/
    (1012039, 3000, 'Accessories'), /*Taegeuk Paint (Korea)*/
    (1012040, 3000, 'Accessories'), /*Heart Face Painting*/
    (1012041, 3000, 'Accessories'), /*Star Spangled Paint (USA)*/
    (1012042, 3000, 'Accessories'), /*Aztec Paint (Mexico)*/
    (1012043, 3000, 'Accessories'), /*Australia Face Painting*/
    (1012044, 3000, 'Accessories'), /*Mummy Mask*/
    (1012047, 3000, 'Accessories'), /*Fu Manchu*/
    (1012048, 3000, 'Accessories'), /*Dark Jack's Scar*/
    (1012049, 3000, 'Accessories'), /*Ogre Mask*/
    (1012050, 3000, 'Accessories'), /*Maple-Stein Face*/
    (1012051, 3000, 'Accessories'), /*Dark Jester*/
    (1012052, 3000, 'Accessories'), /*Tongue Twister Scroll*/
    (1012053, 3000, 'Accessories'), /*Unmanaged Anger*/
    (1012054, 3000, 'Accessories'), /*Purple Rage*/
    (1012055, 3000, 'Accessories'), /*Allergic Reaction*/
    (1012056, 3000, 'Accessories'), /*Doggy Mouth*/
    (1012057, 3000, 'Accessories'), /*Transparent Face Accessory*/
    (1012062, 3000, 'Accessories'), /*Mild Pink Lipstick*/
    (1012063, 3000, 'Accessories'), /*Kitty Paint*/
    (1012074, 3000, 'Accessories'), /*Mocking Laughter*/
    (1012075, 3000, 'Accessories'), /*Cold Sweat*/
    (1012080, 3000, 'Accessories'), /*Fat Lips*/
    (1012081, 3000, 'Accessories'), /*MV Mask*/
    (1012082, 3000, 'Accessories'), /*Ice Cold Red*/
    (1012083, 3000, 'Accessories'), /*Dollish Pink*/
    (1012085, 3000, 'Accessories'), /*Cherry Bubblegum*/
    (1012090, 3000, 'Accessories'), /*Facial Powder*/
    (1012096, 3000, 'Accessories'), /*Apple Bubble Gum*/
    (1012097, 3000, 'Accessories'), /*Purple Noisemaker*/
    (1012099, 3000, 'Accessories'), /*Facial Powder(blue)*/
    (1012100, 3000, 'Accessories'), /*Facial Powder(red)*/
    (1012105, 3000, 'Accessories'), /*Super Sucker*/
    (1012112, 3000, 'Accessories'), /*Bauhinia Paint (Hong Kong)*/
    (1012113, 3000, 'Accessories'), /*ROC Paint (Taiwan)*/
    (1012114, 3000, 'Accessories'), /*5-Starred Red Paint (China)*/
    (1012121, 3000, 'Accessories'), /*Coat of Arms Paint (Spain)*/
    (1012122, 3000, 'Accessories'), /*Gold Nordic Paint (Sweden)*/
    (1012123, 3000, 'Accessories'), /*Holland Paint (Netherlands)*/
    (1012124, 3000, 'Accessories'), /*Union Paint (UK)*/
    (1012125, 3000, 'Accessories'), /*Chakra Paint (Thailand)*/
    (1012126, 3000, 'Accessories'), /*Yellow Star Paint (Vietnam)*/
    (1012127, 3000, 'Accessories'), /*Crescent Paint (Singapore)*/
    (1012128, 3000, 'Accessories'), /*Jalur Gemilang Paint (Malaysia)*/
    (1012129, 3000, 'Accessories'), /*Maple Leaf Paint  (Canada)*/
    (1012131, 3000, 'Accessories'), /*Smiling Face*/
    (1012572, 3000, 'Accessories'), /*Little Kitten Face Accessory*/
    (1012134, 3000, 'Accessories'), /*Tear Drop Face Tattoo*/
    (1012137, 3000, 'Accessories'), /*Star Face Painting*/
    (1012147, 3000, 'Accessories'), /*Immortal Mask*/
    (1012159, 3000, 'Accessories'), /*Foxy Mask*/
    (1012562, 3000, 'Accessories'), /*Heartbeam Face*/
    (1012165, 3000, 'Accessories'), /*Clown Nose*/
    (1012166, 3000, 'Accessories'), /*Villain Mask*/
    (1012176, 3000, 'Accessories'), /*Orange Blush*/
    (1012179, 3000, 'Accessories'), /*Reindeer Red Nose*/
    (1012180, 3000, 'Accessories'), /*Chocolate Heart*/
    (1012192, 3000, 'Accessories'), /*Shadow Mask*/
    (1012208, 3000, 'Accessories'), /*Lovely Smile*/
    (1012253, 3000, 'Accessories'), /*Heart Pounding Lip Gloss*/
    (1012275, 3000, 'Accessories'), /*6th Anniversary Party Glasses*/
    (1012298, 3000, 'Accessories'), /*Hand Mark*/
    (1012315, 3000, 'Accessories'), /*Adhesive Bandage*/
    (1012366, 3000, 'Accessories'), /*Zombie Hunter Mask*/
    (1012374, 3000, 'Accessories'), /*Heartbreaker Lips*/
    (1012379, 3000, 'Accessories'), /*Flushed Cheeks*/
    (1012384, 3000, 'Accessories'), /*Playful Band*/
    (1012388, 3000, 'Accessories'), /*Clown*/
    (1012390, 3000, 'Accessories'), /*Peruvian Flag Face Paint*/
    (1012412, 3000, 'Accessories'), /*Bloody Tears*/
    (1012413, 3000, 'Accessories'), /*Naked Face*/
    (1012415, 3000, 'Accessories'), /*Blingin' Red Lipstick*/
    (1012427, 3000, 'Accessories'), /*Surprised Face Accessory*/
    (1012428, 3000, 'Accessories'), /*Thick Eyebrows Face Accessory*/
    (1012429, 3000, 'Accessories'), /*Round Eyes-And-Mouth Face Accessory*/
    (1012430, 3000, 'Accessories'), /*Bear Nose Face Accessory*/
    (1012431, 3000, 'Accessories'), /*Straight Face Accessory*/
    (1012432, 3000, 'Accessories'), /*Cat-Mouth Face Accessory*/
    (1012433, 3000, 'Accessories'), /*Animal Face Accessory*/
    (1012434, 3000, 'Accessories'), /*Mustache Face Accessory*/
    (1012435, 3000, 'Accessories'), /*Gross Face Accessory*/
    (1012436, 3000, 'Accessories'), /*Enlightened Face Accessory*/
    (1012437, 3000, 'Accessories'), /*Palm Print Mask*/
    (1012450, 3000, 'Accessories'), /*Choco Candy Cookie*/
    (1012462, 3000, 'Accessories'), /*Ghost Bride's Shining Dark Eyes*/
    (1012468, 3000, 'Accessories'), /*Yummy Candy*/
    (1012472, 3000, 'Accessories'), /*레인보우 페인팅*/
    (1012473, 3000, 'Accessories'), /*So Angry!!! Face*/
    (1012474, 3000, 'Accessories'), /*Clobber*/
    (1012475, 3000, 'Accessories'), /*태극 페인팅*/
    (1012479, 3000, 'Accessories'), /*Ruddy Kitten Nose*/
    (1012482, 3000, 'Accessories'), /*Quack Quack*/
    (1012485, 3000, 'Accessories'), /*Sweet Persimmon Blush*/
    (1012486, 3000, 'Accessories'), /*Hothead*/
    (1012487, 3000, 'Accessories'), /*Oozer*/
    (1012489, 3000, 'Accessories'), /*LaLaLa Megaphone*/
    (1012494, 3000, 'Accessories'), /*Worn Skull Mask*/
    (1012495, 3000, 'Accessories'), /*Skull Mask*/
    (1012501, 3000, 'Accessories'), /*No Biting!*/
    (1012502, 3000, 'Accessories'), /*Frosty Frozen Face*/
    (1012569, 3000, 'Accessories'), /*Baby Binkie*/
    (1012567, 3000, 'Accessories'), /*Touched Tears*/
    (1012568, 3000, 'Accessories'), /*Shocked Eyes*/
    (1012509, 3000, 'Accessories'), /*Hange's Glasses*/
    (1012510, 3000, 'Accessories'), /*Sasha's Delicious Bread*/
    (1012511, 3000, 'Accessories'), /*Cleaning Mask*/
    (1012514, 3000, 'Accessories'), /*Heart-Shaped Chocolate*/
    (1012515, 3000, 'Accessories'), /*Strawberry Cake*/
    (1012517, 3000, 'Accessories'), /*Vellum Mask*/
    (1012518, 3000, 'Accessories'), /*Von Bon Mask*/
    (1012525, 3000, 'Accessories'), /*Smile! It's the Sweet Maple Festival!*/
    (1012526, 3000, 'Accessories'), /*So Delish Ice Cream*/
    (1012527, 3000, 'Accessories'), /*Blushing Yeti*/
    (1012528, 3000, 'Accessories'), /*Boss Lotus Eyes*/
    (1012533, 3000, 'Accessories'), /*Spring Cloud Piece*/
    (1012534, 3000, 'Accessories'), /*Rainbow Face Paint*/
    (1012544, 3000, 'Accessories'), /*Culnesis*/
    (1012551, 3000, 'Accessories'), /*The Mighty Face*/
    (1012552, 3000, 'Accessories'), /*Pink Bean Sadface*/
    (1012555, 3000, 'Accessories'), /*Vampire Eyes (Sapphire)*/
    (1012556, 3000, 'Accessories'), /*Vampire Eyes (Ruby)*/
    (1012557, 3000, 'Accessories'), /*Nom Nom Oz*/

    (1032024, 3000, 'Accessories'), /*Transparent Earrings*/
    (1032029, 3000, 'Accessories'), /*Silver Earrings*/
    (1032034, 3000, 'Accessories'), /*Coke Earrings*/
    (1032036, 3000, 'Accessories'), /*Beaded Cross Earrings*/
    (1032038, 3000, 'Accessories'), /*Snow Earrings*/
    (1032051, 3000, 'Accessories'), /*Diamond Earrings*/
    (1032052, 3000, 'Accessories'), /*Slime Earrings*/
    (1032053, 3000, 'Accessories'), /*Clover Earrings*/
    (1032054, 3000, 'Accessories'), /*Rainbow Earrings*/
    (1032063, 3000, 'Accessories'), /*Wireless Headset*/
    (1032071, 3000, 'Accessories'), /*Altair Earrings*/
    (1032072, 3000, 'Accessories'), /*Shiny Altair Earrings*/
    (1032073, 3000, 'Accessories'), /*Wind Bell Earrings*/
    (1032074, 3000, 'Accessories'), /*Heart Rainbow Earrings*/
    (1032138, 3000, 'Accessories'), /*Dragon Spirit Earrings*/
    (1032145, 3000, 'Accessories'), /*Crab Earrings*/
    (1032175, 3000, 'Accessories'), /*Faraway Earring*/
    (1032192, 3000, 'Accessories'), /*Broken Up Today*/
    (1032204, 3000, 'Accessories'), /*フューチャーロイドサイバーイヤリング*/
    (1032228, 3000, 'Accessories'), /*Halloweenroid Sensor*/
    (1032233, 3000, 'Accessories'), /*Warm-hearted Earrings*/
    (1032234, 3000, 'Accessories'), /*Cold-hearted Earrings*/
    (1032255, 3000, 'Accessories'), /*White Earphones*/
    (1032260, 3000, 'Accessories'), /*Golden Bell Drops*/
    (1032262, 3000, 'Accessories'), /*Umbral Earrings*/

/* Eye Accessories */
    (1020000, 3000, 'Eye Accessories'), /*Aqua Toy Shades*/
    (1021000, 3000, 'Eye Accessories'), /*Pink Toy Shades*/
    (1022000, 3000, 'Eye Accessories'), /*Orange Shades*/
    (1022001, 3000, 'Eye Accessories'), /*Blue Shades*/
    (1022002, 3000, 'Eye Accessories'), /*Yellow Shades*/
    (1022003, 3000, 'Eye Accessories'), /*Green Shades*/
    (1022004, 3000, 'Eye Accessories'), /*Black Sunglasses*/
    (1022005, 3000, 'Eye Accessories'), /*Red Hard-Rimmed Glasses*/
    (1022006, 3000, 'Eye Accessories'), /*Blue Hard-Rimmed Glasses*/
    (1022007, 3000, 'Eye Accessories'), /*Green Hard-Rimmed Glasses*/
    (1022008, 3000, 'Eye Accessories'), /*Orange Hard-Rimmed Glasses*/
    (1022009, 3000, 'Eye Accessories'), /*Dark Shades*/
    (1022010, 3000, 'Eye Accessories'), /*Blue & Red Eye Guard*/
    (1022011, 3000, 'Eye Accessories'), /*Red Eye Guard*/
    (1022012, 3000, 'Eye Accessories'), /*Blue Eye Guard*/
    (1022013, 3000, 'Eye Accessories'), /*Black Eye Guard*/
    (1022014, 3000, 'Eye Accessories'), /*Brown Aviator Shades*/
    (1022015, 3000, 'Eye Accessories'), /*Black Aviator Shades*/
    (1022016, 3000, 'Eye Accessories'), /*Blue Aviator Shades*/
    (1022017, 3000, 'Eye Accessories'), /*Purple Aviator Shades*/
    (1022018, 3000, 'Eye Accessories'), /*Classic Masquerade Mask*/
    (1022019, 3000, 'Eye Accessories'), /*Old-School Glasses*/
    (1022020, 3000, 'Eye Accessories'), /*Metal Shades*/
    (1022021, 3000, 'Eye Accessories'), /*Red Head-Spinning Glasses*/
    (1022022, 3000, 'Eye Accessories'), /*Blue Head-Spinning Glasses*/
    (1022023, 3000, 'Eye Accessories'), /*Crested Eye Patch*/
    (1022024, 3000, 'Eye Accessories'), /*Skull Patch*/
    (1022025, 3000, 'Eye Accessories'), /*Red Hearted Eye Patch*/
    (1022026, 3000, 'Eye Accessories'), /*Purple Starred Eye Patch*/
    (1022027, 3000, 'Eye Accessories'), /*Medical Eye Patch*/
    (1022028, 3000, 'Eye Accessories'), /*Spinning Groucho*/
    (1022029, 3000, 'Eye Accessories'), /*Spinning Piglet*/
    (1022030, 3000, 'Eye Accessories'), /*Hot Teacher Glasses*/
    (1022031, 3000, 'Eye Accessories'), /*White Toy Shades*/
    (1022032, 3000, 'Eye Accessories'), /*Yellow Toy Shades*/
    (1022033, 3000, 'Eye Accessories'), /*Politician Glasses*/
    (1022034, 3000, 'Eye Accessories'), /*Bizarre Monocle*/
    (1022035, 3000, 'Eye Accessories'), /*Orange Sports Goggle*/
    (1022036, 3000, 'Eye Accessories'), /*Green Sports Goggle*/
    (1022037, 3000, 'Eye Accessories'), /*Frameless Glasses*/
    (1022038, 3000, 'Eye Accessories'), /*Purple Round Shades*/
    (1022039, 3000, 'Eye Accessories'), /*Orange Round Shades*/
    (1022040, 3000, 'Eye Accessories'), /*Lead Monocle*/
    (1022041, 3000, 'Eye Accessories'), /*Cyclist Shades*/
    (1022042, 3000, 'Eye Accessories'), /*Scouter*/
    (1022043, 3000, 'Eye Accessories'), /*Head Bandage*/
    (1022044, 3000, 'Eye Accessories'), /*Nerdy Glasses*/
    (1022045, 3000, 'Eye Accessories'), /*Red Bushido Bandana*/
    (1022046, 3000, 'Eye Accessories'), /*Butterfly Ball Mask*/
    (1022047, 3000, 'Eye Accessories'), /*Owl Ball Mask*/
    (1022048, 3000, 'Eye Accessories'), /*Transparent Eye Accessory*/
    (1022049, 3000, 'Eye Accessories'), /*Green-Rimmed Glasses*/
    (1022050, 3000, 'Eye Accessories'), /*Vintage Glasses*/
    (1022051, 3000, 'Eye Accessories'), /*Red Half-Rim Glasses*/
    (1022052, 3000, 'Eye Accessories'), /*Future Vision Shades*/
    (1022053, 3000, 'Eye Accessories'), /*Futuristic Shades*/
    (1022054, 3000, 'Eye Accessories'), /*Round Shield Shades*/
    (1022055, 3000, 'Eye Accessories'), /*Pink Sunglasses*/
    (1022056, 3000, 'Eye Accessories'), /*Pink Aviator Sunglasses*/
    (1022057, 3000, 'Eye Accessories'), /*Pop-Eye*/
    (1022059, 3000, 'Eye Accessories'), /*Black Shades*/
    (1022061, 3000, 'Eye Accessories'), /*Redbeard's Pirate Eye Patch*/
    (1022062, 3000, 'Eye Accessories'), /*Black Skull Eye Patch*/
    (1022063, 3000, 'Eye Accessories'), /*Flat Mini Glasses*/
    (1022064, 3000, 'Eye Accessories'), /*Big Red Glasses*/
    (1022065, 3000, 'Eye Accessories'), /*Alphabet Glasses*/
    (1022066, 3000, 'Eye Accessories'), /*Star Spectacles*/
    (1022068, 3000, 'Eye Accessories'), /*White Shades*/
    (1022069, 3000, 'Eye Accessories'), /*Orange Shutter Shades*/
    (1022070, 3000, 'Eye Accessories'), /*Green Shutter Shades*/
    (1022071, 3000, 'Eye Accessories'), /*Red Shutter Shades*/
    (1022072, 3000, 'Eye Accessories'), /*Yellow Shutter Shades*/
    (1022074, 3000, 'Eye Accessories'), /*Gaga Glasses*/
    (1022075, 3000, 'Eye Accessories'), /*Twinkling Eyes*/
    (1022079, 3000, 'Eye Accessories'), /*Clear Glasses*/
    (1022081, 3000, 'Eye Accessories'), /*Cracked Glasses*/
    (1022083, 3000, 'Eye Accessories'), /*Hitman Sunglasses*/
    (1022084, 3000, 'Eye Accessories'), /*Eye Mask (Red)*/
    (1022085, 3000, 'Eye Accessories'), /*Pink Eye Mask*/
    (1022086, 3000, 'Eye Accessories'), /*Blue Eye Mask*/
    (1022087, 3000, 'Eye Accessories'), /*Green Eye Mask*/
    (1022095, 3000, 'Eye Accessories'), /*I Like Money*/
    (1022102, 3000, 'Eye Accessories'), /*LED Sunglasses*/
    (1022104, 3000, 'Eye Accessories'), /*3D Glasses*/
    (1022108, 3000, 'Eye Accessories'), /*Yellow Two-Toned Shades*/
    (1022109, 3000, 'Eye Accessories'), /*Pink Two-Toned Shades*/
    (1022110, 3000, 'Eye Accessories'), /*Big White Sunglasses*/
    (1022121, 3000, 'Eye Accessories'), /*X-Ray Glasses*/
    (1022122, 3000, 'Eye Accessories'), /*6th B-Day Party Glasses*/
    (1022173, 3000, 'Eye Accessories'), /*Silky Black Eye Patch*/
    (1022176, 3000, 'Eye Accessories'), /*Cyclops Bandana*/
    (1022177, 3000, 'Eye Accessories'), /*Star Sunglasses*/
    (1022183, 3000, 'Eye Accessories'), /*Blazing Eyes*/
    (1022184, 3000, 'Eye Accessories'), /*Frozen Eye*/
    (1022188, 3000, 'Eye Accessories'), /*Blank Eye Patch*/
    (1022194, 3000, 'Eye Accessories'), /*Blaze Black Eye*/
    (1022196, 3000, 'Eye Accessories'), /*Money Lover*/
    (1022201, 3000, 'Eye Accessories'), /*フューチャーロイド VR バイザー*/
    (1022207, 3000, 'Eye Accessories'), /*PSY Sunglasses*/
    (1022223, 3000, 'Eye Accessories'), /*Romantic LED Sunglasses*/
    (1022227, 3000, 'Eye Accessories'), /*Aviator Shades*/
    (1022229, 3000, 'Eye Accessories'), /*VIP Glasses*/
    (1022230, 3000, 'Eye Accessories'), /*Bunny Glasses*/
    (1022243, 3000, 'Eye Accessories'), /*Donut Glasses*/
    (1022244, 3000, 'Eye Accessories'), /*Damien's Eyepatch*/
    (1022248, 3000, 'Eye Accessories'), /*Pineapple Glasses*/
    (1022249, 3000, 'Eye Accessories'), /*스마트안경*/
    (1022250, 3000, 'Eye Accessories'), /*투시안경*/
    (1022258, 3000, 'Eye Accessories'), /*Bat Wing Monocle*/
    (1022259, 3000, 'Eye Accessories'), /*Bandage Blindfold*/
    (1022263, 3000, 'Eye Accessories'), /* Sleepy Eye Patch*/
    (1022266, 3000, 'Eye Accessories'), /*Strange Uncle Glasses*/
    (1022269, 3000, 'Eye Accessories'), /*Chained Princess Face Accessory*/
    (1022270, 3000, 'Eye Accessories'), /*Oversized Sunglasses*/

/* Rings */
    (1112000, 4500, 'Rings'), /*Sparkling Ring*/
    (1112001, 4500, 'Rings'), /*Crush Ring*/
    (1112002, 4500, 'Rings'), /*Cloud Ring*/
    (1112003, 4500, 'Rings'), /*Cupid Ring*/
    (1112005, 4500, 'Rings'), /*Venus Fireworks*/
    (1112006, 4500, 'Rings'), /*Crossed Hearts*/
    (1112007, 4500, 'Rings'), /*Mistletoe Crush Ring*/
    (1112012, 4500, 'Rings'), /*Rose Crush Ring*/
    (1112013, 4500, 'Rings'), /*Firery Love String Couple Ring*/
    (1112014, 4500, 'Rings'), /*Flaming Red Lips Ring*/
    (1112015, 4500, 'Rings'), /*Illumination Couples Ring*/
    (1112016, 4500, 'Rings'), /*Snowflake Ring*/
    (1112100, 4500, 'Rings'), /*White Label Ring*/
    (1112101, 4500, 'Rings'), /*Blue Label Ring*/
    (1112103, 4500, 'Rings'), /*The Legendary Gold Ring*/
    (1112104, 4500, 'Rings'), /*Bubbly Label Ring*/
    (1112105, 4500, 'Rings'), /*Pink-Ribboned Label Ring*/
    (1112106, 4500, 'Rings'), /*Blue-Ribboned Label Ring*/
    (1112107, 4500, 'Rings'), /*Skull Label Ring*/
    (1112108, 4500, 'Rings'), /*Butterfly Label Ring*/
    (1112109, 4500, 'Rings'), /*Scoreboard Label Ring*/
    (1112110, 4500, 'Rings'), /*SK Basketball Team Label Ring*/
    (1112111, 4500, 'Rings'), /*KTF Basketball Team Label Ring*/
    (1112112, 4500, 'Rings'), /*Beach Label Ring*/
    (1112113, 4500, 'Rings'), /*Chocolate Label Ring*/
    (1112114, 4500, 'Rings'), /*Pink Candy Label Ring*/
    (1112115, 4500, 'Rings'), /*MapleBowl Label Ring */
    (1112116, 4500, 'Rings'), /*White Cloud Label Ring*/
    (1112117, 4500, 'Rings'), /*Rainbow Label Ring*/
    (1112118, 4500, 'Rings'), /*Rainbow Label RingaCoke Label Ring*/
    (1112119, 4500, 'Rings'), /*Coke (Red) Label Ring*/
    (1112120, 4500, 'Rings'), /*Coke (White) Label Ring*/
    (1112121, 4500, 'Rings'), /*Gingerman Label Ring*/
    (1112122, 4500, 'Rings'), /*Deluxe Rainbow Label Ring*/
    (1112123, 4500, 'Rings'), /*Red Pencil Label Ring*/
    (1112124, 4500, 'Rings'), /*Blue Pencil Label Ring*/
    (1112125, 4500, 'Rings'), /*Green Pencil Label Ring*/
    (1112126, 4500, 'Rings'), /*Brown Teddy Label Ring*/
    (1112127, 4500, 'Rings'), /*Welcome Back Ring*/
    (1112129, 4500, 'Rings'), /*German Label Ring*/
    (1112130, 4500, 'Rings'), /*Dutch Label Ring*/
    (1112131, 4500, 'Rings'), /*French Label Ring*/
    (1112132, 4500, 'Rings'), /*British Label Ring*/
    (1112134, 4500, 'Rings'), /*Bamboo Name Tag Ring*/
    (1112135, 4500, 'Rings'), /*Ink-and-Wash Painting Name Tag Ring*/
    (1112136, 4500, 'Rings'), /*Sausage Ring*/
    (1112137, 4500, 'Rings'), /*Mountain Dew Label Ring*/
    (1112141, 4500, 'Rings'), /*Red Rose Label Ring*/
    (1112142, 4500, 'Rings'), /*Mummy Label Ring*/
    (1112143, 4500, 'Rings'), /*Luxury Pearl Label Ring*/
    (1112144, 4500, 'Rings'), /*Cat-ger Label Ring*/
    (1112145, 4500, 'Rings'), /*Romantic Lace Label Ring*/
    (1112146, 4500, 'Rings'), /*Green Apple Label Ring*/
    (1112148, 4500, 'Rings'), /*Mister Mustache Label Ring*/
    (1112149, 4500, 'Rings'), /*Naver Label Ring*/
    (1112150, 4500, 'Rings'), /*Angel Label Ring*/
    (1112151, 4500, 'Rings'), /*Strawberry Cake Label Ring*/
    (1112152, 4500, 'Rings'), /*Blue Strawberry Basket Label Ring*/
    (1112153, 4500, 'Rings'), /*Strawberry Label Ring*/
    (1112154, 4500, 'Rings'), /*Moon Bunny Label Ring*/
    (1112155, 4500, 'Rings'), /*Frog Label Ring*/
    (1112156, 4500, 'Rings'), /*Oink Label Ring*/
    (1112157, 4500, 'Rings'), /*Blue Beard Label Ring*/
    (1112159, 4500, 'Rings'), /*Diamond Label Ring*/
    (1112160, 4500, 'Rings'), /*Watermelon Label Ring*/
    (1112161, 4500, 'Rings'), /*Quack Quack Label Ring*/
    (1112162, 4500, 'Rings'), /*Island Travel Name Tag Ring*/
    (1112163, 4500, 'Rings'), /*Starring Me Label Ring*/
    (1112164, 4500, 'Rings'), /*Sweet Summer Label Ring*/
    (1112165, 4500, 'Rings'), /*Green Forest Label Ring*/
    (1112166, 4500, 'Rings'), /*Baby Label Ring*/
    (1112170, 4500, 'Rings'), /*Star Label Ring*/
    (1112171, 4500, 'Rings'), /*White Puppy Label Ring*/
    (1112172, 4500, 'Rings'), /*Brown Puppy Label Ring*/
    (1112173, 4500, 'Rings'), /*Bunny Label Ring*/
    (1112176, 4500, 'Rings'), /*G Clef Label Ring*/
    (1112177, 4500, 'Rings'), /*Attack on Titan Label Ring*/
    (1112178, 4500, 'Rings'), /*Snow Day Dream Label Ring*/
    (1112179, 4500, 'Rings'), /*Snowy Christmas Label Ring*/
    (1112180, 4500, 'Rings'), /*Kinship Label Ring*/
    (1112181, 4500, 'Rings'), /*Sheep Label Ring*/
    (1112183, 4500, 'Rings'), /*Meadow Sheep Label Ring*/
    (1112184, 4500, 'Rings'), /*Squishy Pink Label Ring*/
    (1112190, 4500, 'Rings'), /*Carrot Rabbit Label Ring*/
    (1112191, 4500, 'Rings'), /*Honey Bee Label Ring*/
    (1112192, 4500, 'Rings'), /*Pineapple Label Ring*/
    (1112193, 4500, 'Rings'), /*Princess Diary Label Ring*/
    (1112194, 4500, 'Rings'), /*Black Hat Label Ring*/
    (1112195, 4500, 'Rings'), /*Green Hat Label Ring*/
    (1112196, 4500, 'Rings'), /*Blue Hat Label Ring*/
    (1112197, 4500, 'Rings'), /*Good Night Monster Label Ring*/
    (1112198, 4500, 'Rings'), /*Rascally Monster Label Ring*/
    (1112199, 4500, 'Rings'), /*Snowman's Red Scarf Label Ring*/
    (1112200, 4500, 'Rings'), /*Pink Quote Ring*/
    (1112201, 4500, 'Rings'), /*Pink-Hearted Quote Ring*/
    (1112202, 4500, 'Rings'), /*Blue Quote Ring*/
    (1112203, 4500, 'Rings'), /*The Golden Fly Ring*/
    (1112204, 4500, 'Rings'), /*Pink-Flowered Quote Ring*/
    (1112205, 4500, 'Rings'), /*Blue-Flowered Quote Ring*/
    (1112206, 4500, 'Rings'), /*Pink-Ribboned Quote Ring*/
    (1112207, 4500, 'Rings'), /*Blue-Ribboned Quote Ring*/
    (1112208, 4500, 'Rings'), /*Skull Quote Ring*/
    (1112209, 4500, 'Rings'), /*Blue-Hearted Quote Ring*/
    (1112210, 4500, 'Rings'), /*Gold-Yellow Quote Ring*/
    (1112211, 4500, 'Rings'), /*Pink Lady Quote Ring*/
    (1112212, 4500, 'Rings'), /*Silver-Blue Quote Ring*/
    (1112215, 4500, 'Rings'), /*Blue Marine Quote Ring*/
    (1112216, 4500, 'Rings'), /*Kitty Quote Ring*/
    (1112217, 4500, 'Rings'), /*Paw-Print Quote Ring*/
    (1112218, 4500, 'Rings'), /*Teddy Bear Quote Ring*/
    (1112219, 4500, 'Rings'), /*Scoreboard Quote Ring*/
    (1112220, 4500, 'Rings'), /*SK Basketball Team Quote Ring*/
    (1112221, 4500, 'Rings'), /*KTF Basketball Team Quote Ring*/
    (1112222, 4500, 'Rings'), /*Starflower Ring*/
    (1112223, 4500, 'Rings'), /*Beach Quote Ring*/
    (1112224, 4500, 'Rings'), /*Chocolate Quote Ring*/
    (1112225, 4500, 'Rings'), /*Pink Candy Quote Ring*/
    (1112226, 4500, 'Rings'), /*White Cloud Quote Ring*/
    (1112227, 4500, 'Rings'), /*Rainbow Quote Ring*/
    (1112228, 4500, 'Rings'), /*Coke Quote Ring*/
    (1112229, 4500, 'Rings'), /*Coke (Red) Quote Ring*/
    (1112230, 4500, 'Rings'), /*Coke (White) Quote Ring*/
    (1112231, 4500, 'Rings'), /*Gingerman Quote Ring*/
    (1112232, 4500, 'Rings'), /*Deluxe Rainbow Quote Ring*/
    (1112233, 4500, 'Rings'), /*Red Notebook Quote Ring*/
    (1112234, 4500, 'Rings'), /*Blue Notebook Quote Ring*/
    (1112235, 4500, 'Rings'), /*Green Notebook Quote Ring*/
    (1112236, 4500, 'Rings'), /*Brown Teddy Quote Ring*/
    (1112237, 4500, 'Rings'), /*Bamboo Thought Bubble Ring*/
    (1112238, 4500, 'Rings'), /*Ink-and-Wash Thought Bubble Ring*/
    (1112240, 4500, 'Rings'), /*Mountain Dew Quote Ring*/
    (1112244, 4500, 'Rings'), /*Darkness Bat Ring*/
    (1112252, 4500, 'Rings'), /*Red Rose Chat Ring*/
    (1112253, 4500, 'Rings'), /*Mummy Word Bubble Ring*/
    (1112254, 4500, 'Rings'), /*Luxury Pearl Word Bubble Ring*/
    (1112256, 4500, 'Rings'), /*Kitty Word Bubble Ring*/
    (1112257, 4500, 'Rings'), /*Romantic Lace Word Bubble Ring*/
    (1112258, 4500, 'Rings'), /*Green Apple Word Bubble Ring*/
    (1112259, 4500, 'Rings'), /*Mister Mustache Word Bubble Ring*/
    (1112260, 4500, 'Rings'), /*Guild Word Bubble Ring*/
    (1112261, 4500, 'Rings'), /*Naver Word Bubble Ring*/
    (1112262, 4500, 'Rings'), /*Angel Word Bubble Ring*/
    (1112263, 4500, 'Rings'), /*Strawberry Cake Word Bubble Ring*/
    (1112264, 4500, 'Rings'), /*Blue Strawberry Basket Chat Ring*/
    (1112265, 4500, 'Rings'), /*Red Strawberry Basket Chat Ring*/
    (1112266, 4500, 'Rings'), /*Moon Bunny Word Bubble Ring*/
    (1112267, 4500, 'Rings'), /*Frog Word Bubble Ring*/
    (1112268, 4500, 'Rings'), /*Oink Word Bubble Ring*/
    (1112269, 4500, 'Rings'), /*Blue Beard Quote Ring*/
    (1112271, 4500, 'Rings'), /*Diamond Quote Ring*/
    (1112272, 4500, 'Rings'), /*Watermelon Chat Bubble Ring*/
    (1112273, 4500, 'Rings'), /*Quack Quack Word Bubble Ring*/
    (1112274, 4500, 'Rings'), /*Island Travel Speech Bubble Ring*/
    (1112275, 4500, 'Rings'), /*Me From the Star Word Bubble Ring*/
    (1112276, 4500, 'Rings'), /*Sweet Summer Chat Ring*/
    (1112277, 4500, 'Rings'), /*Green Forest Chat Ring*/
    (1112278, 4500, 'Rings'), /*Baby Chat Ring*/
    (1112282, 4500, 'Rings'), /*Star Word Bubble Ring*/
    (1112283, 4500, 'Rings'), /*White Puppy Chat Ring*/
    (1112284, 4500, 'Rings'), /*Brown Puppy Chat Ring*/
    (1112285, 4500, 'Rings'), /*Bunny Word Bubble Ring*/
    (1112288, 4500, 'Rings'), /*G Clef Word Bubble Ring*/
    (1112289, 4500, 'Rings'), /*Attack on Titan Word Bubble Ring*/
    (1112290, 4500, 'Rings'), /*Snow Day Dream Word Bubble Ring*/
    (1112291, 4500, 'Rings'), /*Snowy Christmas Chat Ring*/
    (1112292, 4500, 'Rings'), /*Silver Guild Word Bubble Ring*/
    (1112293, 4500, 'Rings'), /*Kinship Word Bubble Ring*/
    (1112294, 4500, 'Rings'), /*Sheep Word Bubble Ring*/
    (1112295, 4500, 'Rings'), /*Baby Word Bubble Ring*/
    (1112296, 4500, 'Rings'), /*Meadow Sheep Chat Bubble Ring*/
    (1112724, 4500, 'Rings'), /*I'm New Ring*/
    (1112728, 4500, 'Rings'), /*Mapler Ring*/
    (1112757, 4500, 'Rings'), /*Grin Ring*/
    (1112800, 4500, 'Rings'), /*Friendship Ring: Clover*/
    (1112801, 4500, 'Rings'), /*Friendship Ring: Flower Petal*/
    (1112802, 4500, 'Rings'), /*Friendship Ring: Star*/
    (1112808, 4500, 'Rings'), /*MapleBowl Quote Ring */
    (1112810, 4500, 'Rings'), /*Christmas Night Bells Friendship Ring*/
    (1112811, 4500, 'Rings'), /*Christmas Party Friendship Ring*/
    (1112812, 4500, 'Rings'), /*Shared Umbrella Ring*/
    (1112816, 4500, 'Rings'), /*Snow Dome Friendship Ring*/
    (1112817, 4500, 'Rings'), /*Psyche Special Friendship Ring*/
    (1112820, 4500, 'Rings'), /*Friendship Ring: Dragon and Pheonix*/
    (1112900, 4500, 'Rings'), /*Lalala Ring*/
    (1112901, 4500, 'Rings'), /*Starry Spotlight Ring*/
    (1112902, 4500, 'Rings'), /*Baby Blue*/
    (1112903, 4500, 'Rings'), /*Amorian Aura Ring*/
    (1112904, 4500, 'Rings'), /*Rainbow Star Ring*/
    (1112905, 4500, 'Rings'), /*Bright Hot Pink Heart*/
    (1112906, 4500, 'Rings'), /*Baby Pink Heart*/
    (1112908, 4500, 'Rings'), /*Aura Ring*/
    (1112916, 4500, 'Rings'), /*Solo Ring*/
    (1112924, 4500, 'Rings'), /*Lemon Shooting Star Ring*/
    (1112925, 4500, 'Rings'), /*Blue Shooting Star Ring*/
    (1112926, 4500, 'Rings'), /*Pink Shooting Star Ring*/
    (1112928, 4500, 'Rings'), /*Peach Shooting Star Ring*/
    (1112929, 4500, 'Rings'), /*Von Leon Ring*/
    (1112930, 4500, 'Rings'), /*Tomato Ring*/
    (1112937, 4500, 'Rings'), /*Sleepy Zzz*/
    (1112943, 4500, 'Rings'), /*Fashion Week Ring*/
    (1112945, 4500, 'Rings'), /*Always Craving Sweet N' Sour BBQ*/
    (1112946, 4500, 'Rings'), /*Rainbow Jewelry*/
    (1112948, 4500, 'Rings'), /*Couture Critic Ring*/
    (1112949, 4500, 'Rings'), /*Melody Ring*/
    (1112955, 4500, 'Rings'), /*Best Friends Ring*/
    (1112956, 4500, 'Rings'), /*Shining Star Ring*/
    (1112958, 4500, 'Rings'), /*Honey Bee Flower Effect Ring*/
    (1112959, 4500, 'Rings'), /*Butterfly Flower Effect Ring*/
    (1112960, 4500, 'Rings'), /*Memory Guide Ring*/
    (1112961, 4500, 'Rings'), /*해피몬스터 링*/
    (1113003, 4500, 'Rings'), /*Dark Devil Ring*/
    (1114000, 4500, 'Rings'), /*Kinship Ring*/
    (1115003, 4500, 'Rings'), /*Carrot Rabbit Chat Ring*/
    (1115004, 4500, 'Rings'), /*Honey Bee Chat Ring*/
    (1115005, 4500, 'Rings'), /*Pineapple Chat Ring*/
    (1115006, 4500, 'Rings'), /*Princess Diary Chat Ring*/
    (1115007, 4500, 'Rings'), /*Black Hat Chat Ring*/
    (1115008, 4500, 'Rings'), /*Green Hat Chat Ring*/
    (1115009, 4500, 'Rings'), /*Blue Hat Chat Ring*/
    (1115010, 4500, 'Rings'), /*Good Night Monster Chat Ring*/
    (1115011, 4500, 'Rings'), /*Rascally Monster Chat Ring*/
    (1115012, 4500, 'Rings'), /*MVP Chat Ring (Silver)*/
    (1115013, 4500, 'Rings'), /*MVP Chat Ring (Gold)*/
    (1115014, 4500, 'Rings'), /*MVP Chat Ring (Diamond)*/
    (1115015, 4500, 'Rings'), /*Snowman's Red Scarf Chat Ring*/
    (1115016, 4500, 'Rings'), /*Heroes Slumbering Dragon Island Chat Ring*/
    (1115017, 4500, 'Rings'), /*Christmas Chat Ring*/
    (1115018, 4500, 'Rings'), /*Mighty Banana Chat Ring*/
    (1115019, 4500, 'Rings'), /*Heroes Damien Chat Ring*/
    (1115020, 4500, 'Rings'), /*Heroes Transcendence Stone Chat Ring*/
    (1115021, 4500, 'Rings'), /*Heroes Black Mage Chat Ring*/
    (1115022, 4500, 'Rings'), /*Bunny Chat Ring*/
    (1115023, 4500, 'Rings'), /*Sunshine Ranch Chat Ring*/
    (1115024, 4500, 'Rings'), /*Colorbug Chat Ring*/
    (1115025, 4500, 'Rings'), /*Shark Chat Ring*/
    (1115026, 4500, 'Rings'), /*Cat Skein Chat Ring*/
    (1115027, 4500, 'Rings'), /*Red Cloud Chat Ring*/
    (1115029, 4500, 'Rings'), /*DJ JM Chat Ring*/
    (1115030, 4500, 'Rings'), /*Pink Bean Chocolate Chat Ring*/
    (1115031, 4500, 'Rings'), /*Pink Bean Chat Ring*/
    (1115100, 4500, 'Rings'), /*MVP Label Ring (Silver)*/
    (1115101, 4500, 'Rings'), /*MVP Label Ring (Gold)*/
    (1115102, 4500, 'Rings'), /*MVP Label Ring (Diamond)*/
    (1115103, 4500, 'Rings'), /*Slumbering Dragon Island Label Ring*/
    (1115104, 4500, 'Rings'), /*Christmas Label Ring*/
    (1115105, 4500, 'Rings'), /*Mighty Banana Label Ring*/
    (1115108, 4500, 'Rings'), /*Heroes Damien Label Ring*/
    (1115109, 4500, 'Rings'), /*Heroes Transcendence Stone Label Ring*/
    (1115110, 4500, 'Rings'), /*Heroes Black Mage Label Ring*/
    (1115112, 4500, 'Rings'), /*Sunshine Ranch Label Ring*/
    (1115113, 4500, 'Rings'), /*Colorbug Label Ring*/
    (1115114, 4500, 'Rings'), /*Shark Label Ring*/
    (1115115, 4500, 'Rings'), /*Cat Skein Label Ring*/
    (1115116, 4500, 'Rings'), /*Red Cloud Label Ring*/
    (1115118, 4500, 'Rings'), /*DJ JM Label Ring*/
    (1115119, 4500, 'Rings'), /*Pink Bean Chocolate Label Ring*/
    (1115120, 4500, 'Rings'), /*Pink Bean Label Ring*/

/* Effects */
    (5010001, 3000, 'Effects'), /*Moon & the Stars*/
    (5010002, 3000, 'Effects'), /*Colorful Rainbow*/
    (5010003, 3000, 'Effects'), /*Little Devil*/
    (5010004, 3000, 'Effects'), /*Underwater*/
    (5010005, 3000, 'Effects'), /*Looking for Love*/
    (5010006, 3000, 'Effects'), /*Baby Angel*/
    (5010007, 3000, 'Effects'), /*Fugitive*/
    (5010008, 3000, 'Effects'), /*Mr. Jackpot*/
    (5010009, 3000, 'Effects'), /*Martial Effect*/
    (5010010, 3000, 'Effects'), /*Play with Me*/
    (5010011, 3000, 'Effects'), /*Loner*/
    (5010012, 3000, 'Effects'), /*Equalizer*/
    (5010013, 3000, 'Effects'), /*Fireworks*/
    (5010014, 3000, 'Effects'), /*Stormy Cloud*/
    (5010015, 3000, 'Effects'), /*777 Effect*/
    (5010016, 3000, 'Effects'), /*Siren*/
    (5010017, 3000, 'Effects'), /*Twinkling Star*/
    (5010018, 3000, 'Effects'), /*Smile*/
    (5010021, 3000, 'Effects'), /*Skeleton of Horror*/
    (5010022, 3000, 'Effects'), /*Star Trail*/
    (5010023, 3000, 'Effects'), /*Pumping Heart*/
    (5010024, 3000, 'Effects'), /*The Flocking Ducks*/
    (5010025, 3000, 'Effects'), /*Silent Spectre*/
    (5010026, 3000, 'Effects'), /*Bat Manager Effect*/
    (5010027, 3000, 'Effects'), /*Hot Head*/
    (5010028, 3000, 'Effects'), /*Indigo Flames*/
    (5010029, 3000, 'Effects'), /*Demonfyre*/
    (5010030, 3000, 'Effects'), /*Nuclear Fire*/
    (5010031, 3000, 'Effects'), /*My Boyfriend*/
    (5010032, 3000, 'Effects'), /*My Girlfriend*/
    (5010033, 3000, 'Effects'), /*Sheer Fear*/
    (5010034, 3000, 'Effects'), /*Christmas Tree*/
    (5010038, 3000, 'Effects'), /*Shower Power*/
    (5010039, 3000, 'Effects'), /*Spotlight*/
    (5010041, 3000, 'Effects'), /*Super Symphony*/
    (5010042, 3000, 'Effects'), /*Busy Bee*/
    (5010043, 3000, 'Effects'), /*Eyelighter*/
    (5010044, 3000, 'Effects'), /*Shadow Style*/
    (5010045, 3000, 'Effects'), /*Struck by Lightning*/
    (5010046, 3000, 'Effects'), /*Maple Champion*/
    (5010051, 3000, 'Effects'), /*O Maplemas Tree*/
    (5010052, 3000, 'Effects'), /*Santa Sled*/
    (5010053, 3000, 'Effects'), /*Mistletoe*/
    (5010054, 3000, 'Effects'), /*Jingling Santa*/
    (5010055, 3000, 'Effects'), /*UFO*/
    (5010056, 3000, 'Effects'), /*Garden Trail*/
    (5010057, 3000, 'Effects'), /*Flower Fairy*/
    (5010059, 3000, 'Effects'), /*Trail of Darkness Effect*/
    (5010060, 3000, 'Effects'), /*Happy Winter Effect*/
    (5010061, 3000, 'Effects'), /*Ace of Hearts*/
    (5010064, 3000, 'Effects'), /*Rock Band Effect*/
    (5010065, 3000, 'Effects'), /*Scoreboard Effect*/
    (5010066, 3000, 'Effects'), /*Disco Effect*/
    (5010068, 3000, 'Effects'), /*Return of Angel Wing*/
    (5010069, 3000, 'Effects'), /*Seraphim's Dark Wings*/
    (5010070, 3000, 'Effects'), /*Sprite Wings*/
    (5010073, 3000, 'Effects'), /*Miss Popular*/
    (5010074, 3000, 'Effects'), /*Mr. Popular*/
    (5010075, 3000, 'Effects'), /*I'm in London*/
    (5010076, 3000, 'Effects'), /*PARIS Je T'aime*/
    (5010078, 3000, 'Effects'), /*Owl Effect*/
    (5010079, 3000, 'Effects'), /*Cygnus Effect*/
    (5010080, 3000, 'Effects'), /*Spring Rain*/
    (5010081, 3000, 'Effects'), /*Peacock Effect*/
    (5010082, 3000, 'Effects'), /*Shining Star*/
    (5010083, 3000, 'Effects'), /*Winter Wonderland*/
    (5010095, 3000, 'Effects'), /*[Sale] Winter Wonderland*/
    (5010096, 3000, 'Effects'), /*[Sale] Shining Star*/
    (5010097, 3000, 'Effects'), /*[Sale] Echo Wings*/
    (5010098, 3000, 'Effects'), /*[Sale]  Long Lost Angel Wing*/
    (5010099, 3000, 'Effects'), /*[Special] Shadow Style*/
    (5010100, 3000, 'Effects'), /*Maple Style Effect*/
    (5010101, 3000, 'Effects'), /*Rainbow Wings*/
    (5010102, 3000, 'Effects'), /*Sorry!*/
    (5010103, 3000, 'Effects'), /*Friends Plz*/
    (5010104, 3000, 'Effects'), /*Party Plz*/
    (5010106, 3000, 'Effects'), /*Shining Effect*/
    (5010109, 3000, 'Effects'), /*Je t'aime Paris*/
    (5010110, 3000, 'Effects'), /*Rhinne's Protection*/
    (5010111, 3000, 'Effects'), /*Tropical Beach*/
    (5010112, 3000, 'Effects'), /*London Night Effect*/
    (5010113, 3000, 'Effects'), /*PSY Effect*/

/* Transparent */
    (1702585, 4500, 'Transparent'), /*Universal Transparent Weapon*/
    (1092064, 4500, 'Transparent'), /*Transparent Shield*/
    (1342069, 4500, 'Transparent'), /*Transparent Katara*/
    (1002186, 3000, 'Transparent'), /*Transparent Hat*/
    (1102039, 3000, 'Transparent'), /*Transparent Cape*/
    (1082102, 3000, 'Transparent'), /*Transparent Gloves*/
    (1072153, 3000, 'Transparent'), /*Transparent Shoes*/
    (1012057, 3000, 'Transparent'), /*Transparent Face Accessory*/
    (1032024, 3000, 'Transparent'), /*Transparent Earrings*/
    (1022048, 3000, 'Transparent'), /*Transparent Eye Accessory*/


/* Beauty ------------------------------------------------------------------ */
/* Hair */

/* Face */

/* Other */

/* Expressions */
    (5160000, 1000, 'Expressions'), /*Queasy*/
    (5160001, 1000, 'Expressions'), /*Panicky*/
    (5160002, 1000, 'Expressions'), /*Sweetness*/
    (5160003, 1000, 'Expressions'), /*Smoochies*/
    (5160004, 1000, 'Expressions'), /*Wink*/
    (5160005, 1000, 'Expressions'), /*Ouch*/
    (5160006, 1000, 'Expressions'), /*Sparkling Eyes*/
    (5160007, 1000, 'Expressions'), /*Flaming*/
    (5160008, 1000, 'Expressions'), /*Ray*/
    (5160009, 1000, 'Expressions'), /*Goo Goo*/
    (5160010, 1000, 'Expressions'), /*Whoa Whoa*/
    (5160011, 1000, 'Expressions'), /*Constant Sigh*/
    (5160012, 1000, 'Expressions'), /*Drool*/
    (5160013, 1000, 'Expressions'), /*Dragon Breath*/
    (5160014, 1000, 'Expressions'), /*Bleh*/
    (5160015, 1000, 'Expressions'), /*Dizzy*/
    (5160016, 1000, 'Expressions'), /*Awkward*/
    (5160017, 1000, 'Expressions'), /*Villainous*/
    (5160034, 1000, 'Expressions'), /*Nosebleed*/
    (5160035, 1000, 'Expressions'), /*Awesome*/
    (5160036, 1000, 'Expressions'), /*Troll*/


/* Pets */
/* Pets */
    (5000000, 5000, 'Pets'), /*Brown Kitty*/
    (5000001, 5000, 'Pets'), /*Brown Puppy*/
    (5000002, 5000, 'Pets'), /*Pink Bunny*/
    (5000003, 5000, 'Pets'), /*Mini Kargo*/
    (5000004, 5000, 'Pets'), /*Black Kitty*/
    (5000005, 5000, 'Pets'), /*White Bunny*/
    (5000006, 5000, 'Pets'), /*Husky*/
    (5000007, 5000, 'Pets'), /*Black Pig*/
    (5000008, 5000, 'Pets'), /*Panda*/
    (5000009, 5000, 'Pets'), /*Dino Boy*/
    (5000010, 5000, 'Pets'), /*Dino Girl*/
    (5000011, 5000, 'Pets'), /*Monkey*/
    (5000012, 5000, 'Pets'), /*White Tiger*/
    (5000013, 5000, 'Pets'), /*Elephant*/
    (5000015, 5000, 'Pets'), /*Dasher*/
    (5000017, 5000, 'Pets'), /*Robot*/
    (5000020, 5000, 'Pets'), /*Mini Yeti*/
    (5000022, 5000, 'Pets'), /*Turkey*/
    (5000023, 5000, 'Pets'), /*Penguin*/
    (5000024, 5000, 'Pets'), /*Jr. Balrog*/
    (5000025, 5000, 'Pets'), /*Golden Pig*/
    (5000026, 5000, 'Pets'), /*Sun Wu Kong*/
    (5000028, 5000, 'Pets'), /*Dragon*/
    (5000029, 5000, 'Pets'), /*Baby Dragon*/
    (5000030, 5000, 'Pets'), /*Green Dragon*/
    (5000031, 5000, 'Pets'), /*Red Dragon*/
    (5000032, 5000, 'Pets'), /*Blue Dragon*/
    (5000033, 5000, 'Pets'), /*Black Dragon*/
    (5000034, 5000, 'Pets'), /*Black Bunny*/
    (5000036, 5000, 'Pets'), /*Jr. Reaper*/
    (5000038, 5000, 'Pets'), /*White Monkey*/
    (5000039, 5000, 'Pets'), /*Porcupine*/
    (5000042, 5000, 'Pets'), /*Kino*/
    (5000044, 5000, 'Pets'), /*Orange Tiger*/
    (5000045, 5000, 'Pets'), /*Skunk*/
    (5000047, 5000, 'Pets'), /*Robo*/
    (5000048, 5000, 'Pets'), /*Baby Robo*/
    (5000049, 5000, 'Pets'), /*Blue Robo*/
    (5000050, 5000, 'Pets'), /*Red Robo*/
    (5000051, 5000, 'Pets'), /*Green Robo*/
    (5000052, 5000, 'Pets'), /*Gold Robo*/
    (5000053, 5000, 'Pets'), /*Gorilla Robo*/
    (5000054, 5000, 'Pets'), /*Snail*/
    (5000055, 5000, 'Pets'), /*Crys.Rudolph*/
    (5000056, 5000, 'Pets'), /*Toucan*/
    (5000058, 5000, 'Pets'), /*White Duck*/
    (5000060, 5000, 'Pets'), /*Pink Bean*/
    (5000066, 5000, 'Pets'), /*Baby Tiger*/
    (5000067, 5000, 'Pets'), /*Weird Alien*/
    (5000070, 5000, 'Pets'), /*Mir*/
    (5000071, 5000, 'Pets'), /*Ruby*/
    (5000074, 5000, 'Pets'), /*Bing Monkey*/
    (5000076, 5000, 'Pets'), /*Corgi Pup*/
    (5000083, 5000, 'Pets'), /*Persian Cat*/
    (5000084, 5000, 'Pets'), /*Esel*/
    (5000085, 5000, 'Pets'), /*Cake*/
    (5000086, 5000, 'Pets'), /*Pie*/
    (5000089, 5000, 'Pets'), /*Tiel*/
    (5000090, 5000, 'Pets'), /*Galiel*/
    (5000096, 5000, 'Pets'), /*Dummbo*/
    (5000098, 5000, 'Pets'), /*Shark*/
    (5000103, 5000, 'Pets'), /*Chroma Bean*/
    (5000130, 5000, 'Pets'), /*Metus*/
    (5000131, 5000, 'Pets'), /*Mors*/
    (5000132, 5000, 'Pets'), /*Invidia*/
    (5000133, 5000, 'Pets'), /*Storm Dragon*/
    (5000134, 5000, 'Pets'), /*Fennec Fox*/
    (5000135, 5000, 'Pets'), /*Gingerbready*/
    (5000136, 5000, 'Pets'), /*Ice Knight*/
    (5000138, 5000, 'Pets'), /*Merlion Pet*/
    (5000139, 5000, 'Pets'), /*Butterfly*/
    (5000142, 5000, 'Pets'), /*Puffram*/
    (5000143, 5000, 'Pets'), /*Craw*/
    (5000144, 5000, 'Pets'), /*Adriano*/
    (5000145, 5000, 'Pets'), /*Bonkey*/
    (5000146, 5000, 'Pets'), /*Harp Seal*/
    (5000149, 5000, 'Pets'), /*Silver Husky*/
    (5000150, 5000, 'Pets'), /*Pink Yeti*/
    (5000151, 5000, 'Pets'), /*Bandit*/
    (5000152, 5000, 'Pets'), /*Miracle Cat*/
    (5000155, 5000, 'Pets'), /*Abel*/
    (5000156, 5000, 'Pets'), /*Axel*/
    (5000161, 5000, 'Pets'), /*Pink*/
    (5000162, 5000, 'Pets'), /*Aaron*/
    (5000163, 5000, 'Pets'), /*Mint*/
    (5000167, 5000, 'Pets'), /*Starwing*/
    (5000168, 5000, 'Pets'), /*Stickman*/
    (5000170, 5000, 'Pets'), /*PSY*/
    (5000171, 5000, 'Pets'), /*MagiCookie*/
    (5000176, 5000, 'Pets'), /*Kangaroo*/
    (5000193, 5000, 'Pets'), /*Von Soup*/
    (5000197, 5000, 'Pets'), /*Sassy Snake*/
    (5000198, 5000, 'Pets'), /*Lil Moonbeam*/
    (5000199, 5000, 'Pets'), /*Adel*/
    (5000211, 5000, 'Pets'), /*Scurvy Bird*/
    (5000215, 5000, 'Pets'), /*Chunky */
    (5000216, 5000, 'Pets'), /*Brown Burro*/
    (5000217, 5000, 'Pets'), /*Blackheart*/
    (5000228, 5000, 'Pets'), /*Demon Metus*/
    (5000229, 5000, 'Pets'), /*Demon Mors*/
    (5000243, 5000, 'Pets'), /*Pink Dragon*/
    (5000244, 5000, 'Pets'), /*Ice Dragon*/
    (5000249, 5000, 'Pets'), /*Fluffy Teddy*/
    (5000250, 5000, 'Pets'), /*Cutie Teddy*/
    (5000251, 5000, 'Pets'), /*Puffy Teddy*/
    (5000254, 5000, 'Pets'), /*Red Elly*/
    (5000255, 5000, 'Pets'), /*Blue Burro*/
    (5000256, 5000, 'Pets'), /*Pumpkin Jack*/
    (5000257, 5000, 'Pets'), /*Pumpkin Zack*/
    (5000258, 5000, 'Pets'), /*Pumpkin Mack*/
    (5000261, 5000, 'Pets'), /*Royal Thumpy*/
    (5000262, 5000, 'Pets'), /*Merlion*/
    (5000269, 5000, 'Pets'), /*Hedgehog*/
    (5000271, 5000, 'Pets'), /*Frumpy Koala*/
    (5000272, 5000, 'Pets'), /*Grumpy Koala*/
    (5000273, 5000, 'Pets'), /*Nerdy Koala*/
    (5000275, 5000, 'Pets'), /*Chippermunk*/
    (5000276, 5000, 'Pets'), /*Chipmunch*/
    (5000277, 5000, 'Pets'), /*Chubmunk*/
    (5000281, 5000, 'Pets'), /*Vile Metus*/
    (5000282, 5000, 'Pets'), /*Dire Mors*/
    (5000283, 5000, 'Pets'), /*Wild Invidia*/
    (5000290, 5000, 'Pets'), /*Honey Angel*/
    (5000291, 5000, 'Pets'), /*Lime Angel*/
    (5000292, 5000, 'Pets'), /*Peach Angel*/
    (5000293, 5000, 'Pets'), /*Roo-A*/
    (5000294, 5000, 'Pets'), /*Roo-B*/
    (5000295, 5000, 'Pets'), /*Roo-C*/
    (5000296, 5000, 'Pets'), /*Toasty Devil*/
    (5000297, 5000, 'Pets'), /*Icy Devil*/
    (5000298, 5000, 'Pets'), /*Miasma Devil*/
    (5000299, 5000, 'Pets'), /*Gingerhead*/
    (5000300, 5000, 'Pets'), /*Devil Ipos*/
    (5000301, 5000, 'Pets'), /*Devil Shaz*/
    (5000302, 5000, 'Pets'), /*Devil Ose*/
    (5000303, 5000, 'Pets'), /*Devil Iros*/
    (5000304, 5000, 'Pets'), /*Devil Maz*/
    (5000305, 5000, 'Pets'), /*Devil Fose*/
    (5000306, 5000, 'Pets'), /*Devil Imos*/
    (5000307, 5000, 'Pets'), /*Devil Gaz*/
    (5000308, 5000, 'Pets'), /*Devil Tose*/
    (5000309, 5000, 'Pets'), /*Mini Queen*/
    (5000310, 5000, 'Pets'), /*Von Bon*/
    (5000311, 5000, 'Pets'), /*Pierre*/
    (5000318, 5000, 'Pets'), /*Yeti Robot*/
    (5000320, 5000, 'Pets'), /*Pinkadillo*/
    (5000321, 5000, 'Pets'), /*Yellowdillo*/
    (5000322, 5000, 'Pets'), /*Greenadillo*/
    (5000330, 5000, 'Pets'), /*Jr. Von Leon*/
    (5000331, 5000, 'Pets'), /*Jr. Orchid*/
    (5000332, 5000, 'Pets'), /*Jr. Hilla*/
    (5000341, 5000, 'Pets'), /*Punchyroo*/
    (5000342, 5000, 'Pets'), /*Unripe Nut*/
    (5000343, 5000, 'Pets'), /*Chestnut*/
    (5000344, 5000, 'Pets'), /*Burnt Nut*/
    (5000345, 5000, 'Pets'), /*Tiny Gollux*/
    (5000352, 5000, 'Pets'), /*White Candle*/
    (5000353, 5000, 'Pets'), /*Blue Candle*/
    (5000354, 5000, 'Pets'), /*Grape Candle*/
    (5000362, 5000, 'Pets'), /*RED Rudolph*/
    (5000363, 5000, 'Pets'), /*RED Yeti*/
    (5000364, 5000, 'Pets'), /*RED Penguin*/
    (5000365, 5000, 'Pets'), /*Kiwi Puff*/
    (5000366, 5000, 'Pets'), /*Berry Puff*/
    (5000367, 5000, 'Pets'), /*Mango Puff*/
    (5000368, 5000, 'Pets'), /*Happy Bean*/
    (5000369, 5000, 'Pets'), /*Li'l Lai*/
    (5000370, 5000, 'Pets'), /*Li'l Fort*/
    (5000371, 5000, 'Pets'), /*L'il Arby*/
    (5000375, 5000, 'Pets'), /*Pink Pengy*/
    (5000376, 5000, 'Pets'), /*Purple Pengy*/
    (5000377, 5000, 'Pets'), /*Blue Pengy*/
    (5000381, 5000, 'Pets'), /*Toto*/
    (5000382, 5000, 'Pets'), /*Frankie*/
    (5000384, 5000, 'Pets'), /*Petite Mario*/
    (5000402, 5000, 'Pets'), /*Ballet Lyn*/
    (5000403, 5000, 'Pets'), /*Soldier Hong*/
    (5000404, 5000, 'Pets'), /*Soldier Chun*/
    (5000405, 5000, 'Pets'), /*Green Chad*/
    (5000406, 5000, 'Pets'), /*Pink Mel*/
    (5000407, 5000, 'Pets'), /*Orange Leon*/
    (5000408, 5000, 'Pets'), /*Jr. Sierra*/
    (5000409, 5000, 'Pets'), /*Jr. Ryan*/
    (5000414, 5000, 'Pets'), /*Lil' Bobble*/
    (5000415, 5000, 'Pets'), /*Lil' Lotus*/
    (5000416, 5000, 'Pets'), /*Ill Orchid*/
    (5000417, 5000, 'Pets'), /*Gelimer*/
    (5000424, 5000, 'Pets'), /*Sheep*/
    (5000428, 5000, 'Pets'), /*Holoyeti*/
    (5000429, 5000, 'Pets'), /*Pink Seal*/
    (5000430, 5000, 'Pets'), /*New Seal*/
    (5000431, 5000, 'Pets'), /*Newer Seal*/
    (5000432, 5000, 'Pets'), /*Pinker Seal*/
    (5000433, 5000, 'Pets'), /*War Sheep*/
    (5000434, 5000, 'Pets'), /*Mage Sheep*/
    (5000435, 5000, 'Pets'), /*Cleric Sheep*/
    (5000437, 5000, 'Pets'), /*Orange*/
    (5000443, 5000, 'Pets'), /*Furry Elwin*/
    (5000444, 5000, 'Pets'), /*Fluffy Lily*/
    (5000445, 5000, 'Pets'), /*Baby Nero*/
    (5000446, 5000, 'Pets'), /*Strawbear*/
    (5000447, 5000, 'Pets'), /*Bananabear*/
    (5000448, 5000, 'Pets'), /*Cookiebear*/
    (5000449, 5000, 'Pets'), /*Gengerbready*/
    (5000452, 5000, 'Pets'), /*Squishy Bean*/
    (5000456, 5000, 'Pets'), /*Macha Man*/
    (5000457, 5000, 'Pets'), /*Lady Hot Tea*/
    (5000458, 5000, 'Pets'), /*Captain Cafe*/
    (5000460, 5000, 'Pets'), /*Sailor Seal*/
    (5000461, 5000, 'Pets'), /*Admiral Seal*/
    (5000462, 5000, 'Pets'), /*Steward Seal*/
    (5000466, 5000, 'Pets'), /*Ducky*/
    (5000469, 5000, 'Pets'), /*Tiny Nero*/
    (5000470, 5000, 'Pets'), /*Cheesy Cat*/
    (5000471, 5000, 'Pets'), /*Samson Cat*/
    (5000473, 5000, 'Pets'), /*Little Ursus*/
    (5000474, 5000, 'Pets'), /*Moist Cake*/
    (5000475, 5000, 'Pets'), /*Nutty Pie*/
    (5000476, 5000, 'Pets'), /*Sweet Candy*/
    (5000479, 5000, 'Pets'), /*Lil Zakum*/
    (5000483, 5000, 'Pets'), /*Mouse Monkey*/
    (5000496, 5000, 'Pets'), /*Lil Lilin*/
    (5000502, 5000, 'Pets'), /*Pumpkin O'*/
    (5000503, 5000, 'Pets'), /*Pumpkin L*/
    (5000507, 5000, 'Pets'), /*Blue Husky*/
    (5000510, 5000, 'Pets'), /*Fluffram*/
    (5000517, 5000, 'Pets'), /*Hekaton*/
    (5000518, 5000, 'Pets'), /*Hekaton S*/
    (5000519, 5000, 'Pets'), /*Hekaton E*/
    (5000520, 5000, 'Pets'), /*Hekaton A*/
    (5000521, 5000, 'Pets'), /*Lil' Ninja*/
    (5000524, 5000, 'Pets'), /*Alpaca*/
    (5000528, 5000, 'Pets'), /*Meerkat Mob*/
    (5000529, 5000, 'Pets'), /*Pudgycat*/
    (5000545, 5000, 'Pets'), /*Black Bean*/
    (5000568, 5000, 'Pets'), /*Purple Cake*/
    (5000569, 5000, 'Pets'), /*Stjartmes*/
    (5000570, 5000, 'Pets'), /*Lil Tutu*/
    (5000571, 5000, 'Pets'), /*Lil Nene*/
    (5000572, 5000, 'Pets'), /*Lil Lingling*/
    (5000585, 5000, 'Pets'), /*Lil Tengu*/
    (5000586, 5000, 'Pets'), /*Beagle*/
    (5000587, 5000, 'Pets'), /*Salem Cat*/
    (5000588, 5000, 'Pets'), /*Binx Cat*/
    (5000589, 5000, 'Pets'), /*Kit Cat*/
    (5000590, 5000, 'Pets'), /*Bichon*/
    (5000600, 5000, 'Pets'), /*Ursie*/
    (5000601, 5000, 'Pets'), /*Punch Cat*/
    (5000602, 5000, 'Pets'), /*Iron Rabbit*/

/* Pet Equipment */
    (1802000, 3000, 'Pet Equipment'), /*Red Ribbon*/
    (1802001, 3000, 'Pet Equipment'), /*Yellow Hat*/
    (1802002, 3000, 'Pet Equipment'), /*Red Hat*/
    (1802003, 3000, 'Pet Equipment'), /*Black Hat*/
    (1802004, 3000, 'Pet Equipment'), /*Pink Laced Cap*/
    (1802005, 3000, 'Pet Equipment'), /*Sky Blue Lace Cap*/
    (1802006, 3000, 'Pet Equipment'), /*Blue Top Hat*/
    (1802007, 3000, 'Pet Equipment'), /*Red Top Hat*/
    (1802008, 3000, 'Pet Equipment'), /*Rudolph's Hat*/
    (1802009, 3000, 'Pet Equipment'), /*Tree Hat*/
    (1802010, 3000, 'Pet Equipment'), /*Mushroom Suit*/
    (1802011, 3000, 'Pet Equipment'), /*Red Fur Coat*/
    (1802012, 3000, 'Pet Equipment'), /*Chestnut Cap*/
    (1802013, 3000, 'Pet Equipment'), /*Red Scarf*/
    (1802014, 3000, 'Pet Equipment'), /*Mini Kargo Wings*/
    (1802015, 3000, 'Pet Equipment'), /*Dino King & Queen*/
    (1802016, 3000, 'Pet Equipment'), /*Husky's Yellow Tights*/
    (1802017, 3000, 'Pet Equipment'), /*Monkey Sack*/
    (1802018, 3000, 'Pet Equipment'), /*Panda's Clown Costume*/
    (1802019, 3000, 'Pet Equipment'), /*Rudolph's Sleigh*/
    (1802020, 3000, 'Pet Equipment'), /*White Tiger's Thief Suit*/
    (1802021, 3000, 'Pet Equipment'), /*Elephant Hat*/
    (1802022, 3000, 'Pet Equipment'), /*Aladin Vest*/
    (1802023, 3000, 'Pet Equipment'), /*Pelvis Hair*/
    (1802024, 3000, 'Pet Equipment'), /*White Tiger the Wizard*/
    (1802025, 3000, 'Pet Equipment'), /*Bunny Suit*/
    (1802026, 3000, 'Pet Equipment'), /*Prince Pepe*/
    (1802027, 3000, 'Pet Equipment'), /*Husky's Bare Bones*/
    (1802028, 3000, 'Pet Equipment'), /*Dino Ghosty*/
    (1802029, 3000, 'Pet Equipment'), /*Panda's Pet-o-Lantern*/
    (1802030, 3000, 'Pet Equipment'), /*Penguin Earmuff Set*/
    (1802031, 3000, 'Pet Equipment'), /*Cowboy Kargo*/
    (1802032, 3000, 'Pet Equipment'), /*Snowboard Gear*/
    (1802033, 3000, 'Pet Equipment'), /*Crimson Mask*/
    (1802034, 3000, 'Pet Equipment'), /*White Angel*/
    (1802035, 3000, 'Pet Equipment'), /*Cute Beggar Overall*/
    (1802036, 3000, 'Pet Equipment'), /*Golden Pig Fortune Pouch*/
    (1802037, 3000, 'Pet Equipment'), /*Husky's Oinker Suit*/
    (1802038, 3000, 'Pet Equipment'), /*Mini Celestial Wand*/
    (1802039, 3000, 'Pet Equipment'), /*Golden Pig Lucky Sack*/
    (1802042, 3000, 'Pet Equipment'), /*Baby Turkey Carriage*/
    (1802044, 3000, 'Pet Equipment'), /*Dragon's soul*/
    (1802045, 3000, 'Pet Equipment'), /*Jr. Reaper's Guitar */
    (1802046, 3000, 'Pet Equipment'), /*Rabbit Ears*/
    (1802047, 3000, 'Pet Equipment'), /*Porcupine Sunglasses*/
    (1802048, 3000, 'Pet Equipment'), /*Dragon Armor*/
    (1802049, 3000, 'Pet Equipment'), /*Jr. Reaper's Sign (I'm with stoopid)*/
    (1802050, 3000, 'Pet Equipment'), /*Jr. Reaper's Sign (<--Noob)*/
    (1802051, 3000, 'Pet Equipment'), /*Jr. Reaper's Sign (cc plz)*/
    (1802052, 3000, 'Pet Equipment'), /*Jr. Reaper's Sign (I love pie)*/
    (1802053, 3000, 'Pet Equipment'), /*Snowman Gear*/
    (1802054, 3000, 'Pet Equipment'), /*Kino's Green Mushroom Hat*/
    (1802055, 3000, 'Pet Equipment'), /*Gas Mask*/
    (1802059, 3000, 'Pet Equipment'), /*Jail Bird Pet Costume*/
    (1802060, 3000, 'Pet Equipment'), /*Crystal Rudolph's Wings*/
    (1802061, 3000, 'Pet Equipment'), /*Scuba Mask*/
    (1802062, 3000, 'Pet Equipment'), /*Starry Stereo Headset*/
    (1802063, 3000, 'Pet Equipment'), /*Baby Tiger Wings*/
    (1802064, 3000, 'Pet Equipment'), /*Alien's Pet*/
    (1802066, 3000, 'Pet Equipment'), /*Dragon Egg Shell*/
    (1802070, 3000, 'Pet Equipment'), /*Pilot's Cat*/
    (1802071, 3000, 'Pet Equipment'), /*Pink Oxygen Tank*/
    (1802072, 3000, 'Pet Equipment'), /*Caught Fish*/
    (1802073, 3000, 'Pet Equipment'), /*Blue Birdy*/
    (1802077, 3000, 'Pet Equipment'), /*Mango Creampuff Wing's*/
    (1802078, 3000, 'Pet Equipment'), /*Esel's Coronet*/
    (1802079, 3000, 'Pet Equipment'), /*B-Day Candle*/
    (1802080, 3000, 'Pet Equipment'), /*Tiel's Tiara*/
    (1802081, 3000, 'Pet Equipment'), /*Galiel's Angel Star*/
    (1802082, 3000, 'Pet Equipment'), /*Pink Yeti's Blue BFF*/
    (1802083, 3000, 'Pet Equipment'), /*Silver Husky's Hip Glasses*/
    (1802084, 3000, 'Pet Equipment'), /*Dummbo's Hat*/
    (1802093, 3000, 'Pet Equipment'), /*Sky Blue Laced Cap*/
    (1802096, 3000, 'Pet Equipment'), /*Husky Yellow Tights*/
    (1802100, 3000, 'Pet Equipment'), /*Pet Collar*/
    (1802101, 3000, 'Pet Equipment'), /*Pet Label Ring*/
    (1802300, 3000, 'Pet Equipment'), /*Bare Bones*/
    (1802301, 3000, 'Pet Equipment'), /*Ghosty*/
    (1802302, 3000, 'Pet Equipment'), /*Pet-o-Lantern*/
    (1802303, 3000, 'Pet Equipment'), /*Clown Dress*/
    (1802305, 3000, 'Pet Equipment'), /*White Tiger Suit*/
    (1802306, 3000, 'Pet Equipment'), /*Oinker Suit*/
    (1802312, 3000, 'Pet Equipment'), /*Guitar */
    (1802317, 3000, 'Pet Equipment'), /*Jr. Reaper Sign (I'm with stoopid)*/
    (1802318, 3000, 'Pet Equipment'), /*Jr. Reaper Sign (cc plz)*/
    (1802320, 3000, 'Pet Equipment'), /*Jr. Reaper Sign (<--Noob)*/
    (1802321, 3000, 'Pet Equipment'), /*Jr. Reaper Sign (I love pie)*/
    (1802334, 3000, 'Pet Equipment'), /*Fish*/
    (1802337, 3000, 'Pet Equipment'), /*Tube*/
    (1802338, 3000, 'Pet Equipment'), /*Pink Bean's Headset*/
    (1802340, 3000, 'Pet Equipment'), /*Craw's Pirate Hat*/
    (1802341, 3000, 'Pet Equipment'), /*Adriano's Hat*/
    (1802342, 3000, 'Pet Equipment'), /*Bonkey's Ammunition Box*/
    (1802343, 3000, 'Pet Equipment'), /*Starry Muffler*/
    (1802344, 3000, 'Pet Equipment'), /*Parrot Admiral Hat*/
    (1802346, 3000, 'Pet Equipment'), /*Ghost of Fear*/
    (1802347, 3000, 'Pet Equipment'), /*Ghost of Death*/
    (1802348, 3000, 'Pet Equipment'), /*Ghost of Jealousy*/
    (1802349, 3000, 'Pet Equipment'), /*Dragon Orb*/
    (1802351, 3000, 'Pet Equipment'), /*Bean's Headset*/
    (1802352, 3000, 'Pet Equipment'), /*Bandit Goggles*/
    (1802353, 3000, 'Pet Equipment'), /*Sanchito's Carrot*/
    (1802354, 3000, 'Pet Equipment'), /*Black-hearted Earrings*/
    (1802365, 3000, 'Pet Equipment'), /*Harp Seal Hat*/
    (1802366, 3000, 'Pet Equipment'), /*Puffram's Golden Horn*/
    (1802367, 3000, 'Pet Equipment'), /*Gingerbready Bow Tie*/
    (1802368, 3000, 'Pet Equipment'), /*Frost Mallet*/
    (1802369, 3000, 'Pet Equipment'), /*Tiny Fright*/
    (1802370, 3000, 'Pet Equipment'), /*Tiny Sadness*/
    (1802371, 3000, 'Pet Equipment'), /*Tiny Envy*/
    (1802372, 3000, 'Pet Equipment'), /*Sunglass*/
    (1802375, 3000, 'Pet Equipment'), /*Starwing's Star Trail*/
    (1802378, 3000, 'Pet Equipment'), /*Shark's Mini Tube*/
    (1802380, 3000, 'Pet Equipment'), /*Blue Light Ring*/
    (1802381, 3000, 'Pet Equipment'), /*Golden Light Ring*/
    (1802382, 3000, 'Pet Equipment'), /*Purple Light Ring*/
    (1802384, 3000, 'Pet Equipment'), /*Fluffy Teddy's Bunny Ears*/
    (1802385, 3000, 'Pet Equipment'), /*Cutie Teddy's Baby Bonnet*/
    (1802386, 3000, 'Pet Equipment'), /*Puffy Teddy's Crown*/
    (1802387, 3000, 'Pet Equipment'), /*Red Elly's Dress Hat*/
    (1802388, 3000, 'Pet Equipment'), /*Blue Burro's Toy Carrot*/
    (1802389, 3000, 'Pet Equipment'), /*Pumpkin Jack's Magic Lantern*/
    (1802390, 3000, 'Pet Equipment'), /*Pumpkin Zack's Magic Lantern*/
    (1802391, 3000, 'Pet Equipment'), /*Pumpkin Mack's Magic Lantern*/
    (1802392, 3000, 'Pet Equipment'), /*Boxing Gloves*/
    (1802394, 3000, 'Pet Equipment'), /*Baby Frumpy Koala*/
    (1802395, 3000, 'Pet Equipment'), /*Baby Grumpy Koala*/
    (1802396, 3000, 'Pet Equipment'), /*Baby Nerdy Koala*/
    (1802418, 3000, 'Pet Equipment'), /*Chippermunk's Acorn */
    (1802419, 3000, 'Pet Equipment'), /*Chipmunch's Acorn*/
    (1802420, 3000, 'Pet Equipment'), /*Chubmunk's Acorn*/
    (1802424, 3000, 'Pet Equipment'), /*Honey Halo*/
    (1802425, 3000, 'Pet Equipment'), /*Lime Halo*/
    (1802426, 3000, 'Pet Equipment'), /*Peach Halo*/
    (1802427, 3000, 'Pet Equipment'), /*Roo-A Baby Bonnet*/
    (1802428, 3000, 'Pet Equipment'), /*Roo-B Baby Bonnet*/
    (1802429, 3000, 'Pet Equipment'), /*Roo-C Baby Bonnet*/
    (1802430, 3000, 'Pet Equipment'), /*Yellow Devil's Collar*/
    (1802431, 3000, 'Pet Equipment'), /*Red Devil's Collar*/
    (1802432, 3000, 'Pet Equipment'), /*Blue Devil's Collar*/
    (1802433, 3000, 'Pet Equipment'), /*Blazing Horns*/
    (1802434, 3000, 'Pet Equipment'), /*Chilling Horns*/
    (1802435, 3000, 'Pet Equipment'), /*Miasmic Horns*/
    (1802436, 3000, 'Pet Equipment'), /*Gingerbread Bow Tie*/
    (1802444, 3000, 'Pet Equipment'), /*Alluring Mirror*/
    (1802445, 3000, 'Pet Equipment'), /*Von Bon's Staff*/
    (1802446, 3000, 'Pet Equipment'), /*Pierre's Umbrella*/
    (1802447, 3000, 'Pet Equipment'), /*Snake's Pink Bow*/
    (1802448, 3000, 'Pet Equipment'), /*Ice Stick*/
    (1802449, 3000, 'Pet Equipment'), /*Yeti Robot Antenna*/
    (1802450, 3000, 'Pet Equipment'), /*Pinkadillo Star Ball*/
    (1802451, 3000, 'Pet Equipment'), /*Yellowdillow Circus Ball*/
    (1802452, 3000, 'Pet Equipment'), /*Greenadillo Soccer Ball*/
    (1802458, 3000, 'Pet Equipment'), /*Hot Pot Von Bon's Staff*/
    (1802459, 3000, 'Pet Equipment'), /*Ifia's Rose*/
    (1802460, 3000, 'Pet Equipment'), /*Orchid's Hat*/
    (1802461, 3000, 'Pet Equipment'), /*Hilla's Blackheart*/
    (1802462, 3000, 'Pet Equipment'), /*Gentleman Bow Tie*/
    (1802463, 3000, 'Pet Equipment'), /*Kangaroo Boxing Gloves*/
    (1802464, 3000, 'Pet Equipment'), /*Unripe Chestnut Leaf*/
    (1802465, 3000, 'Pet Equipment'), /*Chestnut Leaf*/
    (1802466, 3000, 'Pet Equipment'), /*Burnt Chestnut Leaf*/
    (1802467, 3000, 'Pet Equipment'), /*Gollux's Halo*/
    (1802471, 3000, 'Pet Equipment'), /*Purple Kid Pumpkin*/
    (1802472, 3000, 'Pet Equipment'), /*Green Kid Pumpkin*/
    (1802473, 3000, 'Pet Equipment'), /*Black Kid Pumpkin*/
    (1802474, 3000, 'Pet Equipment'), /*Little RED Admin*/
    (1802475, 3000, 'Pet Equipment'), /*Kiwi Puff Wings*/
    (1802476, 3000, 'Pet Equipment'), /*Berry Puff Wings*/
    (1802477, 3000, 'Pet Equipment'), /*Mango Puff Wings*/
    (1802478, 3000, 'Pet Equipment'), /*Happy Bean's Hat*/
    (1802479, 3000, 'Pet Equipment'), /*Li'l Lai's Necklace*/
    (1802480, 3000, 'Pet Equipment'), /*Li'l Fort's Scarf*/
    (1802481, 3000, 'Pet Equipment'), /*Li'l Arby's Bell*/
    (1802482, 3000, 'Pet Equipment'), /*Pink Pengy Hat*/
    (1802483, 3000, 'Pet Equipment'), /*Purple Pengy Hat*/
    (1802484, 3000, 'Pet Equipment'), /*Blue Pengy Hat*/
    (1802488, 3000, 'Pet Equipment'), /*Cloud Bag*/
    (1802489, 3000, 'Pet Equipment'), /*Frankie's Halo*/
    (1802490, 3000, 'Pet Equipment'), /*Devil Bat*/
    (1802491, 3000, 'Pet Equipment'), /*Lil Moonbeam's Hairband*/
    (1802492, 3000, 'Pet Equipment'), /*Helium Filled Dreams*/
    (1802493, 3000, 'Pet Equipment'), /*Cute Rabbit Hat*/
    (1802497, 3000, 'Pet Equipment'), /*Moon Miho*/
    (1802500, 3000, 'Pet Equipment'), /*Lyn's Tiara*/
    (1802501, 3000, 'Pet Equipment'), /*Hong's Heart*/
    (1802502, 3000, 'Pet Equipment'), /*Chun's Ambition*/
    (1802503, 3000, 'Pet Equipment'), /*Chameleon's Rainbow*/
    (1802504, 3000, 'Pet Equipment'), /*Orange Electronic Display*/
    (1802505, 3000, 'Pet Equipment'), /*Purple Electronic Display*/
    (1802509, 3000, 'Pet Equipment'), /*Lil' Bobble Hat*/
    (1802510, 3000, 'Pet Equipment'), /*Lotus's Aura*/
    (1802511, 3000, 'Pet Equipment'), /*Orchid's Tiny IV*/
    (1802512, 3000, 'Pet Equipment'), /*Gelimer's Teddy*/
    (1802519, 3000, 'Pet Equipment'), /*Fluffram Ribbon (Pet Equip)*/
    (1802520, 3000, 'Pet Equipment'), /*Matcha Man's Leaf*/
    (1802521, 3000, 'Pet Equipment'), /*Lady Hot Tea's Spoon*/
    (1802522, 3000, 'Pet Equipment'), /*Captain Cafe's Whipped Cream*/
    (1802524, 3000, 'Pet Equipment'), /*New Pink Harp Seal Hat*/
    (1802526, 3000, 'Pet Equipment'), /*Warrior Sheep Sword*/
    (1802527, 3000, 'Pet Equipment'), /*Mage Sheep Cane*/
    (1802528, 3000, 'Pet Equipment'), /*Cleric Sheep Staff*/
    (1802529, 3000, 'Pet Equipment'), /*Orange Leaf*/
    (1802530, 3000, 'Pet Equipment'), /*Furry Elwin's Necklace*/
    (1802531, 3000, 'Pet Equipment'), /*Fluffy Lily's Ribbon*/
    (1802532, 3000, 'Pet Equipment'), /*Baby Nero's Ball of Yarn*/
    (1802534, 3000, 'Pet Equipment'), /*Strawbear Fork*/
    (1802535, 3000, 'Pet Equipment'), /*Bananabear Fork*/
    (1802536, 3000, 'Pet Equipment'), /*Cookiebear Fork*/
    (1802537, 3000, 'Pet Equipment'), /*Fancy Fox Mask*/
    (1802538, 3000, 'Pet Equipment'), /*Fox Mask*/
    (1802539, 3000, 'Pet Equipment'), /*Sailor Seal Star Glasses*/
    (1802540, 3000, 'Pet Equipment'), /*Admiral Seal Star Glasses*/
    (1802541, 3000, 'Pet Equipment'), /*Steward Seal Star Glass*/
    (1802542, 3000, 'Pet Equipment'), /*Ducky's Suave Ribbon*/
    (1802543, 3000, 'Pet Equipment'), /*Tiny Nero's Transformation Set*/
    (1802544, 3000, 'Pet Equipment'), /*Cheesy Cat's Purple Yarn*/
    (1802545, 3000, 'Pet Equipment'), /*Samson Cat's Emerald Yarn*/
    (1802546, 3000, 'Pet Equipment'), /*Meerkat Instrument*/
    (1802547, 3000, 'Pet Equipment'), /*Pudgycat Fancytie*/
    (1802548, 3000, 'Pet Equipment'), /*Cake Temptation*/
    (1802549, 3000, 'Pet Equipment'), /*Pie Temptation*/
    (1802550, 3000, 'Pet Equipment'), /*Candy Temptation*/
    (1802551, 3000, 'Pet Equipment'), /*Lil Zakum's Black Sunglasses*/
    (1802552, 3000, 'Pet Equipment'), /*Mousy Overalls*/
    (1802553, 3000, 'Pet Equipment'), /*Evan's Halo*/
    (1802554, 3000, 'Pet Equipment'), /*Aran's Halo*/
    (1802555, 3000, 'Pet Equipment'), /*Phantom's Halo*/
    (1802556, 3000, 'Pet Equipment'), /*Luminous's Halo*/
    (1802557, 3000, 'Pet Equipment'), /*Mercedes's Halo*/
    (1802558, 3000, 'Pet Equipment'), /*Shade's Halo*/
    (1802559, 3000, 'Pet Equipment'), /*Damien's Halo*/
    (1802560, 3000, 'Pet Equipment'), /*Alicia's Halo*/
    (1802561, 3000, 'Pet Equipment'), /*Lilin's Halo*/
    (1802562, 3000, 'Pet Equipment'), /*Ursie's Ribbon*/
    (1802563, 3000, 'Pet Equipment'), /*Gym Cat Dumbbell*/
    (1802564, 3000, 'Pet Equipment'), /*Iron Rabbit Engine*/
    (1802565, 3000, 'Pet Equipment'), /*Cloud's Lollipop Ribbon*/
    (1802566, 3000, 'Pet Equipment'), /*Moss's Lollipop Ribbon*/
    (1802567, 3000, 'Pet Equipment'), /*Pinkie's Lollipop Ribbon*/
    (1802568, 3000, 'Pet Equipment'), /*Mini Stjartmes*/
    (1802569, 3000, 'Pet Equipment'), /*Lingling's Bell*/
    (1802570, 3000, 'Pet Equipment'), /*Nene's Flower*/
    (1802571, 3000, 'Pet Equipment'), /*TuTu's Umbrella*/
    (1802572, 3000, 'Pet Equipment'), /*Blue Ribbon*/
    (1802574, 3000, 'Pet Equipment'), /*Purple Ribbon*/
    (1802575, 3000, 'Pet Equipment'), /*Baby Chickie*/
    (1802576, 3000, 'Pet Equipment'), /*Anguish Crow*/
    (1802578, 3000, 'Pet Equipment'), /*Fondue's Ribbon Collar*/
    (1802579, 3000, 'Pet Equipment'), /*Sasha's Ribbon Collar*/
    (1802580, 3000, 'Pet Equipment'), /*Coco's Ribbon Collar*/
    (1802581, 3000, 'Pet Equipment'), /*Witch's Red Ribbon*/
    (1802582, 3000, 'Pet Equipment'), /*Witch's Purple Ribbon*/
    (1802583, 3000, 'Pet Equipment'), /*Witch's Pink Ribbon*/
    (1802584, 3000, 'Pet Equipment'), /*Red Bow Tie*/

/* Pet Use */
    (5460000, 3000, 'Pet Use'), /*Pet Snack*/
    (5180000, 3000, 'Pet Use'), /*Water of Life*/
    (5170000, 3000, 'Pet Use'), /*Pet Name Tag*/
    (5249000, 1000, 'Pet Use'), /*Premium Pet Food*/

/* Pet Skills */
    (5190000, 3000, 'Pet Skills'), /*Item Pick-up Skill*/
    (5190001, 3000, 'Pet Skills'), /*Auto HP Potion Skill*/
    (5190002, 3000, 'Pet Skills'), /*Expanded Auto Move Skill*/
    (5190003, 3000, 'Pet Skills'), /*Auto Move Skill*/
    (5190004, 3000, 'Pet Skills'), /*Expired Pickup Skill*/
    (5190005, 3000, 'Pet Skills'), /*Ignore Item Skill */
    (5190006, 3000, 'Pet Skills'), /*Auto MP Potion Skill*/
    (5190009, 3000, 'Pet Skills'), /*Auto All Cure Skill*/
    (5190010, 3000, 'Pet Skills'), /*Auto Buff Skill*/
    (5190011, 3000, 'Pet Skills'), /*Auto Feed and Movement Skill*/
    (5190012, 3000, 'Pet Skills'); /*Fatten Up Skill*/
