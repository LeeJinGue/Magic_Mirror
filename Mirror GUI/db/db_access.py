from matplotlib.pyplot import get
import pymysql


def db_connect():
  db = pymysql.connect(
  host='127.0.0.1', 
  port=3306, 
  user='root', passwd='1234', 
  db='mirror_db', charset='utf8')
  print(db)
  return db

#레이아웃 호출 합수
def get_user_layoutsetting(user_id):
    db = db_connect()
    try: 
        with db.cursor() as cursor:
            sql = 'SELECT * FROM layoutsetting where user_num = %s'
            cursor.execute(sql,user_id)
            result = cursor.fetchall()
    finally:
        db.close()

    return result

print(get_user_layoutsetting(2))

#스케쥴 호출 함수
def get_schedule(user_id):
    db = db_connect()
    try: 
        with db.cursor() as cursor:
            sql = 'SELECT * FROM schedule where user_num = %s  ORDER BY start_time'
            cursor.execute(sql,user_id)
            result = cursor.fetchall()
    finally:
        db.close()

    return result    

#소지품 호출 함수
def get_belongings(user_id):
    db = db_connect()
    try: 
        with db.cursor() as cursor:
            sql = 'SELECT stuff_list FROM belongings where user_num = %s AND activation = %s'
            cursor.execute(sql,[user_id,1])
            result = cursor.fetchall()
            #print(result)
    finally:
        db.close()

    return result

#메시지 호출 함수
def get_message(user_id):
    db = db_connect()
    try: 
        with db.cursor() as cursor:
            sql = 'SELECT * FROM message LEFT JOIN user on message.sender_num = user.user_num where message.user_num = %s'
            cursor.execute(sql,user_id)
            result = cursor.fetchall()
    finally:
        db.close()

    return result

#주식 호출 함수
def get_stock(user_id):
    db = db_connect()
    try: 
        with db.cursor() as cursor:
            sql = 'SELECT * FROM stock where user_num = %s'
            cursor.execute(sql,user_id)
            result = cursor.fetchall()
    finally:
        db.close()

    return result


#유저 리스트 호출 함수
def get_name():
    db = db_connect()
    r = {}
    try: 
        with db.cursor() as cursor:
            sql = 'SELECT name, user_num FROM user'
            cursor.execute(sql)
            result = cursor.fetchall()
    finally:
        db.close()
    for i in result:
        r[i[0]] = i
    return r

#print(get_name())

# print(get_user_layoutsetting(1))
# data = get_schedule(1)
# print(data[0][2].minute)

# data = get_belongings(1)
# blist = data[0][3].split(',')
# print(blist )



# def get_message2(user_id):
#     db = db_connect()
#     try: 
#         with db.cursor(pymysql.cursors.DictCursor) as cursor:
#             sql = 'SELECT * FROM message LEFT JOIN user on message.sender_num = user.user_num where message.user_num = %s'
#             cursor.execute(sql,user_id)
#             result = cursor.fetchall()
#     finally:
#         db.close()

#     return result
# data = get_message2(1)
# print(data)
#get_belongings(1)