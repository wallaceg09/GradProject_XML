SELECT Title, FirstName, MidInit, LastName, Suffix, Item_Count , Total_Amount , AvgRate FROM Seller_Details
UNION
SELECT null, null, null, null, 'Grand Total', sum(Item_Count) as Total_Items, sum(Total_Amount) as Total_Cost , null  FROM Seller_Details;