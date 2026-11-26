import tensorflow as tf
import matplotlib.pyplot as plt
import json
import numpy as np
from pathlib import Path
from keras import layers, models

# Ubicacion de los archivos.
ruta = Path(__file__).resolve().parent

# Configuracion de las imagenes.
BATCH_SIZE = 32
IMG_HEIGHT = 128
IMG_WIDTH = 128

# Carga las imagenes de entrenamiento.
train_ds = tf.keras.utils.image_dataset_from_directory(
    ruta / 'dataset',
    validation_split=0.2,
    subset="training",
    seed=123,
    image_size=(IMG_HEIGHT, IMG_WIDTH),
    color_mode='grayscale',
    batch_size=BATCH_SIZE
)

# Carga las imagenes de validacion.
val_ds = tf.keras.utils.image_dataset_from_directory(
    ruta / 'dataset',
    validation_split=0.2,
    subset="validation",
    seed=123,
    image_size=(IMG_HEIGHT, IMG_WIDTH),
    color_mode='grayscale',
    batch_size=BATCH_SIZE
)

# Obtiene los nombres de las carpetas.
nombres_originales = train_ds.class_names

# Guarda los nombres de todas las personas.
class_names = []

for nombre in nombres_originales:
    if nombre.lower() in ["ana", "paola"]:
        nombre = "Ana Paola"

    if nombre not in class_names:
        class_names.append(nombre)

num_classes = len(class_names)

# Relaciona las carpetas con las personas.
equivalencias = []

for nombre in nombres_originales:
    if nombre.lower() in ["ana", "paola"]:
        nombre = "Ana Paola"

    equivalencias.append(class_names.index(nombre))

equivalencias = tf.constant(equivalencias)

# Asigna la etiqueta correcta a cada imagen.
def cambiar_etiquetas(imagenes, etiquetas):
    etiquetas = tf.gather(equivalencias, etiquetas)
    etiquetas = tf.one_hot(etiquetas, num_classes)
    return imagenes, etiquetas

train_ds = train_ds.map(cambiar_etiquetas)
val_ds = val_ds.map(cambiar_etiquetas)

# Revisa la cantidad de imagenes por persona.
for nombre, conjunto in [("Entrenamiento", train_ds),
                    ("Validacion", val_ds)]:

    cantidades_revision = np.zeros(num_classes)

    for imagenes, etiquetas in conjunto:
        cantidades_revision += np.sum(etiquetas.numpy(), axis=0)

    print(nombre)

    for i in range(num_classes):
        print(class_names[i], int(cantidades_revision[i]))

print("Personas detectadas:", class_names)

# Guarda los nombres de las personas.
with open(ruta / 'nombres_cnn.json', 'w', encoding='utf-8') as archivo:
    json.dump(class_names, archivo)

# Cuenta las imagenes de entrenamiento por persona.
cantidades = np.zeros(num_classes)

for imagenes, etiquetas in train_ds:
    cantidades += np.sum(etiquetas.numpy(), axis=0)

# Compensa las diferencias en las cantidades de imagenes.
total = np.sum(cantidades)

pesos = {
    i: float(total / (num_classes * cantidades[i]))
    for i in range(num_classes)
}

# Aumento de datos.
data_augmentation = models.Sequential([
    layers.RandomFlip("horizontal"),
    layers.RandomRotation(0.03),
    layers.RandomZoom(0.05)
])

# Arquitectura de la CNN.
model = models.Sequential([
    layers.Input(shape=(IMG_HEIGHT, IMG_WIDTH, 1)),

    # Normaliza los pixeles de 0 a 1.
    layers.Rescaling(1./255),

    # Aumento de datos.
    data_augmentation,

    # Primera capa convolucional.
    layers.Conv2D(16, 3, padding='same', activation='relu'),
    layers.MaxPooling2D(),

    # Segunda capa convolucional.
    layers.Conv2D(32, 3, padding='same', activation='relu'),
    layers.MaxPooling2D(),

    # Tercera capa convolucional.
    layers.Conv2D(64, 3, padding='same', activation='relu'),
    layers.MaxPooling2D(),

    # Procesa las caracteristicas de los rostros.
    layers.Flatten(),
    layers.Dense(128, activation='relu'),

    # Ayuda a evitar el sobreajuste.
    layers.Dropout(0.3),

    # Clasifica los rostros.
    layers.Dense(num_classes, activation='softmax')
])

# Configura el modelo.
model.compile(
    optimizer=tf.keras.optimizers.Adam(
        learning_rate=0.0005
    ),
    loss='categorical_crossentropy',
    metrics=['accuracy']
)

model.summary()

# Detiene el entrenamiento si deja de mejorar.
early_stopping = tf.keras.callbacks.EarlyStopping(
    monitor='val_loss',
    patience=10,
    restore_best_weights=True
)

# Guarda el mejor modelo.
checkpoint = tf.keras.callbacks.ModelCheckpoint(
    filepath=str(ruta / 'modelo_cnn_rostros.keras'),
    monitor='val_loss',
    save_best_only=True,
    mode='min'
)

# Entrena la red neuronal.
print("Entrenando la red neuronal.")

history = model.fit(
    train_ds,
    validation_data=val_ds,
    epochs=100,
    class_weight=pesos,
    callbacks=[early_stopping, checkpoint]
)

# Evalua el modelo.
perdida, precision = model.evaluate(val_ds)

print("Entrenamiento finalizado.")
print(f"Precision de validacion: {precision * 100:.2f}%")

# Grafica de precision.
plt.figure(figsize=(12, 5))

plt.subplot(1, 2, 1)
plt.plot(history.history['accuracy'], label='Entrenamiento')
plt.plot(history.history['val_accuracy'], label='Validacion')
plt.title('Precision del modelo')
plt.xlabel('Epocas')
plt.ylabel('Accuracy')
plt.legend()
plt.grid()

# Grafica de perdida.
plt.subplot(1, 2, 2)
plt.plot(history.history['loss'], label='Entrenamiento')
plt.plot(history.history['val_loss'], label='Validacion')
plt.title('Perdida del modelo')
plt.xlabel('Epocas')
plt.ylabel('Loss')
plt.legend()
plt.grid()

# Guarda y muestra las graficas.
plt.tight_layout()
plt.savefig(ruta / 'graficas_cnn.png')
plt.close()