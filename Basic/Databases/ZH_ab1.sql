--1.feladat

select distinct gyumolcs from szeret, (select nev as Nevek from szeret where gyumolcs = 'alma')
where nev = Nevek and gyumolcs != 'alma';
;

SELECT USER YQE5B1, NAME SZERET, TO_CHAR(SYSDATE,'YYYY.MM.DD HH24:MI:SS') AS "Futtatás id?pontja" FROM V$DATABASE;

--2.feladat

select distinct nev from szeret, (select nev as Nevek from szeret where gyumolcs = 'alma')
where nev = Nevek and gyumolcs != 'alma';

SELECT USER YQE5B1, NAME SZERET, TO_CHAR(SYSDATE,'YYYY.MM.DD HH24:MI:SS') AS "Futtatás id?pontja" FROM V$DATABASE;

--3.feladat

select gyumolcs from (select gyumolcs, count(gyumolcs) as Szam from szeret group by gyumolcs) where Szam >= 2 ;

SELECT USER YQE5B1, NAME SZERET, TO_CHAR(SYSDATE,'YYYY.MM.DD HH24:MI:SS') AS "Futtatás id?pontja" FROM V$DATABASE;

--

select Munkas, d3.dnev as Nagyfonok_nev from Dolgozo d3,(select d1.dnev as Dolgozo_nev, d2.dnev as Kisfonok, d1.fonoke as Munkas_fonoke, d2.fonoke as Kisfonok_fonoke from Dolgozo d1, Dolgozo d2 where d1.fonoke = d2.dkod)
where d3.dkod = Kisfonok_fonoke and Munkas like '%A%A%' and d3.dnev like '%A%A%';

SELECT USER YQE5B1, NAME DOLGOZO, TO_CHAR(SYSDATE,'YYYY.MM.DD HH24:MI:SS') AS "Futtatás id?pontja" FROM V$DATABASE;

create table szallit as select * from nikovits.szallit;
create table szallito as select * from nikovits.szallito;
create table cikk as select * from nikovits.cikk;
create table projekt as select * from nikovits.projekt;

--

select distinct sz1.szkod, sz1.sznev from Szallito sz1, Szallit sz2, Cikk c where sz1.szkod = sz2.szkod and sz2.ckod = c.ckod and c.szin != 'zold';

SELECT USER YQE5B1, NAME SZALLITO, TO_CHAR(SYSDATE,'YYYY.MM.DD HH24:MI:SS') AS "Futtatás id?pontja" FROM V$DATABASE;

--

select sz1.sznev from  Projekt p, Szallito sz1, Szallit sz2, Cikk c where sz1.szkod = sz2.szkod and sz2.ckod = c.ckod and c.szin = 'zold' and sz2.pkod = p.pkod group by sz1.sznev;

SELECT USER YQE5B1, NAME SZALLITO, TO_CHAR(SYSDATE,'YYYY.MM.DD HH24:MI:SS') AS "Futtatás id?pontja" FROM V$DATABASE;

