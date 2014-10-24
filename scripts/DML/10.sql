SELECT T.IDNumber, T.Email ,((T.Amount*GC.Discount*0.01) - (I.ExpectedPrice)) as AmountOverpaid FROM Transaction T , Item I, ItemTransaction IT , GoldCustomer GC
WHERE T.Type = 'bought' AND T.IDNumber = IT.IDNumber AND IT.ItemNumber = I.ItemNumber AND IT.Email = I.Email AND T.Email = GC.Email AND T.Email IN ( SELECT GC.Email FROM GoldCustomer GC)
UNION
SELECT T.IDNumber, T.Email ,(T.Amount - I.ExpectedPrice) as AmountOverpaid FROM Transaction T , Item I, ItemTransaction IT 
WHERE T.Type = 'bought' AND T.IDNumber = IT.IDNumber AND IT.ItemNumber = I.ItemNumber AND IT.Email = I.Email AND T.Email NOT IN ( SELECT GC.Email FROM GoldCustomer GC);