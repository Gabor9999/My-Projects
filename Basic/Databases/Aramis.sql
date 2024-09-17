CREATE TABLE szeret AS SELECT * FROM nikovits.szeret;
CREATE TABLE dolgozo AS SELECT * FROM nikovits.dolgozo;
CREATE TABLE osztaly AS SELECT * FROM nikovits.osztaly;
CREATE TABLE employees AS SELECT * FROM nikovits.employees;
CREATE TABLE departments AS SELECT * FROM nikovits.departments;

create table gyak2 as 
(select nev from szeret where gyumolcs='alma'
intersect
select nev from szeret where gyumolcs='körte');

select * from osztaly o, dolgozo d, fiz_kategoria
where o.oazon = d.oazon and d.fizetes <= felso and d.fizetes >= also group by kategoria;

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

