# --- !Ups

CREATE TABLE trials
(
  id bigserial NOT NULL,
  name character varying NOT NULL,
  description character varying,
  CONSTRAINT pk_trials_id PRIMARY KEY (id )
)

# --- !Downs

DROP TABLE IF EXISTS trials;
