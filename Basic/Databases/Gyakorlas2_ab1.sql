create table dolg2 as select * from nikovits.dolgozo;
create table oszt2 as select * from nikovits.osztaly;

drop table oszt2;

--1.
delete dolg2 where jutalek IS NULL;

--2.
delete dolg2 where belepes < '01-JAN-82';

--4.
delete dolg2 where fizetes < (select avg(fizetes) from dolg2);
COMMIT;
delete dolg2 where fizetes < (select avg(fizetes) from dolg2);
ROLLBACK;

--5.
delete dolg2 where fizetes = (select max(fizetes) from dolg2);
ROLLBACK;

--7.
delete oszt2 where 
(select oazon from
(select o.oazon, count(*) as ctr from oszt2 o, (select d.dnev as dn from dolgozo d,fiz_kategoria f where f.kategoria = 2 and d.fizetes < f.felso and d.fizetes > f.also) d2 where o.dnev = d2.dn group by o.oazon)
 where ctr = 2)
 = oszt2.oazon;
ROLLBACK;

--8.
INSERT INTO oszt2 values(1,'KOVACS',NULL,NULL,SYSDATE,(select round(avg(d.fizetes)) from dolg2 d,oszt2 o where d.dkod = o.dkod and o.oazon = 10),NULL,10);
delete oszt2 where dkod = 1;

--9.
update dolg2 set fizetes = fizetes * 1.20 where oazon = 20;

--10.
update dolg2 set fizetes = fizetes + 500 where jutalek IS NULL or fizetes < (select avg(fizetes) from dolg2);

--11.
update dolg2 set jutalek =
(CASE
    WHEN jutalek IS NULL THEN (select max(jutalek) from dolgozo)
    ELSE jutalek + (select max(jutalek) from dolgozo)
END);
ROLLBACK;

--12.
update dolg2 set dnev = 'Loser' where fizetes = (select min(fizetes) from dolg2);

--14.
update dolg2 set fizetes = fizetes + (select min(fizetes) from dolg2) where dnev = ANY(select DISTINCT d1.dnev from dolg2 d1, dolg2 d2 where d1.dkod = d2.fonoke);

--15.
update dolg2 d set d.fizetes = d.fizetes + (select avg(d1.fizetes) from dolg2 d1 where d1.oazon = d.oazon) where dnev NOT IN (select DISTINCT d1.dnev from dolg2 d1, dolg2 d2 where d1.dkod = d2.fonoke);

--16.
ALTER TABLE dolg2 ADD lakhely VARCHAR2(50);
update dolg2 d set d.lakhely = 
(CASE
    WHEN (select d.fonoke from oszt2 o where o.oazon = d.oazon and o.telephely = 'CHICAGO' and d.foglalkozas = 'CLERK') = (select dkod from dolg2 where dnev = 'BLAKE') THEN NULL
    WHEN (select d.fonoke from oszt2 o where o.oazon = d.oazon and o.telephely = 'CHICAGO') = (select dkod from dolg2 where dnev = 'BLAKE') THEN 'INDIANAPOLIS'
    WHEN (select o.telephely from oszt2 o where o.oazon = d.oazon and o.telephely = 'CHICAGO') = 'CHICAGO' THEN 'BOSTON'
    WHEN (select o.telephely from oszt2 o where o.oazon = d.oazon and o.telephely = 'BOSTON') = 'BOSTON' THEN 'CHICAGO'
END);

CREATE OR REPLACE FUNCTION prim(n number) return number IS
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

CREATE OR REPLACE PROCEDURE fib(n number) IS 
    fib_2 number := 1;
    fib_3 number := 1;
    fib number;
begin
    if n = 1 THEN
        dbms_output.put_line(0);
    elsif n <= 3 then
        dbms_output.put_line(fib_2);
    else
        for i in 4..n loop
            fib := fib_2 + fib_3;
            fib_2 := fib_3;
            fib_3 := fib;
        end loop;
        dbms_output.put_line(fib);
    end if;
end;

set serveroutput on;
execute fib(10);

CREATE OR REPLACE FUNCTION lnko(n1 integer, n2 integer) return number IS
oszto number;
begin
    if (n1 > n2) then
        oszto := n2;
    elsif (n2 > n1) then
        oszto := n1;
    else return n1;
    end if;
    
    while oszto != 1 loop
        if(mod(n1,oszto) = 0 and mod(n2,oszto) = 0) then
            return oszto;
        else oszto := oszto - 1;
        end if;
    end loop;
    return oszto;
end;

select lnko(3570,7293) from dual;

CREATE OR REPLACE FUNCTION faktor(n integer) return integer IS
res number := n;
begin
    for i in 1..n-1 loop
        res := res * i;
    end loop;
    return res;
end;

select faktor(10) from dual;

CREATE OR REPLACE FUNCTION hanyszor(str varchar2, p varchar2) return integer IS
res number := 0;
str2 varchar2(10);
i number := 1; 
begin
    while i != length(str) loop
        str2 := substr(str,i,length(p));
        if str2 = p then
            res := res + 1;
        end if;
        i := i + 1;
    end loop;
    return res;
end;
select hanyszor('aaa aa aaa aa aaa baab aaa fd', 'aa') from dual;

--select substr('aaa aa aaa aa aaa baab aaa fd',1,2) from dual;

CREATE OR REPLACE FUNCTION osszeg(str varchar2) return number IS
res number(2) := 0;
str2 varchar2(10);
i number := 1; 
begin
    while i != length(str) loop
        if instr(substr(substr(str,i,length(str)-i+1),i,length(str)-i+1),'+') != 0 then
            str2 := substr(substr(str,i,length(str)-i+1),i,instr(substr(str,i,length(str)-i+1),'+')-1);
        else
            str2 := substr(substr(str,i,length(str)-i+1),i,length(str)-i);
        end if;
        res := res + to_number(str2);
        if instr(substr(substr(str,i,length(str)-i+1),i,length(str)-i+1),'+') != 0 then
            i := i + instr(substr(substr(str,i,length(str)-i+1),i,length(str)-i+1),'+')+1;
        else
            i := length(str);
        end if;
    end loop;
    return res;
end;

select osszeg('1 + 2 + 3 + 4') from dual;

select to_number(substr('1+2+3+4',1,instr('1+2+3+4','+')-1)) from dual;

--select instr('aaa,aa',',') from dual;

CREATE OR REPLACE FUNCTION kat_atlag(n integer) return number IS
v1 dolgozo.fizetes%TYPE;
begin
    select (select avg(d.fizetes) from dolg2 d, fiz_kategoria f where f.kategoria = n and d.fizetes > f.also and d.fizetes < f.felso) into v1 from dual;
    return v1;
end;

select kat_atlag(2) from dual;

CREATE OR REPLACE PROCEDURE nap_atl(nap varchar2) IS
v1 dolgozo.fizetes%TYPE;
v2 number;
begin
    select (select avg(d.fizetes) from dolg2 d where trim(to_char(d.belepes,'Day','nls_date_language=hungarian')) = nap) into v1 from dual;
    select (select Count(*) from dolg2 d where trim(to_char(d.belepes,'Day','nls_date_language=hungarian')) = nap) into v2 from dual;
    dbms_output.put_line('Dolgozók száma: '|| v2 || ', Átlag_fiz: ' || v1 );
end;



