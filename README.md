# 📌 Drivecat
Drivecat es una aplicación móvil que permite a los estudiantes universitarios ver la información de los horarios de las rutas y reservas para el transporte. Además, permite 
a lo transportistas validar los pagos mediante un código QR de las tarjetas de los estudiantes de manera eficiente y segura. 

<br/>

<div align="center"> 
<img src="https://github.com/user-attachments/assets/d4a5260d-9004-4bb3-ab74-55deb820f920"  width="180"  height="180"/>
</div>

<br/>

## 📜 Descripción
La app está desarrollada en Android Studio, utilizando Firebase para la gestión de datos y autenticación, y la biblioteca ZXing para el escaneo optimizado de códigos QR, garantizando un proceso de validación de pagos ágil y seguro.

## ✅ Características
- Cada usuario debe iniciar sesión e identificar si son “usuarios” o “transportistas”.
-	Los transportistas sólo podrán acceder a su cuenta cuando el administrador lo haya autorizado.
-	Los usuarios pueden consultar la cantidad de viajes pagados, el tiempo de espera de salida del transporte y los horarios de las rutas del transporte.
-	Los usuarios pueden reservar viajes de cualquier ruta.
-	Los usuarios podrán acceder a la información de los transportistas en el historial de movimientos de pago.
-	Los usuarios y transportistas pueden acceder a la opción de soporte.
-	Los transportistas son los únicos que pueden validar el pago de la tarjeta con QR.
-	Los transportistas podrán hacer un seguimiento de la cantidad de usuarios que viajan.
-	Los transportistas podrán ver un historial de todos los pagos por QR que se han efectuado.
-	Se almacenarán los datos de la información del usuario correctamente.
-	Los usuarios y transportistas no podrán disponer de todas las vistas.
-	Los usuarios y transportistas dispondrán de un perfil y un panel de opciones.
-	El registro de usuarios y transportistas debe aparecer en forma de lista en la que se podrá navegar fácilmente.
-	Para la interfaz se tiene que seguir un estilo minimalista y atractivo.
-	El sistema debe tener una respuesta rápida y optimizada.
-	La información de las transacciones se debe de mostrar en el panel de “Actividades” en una lista vertical.
-	Los formularios e ingreso de datos deben ser de una manera sencilla.
-	Los usuarios pueden ver el tiempo estimado de salida de cada ruta.

## 💻 Tecnologías Utilizadas 

![Android](https://img.shields.io/badge/Android-34A853.svg?style=for-the-badge&logo=Android&logoColor=white) &nbsp;
![AndroidStudio](https://img.shields.io/badge/Android%20Studio-3DDC84.svg?style=for-the-badge&logo=Android-Studio&logoColor=white) &nbsp;
![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white) &nbsp;
![Gradle](https://img.shields.io/badge/Gradle-02303A.svg?style=for-the-badge&logo=Gradle&logoColor=white) &nbsp;
![XML](https://img.shields.io/badge/xml-968b01?style=for-the-badge&logo=openjdk&logoColor=white) &nbsp;
![Firebase](https://img.shields.io/badge/Firebase-DD2C00.svg?style=for-the-badge&logo=Firebase&logoColor=white) &nbsp;
![Zxing](https://img.shields.io/badge/Zxing-ECD53F.svg?style=for-the-badge&logo=&logoColor=black) &nbsp;

## ℹ️ Instalación

1. Clona este repositorio:
```bash
   git clone https://github.com/tu-usuario/drivecat.git
```
2. Abre el proyecto en Android Studio.
3. Configura Firebase:
- Añade el archivo google-services.json en la carpeta app/.
  - Para añadir ese archivo, debes de crear un proyecto en Firebase.
- Configura la autenticación en Firebase y la base de datos Firestore.
4. Ejecuta el proyecto en un emulador o dispositivo físico.
5. Para que se habilite el "Modo Taquilla" es necesario que se registre al menos un transportista, y hay que registrarlo manualmente en Firebase.

## 📲 Arquitectura
- **Frontend:** XML para la interfaz de usuario, con una estructura minimalista y colores claros.
- **Backend:** Java para la lógica y manejo de la aplicación.
- **Base de datos:** Firebase Firestore para almacenamiento de datos y gestión de usuarios.

## 🔎 Seguridad
- Todos los datos se almacenan en Firebase Firestore de manera segura y están protegidos con reglas de acceso basadas en la autenticación de usuarios.
- El escaneo de QR es manejado por la biblioteca ZXing, asegurando un procesamiento eficiente y seguro de los códigos.
- Se implementaron validaciones robustas para garantizar que los códigos QR no puedan ser manipulados o falsificados, evitando pagos no autorizados.
- El proceso de pagos cuenta con múltiples verificaciones para asegurar que solo los códigos QR generados y asociados a un usuario válido sean aceptados, protegiendo contra intentos de fraude.

## 💳 Diseño de Tarjeta con Código QR
El diseño de una tarjeta con código QR implica la integración visual y funcional de un código QR en el diseño gráfico de una tarjeta física. Con este código QR se pueden realizar los pagos a los transportistas. Su diseño considera aspectos estéticos como funcionales para asegurar una experiencia eficaz para el usuario final.

<br/>
<div align="center"> 
<img src="https://github.com/user-attachments/assets/e6570ab7-26ee-4287-a31b-aa86368fbc60"  width="310"  height="190"/>
</div>

<br/>

## 🖼️ Imagenes de la App

<br/>

<img src="https://github.com/user-attachments/assets/015e606f-adf5-4380-b347-8d392f135206"  width="190"  height="410"/> &nbsp; &nbsp; &nbsp;
<img src="https://github.com/user-attachments/assets/b47f9d3d-a611-4375-bf57-03e4344b491f"  width="190"  height="410"/> &nbsp; &nbsp; &nbsp;
<img src="https://github.com/user-attachments/assets/c61ffcfe-4ce5-469a-895b-7af0ba9005d7"  width="190"  height="410"/> &nbsp; &nbsp; &nbsp;
<img src="https://github.com/user-attachments/assets/955b5ce1-c546-4fd8-a1fb-738ae5dc8786"  width="190"  height="410"/> &nbsp; &nbsp; &nbsp;
<img src="https://github.com/user-attachments/assets/4624b222-eeed-4890-bc5e-78c4f513501a"  width="190"  height="410"/> &nbsp; &nbsp; &nbsp;
<img src="https://github.com/user-attachments/assets/cbf78382-3bde-4754-99b1-92e63acf801f"  width="190"  height="410"/> &nbsp; &nbsp; &nbsp;
<img src="https://github.com/user-attachments/assets/65bdf4c3-60bb-44ec-8fc2-4a5ca9a39147"  width="190"  height="410"/> &nbsp; &nbsp; &nbsp;
<img src="https://github.com/user-attachments/assets/8951a188-63b4-43ac-a358-8436538f1298"  width="190"  height="410"/> &nbsp; &nbsp; &nbsp;

<br/>

## 📝 Licencia

Este proyecto está licenciado bajo la [Licencia MIT](LICENSE).

## 🖋️ Autor

- [Carlos Cantero](https://github.com/carloscantero11) ☕ 
