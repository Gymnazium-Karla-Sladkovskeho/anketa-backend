-- Remove existing tables cleanly
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

-- 1. Independent Tables
CREATE TABLE classes (
                         id SERIAL PRIMARY KEY,
                         name VARCHAR(256) NOT NULL
);

CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       first_name VARCHAR(256) NOT NULL,
                       last_name VARCHAR(256) NOT NULL,
                       password_hash TEXT NOT NULL
);

-- 2. 1-to-1 User Extensions (Exposed supports these natively via IdTable<Int>)
CREATE TABLE poll_creators (
                               id INTEGER PRIMARY KEY REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE admins (
                        id INTEGER PRIMARY KEY REFERENCES poll_creators (id) ON DELETE CASCADE
);

CREATE TABLE teachers (
                          id INTEGER PRIMARY KEY REFERENCES poll_creators (id) ON DELETE CASCADE,
                          is_active BOOLEAN NOT NULL
);

-- 3. Core Entities
-- Note: 'id' is now a surrogate PK. The original 'id' column was renamed to 'classes_id' for clarity.
CREATE TABLE students (
                          id SERIAL PRIMARY KEY,
                          classes_id INTEGER NOT NULL REFERENCES classes (id) ON DELETE CASCADE,
                          users_id INTEGER NOT NULL UNIQUE REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE poll_templates (
                                id SERIAL PRIMARY KEY,
                                poll_creators_id INTEGER NOT NULL REFERENCES poll_creators (id) ON DELETE CASCADE,
                                name VARCHAR(256) NOT NULL,
                                is_global BOOLEAN NOT NULL
);

CREATE TABLE student_lists (
                               id SERIAL PRIMARY KEY,
                               poll_creators_id INTEGER NOT NULL REFERENCES poll_creators (id) ON DELETE CASCADE,
                               name VARCHAR(256)
);

-- 4. Junction Tables
-- Note: users_id was removed as students_id already uniquely identifies the user.
CREATE TABLE student_lists_students (
                                        id SERIAL PRIMARY KEY,
                                        student_lists_id INTEGER NOT NULL REFERENCES student_lists (id) ON DELETE CASCADE,
                                        students_id INTEGER NOT NULL REFERENCES students (id) ON DELETE CASCADE,
                                        UNIQUE (student_lists_id, students_id)
);

-- 5. Polls & Questions
CREATE TABLE polls (
                       id SERIAL PRIMARY KEY,
                       poll_creators_id INTEGER NOT NULL REFERENCES poll_creators (id) ON DELETE CASCADE,
                       student_lists_id INTEGER NOT NULL REFERENCES student_lists (id) ON DELETE CASCADE,
                       name VARCHAR(256) NOT NULL,
                       start_date DATE NOT NULL,
                       end_date DATE NOT NULL
);

CREATE TABLE questions (
                           id SERIAL PRIMARY KEY,
                           polls_id INTEGER NOT NULL REFERENCES polls (id) ON DELETE CASCADE,
                           title VARCHAR(256) NOT NULL,
                           is_mandatory BOOLEAN NOT NULL,
                           type INTEGER NOT NULL
);

-- 6. Answers & Options
-- Note: The original 'id' column acted as questions_id. It is now explicitly questions_id.
CREATE TABLE answers (
                         id SERIAL PRIMARY KEY,
                         students_id INTEGER NOT NULL REFERENCES students (id) ON DELETE CASCADE,
                         questions_id INTEGER NOT NULL REFERENCES questions (id) ON DELETE CASCADE,
                         polls_id INTEGER NOT NULL REFERENCES polls (id) ON DELETE CASCADE,
                         answer TEXT NOT NULL,
                         UNIQUE (students_id, polls_id, questions_id)
);

-- Note: 'text' typo fixed to VARCHAR. The original 'id' column is now explicitly questions_id.
CREATE TABLE close_question_options (
                                        id SERIAL PRIMARY KEY,
                                        questions_id INTEGER NOT NULL REFERENCES questions (id) ON DELETE CASCADE,
                                        option_text VARCHAR(256) NOT NULL
);