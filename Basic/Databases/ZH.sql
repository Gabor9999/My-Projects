--1.
select distinct table_name from dba_indexes where index_type = 'BITMAP' and owner = 'HR'
MINUS
select distinct table_name from dba_tab_columns where data_type = 'NUMBER';
--2.
select owner, table_name from dba_tab_columns where (column_id = 1 and data_type = 'DATE')
INTERSECT
select owner, table_name from dba_tab_columns where (column_id = 4 and data_type = 'VARCHAR2')
INTERSECT
select distinct owner, table_name from dba_tab_columns where data_type = 'NUMBER'
--3.
select s.blocks Allokált_blokkok_száma, fel.f Felírt_blokkok_száma from dba_segments s, (select count(distinct dbms_rowid.rowid_block_number(ROWID)) f from nikovits.hallgatok) fel where owner = 'NIKOVITS' and segment_name = 'HALLGATOK'
--4.
CREATE OR REPLACE PROCEDURE cr_tab(p_owner VARCHAR2, p_tabla VARCHAR2) IS
v_create_sql CLOB;
begin
    v_create_sql := DBMS_METADATA.GET_DDL('TABLE', p_tabla, p_owner);

   IF v_create_sql IS NOT NULL THEN
      DBMS_OUTPUT.PUT_LINE(v_create_sql);
   ELSE
      DBMS_OUTPUT.PUT_LINE('A megadott tábla nem található.');
   END IF;
end;

CREATE TABLE tipus_proba(c10 CHAR(10) DEFAULT 'bubu', vc20 VARCHAR2(20), nc10 NCHAR(10), nvc15 NVARCHAR2(15), blo BLOB, clo CLOB,nclo NCLOB,num NUMBER, num10_2 NUMBER(10,2), num10 NUMBER(10) DEFAULT 100,flo FLOAT, bin_flo binary_float DEFAULT '2e+38',
bin_doub binary_double DEFAULT 2e+40, dat DATE DEFAULT TO_DATE('2007.01.01','yyyy.mm.dd'), rid ROWID);

set serveroutput on
execute cr_tab('YQE5B1', 'TIPUS_PROBA');