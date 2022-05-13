# pip install opencv-python-headless
# pip install opencv-contrib-python
import shutil
import cv2
import cvlib as cv
import numpy as np
import os
import cam
import pymysql
from PIL import Image

class FaceRecog():
    def __init__(self):
        self.cam = None
        self.faceimagepath = './faceimage/'
        self.recognizer = cv2.face.LBPHFaceRecognizer_create()
        self.dict_label = dict()
        self.dict_reverlabel = dict()
        self.font = cv2.FONT_HERSHEY_SIMPLEX
        self.process_this_frame = True

        #Initiallize Variables
        self.cursor = None
        self.trainerset = []

        self.updateLabel()  # set initialize & update

    def __del__(self):
        del self.cam

    def facedetect_recogize(self):
        try:
            self.updateModel()  # set initialize & update
            id_conf = [(0, 0) for i in range(len(self.trainerset))]
            llt = [0 for i in range(len(self.trainerset))]
            for i in range(len(self.trainerset)):
                llt[i] = cv2.face.LBPHFaceRecognizer_create()
                llt[i].read(self.trainerset[i])

            self.cam = cam.VideoCamera()
            # loop through frames
            while self.process_this_frame:
                frame = self.cam.get_frame()

                gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)

                face, confidence = cv.detect_face(frame)

                for idx, f in enumerate(face):
                    (startX, startY) = f[0], f[1]
                    (endX, endY) = f[2], f[3]
                    cv2.rectangle(frame, (startX, startY), (endX, endY), (0, 0, 255), 2)
                    face_in_img = gray[startY:endY, startX:endX]
                    # for i in range(len(self.trainerset)):
                    #     self.recognizer.read(self.trainerset[i])
                    #     id_conf[i] = (self.recognizer.predict(face_in_img))
                    # id, confidence = max(id_conf, key=lambda x:x[1])
                    for i in range(len(self.trainerset)):
                        id_conf[i] = llt[i].predict(face_in_img)
                    id, confidence = min(id_conf, key=lambda x: x[1])
                    if confidence < 100:
                        id = self.dict_reverlabel[id]
                        confidence = "  {0}%".format(round(confidence))
                    else:
                        id = "unknown"
                        confidence = "  {0}%".format(round(confidence))

                    cv2.putText(frame, str(id), (startX + 5, startY - 5), self.font, 1, (255, 255, 255), 2)
                    cv2.putText(frame, str(confidence), (startX + 5, endY - 5), self.font, 1, (255, 255, 0), 1)

                cv2.imshow('camera', frame)  # camera라는 이름으로 출력
                k = cv2.waitKey(1) & 0xff
                if k == ord('q'):  # press "Q" to stop
                    break

            print("\n [INFO] Exiting Program and cleanup stuff")
            self.cam.__del__()
            cv2.destroyAllWindows()
        except cv2.error as cve:
            print(cve)
            #얼굴인식영역의 좌표가 음수가 되면 오류발생
            print("카메라와 인식된 얼굴이 너무 가까워서 발생하는 오류같음")
            self.cam.__del__()
            cv2.destroyAllWindows()
            print("재시작")
            self.facedetect_recogize()




    # param : name (profile_name)
    # after enroll profile, collect face datasets with cam
    def collectmaskFace(self, name):
        if not os.path.exists(self.faceimagepath + name + '/mask'):
            self.createFloder(self.faceimagepath + name + '/mask')

        self.cam = cam.VideoCamera()
        captured_num = 0
        interval_num = 0
        # loop through frames
        while self.process_this_frame:
            frame = self.cam.get_frame()

            # 이미지 내 얼굴 검출
            face, confidence = cv.detect_face(frame)
            interval_num += 1

            # loop through detected faces
            for idx, f in enumerate(face):
                (startX, startY) = f[0], f[1]
                (endX, endY) = f[2], f[3]

                if interval_num % 4 == 0:
                    captured_num += 1
                    face_in_img = frame[startY:endY, startX:endX, :]
                    face_in_img = cv2.resize(face_in_img, dsize=(224, 224), interpolation=cv2.INTER_AREA)
                    face_in_img = cv2.cvtColor(face_in_img, cv2.COLOR_BGR2GRAY)
                    cv2.imwrite(self.faceimagepath + name + '/nomask/' + name + '_' + str(captured_num) + '.jpg',
                                face_in_img)

            # display output
            cv2.imshow("captured frames", frame)
            key = cv2.waitKey(1) & 0xFF
            if key == ord('q'):  # press "Q" to stop
                break
            if captured_num >= 30:
                break

        # release resources
        self.cam.__del__()
        cv2.destroyAllWindows()

    def collectnomaskFace(self, name):
        if not os.path.exists(self.faceimagepath + name + '/nomask'):
            self.createFloder(self.faceimagepath + name + '/nomask')

        self.cam = cam.VideoCamera()
        captured_num = 0
        interval_num = 0
        # loop through frames
        while self.process_this_frame:
            frame = self.cam.get_frame()

            # 이미지 내 얼굴 검출
            face, confidence = cv.detect_face(frame)
            interval_num += 1

            # loop through detected faces
            for idx, f in enumerate(face):
                (startX, startY) = f[0], f[1]
                (endX, endY) = f[2], f[3]

                if interval_num % 4 == 0:
                    captured_num += 1
                    face_in_img = frame[startY:endY, startX:endX, :]
                    face_in_img = cv2.resize(face_in_img, dsize=(224, 224), interpolation=cv2.INTER_AREA)
                    face_in_img = cv2.cvtColor(face_in_img, cv2.COLOR_BGR2GRAY)
                    cv2.imwrite(self.faceimagepath + name + '/nomask/' + name + '_' + str(captured_num) + '.jpg',
                                face_in_img)

            # display output
            cv2.imshow("captured frames", frame)
            key = cv2.waitKey(1) & 0xFF
            if key == ord('q'):  # press "Q" to stop
                break
            if captured_num >= 30:
                break

        # release resources
        self.cam.__del__()
        cv2.destroyAllWindows()

    # function to get the images and label data
    def getImagesAndLabels(self, name):
        m_faceimagepath = self.faceimagepath + name + '/mask/'
        nm_faceimagepath = self.faceimagepath + name + '/nomask/'
        maskimagePaths = [os.path.join(m_faceimagepath, f) for f in os.listdir(m_faceimagepath)]
        nomaskimagePaths = [os.path.join(nm_faceimagepath, f) for f in os.listdir(nm_faceimagepath)]
        faceSamples = []
        ids = []

        # mask
        for maskimagePath in maskimagePaths:
            img = cv2.imread(maskimagePath, cv2.IMREAD_GRAYSCALE)
            id = self.dict_label[os.path.split(maskimagePath)[1].split("_")[0]]
            faceSamples.append(img)
            ids.append(id)

        # nomask
        for nomaskimagePath in nomaskimagePaths:
            img = cv2.imread(nomaskimagePath, cv2.IMREAD_GRAYSCALE)
            id = self.dict_label[os.path.split(nomaskimagePath)[1].split("_")[0]]
            faceSamples.append(img)
            ids.append(id)

        return faceSamples, ids


    def addtrainModel(self, name):
        _faceimagepath = self.faceimagepath + name + '/'
        print("\n [INFO] Training faces. It will take a few seconds. Wait ...")
        faces, ids = self.getImagesAndLabels(name)
        self.recognizer.train(faces, np.array(ids))

        # Save the model into trainer/trainer.yml
        # recognizer.write(os.path.join(_faceimagepath,'trainer.yml'))   # recognizer.save() worked on Mac, but not on Pi
        self.recognizer.write(_faceimagepath + name + '_trainer.yml')  # recognizer.save() worked on Mac, but not on Pi

        self.trainerset.append(_faceimagepath + name + '_trainer.yml')

        # Print the numer of faces trained and end program
        print("\n [INFO] {0} faces trained. Exiting Program".format(len(self.trainerset)))

    # ./fileimage/user_name dir 생성
    def createFloder(self, name):
        try:
            if not os.path.exists(self.faceimagepath + name):
                os.makedirs(self.faceimagepath + name)
        except OSError:
            print('Error: Creating directory. ' + self.faceimagepath + name)

    #if delete profile, first call deleteFolder, next step updateLabel(), updateModel()
    def deleteFolder(self, name):
        try:
            if os.path.exists(self.faceimagepath + name):
                shutil.rmtree(self.faceimagepath + name)
                del(self.trainerset[self.faceimagepath + name + '/' + name + '_trainer.yml'])
        except OSError:
            print('Error: delete directory. ' + self.faceimagepath + name)

        # 인식모델 학습 labeling하기 위한 사전작업
        # DB에서 현재 등록한 User의 정보를 불러와 'User name' : idx로 dict 생성 후,
        # 생성한 dict로 getImageAndLabels에서 labeling에 이용

    def connDB(self):
        '''
               db = pymysql.connect(
                   host='127.0.0.1',
                   port=3306,
                   user='root', passwd='1234',
                   db='mirror_db', charset='utf8')

               self.cursor = db.cursor()
        '''

    def updateLabel(self):
        # User Table data
        '''
        # db = pymysql.connect(
        #     host='127.0.0.1',
        #     port=3306,
        #     user='root', passwd='1234',
        #     db='mirror_db', charset='utf8')
        #
        # cursor = db.cursor()

        sql = 'SELECT user_num, name FROM User'
        self.cursor.execute(sql)

        while True:
            row = self.cursor.fetchone()
            if row = None:
                break
            self.dict_label[row[1]] = row[0]
            self.dict_reverlabel[row[0]] = row[1]
        '''

        self.dict_label = {'park': 1, 'lee': 2, 'jeon': 9}
        self.dict_reverlabel = {1:'park', 2:'lee',9:'jeon'}

    # # 'idx : User name'으로 이루어진 dict를 반환하는 함수
    # def reverLabel(self):
    #     dict_reverlabel = dict()
    #     for k, v in self.dict_label.items():
    #         dict_reverlabel[v] = k
    #     return dict_reverlabel


    def updateModel(self):
        self.trainerset = [self.faceimagepath + f + '/' + f + '_trainer.yml'
                            for f in os.listdir(self.faceimagepath)]


if __name__ == "__main__":
    face_rg = FaceRecog()
    #face_rg.addtrainModel('jeon')
    #face_rg.addtrainModel('park')
    #face_rg.addtrainModel('lee')
    face_rg.facedetect_recogize()
    #face_rg.test()

    print('clear')
