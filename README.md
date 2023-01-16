# WorkHub

- Con esta aplicacion puedes reservar espacios en edificios habilitados para ellos, en lo que podras trabajar tranquilamente sin ninguna molestia.
- Cada edificio tiene sus espacios propios, tambien puedes ver que servicios ofrece cada espacio y realizar una reserva de ese espacio.
- Puedes ver todas tus reservas y sus detalles en la aplicacion, asi como cancerlarlas o editarlas.
- :warning: La aplicacion solo funciona de forma local :warning:

### Como compilar la aplicacion
* Necesitas tener instalado y configurado el SDK de Android asi como Android Studio
* Importa la aplicacion desde GitHub clonandola o descargandote el fichero con el codigo fuente
* Configura los TOKENS de MapBox para poder acceder a los mapas.
    * Edita el fichero gradle.properties con TOKEN privado.
    * Añade un fichero xml en la carpeta values con esta estructura `<?xml version="1.0" encoding="utf-8"?>  
      <resources><string name="mapbox_access_token" translatable="false">PUBLICTOKEN</string></resources>` y añade tu TOKEN public de MapBox

### Requisitos obligatorios
- [x] La aplicación contará con, al menos, 7 Activities, utilizando controles ImageView, TextView, Button, CheckBox y RecyclerView para recoger y presentar información en pantalla y se hará, como mínimo, en dos idiomas
- [x] Se deberán usar Bases de datos para almacenar información. El usuario deberá ser capaz de registrar, modificar, eliminar y visualizar en un RecyclerView esa información con un adaptador personalizado (Un CRUD completo). El modelo de datos de la aplicación estará compuesto, al menos, de 3 clases.
- [x] La aplicación contará con un menú de opciones o ActionBar desde donde se podrá acceder a las acciones que el usuario pueda realizar en cada Activity.
- [x] Añadir alguna función que interactúe con otras aplicaciones del dispositivo (cámara, contactos, . . .)
- [x] Se mostrará información útil para la aplicación en un mapa (GoogleMaps o MapBox) de forma que el usuario pueda interactuar con el mismo para llevar a cabo alguna acción de utilidad para la aplicación

### Requisitos opcionales
- [x] Utilizar diálogos siempre que sea necesario (al modificar o eliminar información, por ejemplo)
- [x] Utiliza la herramienta Git (y GitHub) durante todo el desarrollo de la aplicación. Utiliza el gestor de Issues para los problemas/fallos que vayan surgiendo
- [x] Utilizar el GPS del dispositivo para realizar alguna función sobre el mapa
- [x] Añadir un menú de preferencias con al menos 3 opciones que modifiquen el comportamiento de la aplicación. Este menú estará siempre disponible en el ActionBar
- [ ] Diseñar algunos layouts para otras posiciones de la pantalla (portrait/landscape)
- [x] Utilizar imágenes como atributos de algún objeto (y almacenarlo en la base de datos)
- [x] Emplear Fragments en el diseño de alguna de las Activities de la aplicación
- [x] Utilizar Material Design para personalizar el diseño de la aplicación
