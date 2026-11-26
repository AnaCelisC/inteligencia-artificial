import cv2
import numpy as np
import tensorflow as tf
import json
from pathlib import Path

# Ubicacion de los archivos.
ruta = Path(__file__).resolve().parent

# Carga el modelo entrenado.
model = tf.keras.models.load_model(
    ruta / 'modelo_cnn_rostros.keras'
)

# Carga los nombres de las personas.
with open(ruta / 'nombres_cnn.json', 'r', encoding='utf-8') as archivo:
    class_names = json.load(archivo)

# Carga el detector de rostros.
face_cascade = cv2.CascadeClassifier(
    str(ruta / 'haarcascade_frontalface_default.xml')
)

if face_cascade.empty():
    print("No se pudo cargar el detector de rostros.")
    exit()

# Abre la camara.
cap = cv2.VideoCapture(0)

if not cap.isOpened():
    print("No se pudo abrir la camara.")
    exit()

while True:

    # Lee la imagen de la camara.
    ret, frame = cap.read()

    if not ret:
        break

    # Convierte la imagen a escala de grises.
    gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)

    # Detecta los rostros.
    faces = face_cascade.detectMultiScale(gray, 1.3, 5)

    for (x, y, w, h) in faces:

        # Recorta el rostro.
        roi_gray = gray[y:y+h, x:x+w]

        # Redimensiona el rostro.
        roi_gray = cv2.resize(roi_gray, (128, 128))

        # Agrega las dimensiones para la CNN.
        roi_gray = np.expand_dims(roi_gray, axis=-1)
        roi_gray = np.expand_dims(roi_gray, axis=0)

        # Realiza la prediccion.
        predictions = model.predict(roi_gray, verbose=0)

        # Obtiene la persona con mayor probabilidad.
        class_idx = np.argmax(predictions[0])
        confidence = np.max(predictions[0]) * 100

        # Comprueba la confianza.
        if confidence >= 60:
            nombre = class_names[class_idx]
            color = (0, 255, 0)
        else:
            nombre = "Desconocido"
            color = (0, 0, 255)

        # Dibuja el recuadro del rostro.
        cv2.rectangle(
            frame,
            (x, y),
            (x+w, y+h),
            color,
            2
        )

        # Muestra el nombre y la confianza.
        cv2.putText(
            frame,
            f"{nombre} ({confidence:.1f}%)",
            (x, max(y-10, 20)),
            cv2.FONT_HERSHEY_SIMPLEX,
            0.8,
            color,
            2
        )

    # Muestra la camara.
    cv2.imshow('Reconocimiento Facial con CNN', frame)

    # Presiona Q para salir.
    if cv2.waitKey(1) & 0xFF == ord('q'):
        break

cap.release()
cv2.destroyAllWindows()