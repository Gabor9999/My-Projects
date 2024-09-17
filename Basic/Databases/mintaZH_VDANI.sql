--5.)
--a)
create table dolgozo_zh as select * from vdani.dolgozo_zh;
--b)
create index ind_dzh_nev on dolgozo_zh(dnev);
--c)
select * from dolgozo_zh where dnev = 'KING';
EXPLAIN PLAN SET statement_id = 'ut1' 
for
select * from dolgozo_zh where dnev = 'KING';
select plan_table_output from table(dbms_xplan.display('plan_table', 'ut1', 'all'));

--6.)

create table osztaly_zh as select * from vdani.osztaly_zh;
--a)
EXPLAIN PLAN SET statement_id = 'ut2' 
for
select /*+ ordered */ dnev from dolgozo_zh natural join osztaly_zh where fizetes > 2000 and telephely = 'NEW YORK' and oazon = oazon;
select plan_table_output from table(dbms_xplan.display('plan_table', 'ut2', 'all'));
--b)
EXPLAIN PLAN SET statement_id = 'ut3' 
for
select /*+ use_nl(dolgozo_zh osztaly_zh) */ dnev from dolgozo_zh natural join osztaly_zh where fizetes > 2000 and telephely = 'NEW YORK' and oazon = oazon;
select plan_table_output from table(dbms_xplan.display('plan_table', 'ut3', 'all'));
--c)
EXPLAIN PLAN SET statement_id = 'ut4' 
for
select /*+ use_merge(dolgozo_zh osztaly_zh) */ dnev from dolgozo_zh natural join osztaly_zh where fizetes > 2000 and telephely = 'NEW YORK' and oazon = oazon;
select plan_table_output from table(dbms_xplan.display('plan_table', 'ut4', 'all'));


--7.)
--a)
EXPLAIN PLAN SET statement_id = 'ut5' 
for
select /*+ full(z) */ *
from dolgozo_zh z
union
select /*+ full(z) */ *
from dolgozo_zh z;
select plan_table_output from table(dbms_xplan.display('plan_table', 'ut5', 'all'));
--b)
EXPLAIN PLAN SET statement_id = 'ut6' 
for
select /*+ use_hash(d o) */ d.oazon
from dolgozo_zh d left outer join osztaly_zh o
on d.oazon = o.oazon
group by d.oazon;
select plan_table_output from table(dbms_xplan.display('plan_table', 'ut6', 'all'));
