drop table dolgozo;
create table dolgozo as select * from nikovits.dolgozo
create table osztaly as select * from nikovits.osztaly;
create table fiz_kategoria as select * from nikovits.fiz_kategoria

select distinct onev from osztaly o, dolgozo d, fiz_kategoria f where o.oazon = d.oazon and d.fizetes > f.also and d.fizetes < f.felso and f.kategoria = 1;

create index ind on dolgozo(fizetes);

explain plan for select distinct onev from osztaly o, dolgozo d, fiz_kategoria f where o.oazon = d.oazon and d.fizetes > f.also and d.fizetes < f.felso and f.kategoria = 1;

select * from table(dbms_xplan.display);

create table dept as select * from vzoli.dept;
create table emp as select * from vzoli.emp;

drop table emp;
drop table dept;

EXPLAIN PLAN SET statement_id = 'ut1' 
for
SELECT /*+ full(emp) */ deptno FROM dept MINUS SELECT /*+ full(emp) */ deptno FROM emp;

select plan_table_output from table(dbms_xplan.display('plan_table', 'ut1', 'all'));

EXPLAIN PLAN SET statement_id = 's1' for SELECT ckod
from nikovits.szallit sz, nikovits.szallito szto, nikovits.projekt p
where sz.pkod = p.pkod and sz.szkod = szto.szkod and szto.szkod = 15 and helyszin = 'Szeged' and telephely = 'Pecs'
group by ckod having ckod<100;
select plan_table_output from table(dbms_xplan.display('plan_table', 's1', 'all'));
