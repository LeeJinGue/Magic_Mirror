import sys
from matplotlib.pyplot import get
import pymysql
import time

# from sympy import re

#데이터 베이스 연결 함수
def db_connect():
  db = pymysql.connect(
  host='127.0.0.1', 
  port=3306, 
  user='root', passwd='wlsdn153', 
  db='mirror_db', charset='utf8')
  print(db)
  return db


#모든 테이블 호출
def get_all_table():
    db = db_connect()
    table_name = ('user', 'layoutsetting','message','schedule','stock','belongings')
    all_table_json = {}
    try: 
        with db.cursor(pymysql.cursors.DictCursor) as cursor:
            for s in table_name:
                sql = 'SELECT * FROM '
                sql += s
                cursor.execute(sql)
                result = cursor.fetchall()
                #print(result)
                if(s == table_name[3]):
                    for i in range(len(result)):
                        result[i]['start_time'] = str(result[i]['start_time'])
                        result[i]['end_time'] = str(result[i]['end_time'])
                        #print(result[i])
                
                if(s == table_name[2]):
                    for i in range(len(result)):
                        result[i]['date'] = str(result[i]['date'])
                        #print(result[i])
                 
                all_table_json[s] = result
    finally:
        db.close()

    # print(all_table_json)
    return all_table_json

#print(get_all_table())

#시리얼넘버 체크 함수
def checkSerial(input):
    db = db_connect()
    try:
        with db.cursor(pymysql.cursors.DictCursor) as cursor:
            sql = "SELECT serial_no FROM device;"
            cursor.execute(sql)
            result = cursor.fetchall()
            for i in range(len(result)):
                #print(result[i])
                #print(input)
                if(result[i]['serial_no'] == input['serial_no']):
                    return 0
    finally:
        db.close()
    return 1

#프로필 추가 함수
def add_profile(input):
    db = db_connect()
    user_insert_sql="INSERT INTO user(serial_no, name, user_image_pass) VALUES (%s, %s, %s);"

    try: 
        with db.cursor(pymysql.cursors.DictCursor) as cursor:
            cursor.execute(user_insert_sql,[input['serial_no'],input['name'],'/'+input['name']])
            db.commit()
            r = cursor.lastrowid

    finally:
        db.close()

    return {'user_num':r}

#프로필 수정 함수
def edit_profile(input):
    db = db_connect()
    profile_edit_sql = 'UPDATE user SET name = %s, user_image_pass = %s WHERE user_num = %s'
    
    try: 
        with db.cursor(pymysql.cursors.DictCursor) as cursor:
            cursor.execute(profile_edit_sql,[input["name"],'/'+input['name'], input['user_num']])
            db.commit()
    except:
        return 1

    finally:
        db.close()

    return 0

#프로필 삭제 함수
def del_profile(input):
    db = db_connect()
    table_name = ('user', 'layoutsetting','message','schedule','stock','belongings')
    
    try: 
        with db.cursor(pymysql.cursors.DictCursor) as cursor:
            for s in table_name:
                sql = 'DELETE FROM '
                sql = sql + s + ' where user_num = %s'
                cursor.execute(sql,input['user_num'])
                db.commit()
    except Exception as e:
        print(e)
        return 1

    finally:
        db.close()

    return 0

#레이아웃 설정 함수
def layout_set(input):
    db = db_connect()
    layout_del_sql = "DELETE FROM layoutsetting where user_num = %s"
    layout_set_sql = "INSERT INTO layoutsetting(user_num, loc, type) VALUES (%s, %s, %s);"
    get_layout_id_sql = 'SELECT layout_id FROM layoutsetting WHERE user_num = %s'
    
    try: 
        with db.cursor(pymysql.cursors.DictCursor) as cursor:
            #현재설정 삭제
            cursor.execute(layout_del_sql, input[0]['user_num'])
            db.commit()
            #신규설정 등록
            for s in input:
                cursor.execute(layout_set_sql, [s['user_num'],s['loc'], s['type']])
                db.commit()
            #id 요청
            cursor.execute(get_layout_id_sql, input[0]['user_num'])            
            r = cursor.fetchall()

    finally:
        db.close()

    return r

#메시지 등록 함수
def send_message(input):
    db = db_connect()
    message_send_sql="INSERT INTO message(sender_num, user_num, text, date) VALUES (%s, %s, %s, %s);"
    get_message_id_sql = 'SELECT message_id FROM message WHERE date = %s and sender_num = %s'
    
    try: 
        with db.cursor(pymysql.cursors.DictCursor) as cursor:
            cursor.execute(message_send_sql,[input['sender_num'],input['user_num'], input['text'], input['date']])
            db.commit()
            r = cursor.lastrowid

    finally:
        db.close()

    return {'message_id':r}

#메시지 삭제 함수
def message_del(input):
    db = db_connect()
    message_del_sql = "DELETE FROM message where message_id = %s"
    try: 
        with db.cursor(pymysql.cursors.DictCursor) as cursor:
            #메시지 삭제
            cursor.execute(message_del_sql, input['message_id'])
            db.commit()
    except Exception as e:
        print(e)
        return 1

    finally:
        db.close()

    return 0

#일정 등록 함수
def add_schedule(input):
    db = db_connect()
    add_schedule_sql="INSERT INTO schedule(user_num, start_time, end_time, text) VALUES (%s, %s, %s, %s);"
    
    try: 
        with db.cursor(pymysql.cursors.DictCursor) as cursor:
            cursor.execute(add_schedule_sql,[input['user_num'],input['start_time'], input['end_time'], input['text']])
            db.commit()
            r = cursor.lastrowid

    finally:
        db.close()

    return {'schedule_id': r}

#일정 수정 함수
def edit_schedule(input):
    db = db_connect()
    schedule_edit_sql = 'UPDATE schedule SET start_time = %s, end_time = %s, text = %s WHERE schedule_id = %s'
    
    try: 
        with db.cursor(pymysql.cursors.DictCursor) as cursor:
            cursor.execute(schedule_edit_sql,[input['start_time'],input['end_time'], input['text'], input['schedule_id']])
            db.commit()
    except:
        return 1

    finally:
        db.close()

    return 0

#일정 삭제 함수
def del_schedule(input):
    db = db_connect()
    del_schedule_sql = "DELETE FROM schedule where schedule_id = %s"
    try: 
        with db.cursor(pymysql.cursors.DictCursor) as cursor:
            cursor.execute(del_schedule_sql, input['schedule_id'])
            db.commit()
    except:
        return 1

    finally:
        db.close()
    return 0


#관심주 등록 함수
def add_stock(input):
    db = db_connect()
    add_stock_sql="INSERT INTO stock(user_num, stock_code, stock_name) VALUES (%s, %s, %s);"
    
    try: 
        with db.cursor(pymysql.cursors.DictCursor) as cursor:
            cursor.execute(add_stock_sql,[input['user_num'],input['stock_code'], input['stock_name']])
            db.commit()
            r = cursor.lastrowid

    finally:
        db.close()

    return {'stock_id': r}

#관심주 삭제 함수
def del_stock(input):
    db = db_connect()
    del_stock_sql = "DELETE FROM stock where stock_id = %s"
    try: 
        with db.cursor(pymysql.cursors.DictCursor) as cursor:
            cursor.execute(del_stock_sql, input['stock_id'])
            db.commit()
    except:
        return 1

    finally:
        db.close()
    return 0

#소지품 등록 함수
def add_belongings(input):
    db = db_connect()
    bleonging_send_sql="INSERT INTO belongings(user_num, set_name, set_info, stuff_list, activation) VALUES (%s, %s, %s, %s, %s);"
    
    try: 
        with db.cursor(pymysql.cursors.DictCursor) as cursor:
            cursor.execute(bleonging_send_sql,[input['user_num'],input['set_name'], input['set_info'], input['stuff_list'],input['activation']])
            db.commit()
            r = cursor.lastrowid

    finally:
        db.close()

    return {'belonging_id': r}


#소지품 수정 함수
def edit_belongings(input):
    db = db_connect()
    belongings_edit_sql = 'UPDATE belongings SET set_name = %s, set_info = %s, stuff_list = %s WHERE belonging_id = %s'
    
    try: 
        with db.cursor(pymysql.cursors.DictCursor) as cursor:
            cursor.execute(belongings_edit_sql,[input['set_name'],input['set_info'], input['stuff_list'], input['belonging_id']])
            db.commit()
    except:
        return 1

    finally:
        db.close()
    return 0


#소지품 활성화 함수
def activation_belongings(input):
    db = db_connect()
    activation_edit_sql1 = 'UPDATE belongings SET activation = 1 WHERE belonging_id = %s'
    activation_edit_sql0 = 'UPDATE belongings SET activation = 0 WHERE user_num = %s'
    
    try: 
        with db.cursor(pymysql.cursors.DictCursor) as cursor:
            cursor.execute(activation_edit_sql0,input['user_num'])
            db.commit()
            cursor.execute(activation_edit_sql1,input['belonging_id'])
            db.commit()
    except:
        return 1

    finally:
        db.close()
    return 0

#소지품 비활성화 함수
def deactivation_belongings(input):
    db = db_connect()
    deactivation_edit_sql = 'UPDATE belongings SET activation = 0 WHERE belonging_id = %s'
    
    try: 
        with db.cursor(pymysql.cursors.DictCursor) as cursor:
            cursor.execute(deactivation_edit_sql,input['belonging_id'])
            db.commit()
            
    except:
        return 1

    finally:
        db.close()
    return 0
    
#소지품 삭제 함수
def del_belongings(input):
    db = db_connect()
    del_belonging_sql = "DELETE FROM belongings where belonging_id = %s"
    try: 
        with db.cursor(pymysql.cursors.DictCursor) as cursor:
            cursor.execute(del_belonging_sql, input['belonging_id'])
            db.commit()
    except:
        return 1

    finally:
        db.close()
    return 0

#소지품 삭제
# input = {'belonging_id':3}
# print(del_belongings(input))

#소지품 활성화
# input = {'belonging_id':3, 'user_num': 2}
# print(activation_belongings(input))


#소지품 수정
# input = {'belonging_id':2, 'set_name': '금요일', 'set_info': '공강', 'stuff_list':'핸드폰,핸드크림,립밤,노트북'}
# edit_belongings(input)

#소지품 등록 테스트
# input = {'user_num':2, 'set_name': '목요일', 'set_info': '공강', 'stuff_list' :'지갑,핸드폰,핸드크림,립밤,노트북', 'activation':'0'}
# print(add_belongings(input))

#관심주 삭제 테스트
# input = {'stock_id': 5}
# print(del_stock(input))

#관심주 등록 테스트
# input = {'user_num': 2,'stock_code': '005930','stock_name': '삼성전자'}
# print(add_stock(input))

#일정 수정 테스트
# input = {'schedule_id':5, 'start_time':'2022-05-24 18:00:00', 'end_time': '2022-05-24-20:00:00', 'text': '코딩박살'}
# print(edit_schedule(input))

#일정 등록 테스트
# input = {'user_num':1, 'start_time':'2022-05-24 18:00:00', 'end_time': '2022-05-24-20:00:00', 'text': '코딩공부'}
# print(add_schedule(input))

#메시지 삭제 테스트
# input = {'message_id' : 6}
# message_del(input)


#메시지 전송 테스트
# input = {'sender_num':2, 'user_num':1, 'text':'안녕하세요', 'date':'2022-05-24 00:44:30'}
# print(send_message(input))

#레이아웃 설정 테스트
# input =  [ {'user_num':2, 'loc':0, 'type':3}, {'user_num':2, 'loc':2, 'type':5} ]
# print(layout_set(input))

#프로필 삭제 테스트
# input = {'user_num': 19}
# del_profile(input)

#프로필 변경 테스트
# input = {'user_num':19, 'name': 'AAA'}
# print(edit_profile(input))

# #사용자 추가 테스트
# input = { 'name': 'BBB', 'serial_no' : 101}
# print(add_profile(input))

#모든 테이블 요청 테스트
#print(get_all_table())
