select * from szeret where nev='Micimackó';
select gyumolcs from szeret where nev='Micimackó';

-- 2.
select nev from szeret where gyumolcs='körte'
intersect
select nev from szeret where gyumolcs='alma';

-- 4.
select gyumolcs from szeret
minus
select gyumolcs from szeret where nev='Micimackó';
-- 5.a
select nev from szeret where gyumolcs='körte'
union
select nev from szeret where gyumolcs='dinnye';

select distinct s1.nev from szeret s1,szeret s2
where s1.nev=s2.nev and s1.gyumolcs!=s2.gyumolcs;

select distinct s1.nev from szeret s1,szeret s2,szeret s3
where s1.nev=s2.nev and s2.nev=s3.nev
and s1.gyumolcs!=s2.gyumolcs
and s3.gyumolcs!=s2.gyumolcs
and s1.gyumolcs!=s3.gyumolcs;
-- orai
create table gyak2 as 
(select nev from szeret where gyumolcs='alma'
intersect
select nev from szeret where gyumolcs='körte');

select distinct s1.gyumolcs from szeret s1,szeret s2
where s1.nev!=s2.nev and s1.gyumolcs=s2.gyumolcs;

create table gyak7 as select * from osztaly;

select distinct o.oazon from osztaly o,dolgozo d,fiz_kategoria f where o.oazon = d.oazon and d.fizetes > f.also and d.fizetes < f.felso and f.kategoria = 2;

DELETE from gyak7 where gyak7.oazon in (select distinct o.oazon from osztaly o,dolgozo d,fiz_kategoria f where o.oazon = d.oazon and d.fizetes > f.also and d.fizetes < f.felso and f.kategoria = 2);

commit;

create function isPrime4
(
    n number
) 
return number is
Begin
	if n<= 1 then
        return 0;
    else
        for i in 2..sqrt(n) loop
            if mod(n,i) = 0 then
                return 0;
            end if;
        end loop;
    end if;
    return 1;
End;

create table gyak8 as select dkod, dnev from dolgozo where isPrime4(dkod) = 1;

