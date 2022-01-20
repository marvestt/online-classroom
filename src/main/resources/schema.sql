
CREATE TABLE IF NOT EXISTS public.users(
	user_id SERIAL UNIQUE,
	username VARCHAR(120) NOT NULL, 
    password VARCHAR(120) NOT NULL,
    first_name VARCHAR(120) NOT NULL,
    surname VARCHAR(120) NOT NULL,
    PRIMARY KEY(user_id)
);

CREATE TABLE IF NOT EXISTS public.students(
	student_id SERIAL UNIQUE,
    user_id BIGINT NOT NULL,
	goals VARCHAR(120) NOT NULL, 
    description VARCHAR(120) NOT NULL,
    PRIMARY KEY(student_id),
    FOREIGN KEY(user_id) REFERENCES users(user_id) ON DELETE CASCADE
); 

CREATE TABLE IF NOT EXISTS public.teachers(
	teacher_id SERIAL UNIQUE,
    user_id BIGINT NOT NULL,
	professional_name VARCHAR(120), 
    description VARCHAR(120) NOT NULL, 
    PRIMARY KEY(teacher_id),
    FOREIGN KEY(user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS public.classes(
	class_id SERIAL UNIQUE,
	main_teacher_id BIGINT NOT NULL, 
	name  VARCHAR(120) NOT NULL,
    description VARCHAR(120) NOT NULL,
    PRIMARY KEY(class_id),
    FOREIGN KEY(main_teacher_id) REFERENCES teachers(teacher_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS public.class_members(
	user_id BIGINT NOT NULL,
	class_id BIGINT NOT NULL,
	PRIMARY KEY(user_id,class_id),
	FOREIGN KEY(user_id) REFERENCES users(user_id) ON DELETE CASCADE, 
	FOREIGN KEY(class_id) REFERENCES classes(class_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS public.assignments(
	assignment_id SERIAL UNIQUE,
	class_id BIGINT NOT NULL, 
	title VARCHAR(120) NOT NULL,
    description VARCHAR(2500) NOT NULL,
    PRIMARY KEY(assignment_id),
    FOREIGN KEY(class_id) REFERENCES classes(class_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS public.lessons(
	lesson_id SERIAL UNIQUE,
	class_id BIGINT NOT NULL, 
	title VARCHAR(120) NOT NULL,
    text VARCHAR(2500) NOT NULL,
    PRIMARY KEY(lesson_id),
    FOREIGN KEY(class_id) REFERENCES classes(class_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS public.announcements(
	announcement_id SERIAL UNIQUE,
	class_id BIGINT NOT NULL, 
	title VARCHAR(120) NOT NULL,
    text VARCHAR(2500) NOT NULL,
    PRIMARY KEY(announcement_id),
    FOREIGN KEY(class_id) REFERENCES classes(class_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS public.submissions(
	submission_id SERIAL UNIQUE,
	assignment_id BIGINT NOT NULL, 
	student_id BIGINT NOT NULL,
    title VARCHAR(120) NOT NULL,
    text VARCHAR(2500) NOT NULL,
    PRIMARY KEY(submission_id),
    FOREIGN KEY(assignment_id) REFERENCES assignments(assignment_id) ON DELETE CASCADE,
    FOREIGN KEY(student_id) REFERENCES students(student_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS public.assignment_grades(
	assignment_grade_id SERIAL UNIQUE,
	assignment_id BIGINT NOT NULL, 
	student_id BIGINT NOT NULL,
    grade VARCHAR(120) NOT NULL,
    PRIMARY KEY(assignment_grade_id),
    FOREIGN KEY(assignment_id) REFERENCES assignments(assignment_id) ON DELETE CASCADE,
    FOREIGN KEY(student_id) REFERENCES students(student_id) ON DELETE CASCADE
);