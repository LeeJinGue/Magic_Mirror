import pymysql


def db_connect():
  db = pymysql.connect(
  host='127.0.0.1', 
  port=3306, 
  user='root', passwd='1234', 
  db='mirror_db', charset='utf8')
  print(db)
  return db

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


#print(get_user_layoutsetting(1))