CREATE TABLE vagyonok as select * from nikovits.vagyonok;

CREATE OR REPLACE FUNCTION dif(p VARCHAR2) return number is
max_f number;
min_f number;
begin
     select (select max(d.fizetes) from osztaly o, dolgozo d where o.telephely = p and o.oazon = d.oazon) into max_f from dual;
     select (select min(d.fizetes) from osztaly o, dolgozo d where o.telephely = p and o.oazon = d.oazon) into min_f from dual;
     return max_f - min_f;
end;

select dif('CHICAGO') from dual;

set serveroutput on
DECLARE                             
  v number;
BEGIN
  v := dif('CHICAGO');
  dbms_output.put_line('Különbség: ' || TO_CHAR(v));
END;

CREATE TABLE DOLGOZO_D1 as SELECT * from dolgozo;

CREATE OR REPLACE FUNCTION get_foglalkozas(o_nev varchar2) RETURN varchar2 IS res varchar2(100);
BEGIN
  FOR rec IN (SELECT DISTINCT d.foglalkozas FROM osztaly o, (select * from dolgozo order by dnev asc) d
    WHERE o.oazon = d.oazon AND o.onev = o_nev) LOOP
    if (res IS NOT NULL) then
        res := rec.foglalkozas || '-' || res;
    else
        res := rec.foglalkozas || res;
    end if;
  END LOOP;
  return(res);
END;

select get_foglalkozas('ACCOUNTING') from dual;
set serveroutput on
begin
    dbms_output.put_line(get_foglalkozas('ACCOUNTING'));
end;


CREATE OR REPLACE PROCEDURE felmeno(neve varchar2) IS
TYPE leszarm IS TABLE OF vagyonok.nev%TYPE INDEX BY binary_integer;
vege number := 0;
begin
    while(vege != 1) loop
        if(select v1.nev from vagyonok where v1.apja = neve)
    end loop;
end;

set serveroutput on
execute felmeno('ABEL');

CREATE TABLE DOLGOZO_MC2 as SELECT * from dolgozo;

CREATE OR REPLACE PROCEDURE idk() IS
avg number;
begin
    select (select avg(fizetes) as av from dolgozo) into avg;
    dbms_output.put_line((delete DOLGOZO_D1 d where d.fizetes < avg)%ROWCOUNT);
end;

execute idk() from dual;

CREATE OR REPLACE PROCEDURE p_novel() IS
CURSOR c1 IS select * from DOLGOZO_MC2 for update of DOLGOZO_MC2;
begin
    for rec in c1 loop
        update DOLGOZO_MC2 set
        case
        WHEN jutalek IS NULL THEN jutalek := 0 + ctr_letters((select o.telephely from osztaly o, DOLGOZO_MC2 d where o.oazon = d.oazon and rec.dnev = d.dnev),'A','O') where current of c1;
        else jutalek := jutalek + ctr_letters((select o.telephely from osztaly o, DOLGOZO_MC2 d where o.oazon = d.oazon and rec.dnev = d.dnev),'A','O') where current of c1;   
    end loop;
end;

CREATE OR REPLACE FUNCTION ctr_letters(param varchar2,l1 varchar2,l2 varchar2) return number is
res number := 0;
param1 varchar2(100) := param;
param2 varchar2(100) := param;
begin
    while(instr(param1,l1) != 0) loop
        res := res + 1;
        param1 := substr(param1,instr(param1,l1)+1,length(param1)-instr(param1,l1));
    end loop;
    while(instr(param2,l2) != 0) loop
        res := res + 1;
        param2 := substr(param2,instr(param2,l2)+1,length(param2)-instr(param2,l2));
    end loop;
    return res;
end;

select ctr_letters('CHICAGO','A','O') from dual;


select substr('CHICAGO',instr('CHICAGO','A')+1,length('CHICAGO') - instr('CHICAGO','A')) from dual;
