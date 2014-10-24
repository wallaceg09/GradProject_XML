SELECT Title, FirstName, MidInit, LastName, Suffix, TotalTrade, TotalItems FROM Customer_Details 
UNION 
SELECT null, null, null, null, 'Grand Total', Sum(TotalTrade) , sum (TotalItems) FROM Customer_Details ORDER BY TotalTrade DESC;