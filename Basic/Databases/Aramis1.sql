SELECT
    dbms_rowid.rowid_relative_fno(ROWID) fajl, dbms_rowid.rowid_block_number(ROWID) blokk
FROM dba_extents
WHERE segment_name = 'TABLA_123';

SELECT dbms_rowid.rowid_block_number(ROWID) blokk
FROM NIKOVITS.TABLA_123
GROUP BY dbms_rowid.rowid_block_number(ROWID)
HAVING COUNT(*) = 0;

SELECT segment_name,  dbms_rowid.rowid_block_number(ROWID) blokk
FROM dba_extents
WHERE segment_name = 'TABLA_123';

select * from dba_segments join dba_extents on dba_segments.segment_name = dba_extents.segment_name where dba_segments.segment_name = 'TABLA_123' or dba_extents.segment_name = 'TABLA_123';

CREATE OR REPLACE PROCEDURE num_of_rows IS
begin
    for x in 24192..24194
    loop
        dbms_output.put_line('2;' || x || '->0');
    end loop;
    for x in (select dbms_rowid.rowid_relative_fno(ROWID) fajl, dbms_rowid.rowid_block_number(ROWID) blokk, count(*) szam from NIKOVITS.TABLA_123 group by dbms_rowid.rowid_relative_fno(ROWID), dbms_rowid.rowid_block_number(ROWID) order by dbms_rowid.rowid_relative_fno(ROWID), dbms_rowid.rowid_block_number(ROWID))
    LOOP
        if x.blokk = 24196 then
            dbms_output.put_line(x.fajl ||';' || x.blokk || '->' || x.szam);
            dbms_output.put_line('2;24197->0');
        else
            dbms_output.put_line(x.fajl ||';' || x.blokk || '->' || x.szam);
        end if;
    end loop;
    for x in 512..535
    loop
        dbms_output.put_line('7;' || x || '->0');
    end loop;
end;

set serveroutput on
execute num_of_rows();

EXECUTE check_plsql('num_of_rows()');

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

select * from dba_segments where owner = 'NIKOVITS' and segment_name like 'CIKK_IOT_PK'

CREATE OR REPLACE PROCEDURE empty_blocks(p_owner VARCHAR2, p_table VARCHAR2) IS
a1 NUMBER;
a2 NUMBER;
v_statement VARCHAR2(100);
begin
    select (select sum(blocks) from (select blocks from DBA_SEGMENTS where owner = UPPER(p_owner) and segment_name = UPPER(p_table))) into a1 from dual;
    execute immediate 'select count(db) from (SELECT dbms_rowid.rowid_block_number(ROWID) blokk, count(*) db FROM ' || p_owner || '.' || p_table || ' GROUP BY dbms_rowid.rowid_block_number(ROWID))' into a2;
    a1 := a1 - a2;
    dbms_output.put_line('Empty Blocks: ' || TO_CHAR(a1));
end;

set serveroutput on;
EXECUTE check_plsql('empty_blocks(''nikovits'', ''customers'')');
EXECUTE check_plsql('empty_blocks(''nikovits'', ''eladasok'')');


select segment_name, count(distinct file_id) from dba_extents where owner = 'NIKOVITS' and segment_type = 'TABLE' group by segment_name having count(distinct file_id) > 1