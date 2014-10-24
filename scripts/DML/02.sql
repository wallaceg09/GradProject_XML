select	c.Title, c.FirstName, c.MidInit, c.LastName, c.City, c.Country, v.UpgradedTime
from vip v join Customer c on (v.Email = c.Email)
order by SortOrder asc, UpgradedTime desc, LastName asc
;
