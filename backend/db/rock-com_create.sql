-- Created by Vertabelo (http://vertabelo.com)
-- Last modification date: 2025-04-24 15:09:02.946

-- tables
-- Table: brackets
CREATE TABLE brackets (
    bracket_id int  NOT NULL,
    match_id int  NOT NULL,
    next_match_id int  NOT NULL,
    CONSTRAINT brackets_pk PRIMARY KEY (bracket_id)
);

-- Table: competition_dates
CREATE TABLE competition_dates (
    date_id serial  NOT NULL,
    competition_id int  NOT NULL,
    start_time timestamp  NOT NULL,
    end_time timestamp  NOT NULL,
    CONSTRAINT competition_dates_pk PRIMARY KEY (date_id)
);

-- Table: competition_participants
CREATE TABLE competition_participants (
    competition_id int  NOT NULL,
    participant_id int  NOT NULL,
    status_id int  NOT NULL,
    status_change_date timestamp  NOT NULL,
    CONSTRAINT competition_participants_pk PRIMARY KEY (competition_id,participant_id)
);

-- Table: competition_referees
CREATE TABLE competition_referees (
    competition_id int  NOT NULL,
    referee_id int  NOT NULL,
    CONSTRAINT competition_referees_pk PRIMARY KEY (competition_id,referee_id)
);



-- Table: competitions
CREATE TABLE competitions (
    competition_id serial  NOT NULL,
    type varchar(50)  NOT NULL,
    match_duration_minutes int  NOT NULL,
    available_courts int  NOT NULL,
    participants_limit int  NULL,
    street_address varchar(255)  NOT NULL,
    city varchar(100)  NOT NULL,
    postal_code varchar(20)  NOT NULL,
    registration_open boolean  NOT NULL DEFAULT FALSE,
    CONSTRAINT competitions_pk PRIMARY KEY (competition_id)
);

-- Table: match_statuses
CREATE TABLE match_statuses (
    status_id serial  NOT NULL,
    status_label varchar(20)  NOT NULL,
    CONSTRAINT match_statuses_pk PRIMARY KEY (status_id)
);

INSERT INTO match_statuses (status_label) VALUES
('scheduled'),
('completed'),
('delayed'),
('cancelled');

-- Table: matches
CREATE TABLE matches (
    match_id serial  NOT NULL,
    competition_id int  NOT NULL,
    player1_id int  NULL,
    player2_id int  NULL,
    referee_id int  NOT NULL,
    match_date timestamp  NOT NULL,
    status_id int  NOT NULL,
    score varchar(10)  NULL,
    winner_id int  NULL,
    CONSTRAINT matches_pk PRIMARY KEY (match_id)
);

-- Table: organizers
CREATE TABLE organizers (
    user_id int  NOT NULL,
    is_admin boolean  NOT NULL,
    CONSTRAINT organizers_pk PRIMARY KEY (user_id)
);

-- Table: participant_statuses
CREATE TABLE participant_statuses (
    status_id serial  NOT NULL,
    status_label varchar(20)  NOT NULL,
    CONSTRAINT participant_statuses_pk PRIMARY KEY (status_id)
);

INSERT INTO participant_statuses (status_label) VALUES
('confirmed'),
('withdrawn'),
('waiting_list');

-- Table: participants
CREATE TABLE participants (
    user_id int  NOT NULL,
    birth_date date  NOT NULL,
    CONSTRAINT user_id PRIMARY KEY (user_id)
);

-- Table: referee_licences
CREATE TABLE referee_licences (
    type_id int  NOT NULL,
    referee_id int  NOT NULL,
    license varchar(50)  NOT NULL,
    CONSTRAINT referee_licences_pk PRIMARY KEY (type_id,referee_id)
);

-- Table: referees
CREATE TABLE referees (
    user_id int  NOT NULL,
    CONSTRAINT referees_pk PRIMARY KEY (user_id)
);

-- Table: users
CREATE TABLE users (
    user_id serial  NOT NULL,
    firstname varchar(50)  NOT NULL,
    lastname varchar(50)  NOT NULL,
    email varchar(50)  NOT NULL,
    city varchar(50)  NOT NULL,
    phone_number varchar(12)  NOT NULL,
    CONSTRAINT users_pk PRIMARY KEY (user_id)
);

-- foreign keys
-- Reference: Table_14_competitions (table: competition_referees)
ALTER TABLE competition_referees ADD CONSTRAINT Table_14_competitions
    FOREIGN KEY (competition_id)
    REFERENCES competitions (competition_id)  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: bracket_matches_1 (table: brackets)
ALTER TABLE brackets ADD CONSTRAINT bracket_matches_1
    FOREIGN KEY (match_id)
    REFERENCES matches (match_id)  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: bracket_matches_2 (table: brackets)
ALTER TABLE brackets ADD CONSTRAINT bracket_matches_2
    FOREIGN KEY (next_match_id)
    REFERENCES matches (match_id)  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: competition_dates_competitions (table: competition_dates)
ALTER TABLE competition_dates ADD CONSTRAINT competition_dates_competitions
    FOREIGN KEY (competition_id)
    REFERENCES competitions (competition_id)  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: competition_participants_competitions (table: competition_participants)
ALTER TABLE competition_participants ADD CONSTRAINT competition_participants_competitions
    FOREIGN KEY (competition_id)
    REFERENCES competitions (competition_id)  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: competition_participants_participant_statuses (table: competition_participants)
ALTER TABLE competition_participants ADD CONSTRAINT competition_participants_participant_statuses
    FOREIGN KEY (status_id)
    REFERENCES participant_statuses (status_id)  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: competition_participants_participants (table: competition_participants)
ALTER TABLE competition_participants ADD CONSTRAINT competition_participants_participants
    FOREIGN KEY (participant_id)
    REFERENCES participants (user_id)  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: matches_competition_participants_1 (table: matches)
ALTER TABLE matches ADD CONSTRAINT matches_competition_participants_1
    FOREIGN KEY (competition_id, player1_id)
    REFERENCES competition_participants (competition_id, participant_id)  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: matches_competition_participants_2 (table: matches)
ALTER TABLE matches ADD CONSTRAINT matches_competition_participants_2
    FOREIGN KEY (competition_id, player2_id)
    REFERENCES competition_participants (competition_id, participant_id)  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: matches_competition_participants_3 (table: matches)
ALTER TABLE matches ADD CONSTRAINT matches_competition_participants_3
    FOREIGN KEY (competition_id, winner_id)
    REFERENCES competition_participants (competition_id, participant_id)  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: matches_competition_referees (table: matches)
ALTER TABLE matches ADD CONSTRAINT matches_competition_referees
    FOREIGN KEY (competition_id, referee_id)
    REFERENCES competition_referees (competition_id, referee_id)  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: matches_competitions (table: matches)
ALTER TABLE matches ADD CONSTRAINT matches_competitions
    FOREIGN KEY (competition_id)
    REFERENCES competitions (competition_id)  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: matches_match_statuses (table: matches)
ALTER TABLE matches ADD CONSTRAINT matches_match_statuses
    FOREIGN KEY (status_id)
    REFERENCES match_statuses (status_id)  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: referee_licences_referees (table: referee_licences)
ALTER TABLE referee_licences ADD CONSTRAINT referee_licences_referees
    FOREIGN KEY (referee_id)
    REFERENCES referees (user_id)  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: referees_competition_referees (table: competition_referees)
ALTER TABLE competition_referees ADD CONSTRAINT referees_competition_referees
    FOREIGN KEY (referee_id)
    REFERENCES referees (user_id)  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: users_organizers (table: organizers)
ALTER TABLE organizers ADD CONSTRAINT users_organizers
    FOREIGN KEY (user_id)
    REFERENCES users (user_id)  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: users_participants (table: participants)
ALTER TABLE participants ADD CONSTRAINT users_participants
    FOREIGN KEY (user_id)
    REFERENCES users (user_id)  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: users_referees (table: referees)
ALTER TABLE referees ADD CONSTRAINT users_referees
    FOREIGN KEY (user_id)
    REFERENCES users (user_id)  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- End of file.

