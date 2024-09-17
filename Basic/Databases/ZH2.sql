--2.)
explain plan set statement_id = 'zh1' for
select /*+ use_nl(c nikovits.szallit nikovits.projekt) index(c)*/ sum(mennyiseg) from nikovits.cikk c where c.szin = 'fekete' and 
exists (select /*+ full(nikovits.szallit)*/ ckod from nikovits.szallit) and
exists (select /*+ full(nikovits.projekt)*/ pkod from nikovits.projekt where helyszin = 'Pecs');
select plan_table_output from table(dbms_xplan.display('plan_table','zh1', 'all'));
--3.)

select count(*) from nikovits.cikkzh 
