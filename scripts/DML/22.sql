UPDATE GoldCustomer SET Discount = Discount + 5 WHERE Email IN (SELECT  DISTINCT GCT.GCEmail FROM GoldCustomers_Trade GCT, PlatinumCustomers_Trade PCT WHERE GCT.GCTotal > PCT.PCTotal);