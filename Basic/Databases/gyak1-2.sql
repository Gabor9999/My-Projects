--Gyak1-2
--1.feladat
select owner, object_name, object_type from DBA_OBJECTS where object_name='DBA_TABLES' and object_type = 'VIEW';
select owner, object_name, object_type from DBA_OBJECTS where object_name='DUAL' and object_type = 'TABLE';
--2.feladat
select owner from DBA_SYNONYMS where table_name='DUAL';
select owner from DBA_SYNONYMS where table_name='DBA_TABLES';
--3.feladat
select distinct object_type from DBA_OBJECTS where owner='ORAUSER';
--4.feladat
select count(distinct object_type) from DBA_OBJECTS;
--5.feladat
select distinct object_type from DBA_OBJECTS;
--6.feladat

--ÓRAI
create table gyak01 as select table_name from DBA_TABLES where owner='NIKOVITS' and table_name like '%B%';
drop table gyak01

CREATE OR REPLACE PROCEDURE newest_table(p_user VARCHAR2) IS
res3 dba_objects.created%TYPE;
res1 dba_tables.table_name%TYPE;
begin
    select(select created from dba_objects where owner = upper(p_user) and object_type='TABLE' order by created desc fetch first 1 ROWS ONLY) into res3 from dual;
    select(select object_name from dba_objects where owner = upper(p_user) and object_type='TABLE' order by created desc fetch first 1 ROWS ONLY) into res1 from dual;
    dbms_output.put_line('Tabla neve: ' || res1 || 'Letrehozas ideje: ' || res3);
end;

SET SERVEROUTPUT ON
EXECUTE newest_table('yqe5b1');


EXECUTE check_plsql('newest_table(''nikovits'')');