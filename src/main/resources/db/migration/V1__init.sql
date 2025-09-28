-- === V1__init.sql ===
-- Schema init for TARO MVP (PostgreSQL)

-- 1) Tables

CREATE TABLE IF NOT EXISTS users (
                                     user_id     UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                     telegram_id BIGINT UNIQUE NOT NULL,
                                     name        TEXT,
                                     birth_date TEXT
);

CREATE TABLE IF NOT EXISTS cards (
                                     card_id BIGINT PRIMARY KEY,
                                     title   TEXT NOT NULL,
                                     file_name TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS spreads (
                                       spread_id   UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                       telegram_id BIGINT NOT NULL,
                                       topic      TEXT NOT NULL,
                                       cards_id    INTEGER[] NOT NULL,
                                       created_at  TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    -- Ровно 3 карты в раскладе
    CONSTRAINT chk_spreads_cards_3
    CHECK (array_length(cards_id, 1) = 3)
    );

CREATE TABLE IF NOT EXISTS activities (
                                          activity_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                          telegram_id BIGINT NOT NULL,
                                          action      TEXT NOT NULL,
                                          ts          TIMESTAMPTZ NOT NULL DEFAULT NOW()
    );

-- 2) Indexes

CREATE INDEX IF NOT EXISTS idx_spreads_telegram_id   ON spreads(telegram_id);
CREATE INDEX IF NOT EXISTS idx_activities_telegram_id ON activities(telegram_id);
CREATE INDEX IF NOT EXISTS idx_cards_title            ON cards USING GIN (to_tsvector('simple', title));

-- 3) Seed deck (78 cards)
-- Маппинг ID 1..78:
-- 1..22 = Старшие арканы (MAJOR_00..MAJOR_21)
-- 23..36 = Pentacles (Ace..King)
-- 37..50 = Cups (Ace..King)
-- 51..64 = Swords (Ace..King)
-- 65..78 = Wands (Ace..King)

INSERT INTO cards (card_id, title, file_name) VALUES
-- Majors (00..21) → card_id 1..22
(1 , 'The Fool'           , '00-TheFool.jpg'),
(2 , 'The Magician'       , '01-TheMagician.jpg'),
(3 , 'The High Priestess' , '02-TheHighPriestess.jpg'),
(4 , 'The Empress'        , '03-TheEmpress.jpg'),
(5 , 'The Emperor'        , '04-TheEmperor.jpg'),
(6 , 'The Hierophant'     , '05-TheHierophant.jpg'),
(7 , 'The Lovers'         , '06-TheLovers.jpg'),
(8 , 'The Chariot'        , '07-TheChariot.jpg'),
(9 , 'Strength'           , '08-Strength.jpg'),
(10, 'The Hermit'         , '09-TheHermit.jpg'),
(11, 'Wheel of Fortune'   , '10-WheelOfFortune.jpg'),
(12, 'Justice'            , '11-Justice.jpg'),
(13, 'The Hanged Man'     , '12-TheHangedMan.jpg'),
(14, 'Death'              , '13-Death.jpg'),
(15, 'Temperance'         , '14-Temperance.jpg'),
(16, 'The Devil'          , '15-TheDevil.jpg'),
(17, 'The Tower'          , '16-TheTower.jpg'),
(18, 'The Star'           , '17-TheStar.jpg'),
(19, 'The Moon'           , '18-TheMoon.jpg'),
(20, 'The Sun'            , '19-TheSun.jpg'),
(21, 'Judgement'          , '20-Judgement.jpg'),
(22, 'The World'          , '21-TheWorld.jpg'),

-- Pentacles (Ace..King) → 23..36 → Pentacles01..14.jpg
(23, 'Ace of Pentacles'   , 'Pentacles01.jpg'),
(24, 'Two of Pentacles'   , 'Pentacles02.jpg'),
(25, 'Three of Pentacles' , 'Pentacles03.jpg'),
(26, 'Four of Pentacles'  , 'Pentacles04.jpg'),
(27, 'Five of Pentacles'  , 'Pentacles05.jpg'),
(28, 'Six of Pentacles'   , 'Pentacles06.jpg'),
(29, 'Seven of Pentacles' , 'Pentacles07.jpg'),
(30, 'Eight of Pentacles' , 'Pentacles08.jpg'),
(31, 'Nine of Pentacles'  , 'Pentacles09.jpg'),
(32, 'Ten of Pentacles'   , 'Pentacles10.jpg'),
(33, 'Page of Pentacles'  , 'Pentacles11.jpg'),
(34, 'Knight of Pentacles', 'Pentacles12.jpg'),
(35, 'Queen of Pentacles' , 'Pentacles13.jpg'),
(36, 'King of Pentacles'  , 'Pentacles14.jpg'),

-- Cups (Ace..King) → 37..50 → Cups01..14.jpg
(37, 'Ace of Cups'        , 'Cups01.jpg'),
(38, 'Two of Cups'        , 'Cups02.jpg'),
(39, 'Three of Cups'      , 'Cups03.jpg'),
(40, 'Four of Cups'       , 'Cups04.jpg'),
(41, 'Five of Cups'       , 'Cups05.jpg'),
(42, 'Six of Cups'        , 'Cups06.jpg'),
(43, 'Seven of Cups'      , 'Cups07.jpg'),
(44, 'Eight of Cups'      , 'Cups08.jpg'),
(45, 'Nine of Cups'       , 'Cups09.jpg'),
(46, 'Ten of Cups'        , 'Cups10.jpg'),
(47, 'Page of Cups'       , 'Cups11.jpg'),
(48, 'Knight of Cups'     , 'Cups12.jpg'),
(49, 'Queen of Cups'      , 'Cups13.jpg'),
(50, 'King of Cups'       , 'Cups14.jpg'),

-- Swords (Ace..King) → 51..64 → Swords01..14.jpg
(51, 'Ace of Swords'      , 'Swords01.jpg'),
(52, 'Two of Swords'      , 'Swords02.jpg'),
(53, 'Three of Swords'    , 'Swords03.jpg'),
(54, 'Four of Swords'     , 'Swords04.jpg'),
(55, 'Five of Swords'     , 'Swords05.jpg'),
(56, 'Six of Swords'      , 'Swords06.jpg'),
(57, 'Seven of Swords'    , 'Swords07.jpg'),
(58, 'Eight of Swords'    , 'Swords08.jpg'),
(59, 'Nine of Swords'     , 'Swords09.jpg'),
(60, 'Ten of Swords'      , 'Swords10.jpg'),
(61, 'Page of Swords'     , 'Swords11.jpg'),
(62, 'Knight of Swords'   , 'Swords12.jpg'),
(63, 'Queen of Swords'    , 'Swords13.jpg'),
(64, 'King of Swords'     , 'Swords14.jpg'),

-- Wands (Ace..King) → 65..78 → Wands01..14.jpg
(65, 'Ace of Wands'       , 'Wands01.jpg'),
(66, 'Two of Wands'       , 'Wands02.jpg'),
(67, 'Three of Wands'     , 'Wands03.jpg'),
(68, 'Four of Wands'      , 'Wands04.jpg'),
(69, 'Five of Wands'      , 'Wands05.jpg'),
(70, 'Six of Wands'       , 'Wands06.jpg'),
(71, 'Seven of Wands'     , 'Wands07.jpg'),
(72, 'Eight of Wands'     , 'Wands08.jpg'),
(73, 'Nine of Wands'      , 'Wands09.jpg'),
(74, 'Ten of Wands'       , 'Wands10.jpg'),
(75, 'Page of Wands'      , 'Wands11.jpg'),
(76, 'Knight of Wands'    , 'Wands12.jpg'),
(77, 'Queen of Wands'     , 'Wands13.jpg'),
(78, 'King of Wands'      , 'Wands14.jpg')
    ON CONFLICT (card_id) DO UPDATE
                                 SET title = EXCLUDED.title,
                                 file_name = EXCLUDED.file_name;