# Automatización busqueda de productos en la pagina Web Dekosas
En este proyecto se desarrollo la automatización en la búsqueda de cinco (5) productos para la página [Dekosas.com](https://dekosas.com/co/), utilizando las herramientas de pruebas Selenium y Cucumber.

## Estructura del proyecto
_En esta sección encontrara los pasos básicos para el desarrollo del proyecto_

* Explorar la pagina [Dekosas.com](https://dekosas.com/co/) y seleccionar los productos utilizados en la automatización. Se crea un archivo en Excel  con el nombre de ``` retoDekosas.xlsx``` que contiene el nombre y precio de los productos.

![productosExcel](https://github.com/KaJuCave/imagenesDekosas/blob/master/productosExcel.PNG)

* Se crea un proyecto **Gradle** en el entorno de desarrollo.

* Para este proyecto se crearán paquetes y directorios para la automatización. Comencemos creando cuatro paquetes en nuestro directorio **main/java** para los drivers del navegador, leer el archivo de Excel, los elementos que se utilizaran de la página y los pasos que se automatizaran en la página Dekosas.

![src](https://github.com/KaJuCave/imagenesDekosas/blob/master/src.PNG)

Dentro del fichero **test/java** se creara los paquetes para las clase de ejecución y definición de los pasos dentro de la pagina Dekosas

![test](https://github.com/KaJuCave/imagenesDekosas/blob/master/test.PNG)

Por último en el directorio **resources** se creara la clase con la extensión **.feature**, la cual contiene la descripción de prueba que se va a ejecutar.

![resourse](https://github.com/KaJuCave/imagenesDekosas/blob/master/resourse.PNG)

_Nota: En las siguientes secciones se explicarán con más detalle la codificación de cada una de sus clases_ 

* Adicionalmente se debe descargar el [chromedriver]( https://chromedriver.chromium.org/downloads) de acuerdo a la versión del navegador, este le permitira que implementa el protocolo de WebDriver para Chromium. Por último, se agrega este driver y el archivo de Excel (con el nombre de los productos) a los archivos del proyecto.

![driverExcel](https://github.com/KaJuCave/imagenesDekosas/blob/master/driversExcel.PNG)

## Codificación del proyecto

En esta sesión se explicará detalladamente la codificación que se implementó para automatización de los productos de la página Dekosas.

### Agregar dependencias

Para que el correcto funcionamiento de algunas utilidades en el proyecto se debe agregar las dependencias en el archivo ```build.gradle``` que se muestran a continuación 

```gradle
apply plugin: 'java-library'
apply plugin: 'net.serenity-bdd.aggregator'
apply plugin: 'eclipse'

repositories {
    mavenLocal()
    mavenCentral()
    jcenter()

}

buildscript {
    repositories {
        mavenLocal()
        mavenCentral()
        jcenter()

    }
    dependencies {
        classpath("net.serenity-bdd:serenity-gradle-plugin:2.0.80")
    }
}

dependencies {
    implementation 'net.serenity-bdd:serenity-junit:2.0.80'
    implementation 'net.serenity-bdd:serenity-cucumber:1.9.45'
    implementation 'net.serenity-bdd:serenity-core:2.0.80'
    implementation 'org.slf4j:slf4j-simple:1.7.7'
    implementation group: 'org.apache.poi', name: 'poi', version: '3.17'
    implementation group: 'org.apache.poi', name: 'poi-ooxml', version: '3.17'
}

test {
    ignoreFailures = true
}
gradle.startParameter.continueOnFailure = true

```
### Archivo Excel

Para este proyecto se necesita leer los nombres de cada uno de los productos elegidos en la página por medio de un archivo Excel, por tal razón se implementó la clase ```Excel.java``` y el siguiente método ```leerDatosDeHojaDeExcel ``` el cual recibe como parámetro dos datos: ```String rutaDeExcel``` se especifica la ruta del archivo Excel con extension .xlsx (el cual se agregó en pasos anteriores) y  el segundo ```String hojaDeExcel``` se refiere al nombre de la hoja del archivo Excel donde se guardaron los nombres de los productos.
Al finalizar los procesos de este método nos retorna una lista con los datos encontrados y solicitados del archivo Excel. 


```java
public class Excel {

    public static <rutaDeExcel, hojaExcel> ArrayList<Map<String, String>> leerDatosDeHojaDeExcel( String rutaDeExcel, String hojaDeExcel) throws IOException {
        ArrayList<Map<String, String>> arrayListDatoPlanTrabajo = new ArrayList<Map<String, String>>();
        Map<String, String> informacionProyecto = new HashMap<String, String>();
        File file = new File(rutaDeExcel);
        FileInputStream inputStream = new FileInputStream(file);
        XSSFWorkbook newWorkbook = new XSSFWorkbook(inputStream);
        XSSFSheet newSheet = newWorkbook.getSheet(hojaDeExcel);
        Iterator<Row> rowIterator = newSheet.iterator();
        Row titulos = rowIterator.next();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Iterator<Cell> cellIterator = row.cellIterator();
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                cell.getColumnIndex();
                switch (cell.getCellTypeEnum()) {
                    case STRING:
                        informacionProyecto.put(titulos.getCell(cell.getColumnIndex()).toString(), cell.getStringCellValue());
                        break;
                    case NUMERIC:
                        informacionProyecto.put(titulos.getCell(cell.getColumnIndex()).toString(), String.valueOf((long) cell.getNumericCellValue()));
                        break;
                    case BLANK:
                        informacionProyecto.put(titulos.getCell(cell.getColumnIndex()).toString(), "");
                        break;
                    default:
                }
            }
            arrayListDatoPlanTrabajo.add(informacionProyecto);
            informacionProyecto = new HashMap<String, String>();
        }
        return arrayListDatoPlanTrabajo;
    }
}
```
### Características ChromeDriver

Para este proyecto se utilizará el navegador **Google Chrome**, por esto creamos unos drivers que nos permitirán utilizar este navegador. Para realizar se creó la clase ```GoogleChromeDriver.java``` y se instanció un objeto de la interfaz **WebDriver** 

``` java
public static WebDriver driver;
```

Seguidamente se crea un método donde se especifican las opciones que tendrá el navegador como al iniciar la automatización la ventana este maximizada y por ultimo se implementa el método ```driver.get(url) ```  el cual nos permite navegar a la URL pasada como argumento y espera hasta que se cargue la página.


```java
public class GoogleChromeDriver {

    public static WebDriver driver;

    public static void chomeWebDriver(String url) {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--ignore-certificate-errors");
        options.addArguments("--disable-infobars");
        driver = new ChromeDriver(options);
        driver.get(url);
    }
}

```
### Elementos y pasos en la página Dekosas

Como la página que utilizamos para la automatización es [Dekosas.com](https://dekosas.com/co/) creamos una clase ```DekosasPage.java ``` en la cual creamos como atributos de la clase **By** los botones y los textos de los elementos a buscar en la página.

```java
By txtBuscador 
By btnBuscador 
By btnElementoBusqueda
By txtElementoBusqueda
```
De estos atributos inicializamos a ```txtBuscador ``` ``` btnBuscador``` con los respectivos **Xpath** que nos permitirán encontrar la barra de búsqueda y escribir el producto en la página.

```java
By txtBuscador = By.xpath("//input[@id='search' and @name='q']");
By btnBuscador = By.xpath("//button[@class='amsearch-loupe' and @title='Buscar']");
```
Por último, se crean los ``` Getter ``` de todos los atributos, pero en los ``` Setter ``` solo los de ``` btnElementoBusqueda ``` y ``` txtElementoBusqueda``` se inicalizan como se muestra a continuación 
```java
public void setBtnElementoBusqueda(String producto) {
        this.btnElementoBusqueda = By.xpath("//a[contains(text(),'"+producto+"')]");
    }

    public void setTxtElementoBusqueda(String producto) {
        this.txtElementoBusqueda = By.xpath("//span[contains(text(),'"+producto+"')]");
    }

```
**DekosasSteps**

Dentro de la clase ``` DekosasSteps.java``` especificaremos los pasos que la página realizara en la automatización. Para comenzar debemos instanciar objetos de las clases ``` DekosasPage```, ``` Excel``` y ``` ArrayList``` que contiene los datos del Excel. 

```java
DekosasPage dekosasPage = new DekosasPage();
Excel excelarchivo= new Excel();
ArrayList<Map<String, String>> datosExcel;

```
En los siguientes métodos se especifican los pasos que realizara el navegador en la automatización

```java
public void abrirPagina(){
        GoogleChromeDriver.chomeWebDriver("https://dekosas.com/co/");
    }

    public void leerProductosDekosasExcel() {
        try {
            datosExcel = Excel.leerDatosDeHojaDeExcel("retoDekosas.xlsx","Productos");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void validarElementoEnPantalla(){
        for (int i = 0; i <= datosExcel.size()-1; i++) {
            GoogleChromeDriver.driver.findElement(dekosasPage.getTxtBuscador()).sendKeys(datosExcel.get(i).get("Nombre Producto"));
            GoogleChromeDriver.driver.findElement(dekosasPage.getTxtBuscador()).sendKeys(Keys.ENTER);
            dekosasPage.setBtnElementoBusqueda(datosExcel.get(i).get("Nombre Producto"));
            GoogleChromeDriver.driver.findElement(dekosasPage.getBtnElementoBusqueda()).click();
            dekosasPage.setTxtElementoBusqueda(datosExcel.get(i).get("Nombre Producto"));
            Assert.assertEquals(datosExcel.get(i).get("Nombre Producto"), GoogleChromeDriver.driver.findElement(dekosasPage.getTxtElementoBusqueda()).getText());
        }
    }

    public void cerrarNavegador(){
        GoogleChromeDriver.driver.quit();
    }
```
### Caracteriscticas de la automatización (feature)

Se crea un archivo con nombre ``` DekosasBuscador``` y con extensión ```.feature``` donde se describen las siguientes características de la prueba. 

``` Feature: ```  nombre de la funcionalidad de la prueba. Con el caso de usuario que se va a probar 
``` Scenario: ``` se realiza para especificar la funcionalidad la búsqueda de productos 
``` Given: ```  se describe el contexto, las precondiciones.
``` When: ```  se especifican las acciones que se van a ejecutar.
``` Then: ```  y acá se especifica el resultado esperado, las validaciones a realizar.

```feature
Feature: HU-001 Buscador Dekosas
  Yo como usuario en la pagina web Dekosas
  Quiero buscar los productos en la plataforma
  Para ver las caracteristicas de los producto

  Scenario: Buscar productos
    Given me encuentro en la pagina web Dekosas
    When busque los productos
    Then puedo ver los productos en pantalla

```
### Definición de los pasos (Steps Definitions)

Después de ejecutar por primera vez se crean los métodos ``` Given ``` ,``` When ```, ``` Then```  del proyecto. Estos métodos los implementamos en la clase ``` DekosaStepsDefinitions.java``` seguidamente se instancia un objeto de la clase ``` DekosasSteps ``` 

``` java
DekosasSteps dekosasSteps = new DekosasSteps();
```

Lo que faltaría en los métodos anteriores llamar los métodos que contiene los pasos para la automatización como abrir el navegador, leer los productos que están guardados en el archivo de Excel, validar los productos encontrados en pantalla y por último cerrar el navegador cuando la automatización finalice de forma exitosa.

```java

    @Given("^me encuentro en la pagina web Dekosas$")
    public void meEncuentroEnLaPaginaWebDekosas() {
        dekosasSteps.abrirPagina();

    }
    @When("^busque los productos$")
    public void busqueLosProductos() {
        dekosasSteps.leerProductosDekosasExcel();
    }

    @Then("^puedo ver los productos en pantalla$")
    public void puedoVerLosProductosEnPantalla() {
        dekosasSteps.validarElementoEnPantalla();
        dekosasSteps.cerrarNavegador();
    }
```

## Ejecución

Después de realizar la codificación que se explicó anteriormente se  _ejecutar_ el proyecto en desde la clase **DekosasBuscadorRunner.java**, donde se definió los siguientes parámetros:

* @RunWith :  Es el runner de cucumber con serenity ejecutará todas las funciones que se encuentran en la ruta de clase en el mismo paquete que esta clase.

*  @CucumberOptions :  Es propio de cucumber. Tiene varias opciones de configuración, para este proyecto utilizamos:

	* features: Se coloca la dirección con la ubicar del archivo de características en la carpeta del proyecto.
	* glue: Es muy parecido a la opción anterior, pero la diferencia es que ayuda a Cucumber a localizar el archivo con la **definición de pasos**. Para este proyecto es la clase **stepsDefinitions**.
	* snippets: Es el formato de los fragmentos del código que genera Cucumber, para este caso se elige le tipo CAMELCASE.

``` java
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
