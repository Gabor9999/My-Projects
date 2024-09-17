--1.1

select dnev from dolgozo where fizetes > 2800; --? dnev (? fizetes > 2800 (Dolgozo))

--1.2

select dnev from dolgozo where oazon = 10 or oazon = 20; --? dnev (? oazon = 10 ? oazon = 20 (Dolgozo))

--1.9

select dnev from dolgozo where fonoke is null; -- ? dnev (? fonoke = null (Dolgozo))

--1.6

select distinct foglalkozas from dolgozo; --distinct(? foglalkozas (Dolgozo))

--1.10

select dnev from dolgozo where dnev like '%A%';

--1.11

select dnev from dolgozo where dnev like '%L%L%';

--1.12

select dnev from dolgozo where fizetes > 2000 and fizetes < 3000; --? dnev (? fizetes > 2000 ? fizetes < 3000 (Dolgozo))

--1.13

select dnev from dolgozo order by fizetes asc; --? dnev (? fizetes asc (Dolgozo))

--1.14

select dnev from dolgozo order by fizetes desc, dnev asc; --? dnev (? fizetes desc , dnev asc (Dolgozo))

--1.15

select d1.* from dolgozo d1, dolgozo d2 where d2.dnev = 'KING' and d1.fonoke = d2.dkod; --? D2.dnev = 'KING' ? D1.fonoke = D2.dkod ((? D1 (Dolgozo)) x (? D2 (Dolgozo)))

--2.17

select d1.dnev from dolgozo d1, dolgozo d2 where d1.foglalkozas != 'MANAGER' and d2.fonoke = d1.dkod; --? D1.dnev (? D1.foglalkozas ? 'MANAGER' ? (D2.fonoke = D1.dkod) ((? D1 (Dolgozo)) x (? D2 (Dolgozo))))

--2.18

select d1.* from dolgozo d1, dolgozo d2 where d1.fizetes > d2.fizetes and d1.fonoke = d2.dkod;

--2.20

select d1.dnev from dolgozo d1, osztaly d2 where d1.oazon = d2.oazon and (d2.telephely = 'DALLAS' or d2.telephely = 'CHICAGO');

--2.21

select dnev from dolgozo
MINUS
select d1.dnev from dolgozo d1, osztaly d2 where d1.oazon = d2.oazon and (d2.telephely = 'DALLAS' or d2.telephely = 'CHICAGO'); --? dnev (Dolgozo) - ? D1.dnev (? D1.oazon = D2.oazon ? (D2.telephely = 'DALLAS' ? D2.telephely = 'CHICAGO') ((? D1 (Dolgozo)) x (? D2 (Osztaly))))

--2.23

select onev from osztaly
MINUS
select distinct d1.onev from osztaly d1,dolgozo d2 where d1.oazon = d2.oazon;

--2.28

select dnev from dolgozo,(select max(fizetes) as Max_Fiz from dolgozo) where fizetes = Max_Fiz; 

-- 3.1

select dnev from dolgozo where mod(fizetes, 15) = 0; 

--3.2

select dnev from dolgozo where belepes < '01-JAN-1982';

--3.3

select dnev from dolgozo where substr(dnev,2,1) = 'A';

--3.4

select dnev from dolgozo where instr(substr(dnev,instr(dnev,'L')+1,length(dnev)),'L') != 0;

--3.9

select dnev from dolgozo where trim(to_char(belepes, 'Day')) = 'Tuesday';

--3.11

select dnev from dolgozo, fiz_kategoria where kategoria = 1 and fizetes < felso and fizetes > also;

--3.12

select dnev from dolgozo, fiz_kategoria where mod(kategoria,2) = 0 and fizetes < felso and fizetes > also;

--3.13

select abs(d1.belepes - d2.belepes) as diff from dolgozo d1, dolgozo d2 where d1.dnev = 'KING' and d2.dnev = 'JONES';

--3.14

select to_char(last_day(belepes),'Day') from dolgozo where dnev = 'KING';

--3.15

select to_char(trunc(belepes,'Month'),'Day') from dolgozo where dnev = 'KING';

--3.16

select d.dnev from dolgozo d,osztaly o ,fiz_kategoria f where d.oazon = o.oazon and instr(onev,'C') != 0 and f.kategoria >= 4 and d.fizetes > f.also and d.fizetes > f.felso;

--4.1

select max(fizetes) from dolgozo;

--4.2

select sum(fizetes) from dolgozo;

--4.3

select avg(d.fizetes) as Atlag, sum(d.fizetes) as Ossz from dolgozo d, osztaly o where d.oazon = o.oazon and d.oazon = '20';

--4.5

select count(dnev) from dolgozo where fizetes > 2000;

--4.6

select o.oazon, avg(d.fizetes) from dolgozo d, osztaly o where d.oazon = o.oazon group by o.oazon;

--4.8

select o.oazon, count(d.fizetes) from dolgozo d, osztaly o where d.oazon = o.oazon group by o.oazon;

--4.9

select Azon, Atlag from (select o.oazon as Azon, avg(d.fizetes) as Atlag from dolgozo d, osztaly o where d.oazon = o.oazon group by o.oazon) where Atlag > 2000;

--4.10

select Azon, Atlag from (select o.oazon as Azon, avg(d.fizetes) as Atlag, count(d.fizetes) as Dolg from dolgozo d, osztaly o where d.oazon = o.oazon group by o.oazon) where Dolg >= 4;

--4.12

select Nev, Telephely from (select o.onev as Nev, avg(d.fizetes) as Atlag, o.telephely as Telephely from dolgozo d, osztaly o where d.oazon = o.oazon group by o.onev,o.telephely) where Atlag > 2000;

--4.13

select kategoria from (select kategoria, count(d.dnev) as Szam from dolgozo d, fiz_kategoria f where (f.felso > d.fizetes and f.also < d.fizetes) group by kategoria) where Szam = 3;

--4.14

select kategoria, count(distinct d.oazon) as Szam from dolgozo d, fiz_kategoria f where (f.felso > d.fizetes and f.also < d.fizetes) group by kategoria;

--4.15

select o.oazon, count(d.dnev) as Szam from dolgozo d, fiz_kategoria f, osztaly o where (f.felso > d.fizetes and f.also < d.fizetes and kategoria = 1 and d.oazon = o.oazon) group by o.oazon;


















