select I.itemnumber, I.weight, I.expectedprice, a.tradecount, a.Maxvalue
from item I inner join query_17 a on
i.Itemnumber = a.ItemNumber
order by a.tradecount;