use newservlet;

insert into category(code,name) values('the-thao','Thể thao');
insert into category(code,name) values('the-gioi','Thế giới');
insert into category(code,name) values('chinh-tri','Chính trị');
insert into category(code,name) values('thoi-su','Thời sự');
insert into category(code,name) values('goc-nhin','Góc nhìn');

DELETE FROM category
WHERE id = "11";
ALTER TABLE category AUTO_INCREMENT = 1;

insert into news(title,categoryid) values('Bài viết 1', 1);
insert into news(title,categoryid) values('Bài viết 2', 1);
insert into news(title,categoryid) values('Bài viết 3', 2);