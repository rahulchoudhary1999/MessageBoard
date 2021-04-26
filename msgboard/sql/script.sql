create table Administrator
(
username char(15) primary key,
password char(100) not null,
password_key char(100) not null
);
create table branch
(
code int primary key auto_increment,
title char(50) not null unique
);
create table semester
(
code int primary key auto_increment,
title char(25) not null unique
);
create table student
(
roll_number char(15) primary key,
first_name char(20) not null,
last_name char(20) not null,
email_id char(100) not null unique,
password char(100) not null,
password_key char(100) not null
);
create table student_semester_mapping
(
roll_number char(15),
semester_code int,
primary key(roll_number,semester_code),
foreign key (roll_number) references student(roll_number),
foreign key (semester_code) references semester(code)
);
create table student_branch_mapping
(
roll_number char(15),
branch_code int,
primary key(roll_number,branch_code),
foreign key (roll_number) references student(roll_number),
foreign key (branch_code) references branch(code)
);