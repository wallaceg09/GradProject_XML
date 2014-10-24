UPDATE Item SET  ExpectedPrice= ExpectedPrice/2 where ItemNumber
 IN 
(SELECT Itemnumber from Item_AvgRate  WHERE Avg_Rate = (SELECT min(Avg_Rate) from Item_AvgRate ) )
AND Email IN (SELECT IEmail FROM Item_AvgRate  WHERE Avg_Rate = (SELECT min(Avg_Rate) from Item_AvgRate ) );