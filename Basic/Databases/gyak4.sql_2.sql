
select gyumolcs from szeret minus 
Select distinct gyumolcs from 
(SELECT distinct s1.nev, s2.gyumolcs from szeret s1, szeret s2
minus
select * from szeret)
;

select * from dolgozo d1, dolgozo d2
where d1.fonoke=d2.dkod and d1.fizetes>d2.fizetes;

--orai 
create table gyak4 as
(select o.oazon as Oazon, o.telephely as Telephely, round(avg(d.fizetes)) as AtlagFiz From dolgozo d Join osztaly o on d.oazon=o.oazon group by o.oazon, o.telephely);
--(select * From dolgozo d Join osztaly o on d.oazon=o.oazon);

create table gyak7 as select * from osztaly;

DELETE from gyak7 where gyak7.oazon in (select distinct o.oazon from osztaly o,dolgozo d,fiz_kategoria f where o.oazon = d.oazon and d.fizetes > f.also and d.fizetes < f.felso and f.kategoria = 2);

commit;