/*SELECT STATEMENT +  + 
  FILTER +  + 
    HASH + GROUP BY + 
      HASH JOIN +  + 
        NESTED LOOPS +  + 
          TABLE ACCESS + BY INDEX ROWID + NIKOVITS.SZALLITO
            INDEX + UNIQUE SCAN + NIKOVITS.SZO_SZKOD
          TABLE ACCESS + FULL + NIKOVITS.PROJEKT
        TABLE ACCESS + FULL + NIKOVITS.SZALLIT*/

EXPLAIN PLAN SET statement_id = 'a1'
for
select /*+ full(sz) */ sum(mennyiseg), ckod
from nikovits.szallit sz, nikovits.szallito szto, nikovits.projekt p
where sz.pkod=p.pkod and sz.szkod=szto.szkod and szto.szkod=15
and helyszin='Szeged' and telephely='Pecs'
group by ckod having ckod>100;
select plan_table_output from table(dbms_xplan.display('plan_table','a1', 'all'));

/*SELECT STATEMENT +  + 
  SORT + AGGREGATE + 
    HASH JOIN + SEMI + 
      TABLE ACCESS + FULL + NIKOVITS.SZALLIT
      TABLE ACCESS + FULL + NIKOVITS.CIKK*/

explain plan set statement_id = 'a2' 
for
select /*+ leading(sz) */ sum(mennyiseg)
from nikovits.szallit sz
where exists
  (select /*+ hash_sj */ ckod from nikovits.cikk c where c.ckod=sz.ckod and szin='piros');
select plan_table_output from table(dbms_xplan.display('plan_table','a2', 'all'));


/*SELECT STATEMENT +  + 
  SORT + AGGREGATE + 
    TABLE ACCESS + FULL + NIKOVITS.CIKK*/

explain plan set statement_id = 'a3'
for
select /*+ full(c) */ count(*) from nikovits.cikk c;
select plan_table_output from table(dbms_xplan.display('plan_table','a3', 'all'));

/*SELECT STATEMENT +  + 
  SORT + AGGREGATE + 
    TABLE ACCESS + BY INDEX ROWID + NIKOVITS.CIKK
      INDEX + UNIQUE SCAN + NIKOVITS.C_CKOD*/

explain plan set statement_id = 'a4'
for
select /*+ index(c) */ count(suly) from nikovits.cikk c where ckod = 1;
select plan_table_output from table(dbms_xplan.display('plan_table','a4', 'all'));

/*SELECT STATEMENT +  + 
  SORT + AGGREGATE + 
    HASH JOIN +  + 
      TABLE ACCESS + FULL + NIKOVITS.PROJEKT
      TABLE ACCESS + FULL + NIKOVITS.SZALLIT*/

explain plan set statement_id = 'a5'
for
select /*+ full(p) */ sum(mennyiseg)
from nikovits.szallit sz natural join nikovits.projekt p 
where helyszin='Szeged';
select plan_table_output from table(dbms_xplan.display('plan_table','a5', 'all'));

/*SELECT STATEMENT +  + 
  HASH + GROUP BY + 
    HASH JOIN +  + 
      TABLE ACCESS + FULL + NIKOVITS.PROJEKT
      TABLE ACCESS + FULL + NIKOVITS.SZALLIT*/

explain plan set statement_id = 'a6'
for
select /*+ full(p) */ mennyiseg
from nikovits.szallit sz natural join nikovits.projekt p 
where helyszin='Szeged'
group by mennyiseg;
select plan_table_output from table(dbms_xplan.display('plan_table','a6', 'all'));

/*SELECT STATEMENT +  + 
  SORT + AGGREGATE + 
    MERGE JOIN +  + 
      SORT + JOIN + 
        TABLE ACCESS + BY INDEX ROWID BATCHED + NIKOVITS.CIKK
          INDEX + RANGE SCAN + NIKOVITS.C_SZIN
      SORT + JOIN + 
        TABLE ACCESS + FULL + NIKOVITS.SZALLIT*/
        
explain plan set statement_id = 'a7'
for
select /*+ use_merge(sz c) index(c)*/ sum(mennyiseg)
from nikovits.szallit sz natural join nikovits.cikk c 
where szin = 'piros';
select plan_table_output from table(dbms_xplan.display('plan_table','a7', 'all'));

select * from nikovits.szallit;