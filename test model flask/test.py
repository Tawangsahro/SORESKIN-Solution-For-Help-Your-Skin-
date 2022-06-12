#libary yang di gunakan dalam codingan ini
import numpy as np
import tensorflow as tf
import pandas as pd
import matplotlib.pyplot as plt

from flask import Flask, render_template, request
from tensorflow import keras
from keras.models import load_model
from keras.preprocessing import image


app = Flask(__name__, template_folder='templates')

model = keras.models.load_model('modelnonpredict.h5')


frs = pd.read_csv('templates/level0.csv')
frd = pd.read_csv('templates/level1.csv')
frf = pd.read_csv('templates/level2.csv')
#fungsi untuk mengambil data ui dari html
@app.route('/', methods = ['GET'])
def hello_word():
    return render_template('test.html')

#fungsi untuk mengirimkan data prediksi ke halaman html 
@app.route('/', methods = ['POST'])
def predict():
    # fungsi untuk menyimpan gambar yang di upload dari html 
    imagefile = request.files['file']
    image_path = "./images/"+ imagefile.filename
    imagefile.save(image_path)

    #fungsi untuk prediksi gambar 
    img = tf.keras.preprocessing.image.load_img(image_path, target_size=(229, 229))
    z = tf.keras.preprocessing.image.img_to_array(img)
    z = np.expand_dims(z, axis=0)
    z = np.vstack([z])
    classes = model.predict(z, batch_size=16)
    imgplot = plt.imshow(img)

    #ntuk menetukan nilai prediksi masuk ke kelas mana 
    #x=(classes[0])
    if classes[0]<150:
     b=(" is a level0")
     a=frs
    elif classes[0]<400:
      b=(" is a level1")
      a=frd
    else:
      b=( " is a level2")
      a=frf

    #untuk mengirimkan data ke htmlnya 
    return render_template('test.html', n=b , tables=[a.to_html()])

#nama ip port yang di gunakan 
if __name__ == '__main__':
    app.run(port=3000,debug=True)