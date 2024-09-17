select telephely from osztaly
minus
select distinct telephely from osztaly o, dolgozo d, fiz_kategoria
where o.oazon = d.oazon and d.fizetes <= felso and d.fizetes >= also and kategoria=2;

select kategoria, count(*) dolgozo_Db from osztaly o, dolgozo d, fiz_kategoria
where o.oazon = d.oazon and d.fizetes <= felso and d.fizetes >= also group by kategoria having count(*) = 3;

--orai
CREATE TABLE R(A VARCHAR(10), B INTEGER, C INTEGER);
INSERT INTO R VALUES('X',1, 2);
INSERT INTO R VALUES('Y',2, 3);
INSERT INTO R VALUES('Y',3, 4);
INSERT INTO R VALUES('X',1, 5);
INSERT INTO R VALUES('Y',3, 5);
INSERT INTO R VALUES('X',4, 2);
INSERT INTO R VALUES('X',4, 4);
CREATE TABLE S(C INTEGER, D INTEGER);
INSERT INTO S VALUES(2, 8);
INSERT INTO S VALUES(2, 15);
INSERT INTO S VALUES(3, 9);
INSERT INTO S VALUES(3, 14);
INSERT INTO S VALUES(4, 11);
INSERT INTO S VALUES(4, 17);
INSERT INTO S VALUES(2, 1);
INSERT INTO S VALUES(6, 20);

create table gyak5 as 
select R.A, avg(S.D) as av from R, S
where R.B >= 2 group by R.A;