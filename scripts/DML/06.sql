select t.title, LastName, c.Email, m_c.mCount, last_post_date
from 	thread t 	join moderator mo on(t.ThreadID = mo.ThreadID) 
					join latest_post_date l_p_d on(t.ThreadID = l_p_d.ThreadID)
					join Customer c on (mo.Email = c.Email)
					join message_count m_c on (m_c.ThreadID = t.ThreadID)
order by last_post_date desc;