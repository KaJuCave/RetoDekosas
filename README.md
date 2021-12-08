# Automatización busqueda de productos en la pagina Web Dekosas
En este proyecto se desarrollo la automatización en la búsqueda de cinco (5) productos para la página [Dekosas.com](https://dekosas.com/co/), utilizando las herramientas de pruebas Selenium y Cucumber.

## Estructura del proyecto
_En esta sección encontrara los pasos básicos para el desarrollo del proyecto_

* Explorar la pagina [Dekosas.com](https://dekosas.com/co/) y seleccionar los productos utilizados en la automatización. Se crea un archivo en Excel con el nombre y precio de los productos

![productosExcel](https://github.com/KaJuCave/imagenesDekosas/blob/master/productosExcel.PNG)

* Se crea un proyecto **Gradle** en el entorno de desarrollo.

* Para este proyecto se crearán paquetes y directorios para la automatización. Comencemos creando cuatro paquetes en nuestro directorio **main/java** para los drivers del navegador, leer el archivo de Excel, los elementos que se utilizaran de la página y los pasos que se automatizaran en la página Dekosas.

![src](https://github.com/KaJuCave/imagenesDekosas/blob/master/src.PNG)

Dentro del fichero **test/java** se creara los paquetes para las clase de ejecución y definición de los pasos dentro de la pagina Dekosas

![test](https://github.com/KaJuCave/imagenesDekosas/blob/master/test.PNG)

Por último en el directorio **resources** se creara la clase con la extensión **.feature**, la cual contiene la descripción de prueba que se va a ejecutar.

![resourse](https://github.com/KaJuCave/imagenesDekosas/blob/master/resourse.PNG)

_Nota: En las siguientes secciones se explicarán con más detalle la codificación de cada una de sus clases_ 

* Adicionalmente se debe descargar el [chomedriver]( https://chromedriver.chromium.org/downloads) de acuerdo a la versión del navegador, este le permitira que implementa el protocolo de WebDriver para Chromium. Por último, se agrega este driver y el archivo de Excel (con el nombre de los productos) a los archivos del proyecto.

![driverExcel](https://github.com/KaJuCave/imagenesDekosas/blob/master/driversExcel.PNG)

## Codificación del proyecto



## Ejecución

Después de realizar la codificación que se explicó anteriormente se  _ejecutar_ el proyecto en desde la clase **DekosasBuscadorRunner.java**, donde se definió los siguientes parámetros:

* @RunWith :  Es el runner de cucumber con serenity ejecutará todas las funciones que se encuentran en la ruta de clase en el mismo paquete que esta clase.

*  @CucumberOptions :  Es propio de cucumber. Tiene varias opciones de configuración, para este proyecto utilizamos:

	* features: Se coloca la dirección con la ubicar del archivo de características en la carpeta del proyecto.
	* glue: Es muy parecido a la opción anterior, pero la diferencia es que ayuda a Cucumber a localizar el archivo con la **definición de pasos**. Para este proyecto es la clase **stepsDefinitions**.
	* snippets: Es el formato de los fragmentos del código que genera Cucumber, para este caso se elige le tipo CAMELCASE.

```
@RunWith(CucumberWithSerenity.class)
@CucumberOptions(
        features = "src\\test\\resources\\features\\DekosasBuscador.feature",
        glue = "stepsDefinitions",
        snippets = SnippetType.CAMELCASE
)
public class DekosasBuscadorRunner {
}

```
## Construido con 🛠️

_En este proyecto se utilizaron las siguientes herramientas_

* [IntelliJ IDEA ](https://www.jetbrains.com/es-es/idea/) - El entorno de desarrollo usado
* [Gradle](https://gradle.org/) - Sistema de automatización  
* [Cucumber](https://cucumber.io/) - Software que aplica el desarrollo impulsado por el comportamiento
* [Selenium](https://www.selenium.dev/) - Entorno de pruebas de software para aplicaciones basadas en la web

# Demo
Para ver la demostracion de la ejecución del proyecto puede consultar en el video [automatizacion Dekosas](https://youtu.be/zirpW6UrqR8)

---
Elaborado por: [Juliana Cano Vega](https://github.com/KaJuCave)💻👩🏻
