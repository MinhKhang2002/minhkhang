use newservlet;
select count(*) from news;

DELETE FROM user WHERE id = "4";
ALTER TABLE comment AUTO_INCREMENT = 1;


insert into category(code,name) values('giai-tri','Giải trí');

insert into comment(content,user_id,new_id) values('Quá tuyệt vời và bổ ít', 3, 8);

SELECT * FROM comment WHERE new_id = 17;

SELECT u.id, u.content, u.user_id, u.new_id, u.createddate, n.id, r.fullname FROM comment AS u INNER JOIN user AS r ON r.id = u.user_id INNER JOIN news AS n ON n.id = u.new_id WHERE u.new_id = 17;

SELECT * FROM comment AS u INNER JOIN user AS r ON r.id = u.user_id INNER JOIN news AS n ON n.id = u.new_id;
SELECT * FROM comment AS u INNER JOIN user AS r ON r.id = u.user_id;

SELECT * FROM comment order by id DESC limit 3;

SELECT * FROM comment AS u INNER JOIN user AS r ON r.id = u.user_id INNER JOIN news AS n ON n.id = u.new_id order by u.id DESC LIMIT 3;

select count(*) from comment;