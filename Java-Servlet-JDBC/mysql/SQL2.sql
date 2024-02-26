use newservlet;
SELECT * FROM user AS u INNER JOIN role AS r ON r.id = u.roleid WHERE username = 'admin' AND password = 123456 AND status = 1;
select * from user;
SELECT * FROM user AS u INNER JOIN role AS r ON r.id = u.roleid;

SELECT
    DATE(`createddate`) AS 'date_news', 
	COUNT(*) AS 'number_of_news'
FROM `news`
GROUP BY DATE(`createddate`);

SELECT
    DATE(`createddate`) AS 'date', 
	COUNT(*) AS 'number_of_comment'
FROM `comment`
GROUP BY DATE(`createddate`);

SELECT DISTINCT DATE(`createddate`) AS 'date'
FROM `comment`;

SELECT
    DATE(`createddate`) AS 'date',
    COUNT(*) AS 'number_of_comment',
    0 AS 'number_of_news'
FROM
    `comment`
GROUP BY
    DATE(`createddate`)

UNION

SELECT
    DATE(`createddate`) AS 'date',
    0 AS 'number_of_comment',
    COUNT(*) AS 'number_of_news'
FROM
    `news`
GROUP BY
    DATE(`createddate`);
    
SELECT
    `date`,
    SUM(`number_of_comment`) AS 'number_of_comment',
    SUM(`number_of_news`) AS 'number_of_news'
FROM (
    SELECT
        DATE(`createddate`) AS 'date',
        COUNT(*) AS 'number_of_comment',
        0 AS 'number_of_news'
    FROM
        `comment`
    GROUP BY
        DATE(`createddate`)

    UNION

    SELECT
        DATE(`createddate`) AS 'date',
        0 AS 'number_of_comment',
        COUNT(*) AS 'number_of_news'
    FROM
        `news`
    GROUP BY
        DATE(`createddate`)
) AS subquery
GROUP BY
    `date`;

------------

SELECT `date`, SUM(`number_of_comment`) AS 'number_of_comment', SUM(`number_of_news`) AS 'number_of_news'
FROM (SELECT DATE(`createddate`) AS 'date', COUNT(*) AS 'number_of_comment', 0 AS 'number_of_news'
    FROM `comment` GROUP BY DATE(`createddate`)
    UNION
    SELECT DATE(`createddate`) AS 'date', 0 AS 'number_of_comment', COUNT(*) AS 'number_of_news'
    FROM `news` GROUP BY DATE(`createddate`)
) AS subquery GROUP BY `date` ORDER BY `date` ASC;