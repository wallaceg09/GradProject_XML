 DELETE FROM Customer WHERE Email in (
SELECT CEmail FROM CustomerReview  WHERE Critique='poor' GROUP BY CEmail HAVING COUNT(Critique)>2);