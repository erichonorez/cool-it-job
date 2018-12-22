# Users schema

# --- !Ups

CREATE TABLE jobs2 (
    job_id varchar(255) NOT NULL PRIMARY KEY,
    title varchar(255) NOT NULL,
    location varchar(255) NOT NULL,
    description TEXT NOT NULL,
    target_salary DECIMAL NOT NULL,
    maximum_salary DECIMAL NOT NULL,
    has_homeworking TINYINT(1) NOT NULL,
    has_health_insurance TINYINT(1) NOT NULL,
    has_car TINYINT(1) NOT NULL,
    has_lunch_voucher TINYINT(1) NOT NULL,
    has_bonus TINYINT(1) NOT NULL,
    latitude DOUBLE NOT NULL,
    longitude DOUBLE NOT NULL,
    company_id varchar(255) NOT NULL
);

# --- !Downs

DROP TABLE jobs2;