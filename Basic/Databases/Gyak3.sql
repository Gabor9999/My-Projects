--Órai
CREATE OR REPLACE PROCEDURE num_of_rows IS
begin
    --for x in (select file_id, block_id, dbms_rowid.rowid_block_number(ROWID) fajl from DBA_EXTENTS e where e.OWNER = 'NIKOVITS' and e.SEGMENT_NAME = 'TABLA_123' order by file_id)
    for x in (select dbms_rowid.rowid_relative_fno(ROWID) fajl, dbms_rowid.rowid_block_number(ROWID) blokk, count(*) szam from NIKOVITS.TABLA_123 group by dbms_rowid.rowid_relative_fno(ROWID), dbms_rowid.rowid_block_number(ROWID) order by dbms_rowid.rowid_relative_fno(ROWID), dbms_rowid.rowid_block_number(ROWID))
    LOOP
        dbms_output.put_line(x.fajl ||';' || x.blokk || '->' || x.szam);
    end loop;
    for x in (select file_id fajl, block_id blokk from DBA_EXTENTS where segment_name like 'TABLA_123' and owner = 'NIKOVITS')
    loop
        dbms_output.put_line(x.fajl ||';' || x.blokk || '->' || 0);
    end loop;
end;

set serveroutput on
execute num_of_rows();

EXECUTE check_plsql('num_of_rows()');

select * from DBA_SEGMENTS where segment_name = 'TABLA_123' and owner = 'NIKOVITS' ;
select file_id, block_id, count(*) szam from DBA_EXTENTS where segment_name like 'TABLA_123' group by file_id, block_id;
select * from DBA_EXTENTS where file_id = '7' and segment_name = 'TABLA_123';

select *
from dba_extents
where file_id = 2
and 520 between block_id and (block_id + blocks -1)

select *
from dba_extents
where file_id = 7
and block_id = 529;

--jó
CREATE OR REPLACE PROCEDURE num_of_rows IS
begin
    for x in 560..562
    loop
        dbms_output.put_line('2;' || x || '->0');
    end loop;
    for x in (select dbms_rowid.rowid_relative_fno(ROWID) fajl, dbms_rowid.rowid_block_number(ROWID) blokk, count(*) szam from NIKOVITS.TABLA_123 group by dbms_rowid.rowid_relative_fno(ROWID), dbms_rowid.rowid_block_number(ROWID) order by dbms_rowid.rowid_relative_fno(ROWID), dbms_rowid.rowid_block_number(ROWID))
    LOOP
        if x.blokk = 564 then
            dbms_output.put_line(x.fajl ||';' || x.blokk || '->' || x.szam);
            dbms_output.put_line('2;565->0');
        else
            dbms_output.put_line(x.fajl ||';' || x.blokk || '->' || x.szam);
        end if;
    end loop;
    for x in 512..535
    loop
        dbms_output.put_line('7;' || x || '->0');
    end loop;
end;

select distinct dbms_rowid.rowid_block_number(ROWID) blokk from NIKOVITS.TABLA_123

select file_id from dba_extents where segment_name = 'TABLA_123' and owner = 'NIKOVITS';

select file_id, block_id from dba_extents where segment_name = 'TABLA_123' and owner = 'NIKOVITS';
select extent_id,file_id,block_id,bytes,blocks from dba_extents where segment_name like 'TABLA_123';
