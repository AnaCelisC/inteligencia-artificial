
import cv2
import os
import time

# Pedir el nombre del familiar
nombre = input("Nombre de la persona: ").strip().lower()

if not nombre or not nombre.replace("_", "").isalnum():
    raise ValueError("Escribe un nombre valido, sin espacios ni simbolos.")

# Crear carpeta para guardar las fotografias
carpeta = os.path.join("dataset", nombre)
os.makedirs(carpeta, exist_ok=True)

# Cargar detector de rostros
detector = cv2.CascadeClassifier(
    "haarcascade_frontalface_default.xml"
)

if detector.empty():
    raise RuntimeError("No se pudo cargar el archivo XML.")

# Abrir la camara
camara = cv2.VideoCapture(0)

if not camara.isOpened():
    raise RuntimeError("No se pudo abrir la camara.")

contador = 0
ultima_foto = 0
total_fotos = 100

print("Mira a la camara y mueve ligeramente tu rostro.")
print("Presiona Q para salir.")

while contador < total_fotos:
    ret, frame = camara.read()

    if not ret:
        print("No se pudo leer la camara.")
        break

    gris = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)

    rostros = detector.detectMultiScale(
        gris,
        scaleFactor=1.1,
        minNeighbors=5,
        minSize=(80, 80)
    )

    # Guardar solo cuando aparezca un rostro
    if len(rostros) == 1:
        x, y, w, h = rostros[0]

        rostro = gris[y:y+h, x:x+w]
        rostro = cv2.resize(rostro, (100, 100))

        if time.time() - ultima_foto >= 0.3:
            contador += 1

            archivo = os.path.join(
                carpeta,
                f"{nombre}_{contador:03d}.jpg"
            )

            cv2.imwrite(archivo, rostro)
            ultima_foto = time.time()

        cv2.rectangle(
            frame, (x, y), (x+w, y+h),
            (0, 255, 0), 2
        )

    cv2.putText(
        frame,
        f"Fotos: {contador}/{total_fotos}",
        (20, 40),
        cv2.FONT_HERSHEY_SIMPLEX,
        1,
        (0, 255, 0),
        2
    )

    cv2.imshow("Registro de rostros", frame)

    if cv2.waitKey(1) & 0xFF == ord('q'):
        break

camara.release()
cv2.destroyAllWindows()

print(f"Registro terminado. Fotos guardadas: {contador}")
