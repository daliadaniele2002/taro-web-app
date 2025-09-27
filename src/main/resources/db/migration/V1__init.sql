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
                                     title   TEXT NOT NULL
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

INSERT INTO cards(card_id, title) VALUES
-- Majors (00..21)
(1 , 'The Fool'),
(2 , 'The Magician'),
(3 , 'The High Priestess'),
(4 , 'The Empress'),
(5 , 'The Emperor'),
(6 , 'The Hierophant'),
(7 , 'The Lovers'),
(8 , 'The Chariot'),
(9 , 'Strength'),
(10, 'The Hermit'),
(11, 'Wheel of Fortune'),
(12, 'Justice'),
(13, 'The Hanged Man'),
(14, 'Death'),
(15, 'Temperance'),
(16, 'The Devil'),
(17, 'The Tower'),
(18, 'The Star'),
(19, 'The Moon'),
(20, 'The Sun'),
(21, 'Judgement'),
(22, 'The World'),

-- Pentacles (Ace..King) → 23..36
(23, 'Ace of Pentacles'),
(24, 'Two of Pentacles'),
(25, 'Three of Pentacles'),
(26, 'Four of Pentacles'),
(27, 'Five of Pentacles'),
(28, 'Six of Pentacles'),
(29, 'Seven of Pentacles'),
(30, 'Eight of Pentacles'),
(31, 'Nine of Pentacles'),
(32, 'Ten of Pentacles'),
(33, 'Page of Pentacles'),
(34, 'Knight of Pentacles'),
(35, 'Queen of Pentacles'),
(36, 'King of Pentacles'),

-- Cups (Ace..King) → 37..50
(37, 'Ace of Cups'),
(38, 'Two of Cups'),
(39, 'Three of Cups'),
(40, 'Four of Cups'),
(41, 'Five of Cups'),
(42, 'Six of Cups'),
(43, 'Seven of Cups'),
(44, 'Eight of Cups'),
(45, 'Nine of Cups'),
(46, 'Ten of Cups'),
(47, 'Page of Cups'),
(48, 'Knight of Cups'),
(49, 'Queen of Cups'),
(50, 'King of Cups'),

-- Swords (Ace..King) → 51..64
(51, 'Ace of Swords'),
(52, 'Two of Swords'),
(53, 'Three of Swords'),
(54, 'Four of Swords'),
(55, 'Five of Swords'),
(56, 'Six of Swords'),
(57, 'Seven of Swords'),
(58, 'Eight of Swords'),
(59, 'Nine of Swords'),
(60, 'Ten of Swords'),
(61, 'Page of Swords'),
(62, 'Knight of Swords'),
(63, 'Queen of Swords'),
(64, 'King of Swords'),

-- Wands (Ace..King) → 65..78
(65, 'Ace of Wands'),
(66, 'Two of Wands'),
(67, 'Three of Wands'),
(68, 'Four of Wands'),
(69, 'Five of Wands'),
(70, 'Six of Wands'),
(71, 'Seven of Wands'),
(72, 'Eight of Wands'),
(73, 'Nine of Wands'),
(74, 'Ten of Wands'),
(75, 'Page of Wands'),
(76, 'Knight of Wands'),
(77, 'Queen of Wands'),
(78, 'King of Wands')
    ON CONFLICT (card_id) DO NOTHING;