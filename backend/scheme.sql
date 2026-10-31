-- Remove conflicting tables
DROP TABLE IF EXISTS admins CASCADE;
DROP TABLE IF EXISTS answers CASCADE;
DROP TABLE IF EXISTS classes CASCADE;
DROP TABLE IF EXISTS close_question_options CASCADE;
DROP TABLE IF EXISTS poll_creators CASCADE;
DROP TABLE IF EXISTS poll_templates CASCADE;
DROP TABLE IF EXISTS polls CASCADE;
DROP TABLE IF EXISTS questions CASCADE;
DROP TABLE IF EXISTS student_lists CASCADE;
DROP TABLE IF EXISTS students CASCADE;
DROP TABLE IF EXISTS teachers CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS student_lists_students CASCADE;
-- End of removing

CREATE TABLE admins
(
    id INTEGER NOT NULL
);
ALTER TABLE admins
    ADD CONSTRAINT pk_admins PRIMARY KEY (id);

CREATE TABLE answers
(
    id          INTEGER NOT NULL,
    students_id INTEGER NOT NULL,
    polls_id    INTEGER NOT NULL,
    answer      TEXT    NOT NULL
);
ALTER TABLE answers
    ADD CONSTRAINT pk_answers PRIMARY KEY (id, students_id, polls_id);

CREATE TABLE classes
(
    id   SERIAL       NOT NULL,
    name VARCHAR(256) NOT NULL
);
ALTER TABLE classes
    ADD CONSTRAINT pk_classes PRIMARY KEY (id);

CREATE TABLE close_question_options
(
    text TEXT    NOT NULL,
    id   INTEGER NOT NULL
);
ALTER TABLE close_question_options
    ADD CONSTRAINT pk_close_question_options PRIMARY KEY (text, id);

CREATE TABLE poll_creators
(
    id INTEGER NOT NULL
);
ALTER TABLE poll_creators
    ADD CONSTRAINT pk_poll_creators PRIMARY KEY (id);

CREATE TABLE poll_templates
(
    id               SERIAL       NOT NULL,
    poll_creators_id INTEGER      NOT NULL,
    name             VARCHAR(256) NOT NULL,
    is_global        BOOLEAN      NOT NULL
);
ALTER TABLE poll_templates
    ADD CONSTRAINT pk_poll_templates PRIMARY KEY (id);

CREATE TABLE polls
(
    id               SERIAL       NOT NULL,
    poll_creators_id INTEGER      NOT NULL,
    student_lists_id INTEGER      NOT NULL,
    name             VARCHAR(256) NOT NULL,
    start_date       DATE         NOT NULL,
    end_date         DATE         NOT NULL
);
ALTER TABLE polls
    ADD CONSTRAINT pk_polls PRIMARY KEY (id);

CREATE TABLE questions
(
    id           SERIAL       NOT NULL,
    polls_id     INTEGER      NOT NULL,
    title        VARCHAR(256) NOT NULL,
    is_mandatory BOOLEAN      NOT NULL,
    type         INTEGER      NOT NULL
);
ALTER TABLE questions
    ADD CONSTRAINT pk_questions PRIMARY KEY (id);

CREATE TABLE student_lists
(
    id               SERIAL       NOT NULL,
    poll_creators_id INTEGER      NOT NULL,
    name             VARCHAR(256) NOT NULL
);
ALTER TABLE student_lists
    ADD CONSTRAINT pk_student_lists PRIMARY KEY (id);

CREATE TABLE students
(
    id         INTEGER NOT NULL,
    classes_id INTEGER NOT NULL
);
ALTER TABLE students
    ADD CONSTRAINT pk_students PRIMARY KEY (id);

CREATE TABLE teachers
(
    id        INTEGER NOT NULL,
    is_active BOOLEAN NOT NULL
);
ALTER TABLE teachers
    ADD CONSTRAINT pk_teachers PRIMARY KEY (id);

CREATE TABLE users
(
    id            SERIAL       NOT NULL,
    first_name    VARCHAR(256) NOT NULL,
    last_name     VARCHAR(256) NOT NULL,
    password_hash TEXT         NOT NULL,
    uuid          VARCHAR(25)  NOT NULL
);
ALTER TABLE users
    ADD CONSTRAINT pk_users PRIMARY KEY (id);
ALTER TABLE users
    ADD CONSTRAINT uc_users_uuid UNIQUE (uuid);

CREATE TABLE student_lists_students
(
    student_lists_id INTEGER NOT NULL,
    students_id      INTEGER NOT NULL
);
ALTER TABLE student_lists_students
    ADD CONSTRAINT pk_student_lists_students PRIMARY KEY (student_lists_id, students_id);

ALTER TABLE admins
    ADD CONSTRAINT fk_admins_poll_creators FOREIGN KEY (id) REFERENCES poll_creators (id) ON DELETE CASCADE;

ALTER TABLE answers
    ADD CONSTRAINT fk_answers_questions FOREIGN KEY (id) REFERENCES questions (id) ON DELETE CASCADE;
ALTER TABLE answers
    ADD CONSTRAINT fk_answers_students FOREIGN KEY (students_id) REFERENCES students (id) ON DELETE CASCADE;
ALTER TABLE answers
    ADD CONSTRAINT fk_answers_polls FOREIGN KEY (polls_id) REFERENCES polls (id) ON DELETE CASCADE;

ALTER TABLE close_question_options
    ADD CONSTRAINT fk_close_question_options_quest FOREIGN KEY (id) REFERENCES questions (id) ON DELETE CASCADE;

ALTER TABLE poll_creators
    ADD CONSTRAINT fk_poll_creators_users FOREIGN KEY (id) REFERENCES users (id) ON DELETE CASCADE;

ALTER TABLE poll_templates
    ADD CONSTRAINT fk_poll_templates_poll_creators FOREIGN KEY (poll_creators_id) REFERENCES poll_creators (id) ON DELETE CASCADE;

ALTER TABLE polls
    ADD CONSTRAINT fk_polls_poll_creators FOREIGN KEY (poll_creators_id) REFERENCES poll_creators (id) ON DELETE CASCADE;
ALTER TABLE polls
    ADD CONSTRAINT fk_polls_student_lists FOREIGN KEY (student_lists_id) REFERENCES student_lists (id) ON DELETE CASCADE;

ALTER TABLE questions
    ADD CONSTRAINT fk_questions_polls FOREIGN KEY (polls_id) REFERENCES polls (id) ON DELETE CASCADE;

ALTER TABLE student_lists
    ADD CONSTRAINT fk_student_lists_poll_creators FOREIGN KEY (poll_creators_id) REFERENCES poll_creators (id) ON DELETE CASCADE;

ALTER TABLE students
    ADD CONSTRAINT fk_students_users FOREIGN KEY (id) REFERENCES users (id) ON DELETE CASCADE;
ALTER TABLE students
    ADD CONSTRAINT fk_students_classes FOREIGN KEY (classes_id) REFERENCES classes (id) ON DELETE CASCADE;

ALTER TABLE teachers
    ADD CONSTRAINT fk_teachers_poll_creators FOREIGN KEY (id) REFERENCES poll_creators (id) ON DELETE CASCADE;

ALTER TABLE student_lists_students
    ADD CONSTRAINT fk_student_lists_students_stude FOREIGN KEY (student_lists_id) REFERENCES student_lists (id) ON DELETE CASCADE;
ALTER TABLE student_lists_students
    ADD CONSTRAINT fk_student_lists_students_stu_1 FOREIGN KEY (students_id) REFERENCES students (id) ON DELETE CASCADE;