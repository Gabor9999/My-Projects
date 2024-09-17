--gyak4

create table dolgozo as select * from vzoli.dolgozo;

select dbms_rowid.rowid_relative_fno(ROWID), dbms_rowid.rowid_object(ROWID),dbms_rowid.rowid_block_number(ROWID), dbms_rowid.rowid_row_number(ROWID), t.* from dolgozo t where dkod = 7788;
select bytes, blocks from dba_segments where owner = 'VZOLI' and segment_name='DOLGOZO';

select * from DBA_indexes where table_name = 'DOLGOZO';
select * from DBA_IND_COLUMNS where table_name = 'dolgozo';
 SELECT *
  FROM user_ind_columns
 WHERE table_name = 'DOLGOZO'
 ORDER BY index_name

create table customers as select * from NIKOVITS.CUSTOMERS;
create table cikk_iot as select * from NIKOVITS.CIKK_IOT;
CREATE OR REPLACE PROCEDURE list_indexes(p_owner VARCHAR2, p_table VARCHAR2) IS
begin
    for x in (select distinct idx.index_name, seg.bytes from dba_indexes idx, dba_segments seg where idx.table_name = UPPER(p_table) and seg.owner = UPPER(p_owner) and idx.index_name = seg.segment_name order by idx.index_name)
    loop
        dbms_output.put_line(x.index_name || ': ' || x.bytes );
    end loop;
end;
SET SERVEROUTPUT ON
EXECUTE list_indexes('nikovits', 'customers');
EXECUTE list_indexes('nikovits', 'cikk_iot'); 
EXECUTE check_plsql('list_indexes(''nikovits'', ''customers'')');
EXECUTE check_plsql('list_indexes(''nikovits'', ''cikk_iot'')');

EXECUTE check_plsql('list_indexes(''nikovits'', ''customers'')');
create index x on dolgozo(dnev);
EXECUTE list_indexes('vzoli', 'dolgozo');
select * from dba_tables where table_name like 'CIKK_IOT'

select segment_name, partition_name, blocks from DBA_SEGMENTS where owner = 'NIKOVITS' and segment_name = 'CUSTOMERS';



/
CREATE OR REPLACE PROCEDURE empty_blocks(p_owner VARCHAR2, p_table VARCHAR2) IS
a1 NUMBER;
a2 NUMBER;
begin
    select (select sum(blocks) from (select blocks from DBA_SEGMENTS where owner = UPPER(p_owner) and segment_name = UPPER(p_table))) into a1 from dual;
    execute immediate 'select count(db) from (SELECT dbms_rowid.rowid_block_number(ROWID) blokk, count(*) db FROM ' || p_owner || '.' || p_table || ' GROUP BY dbms_rowid.rowid_block_number(ROWID))' into a2;
    a1 := a1 - a2;
    dbms_output.put_line('Empty Blocks: ' || TO_CHAR(a1));
end;
/


set serveroutput on;
EXECUTE empty_blocks('nikovits','customers');
EXECUTE check_plsql('empty_blocks(''nikovits'', ''customers'')');
EXECUTE check_plsql('empty_blocks(''nikovits'', ''eladasok'')');



select count(db) from (SELECT dbms_rowid.rowid_block_number(ROWID) blokk, count(*) db
FROM nikovits.eladasok GROUP BY dbms_rowid.rowid_block_number(ROWID));

select sum(blocks) from (select blocks from DBA_SEGMENTS where owner = UPPER('NIKOVITS') and segment_name = UPPER('ELADASOK'))



