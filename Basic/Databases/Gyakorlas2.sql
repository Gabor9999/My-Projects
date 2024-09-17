--Gyakorlás
--1.txt
-- #############
--1.
select owner, object_name, object_type from dba_objects where object_name = 'DBA_TABLES' and object_type = 'VIEW';
select owner, object_name, object_type from dba_objects where object_name = 'DUAL' and object_type = 'TABLE';
--2.
select owner, object_name, object_type from dba_objects where object_name = 'DBA_TABLES' and object_type = 'SYNONYM';
select owner, object_name, object_type from dba_objects where object_name = 'DUAL' and object_type = 'SYNONYM';
--3.
select distinct object_type from dba_objects where owner='ORAUSER';
--7.
select v.owner from (select distinct owner from dba_objects where object_type = 'VIEW') v, (select distinct owner from dba_objects where object_type = 'TRIGGER') t where t.owner = v.owner;
--8
(select distinct owner from dba_objects where object_type = 'VIEW')
minus
(select v.owner from (select distinct owner from dba_objects where object_type = 'VIEW') v, (select distinct owner from dba_objects where object_type = 'TRIGGER') t where t.owner = v.owner);
--9.
select t.owner from (select owner, count(*) db from dba_indexes group by owner) i, (select owner, count(*) db from dba_tables group by owner) t where t.owner = i.owner and t.db > 20 and i.db <= 15
--13.
select count(*) from dba_tab_columns where table_name = 'EMP' and owner = 'NIKOVITS'
--14.
select data_type from dba_tab_columns where column_id = '6' and table_name = 'EMP' and owner = 'NIKOVITS'
--15.
select owner, table_name from dba_tab_columns where column_name like 'Z%' group by owner, table_name
--18.

CREATE OR REPLACE PROCEDURE table_print(p_kar VARCHAR2) IS
CURSOR curs1 IS
select owner, table_name from dba_tables where upper(table_name) like UPPER(p_kar) ||'%';
rec curs1%ROWTYPE;
begin
   open curs1;
   LOOP
    FETCH curs1 INTO rec;
    EXIT WHEN curs1%NOTFOUND;
    dbms_output.put_line(rec.owner||' - '||rec.table_name);
   END LOOP;
   CLOSE curs1;
end;

CREATE OR REPLACE PROCEDURE table_print2(p_kar VARCHAR2) IS
begin
   for rec in (select owner, table_name from dba_tables where upper(table_name) like UPPER(p_kar) ||'%')
   loop
    dbms_output.put_line(rec.owner||' - '||rec.table_name);
   end loop;
end;


SET SERVEROUTPUT ON;
EXECUTE table_print2('z');
--


--2.txt

--1.
(select file_name, bytes from dba_data_files UNION
select file_name, bytes from dba_temp_files) order by bytes desc;
--4.
SELECT tablespace_name FROM dba_tablespaces WHERE tablespace_name NOT IN
 (SELECT tablespace_name FROM dba_data_files);
SELECT file_name, tablespace_name FROM dba_temp_files;
--5.
select owner, segment_name, extents from dba_segments where segment_type = 'TABLE' order by bytes desc fetch first 1 rows only;
--6.
select owner, segment_name, blocks from dba_segments where segment_type = 'INDEX' order by bytes desc fetch first 1 rows only;
--8.
select owner, sum(bytes) ossz from dba_segments group by owner order by ossz desc fetch first 2 rows only
--9.
select count(*), sum(e.bytes) from dba_data_files df, dba_extents e where df.file_name like '%users02.dbf' and df.file_id = e.file_id;
select count(*), sum(f.bytes) from dba_data_files df, dba_free_space f where df.file_name like '%users02.dbf' and df.file_id = f.file_id;
select sum(e.bytes)/df.bytes from dba_data_files df, dba_extents e where df.file_name like '%users02.dbf' and df.file_id = e.file_id group by df.bytes;
--10. (Aramis)
select segment_name, count(distinct file_id) from dba_extents where owner = 'NIKOVITS' and segment_type = 'TABLE' group by segment_name having count(distinct file_id) > 1
--12.
select tablespace_name from dba_tables where owner = 'ORAUSER' and table_name = 'DOLGOZO'
-- select * from sz1;
select * from dba_objects where object_name like '%SZ1%'
SELECT * FROM DBA_SYNONYMS WHERE owner='PUBLIC' AND synonym_name like'SZ1%';
select * from dba_objects where object_name like '%V1%' and owner = 'NIKOVITS'
select * from dba_views where owner = 'NIKOVITS' and view_name = 'V1'

CREATE DATABASE LINK aramisdb CONNECT TO YQE5B1 IDENTIFIED BY markoG200204
USING 'aramis.inf.elte.hu:1521/aramis';
select * from nikovits.vilag_orszagai
select * from NIKOVITS.FOLYOK@aramisdb

select o.nev from nikovits.vilag_orszagai o, (select orszagok from NIKOVITS.FOLYOK@aramisdb where UPPER(nev) = 'MEKONG') c where c.orszagok like '%'||o.tld||'%'

set serveroutput on
declare
s number;
e number;
orszag VARCHAR2(100);
cond VARCHAR2(100);
begin
    s := 1;
    select(select SUBSTR(orszagok, s, 2) from NIKOVITS.FOLYOK@aramisdb where UPPER(nev)= 'MEKONG') into cond from dual;
    while cond IS NOT NULL
    loop
        select (select o.nev from nikovits.vilag_orszagai o, (select SUBSTR(orszagok, s, 2) sol from NIKOVITS.FOLYOK@aramisdb where UPPER(nev)= 'MEKONG') orsz where orsz.sol = o.tld) into orszag from dual;
        dbms_output.put_line(orszag);
        s := s + 3;
        select(select SUBSTR(orszagok, s, 2) from NIKOVITS.FOLYOK@aramisdb where UPPER(nev)= 'MEKONG') into cond from dual;
    end loop;
end;
--


--3.txt
--1.
select blocks from dba_segments where owner = 'NIKOVITS' and segment_name = 'CIKK'
--2.
select count(*) from (select distinct dbms_rowid.rowid_relative_fno(ROWID) fajl, dbms_rowid.rowid_block_number(ROWID) blokk from nikovits.cikk)
--3.
SELECT dbms_rowid.rowid_relative_fno(ROWID) fajl,
       dbms_rowid.rowid_block_number(ROWID) blokk, count(*) db
FROM nikovits.cikk
GROUP BY dbms_rowid.rowid_relative_fno(ROWID), dbms_rowid.rowid_block_number(ROWID);
--4.
select fajl, blokk, sor, objektum, object_type o_nev, object_id o_id
from
(SELECT dbms_rowid.rowid_relative_fno(ROWID) fajl,
       dbms_rowid.rowid_block_number(ROWID) blokk,  
       dbms_rowid.rowid_row_number(ROWID) sor,
       dbms_rowid.rowid_object(ROWID) objektum
from nikovits.eladasok where szla_szam = 100), dba_objects where data_object_id = objektum
--

--4.txt
--1.
select index_name from dba_ind_columns where descend = 'DESC'
--2.
SELECT index_owner, index_name FROM dba_ind_columns 
GROUP BY index_owner, index_name HAVING count(*) >=9;
--3.
select index_name from dba_indexes where index_type = 'BITMAP' and table_owner = 'NIKOVITS' and table_name = 'CUSTOMERS'
--4.
SELECT index_owner, index_name FROM dba_ind_columns 
GROUP BY index_owner, index_name HAVING count(*)=2
 INTERSECT
SELECT index_owner, index_name FROM dba_ind_expressions;
--5.
SELECT column_expression FROM dba_ind_expressions WHERE index_owner='NIKOVITS';
--6.
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
--


--5.txt
--1.
SELECT table_name, partitioning_type FROM dba_part_tables WHERE owner = 'NIKOVITS';
--2.
SELECT segment_name, partition_name, blocks 
FROM dba_segments WHERE owner='NIKOVITS' AND segment_type='TABLE PARTITION' and segment_name='ELADASOK';
--3.
SELECT column_name, column_position FROM dba_part_key_columns 
WHERE owner='NIKOVITS' AND name='ELADASOK' AND object_type='TABLE';
--4.
SELECT partition_name, high_value, partition_position FROM dba_tab_partitions 
WHERE table_owner='NIKOVITS' AND table_name='ELADASOK3' AND partition_position=2;
--5.
SELECT object_name, object_type, subobject_name, object_id, data_object_id
FROM dba_objects WHERE owner='NIKOVITS' AND object_name='ELADASOK';

SELECT * FROM dba_segments WHERE owner='NIKOVITS' AND segment_name='ELADASOK';
--6.
SELECT object_name, object_type, subobject_name, object_id, data_object_id
FROM dba_objects WHERE owner='NIKOVITS' AND object_name='ELADASOK4';

SELECT * FROM dba_segments WHERE owner='NIKOVITS' AND segment_name='ELADASOK4';
--7.
SELECT owner, segment_name, SUM(bytes) FROM dba_segments 
WHERE segment_type LIKE 'TABLE%PARTITION'
GROUP BY owner, segment_name
ORDER BY SUM(bytes) DESC;

--8.
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

--1.
select distinct cluster_name from dba_clusters
MINUS
--select distinct cluster_name from dba_tables
select distinct cluster_name from dba_clu_columns;
--2.
SELECT owner, cluster_name FROM dba_tables WHERE cluster_name IS NOT NULL
GROUP BY owner, cluster_name HAVING COUNT(*) = 2;
--3.
SELECT owner, cluster_name FROM dba_clu_columns  
GROUP BY owner, cluster_name HAVING COUNT(DISTINCT clu_column_name) = 3;
--4.
SELECT COUNT(*) FROM
(SELECT owner, cluster_name, hash_expression FROM dba_cluster_hash_expressions);