SELECT extract(year from tdate) as year, extract (month from tdate) as month, count (*) as  Transactions , sum(Amount) as TotalAmount
FROM Transaction 
GROUP BY extract(year from tdate),extract (month from tdate);