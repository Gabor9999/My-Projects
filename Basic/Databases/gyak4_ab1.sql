--orai 
create table gyak4 as
(select o.oazon as Oazon, o.telephely as Telephely, round(avg(d.fizetes)) as AtlagFiz From dolgozo d Join osztaly o on d.oazon=o.oazon group by o.oazon, o.telephely);