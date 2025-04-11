-- Created by Vertabelo (http://vertabelo.com)
-- Last modification date: 2025-04-11 13:27:37.367

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
    date_id int  NOT NULL,
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

-- Table: competition_types
CREATE TABLE competition_types (
    type_id serial  NOT NULL,
    type_label varchar(20)  NOT NULL,
    CONSTRAINT competition_types_pk PRIMARY KEY (type_id)
);

INSERT INTO competition_types (type_label) VALUES
('Tennis Outdoor'),
('Table Tennis'),
('Badminton');

-- Table: competitions
CREATE TABLE competitions (
    competition_id serial  NOT NULL,
    type_id int  NOT NULL,
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

-- Table: referee_licences
CREATE TABLE referee_licences (
    user_id int  NOT NULL,
    type_id int  NOT NULL,
    license varchar(50)  NOT NULL,
    CONSTRAINT referee_licences_pk PRIMARY KEY (user_id,type_id)
);

-- Table: roles
CREATE TABLE roles (
    role_id serial  NOT NULL,
    role_name varchar(20)  NOT NULL,
    CONSTRAINT roles_pk PRIMARY KEY (role_id)
);

INSERT INTO roles (role_name) 
VALUES 
    ('Organizer'),
    ('Participant'),
    ('Referee');

-- Table: user_roles
CREATE TABLE user_roles (
    user_id int  NOT NULL,
    role_id int  NOT NULL,
    CONSTRAINT user_roles_pk PRIMARY KEY (user_id,role_id)
);

-- Table: users
CREATE TABLE users (
    user_id serial  NOT NULL,
    firstname varchar(50)  NOT NULL,
    lastname varchar(50)  NOT NULL,
    email varchar(50)  NOT NULL,
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

-- Reference: Table_14_user (table: competition_referees)
ALTER TABLE competition_referees ADD CONSTRAINT Table_14_user
    FOREIGN KEY (referee_id)
    REFERENCES users (user_id)  
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

-- Reference: competition_participants_user (table: competition_participants)
ALTER TABLE competition_participants ADD CONSTRAINT competition_participants_user
    FOREIGN KEY (participant_id)
    REFERENCES users (user_id)  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: competitions_competition_types (table: competitions)
ALTER TABLE competitions ADD CONSTRAINT competitions_competition_types
    FOREIGN KEY (type_id)
    REFERENCES competition_types (type_id)  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: judge_licences_competition_types (table: referee_licences)
ALTER TABLE referee_licences ADD CONSTRAINT judge_licences_competition_types
    FOREIGN KEY (type_id)
    REFERENCES competition_types (type_id)  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: judge_licences_user (table: referee_licences)
ALTER TABLE referee_licences ADD CONSTRAINT judge_licences_user
    FOREIGN KEY (user_id)
    REFERENCES users (user_id)  
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

-- Reference: user_roles_roles (table: user_roles)
ALTER TABLE user_roles ADD CONSTRAINT user_roles_roles
    FOREIGN KEY (role_id)
    REFERENCES roles (role_id)  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: user_roles_user (table: user_roles)
ALTER TABLE user_roles ADD CONSTRAINT user_roles_user
    FOREIGN KEY (user_id)
    REFERENCES users (user_id)  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- End of file.

