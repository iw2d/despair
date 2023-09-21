package net.swordie.ms.handlers.header;

import java.util.*;

public enum InHeader {

    PONG(150),

    // Login ops
    CONNECT_CHAT(1),
    REDISPLAY_SERVERLIST(7),
    GUILD_CHAT(19),
    FRIEND_CHAT(20),
    GUEST_LOGIN(22),
    TOS(29),
    VIEW_SERVERLIST(33),
    VIEW_REGISTER_PIC(50),
    VIEW_SELECT_PIC(53),
    CLIENT_FAILED(57),
    PART_TIME_JOB(59),
    CHARACTER_CARD(60),
    ENABLE_LV50_CHAR(61),
    CREATE_LV50_CHAR(62),
    ENABLE_SPECIAL_CREATION(62),
    CREATE_SPECIAL_CHAR(65),

    DUMMY_CODE(100),
    BEGIN_SOCKET(101),
    SECURITY_PACKET(102),
    PERMISSION_REQUEST(103),
    LOGIN_BASIC_INFO(104),
    CHECK_LOGIN_AUTH_INFO(105),
    SELECT_WORLD(106),
    CHECK_SPW_REQUEST(107),
    CHAR_SELECT(108),
    CHECK_SPW_EXIST_REQUEST(109),
    MIGRATE_IN(110),
    SELECT_CHARACTER(111),
    SELECT_GO_TO_STARPLANET(112),
    SELECT_ACCOUNT(113),
    WORLD_INFO_REQUEST(114),
    WORLD_INFO_FOR_SHINING_REQUEST(115),
    CHECK_DUPLICATE_ID(116),
    LOGOUT_WORLD(117),
    PERMISSION_REQUEST_FAKE(118),
    CHECK_LOGIN_AUTH_INFO_FAKE(119),
    CREATE_MAPLE_ACCOUNT_FAKE(120),
    SELECT_ACCOUNT_FAKE(121),
    SELECT_WORLD_FAKE(122),
    SELECT_CHARACTER_FAKE(123),
    CREATE_NEW_CHARACTER_FAKE(124),
    CREATE_NEW_CHARACTER(125),
    DELETE_CHARACTER(128),
    RESERVED_DELETE_CHARACTER_CONFIRM(129),
    RESERVED_DELETE_CHARACTER_CANCEL(130),
    RENAME_CHARACTER(131),
    ALIVE_ACK_FAKE(132),
    EXCEPTION_LOG(133),
    PRIVATE_SERVER_PACKET(134),
    RESET_LOGIN_STATE_ON_CHECK_OTP(135),
    CHANGE_SPW_REQUEST(136),
    CHECK_SPW_REQUEST_FAKE(137),
    CHECK_OTP_REQUEST(138),
    CHECK_DELETE_CHARACTER_OTP(139),
    CHECK_OTP_FOR_WEB_LAUNCHING_REQUEST(140),
    CREATE_SECURITY_HANDLE(141), // v176
    ALBA_REQUEST(141),
    UPDATE_CHARACTER_CARD_REQUEST(142),
    ALIVE_ACK(147),
    CLIENT_ERROR(149),
    APPLIED_HOT_FIX(152),
    SERVERSTATUS_REQUEST(-1), // disconnects upon sending message (no error given)
    WVS_SET_UP_STEP(154),
    WVS_CRASH_CALLBACK(155),
    WORLD_STATUS_REQUEST(162),
    WORLD_LIST_REQUEST(165),
    WORLD_LIST_REQ(167),
    CHAR_SELECT_NO_PIC(171),
    CHANGE_PIC_REQUEST(175),
    USE_AUTH_SERVER(176),

    B_E_G_I_N__U_S_E_R(179),
    USER_TRANSFER_FIELD_REQUEST(180),
    USER_TRANSFER_CHANNEL_REQUEST(181),
    USER_TRANSFER_TO_HUB_REQUEST(182),
    WORLD_TRANSFER_REQUEST(183),
    WORLD_TRANSFER_SHINNING_STAR_REQUEST(184),
    USER_MIGRATE_TO_CASH_SHOP_REQUEST(186),
    USER_MIGRATE_AUCTION_HOUSE_REQUEST(187), // new v178
    USER_MIGRATE_TO_PVP_REQUEST(188),
    PARTY_MIGRATE_TO_PVP_REQUEST(189),
    USER_MIGRATE_TO_MONSTER_FARM(190),
    USER_MIGRATE_TO_MONSTER_FARM_BY_INVITE_ITEM(191),
    USER_TRANSFER_ASWAN_REQUEST(192),
    USER_TRANSFER_ASWAN_READY_REQUEST(193),
    ASWAN_RETIRE_REQUEST(194),
    USER_REQUEST_PV_P_STATUS(195),
    USER_MIGRATE_TO_PVE_REQUEST(196),
    USER_MOVE(197),
    USER_SIT_REQUEST(198),
    USER_PORTABLE_CHAIR_SIT_REQUEST(199),
    USER_EMOTICON_ITEM_USE_REQUEST(200),
    USER_DANCE_STOP_REQUEST(201),
    USER_MELEE_ATTACK(202),
    USER_SHOOT_ATTACK(203),
    USER_MAGIC_ATTACK(204),
    USER_BODY_ATTACK(205),
    USER_AREA_DOT_ATTACK(206),
    USER_MOVING_SHOOT_ATTACK_PREPARE(207),
    USER_HIT(208),
    USER_ATTACK_USER(209),
    USER_CHAT(210),
    USER_A_D_BOARD_CLOSE(211),
    USER_EMOTION(212),
    ANDROID_EMOTION(213),
    USER_ACTIVATE_EFFECT_ITEM(214),
    USER_MONKEY_EFFECT_ITEM(215),
    USER_ACTIVATE_NICK_ITEM(216),
    USER_ACTIVATE_DAMAGE_SKIN(217),
    USER_ACTIVATE_DAMAGE_SKIN__PREMIUM(218),
    USER_DAMAGE_SKIN_SAVE_REQUEST(219),
    USER_DEFAULT_WING_ITEM(220),
    USER_KAISER_TRANSFORM_WING(221),
    USER_KAISER_TRANSFORM_TAIL(222),
    USER_UPGRADE_TOMB_EFFECT(223),
    USER_H_P(224),
    PREMIUM(225),
    USER_BAN_MAP_BY_MOB(226),
    USER_MONSTER_BOOK_SET_COVER(227),
    USER_SELECT_NPC(228),
    USER_REMOTE_SHOP_OPEN_REQUEST(229),
    USER_SCRIPT_MESSAGE_ANSWER(230),
    USER_SHOP_REQUEST(231),
    USER_TRUNK_REQUEST(232),
    USER_ENTRUSTED_SHOP_REQUEST(233),
    USER_STORE_BANK_REQUEST(234),
    USER_PARCEL_REQUEST(235),
    USER_EFFECT_LOCAL(236),
    USER_SPECIAL_EFFECT_LOCAL(237),
    USER_FINAL_ATTACK_REQUEST(238),
    USER_CREATE_AREA_DOT_REQUEST(239),
    USER_CREATE_HOLIDOM_REQUEST(240),
    REQ_MAKING_SKILL_EFF(241),
    SHOP_SCANNER_REQUEST(242),
    SHOP_LINK_REQUEST(243),
    AUCTION_REQUEST(244),
    ADMIN_SHOP_REQUEST(245),
    USER_GATHER_ITEM_REQUEST(246),
    USER_SORT_ITEM_REQUEST(247),
    USER_CHANGE_SLOT_POSITION_REQUEST(248),
    USER_TEXT_EQUIP_INFO(249),
    USER_POP_OR_PUSH_BAG_ITEM_TO_INVEN(250),
    USER_BAG_TO_BAG_ITEM(251),
    USER_POUR_IN_BAG_TO_BAG(252),
    USER_STAT_CHANGE_ITEM_USE_REQUEST(253),
    USER_STAT_CHANGE_ITEM_CANCEL_REQUEST(254),
    USER_STAT_CHANGE_BY_PORTABLE_CHAIR_REQUEST(255),
    USER_MOB_SUMMON_ITEM_USE_REQUEST(256),
    USER_PET_FOOD_ITEM_USE_REQUEST(257),
    USER_TAMING_MOB_FOOD_ITEM_USE_REQUEST(258),
    USER_SCRIPT_ITEM_USE_REQUEST(259),
    USER_RECIPE_OPEN_ITEM_USE_REQUEST(260),
    USER_CONSUME_CASH_ITEM_USE_REQUEST(261),
    USER_ADDITIONAL_SLOT_EXTEND_ITEM_USE_REQUEST(262),
    USER_CASH_PET_PICK_UP_ON_OFF_REQUEST(263),
    USER_CASH_PET_SKILL_SETTING_REQUEST(264),
    USER_OPTION_CHANGE_REQUEST(265),
    USER_DESTROY_PET_ITEM_REQUEST(266),
    USER_BRIDLE_ITEM_USE_REQUEST(267),
    USER_SKILL_LEARN_ITEM_USE_REQUEST(268),
    USER_SKILL_RESET_ITEM_USE_REQUEST(269),
    USER_ABILITY_RESET_ITEM_USE_REQUEST(270),
    USER_ABILITY_CHANGE_ITEM_USE_REQUEST(271),
    USER_EXP_CONSUME_ITEM_USE_REQUEST(272),
    USER_MONSTER_LIFE_INVITE_ITEM_USE_REQUEST(273),
    USER_EXP_ITEM_GET_REQUEST(274),
    USER_CHAR_SLOT_INC_ITEM_USE_REQUEST(275),
    USER_CHAR_RENAME_ITEM_USE_REQUEST(276),
    USER_KAISER_COLOR_CHANGE_ITEM_USE_REQUEST(277),
    USER_SHOP_SCANNER_ITEM_USE_REQUEST(279),
    USER_MAP_TRANSFER_ITEM_USE_REQUEST(280),
    USER_PORTAL_SCROLL_USE_REQUEST(281),
    USER_FIELD_TRANSFER_REQUEST(282),
    USER_UPGRADE_ITEM_USE_REQUEST(283),
    USER_UPGRADE_ASSIST_ITEM_USE_REQUEST(284),
    USER_HYPER_UPGRADE_ITEM_USE_REQUEST(285),
    USER_EX_ITEM_UPGRADE_ITEM_USE_REQUEST(286),
    USER_KARMA_CONSUME_ITEM_USE_REQUEST(287),
    USER_EVENT_UPGRADE_ITEM_USE_REQUEST(288),
    USER_ITEM_OPTION_UPGRADE_ITEM_USE_REQUEST(289),
    USER_ADDITIONAL_OPT_UPGRADE_ITEM_USE_REQUEST(290),
    USER_ITEM_SLOT_EXTEND_ITEM_USE_REQUEST(291),
    USER_WEAPON_TEMP_ITEM_OPTION_REQUEST(292),
    USER_ITEM_SKILL_SOCKET_UPGRADE_ITEM_USE_REQUEST(293),
    USER_ITEM_SKILL_OPTION_UPGRADE_ITEM_USE_REQUEST(294),
    USER_FREE_MIRACLE_CUBE_ITEM_USE_REQUEST(295),
    USER_EQUIPMENT_ENCHANT_WITH_SINGLE_UI_REQUEST(296),
    USER_UI_OPEN_ITEM_USE_REQUEST(297),
    USER_BAG_ITEM_USE_REQUEST(299),
    USER_ITEM_RELEASE_REQUEST(300),
    USER_MEMORIAL_CUBE_OPTION_REQUEST(302),
    USER_USE_NAVI_FLYING_SKILL_REQUEST(303),
    USER_TOADS_HAMMER_REQUEST(304),
    USER_TOADS_HAMMER_HELP_REQUEST(305),
    USER_CHANGE_SOUL_COLLECTION_REQUEST(306),
    USER_SELECT_SOUL_SKILL_UP_REQUEST(307),
    USER_ABILITY_UP_REQUEST(308),
    USER_ABILITY_MASS_UP_REQUEST(309),
    USER_DOT_HEAL(310),
    USER_CHANGE_STAT_REQUEST(311),
    USER_CHANGE_STAT_REQUEST_BY_ITEM_OPTION(312),
    SET_SON_OF_LINKED_SKILL_REQUEST(313),
    USER_SKILL_UP_REQUEST(316),
    USER_SKILL_USE_REQUEST(317),
    USER_SKILL_CANCEL_REQUEST(318),
    USER_SKILL_PREPARE_REQUEST(319),
    USER_DROP_MONEY_REQUEST(322),
    USER_GIVE_POPULARITY_REQUEST(323),
    USER_PARTY_REQUEST(324),
    USER_CHARACTER_INFO_REQUEST(325),
    USER_ACTIVATE_PET_REQUEST(326),
    USER_REGISTER_PET_AUTO_BUFF_REQUEST(327),
    USER_TEMPORARY_STAT_UPDATE_REQUEST(328),
    USER_PORTAL_SCRIPT_REQUEST(329),
    USER_PORTAL_TELEPORT_REQUEST(330),
    USER_CALLING_TELEPORT_REQUEST(331),
    USER_MAP_TRANSFER_REQUEST(332),
    USER_ANTI_MACRO_ITEM_USE_REQUEST(333),
    USER_ANTI_MACRO_SKILL_USE_REQUEST(334),
    USER_OLD_ANTI_MACRO_QUESTION_RESULT(335),
    USER_ANTI_MACRO_REFRESH_REQUEST(336),
    USER_CLAIM_REQUEST(337),
    USER_QUEST_REQUEST(338),
    USER_MEDAL_REISSUE_REQUEST(339),
    USER_CALC_DAMAGE_STAT_SET_REQUEST(340),
    USER_B2_BODY_REQUEST(341),
    USER_THROW_GRENADE(342),
    USER_DESTROY_GRENADE(343),
    USER_CREATE_AURA_BY_GRENADE(344),
    USER_SET_MOVE_GRENADE(345),
    USER_MACRO_SYS_DATA_MODIFIED(346),
    USER_SELECT_NPC_ITEM_USE_REQUEST(347),
    USER_LOTTERY_ITEM_USE_REQUEST(348),
    USER_ROULETTE_START_REQUEST(349),
    USER_ROULETTE_RESULT_REQUEST(350),
    USER_ITEM_MAKE_REQUEST(351),
    USER_REPAIR_DURABILITY_ALL(913),
    USER_REPAIR_DURABILITY(352),
    USER_QUEST_RECORD_SET_STATE(353),
    USER_CLIENT_TIMER_END_REQUEST(354),
    USER_CLIENT_RESOLUTION_RESULT(355),
    USER_FOLLOW_CHARACTER_REQUEST(356),
    USER_FOLLOW_CHARACTER_WITHDRAW(357),
    USER_SELECT_PQ_REWARD(358),
    USER_REQUEST_PQ_REWARD(359),
    SET_PASSENGER_RESULT(360),
    USER_REQUEST_INSTANCE_TABLE(361),
    USER_REQUEST_CREATE_ITEM_POT(362),
    USER_REQUEST_REMOVE_ITEM_POT(363),
    USER_REQUEST_INC_ITEM_POT_LIFE_SATIETY(364),
    USER_REQUEST_CURE_ITEM_POT_LIFE_SICK(365),
    USER_REQUEST_COMPLATE_TO_ITEM_POT(366),
    USER_REQUEST_RESPAWN(367),
    USER_CONSUME_HAIR_ITEM_USE_REQUEST(368),
    USER_FORCE_ATOM_COLLISION(369),
    USER_DEBUFF_OBJ_COLLISION(373),
    USER_UPDATE_LAPIDIFICATION(374),
    USER_REQUEST_CHARACTER_POTENTIAL_SKILL_RAND_SET(375),
    USER_REQUEST_CHARACTER_POTENTIAL_SKILL_RAND_SET_UI(376),
    USER_REQUEST_OCCUMPATION_DATA(377),
    USER_REQUEST_ASWAN_TIME_TABLE_CLIENT_INIT(378),
    USER_PROTECT_BUFF_DIE_ITEM_REQUEST(379),
    USER_PROTECT_BUFF_DIE_MAPLE_POINT_REQUEST(380),
    USER_PROTECT_EXP_DIE_MAPLE_POINT_REQUEST(381),
    USER_KEY_DOWN_AREA_MOVING(382),
    USER_CHECK_WEDDING_EX_REQUEST(383),
    USER_CATCH_DEBUFF_COLLISION(384),
    USER_AFFECTED_AREA_CREATED(385),
    USER_AFFECTED_AREA_REMOVED(386),
    USER_DAZZLE_HIT(387),
    USER_MESO_EXCHANGE_REQUEST(388),
    ZERO_TAG(392),
    ZERO_SHARE_CASH_EQUIP_PART(393),
    ZERO_LAST_ASSIST_STATE(394),
    USER_SHOOT_ATTACK_IN_FPS(395),
    USER_LUCKY_ITEM_USE_REQUEST(396),
    USER_MOB_MOVE_ABILITY_CHANGE(397),
    USER_DRAGON_ACTION(398),
    USER_DRAGON_BREATH_EARTH_EFFECT(399),
    USER_RENAME_REQUEST(400),
    BROADCAST_MSG(402),
    GROUP_MESSAGE(403),
    FIELD_UNIVERSE_MESSAGE(404),
    WHISPER(405),
    MESSENGER(406),
    MINI_ROOM(407),
    PARTY_REQUEST(408),
    PARTY_RESULT(409),
    PARTY_INVITABLE_SET(410),
    EXPEDITION_REQUEST(411),
    PARTY_ADVER_REQUEST(412),
    URUS_PARTY_REQUEST(413),
    GUILD_REQUEST(414),
    GUILD_RESULT(415),
    GUILD_JOIN_REQUEST(416),
    GUILD_JOIN_CANCEL_REQUEST(417),
    GUILD_JOIN_ACCEPT(418),
    GUILD_JOIN_REJECT(419),
    GUILD_CONTENT_RANK_REQUEST(420),
    TOWER_RANK_REQUEST(421),
    ADMIN(422),
    LOG(423),
    FRIEND_REQUEST(424),
    STAR_FRIEND_REQUEST(425),
    STAR_PLANET_POINT_REQUEST(-1),
    LOAD_ACCOUNT_ID_OF_CHARACTER_FRIEND_REQUEST(426),
    MEMO_REQUEST(427),
    MEMO_FLAG_REQUEST(428),
    ENTER_TOWN_PORTAL_REQUEST(429),
    ENTER_RANDOM_PORTAL_REQUEST(430),
    ENTER_OPEN_GATE_REQUEST(431),
    SLIDE_REQUEST(431),
    FUNC_KEY_MAPPED_MODIFIED(433), // v178
    R_P_S_GAME(434),
    G_S_R_P_S_GAME(435),
    STAR_PLANET__G_S_R_P_S_GAME(436),
    G_S_R_P_S_FORCE_SELECT(437),
    MARRIAGE_REQUEST(438),
    WEDDING_WISH_LIST_REQUEST(439),
    GUEST_BLESS(421),
    BOOBY_TRAP_ALERT(441),
    STALK_BEGIN(442),
    ALLIANCE_REQUEST(443),
    ALLIANCE_RESULT(444),
    FAMILY_CHART_REQUEST(445),
    FAMILY_INFO_REQUEST(446),
    FAMILY_REGISTER_JUNIOR(447),
    FAMILY_UNREGISTER_JUNIOR(448),
    FAMILY_UNREGISTER_PARENT(449),
    FAMILY_JOIN_RESULT(450),
    FAMILY_USE_PRIVILEGE(451),
    FAMILY_SET_PRECEPT(452),
    FAMILY_SUMMRESULT(453),
    TALK_TO_TUTOR(454),
    TALK_TO_PARTNER(455),
    USER_SWITCH_R_P(456),
    REQUEST_INC_COMBO(457),
    REQUEST_DEC_COMBO(458),
    REQUEST_SET_BLESS_OF_DARKNESS(459),
    REQUEST_SET_HP_BASE_DAMAGE(460),
    MOB_CRC_KEY_CHANGED_REPLY(461),
    MOB_CRC_DATA_RESULT(462),
    MAKING_SKILL_REQUEST(463),
    BROADCAST_EFFECT_TO_SPLIT(466),
    BROADCAST_ONE_TIME_ACTION_TO_SPLIT(467),
    BROADCAST_AFFECTED_EFFECT_TO_SPLIT(468),
    DEBUG_ONLY_COMMAND(469),
    MICRO_BUFF_END_TIME(470),
    REQUEST_SESSIVALUE(471),
    USER_TRANSFER_FREE_MARKET_REQUEST(472),
    USER_REQUEST_SET_STEAL_SKILL_SLOT(473),
    USER_REQUEST_STEAL_SKILL_MEMORY(474),
    USER_REQUEST_STEAL_SKILL_LIST(475),
    USER_REQUEST_STEAL_SKILL(476),
    REWARD_MOB_LIST_REQUEST(477),
    USER_LV_UP_GUIDE_NOTICE(478),
    RESET_CROSS_HUNTER_ALERT(479),
    CROSS_HUNTER_COMPLETE_REQUEST(480),
    CROSS_HUNTER_SHOP_REQUEST(481),
    USER_EQUIP_SLOT_LEVEL_MINUS_ITEM_USE_REQUEST(482),
    BOARD_GAME_REQUEST(483),
    USER_REQUEST_FLYING_SWORD_START(484),
    BINGO_REQUEST(485),
    BINGO_CASSANDRA_REQUEST(486),
    ACTION_BAR_REQUEST(487),
    USER_REQUEST_SET_OFF_TRINITY(488),
    MESO_RANGER_REQUEST(489),
    USER_REQUEST_SET_SMASH_COUNT(490),
    USER_HYPER_SKILL_UP_REQUEST(491),
    USER_HYPER_SKILL_RESET_REQUSET(492),
    USER_HYPER_STAT_SKILL_UP_REQUEST(493),
    USER_HYPER_STAT_SKILL_RESET_REQUEST(494),
    USER_SET_DRESS_CHANGED_REQUEST(495),
    ENTRY_RECORD_REQUEST(496),
    SET_MAX_GAUGE(497),
    USER_RETURN_EFFECT_RESPONSE(498),
    GET_SERVER_TIME(499),
    GET_CHARACTER_POSITION(500),
    USER_REQUEST_CHANGE_MOB_ZONE_STATE(501),
    EVOLVING_REQUEST(502),
    USER_MIXER_REQUEST(503),
    SUMMON_EVENT_REWARD(502),
    MYSTIC_FIELD_MOVE(503),
    YUT_GAME_REQUEST(506),
    USER_JEWEL_CRAFT_REQUEST(507),
    VALUE_PACK_REQUEST(508),
    REQUEST_RELOGIN_COOKIE(509),
    WAIT_QUEUE_REQUEST(510),
    CHECK_TRICK_OR_TREAT_REQUEST(511),
    MONSTER_FARM_MIGRATE_OUT_REQUEST(512),
    HALLOWEEN_CANDY_RANKING_REQUEST(513),
    GET_REWARD_REQUEST(514),
    MAPLE_STYLE_BONUS_REQUEST(515),
    MAPLE_STYLE_ADVICE_REQUEST(516),
    MAPLE_STYLE_SET_SCORE_REQUEST(517),
    MENTORING(517),
    GET_LOTTERY_RESULT(518),
    ROOTABYSS_ENTER_REQUEST(519),
    USER_SET_ITEM_ACTION(520),
    USER_SET_BITS_CASE(521),
    USER_SET_BITS_SLOT(521),
    USER_ANTI_MACRO_QUESTION_RESULT(522),
    USER_PINKBEAN_ROLLING(523),
    USER_PINKBEAN_YO_YO_STACK(524),
    USER_QUICK_MOVE_SCRIPT(526),
    TIME_GATE_REQUEST(527),
    USER_SELECT_ANDROID(528),
    USER_COMPLETE_NPC_SPEECH(529),
    USER_COMPLETE_ANOTHER_USER_CHECK(530),
    USER_COMPLETE_COMBO_KILL_COUNT_CHECK(531),
    USER_COMPLETE_MULTI_KILL_COUNT_CHECK(532),
    USER_COMPLETE_MULTI_KILL_CHECK(533),
    USER_DAMAGE_FALLING_CHECK(534),
    USER_COMPLETE_PERSONAL_SHOP_BUY_CHECK(535),
    USER_DAILY_COMMITMENT_CHECK(536),
    USER_MOB_DROP_MESO_PICKUP(537),
    USER_BREAK_TIME_FIELD_ENTER(538),
    USER_RUNE_ACT_REQUEST(539),
    JOURNAL_AVATAR_REQUEST(540),
    REQUEST_EVENT_LIST(541),
    USER_SIGN_REQUEST(542),
    ADD_ATTACK_RESET(543),
    SET_EVENT_NAME_TAG(544),
    USER_AFFECTED_AREA_REMOVE_BY_TIME(545),
    REQUEST_FREE_CHANGE_JOB(546),
    LIBRARY_START_SCRIPT(547),
    CHANNEL_USER_COUNT_REQUEST(548),
    UN_URUS_SELECTED_SKILL_LIST(549),
    SOUL_DUNGESYS(550),
    USER_COORDINATION_CONTEST_REQUEST(551),
    USER_SOUL_EFFECT_REQUEST(552),
    USER_SPIN_OFF_NEW_MODIFY_REQUEST(553),
    BLACK_LIST(554),
    SKILL_HELPER_POPUP(555),
    BLOCKBUSTER(556),
    M_TALK_OFFLINE_ACCOUNT_FRIENDS_NAME_REQUEST(-1),
    CHARACTER_BURNING(-1),
    UPDATE_CHARACTER_SELECT_LIST(557),
    DIRECT_GO_TO_FIELD(558),
    TRY_REGISTER_TELEPORT(559),
    USER_TOWER_CHAIR_SETTING(560),
    USER_LAST_COLLECT_MONSTER_RESET(542),
    END_OVER_HEAT(563),
    TRADE_KING_SHOP_REQ(564),
    TRADE_KING_SHOP_INFO_REQ(565),

    B_E_G_I_N__P_E_T(573),
    PET_MOVE(574),
    PET_ACTION(575),
    PET_INTERACTION_REQUEST(576),
    PET_DROP_PICK_UP_REQUEST(577),
    PET_STAT_CHANGE_ITEM_USE_REQUEST(578),
    PET_UPDATE_EXCEPTION_LIST_REQUEST(579),
    PET_FOOD_ITEM_USE_REQUEST(580),
    PET_OPEN_SHOP(581),
    E_N_D__P_E_T(582),

    B_E_G_I_N__S_K_I_L_L_P_E_T(583),
    SKILL_PET_MOVE(584),
    SKILL_PET_ACTION(585),
    SKILL_PET_STATE(586),
    SKILL_PET_DROP_PICK_UP_REQUEST(587),
    SKILL_PET_UPDATE_EXCEPTION_LIST_REQUEST(588),
    E_N_D__S_K_I_L_L_P_E_T(589),

    B_E_G_I_N__S_U_M_M_O_N_E_D(590),
    SUMMONED_MOVE(591),
    SUMMONED_ATTACK(592),
    SUMMONED_HIT(593),
    SUMMONED_SKILL(594),
    SUMMONED_REMOVE(595),
    SUMMONED_ATTACK_PV_P(596),
    SUMMONED_ACTION(597),
    SUMMONED_ASSIST_ATTACK_DONE(598),
    E_N_D__S_U_M_M_O_N_E_D(599),

    B_E_G_I_N__D_R_A_G_O_N(600),
    DRAGON_MOVE(601),
    DRAGON_GLIDE(602),
    E_N_D__D_R_A_G_O_N(603),

    B_E_G_I_N__A_N_D_R_O_I_D(604),
    ANDROID_MOVE(605),
    ANDROID_ACTION_SET(606),
    E_N_D__A_N_D_R_O_I_D(607),

    B_E_G_I_N__F_O_X_M_A_N(608),
    FOX_MAN_MOVE(609),
    FOX_MAN_ACTION_SET_USE_REQUEST(610),
    E_N_D__F_O_X_M_A_N(610),

    QUICKSLOT_KEY_MAPPED_MODIFIED(612),
    PASSIVE_SKILL_INFO_UPDATE(613),
    UPDATE_CLIENT_ENVIRONMENT(614),
    DIRECTION_NODE_COLLISION(616),
    USER_LASER_INFO_FOR_REMOTE(617),
    RETURN_TELEPORT_DEBUFF(618),
    CHECK_PROCESS(619),
    MEMO_IN_GAME_REQUEST(619),
    EGO_EQUIP_GAUGE_COMPLETE_RETURN(620),
    EGO_EQUIP_CREATE_UPGRADE_ITEM(621),
    EGO_EQUIP_CREATE_UPGRADE_ITEM_COST_REQUEST(622),
    EGO_EQUIP_TALK_REQUEST(623),
    EGO_EQUIP_CHECK_UPDATE_ITEM_REQUEST(624),
    INHERITANCE_INFO_REQUEST(625),
    INHERITANCE_UPGRADE_REQUEST(626),
    MIRROR_READING_SELECT_BOOK_REQUEST(627),
    LIKE_POINT(628),
    USER10TH_STREAMING_URL_REQUEST(629),
    USER_HELP_GUIDE_REQUEST(630),
    USER_UPDATE_MAPLE_TV_SHOW_TIME(631),
    REDUCE_DOT_DAMGE_BASE_BUFF_REQUEST(-1),
    MIRROR_STUDY_COMPLETE(632),
    REQUEST_ARROW_PLATTER_OBJ(634),
    USER_TIME_EVENT_UI_OPEN_REQUEST(635),
    USER_TIME_EVENT_RESULT_REQUEST(636),
    USER_TIME_EVENT_COMPLETE_REQUEST(637),
    USER_TIME_EVENT_PASSED_TIME_SET(638),
    INVASION_SETTING_REQUEST(639),
    USER_FIELD_ATTACK_OBJ_REQUEST_BOARDING(640),
    USER_FIELD_ATTACK_OBJ_REQUEST_GET_OFF(641),
    MONSTER_BATTLE(642),
    BOSS_ARENA_MATCH_REQUEST(643),
    BOSS_ARENA_MIGRATE_REQUEST(644),
    MOB_USER_CONTROL_SKILL_Q_PUSH(645),
    MOB_USER_CONTROL_SKILL_Q_POP(646),
    MOB_USER_CONTROL_SKILL_FORCED_Q_POP(647),
    PARTY_QUEST_RANKING_REQUEST(648),
    ATTENDANCE_EVENT(649),
    ATTENDANCE_EVENT_REWARD_REQ(650),
    UI_ATTENDANCE_GHOST_CHANGE_REQ(651),
    EVENT_UI_REQ(652),
    ATTENDANCE_EVENT_SELECT_REWARD(653),
    USER_SET_CUSTOMIZE_EFFECT(655),
    USER_CUSTOMIZE_EFFECT_ITEM_MODIFY(656),
    USER_GROWTH_HELPER_REQUEST(657),
    USER_CONTENTS_MAP_REQUEST(658),
    USER_TIMER_REQ(659),
    USER_MANNEQUIN(660),
    USER_BUFFER_FLY_REQ(661),
    USER_NON_TARGET_FORCE_ATOM_ATTACK(662),
    USER_RUN_SCRIPT(663),
    CHECK_BOSS_PARTY_BY_SCRIPT(664),
    SET_GO_TO_SHINE_CITY_WAIT_FIELD(665),
    SET_STARPLANET_MINI_GAME_CUSTOMIZING_OPTION(666),
    STAR_PLANET_TREND_SHOP_SET_AVATAR(667),
    STAR_PLANET_TREND_SHOP_RESTORE_AVATAR(668),
    STAR_PLANET_REQUEST(669),
    STAR_PLANET_MATCHING_REENTRANCE(670),
    STAR_PLANET_RESULT_UI_EXIT_SCRIPT(671),
    MOVE_TO_URUS_WAITING_FIELD(672),
    MONSTER_COLLECTION_COMPLETE_REWARD_REQ(673),
    MONSTER_COLLECTION_EXPLORE_REQ(674),
    PLAT_FORMAR_ENTER_REQUEST(675),
    STIGMA_DELEVERY_REQUEST(676),

    B_E_G_I_N__E_V_E_N_T__G_R_O_U_P(677),
    INVITATION_ACCEPT_USER(678),
    INVITATION_CANCEL_USER(679),
    EVENT_GROUP_LEAVE(680),
    B_E_G_I_N__E_V_E_N_T__G_R_O_U_P__P_A_C_K_E_T(681),
    E_N_D__E_V_E_N_T__G_R_O_U_P__P_A_C_K_E_T(682),
    E_N_D__E_V_E_N_T__G_R_O_U_P(683),

    B_E_G_I_N__E_V_E_N_T__R_A_N_K_I_N_G(684),
    EVENT_RANKING_OPEN(685),
    EVENT_RANKING_HELP(686),
    E_N_D__E_V_E_N_T__R_A_N_K_I_N_G(687),

    B_E_G_I_N__A_F_R_E_E_C_A_T_V(688),
    AFREECA_TV_BROAD_START(689),
    AFREECA_TV_BROAD_STOP(690),
    E_N_D__A_F_R_E_E_C_A_T_V(691),

    // TODO : mismatch somewhere from 679 - 707

    B_E_G_I_N__S_T_A_R_P_L_A_N_E_T__M_A_T_C_H_I_N_G(694),
    STAR_PLANET_MATCHING__REGISTER_REQ(695),
    STAR_PLANET_MATCHING__UN_REGISTER_REQ(696),
    STAR_PLANET_MATCHING__REGISTER_PARTY_REQ(697),
    STAR_PLANET_MATCHING__JOIN_PARTY_PLAY_REQ(698),
    STAR_PLANET_MATCHING__TRANSFER_ASSENT(699),
    STAR_PLANET_MATCHING__HELP_SCRIPT(700),
    E_N_D__S_T_A_R_P_L_A_N_E_T__M_A_T_C_H_I_N_G(701),

    B_E_G_I_N__S_T_A_R_P_L_A_N_E_T__Q_U_E_U_E(702),
    STAR_PLANET_QUEUE__REGISTER_REQ(703),
    STAR_PLANET_QUEUE__UN_REGISTER_REQ(704),
    E_N_D__S_T_A_R_P_L_A_N_E_T__Q_U_E_U_E(705),

    B_E_G_I_N__S_T_A_R_P_L_A_N_E_T__I_N_V_I_T_A_T_I_O_N(706),
    STAR_PLANET_INVITATION_ACCEPT(707),
    STAR_PLANET_INVITATION_CANCLE(708),
    E_N_D__S_T_A_R_P_L_A_N_E_T__I_N_V_I_T_A_T_I_O_N(709),

    USER_SURVEY_REQUEST(710),
    NPS_INFO(711),
    USER_FLAME_ORB_REQUEST(712),
    USER__SAD_RESULT_UI__CLOSE(713),
    FREE_LOOK_CHANGE_REQUEST(714),
    FREE_LOOK_CHANGE_UI_OPEN_FAILED(715),
    USER_SMART_PHONE_CALL_REQUEST(716),
    USER_JAGUAR_CHANGE_REQUEST(717),
    FPS_LOG(718),
    PACKET_MODIFY_LOG(719),
    BATTLE_USER_AVATAR_SELECT(721),
    BATTLE_USER_ATTACK(722),
    BATTLE_USER_ATTACK_EXPIRE(723),
    BATTLE_USER_ATTACK_POSITION(724),
    BATTLE_USER_HIT(725),
    BATTLE_USER_HIT_BY_MOB(726),
    BATTLE_STAT_CORE_REQUEST(727),
    BATTLE_USER_ALIVE(728),
    USER_CONTENTS_BOOK_REQUEST(729),
    PERFORMANCE_CLIENT_LOGIN(730),
    PERFORMANCE_CLIENT_IN_FIELD(731),
    PING_CHECK_REQUEST__CLIENT_TO_GAME(732),
    PING__CLIENT_TO_GAME(733),
    PLANT_POT_CLICK(734),
    RANDOM_MISSION_REQUEST(735),
    ITEM_COLLECTION_SET_FLAG(736),
    ITEM_COLLECTION_CHECK_COMPLETE(737),
    SELF_STAT_CHANGE_REQUEST(738),
    CASH_BUFF_EVENT_CANCLE(739),
    CREATE_PSYCHIC_LOCK(740),
    RESET_PATH_PSYCHIC_LOCK(741),
    RELEASE_PSYCHIC_LOCK(742),
    RESERVE_DAMAGE_PSYCHIC_LOCK(743),
    CREATE_KINESIS_PSYCHIC_AREA(744),
    DO_ACTIVE_PSYCHIC_AREA(745),
    DEBUFF_PSYCHIC_AREA(746),
    RELEASE_PSYCHIC_AREA(747),
    PSYCHIC_OVER_REQUEST(748),
    DEC_PSYCHIC_POINT_REQUEST(749),
    TOUCH_ME_END_REQUEST(751), // TODO: check PSYCHIC
    BITE_ATTACK_RESPONSE(752),
    SAVE_URUS_SKILL(753),
    GET_SAVED_URUS_SKILL(754),
    URUS_SHOP_REQUEST(755),
    URUS_PARTY_MEMBER_LIST(756),
    USER_KEY_DOWN_STEP_REQUEST(757),
    DAILY_GIFT_REQUEST(758),
    SKILL_COMMAND_LOCK(759),
    BEAST_FORM_WING_OFF(760),
    RESET_AIR_HIT_COUNT_REQUEST(761),
    RW_ACTION_CANCEL(762),
    RELEASE_RW_GRAB(763),
    RW_CLEAR_CURRENT_ATTACK_REQUEST(764),
    RW_MULTI_CHARGE_CANCEL_REQUEST(765),
    FUNTION_FOOTHOLD_MAN(766),

    MONSTER_BOOK_MOB_INFO(779),
    GACHAPON_REQUEST(781),
    NEBULITE_INSERT_REQUEST(782),
    SOCKET_CREATE_REQUEST(783),

    FAMILIAR_ADD_REQUEST(795),
    FAMILIAR_SPAWN_REQUEST(796),
    FAMILIAR_RENAME_REQUEST(797),

    // Beast Tamer
    BEAST_TAMER_REGROUP_REQUEST(831),

    SALON_REQUEST(842),

    FAMILIAR_MOVE(856),
    FAMILIAR_ATTACK(858),
    FAMILIAR_SKILL(859),

    GUILD_BBS(808),
    SURPRISE_BOX(816),

    E_N_D__U_S_E_R(809),

    B_E_G_I_N__F_I_E_L_D(875),
    B_E_G_I_N__L_I_F_E_P_O_O_L(876),


    B_E_G_I_N__M_O_B(877),
    MOB_MOVE(878),
    MOB_APPLY_CTRL(879),
    MOB_DROP_PICK_UP_REQUEST(880),
    MOB_HIT_BY_OBSTACLE(881),
    MOB_HIT_BY_OBSTACLE_ATOM(882),
    MOB_HIT_BY_MOB(883),
    MOB_SELF_DESTRUCT(884),
    MOB_SELF_DESTRUCT_COLLISION_GROUP(885),
    MOB_ATTACK_MOB(886),
    MOB_SKILL_DELAY_END(886),
    MOB_TIME_BOMB_END(887),
    MOB_ESCORT_COLLISION(888),
    MOB_REQUEST_ESCORT_INFO(889),
    MOB_ESCORT_STOP_END_REQUEST(890),
    MOB_AREA_ATTACK_DISEASE(891),
    MOB_EXPLOSION_START(892),
    MOB_LIFTING_END(893),
    MOB_UPDATE_FIXED_MOVE_DIR(894),
    MOB_CREATE_FIRE_WALK(895),
    MOB_AFTER_DEAD_REQUEST(896),
    MOB_DAMAGE_SHARE_INFO(897),
    MOB_CREATE_AFFECTED_AREA(898),
    MOB_DOWN_RESPONSE(899),
    E_N_D__M_O_B(900),

    B_E_G_I_N__N_P_C(903),
    NPC_MOVE(904),
    NPC_SPECIAL_ACTION(905),
    E_N_D__N_P_C(906),
    E_N_D__L_I_F_E_P_O_O_L(907),

    B_E_G_I_N__D_R_O_P_P_O_O_L(908),
    DROP_PICK_UP_REQUEST(909),
    E_N_D__D_R_O_P_P_O_O_L(910),

    B_E_G_I_N__R_E_A_C_T_O_R_P_O_O_L(910),
    REACTOR_HIT(911),
    REACTOR_CLICK(912),
    REACTOR_RECT_IN_MOB(913),
    REACTOR_KEY(914),
    E_N_D__R_E_A_C_T_O_R_P_O_O_L(915),

    B_E_G_I_N__F_I_S_H_I_N_G_Z_O_N_E_P_O_O_L(916),
    FISHING_INFO(917),
    FISHING_END(918),
    E_N_D__F_I_S_H_I_N_G_Z_O_N_E_P_O_O_L(919),

    B_E_G_I_N__P_E_R_S_O_N_A_L__O_B_J_E_C_T(921),
    DECOMPOSER_REQUEST(922),
    E_N_D__P_E_R_S_O_N_A_L__O_B_J_E_C_T(923),

    B_E_G_I_N__E_V_E_N_T__F_I_E_L_D(924),
    EVENT_START(925),
    SNOW_BALL_HIT(926),
    SNOW_BALL_TOUCH(927),
    COCONUT_HIT(928),
    TOURNAMENT_MATCH_TABLE(929),
    PULLEY_HIT(930),
    E_N_D__E_V_E_N_T__F_I_E_L_D(931),

    B_E_G_I_N__M_O_N_S_T_E_R__C_A_R_N_I_V_A_L__F_I_E_L_D(932),
    M_CARNIVAL_REQUEST(933),
    E_N_D__M_O_N_S_T_E_R__C_A_R_N_I_V_A_L__F_I_E_L_D(933),

    B_E_G_I_N__D_E_F_E_N_S_E__F_I_E_L_D(935),
    DEFENSE_GAME_REQUEST(936),
    E_N_D__D_E_F_E_N_S_E__F_I_E_L_D(937),

    C_O_N_T_I_S_T_A_T_E(938),
    B_E_G_I_N__P_A_R_T_Y__M_A_T_C_H(939),
    INVITE_PARTY_MATCH(943),
    CANCEL_INVITE_PARTY_MATCH(944),
    PARTY_MEMBER_CANDIDATE_REQUEST(945),
    URUS_PARTY_MEMBER_CANDIDATE_REQUEST(946),
    PARTY_CANDIDATE_REQUEST(947),
    INTRUSION_FRIEND_CANDIDATE_REQUEST(948),
    INTRUSION_LOBBY_CANDIDATE_REQUEST(949),
    END_PARTY_MATCH(950),
    REQUEST_FOOT_HOLD_MOVE(951),
    GATHER_REQUEST(952),
    GATHER_END_NOTICE(953),
    ACT_CHANGE_REACTOR_USE_REQUST(954),
    USER_ANTI_MACRO_BOMB_REQUEST(955),
    MAKE_ENTER_FIELD_PACKET_FOR_QUICK_MOVE(956),
    RUNE_STONE_USE_REQ(957),
    RUNE_STONE_SKILL_REQ(958),
    OBTACLE_ATOM_COLLISION(959),
    TIMER_GAUGE_TIMEOUT(960),
    FALLING_CATCHER_HACK_CHECK(961),
    COOK_GAME_REQUEST(962),
    FPS_MODE_EXIT_REQUEST(963),
    MAKE_ENTER_FIELD_PACKET_FOR_MIRROR_DUNGEON(964),
    LEAVE_MIRROR_DUNGEON(965),

    B_E_G_I_N__R_H_Y_T_H_M_G_A_M_E(-1),
    RHYTHM_GAME_HIT_NOTE(966),
    RHYTHM_GAME_START_REGION(967),
    RHYTHM_GAME_END_REGION(968),
    RHYTHM_GAME_END_SPECIAL_ATTACK(967),
    RHYTHM_GAME_SELECT_MODE(968),
    RHYTHM_GAME_ATTACK_REQUEST(969),
    RHYTHM_GAME_NPC_ACTION_REQUEST(970),
    RHYTHM_GAME_EXIT_GAME(971),
    E_N_D__R_H_Y_T_H_M_G_A_M_E(972),

    B_E_G_I_N__M_O_U_N_T_A_I_N_R_I_D_I_N_G(973),
    MOUNTAIN_RIDING_HP_INFO(974),
    MOUNTAIN_RIDING_MOB_INFO(975),
    E_N_D__M_O_U_N_T_A_I_N_R_I_D_I_N_G(976),

    B_E_G_I_N__P_I_R_A_N_H_A(977),
    PIRANHA_INFO(978),
    PIRANHA_DIE(979),
    PIRANHA_DAMAGE(980),
    PIRANHA_PREPARE(981),
    E_N_D__P_I_R_A_N_H_A(982),

    B_E_G_I_N__R_O_B_B_I_N_S(983),
    ROBBINS__INC_BOMB(984),
    ROBBINS__DEC_BOMB(985),
    ROBBINS__TOSS_BOMB(986),
    E_N_D__R_O_B_B_I_N_S(987),

    B_E_G_I_N__F_A_L_L_I_N_G_S_T_O_N_E(988),
    FALLING_STONE_INFO(989),
    FALLING_STONE_HP_INFO(990),
    FALLING_STONE_PREPARE(991),
    E_N_D__F_A_L_L_I_N_G_S_T_O_N_E(992),

    B_E_G_I_N__B_A_T_T_L_E_F_I_E_L_D(993),
    BATTLE_FIELD_DAMAGE(994),
    BATTLE_FIELD_SHEEP_REVIVAL_REQ(995),
    BATTLE_FIELD_SHEEP_REVIVE(996),
    E_N_D__B_A_T_T_L_E_F_I_E_L_D(997),

    B_E_G_I_N__B_O_S_S_A_R_E_N_A(998),
    BOSS_ARENA_SELECT_BOSS(999),
    BOSS_ARENA_SELECT_SKILL(1000),
    BOSS_ARENA_SELECT_DIFFICULTY(1001),
    BOSS_ARENA_SELECT_CONFIRM(1002),
    E_N_D__B_O_S_S_A_R_E_N_A(1003),

    B_E_G_I_N__C_A_T_A_P_U_L_T(1004),
    CATAPULT__UPGRADE_SKILL(1005),
    E_N_D__C_A_T_A_P_U_L_T(1006),

    FIELD_ATTACK_OBJ_PUSH_ACT(1007),
    FIELD_RESPAWN(1008),
    B_E_G_I_N__S_O_U_L__D_U_N_G_E_O_N(1009),
    SOUL_ITEM_USE(1010),
    SOUL_ITEM_USE_VOTE(1011),
    SOUL_DUNGE_SUMM_SOUL_BOSS(1012),
    E_N_D__S_O_U_L__D_U_N_G_E_O_N(1013),

    B_E_G_I_N__S_P_A_C_E_M_O_O_N_R_A_B_B_I_T(1014),
    SPACE_MORABBIT_HIT_EFF(1015),
    SPACE_MORABBIT_DAMAGE(1016),
    E_N_D__S_P_A_C_E_M_O_O_N_R_A_B_B_I_T(1017),

    B_E_G_I_N__H_U_N_D_R_E_D_B_I_N_G_O(1018),
    H_BINGO_CHECK_NUMBER(1019),
    H_BINGO_CHECK_BINGO(1020),
    E_N_D__H_U_N_D_R_E_D_B_I_N_G_O(1021),

    B_E_G_I_N__T_Y_P_I_N_G_G_A_M_E(1022),
    TYPING_GAME_CHECK_MSG(1023),
    TYPING_GAME_WORD_MOB_ATTACK(1024),
    TYPING_GAME_LEVEL_SELECT(1025),
    TYPING_GAME_SKILL_ATTACK(1026),
    TYPING_GAME_FIELD_OUT(1027),
    E_N_D__T_Y_P_I_N_G_G_A_M_E(1028),

    B_E_G_I_N__H_U_N_D_R_E_D_O_X_Q_UI_Z(1029),
    H_OX_QUIZ_SELECTED(1030),
    E_N_D__H_U_N_D_R_E_D_O_X_Q_UI_Z(1031),

    B_E_G_I_N__S_T_A_R_P_L_A_N_E_T_R_P_S(1032),
    S_P_RPS_GAME_USER_SELECT(1033),
    S_P_RPS_GAME_ANSWER(1034),
    S_P_RPS_GAME_LEAVE(1035),
    E_N_D__S_T_A_R_P_L_A_N_E_T_R_P_S(1036),

    B_E_G_I_N__S_I_D_E_S_C_R_O_L_L_F_L_Y_S_H_O_O_T_I_N_G(1037),
    SSFS_HIT_USER(1038),
    SSFS_LETHAL_ATTACK(1039),
    SSFS_EXIT(1040),
    E_N_D__S_I_D_E_S_C_R_O_L_L_F_L_Y_S_H_O_O_T_I_N_G(1041),

    B_E_G_I_N__F_I_E_L_D_S_T_A_T_E(1042),
    STAR_PLANET__H_BINGO_CHECK_NUMBER(1043),
    STAR_PLANET__H_BINGO_CHECK_BINGO(1044),
    STAR_PLANET__H_BINGO_SEND_NUMBER(1045),
    STAR_PLANET__H_RPS_SELECTED(1046),
    STAR_PLANET__GROUP_DANCE_SELECTED(1047),
    STAR_PLANET__GROUP_DANCE_CANCEL(1048),
    E_N_D__F_I_E_L_D_S_T_A_T_E(1049),

    B_E_G_I_N__M_O_M_E_N_T_S_W_I_M_A_R_E_A(1050),
    MOMENT_SWIM_AREA__WATER_LEVEL(1051),
    E_N_D__M_O_M_E_N_T_S_W_I_M_A_R_E_A(-1),

    B_E_G_I_N__G_H_O_S_T_P_A_R_K(-1),
    GHOST_PARK_USE_RUNE_REQ(1052),
    GHOST_PARK_RUNE_HEAL_REQ(1053),
    GHOST_PARK_HIT_BULLET(1054),
    E_N_D__G_H_O_S_T_P_A_R_K(1055),

    B_E_G_I_N__F_I_E_L_D__U_R_U_S(1056),
    URUS_SAVE_DEADLY_USER(1057),
    E_N_D__F_I_E_L_D__U_R_U_S(1058),

    FIELD_SKILL_USE_REQUEST(1059),
    FIELD_WEATHER__MSG(1060),
    FIELD_STAT_Q_R_EX_DAY(1061),

    B_E_G_I_N__P_O_L_O_F_R_I_T_O_B_O_U_N_T_Y_H_U_N_T_I_N_G(1062),
    POLO_FRITO_BOUNTY_HUNTING_REQUEST(1063),
    E_N_D__P_O_L_O_F_R_I_T_O_B_O_U_N_T_Y_H_U_N_T_I_N_G(1064),

    B_E_G_I_N__P_O_L_O_F_R_I_T_O_T_O_W_N_D_E_F_E_N_S_E(1065),
    POLO_FRITO_TOWN_DEFENSE_REQUEST(1066),
    E_N_D__P_O_L_O_F_R_I_T_O_T_O_W_N_D_E_F_E_N_S_E(1067),

    B_E_G_I_N__P_O_L_O_F_R_I_T_O_C_O_U_R_T_S_H_I_P_D_A_N_C_E(1068),
    POLO_FRITO_COURTSHIP_DANCE_RESULT(1069),
    E_N_D__P_O_L_O_F_R_I_T_O_C_O_U_R_T_S_H_I_P_D_A_N_C_E(1070),

    B_E_G_I_N__B_O_S_S__D_E_M_I_A_N__F_L_Y_I_N_G_S_W_O_R_D(1074),
    DEMIAN_OBJECT_MAKE_ENTER_ACK(1075),
    DEMIAN_OBJECT_NODE_END(1076),
    DEMIAN_OBJECT_ERR__RECREATE(1077),
    E_N_D__B_O_S_S__D_E_M_I_A_N__F_L_Y_I_N_G_S_W_O_R_D(1078),
    E_N_D__F_I_E_L_D(1085),

    GOLLUX_OUT_REQUEST(1088),

    CHECK_S_P_W_CREATE_NEW_CHARACTER(1097),
    SET_S_P_W(1098),

    B_E_G_I_N__C_A_S_H_S_H_O_P(1103),
    CASH_SHOP_CHARGE_PARAM_REQUEST(1104),
    CASH_SHOP_QUERY_CASH_REQUEST(1105),
    CASH_SHOP_CASH_ITEM_REQUEST(1106),
    CASH_SHOP_CHECK_COUPON_REQUEST(1107),
    CASH_SHOP_MEMBER_SHOP_REQUEST(1108),
    CASH_SHOP_GIFT_MATE_INFO_REQUEST(1109),
    CASH_SHOP_SEARCH_LOG(1110),
    CASH_SHOP_COODINATION_REQUEST(1111),
    CASH_SHOP_CHECK_MILEAGE_REQUEST(1112),
    CASH_SHOP_NAVER_USAGE_INFO_REQUEST(1113),
    MVP_DAILY_PACK_REQUEST(1114),
    MVP_SPECIAL_PACK_REQUEST(1115),
    CASH_SHOP_FREE_ITEM(1116),
    CASH_SHOP_ACTION(1118),
    CASH_SHOP_DONE(1119),
    E_N_D__C_A_S_H_S_H_O_P(1120),

    B_E_G_I_N__R_A_I_S_E(9999),
    RAISE_REFESH(9999),
    RAISE_UI_STATE(9999),
    RAISE_INC_EXP(9999),
    RAISE_ADD_PIECE(9999),
    E_N_D__R_A_I_S_E(9999),

    SEND_MATE_MAIL(9999),
    REQUEST_GUILD_BOARD_AUTH_KEY(9999),
    REQUEST_CONSULT_AUTH_KEY(9999),
    REQUEST_CLASS_COMPETION_AUTH_KEY(9999),
    REQUEST_WEB_BOARD_AUTH_KEY(9999),
    IRBOX_EVENT(9999),

    B_E_G_I_N__I_T_E_M_U_P_G_R_A_D_E(1137),
    GOLD_HAMMER_REQUEST(1138),
    GOLD_HAMMER_COMPLETE(1139),
    E_N_D__I_T_E_M_U_P_G_R_A_D_E(1140),

    B_E_G_I_N__B_A_T_T_L_E_R_E_C_O_R_D(1142),
    BATTLE_RECORD_ON_OFF_REQUEST(1143),
    BATTLE_RECORD_SKILL_DAMAGE_LOG(1144),
    E_N_D__B_A_T_T_L_E_R_E_C_O_R_D(1145),

    B_E_G_I_N__F_A_R_M(1087),
    INSERT_FARM_OBJECT(1148),
    DELETE_FARM_OBJECT(1149),
    INSERT_FARM_TILE(1150),
    DELETE_FARM_TILE(1151),
    TRANSFER_FARM_FIELD(1152),
    BUY_FARM_OBJECT(1153),
    SEL_FARM_OBJECT(1153),
    MY_HOUSE_UPGRADE(1154),
    FARM_ADMIN_COMMAND(1155),
    REQUEST_QUEST_COMPLETE(1156),
    CHECK_FARM_NAME(1157),
    REQUEST_INTERACT_OBJECT(1157),
    REQUEST_CONSUME_OBJECT(1159),
    REQUEST_CLICK_NPC(1160),
    REQUEST_RANDOM_FARM(1161),
    REQUEST_FARM_VISIT(1162),
    REQUEST_ADD_FARM_FRIEND(1163),
    FARM_IN_GAME_FRIEND_REQUEST(1164),
    REQUEST_DELETE_FARM_FRIEND(1165),
    REQUEST_ADD_BLACK_LIST(1166),
    REQUEST_DELETE_BLACK_LIST(1167),
    REQUEST_FARM_INFO_UI(1168),
    CHECK_FARM_NAME_BY_CONSUME(1169),
    REQUEST_AUTO_HARVEST(1170),
    REQUEST_SOUL_RECHARGE(1171),
    REQUEST_TRANFER_FARM_BY_NAME(1172),
    MY_FARM_MONSTER_REQUEST(1173),
    FARM_MONSTER_COMBINE(1174),
    FARM_MONSTER_SET_NAME_REQUEST(1115),
    FARM_MONSTER_CARE(1176),
    FARM_MONSTER_PLAY(1177),
    FARM_MONSTER_RELEASE(1178),
    FARM_MONSTER_SEARCH_REQUEST(1179),
    FARM_MONSTER_POS_RECODE(1180),
    FARM_MONSTER_LOCKER_REQUEST(1181),
    FARM_MONSTER_LIFE_EXTEND(1182),
    PHOTO_UPDATE(1183),
    PHOTO_GET_REQUEST(1184),
    MY_HOME_SEND_NOTICE_REQUEST(1185),
    MY_HOME_LOAD_MAIN_LOG_REQUEST(1186),
    MY_HOME_DELETE_LOG_REQUEST(1187),
    FARM_WHISPER(1188),
    FARM_GROUP_MESSAGE(1189),
    REQUEST_FARM_FRIEND_RECOMMEND(1190),
    FARM_REQUEST_IN_GAME_INFO_SET(1191),
    FARM_REQUEST_IN_GAME_INFO_MOD(1192),
    REQUEST_FARM_FRIEND_INFO(1193),
    REQUEST_FARM_TODAY(1194),
    REQUEST_FIRST_ENTER_REWARD(1195),
    CHECK_ADMIN_ACCOUNT_LOG(1196),
    E_N_D__F_A_R_M(1197),

    CHARACTER_LIST_REQUEST(9999),
    COPY_CHARACTER_TO_TEST_REQUEST(9999),
    NO(12345),
    ;

    private static Set<InHeader> spam = Set.of(
            UPDATE_CLIENT_ENVIRONMENT,
            PONG,
            WVS_SET_UP_STEP,
            SECURITY_PACKET,
            PRIVATE_SERVER_PACKET,
            MOB_MOVE,
            USER_MOVE,
            PASSIVE_SKILL_INFO_UPDATE,
            USER_CHANGE_STAT_REQUEST,
            SUMMONED_MOVE,
            USER_CALC_DAMAGE_STAT_SET_REQUEST,
            USER_TEMPORARY_STAT_UPDATE_REQUEST,
            MOB_APPLY_CTRL,
            USER_REQUEST_INSTANCE_TABLE,
            USER_QUEST_REQUEST,
            CHECK_LOGIN_AUTH_INFO,
            CHECK_SPW_REQUEST,
            NPC_MOVE,
            FAMILIAR_MOVE,
            PET_MOVE,
            ALIVE_ACK,
            OBTACLE_ATOM_COLLISION,
            ANDROID_MOVE,
            DRAGON_MOVE,
            USER_HIT,
            USER_EMOTION,
            USER_B2_BODY_REQUEST
    );

    private short value;
    private final static Map<Short, InHeader> opToHeaderMap = new HashMap<>();

    static {
        for (InHeader ih : values()) {
            opToHeaderMap.put(ih.getValue(), ih);
        }
    }

    InHeader(int op) {
        this.value = (short) op;
    }

    public short getValue() {
        return value;
    }

    public static InHeader getInHeaderByOp(int op) {
        return opToHeaderMap.getOrDefault((short) op, null);
    }

    public static boolean isSpamHeader(InHeader inHeaderByOp) {
        return spam.contains(inHeaderByOp);
    }
}
