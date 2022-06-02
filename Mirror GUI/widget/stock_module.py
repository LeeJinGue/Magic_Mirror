from pykrx import stock
import os
import sys
import datetime
#sys.path.append(os.path.dirname(os.path.abspath(os.path.dirname(__file__))))
from db import db_access

def get_stock(user_num):
    result =[]
    now = datetime.datetime.now()
    if(now.weekday() == 5):
        now = now - datetime.timedelta(days=1)
    elif(now.weekday() == 6):
        now = now - datetime.timedelta(days=2)
    else:
        if now.hour < 9:
            if now.weekday() == 0:
                now = now - datetime.timedelta(days=3)
            else:
                now = now - datetime.timedelta(days=1)
    if now.weekday() == 0:
        yd = now - datetime.timedelta(days=3)
    else:
        yd = now - datetime.timedelta(days=1)
    mydate = str(now.year)+str(now.month).zfill(2)+ str(now.day).zfill(2)
    #print(mydate)
    r = db_access.get_stock(user_num)
    #print(r)
    for i in r:
        data = stock.get_market_ohlcv_by_date(fromdate=yd, todate=mydate, ticker=i[2])
        #print(data)
        result.append((data.iloc[0]['종가'],data.iloc[1]['종가'], i[3]))
        #print(result)
    return result


#get_stock(1)
