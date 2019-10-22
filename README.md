# Jesus Saí AR App

## Agregar más imágenes

### Generar Base de Datos

1. Obtener herramienta para generar base de datos de arcore en: https://developers.google.com/ar/develop/java/augmented-images/arcoreimg
2. Siguiendo los comandos, evaluar la imagen (**Nota: ** Considerar la extención `.png`, `.jpg` o `.jpeg`):
	```shell
	$ arcoreimg.exe eval-img --input_image_path=dog.png
	```
3. Si obtiene una evaluzación acebtable, guardar en la carpeta con todas las imágenes seleccionadas, y ejecutar el comando:
	```shell
	$ arcoreimg.exe build-db --input_images_directory=/path/to/images --output_db_path=./ar.imgdb
	```
### Agregar Base de Datos a la aplicación

El archivo `ar.imgdb` es la base de datos que el framework de ARCore va a leer.

1. Dentro del directorio del proyecto de Android, agregar la nueva base de datos `ar.imgdb` en la ruta `/app/src/main/assets`.

### Agregar imagen a la aplicación

La nueva imagen debe ser agregada en los recursos de la aplicación para que pueda aparecer en la galería.

1. Agregar la imagen en la ruta de recursos `/app/main/res/drawable`
2. Agregar la imagen a la vista de la galería en el archivo `/app/main/java/com.villap.jesussai/galeria/GaleriaActivity.kt`
	1. A partir de la línea 20 se hace la referencia a los recursos, agregándolos al `arrayListImage`. Agregar la nueva imagen de la forma siguiente **sin extension**
	```kotlin
	arrayListImage.add(R.drawable.nombre_de_imagen)
	```
	2. Agregar al `arrayOf`, en la línea siguiete, considerando el nombre **con extensión**. Los nombres van entre comillas y separados por comas
	```kotlin
	val name = arrayOf("pez.jpeg", "paloma.jpg", "catrinas.jpg", "nombre_de_imagen.jpg")
		
	```
### Agregar un video

El video al que será repreoducido con la imagen detectada

1. Agregar el video en la carpeta `/app/main/res/raw`
2. Hacer la referencia al video en el archivo `/app/main/java/com.villap.jesussai/ar/ArResources.kt`
	1. A partir de la línea 30 se agregan los diferentes recursos, agregar el nuevo video de la misma forma que los anteriores. Dentro de `when (selection) {`. El nombre debe incluir **extensión**
	```kotlin
	"nombre_de_imagen.jpg" -> {
       videoPlayer = MediaPlayer.create(context, R.raw.nombre_de_imagen)
   }
	```

### Agregar tracking de imagen

1. Agregar en el archivo `/app/main/java/com.villap.jesussai/app/CustomArFragment.kt` el nuevo condicional en la línea 72 (`when (image.name) {`). De la misma forma que el resto de las imágenes. El nombre debe incluir **extensión.** Por ejemplo:
```kotlin
"nombre_de_imagen.jpeg" -> {
    val node = CustomAnchorNode(1.3F, 2F).init(image)
    trackableMap[image.name] = node
    arSceneView.scene.addChild(node)
}
```

4. El argumento `1.3F` se deduce a partir de experimentar con diferentes anchos, pues el tamaño de la imagen en relación al video puede cambiar