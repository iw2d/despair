set FOREIGN_KEY_CHECKS = 0;
drop table if exists damageskinsavedatas,
users,
friends,
linkskills,
accounts,
monster_collection_rewards,
monster_collection_mobs,
monster_collection_explorations,
monster_collections,
macroskills,
macros,
familiars,
stolenskills,
chosenskills,
skillcooltimes,
hyperrockfields,
characterpotentials,
test,
skills,
characters,
avatardata,
alliance_gradenames,
alliances,
keymaps,
funckeymap,
offenses,
offense_managers,
characterstats,
hairequips,
unseenequips,
petids,
totems,
spset,
extendsp,
noncombatstatdaylimit,
systemtimes,
charactercards,
avatarlook,
equips,
petitems,
items,
auctionitems,
merchantitems,
inventories,
questprogressrequirements,
questprogressitemrequirements,
questprogresslevelrequirements,
questprogressmoneyrequirements,
questprogressmobrequirements,
questlists,
questmanagers,
quests,
bbs_replies,
bbs_records,
gradenames,
guildmembers,
guildrequestors,
guildskills,
guildskill,
guilds,
monsterbookcards,
monsterbookinfos,
trunks,
employeetrunk,
cashiteminfos,
couplerecords,
beautyalbuminventory
;

create table trunks(
	id int not null auto_increment,
    slotcount int default '4',
    money bigint,
    primary key (id)
);

create table employeetrunk (
  id int(11) not null auto_increment,
  money bigint(20) default '0',
  primary key (id)
);

create table cashiteminfos(
	id bigint not null auto_increment,
    accountid int,
    characterid int,
    commodityid int,
    buycharacterid varchar(255),
    paybackrate int,
    discount double,
    orderno int,
    productno int,
    refundable boolean,
    sourceflag tinyint,
    storebank boolean,
    itemid int,
    trunkid int,
    position int,
    primary key (id)
);

create table quests (
	id bigint not null auto_increment,
    qrkey int,
    qrvalue varchar(255),
    status int,
    completedtime datetime(3),
	primary key (id)
);

create table questmanagers (
	id bigint not null auto_increment,
    primary key(id)
);

create table questlists (
	questlist_id bigint not null auto_increment,
	questmanager_id bigint,
    questid int,
    fk_questid bigint,
    primary key (questlist_id),
    foreign key (questmanager_id) references questmanagers(id)  on delete cascade,
    foreign key (fk_questid) references quests(id)
);

create table questprogressrequirements (
	id bigint not null auto_increment,
    orderNum int,
    progresstype varchar(255),
	questid bigint,
    unitid int,
    requiredcount int,
    currentcount int,
    primary key (id),
    foreign key (questid) references quests(id)  on delete cascade
);

create table inventories (
	id int not null auto_increment,
    type int,
    slots int,
    primary key (id)
);

create table items (
	id bigint not null auto_increment,
    inventoryid int, # item can be inside an inventory or storage, so cannot be a foreign key :(
    trunkid int,
    itemid int,
    bagindex int,
    cashitemserialnumber bigint,
    dateexpire datetime(3),
    invtype int,
    type int,
    iscash boolean,
    quantity int,
    attribute smallint,
    owner varchar(255),
    primary key (id)
);

create table auctionitems
(
    id           int not null auto_increment,
    type         int,
    accountid    int,
    charid       int,
    state        int,
    itemType     int,
    charName     varchar(255),
    price        bigint,
    endDate      datetime,
    biduserid    int,
    bidusername  varchar(255),
    bidworld     int,
    oid          int,
    regdate      datetime,
    deposit      bigint,
    sstype       int,
    item         int,
    itemname     varchar(255),
    soldquantity int,
    primary key (id)
);

create table merchantitems (
	id bigint not null auto_increment,
	merchantitemid int,
    bundles int,
    price long,
	employeetrunkid int,
    primary key (id)
);

create table petitems (
	itemid bigint,
    name varchar(255),
    level tinyint,
    tameness smallint,
    repleteness tinyint,
    petattribute smallint,
    petskill int,
    datedead datetime(3),
    remainlife int,
    activestate tinyint,
    autobuffskill int,
    pethue int,
    giantrate smallint,
    primary key (itemid),
    foreign key (itemid) references items(id) on delete cascade
);

create table equips (
	serialnumber bigint,
    itemid bigint,
    title varchar(255),
    equippeddate datetime(3),
    prevbonusexprate int,
    options varchar(255),
    sockets varchar(255),
    tuc smallint,
    cuc smallint,
    istr smallint,
    idex smallint,
    iint smallint,
    iluk smallint,
    imaxhp smallint,
    imaxmp smallint,
    ipad smallint,
    imad smallint,
    ipdd smallint,
    imdd smallint,
    iacc smallint,
    ieva smallint,
    icraft smallint,
    ispeed smallint,
    ijump smallint,
    leveluptype smallint,
    level smallint,
    exp smallint,
    durability smallint,
    iuc smallint,
    ipvpdamage smallint,
    ireducereq smallint,
    specialattribute smallint,
    durabilitymax smallint,
    iincreq smallint,
    growthenchant smallint,
    psenchant smallint,
    hyperupgrade smallint,
    bdr smallint,
    imdr smallint,
    damr smallint,
    statr smallint,
    cuttable smallint,
    exgradeoption smallint,
    itemstate smallint,
    grade smallint,
    chuc smallint,
    souloptionid smallint,
    soulsocketid smallint,
    souloption smallint,
    rstr smallint,
    rdex smallint,
    rint smallint,
    rluk smallint,
    rlevel smallint,
    rjob smallint,
    rpop smallint,
    specialgrade int,
    fixedpotential boolean,
    tradeblock boolean,
    isonly boolean,
    notsale boolean,
    attackspeed int,
    price int,
    charmexp int,
    expireonlogout boolean,
    setitemid int,
    exitem boolean,
    equiptradeblock boolean,
    islot varchar(255),
    vslot varchar(255),
    fixedgrade int,
    nopotential tinyint,
    bossreward tinyint,
    fstr smallint,
	fdex smallint,
	fint smallint,
	fluk smallint,
	fatt smallint,
	fmatt smallint,
	fdef smallint,
	fhp smallint,
	fmp smallint,
	fspeed smallint,
	fjump smallint,
	fallstat smallint,
	fboss smallint,
	fdamage smallint,
	flevel smallint,
    superioreqp tinyint,
    android int,
    androidgrade int,
    primary key (itemid),
    foreign key (itemid) references items(id) on delete cascade
);

create table monsterbookinfos (
	id int not null auto_increment,
    setid int,
    coverid int,
    primary key (id)
);

create table monsterbookcards (
	id bigint not null auto_increment,
    bookid int,
    cardid int,
    primary key (id),
    foreign key (bookid) references monsterbookinfos(id)
);

create table avatarlook (
	id int not null auto_increment,
    gender int,
    skin int,
    face int,
    hair int,
    weaponstickerid int,
    weaponid int,
    subweaponid int,
    job int,
    drawelfear boolean,
    demonslayerdeffaceacc int,
    xenondeffaceacc int,
	beasttamerdeffaceacc int,
    iszerobetalook boolean,
    mixedhaircolor int,
    mixhairpercent int,
    ears int,
    tail int,
    primary key (id)
);

create table hairequips (
	id int not null auto_increment,
    alid int,
    equipid int,
    primary key (id),
    foreign key (alid) references avatarlook(id) on delete cascade
);

create table unseenequips (
	id int not null auto_increment,
    alid int,
    equipid int,
    primary key (id),
    foreign key (alid) references avatarlook(id) on delete cascade
);

create table petids (
	id int not null auto_increment,
    alid int,
    petid int,
    primary key (id),
    foreign key (alid) references avatarlook(id) on delete cascade
);

create table totems (
	id int not null auto_increment,
    alid int,
    totemid int,
    primary key (id),
    foreign key (alid) references avatarlook(id) on delete cascade
);

create table extendsp (
	id int not null auto_increment,
    primary key (id)
);

create table spset (
	id int not null auto_increment,
    extendsp_id int,
    joblevel tinyint,
    sp int,
    primary key (id),
    foreign key (extendsp_id) references extendsp(id) on delete cascade
);

create table systemtimes (
	id int not null auto_increment,
    yr int,
    mnth int,
    primary key (id)
);

create table noncombatstatdaylimit (
	id int not null auto_increment,
    charisma smallint,
    charm smallint,
    insight smallint,
    will smallint,
    craft smallint,
    sense smallint,
    lastupdatecharmbycashpr datetime(3),
    charmbycashpr tinyint,
    primary key (id)
);

create table charactercards (
	id int not null auto_increment,
    characterid int,
    job int,
    level tinyint,
    primary key (id)
);

create table characterstats (
	id int not null auto_increment,
    characterid int,
    characteridforlog int,
    worldidforlog int,
    name varchar(255),
    gender int,
    skin int,
    face int,
    hair int,
    mixbasehaircolor int,
    mixaddhaircolor int,
    mixhairbaseprob int,
    level int,
    job int,
    str int,
    dex int,
    inte int,
    luk int,
    hp int,
    maxhp int,
    mp int,
    maxmp int,
    ap int,
    sp int,
    exp long,
    pop int,
    money long,
    wp int,
    extendsp int,
    posmap long,
    portal int,
    subjob int,
    deffaceacc int,
	fatigue int,
    lastfatigueupdatetime int,
    charismaexp int,
    insightexp int,
    willexp int,
    craftexp int,
    senseexp int,
    charmexp int,
    noncombatstatdaylimit int,
    pvpexp int,
    pvpgrade int,
    pvppoint int,
    pvpmodelevel int,
    pvpmodetype int,
    eventpoint int,
    albaactivityid int,
    albastarttime datetime(3),
    albaduration int,
    albaspecialreward int,
    burning boolean,
    charactercard int,
    accountlastlogout int,
    lastlogout datetime(3),
    gachexp int,
    honorexp int,
    nextavailablefametime datetime(3),
    primary key (id),
    foreign key (extendsp) references extendsp(id),
    foreign key (noncombatstatdaylimit) references noncombatstatdaylimit(id),
    foreign key (charactercard) references charactercards(id),
    foreign key (accountlastlogout) references systemtimes(id)
);

create table avatardata (
	id int not null auto_increment,
    characterstat int,
    avatarlook int,
    zeroavatarlook int,
    primary key (id),
    foreign key (characterstat) references characterstats(id),
    foreign key (avatarlook) references avatarlook(id),
    foreign key (zeroavatarlook) references avatarlook(id)
);

create table funckeymap (
	id int not null auto_increment,
    primary key (id),
    charId int,
    ord int
);

create table keymaps (
	id int not null auto_increment,
    fkmapid int,
    idx int,
    type tinyint,
    val int,
    primary key (id),
    foreign key (fkmapid) references funckeymap(id)
);


create table guilds (
	id int not null auto_increment,
    name varchar(255),
    leaderid int,
    worldid int,
    markbg int,
    markbgcolor int,
    mark int,
    markcolor int,
    maxmembers int,
    notice varchar(255),
    points int,
    seasonpoints int,
    allianceid int,
    level int,
    `rank` int,
    ggp int,
    appliable boolean,
    joinsetting int,
    reqlevel int,
    bbsNotice int,
    battleSp int,
    fk_allianceid int,
    primary key (id)
);

create table alliances (
	id int not null auto_increment,
    name varchar(255),
    maxmembernum int,
    notice varchar(255),
    primary key (id)
);

create table alliance_gradenames(
	id int not null auto_increment,
	gradename varchar(255),
    allianceid int,
    primary key (id),
    foreign key (allianceid) references alliances(id) on delete cascade
);

create table bbs_records (
	id int not null auto_increment,
    idforbbs int,
    creatorid int,
    subject varchar(255),
    msg text,
    creationdate datetime(3),
    icon int,
    guildid int,
    primary key (id)
);

create table bbs_replies (
	id int not null auto_increment,
    idforreply int,
    creatorid int,
    creationdate bigint,
    msg text,
    recordid int,
    primary key (id),
    foreign key (recordid) references bbs_records(id) on delete cascade
);

create table offense_managers (
    id int not null auto_increment,
    points int,
    primary key (id)
);

create table offenses (
    id bigint not null auto_increment,
    manager_id int,
    charid int,
    accountid int,
    msg text,
    type varchar(255),
    issuedate datetime(3),
    issuer_char_id int,
    primary key (id),
    foreign key (manager_id) references offense_managers(id) on delete cascade
);

create table characters (
	id int not null auto_increment,
    accid int,
	avatardata int,
    equippedinventory int,
    equipinventory int,
    consumeinventory int,
    etcinventory int,
    installinventory int,
    cashinventory int,
    hairinventory int,
    faceinventory int,
    funckeymap_id int,
    fieldid int,
    questmanager bigint,
    guild int,
    rewardPoints int,
    monsterbook int,
    partyid int,
    monsterparkcount tinyint default 0,
    previousFieldID bigint,
    quickslotKeys varchar(255), # inlined array
	primary key (id),
	foreign key (accid) references accounts(id),
    foreign key (avatardata) references avatardata(id),
    foreign key (equippedinventory) references inventories(id),
    foreign key (equipinventory) references inventories(id),
    foreign key (consumeinventory) references inventories(id),
    foreign key (etcinventory) references inventories(id),
    foreign key (installinventory) references inventories(id),
    foreign key (cashinventory) references inventories(id),
    foreign key (hairinventory) references inventories(id),
    foreign key (faceinventory) references inventories(id),
    foreign key (funckeymap_id) references funckeymap(id),
    foreign key (questmanager) references questmanagers(id),
    foreign key (guild) references guilds(id),
    foreign key (monsterbook) references monsterbookinfos(id)
);

create table familiars (
	id bigint not null auto_increment,
    charid int,
    idk1 int,
    familiarid int,
    name varchar(13),
    idk2 boolean,
    idk3 smallint,
    fatigue int,
    idk4 bigint,
    idk5 bigint,
    expiration datetime(3),
    vitality smallint,
    primary key (id),
    foreign key (charid) references characters(id) on delete cascade
);

create table stolenskills (
	id int not null auto_increment,
    charid int,
    skillid int,
    position int,
    currentlv tinyint,
    primary key (id),
    foreign key (charid) references characters(id)
);

create table chosenskills (
	id int not null auto_increment,
    charid int,
    skillid int,
    position int,
    primary key (id),
    foreign key (charid) references characters(id)
);

create table skillcooltimes (
	id int not null auto_increment,
    charid int,
    skillid int,
    nextusabletime bigint,
    primary key (id),
    foreign key (charid) references characters(id)
);

create table hyperrockfields (
	id bigint not null auto_increment,
    charid int,
    ord int,
    fieldid int,
    primary key (id),
    foreign key (charid) references characters(id)
);

create table characterpotentials (
	id bigint not null auto_increment,
    potkey tinyint,
    skillid int,
    slv tinyint,
    grade tinyint,
    charid int,
    primary key (id),
    foreign key (charid) references characters(id)
);

create table guildskill (
	id int not null auto_increment,
    skillid int,
    level int,
    expiredate datetime(3),
    buycharactername varchar(255),
    extendcharactername varchar(255),
    primary key (id)
);

create table guildskills (
	guildskill_id int not null auto_increment,
    skills_id int,
    guild_id int,
    skillid int,
    fk_guildskillid int,
    primary key (guildskill_id),
	foreign key (guild_id) references guilds(id) on delete cascade,
    foreign key (fk_guildskillid) references guildskill(id) on delete cascade
);

create table guildmembers (
	id int not null auto_increment,
    charid int,
    guildid int,
    grade int,
    alliancegrade int,
    commitment int,
    daycommitment int,
    igp int,
    commitmentinctime datetime(3),
    name varchar(255),
    job int,
    level int,
    loggedin boolean,
	primary key (id),
    foreign key (guildid) references guilds(id)
);

create table guildrequestors (
	id int not null auto_increment,
    requestors_id int,
    charid int,
    guildid int,
    name varchar(255),
    job int,
    level int,
    loggedin boolean,
	primary key (id),
    foreign key (guildid) references guilds(id) on delete cascade
);

create table gradenames (
	id int not null auto_increment,
	gradename varchar(255),
    guildid int,
    primary key (id),
    foreign key (guildid) references guilds(id) on delete cascade
);


create table skills (
	id int not null auto_increment,
    charid int,
    skillid int,
    rootid int,
    maxlevel int,
    currentlevel int,
    masterlevel int,
    primary key (id),
    foreign key (charid) references characters(id)  on delete cascade
);

create table macros (
	id bigint not null auto_increment,
    charid int,
    muted boolean,
    name varchar(255),
    primary key (id),
    foreign key (charid) references characters(id)
);

create table macroskills (
	id bigint not null auto_increment,
    ordercol int,
    skillid int,
    macroid bigint,
    primary key (id),
    foreign key (macroid) references macros(id) on delete cascade
);

create table monster_collections (
	id int not null auto_increment,
    primary key (id)
);

create table monster_collection_mobs (
	id int not null auto_increment,
    collectionid int,
    mobid int,
    primary key (id)
);

create table monster_collection_explorations (
	id bigint not null auto_increment,
    collectionid int,
    collectionkey int,
    monsterkey varchar(255),
    endDate datetime(3),
    position int,
    primary key (id)
);

create table monster_collection_rewards (
	region int,
    session int,
    groupid int,
    collectionid int,
    primary key (region, session, groupid)
);

create table users (
	id int not null auto_increment,
    banexpiredate datetime(3),
    banreason varchar(255),
    offensemanager int,
    votepoints int default 0,
    donationpoints int default 0,
    maplePoints int default 0,
    nxPrepaid int default 0,
	name varchar(255),
	password varchar(255),
	email varchar(255),
	pic varchar(255),
	mac varchar(255),
	lastloginip varchar(255),
	accounttype int default 0,
	age int default 0,
	vipgrade int default 0,
	nblockreason int default 0,
	gender tinyint default 0,
	msg2 tinyint default 0,
	purchaseexp tinyint default 0,
	pblockreason tinyint default 3,
	chatunblockdate bigint default 0,
	hascensorednxloginid boolean default 0,
	gradecode tinyint default 0,
	censorednxloginid varchar(255),
	characterslots int default 4,
	creationdate datetime(3),
    primary key (id),
    foreign key (offensemanager) references offense_managers(id)
);

create table accounts (
	id int not null auto_increment,
    worldid int,
    userid int,
    trunkid int,
    nxCredit int default 0,
    monstercollectionid int,
	employeetrunkid int,
	primary key (id),
    foreign key (userid) references users(id),
    foreign key (trunkid) references trunks(id),
    foreign key (monstercollectionid) references monster_collections(id)
);


create table linkskills (
	id bigint not null auto_increment,
    accid int,
    ownerid int,
    linkskillid int,
    level int,
    primary key (id),
    foreign key (accid) references accounts(id)
);

create table damageskinsavedatas (
	id bigint not null auto_increment,
    damageskinid int,
    itemid int,
    notsave boolean,
    description varchar(255),
    accid int,
    primary key (id),
    foreign key (accid) references accounts(id) on delete cascade
);

create table friends (
	id int not null auto_increment,
    ownerid int,
    owneraccid int,
    friendid int,
    friendaccountid int,
    name varchar(255),
    flag tinyint,
    groupname varchar(255),
    mobile tinyint,
    nickname varchar(255),
    memo varchar(255),
    primary key (id)
);

create table beautyalbuminventory (
    id bigint not null auto_increment,
    styleid int,
    slotid int,
    charid int,
    primary key (id),
    foreign key (charid) references characters(id)
);

create table couplerecords (
    id int not null auto_increment,
    type int,
    status int,
    itemid int,
    charid int,
    partnerid int,
    charname varchar(255),
    partnername varchar(255),
    charsn bigint,
    partnersn bigint,
    primary key (id),
    foreign key (charid) references characters(id) on delete cascade,
    foreign key (partnerid) references characters(id) on delete cascade,
    foreign key (charsn) references items(id) on delete cascade,
    foreign key (partnersn) references items(id) on delete cascade
);

insert into `users` (`name`, `password`, `accounttype`, `chatunblockdate`, `characterslots`) values ('admin', 'admin', '4', '0', '40');