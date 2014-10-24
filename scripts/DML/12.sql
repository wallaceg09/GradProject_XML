SELECT I.ItemNumber as INum, I.Weight, I.ExpectedPrice as Price, C.Title, C.FirstName, C.MidInit, C.LastName, C.Suffix FROM Item I, Customer C WHERE I.Email=C.Email
 AND 
C.Email IN ( SELECT CR.CEmail FROM CustomerReview CR WHERE CR.Critique='poor' GROUP BY CR.CEmail  HAVING  count(CR.Critique) > 1
UNION
SELECT CR.CEmail FROM CustomerReview CR WHERE CR.Critique='fair' GROUP BY CR.CEmail  HAVING  count(CR.Critique) > 1) 
ORDER BY 
C.LastName, C.FirstName, C.MidInit ;