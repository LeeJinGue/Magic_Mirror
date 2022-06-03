from flask import Flask, jsonify
from flask import request
from pytest import param
import json
import server_db_access
app = Flask(__name__)
@app.route('/')

# #테스트 요청 응답
# @app.route('/addProfileTest', methods=['GET','POST'])
# def addProfileTest():
#     # data = server_db_access.get_all_table()
#     params = json.loads(request.get_data())
#     print("받은프로필: "+params)
#     data2 = {'user_num':1}
#     return jsonify(data2)

#임시 테스트 코드
# @app.route('/syncAllTable')

# @app.route('/home',methods=['GET','POST'])
# def home():
#     a = [{'i': 1, 'j':2},{'i': 5, 'j':10}]
#     b = {"table": a}
    
#     return jsonify(b)
# @app.route('/user')
# def user():
#     a = [{'i': 4, 'j':4},{'i': 5, 'j':10}]
#     return jsonify(a)

@app.route('/getData', methods=['GET','POST'])
def create():
    params = json.loads(request.get_data())
    print(params)
    a = {'user_num':1}
    return jsonify(a)


#테이블 요청 응답
@app.route('/syncAllTable', methods=['GET','POST'])
def syncAllTable():
    data = server_db_access.get_all_table()
    return jsonify(data)

#프로필 추가
@app.route('/addProfile', methods=['GET','POST'])
def addProfile():
    params = json.loads(request.get_data())
    data = server_db_access.add_profile(params)
    return data

#프로필 변경
@app.route('/editProfile', methods=['GET','POST'])
def editProfile():
    params = json.loads(request.get_data())
    data = server_db_access.edit_profile(params)
    if data == 0:
        return 'ok'
    else:
        return 'no'

#프로필 삭제
@app.route('/delProfile', methods=['GET','POST'])
def delProfile():
    params = json.loads(request.get_data())
    data = server_db_access.del_profile(params)
    if data == 0:
        return 'ok'
    else:
        return 'no'

#레이아웃 설정
@app.route('/layoutSet', methods=['GET','POST'])
def layoutSet():
    params = json.loads(request.get_data())
    data = server_db_access.layout_set(params)
    print(data)
    return jsonify(data)

#메시지 수신
@app.route('/sendMessage', methods=['GET','POST'])
def sendMessage():
    params = json.loads(request.get_data())
    data = server_db_access.send_message(params)
    return jsonify(data)

#메시지 삭제
@app.route('/delMessage', methods=['GET','POST'])
def delMessage():
    params = json.loads(request.get_data())
    data = server_db_access.message_del(params)
    if data == 0:
        return 'ok'
    else:
        return 'no'

#일정 등록
@app.route('/addSchedule', methods=['GET','POST'])
def addSchedule():
    params = json.loads(request.get_data())
    data = server_db_access.add_schedule(params)
    return jsonify(data)

#일정 수정
@app.route('/editSchedule', methods=['GET','POST'])
def editSchedule():
    params = json.loads(request.get_data())
    data = server_db_access.edit_schedule(params)
    if data == 0:
        return 'ok'
    else:
        return 'no'

#일정 삭제
@app.route('/delSchedule', methods=['GET','POST'])
def delSchedule():
    params = json.loads(request.get_data())
    data = server_db_access.del_schedule(params)
    if data == 0:
        return 'ok'
    else:
        return 'no'

#주식 등록
@app.route('/addStock', methods=['GET','POST'])
def addStock():
    params = json.loads(request.get_data())
    data = server_db_access.add_stock(params)
    return jsonify(data)

#관심주 삭제
@app.route('/delStock', methods=['GET','POST'])
def delStock():
    params = json.loads(request.get_data())
    data = server_db_access.del_stock(params)
    if data == 0:
        return 'ok'
    else:
        return 'no'


#소지품 등록
@app.route('/addBelongingSet', methods=['GET','POST'])
def addBelongingSet():
    params = json.loads(request.get_data())
    data = server_db_access.add_belongings(params)
    return jsonify(data)

#소지품 수정
@app.route('/editBelongingSet', methods=['GET','POST'])
def editBelongingSet():
    params = json.loads(request.get_data())
    data = server_db_access.edit_belongings(params)
    if data == 0:
        return 'ok'
    else:
        return 'no'

#소지품 활성화
@app.route('/activationBelongingSet', methods=['GET','POST'])
def activationBelongingSet():
    params = json.loads(request.get_data())
    data = server_db_access.activation_belongings(params)
    if data == 0:
        return 'ok'
    else:
        return 'no'

#소지품 삭제
@app.route('/delBelongingSet', methods=['GET','POST'])
def delBelongingSet():
    params = json.loads(request.get_data())
    data = server_db_access.del_belongings(params)
    if data == 0:
        return 'ok'
    else:
        return 'no'
@app.route('/checkSerial', methods=['GET', 'POST'])
def checkSerial():
    params = json.loads(request.get_data())
    data = server_db_access.checkSerial(params)
    if data == 0:
        return 'ok'
    else: 
        return 'no'

if __name__ == '__main__':
    app.run(host='0.0.0.0',port=8000)
    # app.run() 
