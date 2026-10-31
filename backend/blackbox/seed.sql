-- Resets the blackbox test database (gykas_test) to a known fixture state.
--
-- The schema is created by the application itself on startup
-- (SchemaUtils.create in DatabaseFactory), so this script contains no DDL —
-- it only wipes all data and re-inserts the fixtures, and can run repeatedly
-- without restarting the app.
--
-- The password of all fixture users is: p2%123a
-- (Argon2 parameters are stored inside the hash string, so the hashes keep
-- verifying even if the provider's calibration changes later.)

TRUNCATE TABLE
    answers,
    close_question_options,
    questions,
    polls,
    poll_templates,
    student_lists_students,
    student_lists,
    students,
    classes,
    teachers,
    admins,
    poll_creators,
    users
RESTART IDENTITY CASCADE;

-- All people below are invented test fixtures, not real persons.
INSERT INTO users (id, first_name, last_name, password_hash, uuid) VALUES
    (1, 'Jenda', 'Perníček',
     '$argon2i$v=19$m=65536,t=10,p=1$ZmRzZmRzZnNkZ2doZ2RzZmZmYmdi$lCmQaHQYrLyGpROzRJdMxw',
     'jenda'),
    (2, 'Petr', 'Kramný',
     '$argon2i$v=19$m=65536,t=10,p=1$ZmRzZmRzZnNkZ2doZ2RzZmZmYmdi$lCmQaHQYrLyGpROzRJdMxw',
     'kramny'),
    (3, 'Jindřich', 'ZeSkalice',
     '$argon2i$v=19$m=65536,t=10,p=1$ZmRzZmRzZnNkZ2doZ2RzZmZmYmdi$lCmQaHQYrLyGpROzRJdMxw',
     'jindra');

INSERT INTO classes (id, name) VALUES (1, '3.B');
INSERT INTO classes (id, name) VALUES (2, '9.A');

INSERT INTO students (id, classes_id) VALUES (1, 1);

INSERT INTO poll_creators (id) VALUES (2), (3);

INSERT INTO admins (id) VALUES (2);

INSERT INTO teachers (id, is_active) VALUES (3, true);
